package com.example.meanings_downloader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.ImageButton;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meanings_downloader.Database.Database;
import com.example.meanings_downloader.Database.Entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;


public class MainFragment extends Fragment implements AbsListView.MultiChoiceModeListener {
    public static Entity share_id_of_entity;
    static ActionBarDrawerToggle actionBarDrawerToggle;
    static DrawerLayout drawerLayout;
    static StringBuilder parbuilder = new StringBuilder();
    static StringBuilder exmbuilder = new StringBuilder();
    static StringBuilder soubuilder = new StringBuilder();
    static StringBuilder namebuilder = new StringBuilder();
    LinearLayout bottom_bar_LinearLayout;
    LinearLayoutManager bottom_linearlayout;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    String url_string;
    String Json_raw;
    int i = 0;
    List<String> def = new ArrayList<>();
    List<String> exa = new ArrayList<>();
    List<String> par = new ArrayList<>();
    List<String> sou = new ArrayList<>();
    List<String> nam = new ArrayList<>();
    String definition;
    String example;
    String divide_parts_of_speech;
    String sound;
    int j = 0;
    List<String> remaining_words = new ArrayList<>();
    List<Entity> entities;
    StringBuilder defbuilder = new StringBuilder();
    List<Entity> string_resource_array = new ArrayList<>();
    ExecutorService executorService1 = Executors.newSingleThreadExecutor();
    private String entered_meaning;
    private Database database;
    private Adapter adapter;
    private ConstraintLayout rootlayout;
    private datepicker date;
    private Adapter.Clicklistener clicklistener;
    private Toolbar toolbar;
    private ProgressBar progressbar;
    private ArrayAdapter<String> arrayAdapter;
    private Scanner sc;
    private AutoCompleteTextView editText;
    private View v;
    private URL url;
    private ImageButton searchButton;
    private HttpURLConnection connection;
    private InputStream inputStream;
    private LiveData<List<Entity>> list;
    private Entity entity;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;


    MainFragment(Adapter.Clicklistener clicklistener, ActionBarDrawerToggle actionBarDrawerToggle, DrawerLayout drawerLayout) {
        this.clicklistener = clicklistener;
        MainFragment.actionBarDrawerToggle = actionBarDrawerToggle;
        MainFragment.drawerLayout = drawerLayout;
        //  this.entities=entities;
    }

    public MainFragment() {
    }

    public static void get(Entity entity1) {

        share_id_of_entity = entity1;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            initialize_views(inflater, container);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getDataFromDatabase();

        initialize_recyclerview();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String[] a1 = getActivity().getResources().getStringArray(R.array.remainingwords);
//
//                        File file2 = new File(getActivity().getApplicationContext().getExternalFilesDir(null), "sou.txt");
//                        StringBuilder soubuilder = new StringBuilder();
//
//                        try {
//                            file2.createNewFile();
//                            for (String s : sou) {
//                                soubuilder = soubuilder.append(s);
//                            }
//                            String x2 = null;
//                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                x2 = String.join("</item><item>", soubuilder.toString().trim().split("\\s\\s\\s"));
//                            }
//                            Log.d("hellog", x2);
//                            try (PrintWriter out = new PrintWriter(file.toString())) {
//                                out.println(x2);
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//
//
//                        File file3 = new File(getActivity().getApplicationContext().getExternalFilesDir(null), "exm.txt");
//                        StringBuilder exmbuilder = new StringBuilder();
//
//                        try {
//                            file3.createNewFile();
//                            for (String s : exa) {
//                                exmbuilder = exmbuilder.append(s);
//                            }
//                            String x3 = null;
//                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                x3 = String.join("</item><item>", exmbuilder.toString().trim().split("\\s\\s\\s"));
//                            }
//                            Log.d("hellog", x3);
//                            try (PrintWriter out = new PrintWriter(file.toString())) {
//                                out.println(x3);
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//        Log.d("meaningmm", String.valueOf(i));
//        executorService1.execute(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void run() {
//                Log.d("meaningmmm", String.valueOf(i));
//              for(String a: a1) {
//                  try {
//                      set_url(a);
//                  } catch (IOException e) {
//                      e.printStackTrace();
//                  } catch (JSONException e) {
//                      e.printStackTrace();
//                  }
//              }
//                }
//
//        });


    }


    private void initialize_recyclerview() {
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapter = new Adapter((MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        bottom_linearlayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


        recyclerView.setLayoutManager(linearLayoutManager);
        registerForContextMenu(recyclerView);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(adapter.simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        arrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_activated_1, adapter.list);
        editText.setAdapter(arrayAdapter);
    }

    private void initialize_clickListener() {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hide_keyboard();

                    try {
                        set_url("from_google");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });


    }


    private void initialize_views(LayoutInflater inflater, ViewGroup container) throws IOException, JSONException {
        v = inflater.inflate(R.layout.fragment_main, container, false);
        searchButton = v.findViewById(R.id.search);
        bottom_bar_LinearLayout = v.findViewById(R.id.bottom_recycler);
        rootlayout = v.findViewById(R.id.rootlayout_main_fragment);
        editText = v.findViewById(R.id.meaning);
        progressbar = v.findViewById(R.id.progress);
        recyclerView = v.findViewById(R.id.recycler_view);
        toolbar = v.findViewById(R.id.toolbar_main);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getTitle().toString()) {
            case "Share":
                start_share_activity(share_id_of_entity);


        }
        return super.onContextItemSelected(item);
    }

    public void start_share_activity(Entity share_id_of_entity) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        String msg = "Word: " + share_id_of_entity.getName_of_meaning().toUpperCase() + " (" + share_id_of_entity.getParts_of_speech() + ")" + " \n \n" + share_id_of_entity.getMeaning_of_word().substring(0, share_id_of_entity.getMeaning_of_word().length() - 5) + "\n\n Example: " + share_id_of_entity.getExample();
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        intent.setType("text/plain");
        if (getActivity() != null && isAdded())
            startActivity(intent);
    }

    private void hide_keyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = data.getData();
                intent.setDataAndType(data.getData(), "audio/mp3");
                //  intent.putExtra(Intent.EXTRA_STREAM,uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivity(intent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();
        initialize_clickListener();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try {
                    set_url("from_google");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hide_keyboard();


            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = Database.Database_create(getActivity().getApplicationContext());
        date = new datepicker();


        setHasOptionsMenu(true);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void set_url(final String extra) throws IOException, JSONException {

        //  entity = new Entity();

//        if (!editText.getText().toString().isEmpty()) {
//            entered_meaning = editText.getText().toString();
        // Log.d("enteredtt", he);
        entered_meaning = extra;

        url_string = "https://googledictionaryapi.eu-gb.mybluemix.net/?define=" + entered_meaning + "&lang=en";


//            Log.d("enteredtt", entered_meaning);
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    MainFragment.this.progressbar.setVisibility(View.VISIBLE);
//                }
//            });


        //  try {
        url = new URL(url_string);
//                Executors.newSingleThreadExecutor().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
        //  if (extra.equals("from_google"))
        retrieve(url);
        // else
        // create_new_connection(entered_meaning);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        //  }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void retrieve(URL se) throws IOException, JSONException {
        connection = (HttpURLConnection) se.openConnection();
        if (connection.getResponseCode() == (HttpURLConnection.HTTP_NOT_FOUND)) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No meaning found", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Log.d("enteredtt", "hellooo");
            inputStream = connection.getInputStream();
            if (inputStream != null) {

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                Json_raw = stringBuilder.toString();
            }


            String[] arr = JsonParser.parser(Json_raw);


            definition = arr[0];
            example = arr[1];
            divide_parts_of_speech = arr[2];
            sound = "";

//               String s = "";

//               if (divide_parts_of_speech.length > 1) {
//                   s = divide_parts_of_speech[1];
//               } else if (divide_parts_of_speech.length == 1)
//                   s = divide_parts_of_speech[0];


            if (!arr[3].isEmpty()) {
                sound = arr[3];
            } else
                sound = "";


//               getActivity().runOnUiThread(new Runnable() {
//                   @Override
//                   public void run() {
//                       progressbar.setVisibility(View.GONE);
//                       editText.setText("");
//                   }
//               });
            // if (definition != null) {
            Log.d("diss", definition);
            def.add(definition);
            par.add(divide_parts_of_speech);
            sou.add(sound);
            exa.add(example);
            nam.add(entered_meaning);
            Log.d("sizeof", String.valueOf(def.size()));
            //storeInDatabase(entered_meaning, definition, example, divide_parts_of_speech, sound);
            //   } else
//                   getActivity().runOnUiThread(new Runnable() {
//                       @Override
//                       public void run() {
//                           Toast.makeText(getContext(), Constants.NO_MEANING_FOUND, Toast.LENGTH_LONG).show();
//                       }
//                   });
            connection.disconnect();


        }

        executorService1.execute(new Runnable() {
            @Override
            public void run() {
                if (j == 0) {
                    Log.d("vastundi", "haa");
                    read();
                }
                j = 1;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void read() {
        Log.d("filesizeis", String.valueOf(def.size()));
        File file = new File(getActivity().getApplicationContext().getExternalFilesDir(null), "deff.txt");


        for (String s : def) {
            defbuilder = defbuilder.append(s).append("ssss");
        }
        String x = null;
        Log.d("meaningdef", defbuilder.toString());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Log.d("meaningdef", defbuilder.toString());
            x = String.join("</item><item>", defbuilder.toString().split("ssss"));

        }
        Log.d("hellodef", x);
        //  Log.d("meaningmm", "camehere");
        try (PrintStream out = new PrintStream(file)) {
            out.print(x);
            // defbuilder=null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


//                    try {
//                        create_new_connection(a2[i]);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }


        File file1 = new File(getActivity().getApplicationContext().getExternalFilesDir(null), "par.txt");


        for (String s1 : par) {
            parbuilder = parbuilder.append(s1).append("\n");
        }
        Log.d("meaningmmparts", parbuilder.toString());
        String x1 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            x1 = String.join("</item><item>", parbuilder.toString().trim().split("\n"));
        }
        Log.d("helloparts", x1);
        try (PrintStream out = new PrintStream(file1.toString())) {
            out.append(x1);
            // parbuilder=null;
        } catch (IOException e) {
            e.printStackTrace();
        }


        File file2 = new File(getActivity().getApplicationContext().getExternalFilesDir(null), "sou.txt");


        for (String s2 : sou) {
            soubuilder = soubuilder.append(s2).append("\n");
        }
        Log.d("meaningmmsou", soubuilder.toString());
        String x2 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            x2 = String.join("</item><item>", soubuilder.toString().trim().split("\n"));
        }
        Log.d("hellosou", x2);
        try (PrintStream out = new PrintStream(file2.toString())) {
            out.append(x2);
            // soubuilder=null;
        } catch (IOException e) {
            e.printStackTrace();
        }


        File file3 = new File(getActivity().getApplicationContext().getExternalFilesDir(null), "exm.txt");


        for (String s3 : exa) {
            exmbuilder = exmbuilder.append(s3).append("\n");
        }
        Log.d("meaningmmexm", exmbuilder.toString());
        String x3 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            x3 = String.join("</item><item>", exmbuilder.toString().trim().split("\n"));
        }
        Log.d("helloexam", x3);
        try (PrintStream out = new PrintStream(file3.toString())) {
            out.append(x3);
            //  exmbuilder=null;
        } catch (IOException e) {
            e.printStackTrace();
        }


        File file4 = new File(getActivity().getApplicationContext().getExternalFilesDir(null), "name.txt");


        for (String s4 : nam) {
            namebuilder = namebuilder.append(s4).append("\n");
        }
        Log.d("meaningmmnam", namebuilder.toString());
        String x4 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            x4 = String.join("</item><item>", namebuilder.toString().trim().split("\n"));
        }
        Log.d("helloname", x4);
        try (PrintStream out = new PrintStream(file4.toString())) {
            out.append(x4);
            //    namebuilder=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void storeInDatabase(String name_of_word, String str, String example, String parts_of_speech, String sound) {
        Log.d("timesss", str);
        Entity entity = new Entity();
        entity.setName_of_meaning(name_of_word);
        entity.setMeaning_of_word(str);
        entity.setUserTypedData("");
        // entity.setName_of_meaning(entered_meaning);
        entity.setParts_of_speech(parts_of_speech);
        entity.setSound(sound);
        entity.setExample(example);
        entity.setFav_meaning(0);
        entity.setLearned_words(false);
        entity.setRepeat_words(false);


        Long saved_id = database.dao().insert(entity);
        if (saved_id != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.smoothScrollToPosition(0);
                        }
                    }, 200);

                }
            });
        }


    }


    private void getDataFromDatabase() {
        list = database.dao().load_all_data();
        list.observe(getViewLifecycleOwner(), new Observer<List<Entity>>() {
            @Override
            public void onChanged(final List<Entity> entities) {
                Collections.reverse(entities);
                MainActivity.set_total_words(entities);
                adapter.submitList(entities);

            }
        });


    }


    void create_new_connection(String entered_meaning) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("https://wordsapiv1.p.rapidapi.com/words/" + entered_meaning)
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "ef67aefe13msh419b5d28d535d0cp131b8bjsnfebcf461a84e")
                .build();

        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();
        Log.d("defff", jsonData);
        JSONObject Jobject = new JSONObject(jsonData);
        JSONArray Jarray = Jobject.getJSONArray("results");
        String definition = "";
        String parts_of_speech = "";
        String sound;
        String example = "";
        JSONArray examplearray;

        for (int i = 0; i < Jarray.length(); i++) {
            JSONObject object = Jarray.getJSONObject(i);
            //  Log.d("deff",parts_of_speech+example);
            // Log.d("ivale", String.valueOf(i));
            if (!object.isNull("examples")) {
                Log.d("ivale", String.valueOf(i));
                definition = object.getString("definition");
                parts_of_speech = object.getString("partOfSpeech");
                Log.d("defff", parts_of_speech + definition);
                examplearray = object.getJSONArray("examples");
                example = (String) examplearray.get(0);
                JSONObject pronounc = Jobject.getJSONObject("pronunciation");

                Log.d("meaningmm", "words is" + Jobject.getString("word"));
                if (definition != null)
                    storeInDatabase(Jobject.getString("word"), definition, example, parts_of_speech, pronounc.getString("all"));
                else
                    Toast.makeText(getContext(), "Nomeaning found", Toast.LENGTH_SHORT).show();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainFragment.this.progressbar.setVisibility(View.GONE);
                    }
                });

                def.add(definition);
                par.add(parts_of_speech);
                sou.add(pronounc.getString("all"));
                exa.add(example);
                Log.d("meaningmm", "camehereeeee");

                break;
            }


        }


    }


    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.contextual_menu, menu);
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
