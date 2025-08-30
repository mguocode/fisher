package fisher.input;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fisher.input.ActionExecutorIntf;
import fisher.math.ClippedGaussian;

public class Clicker implements ActionExecutorIntf {
    private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final Set<Future<?>> pendingTasks = ConcurrentHashMap.newKeySet();

    private ClippedGaussian rightClickHoldTimeMs = new ClippedGaussian(70, 11.2, 51.231, 112.31);
    private ClippedGaussian reelInitialSleepTimeMs = new ClippedGaussian(200, 30, 79.87, 3000);
    private ClippedGaussian reelIntercastSleepTimeMs = new ClippedGaussian(300, 50, 212.31, 2000);

    @Override
    public void onFishingStop() {
        pendingTasks.forEach(task -> task.cancel(true));
        pendingTasks.clear();
    }

    public void scheduleTask(Runnable task, long delayMs) {
        Future<?> future = executor.schedule(() -> {
            try {
                task.run();
            } finally {
                pendingTasks.remove(Thread.currentThread());
            }
        }, delayMs, TimeUnit.MILLISECONDS);
        pendingTasks.add(future);

    }

    private void scheduleDelay(long delayMs) {
        scheduleTask(() -> {
        }, delayMs);
    }

    private void scheduleRightClick() {
        scheduleTask(
                () -> {
                    toggleMouse(true);
                },
                0

        );
        scheduleDelay((long) rightClickHoldTimeMs.get());
        scheduleTask(
                () -> {
                    toggleMouse(false);
                },
                0

        );
    }

    public void scheduleReelAndRecast() {
        scheduleDelay((long) reelInitialSleepTimeMs.get());
        scheduleRightClick();
        scheduleDelay((long) reelIntercastSleepTimeMs.get());
        scheduleRightClick();
    }

}
