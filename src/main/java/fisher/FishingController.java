package mypackage;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Random;

public class FishingController {
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private long lastDetectionTime;
    private double fishingStartX, fishingStartZ;
    private String fishingStartBiome;

    public boolean isRunning() {
        return isRunning.get();
    }

    public void startFishing() {
        isRunning.set(true);
        SoundManager.play(SoundListener.startSound);
        lastDetectionTime = System.currentTimeMillis();
        MinecraftClient client = MinecraftClient.getInstance();
        fishingStartX = client.player.getX();
        fishingStartZ = client.player.getZ();
        fishingStartBiome = client.world.getBiome(client.player.getBlockPos()).getKey().get().getValue().toString();
        FileUtils.writeStringToFile("[LuluTheFish] Auto-fishing started!");
    }

    public void stopFishing() {
        isRunning.set(false);
        SoundManager.play(stopSound);
        FileUtils.writeStringToFile("[LuluTheFish] Auto-fishing stopped.");
    }

    private void sanityCheck() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!isRunning.get())
            return;
        // CR mguo: This function crashes when closing the game because onTick still
        // gets called but player has no location
        double distance = Math.sqrt(
                Math.pow(client.player.getX() - fishingStartX, 2) +
                        Math.pow(client.player.getZ() - fishingStartZ, 2));
        boolean tooLongSinceLastDetection = (System.currentTimeMillis() - lastDetectionTime > SANITY_CHECK_TIMEOUT);
        boolean xyzCoordsTooFar = distance > MAX_DISTANCE_FROM_FISHING_START;
        boolean biomeChanged = !fishingStartBiome
                .equals(client.world.getBiome(client.player.getBlockPos()).getKey().get()
                        .getValue().toString());

        if (tooLongSinceLastDetection || xyzCoordsTooFar || biomeChanged) {
            FileUtils.writeStringToFile("[LuluTheFish] SANITY CHECK FAILED!");
            FileUtils.writeStringToFile(String.format("[LuluTheFish] biomeChanged: %s, xyz: %s, toolong: %s",
                    biomeChanged, xyzCoordsTooFar, tooLongSinceLastDetection));
            SoundManager.play(sanitySound);
            stopFishing();
        }
    }

    private void detectGui() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!isRunning.get())
            return;

        Screen screen = client.currentScreen;
        if (screen == null)
            return;

        // Stop fishing only if we're in a server-driven GUI (like inventories,
        // villagers, chests, etc.)
        if (screen instanceof HandledScreen<?>) {
            stopFishing();
        }
    }

    private void handleKeyPresses() {
        // Handle toggle key - only process once per key press
        if (KeyBindings.startKey.wasPressed()) {
            FileUtils.writeStringToFile("start key pressed");
            if (!isRunning.get()) {
                startFishing();
            }
        }

        // Handle stop key - only process once per key press
        if (KeyBindings.stopKey.wasPressed()) {
            FileUtils.writeStringToFile("stop key pressed");
            stopFishing();
            FileUtils.writeStringToFile("[LuluTheFish] Stop activated! (F10 key pressed)");
        }
    }

    private void onTick() {
        sanityCheck();
        detectGui();
        handleKeyPresses();
    }

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
