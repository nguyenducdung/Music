package rikkeisoft.nguyenducdung.com.music.notification;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import rikkeisoft.nguyenducdung.com.music.activity.PlayActivity;
import rikkeisoft.nguyenducdung.com.music.model.SongInfo;

public class Notification extends Service {
    private MusicReceiver musicReceiver;
    private SongInfo music;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notIntent = new Intent(this, PlayActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        android.app.Notification.Builder builder = new android.app.Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setTicker(music.getSongname())
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(music.getSongname());
        android.app.Notification not = builder.build();

        startForeground(-1, not);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    private class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Do nothing
        }
    }
}
