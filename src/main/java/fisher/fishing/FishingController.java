package fisher.fishing;

import fisher.util.FileUtils;
import fisher.input.Clicker;

public class FishingController {
    private static final double MIN_TIME_BETWEEN_DETECTIONS = 1.5; // seconds
    private static long lastDetectionTime = System.currentTimeMillis();

    public static void onDetect() {
        if (!State.getInstance().isRunning()) {
            return;
        }
        long now = System.currentTimeMillis();
        double nowSeconds = now / 1000.0;
        double lastDetectionSeconds = lastDetectionTime / 1000.0;
        double timeSinceLastDetection = nowSeconds - lastDetectionSeconds;

        if (timeSinceLastDetection > MIN_TIME_BETWEEN_DETECTIONS) {
            lastDetectionTime = now;

            FileUtils.writeStringToFile("[LuluTheFish] detected note block!!");
            Clicker.getInstance().scheduleReelAndRecast();

        } else {
            FileUtils.writeStringToFile(
                    String.format("[LuluTheFish] Warning!! detected duplicate note block again in only %.3f",
                            timeSinceLastDetection));
        }
    }
}
