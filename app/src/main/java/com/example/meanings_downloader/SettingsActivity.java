package com.example.meanings_downloader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
         SharedPreferences sharedPreferences;
        SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
          sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            final ListPreference listPreference=findPreference("reply");
             onSharedPreferenceChangeListener=new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if(key.equals("reply")){
                        Log.d("valueis",sharedPreferences.getString("reply","he"));
                    }
                    else
                    {
                        Log.d("valueis",sharedPreferences.getString("signature","yy"));
                    }
                }
            };


        }

        @Override
        public void onResume() {
            super.onResume();
            sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        }
    }

}