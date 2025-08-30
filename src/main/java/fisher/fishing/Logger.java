package fisher.fishing;

import fisher.util.FileUtils;

public class Logger implements FishingIntf {
    private static final Logger INSTANCE = new Logger();

    private Logger() {
    }

    public static Logger getInstance() {
        return INSTANCE;
    }

    @Override
    public void onStartFishing() {
        FileUtils.writeStringToFile("[LuluTheFish] Auto-fishing started!");
    }

    @Override
    public void onStopFishing() {
        FileUtils.writeStringToFile("[LuluTheFish] Auto-fishing stopped.");
    }

}
