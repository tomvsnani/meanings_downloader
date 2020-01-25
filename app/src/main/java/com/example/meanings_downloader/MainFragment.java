package com.example.meanings_downloader;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
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
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import java.util.concurrent.Executors;

import static android.app.Activity.RESULT_OK;


public class MainFragment extends Fragment implements AbsListView.MultiChoiceModeListener {
    public static Entity share_id_of_entity;


    LinearLayout bottom_bar_LinearLayout;
    LinearLayoutManager bottom_linearlayout;
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
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;


    MainFragment(Adapter.Clicklistener clicklistener,ActionBarDrawerToggle actionBarDrawerToggle,DrawerLayout drawerLayout) {
        this.clicklistener = clicklistener;
        this.actionBarDrawerToggle=actionBarDrawerToggle;
        this.drawerLayout=drawerLayout;
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

        initialize_views(inflater, container);


        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getDataFromDatabase();

        initialize_recyclerview();
        return v;
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
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hide_keyboard();
                    entity = new Entity();
                    set_url();
                }
                return false;
            }
        });



    }


    private void initialize_views(LayoutInflater inflater, ViewGroup container) {
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
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
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
                entity = new Entity();
                set_url();
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


    private void set_url() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                if (!editText.getText().toString().isEmpty()) {
                    entered_meaning = editText.getText().toString();
                    String url_string = "https://googledictionaryapi.eu-gb.mybluemix.net/?define=" + entered_meaning + "&lang=en";
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainFragment.this.progressbar.setVisibility(View.VISIBLE);
                        }
                    });
                    try {
                        url = new URL(url_string);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        retrieve(url);
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
                    Toast.makeText(getContext(), Constants.NO_MEANING_FOUND, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            inputStream = connection.getInputStream();
            sc = new Scanner(inputStream);
            sc.useDelimiter("\\A");

            StringBuilder stringBuilder = new StringBuilder();
            final String Json_raw = sc.next();
            String[] arr = JsonParser.parser(Json_raw);
            assert arr != null;
            String definition = arr[0];
            String example = arr[1];
            final String[] divide_parts_of_speech = arr[2].split(" ");
            String sound = "";

            String s = "";
            for (int i = 0; i < 4; i++) {
                if (divide_parts_of_speech.length > 1) {
                    s = divide_parts_of_speech[1];
                } else if (divide_parts_of_speech.length == 1)
                    s = divide_parts_of_speech[0];
                stringBuilder = stringBuilder.append(s.charAt(i));
            }


            if (!arr[3].isEmpty()) {
                sound = arr[3];
            }


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressbar.setVisibility(View.GONE);
                    editText.setText("");
                }
            });
            if (definition != null) {
                storeInDatabase(definition, example, stringBuilder.toString(), sound);
            } else
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), Constants.NO_MEANING_FOUND, Toast.LENGTH_LONG).show();
                    }
                });
            connection.disconnect();
        }
    }


    private void storeInDatabase(String str, String example, String parts_of_speech, String sound) {
        entity.setMeaning_of_word(str);
        entity.setUserTypedData("");
        entity.setParts_of_speech(parts_of_speech);
        entity.setSound(sound);
        entity.setExample(example);
        entity.setFav_meaning(0);
        entity.setName_of_meaning(entered_meaning);

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Long saved_id = database.dao().insert(entity);
                if (saved_id != null) {
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        });
    }

    private void getDataFromDatabase() {
        list = database.dao().load_all_data();
        list.observe(getViewLifecycleOwner(), new Observer<List<Entity>>() {
            @Override
            public void onChanged(final List<Entity> entities) {
                Collections.reverse(entities);
                adapter.submitList(entities);

            }
        });


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



