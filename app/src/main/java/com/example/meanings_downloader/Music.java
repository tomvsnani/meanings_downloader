package com.example.meanings_downloader;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import java.io.IOException;
import java.util.List;

public class Music extends MediaBrowserServiceCompat {

    MediaSessionCompat compat;
    MediaPlayer player;

    @Override
    public void onCreate() {
        Log.d("ippudu","vacchina");
        super.onCreate();
        player=new MediaPlayer();

       compat =new MediaSessionCompat(this,"hello");
       compat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS|MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        PlaybackStateCompat.Builder builder=new PlaybackStateCompat.Builder().setActions(PlaybackStateCompat.ACTION_PLAY|PlaybackStateCompat.ACTION_PLAY_PAUSE);
        compat.setPlaybackState(builder.build());
        compat.setCallback(new MediaSessionCompat.Callback() {

            @Override
            public void onPlayFromMediaId(String mediaId, Bundle extras) {
                super.onPlayFromMediaId(mediaId, extras);
                Log.d("lengthof","duration1");
                Uri uri=Uri.parse("android.resource://"+getPackageName()+"/"+mediaId);
                Log.d("lengthof","duration2");
                try {
                    player.setDataSource(getApplicationContext(),uri);
                    player.prepare();
                    player.start();
                    Log.d("lengthof","duration"+String.valueOf(player.getDuration()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onPause() {
                super.onPause();
                player.pause();
            }
        });
       setSessionToken(compat.getSessionToken());
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return new  BrowserRoot("hello",null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }
}
