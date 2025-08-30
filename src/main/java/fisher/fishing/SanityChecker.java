
package fisher.fishing;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

import fisher.FileUtils;
import fisher.SoundPlayer;
import net.minecraft.client.MinecraftClient;

public class SanityChecker implements FishingIntf {
    private static final SanityChecker INSTANCE = new SanityChecker();
    private static final long SANITY_CHECK_TIMEOUT = 30000;
    private static final long MAX_DISTANCE_FROM_FISHING_START = 30;

    private SanityChecker() {
    }

    public static SanityChecker getInstance() {
        return INSTANCE;
    }

    private long lastDetectionTime;
    private double fishingStartX, fishingStartZ;
    private String fishingStartBiome;

    public void onStartFishing() {
        MinecraftClient client = MinecraftClient.getInstance();
        lastDetectionTime = System.currentTimeMillis();
        fishingStartX = client.player.getX();
        fishingStartZ = client.player.getZ();
        fishingStartBiome = client.world.getBiome(client.player.getBlockPos()).getKey().get().getValue().toString();
    }

    public void onStopFishing() {
    }

    private void sanityCheck() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!State.getInstance().isRunning())
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
            SoundPlayer.play(SoundPlayer.sanitySound);
            Manager.stopFishing();
        }
    }

    private void guiCheck() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!State.getInstance().isRunning())
            return;

        Screen screen = client.currentScreen;
        if (screen == null)
            return;

        // Stop fishing only if we're in a server-driven GUI (like inventories,
        // villagers, chests, etc.)
        if (screen instanceof HandledScreen<?>) {
            Manager.stopFishing();
        }
    }

    public void onTick() {
        guiCheck();
        sanityCheck();
    }

}
