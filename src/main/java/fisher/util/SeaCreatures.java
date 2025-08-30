package fisher.util;

// import java.util.HashMap;
// import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SeaCreatures {
        private static final Map<String, String> messageToCreature = Map.ofEntries(
                        // Water Sea Creatures
                        Map.entry("You caught a Sea Walker.", "Sea Walker"),
                        Map.entry("A Squid appeared.", "Squid"),
                        Map.entry("Pitch darkness reveals a Night Squid.", "Night Squid"),
                        Map.entry("You stumbled upon a Sea Guardian.", "Sea Guardian"),
                        Map.entry("It looks like you've disrupted the Sea Witch's brewing session. Watch out, she's furious!",
                                        "Sea Witch"),
                        Map.entry("You reeled in a Sea Archer.", "Sea Archer"),
                        Map.entry("The Rider of the Deep has emerged.", "Rider of the Deep"),
                        Map.entry("Huh? A Catfish!", "Catfish"),
                        Map.entry("Is this even a fish? It's the Carrot King!", "Carrot King"),
                        Map.entry("Your Chumcap Bucket trembles, it's an Agarimoo.", "Agarimoo"),
                        Map.entry("Gross! A Sea Leech!", "Sea Leech"),
                        Map.entry("You've discovered a Guardian Defender of the sea.", "Guardian Defender"),
                        Map.entry("You have awoken the Deep Sea Protector, prepare for a battle!",
                                        "Deep Sea Protector"),
                        Map.entry("The Water Hydra has come to test your strength.", "Water Hydra"),
                        Map.entry("The Loch Emperor arises from the depths.", "The Loch Emperor"),

                        // Oasis Sea Creatures
                        Map.entry("An Oasis Rabbit appears from the water.", "Oasis Rabbit"),
                        Map.entry("An Oasis Sheep appears from the water.", "Oasis Sheep"),

                        // Crystal Hollows
                        Map.entry("A Water Worm surfaces!", "Water Worm"),
                        Map.entry("A Poisoned Water Worm surfaces!", "Poisoned Water Worm"),

                        // Abandoned Quarry
                        Map.entry("... you've caught a Bloated Mithril Grubber.", "Bloated Mithril Grubber"),
                        Map.entry("... you've caught a Large Mithril Grubber.", "Large Mithril Grubber"),
                        Map.entry("... you've caught a Medium Mithril Grubber.", "Medium Mithril Grubber"),
                        Map.entry("... you've caught a Mithril Grubber.", "Mithril Grubber"),

                        // Backwater Bayou
                        Map.entry("A Dumpster Diver has emerged from the swamp!", "Dumpster Diver"),
                        Map.entry("The Trash Gobbler is hungry for you!", "Trash Gobbler"),
                        Map.entry("The desolate wail of a Banshee breaks the silence.", "Banshee"),
                        Map.entry("A swampy mass of slime emerges, the Bayou Sludge!", "Bayou Sludge"),
                        Map.entry("A long snout breaks the surface of the water. It's an Alligator!", "Alligator"),
                        Map.entry("A massive Titanoboa surfaces. Its body stretches as far as the eye can see.",
                                        "Titanoboa"),

                        // Hotspot Creatures
                        Map.entry("Is it a frog? Is it a man? Well, yes, sorta, IT'S FROG MAN!!!!!!", "Frog Man"),
                        Map.entry("A Snapping Turtle is coming your way, and it's ANGRY!", "Snapping Turtle"),
                        Map.entry("A garish set of tentacles arise. It's a Blue Ringed Octopus!",
                                        "Blue Ringed Octopus"),
                        Map.entry(
                                        "The water bubbles and froths. A massive form emerges- you have disturbed the Wiki Tiki! You shall pay the price.",
                                        "Wiki Tiki"),

                        // Other Water Creatures
                        Map.entry("An Abyssal Miner breaks out of the water!", "Abyssal Miner"),
                        Map.entry("A leech of the mines surfaces... you've caught a Bloated Mithril Grubber.",
                                        "Bloated Mithril Grubber"),
                        Map.entry("A leech of the mines surfaces... you've caught a Large Mithril Grubber.",
                                        "Large Mithril Grubber"),
                        Map.entry("A leech of the mines surfaces... you've caught a Medium Mithril Grubber.",
                                        "Medium Mithril Grubber"),
                        Map.entry("A leech of the mines surfaces... you've caught a Mithril Grubber.",
                                        "Mithril Grubber"),

                        // Spooky / Festival / Jerry's Workshop / etc.
                        Map.entry("A Frosty emerges.", "Frosty"), // Example placeholder; adjust message as per actual
                                                                  // text
                        Map.entry("A Yeti appears!", "Yeti"),
                        Map.entry("Grinch emerges from the water.", "Grinch"),
                        Map.entry("Phantom Fisher appears!", "Phantom Fisher"),
                        Map.entry("The Grim Reaper appears!", "Grim Reaper"));

        private static final Set<String> interestingCreatures = Set.of("Alligator", "The Loch Emperor", "Titanoboa",
                        "Wiki Tiki",
                        "Blue Ringed Octopus");

        public static String mapMessageToSeaCreatureName(String message) {
                for (Map.Entry<String, String> entry : messageToCreature.entrySet()) {
                        if (message.contains(entry.getKey())) {
                                return entry.getValue();
                        }
                }
                return null;
        }

        public static boolean shouldFlag(String creature) {
                return interestingCreatures.contains(creature);
        }
}