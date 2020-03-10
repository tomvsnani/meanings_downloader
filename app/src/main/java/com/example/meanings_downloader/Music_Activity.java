package com.example.meanings_downloader;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.Executors;

public class Music_Activity extends AppCompatActivity implements View.OnClickListener {
    MediaBrowserCompat mediaBrowser;
    ContentResolver resolver;
    MediaControllerCompat mediaController;

    Uri contentUri;
    Button button;
    MediaBrowserCompat.ConnectionCallback  callback=new MediaBrowserCompat.ConnectionCallback(){

        @Override
        public void onConnected() {
            Log.d("connectedhere","connected");


            MediaSessionCompat.Token token=mediaBrowser.getSessionToken();
            try {
                mediaController=new MediaControllerCompat(getApplicationContext(),token);
                MediaControllerCompat.setMediaController(Music_Activity.this,mediaController);




                MediaControllerCompat.Callback controllerCallback=new MediaControllerCompat.Callback() {
                    @Override
                    public void onSessionReady() {
                        super.onSessionReady();
                    }

                    @Override
                    public void onPlaybackStateChanged(PlaybackStateCompat state) {
                        super.onPlaybackStateChanged(state);
                    }
                };


                mediaController.registerCallback(controllerCallback);



            } catch (RemoteException e) {
                e.printStackTrace();
            }
            super.onConnected();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_);
        button=findViewById(R.id.play_pause);



      mediaBrowser=new MediaBrowserCompat(this,new ComponentName(getApplicationContext(),Music.class),callback,null);

      button.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        mediaBrowser.connect();
        super.onStart();
    }


    public void preparedata() throws IOException {
      /*  resolver=getContentResolver();
        Cursor cursor= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null);
        }
        int cursorid=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID) ;
       cursor.moveToFirst();
           Long id=cursor.getLong(cursorid);
           contentUri=ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id);
           Log.d("enteredprepared",contentUri.toString());

        MediaControllerCompat.getMediaController(Music_Activity.this).getTransportControls().playFromUri(contentUri,null);*/





    }


    @Override
    protected void onResume() {
        super.onResume();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Log.d("entered","entered");

        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},5);




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==5)
        {
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    try {
                        preparedata();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}





