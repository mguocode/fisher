package fisher.fishing;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Random;

import fisher.SoundPlayer;
import fisher.input.ActionExecutorManager;
import fisher.util.FileUtils;

public class FishingController {

    public void handleSound(SoundInstance sound) {
        // Not needed; we should theoretically dispose when getting to mouse click but
        // keeping just in case
        if (!isRunning.get()) {
            return;
        }

        if (passesCheck(sound)) {
            onDetect();
        }
    }

    private void onDetect() {
        long now = System.currentTimeMillis();
        double nowSeconds = now / 1000.0;
        double lastDetectionSeconds = lastDetectionTime / 1000.0;
        double timeSinceLastDetection = nowSeconds - lastDetectionSeconds;

        if (timeSinceLastDetection > MIN_TIME_BETWEEN_DETECTIONS) {
            lastDetectionTime = now;

            // clipped_gaussian(0.3, 0.03, 0.1898912, 3) - sleep before action
            double desiredSleepTime = clippedGaussian(0.2, 0.03, 0.07987, 3.0);
            long sleepMs = (long) (desiredSleepTime * 1000);

            FileUtils.writeStringToFile("[LuluTheFish] detected note block!!");
            FileUtils
                    .writeStringToFile(String.format("[LuluTheFish] time to wait before reel: %.3f", desiredSleepTime));

            // Schedule the fishing action after the delay
            executor.schedule(() -> {
                reelAndRecast();
            }, sleepMs, TimeUnit.MILLISECONDS);
        } else {
            FileUtils.writeStringToFile(
                    String.format("[LuluTheFish] Warning!! detected duplicate note block again in only %.3f",
                            timeSinceLastDetection));
        }
    }
}
