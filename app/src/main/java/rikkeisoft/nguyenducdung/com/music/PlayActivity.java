package rikkeisoft.nguyenducdung.com.music;

import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

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
    private static final int STATE_PAUSED = 0;
    private static final int STATE_PLAYING = 1;
    private int currentState;
    private MediaBrowserCompat mediaBrowserCompat;

    private MediaBrowserCompat.ConnectionCallback connectionCallback = new MediaBrowserCompat.ConnectionCallback() {

        @Override
        public void onConnected() {
            super.onConnected();
            try {
                MediaControllerCompat mediaControllerCompat = new MediaControllerCompat(PlayActivity.this, mediaBrowserCompat.getSessionToken());
                mediaControllerCompat.registerCallback(mediaControllerCompatCallback);
                MediaControllerCompat.setMediaController(PlayActivity.this, mediaControllerCompat);
                MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().playFromMediaId(songUrl, null);

            } catch( RemoteException e ) {
                e.printStackTrace();
            }
        }
    };

    private MediaControllerCompat.Callback mediaControllerCompatCallback = new MediaControllerCompat.Callback() {

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            if( state == null ) {
                return;
            }

            switch( state.getState() ) {
                case PlaybackStateCompat.STATE_PLAYING: {
                    currentState = STATE_PLAYING;
                    break;
                }
                case PlaybackStateCompat.STATE_PAUSED: {
                    currentState = STATE_PAUSED;
                    break;
                }
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initView();
        getData();
        mediaBrowserCompat = new MediaBrowserCompat(this, new ComponentName(this, MyMusicService.class),
                connectionCallback, getIntent().getExtras());
        mediaBrowserCompat.connect();
        ibtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ibtnPlay.setVisibility(View.GONE);
                            ibtnPause.setVisibility(View.VISIBLE);
                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setDataSource(songUrl);
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();
                                    sbSong.setProgress(0);
                                    sbSong.setMax(mediaPlayer.getDuration());
                                }
                            });
                        }catch (Exception e){

                        }
                    }
                };
                if( currentState == STATE_PAUSED ) {
                    MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().play();
                    currentState = STATE_PLAYING;
                } else {
                    if( MediaControllerCompat.getMediaController(PlayActivity.this).getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING ) {
                        MediaControllerCompat.getMediaController(PlayActivity.this).getTransportControls().pause();
                    }

                    currentState = STATE_PAUSED;
                }
                myHandler.postDelayed(runnable,100);
            }
        });
        ibtnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnPause.setVisibility(View.GONE);
                ibtnPlay.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
            }
        });
        Thread thread = new runThread();
        thread.start();
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

    public class runThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mediaPlayer != null) {
                    sbSong.post(new Runnable() {
                        @Override
                        public void run() {
                            sbSong.setProgress(mediaPlayer.getCurrentPosition());
                        }
                    });
                }
            }
        }

    }



}
