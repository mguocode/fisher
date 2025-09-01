package fisher.fishing;

import fisher.util.FileUtils;
import fisher.input.Clicker;

public class FishingController {
    private static final double MIN_TIME_BETWEEN_DETECTIONS_MS = 1500;
    private static final double MIN_TIME_BETWEEN_ABILITY_CASTS_MS = 3000;
    private static long lastDetectionTime = System.currentTimeMillis();
    private static long lastAbilityCastTime = System.currentTimeMillis();

    public static void onDetect() {
        if (!State.getInstance().isRunning()) {
            return;
        }
        long now = System.currentTimeMillis();
        SanityChecker.getInstance().setLastDetectionTime(now);

        if (now - lastDetectionTime > MIN_TIME_BETWEEN_DETECTIONS_MS) {
            lastDetectionTime = now;

            FileUtils.writeStringToFile("[LuluTheFish] detected note block!!");
            if (State.getInstance().inAbilityMode()
                    && (now - lastAbilityCastTime > MIN_TIME_BETWEEN_ABILITY_CASTS_MS)) {
                lastAbilityCastTime = now;
                Clicker.getInstance().scheduleReelAbilityAndRecast();

            } else {
                Clicker.getInstance().scheduleReelAndRecast();
            }

        }
    }
}
