
package fisher.input;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ActionExecutorManager {
    private static final List<ActionExecutorIntf> actionExecutors = new ArrayList<>(
            Arrays.asList(Clicker.getInstance()));

    public static void onStopFishing() {
        for (ActionExecutorIntf actionExecutor : actionExecutors) {
            actionExecutor.onStopFishing();
        }
    }
}
