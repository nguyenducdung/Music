package rikkeisoft.nguyenducdung.com.music.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

import rikkeisoft.nguyenducdung.com.music.R;
import rikkeisoft.nguyenducdung.com.music.adapter.SongAdapter;
import rikkeisoft.nguyenducdung.com.music.model.SongInfo;

public class MainActivity extends AppCompatActivity {
    private ArrayList<SongInfo> songs = new ArrayList<>();
    private RecyclerView rcMusicList;
    private SongAdapter songAdapter;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewLinear();
        checkUserPermission();
    }

    private void initViewLinear() {
        rcMusicList = findViewById(R.id.rc_music_list);
        rcMusicList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcMusicList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        rcMusicList.addItemDecoration(dividerItemDecoration);
        songAdapter = new SongAdapter(getApplicationContext(),songs);
        rcMusicList.setAdapter(songAdapter);
        songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SongInfo obj, int position) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), PlayActivity.class);
                String songName = obj.getSongname();
                String songArtist = obj.getArtistname();
                String songUrl = obj.getSongUrl();
                intent.putExtra("name", songName);
                intent.putExtra("artist" , songArtist);
                intent.putExtra("url", songUrl);
                startActivity(intent);
            }
        });
    }

    private void checkUserPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
                return;
            }
        }
        loadSongs();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadSongs();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    checkUserPermission();
                }
                break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            }

        }

    public void loadSongs(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    SongInfo s = new SongInfo(name,artist,url);
                    songs.add(s);
                } while (cursor.moveToNext());
            }

            cursor.close();
            songAdapter = new SongAdapter(MainActivity.this,songs);
            }
        }
}
