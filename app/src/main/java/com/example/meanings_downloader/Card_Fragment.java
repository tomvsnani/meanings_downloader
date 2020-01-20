package com.example.meanings_downloader;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


import com.google.android.material.appbar.AppBarLayout;

import java.util.Collections;
import java.util.List;


public class Card_Fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static Adapter.Clicklistener clicklistener;
     int count_next_pressed=0;





    Toolbar toolbar;
    LinearLayout linearLayout;
    Button next;
    Button previous;
    String extra;
    RadioButton favRdaioButton;
    RadioButton cardRdaioButton;
    LinearLayout radioButtonsLinear;
    List<Entity> entities;
    AppBarLayout appBarLayout;
    private String meaning;
    private String example;
    private TextView meaningview;
    private TextView example_view;


    public Card_Fragment() {
        // Required empty public constructor
    }

    public Card_Fragment(List<Entity> entities, String extra, Adapter.Clicklistener clicklistener) {
        this.extra = extra;
        this.entities = entities;
        Card_Fragment.clicklistener = clicklistener;
    }

    public static Card_Fragment newInstance(String param1, String param2, String extra, Adapter.Clicklistener clicklistener) {
        Log.d("clicker", "clickededdee");
        Card_Fragment fragment = new Card_Fragment();
        Card_Fragment.clicklistener = clicklistener;
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString("extra", extra);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onclickid", "ininner");
        if (getArguments() != null) {
            meaning = getArguments().getString(ARG_PARAM1);
            example = getArguments().getString(ARG_PARAM2);
            extra = getArguments().getString("extra");
            setHasOptionsMenu(true);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState!=null)
        {
            extra = savedInstanceState.getString("extra");
        }
        View v = inflater.inflate(R.layout.fragment_cardview, container, false);
        meaningview = v.findViewById(R.id.saved_meanings);
        example_view = v.findViewById(R.id.example);
        linearLayout = v.findViewById(R.id.next_prev_buttons);
        radioButtonsLinear = v.findViewById(R.id.linearLayout);
        next = v.findViewById(R.id.next);
        previous = v.findViewById(R.id.previous);
        appBarLayout = v.findViewById(R.id.appbar);
        favRdaioButton = v.findViewById(R.id.favourite_mode_in_card);
        cardRdaioButton = v.findViewById(R.id.card_mode_in_card);
        favRdaioButton.setOnClickListener(this);
        cardRdaioButton.setOnClickListener(this);
        String url = "http://google.com";

        toolbar = v.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (extra.equals("fav_meaning")) {
            Log.d("herehere", "hhh");
            linearLayout.setVisibility(View.VISIBLE);
            radioButtonsLinear.setVisibility(View.GONE);
            meaning = "<strong> <h4>Meaning :</h4></strong> " + entities.get(0).getMeaning_of_word();
            example = "<b> <h4> Example : </h4> </b> " + entities.get(0).getExample() + "";

            meaningview.setText(meaning);
            example_view.setText(example);


        } else if (extra.equals("normal")) {
            linearLayout.setVisibility(View.GONE);
            radioButtonsLinear.setVisibility(View.GONE);

           String meanin = "<h1>Meaning :</h1> " +"<h4><i>"+ meaning+"</h4></i>";
           Log.d("meaning",meaning);
           String exampl = "<h2>Example :</h2> " +"<h5><i>"+ example+"</h5></i>";
            meaningview.setText(Html.fromHtml(meanin));
            example_view.setText(Html.fromHtml(exampl));
        } else {
            Collections.shuffle(entities);
            appBarLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            radioButtonsLinear.setVisibility(View.VISIBLE);
            meaningview.setVisibility(View.GONE);
            String exampl = " <h2>"+ entities.get(0).getName_of_meaning()+"</h1> "  ;
            example_view.setText(Html.fromHtml(exampl));
        }


        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("extra", extra);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count_next_pressed < entities.size() - 1) {
                    count_next_pressed = count_next_pressed + 1;
                    if (extra.equals("normal")) {
                        meaning = "<h1>Meaning :</h1> " +"<h3><i>"+ entities.get(count_next_pressed).getMeaning_of_word()+"</h3></i>";
                        example = "<h1>Meaning :</h1> " +"<h3><i>"+ entities.get(count_next_pressed).getExample()+"</h3></i>";
                        meaningview.setText(Html.fromHtml(meaning));
                        example_view.setText(Html.fromHtml(example));


                    }
                    if (extra.equals("card")) {
                        String exampl = " <h1>"+ entities.get(count_next_pressed).getName_of_meaning()+" </h1> " ;
                        example_view.setText(Html.fromHtml(exampl));
                    }




                }
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count_next_pressed > 0) {
                    count_next_pressed = count_next_pressed - 1;
                    String exampl = " <h1><i>"+ entities.get(count_next_pressed).getName_of_meaning()+"</i></h1> ";
                    example_view.setText(Html.fromHtml(exampl));

                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {


        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favourite_mode_in_card:
                clicklistener.onclick("music");
        }

    }
}
