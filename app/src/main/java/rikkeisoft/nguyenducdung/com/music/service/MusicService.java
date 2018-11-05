package rikkeisoft.nguyenducdung.com.music.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import rikkeisoft.nguyenducdung.com.music.R;
import rikkeisoft.nguyenducdung.com.music.activity.PlayActivity;
import rikkeisoft.nguyenducdung.com.music.model.SongInfo;

public class MusicService extends Service {
    public static RemoteViews notificationLayout;
    SongInfo music;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationLayout = new RemoteViews(getPackageName(), R.layout.big_notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras() != null) {
            music = (SongInfo) intent.getExtras().getSerializable("musicservice");
            boolean isPlaying = intent.getExtras().getBoolean("playkey");
            if (music != null) {
                notificationLayout.setTextViewText(R.id.textSongName, music.getSongname());
                isPlaying = true;
            }
            if(isPlaying){
                notificationLayout.setImageViewResource(R.id.btnPause, R.drawable.ic_pause_white_48dp);
            } else {
                notificationLayout.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
            }
            startServiceWithNotification(isPlaying);
        } else {
            stopService();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startServiceWithNotification(boolean isPlaying) {
        Intent notificationIntent = new Intent(this, PlayActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPlayNoti = new Intent("action");
        intentPlayNoti.putExtra("play", isPlaying);
        PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(this, 0,
                intentPlayNoti, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationLayout.setOnClickPendingIntent(R.id.btnPlay, pendingPlayIntent);

        Notification customNotification = new NotificationCompat.Builder(this, "channel")
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, customNotification);
    }

    public void stopService() {
        stopForeground(true);
        stopSelf();
    }

}
