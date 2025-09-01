package fisher.fishing;

import fisher.util.SoundPlayer;

public class Sound implements FishingIntf {
    private static final Sound INSTANCE = new Sound();

    private Sound() {
    }

    public static Sound getInstance() {
        return INSTANCE;
    }

    @Override
    public void onStartFishing() {
        SoundPlayer.play(SoundPlayer.startSound);
    }

    @Override
    public void onStopFishing() {
        SoundPlayer.play(SoundPlayer.stopSound);
    }

}
