package com.example.meanings_downloader;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements Adapter.Clicklistener, TextToSpeech.OnInitListener {

    static List<Entity> list_local;

    static {
        list_local = new ArrayList<>();
    }

    FrameLayout container;
    FragmentManager fragmentManager;
    MainFragment blankFragment;
    int fav_meaning_count = 0;
    Database database;
    Bundle bundle;
    Random random;
    TextToSpeech textToSpeech;
    InputMethodManager inputMethodManager;
    FragmentManager.OnBackStackChangedListener onBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            Log.d("printing", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textToSpeech = new TextToSpeech(getApplicationContext(), MainActivity.this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        bundle = new Bundle();
        container = findViewById(R.id.container);
        fragmentManager = getSupportFragmentManager();
        blankFragment = new MainFragment(this);
        fragmentManager.beginTransaction().add(R.id.container, blankFragment, "first").addToBackStack("first").commit();
        getSupportFragmentManager().addOnBackStackChangedListener(onBackStackChangedListener);
        database = Database.Database_create(this);
        random = new Random();
        database.dao().load_fav_meaning(1).observe(this, new Observer<List<Entity>>() {
            @Override
            public void onChanged(List<Entity> list) {
                intialize_local_list(list);

            }
        });

    }

    void intialize_local_list(List<Entity> list) {
        list_local = list;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onclick(int adapterposition, final Entity entity, final View view) {
        switch (view.getId()) {
            case R.id.add_fav_meaning:
                if (entity.getFav_meaning() == 0) {
                    ((ImageView) view).setImageResource(R.drawable.likesfill);
                    entity.setFav_meaning(1);
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            database.dao().update(entity);
                        }
                    });
                    fav_meaning_count++;
                } else {
                    ((ImageView) view).setImageResource(R.drawable.likes);
                    fav_meaning_count--;
                    entity.setFav_meaning(0);
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            database.dao().update(entity);
                        }
                    });
                }
                break;
            case R.id.textview_meaning:

                try {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getSupportFragmentManager().beginTransaction().hide((Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("first")))).commit();
                getSupportFragmentManager().beginTransaction().add(R.id.container, Card_Fragment.newInstance((entity.getMeaning_of_word()), entity.getExample(), "normal", MainActivity.this), "second").addToBackStack("second").commit();
                break;
            case R.id.audio_play:


                if (view.getTag().equals(getResources().getString(R.string.audio_play_button))) {
                    ((ImageView) view).setImageResource(R.drawable.ic_volume_mute_black_24dp);
                    view.setTag("getIt");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textToSpeech.speak(entity.getName_of_meaning(), TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        Toast.makeText(this, "Cannot Play audio. Playing Audio needs higher android version", Toast.LENGTH_SHORT).show();
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Thread.sleep(1050);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ((ImageView) view).setImageResource(R.drawable.ic_volume_up_black_24dp);
                            view.setTag(getResources().getString(R.string.audio_play_button));


                            //textToSpeech=null;
                        }
                    }).start();


                } else {
                    ((ImageView) view).setImageResource(R.drawable.ic_volume_up_black_24dp);
                    view.setTag(getResources().getString(R.string.audio_play_button));
                }


                break;
            case R.id.fav_textview_meaning:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, Card_Fragment.newInstance(entity.getMeaning_of_word(), entity.getExample(), "normal", this)).addToBackStack(null).commit();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textToSpeech.stop();
        textToSpeech.shutdown();
        textToSpeech = null;
    }

    @Override
    public void onclick(String extra) {
        if (list_local.size() > 0) {
            switch (extra) {
                case "favourite":
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new Fav_fragment(list_local,this)).addToBackStack(null).commit();
                    break;
                case "card_mode":
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new Card_Fragment(list_local, "card", this)).commit();
                    break;
                case "music":getSupportFragmentManager().beginTransaction().replace(R.id.container,new Fav_fragment(list_local,this)).commit();


            }

        } else
            Toast.makeText(this, "You Have no Favourites", Toast.LENGTH_LONG).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
      //  getSupportFragmentManager().popBackStackImmediate("hey", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        getSupportFragmentManager().beginTransaction().show((Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("first")))).commit();

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            if (blankFragment.drawerLayout.isDrawerOpen(GravityCompat.START))
                blankFragment.drawerLayout.closeDrawer(GravityCompat.START);
            else
                finish();
            super.onBackPressed();

        } else
            super.onBackPressed();

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.ENGLISH);
        }

    }
}
