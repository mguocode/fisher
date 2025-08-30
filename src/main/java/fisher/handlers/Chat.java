package fisher.handlers;
// CR mguo: maybe we want to remove extra chat messages

import net.minecraft.text.Text;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.client.MinecraftClient;
import fisher.util.FileUtils;
import fisher.util.SeaCreatures;
import fisher.util.SoundPlayer;

public class Chat {

    public static void handleChat(Text message) {
        String messageString = message.getString();
        if (messageString != null) {
            String creature = SeaCreatures.mapMessageToSeaCreatureName(messageString);

            if (creature != null) {
                FileUtils.writeStringToFile(creature);
                if (SeaCreatures.shouldFlag(creature))
                    SoundPlayer.play(SoundPlayer.interestingSound);
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
}
