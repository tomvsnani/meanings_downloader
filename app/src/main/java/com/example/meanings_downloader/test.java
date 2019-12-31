package com.example.meanings_downloader;

import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.service.media.MediaBrowserService;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class test {
    HttpURLConnection connection;

    String link="https://repairavoidance.blob.core.windows.net/packages/WindowsDeviceRecoveryToolInstaller.exe";
    URL url;
    public void create( Context context) throws IOException {
        Log.d("vacchina","ok");
      url=new URL(link);

        connection=(HttpURLConnection)url.openConnection();
        connection.setDoOutput(false);
        Log.d("vacchina",String.valueOf( connection.getContentLength()));
        String type=connection.getContentType();
        InputStream stream=connection.getInputStream();
        Log.d("vacchina","hello");

        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(link
                );
         String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.toLowerCase());
        File file=new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), URLUtil.guessFileName(link,null,type));

            file.createNewFile();

        FileOutputStream outputStream=new FileOutputStream(file);
        BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(outputStream,2048);
        Integer count=0;
        Log.d("vacchina","okk");
        while((count=stream.read())!=-1){
            bufferedOutputStream.write(count);

            Log.d("vacchina",String.valueOf(count));
        }

        bufferedOutputStream.close();
        outputStream.close();
        connection.disconnect();
    }

    public void httpdownloader(Context context){

        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(link));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,URLUtil.guessFileName(link,null,MimeTypeMap.getFileExtensionFromUrl(link)));
        DownloadManager manager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        request.setTitle("file download");
        request.setDescription("file is being downloaded");

        manager.enqueue(request);

        MediaPlayer mediaPlayer=new MediaPlayer();
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {


            }
        });
    }

}
