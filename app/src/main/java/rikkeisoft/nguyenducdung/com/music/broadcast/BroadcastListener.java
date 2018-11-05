package rikkeisoft.nguyenducdung.com.music.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import rikkeisoft.nguyenducdung.com.music.activity.PlayActivity;

public class BroadcastListener extends BroadcastReceiver {
    private boolean play;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            play = intent.getBooleanExtra("play", true);
            if (play) {
                PlayActivity.player.pause();
            } else {
                PlayActivity.player.start();
            }
        }
    }
}
