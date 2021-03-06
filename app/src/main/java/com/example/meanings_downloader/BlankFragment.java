package com.example.meanings_downloader;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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


public class BlankFragment extends Fragment {
    String entered_meaning;
    Database database;
    Adapter adapter;
    private Scanner sc;
    private EditText editText;
    private View v;
    private URL url;
    private Button button;
    private HttpURLConnection connection;
    private TextView text;
    private InputStream inputStream;
    private TextView exampleText;
    private LiveData<List<Entity>> list;
    private Entity entity;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;


    public BlankFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_blank, container, false);
        button = v.findViewById(R.id.search);
        //text = v.findViewById(R.id.text);
        editText = v.findViewById(R.id.meaning);
        //exampleText = v.findViewById(R.id.example);
        recyclerView = v.findViewById(R.id.recycler_view);
        getDataFromDatabase();
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapter = new Adapter(this.getContext(),recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        return v;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = Database.Database_create(getActivity().getApplicationContext());



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
    public void retrieve(URL se) throws IOException, JSONException {
        connection = (HttpURLConnection) se.openConnection();
        inputStream = connection.getInputStream();
        Log.d("itsokay", "okay");
        sc = new Scanner(inputStream);
        sc.useDelimiter("\\A");
        final String s = sc.next();
        final String str = JsonParser.parser(s);
        final String example = JsonParser.example();
        storeInDatabase(str, example);


        connection.disconnect();


    }

    public void storeInDatabase(String str, String example) {

        entity.setMeaning_of_word(str);
        entity.setExample(example);
        entity.setName_of_meaning(entered_meaning);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                database.dao().insert(entity);
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



