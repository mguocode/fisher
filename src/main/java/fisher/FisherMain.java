
package fisher;

import net.minecraft.util.Colors;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;

import fisher.handlers.Tick;
import fisher.handlers.Chat;
import fisher.fishing.Manager;
import fisher.fishing.State;
import fisher.util.FileUtils;
import fisher.util.KeyBindings;
import net.fabricmc.api.ClientModInitializer;

public class FisherMain implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register key bindings
        FileUtils.initialize();
        FileUtils.writeStringToFile("[LuluTheFish] Starting initialize");
        KeyBindings.register();

        // Register subtitle logger
        // ClientLifecycleEvents.CLIENT_STARTED.register(this::tryRegisterListener);
        // ClientLifecycleEvents.CLIENT_STARTED.register(this::initializeReflection);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Tick.onTick();
        });

        // Cleanup on shutdown
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            Manager.stopFishing();
            // executor.shutdown();
        });

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            Chat.handleChat(message);
        });

        HudElementRegistry.addLast(Identifier.of("lulutherich", "fishing_status"), (context, tickCounter) ->

        {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.textRenderer == null)
                return;

            String status = State.getInstance().isRunning() ? "[Fishing: ON]" : "[Fishing: OFF]";
            int color = State.getInstance().isRunning() ? Colors.GREEN : Colors.RED;

            context.drawTextWithShadow(client.textRenderer, status, 10, 10, color);
        });

        FileUtils.writeStringToFile("[LuluTheFish] Initialized");
    }

}
