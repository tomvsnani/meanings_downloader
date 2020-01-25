package com.example.meanings_downloader;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    public Learning_mode(Adapter.Clicklistener clicklistener) {
        // Required empty public constructor
        this.clicklistener=clicklistener;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return initialize_views(inflater,container);
    }

    private View initialize_views(LayoutInflater inflater, ViewGroup container){
        View v=inflater.inflate(R.layout.fragment_learning_mode,container,false);
        num_of_words_textview=v.findViewById(R.id.number_of_words_to_show);
        num_of_words_spinner=v.findViewById(R.id.value_of_num_of_words_to_test);
        start_learning_button=v.findViewById(R.id.learning_name_button);
        category_to_select_textview=v.findViewById(R.id.pick_category);
        category_to_select_spinner=v.findViewById(R.id.selectrandom_meanings_from_spinner);
        start_learning_button.setOnClickListener(this);


        return v;


   }

    @Override
    public void onStart() {
        super.onStart();
        num_of_words_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        category_to_select_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
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
        clicklistener.onclick(Constants.FROM_FAVOURITE_TO_CARD_VIEW);

    }
}
