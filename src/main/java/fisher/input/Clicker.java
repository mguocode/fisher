package fisher.input;
// CR mguo: maybe check that pending tasks is empty upon scheduling

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import fisher.util.ClippedGaussian;

public class Clicker implements ActionExecutorIntf {
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final Set<ScheduledFuture<?>> pendingTasks = ConcurrentHashMap.newKeySet();
    private static final ClippedGaussian keyPressHoldTimeMs = new ClippedGaussian(70, 11.2, 51.231, 112.31);
    private static final ClippedGaussian predicatableReactionTimeMs = new ClippedGaussian(200, 30, 79.87, 3000);
    private static final ClippedGaussian reelIntercastSleepTimeMs = new ClippedGaussian(300, 50, 212.31, 2000);
    private static final ClippedGaussian apmTimeMs = new ClippedGaussian(130, 25, 50.31, 300);
    private static final ClippedGaussian swapDelayMs = new ClippedGaussian(550, 100, 321.31, 721.121);
    private static final Clicker INSTANCE = new Clicker();
    private static final int FISHING_ROD_SLOT = 2;
    private static final int ABILITY_SLOT = 3;
    private static final int OVERFLUX_SLOT = 4;

    private Clicker() {
    }

    public static Clicker getInstance() {
        return INSTANCE;
    }

    public static class TimedTask {
        public final long delayMs;
        public final Runnable task;

        public TimedTask(long delayMs, Runnable task) {
            this.delayMs = delayMs;
            this.task = task;
        }

        // Convenience factory methods
        public static TimedTask delay(long ms) {
            return new TimedTask(ms, () -> {
            });
        }

        public static TimedTask action(Runnable task) {
            return new TimedTask(0, task);
        }

        public static TimedTask delayedAction(long delayMs, Runnable task) {
            return new TimedTask(delayMs, task);
        }
    }

    @Override
    public void onStopFishing() {
        pendingTasks.forEach(task -> task.cancel(true));
        pendingTasks.clear();
    }

    private void scheduleTask(Runnable task, long delayMs) {
        class Wrapped implements Runnable {
            private ScheduledFuture<?> self;

            @Override
            public void run() {
                try {
                    task.run();
                } finally {
                    pendingTasks.remove(self);
                }
            }
        }
        Wrapped wrapped = new Wrapped();
        ScheduledFuture<?> future = executor.schedule(wrapped, delayMs, TimeUnit.MILLISECONDS);
        wrapped.self = future;
        pendingTasks.add(future);
    }

    private void scheduleTaskSequence(List<TimedTask> sequence) {
        long cumulativeDelay = 0;

        for (TimedTask timedTask : sequence) {
            cumulativeDelay += timedTask.delayMs;
            scheduleTask(timedTask.task, cumulativeDelay);
        }
    }

    // Convenience method for building right click sequences
    private List<TimedTask> buildRightClickSequence() {
        long holdTime = (long) keyPressHoldTimeMs.get();
        return List.of(
                TimedTask.action(() -> {
                    MouseController.toggleMouse(true);
                }),
                TimedTask.delayedAction(holdTime, () -> {
                    MouseController.toggleMouse(false);
                }));
    }

    private List<TimedTask> buildHotbarSequence(int number) {
        long holdTime = (long) keyPressHoldTimeMs.get();
        return List.of(
                TimedTask.action(() -> {
                    KeySimulator.pressNumberKey(number, true);
                }),
                TimedTask.delayedAction(holdTime, () -> {
                    KeySimulator.pressNumberKey(number, false);
                }));
    }

    public List<TimedTask> buildReelAndRecastSequence() {
        long intercastDelay = (long) reelIntercastSleepTimeMs.get();

        List<TimedTask> sequence = new ArrayList<>();

        sequence.addAll(buildRightClickSequence());
        sequence.add(TimedTask.delay(intercastDelay));
        sequence.addAll(buildRightClickSequence());
        return sequence;
    }

    public List<TimedTask> buildReelAbilityAndRecastSequence(int hotbarNumber, int numCasts) {

        List<TimedTask> sequence = new ArrayList<>();

        sequence.addAll(buildRightClickSequence());
        sequence.add(TimedTask.delay((long) apmTimeMs.get()));
        sequence.addAll(buildHotbarSequence(hotbarNumber));
        for (int i = 0; i < numCasts; i++) {
            sequence.add(TimedTask.delay((long) apmTimeMs.get()));
            sequence.addAll(buildRightClickSequence());
        }
        sequence.add(TimedTask.delay((long) apmTimeMs.get()));
        sequence.addAll(buildHotbarSequence(FISHING_ROD_SLOT));
        sequence.add(TimedTask.delay((long) swapDelayMs.get()));
        sequence.addAll(buildRightClickSequence());
        return sequence;
    }

    public List<TimedTask> addPredictableReactionTime(List<TimedTask> actions) {
        long predictableReactionTime = (long) predicatableReactionTimeMs.get();
        List<TimedTask> sequence = new ArrayList<>();
        sequence.add(TimedTask.delay(predictableReactionTime));
        sequence.addAll(actions);
        return sequence;
    }

    public void scheduleReelAndRecast() {
        List<TimedTask> sequence = buildReelAndRecastSequence();
        scheduleTaskSequence(addPredictableReactionTime(sequence));
    }

    public void scheduleReelAbilityAndRecast(int numCasts) {
        List<TimedTask> sequence = buildReelAbilityAndRecastSequence(ABILITY_SLOT, numCasts);
        scheduleTaskSequence(addPredictableReactionTime(sequence));
    }

    public void scheduleReelOverfluxAndRecast() {
        List<TimedTask> sequence = buildReelAbilityAndRecastSequence(OVERFLUX_SLOT, 1);
        scheduleTaskSequence(addPredictableReactionTime(sequence));
    }
}