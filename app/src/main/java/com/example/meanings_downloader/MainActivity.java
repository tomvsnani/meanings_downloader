package com.example.meanings_downloader;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import com.example.meanings_downloader.Database.Database;
import com.example.meanings_downloader.Database.Entity;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
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
    ClipboardManager clipboardManager;
    TextToSpeech textToSpeech;
    InputMethodManager inputMethodManager;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    private ImageButton homeButton;
    private ImageButton learnButton;
    private ImageButton cameraButton;
    private ImageButton speakButton;
    private ImageButton Fav_button;
    private NavigationView navigationView;
   ViewGroup bottom_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize_views();
        fragmentManager = getSupportFragmentManager();
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        blankFragment = new MainFragment(this, actionBarDrawerToggle, drawerLayout);
        fragmentManager.beginTransaction().add(R.id.container, blankFragment, Constants.ADD_MAINADAPTER_TO_BACKSTACK).addToBackStack(Constants.ADD_MAINADAPTER_TO_BACKSTACK).commit();
        Log.d("okay", "h");
        database = Database.Database_create(this);
        database.dao().load_fav_meaning(1).observe(this, new Observer<List<Entity>>() {
            @Override
            public void onChanged(List<Entity> list) {
                intialize_local_list(list);

            }
        });

    }

    private void initialize_views() {
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.opened, R.string.closed);
        textToSpeech = new TextToSpeech(getApplicationContext(), MainActivity.this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        container = findViewById(R.id.container);
        homeButton = findViewById(R.id.home_icon);
        learnButton = findViewById(R.id.learn_icon);
        cameraButton = findViewById(R.id.camera_icon);
        speakButton = findViewById(R.id.speak_icon);
        Fav_button = findViewById(R.id.fav_icon);
        bottom_toolbar=findViewById(R.id.bottombar_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();


        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                if (clipboardManager.hasPrimaryClip()) {
                    ClipData clipData = clipboardManager.getPrimaryClip();
                    String s = clipData.getItemAt(0).getText().toString();

                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new Learning_mode(MainActivity.this), Constants.LEARN_FRAGMENT).addToBackStack(null).commit();

                highligt_bottom_icons(learnButton);
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highligt_bottom_icons(homeButton);

             //   getSupportFragmentManager().popBackStackImmediate(Constants.ADD_MAINADAPTER_TO_BACKSTACK, 0);

            }
        });
        Fav_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("learnt", "learned");
                highligt_bottom_icons(Fav_button);
                if (list_local.size() > 0)
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new Fav_fragment(list_local, MainActivity.this)).addToBackStack(null).commit();
                else
                    Toast.makeText(getApplicationContext(), "You have no favourites", Toast.LENGTH_LONG).show();
            }
        });
        speakButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("learnt", "learned");
                highligt_bottom_icons(speakButton);

            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                highligt_bottom_icons(cameraButton);

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.date) {
                    //  date.setCancelable(false);
                    // date.show(getFragmentManager(), "Date");
                    final test test = new test();
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                test.create(getApplicationContext());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                if (item.getItemId() == R.id.random) {
                    Log.d("nothing", "random");
                    // Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    // startActivity(intent);
                    Intent intent1 = new Intent(Intent.ACTION_PICK);
                    intent1.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent1, 2);
                }
                return true;
            }
        });

    }

    public void highligt_bottom_icons(ImageButton b) {
        switch (b.getId()) {

            case R.id.home_icon:
                homeButton.setImageResource(R.drawable.home_icon_dark);
                learnButton.setImageResource(R.drawable.learn_icon_light);
                cameraButton.setImageResource(R.drawable.camera_icon_light);
                speakButton.setImageResource(R.drawable.voice_icon_light);
                Fav_button.setImageResource(R.drawable.likes);
                break;

            case R.id.learn_icon:
                homeButton.setImageResource(R.drawable.home_icon_light);
                learnButton.setImageResource(R.drawable.learn_icon_dark);
                cameraButton.setImageResource(R.drawable.camera_icon_light);
                speakButton.setImageResource(R.drawable.voice_icon_light);
                Fav_button.setImageResource(R.drawable.likes);
                break;
            case R.id.camera_icon:
                homeButton.setImageResource(R.drawable.home_icon_light);
                learnButton.setImageResource(R.drawable.learn_icon_light);
                cameraButton.setImageResource(R.drawable.camera_icon_dark);
                speakButton.setImageResource(R.drawable.voice_icon_light);
                Fav_button.setImageResource(R.drawable.likes);
                break;
            case R.id.speak_icon:
                homeButton.setImageResource(R.drawable.home_icon_light);
                learnButton.setImageResource(R.drawable.learn_icon_light);
                cameraButton.setImageResource(R.drawable.camera_icon_light);
                speakButton.setImageResource(R.drawable.voice_icon_dark);
                Fav_button.setImageResource(R.drawable.likes);
                break;
            case R.id.fav_icon:
                homeButton.setImageResource(R.drawable.home_icon_light);
                learnButton.setImageResource(R.drawable.learn_icon_light);
                cameraButton.setImageResource(R.drawable.camera_icon_light);
                speakButton.setImageResource(R.drawable.voice_icon_light);
                Fav_button.setImageResource(R.drawable.likesfill);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("isactivated", "no!!");
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);

            else drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            case R.id.share:


                final Intent intent = new Intent(Intent.ACTION_SEND);
                String msg = "Word: " + entity.getName_of_meaning().toUpperCase() + " (" + entity.getParts_of_speech() + ")" + " \n \n" + entity.getMeaning_of_word().substring(0, entity.getMeaning_of_word().length() - 5) + "\n\n Example: " + entity.getExample();
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                intent.setType("text/plain");

                startActivity(intent);

                break;
            case R.id.audio_play:


                if (view.getTag().equals(getResources().getString(R.string.audio_play_button))) {
                    ((ImageView) view).setImageResource(R.drawable.ic_volume_mute_black_24dp);
                    view.setTag("getIt");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textToSpeech.speak(entity.getName_of_meaning(), TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        Toast.makeText(this, Constants.CANNOT_PLAY_AUDIO, Toast.LENGTH_SHORT).show();
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

                        }
                    }).start();


                } else {
                    ((ImageView) view).setImageResource(R.drawable.ic_volume_up_black_24dp);
                    view.setTag(getResources().getString(R.string.audio_play_button));
                }


                break;
            case R.id.fav_textview_meaning:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, Card_Fragment.newInstance(entity.getMeaning_of_word(), entity.getExample(), Constants.MAIN_ADAPTER_TO_CARD_VIEW, this, entity)).addToBackStack(null).commit();
                break;
            default:
                open_cardview_from_mainFRagment(entity);


        }


    }


    public void open_cardview_from_mainFRagment(Entity entity) {
        try {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction().hide((getSupportFragmentManager().findFragmentByTag(Constants.ADD_MAINADAPTER_TO_BACKSTACK))).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.container, Card_Fragment.newInstance((entity.getMeaning_of_word()), entity.getExample(), Constants.MAIN_ADAPTER_TO_CARD_VIEW, MainActivity.this, entity), "second").addToBackStack("second").commit();
        return;
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
            if (Constants.FROM_FAVOURITE_TO_CARD_VIEW.equals(extra)) {
               // actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new Card_Fragment(list_local, Constants.FROM_FAVOURITE_TO_CARD_VIEW, this)).commit();
          bottom_toolbar.setVisibility(View.GONE);

            }

        } else
            Toast.makeText(this, Constants.YOU_HAVE_NO_FAVOURITES, Toast.LENGTH_LONG).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        if(!bottom_toolbar.isFocused()){
            bottom_toolbar.setVisibility(View.VISIBLE);
        }
        //  getSupportFragmentManager().popBackStackImmediate("hey", FragmentManager.POP_BACK_STACK_INCLUSIVE);

//getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag(Constants.LEARN_FRAGMENT)).commit();
        getSupportFragmentManager().beginTransaction().show((Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(Constants.ADD_MAINADAPTER_TO_BACKSTACK)))).commit();

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {

            finish();

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
