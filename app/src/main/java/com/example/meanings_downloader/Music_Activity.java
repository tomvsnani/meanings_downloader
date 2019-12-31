package com.example.meanings_downloader;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.media.MediaPlayer;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Music_Activity extends AppCompatActivity {
    MediaBrowserCompat mediaBrowser;
    MediaPlayer player;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_);
        final Button button = findViewById(R.id.play_pause);
        player=new MediaPlayer();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String path = Music_Activity.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/23.mp3";
                File file = new File(path);
                if (file.exists()) {
                   // Uri uri=Uri.parse("android.resource://"+getPackageName()+);

                    Log.d("clickedhere","1");
                    MediaControllerCompat.getMediaController(Music_Activity.this).getTransportControls().playFromMediaId(String.valueOf(R.raw.rooba), null);
                    Log.d("clickedhere","2");
                }
               // getMediaController().getTransportControls().play();
                Log.d("clickedhere","3");
            }
        });

        final MediaBrowserCompat.ConnectionCallback connectioncallback = new MediaBrowserCompat.ConnectionCallback() {
            @Override
            public void onConnected() {
                super.onConnected();
                Log.d("clickedhere","5");
                MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
                try {
                    final MediaControllerCompat mediaControllerCompat = new MediaControllerCompat(getApplicationContext(), token);
                    MediaControllerCompat.setMediaController(Music_Activity.this, mediaControllerCompat);


                    mediaControllerCompat.registerCallback(new MediaControllerCompat.Callback() {
                        @Override
                        public void onPlaybackStateChanged(PlaybackStateCompat state) {
                            Log.d("clickedhere","6");

                            super.onPlaybackStateChanged(state);
                            if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
                                button.setText("pause");
                            } else if (state.getState() == PlaybackStateCompat.STATE_PAUSED) {
                                button.setText("play");
                            }
                        }
                    });

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };

        mediaBrowser=new MediaBrowserCompat(this,new ComponentName(getApplicationContext(),Music.class),connectioncallback,null);
        mediaBrowser.connect();

    }
}





