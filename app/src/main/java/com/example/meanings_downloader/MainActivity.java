package com.example.meanings_downloader;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.Toast;

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
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity implements Adapter.Clicklistener, TextToSpeech.OnInitListener {

    static List<Entity> fav_words;
    static List<Entity> learned_words;
    static List<Entity> repeat_words;
    static List<Entity> words_learned_as_sets ;
    static List<Entity> total_words;
    static List<SaveSpokenWordsEntity> saveSpokenWords;

    static {
        fav_words = new ArrayList<>();
        learned_words = new ArrayList<>();
        repeat_words = new ArrayList<>();
    }

    FrameLayout container;
    FragmentManager fragmentManager;
    MainFragment blankFragment;
    int fav_meaning_count = 0;
    Database database;
    Entity entity;
    ClipboardManager clipboardManager;
    TextToSpeech textToSpeech;
    InputMethodManager inputMethodManager;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    ViewGroup bottom_toolbar;
    int i;
    List<Entity> set_entities = new ArrayList<>();
    private ImageButton homeButton;
    private ImageButton learnButton;
    private ImageButton cameraButton;
    private ImageButton speakButton;
    private ImageButton Fav_button;
    private NavigationView navigationView;
   static  Setnum_entity  setnum_entity=new Setnum_entity();

    static void sedata(List<Entity> entities) {

      // words_learned_as_sets=new ArrayList<>();
        words_learned_as_sets = entities;
        Log.d("ggg", " " + words_learned_as_sets.size());
    }

    static void set_total_words(List<Entity> total_words) {
        MainActivity.total_words = total_words;
        Log.d("obtainedtotal", String.valueOf(total_words.size()));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize_views();


        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        database = Database.Database_create(this);
        load_observers();

        SharedPreferences preferences = getSharedPreferences("firstrun", MODE_PRIVATE);
        int build_num = BuildConfig.VERSION_CODE;
        if (preferences.getInt("code_num", build_num) == build_num) {

//
            final String definition_array[] = getResources().getStringArray(R.array.definitions);
            final String parts_of_speech_array[] = getResources().getStringArray(R.array.parts_of_speech);
            final String sound_array[] = getResources().getStringArray(R.array.sound);
            final String example_array[] = getResources().getStringArray(R.array.example);
            final String name_of_meaning_array[] = getResources().getStringArray(R.array.name_of_meanings);
            Log.d("sizeeedef", String.valueOf(definition_array.length));
            Log.d("sizeeedef", String.valueOf(definition_array[501]));
            Log.d("sizeeepar", String.valueOf(parts_of_speech_array.length));
            Log.d("sizeeepar", String.valueOf(parts_of_speech_array[501]));
            Log.d("sizeeesou", String.valueOf(sound_array.length));
            Log.d("sizeeesou", String.valueOf(sound_array[501]));
            Log.d("sizeeeexa", String.valueOf(example_array.length));
            Log.d("sizeeeexa", String.valueOf(example_array[501]));
            Log.d("sizeeenam", String.valueOf(name_of_meaning_array.length));
            Log.d("sizeeenam", String.valueOf(name_of_meaning_array[501]));

//       for (int j=0;j<20;j++){
//           Log.d("villls",name_of_meaning_array[j]);
//       }

            for (int s = 0; s < example_array.length - 1; s++) {
                if (parts_of_speech_array[s].equals("")) {
                    Log.d("emptyexis", String.valueOf(s));
                }
            }


            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    for (i = 0; i < definition_array.length; i++) {

                        entity = new Entity();

                        setnum_entity.setSet_num_at_present(0);
                        entity.setName_of_meaning(name_of_meaning_array[i]);
                        Log.d("villld", name_of_meaning_array[i]);

                        entity.setSound(sound_array[i]);
                        entity.setParts_of_speech(parts_of_speech_array[i]);
                        entity.setMeaning_of_word(definition_array[i]);
                        entity.setExample(example_array[i]);
                        entity.setFav_meaning(0);
                        entity.setLearned_words(false);
                        entity.setRepeat_words(false);
                        entity.setUserTypedData("");
                        entity.setSet_number(0);
                        entity.setLearnedset(false);
                        database.dao().insert(entity);
                        database.dao().insert_setnumat_present(setnum_entity);
                    }
                    Log.d("villls", String.valueOf(i));
                }
            });
        }

        preferences.edit().putInt("code_num", -1).apply();
//adapter.submitList(string_resource_array);


        // Log.d("lennnn",String.valueOf("" + " j is "+ j +"def is "+ definition_array.length));
        if (savedInstanceState == null) {


            fragmentManager = getSupportFragmentManager();

            blankFragment = new MainFragment(this, actionBarDrawerToggle, drawerLayout);
            fragmentManager.beginTransaction().add(R.id.container, blankFragment, Constants.ADD_MAINADAPTER_TO_BACKSTACK).addToBackStack(Constants.ADD_MAINADAPTER_TO_BACKSTACK).commit();


        }


//        Request request = new Request.Builder()
//                .url("https://wordsapiv1.p.rapidapi.com/words/hatchback")
//                .get()
//                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
//                .addHeader("x-rapidapi-key", "ef67aefe13msh419b5d28d535d0cp131b8bjsnfebcf461a84e")
//                .build();
//        Log.d("heyyy",request.toString());
//        File appSpecificExternalDir = new File(getApplicationContext().getExternalFilesDir(null), "meanings.txt");
//Log.d("stringbuild",appSpecificExternalDir.toString());
//        try {
////            FileInputStream inputStream=new FileInputStream(appSpecificExternalDir);
////            InputStreamReader fileOutputStream=new InputStreamReader(inputStream);
////            BufferedReader bufferedReader=new BufferedReader(fileOutputStream);
//            Scanner sc=new Scanner(appSpecificExternalDir);
//            List<String> stringList=new ArrayList<>();
//            sc.useDelimiter(" ");
//            StringBuilder stringBuilder=new StringBuilder();
//            String s="";
//            while (sc.hasNext()){
//                s=sc.next();
//                stringList.add(s);
//                stringBuilder=stringBuilder.append(s);
//            }
//            StringBuilder st=new StringBuilder();
//            for (String m:stringList
//                 ) {
//                st=st.append(m);
//
//
//            }
//            File file=new File(getApplicationContext().getExternalFilesDir(null),"all_meanings.txt");
//            String x = String.join("</item><item>",st.toString().trim().split("\\s\\s\\s"));
//            Log.d("hellog",x);
//            try (PrintWriter out = new PrintWriter(file.toString())) {
//                out.println(x);
//            }
//
//
//
//
//            //  file.createNewFile();
//
//          //  FileOutputStream fileOutputStream=new FileOutputStream(file);
//           // BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(fileOutputStream,2048);
//           for(int i=0;i<stringList.size();i++){
//               st=st.append(stringList.get(i));
//           }
//           String b=st.toString();
//           String g=String.join("b",b.trim().split(" \\s\\s\\s"));
//           URI uri=URI.create((getApplicationContext().getExternalFilesDir(null).toString()));
//
//          // bufferedOutputStream.write( String.join("</item><item>",b.split(" ")).getBytes());
//            Log.d("stringbuild",stringBuilder.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    private void load_observers() {
        database.dao().load_fav_meaning(1).observe(this, new Observer<List<Entity>>() {
            @Override
            public void onChanged(List<Entity> list) {
                intialize_favwords_local_list(list);
            }
        });
        database.dao().load_learned_words(true).observe(this, new Observer<List<Entity>>() {
            @Override
            public void onChanged(List<Entity> entities) {
                initialize_learned_words(entities);
            }
        });
        database.dao().load_repeat_words(true).observe(this, new Observer<List<Entity>>() {
            @Override
            public void onChanged(List<Entity> entities) {
                initialize_repeat_words(entities);
            }
        });
        database.dao().load_spoken_words().observe(this, new Observer<List<SaveSpokenWordsEntity>>() {
            @Override
            public void onChanged(List<SaveSpokenWordsEntity> saveSpokenWords) {
                initialize_saved_spoken_words(saveSpokenWords);
            }
        });


    }

    void initialize_saved_spoken_words(List<SaveSpokenWordsEntity> saveSpokenWords) {
        MainActivity.saveSpokenWords = saveSpokenWords;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("not_empty", "not_empty");
        super.onSaveInstanceState(outState);
    }

    void initialize_learned_words(List<Entity> entities) {
        MainActivity.learned_words = entities;
    }

    void initialize_repeat_words(List<Entity> entities) {
        MainActivity.repeat_words = entities;
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
        // cameraButton = findViewById(R.id.camera_icon);
        speakButton = findViewById(R.id.speak_icon);
        Fav_button = findViewById(R.id.fav_icon);
        bottom_toolbar = findViewById(R.id.bottombar_layout);
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

                getSupportFragmentManager().popBackStackImmediate(Constants.ADD_MAINADAPTER_TO_BACKSTACK, 0);

            }
        });
        Fav_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                highligt_bottom_icons(Fav_button);
                if (fav_words.size() > 0)
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new Fav_fragment(fav_words, MainActivity.this, "")).addToBackStack(null).commit();
                else
                    Toast.makeText(getApplicationContext(), Constants.YOU_HAVE_NO_FAVOURITES, Toast.LENGTH_LONG).show();
            }
        });
        speakButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                highligt_bottom_icons(speakButton);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new Speechrecognition()).addToBackStack(null).commit();


            }
        });

//        cameraButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                highligt_bottom_icons(cameraButton);
//
//            }
//        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.difficult_words) {
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
                if (item.getItemId() == R.id.saved_speech) {
                    // Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    // startActivity(intent);
//                    Intent intent1 = new Intent(Intent.ACTION_PICK);
//                    intent1.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent1, 2);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new Fav_fragment(saveSpokenWords, MainActivity.this, "speech")).addToBackStack(null).commit();

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
                //  cameraButton.setImageResource(R.drawable.camera_icon_light);
                speakButton.setImageResource(R.drawable.voice_icon_light);
                Fav_button.setImageResource(R.drawable.likes);
                break;

            case R.id.learn_icon:
                homeButton.setImageResource(R.drawable.home_icon_light);
                learnButton.setImageResource(R.drawable.learn_icon_dark);
                // cameraButton.setImageResource(R.drawable.camera_icon_light);
                speakButton.setImageResource(R.drawable.voice_icon_light);
                Fav_button.setImageResource(R.drawable.likes);
                break;
//            case R.id.camera_icon:
//                homeButton.setImageResource(R.drawable.home_icon_light);
//                learnButton.setImageResource(R.drawable.learn_icon_light);
//                cameraButton.setImageResource(R.drawable.camera_icon_dark);
//                speakButton.setImageResource(R.drawable.voice_icon_light);
//                Fav_button.setImageResource(R.drawable.likes);
//                break;
            case R.id.speak_icon:
                homeButton.setImageResource(R.drawable.home_icon_light);
                learnButton.setImageResource(R.drawable.learn_icon_light);
                // cameraButton.setImageResource(R.drawable.camera_icon_light);
                speakButton.setImageResource(R.drawable.voice_icon_dark);
                Fav_button.setImageResource(R.drawable.likes);
                break;
            case R.id.fav_icon:
                homeButton.setImageResource(R.drawable.home_icon_light);
                learnButton.setImageResource(R.drawable.learn_icon_light);
                //    cameraButton.setImageResource(R.drawable.camera_icon_light);
                speakButton.setImageResource(R.drawable.voice_icon_light);
                Fav_button.setImageResource(R.drawable.likesfill);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);

            else drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void intialize_favwords_local_list(List<Entity> list) {
        fav_words = list;
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
                getSupportFragmentManager().beginTransaction().replace(R.id.container, Card_Fragment.newInstance(entity.getName_of_meaning(), entity.getMeaning_of_word(), entity.getExample(), entity.getParts_of_speech(), entity.getSound(), Constants.MAIN_ADAPTER_TO_CARD_VIEW, this, entity)).addToBackStack(null).commit();
                break;
            case R.id.rootlayout_main_fragment:
                open_cardview_from_mainFRagment(entity);


        }


    }


    public void open_cardview_from_mainFRagment(Entity entity) {
        try {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //getSupportFragmentManager().beginTransaction().hide((getSupportFragmentManager().findFragmentByTag(Constants.ADD_MAINADAPTER_TO_BACKSTACK))).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, Card_Fragment.newInstance(entity.getName_of_meaning(), entity.getMeaning_of_word(), entity.getExample(), entity.getParts_of_speech(), entity.getSound(), Constants.MAIN_ADAPTER_TO_CARD_VIEW, MainActivity.this, entity), "second").addToBackStack("second").commit();
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
    public void onclick(String extra, String category, final int num_of_words) {
        switch (extra) {
            case Constants.FROM_FAVOURITE_TO_CARD_VIEW:
                Log.d("lennnn", " " + words_learned_as_sets.size());

                if (words_learned_as_sets.size() > 0) {

                    // actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new Card_Fragment(words_learned_as_sets, Constants.FROM_FAVOURITE_TO_CARD_VIEW, this, category, num_of_words, total_words,MainActivity.setnum_entity)).addToBackStack(null).commit();
                    bottom_toolbar.setVisibility(View.GONE);
                }
                break;

            case Constants.LEARN_WORDS:
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        set_entities.clear();
                        set_entities = database.dao().load_set_partticular_data(0);
                        Log.d("numberofsee", String.valueOf(set_entities.size()));
                        if (set_entities.size() > 0) {

                            // actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new Fav_fragment(set_entities, MainActivity.this, "")).addToBackStack(null).commit();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    bottom_toolbar.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), Constants.YOU_HAVE_NO_LEARNWORDS, Toast.LENGTH_LONG).show();
                        }
                    }
                });


                break;
            case Constants.REPEAT_WORDS:


                if (repeat_words.size() > 0) {

                    // actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new Fav_fragment(repeat_words, this, "")).addToBackStack(null).commit();
                    bottom_toolbar.setVisibility(View.GONE);


                } else
                    Toast.makeText(this, Constants.YOU_HAVE_NO_REPAETWORDS, Toast.LENGTH_LONG).show();
                break;
            case "from_learnmode_to_setSelection":
                getSupportFragmentManager().beginTransaction().replace(R.id.container, Select_sets.newInstance(category, String.valueOf(num_of_words), MainActivity.this)).addToBackStack(null).commit();

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        if (!bottom_toolbar.isFocused()) {
            bottom_toolbar.setVisibility(View.VISIBLE);
        }
        //  getSupportFragmentManager().popBackStackImmediate("hey", FragmentManager.POP_BACK_STACK_INCLUSIVE);

//getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag(Constants.LEARN_FRAGMENT)).commit();
        //   getSupportFragmentManager().beginTransaction().show((Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(Constants.ADD_MAINADAPTER_TO_BACKSTACK)))).commit();

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {

            finish();

        } else
            super.onBackPressed();

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.ENGLISH);
        } else {
            Toast.makeText(this, "Could not initialize Text to Speech", Toast.LENGTH_SHORT).show();
        }


    }
}
