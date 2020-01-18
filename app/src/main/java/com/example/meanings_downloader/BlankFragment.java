package com.example.meanings_downloader;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import java.util.concurrent.Executors;

import static android.app.Activity.RESULT_OK;


public class BlankFragment extends Fragment {
    String entered_meaning;
    Database database;
    Adapter adapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    datepicker date;
    Adapter.Clicklistener clicklistener;
    Toolbar toolbar;
    ProgressBar progressbar;
    String s = "";
    private Scanner sc;
    private EditText editText;
    private View v;
    private URL url;
    private ImageButton button;
    private HttpURLConnection connection;
    private InputStream inputStream;
    private LiveData<List<Entity>> list;
    private Entity entity;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;


    public BlankFragment(Adapter.Clicklistener clicklistener) {
        this.clicklistener = clicklistener;
    }

    public BlankFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_blank, container, false);
        button = v.findViewById(R.id.search);
        editText = v.findViewById(R.id.meaning);
        progressbar = v.findViewById(R.id.progress);
        recyclerView = v.findViewById(R.id.recycler_view);
        drawerLayout = v.findViewById(R.id.drawer);
        navigationView = v.findViewById(R.id.navigation);
        toolbar = v.findViewById(R.id.toolbar_main);
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.opened, R.string.closed);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.syncState();

//actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.RED);

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
                                test.create(getContext());
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

                if (item.getItemId() == R.id.music) {
//                    Intent intent = new Intent(getActivity(), Music_Activity.class);
//                    startActivity(intent);
                    clicklistener.onclick();
                }

                return true;
            }
        });

        getDataFromDatabase();
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapter = new Adapter(this.getContext(), recyclerView, (MainActivity) getActivity(), progressbar);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(adapter.simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("givenuri", "a1");

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Log.d("givenuri", data.getData().toString());
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


        button.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try {

                    entity = new Entity();
                    set_url();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("touchedhere", "touch");
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            Log.d("touchedhere", "touched");
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            else drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = Database.Database_create(getActivity().getApplicationContext());
        date = new datepicker();

        setHasOptionsMenu(true);


    }


    public void set_url() throws IOException {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                if (!editText.getText().toString().isEmpty()) {

                    Log.d("entered", "enter");
                    entered_meaning = editText.getText().toString();
                    String url_string = "https://googledictionaryapi.eu-gb.mybluemix.net/?define=" + entered_meaning + "&lang=en";
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BlankFragment.this.progressbar.setVisibility(View.VISIBLE);

                        }
                    });

                    try {
                        url = new URL(url_string);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {

                        retrieve(url);
                        if (!s.equals("entered")) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressbar.setVisibility(View.GONE);
                                }
                            });
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void retrieve(URL se) throws IOException, JSONException {

        connection = (HttpURLConnection) se.openConnection();
        if (connection.getResponseCode() == (HttpURLConnection.HTTP_NOT_FOUND)) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No meaning found. Please Check the spelling", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            inputStream = connection.getInputStream();
            sc = new Scanner(inputStream);
            sc.useDelimiter("\\A");

            final String Json_raw = sc.next();
            final String definition = JsonParser.parser(Json_raw);
            final String example = JsonParser.example();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressbar.setVisibility(View.GONE);
                }
            });
            if (definition != null) {
                storeInDatabase(definition, example);
            } else
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "No meaning found. Please Check the spelling", Toast.LENGTH_LONG).show();
                    }
                });


            connection.disconnect();
        }
    }


    public void storeInDatabase(String str, String example) {

        entity.setMeaning_of_word(str);
        entity.setExample(example);
        entity.setFav_meaning(0);
        entity.setName_of_meaning(entered_meaning);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Long i = database.dao().insert(entity);
                if (i != null) {
                    Log.d("recyclerview", String.valueOf(i));
                    recyclerView.smoothScrollToPosition(0);
                }


            }
        });

    }

    public LiveData<List<Entity>> getDataFromDatabase() {

        list = database.dao().load_all_data();
        list.observe(getViewLifecycleOwner(), new Observer<List<Entity>>() {
            @Override
            public void onChanged(final List<Entity> entities) {

                Collections.reverse(entities);


                adapter.submitList(entities);


            }
        });


        return list;
    }


}



