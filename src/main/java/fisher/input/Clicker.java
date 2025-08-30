package fisher.input;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fisher.math.ClippedGaussian;

public class Clicker implements ActionExecutorIntf {
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    // concurrent set of all scheduled-but-not-finished tasks
    private final Set<ScheduledFuture<?>> pendingTasks = ConcurrentHashMap.newKeySet();

    private final ClippedGaussian rightClickHoldTimeMs = new ClippedGaussian(70, 11.2, 51.231, 112.31);
    private final ClippedGaussian reelInitialSleepTimeMs = new ClippedGaussian(200, 30, 79.87, 3000);
    private final ClippedGaussian reelIntercastSleepTimeMs = new ClippedGaussian(300, 50, 212.31, 2000);

    @Override
    public void onFishingStop() {
        // cancel and clear all scheduled tasks
        pendingTasks.forEach(task -> task.cancel(true));
        pendingTasks.clear();
    }

    public void scheduleTask(Runnable task, long delayMs) {
        // create a wrapper that will remove itself from pendingTasks when done
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

    private void scheduleDelay(long delayMs) {
        scheduleTask(() -> {
        }, delayMs);
    }

    private void scheduleRightClick() {
        scheduleTask(() -> MouseController.toggleMouse(true), 0);
        scheduleDelay((long) rightClickHoldTimeMs.get());
        scheduleTask(() -> MouseController.toggleMouse(false), 0);
    }

    public void scheduleReelAndRecast() {
        scheduleDelay((long) reelInitialSleepTimeMs.get());
        scheduleRightClick();
        scheduleDelay((long) reelIntercastSleepTimeMs.get());
        scheduleRightClick();
    }
}
