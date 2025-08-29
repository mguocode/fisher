package mypackage;
// CR mguo: make it so that we dont detect like X fishes within Y seconds

// CR mguo: maybe read the chat text so you can raise if haven't caught sea

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

// import net.fabricmc.fabric.impl.client.event.lifecycle.ClientLifecycleEventsImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundEvent;
import org.lwjgl.glfw.GLFW;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

// import net.minecraft.client.util.Window;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import mypackage.mixin.MouseInvoker;

public class LuluTheFish implements ClientModInitializer {

    // Configuration constants matching your Python script
    private static final double MIN_TIME_BETWEEN_DETECTIONS = 1.5; // seconds
    private static final long SANITY_CHECK_TIMEOUT = 30000; // 30 seconds in milliseconds
    private static final long MAX_DISTANCE_FROM_FISHING_START = 30;

    // Reflection method cache for performance
    private MouseInvoker invoker = null;

    // State tracking
    private final Random random = new Random();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final FishingController fishingController = new FishingController(executor, random);

    // Key bindings

    // Subtitle logger
    private LuluTheListen subtitleLogger;
    private final AtomicBoolean listenerRegistered = new AtomicBoolean(false);
    private final AtomicBoolean reflectionInitialized = new AtomicBoolean(false);
    // private final AtomicBoolean reflectionInitialized = new AtomicBoolean(false);

    @Override
    public void onInitializeClient() {
        // Register key bindings
        FileUtils.initializeFile();
        FileUtils.writeStringToFile("[LuluTheFish] Starting initialize");
        FileUtils.writeStringToFile("[LuluTheFish] Starting initialize, last modified");

        KeyBindings.register();

        // Register subtitle logger
        // ClientLifecycleEvents.CLIENT_STARTED.register(this::tryRegisterListener);
        // ClientLifecycleEvents.CLIENT_STARTED.register(this::initializeReflection);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!listenerRegistered.get()) {
                tryRegisterListener(client);
            }
            if (!reflectionInitialized.get()) {
                tryInitializeReflection(client);
            }
            onTick();
        });

        // Cleanup on shutdown
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            fishingController.stopFishing();
            executor.shutdown();
            if (subtitleLogger != null) {
                try {
                    subtitleLogger.close();
                } catch (Exception ignored) {
                }
            }
        });

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            handleChat(message);
            FileUtils.writeStringToFile("Event Type: GAME");
            FileUtils.writeStringToFile("GAME Message: " + message.getString());
        });

        HudElementRegistry.addLast(Identifier.of("lulutherich", "fishing_status"), (context, tickCounter) ->

        {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.textRenderer == null)
                return;

            String status = fishingController.isRunning() ? "[Fishing: ON]" : "[Fishing: OFF]";
            int color = fishingController.isRunning() ? Colors.GREEN : Colors.RED;

            context.drawTextWithShadow(client.textRenderer, status, 10, 10, color);
        });

        FileUtils.writeStringToFile("[LuluTheFish] Initialized");
    }

    /**
     * Initialize reflection method for accessing private onMouseButton
     */

    private void tryInitializeReflection(MinecraftClient client) {
        try {
            invoker = (MouseInvoker) (Object) client.mouse;
            reflectionInitialized.set(true);
            FileUtils.writeStringToFile("[LuluTheFish] Successfully initialized reflection for onMouseButton");
        } catch (Throwable e) {
            FileUtils.writeStringToFile("[LuluTheFish] Failed to initialize reflection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Call the private onMouseButton method using reflection
     */
    // Very low level, should only be called by toggleMouse
    private void callOnMouseButton(long window, int button, int action, int mods) {
        if (invoker == null)
            return;

        try {
            invoker.invokeOnMouseButton(window, button, action, mods);
        } catch (Throwable e) {
            System.err.println("[LuluTheFish] Failed to call onMouseButton: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Only interface which actions should be done
    // always safety checks isRunning
    private void toggleMouse(boolean click) {
        if (fishingController.isRunning()) {
            long window = MinecraftClient.getInstance().getWindow().getHandle();
            int button = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
            int action = click ? GLFW.GLFW_PRESS : GLFW.GLFW_RELEASE;
            callOnMouseButton(window, button, action, 0);
        }
    }

    private void tryRegisterListener(MinecraftClient client) {
        if (listenerRegistered.get())
            return;

        net.minecraft.client.sound.SoundManager sm = client.getSoundManager();
        if (sm == null)
            return;

        if (subtitleLogger == null) {
            subtitleLogger = new LuluTheListen(this);
        }
        sm.registerListener(subtitleLogger);
        listenerRegistered.set(true);
        FileUtils.writeStringToFile("[LuluTheFish] Subtitle logger registered.");
    }

    private boolean passesCheck(SoundInstance sound) {
        return sound.getId().toString().contains("note_block.pling");
    }

    private void handleChat(Text message) {
        String messageString = message.getString();
        if (messageString != null) {
            String creature = SeaCreatures.mapMessageToSeaCreatureName(messageString);

            if (creature != null) {
                FileUtils.writeStringToFile(creature);
                if (SeaCreatures.shouldFlag(creature))
                    SoundManager.play(interestingSound);
                MinecraftClient client = MinecraftClient.getInstance();
                client.execute(() -> {
                    if (client.player != null) {
                        Text chatMsg = Text.literal("Sea Creature: " + creature)
                                .setStyle(Style.EMPTY.withColor(Formatting.AQUA));
                        client.player.sendMessage(chatMsg, false); // false = don't put in action bar
                    }
                });
            }
        }

    }

    /**
     * Clipped gaussian function matching your Python script
     */
    private double clippedGaussian(double mean, double stdev, double minTime, double maxTime) {
        double s = random.nextGaussian() * stdev + mean;
        s = Math.max(Math.min(s, maxTime), minTime);
        return s;
    }

    /**
     * Right click function
     */
    private void rightClick() {
        toggleMouse(true);

        double holdTime = clippedGaussian(0.07, 0.0112, 0.051231, 0.11231);
        FileUtils.writeStringToFile(String.format("[LuluTheFish] mouse down, waiting: %.3f", holdTime));

        executor.schedule(() -> {
            toggleMouse(false);
        }, (long) (holdTime * 1000), TimeUnit.MILLISECONDS);
    }

    /**
     * Reel and recast function exactly matching your Python script
     */
    private void reelAndRecast() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null)
            return;

        // Ensure we're on the main thread
        client.execute(() -> {
            // First right click (reel in)
            rightClick();

            // gaussian_sleep(0.3, 0.05, 0.21231, 2) - delay between clicks
            double delayTime = clippedGaussian(0.3, 0.05, 0.21231, 2.0);
            FileUtils.writeStringToFile(String.format("[LuluTheFish] time to wait until recast: %.3f", delayTime));

            // Schedule second right click (recast) after delay
            executor.schedule(() -> {
                client.execute(() -> rightClick()); // Second right click on main thread
            }, (long) (delayTime * 1000), TimeUnit.MILLISECONDS);
        });
    }

    /**
     * Combined subtitle logger that handles both logging and fishing triggers
     */
    private static class LuluTheListen
            implements net.minecraft.client.sound.SoundInstanceListener, AutoCloseable {
        private final LuluTheFish fishingMod;

        public LuluTheListen(LuluTheFish fishingMod) {
            this.fishingMod = fishingMod;
        }

        @Override
        public void onSoundPlayed(net.minecraft.client.sound.SoundInstance sound,
                net.minecraft.client.sound.WeightedSoundSet soundSet, float range) {

            // Text subtitle = soundSet.getSubtitle();
            // if (subtitle == null)
            // return;

            // Subtitle text shown to players
            // String subtitleText = subtitle.getString();

            // Logging for debug
            if (this.fishingMod.passesCheck(sound)) {
                FileUtils.writeStringToFile("=== SOUND DEBUG ===");
                FileUtils.writeStringToFile("Sound ID: " + sound.getId().toString());
                FileUtils.writeStringToFile("Sound Pitch: " + sound.getPitch());
                FileUtils.writeStringToFile("Sound Volume: " + sound.getVolume());
                FileUtils.writeStringToFile("Sound Set weight " + soundSet.getWeight());
                FileUtils.writeStringToFile("Range: " + range);
                FileUtils.writeStringToFile("==================");
            }

            // Check for fishing triggers (enhanced functionality)
            fishingMod.handleSound(sound);
        }

        @Override
        public void close() {
            // Nothing to close right now; method exists for future expansions
        }
    }
}