package com.example.meanings_downloader;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.meanings_downloader.Database.Database;
import com.example.meanings_downloader.Database.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;


public class Learning_mode extends Fragment implements View.OnClickListener
{

    TextView num_of_words_textview;
    TextView category_to_select_textview;
    Spinner num_of_words_spinner;
    Spinner category_to_select_spinner;
    TextView main_textview;
   Button start_learning_button;
    ArrayAdapter num_of_words_adapeter;
    ArrayAdapter category_adapter;
    Adapter.Clicklistener clicklistener;
    Button learned_words_button;
    Button repeat_words_button;
    int num_of_words;
    String category;
    Database database;
   int setnum_at_present;

    public Learning_mode(Adapter.Clicklistener clicklistener) {

        this.clicklistener=clicklistener;
    }


    public Learning_mode() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        database=Database.Database_create(getContext());
        database.dao().getLearned_set(false).observe(getViewLifecycleOwner(), new Observer<List<Entity>>() {
            @Override
            public void onChanged(List<Entity> entities) {
                MainActivity.sedata(entities);
                Log.d("obtainedsize",String.valueOf(entities.size()));
                Log.d("obtainedn",entities.get(0).getName_of_meaning());


                //  Log.d("obtained",String.valueOf(entities.get(5).getLearnedset()));
            }
        });

        return initialize_views(inflater,container);
    }

int getEntities_to_find_set_number()
{
    Executors.newSingleThreadExecutor().execute(new Runnable() {
        @Override
        public void run() {
            setnum_at_present=database.dao().load_setnum_at_present(1);
//            Log.d("setnummmaa", String.valueOf(setnum_at_present));
        }
    });
    return setnum_at_present;
}

    private View initialize_views(LayoutInflater inflater, ViewGroup container){
        View v=inflater.inflate(R.layout.fragment_learning_mode,container,false);
        num_of_words_textview=v.findViewById(R.id.number_of_words_to_show);
        num_of_words_spinner=v.findViewById(R.id.value_of_num_of_words_to_test);
        start_learning_button=v.findViewById(R.id.learning_name_button);
        category_to_select_textview=v.findViewById(R.id.pick_category);
        category_to_select_spinner=v.findViewById(R.id.selectrandom_meanings_from_spinner);
        start_learning_button.setOnClickListener(this);
        learned_words_button=v.findViewById(R.id.Learned_words_button);
        repeat_words_button=v.findViewById(R.id.repeat_vocab_button);
        learned_words_button.setOnClickListener(this);
        repeat_words_button.setOnClickListener(this);
        return v;


   }

    @Override
    public void onStart() {
        super.onStart();
        num_of_words_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getChildAt(0)!=null)
                { num_of_words = Integer.parseInt(  ((TextView) parent.getChildAt(0)).getText().toString());}

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        category_to_select_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent!=null)
                try{category=   ((TextView) parent.getChildAt(0)).getText().toString();}
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        num_of_words_adapeter=ArrayAdapter.createFromResource(getContext(),R.array.num_of_words_to_learn,android.R.layout.simple_spinner_dropdown_item);
        num_of_words_spinner.setAdapter(num_of_words_adapeter);

        category_adapter=ArrayAdapter.createFromResource(getContext(),R.array.select_category_of_words_to_display,android.R.layout.simple_spinner_dropdown_item);
        category_to_select_spinner.setAdapter(category_adapter);
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.Learned_words_button ){

            clicklistener.onclick("from_learnmode_to_setSelection","nothing",getEntities_to_find_set_number());
        }
       else if(v.getId()==R.id.repeat_vocab_button){
            clicklistener.onclick(Constants.REPEAT_WORDS,"nothing",1);
        }
       else
        clicklistener.onclick(Constants.FROM_FAVOURITE_TO_CARD_VIEW,category,getEntities_to_find_set_number());

    }
}
