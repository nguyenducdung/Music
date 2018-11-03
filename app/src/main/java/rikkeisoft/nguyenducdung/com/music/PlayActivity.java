package rikkeisoft.nguyenducdung.com.music;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {
    private TextView tvTitle;
    private TextView tvTimeSong;
    private SeekBar sbSong;
    private ImageButton ibtnPlay;
    private ImageButton ibtnStop;
    private ImageButton ibtnNext;
    private ImageButton ibtnPrev;
    private ImageButton ibtnPause;
    private MediaPlayer mediaPlayer;
    private Handler myHandler = new Handler();
    private ArrayList<SongInfo> songs;
    private String songName;
    private String songArtist;
    private String songUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initView();
        getData();
        ibtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
                startService(new Intent(PlayActivity.this, MyService.class));
                NotificationGenerator.customBigNotification(getApplicationContext());
                NotificationGenerator.openActivityNotification(getApplicationContext());
            }
        });
        ibtnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(PlayActivity.this, MyService.class));
                ibtnPause.setVisibility(View.GONE);
                ibtnPlay.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
            }
        });
    }

    private void getData() {
        Intent intent = getIntent();
        songName = intent.getStringExtra("name");
        songArtist = intent.getStringExtra("artist");
        songUrl = intent.getStringExtra("url");
        tvTitle.setText(songName);
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        tvTimeSong = findViewById(R.id.tv_song_time);
        sbSong = findViewById(R.id.sb_song);
        ibtnNext = findViewById(R.id.ibtn_next);
        ibtnPlay = findViewById(R.id.ibtn_play);
        ibtnPrev = findViewById(R.id.ibtn_previous);
        ibtnStop = findViewById(R.id.ibtn_stop);
        ibtnPause = findViewById(R.id.ibtn_pause);
        ibtnPause.setVisibility(View.GONE);
    }
    public void playMusic() {
        ibtnPlay.setVisibility(View.GONE);
        ibtnPause.setVisibility(View.VISIBLE);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(songUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                sbSong.setProgress(0);
                sbSong.setMax(mediaPlayer.getDuration());
            }
        });
    }
}
