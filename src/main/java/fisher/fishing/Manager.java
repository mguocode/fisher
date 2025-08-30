
package fisher.fishing;

import java.util.List;

import fisher.input.ActionExecutorManager;

import java.util.ArrayList;
import java.util.Arrays;

public class Manager {
    private static final List<FishingIntf> fishers = new ArrayList<>(
            Arrays.asList(State.getInstance()));

    public static void startFishing() {
        for (FishingIntf fisher : fishers) {
            fisher.onStartFishing();
        }
    }

    public static void stopFishing() {
        ActionExecutorManager.onStopFishing();
        for (FishingIntf fisher : fishers) {
            fisher.onStopFishing();
        }
    }

}
