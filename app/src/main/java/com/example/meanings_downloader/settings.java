package com.example.meanings_downloader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.net.URI;
import java.net.URL;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content,new Fragment()).commit();


    }
   public  static  class Fragment extends PreferenceFragmentCompat{

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings,rootKey);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            Preference preference=findPreference("uri");
            Intent intent =new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://google.com"));
            preference.setIntent(intent);

            super.onActivityCreated(savedInstanceState);
        }
    }


}
