package com.example.meanings_downloader;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;


import com.example.meanings_downloader.Database.Database;
import com.example.meanings_downloader.Database.Entity;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;


public class Card_Fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Adapter.Clicklistener clicklistener;
     private int count_next_pressed=0;


    private ImageButton next;
    TextView click_to_reveal_meaning;
    private ImageButton previous;
    private String extra;
    private List<Entity> entities;
    private String meaning;
    LinearLayout inside_card_linear;
    EditText writeOwnWords;
    private String example;
    private TextView meaningview;
    private TextView example_view;
    private ScrollView scrollView;
    Button learned_it_button;
    Button repeat_it_button;
    LinearLayout prev_next_textview;

   // private LinearLayout linearLayout;


    private  static Entity entity;

    ViewGroup progressview;
    private Toolbar toolbar;
    Database database;
    String user_written_data="";
    boolean opened=false;


    public Card_Fragment() {
        // Required empty public constructor
    }

    Card_Fragment(List<Entity> entities, String extra, Adapter.Clicklistener clicklistener) {
        this.extra = extra;
        this.entities = entities;
        Card_Fragment.clicklistener = clicklistener;
    }

    static Card_Fragment newInstance(String param1, String param2, String extra, Adapter.Clicklistener clicklistener, Entity entiy) {
        Log.d("clicker", "clickededdee");
        Card_Fragment fragment = new Card_Fragment();
        Card_Fragment.clicklistener = clicklistener;
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(Constants.KEY_TO_CHECK_EXTRAFIELD_IN_CARDVIEW, extra);
        fragment.setArguments(args);
        entity=entiy;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            meaning = getArguments().getString(ARG_PARAM1);
            example = getArguments().getString(ARG_PARAM2);
            extra = getArguments().getString(Constants.KEY_TO_CHECK_EXTRAFIELD_IN_CARDVIEW);
            setHasOptionsMenu(true);
            database=Database.Database_create(getActivity().getApplicationContext());


        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState!=null)
        {
            extra = savedInstanceState.getString("extra");
        }
        View v = initialize_views(inflater, container);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("hey");

        if (extra.equals(Constants.TO_OPEN_PLAY_CARDS)) {
            //linearLayout.setVisibility(View.VISIBLE);

            meaning = "<strong> <h4>Meaning :</h4></strong> " + entities.get(0).getMeaning_of_word();
            example = "<b> <h4> Example : </h4> </b> " + entities.get(0).getExample() + "";
            meaningview.setText(meaning);
            example_view.setText(example);



        } else if (extra.equals(Constants.MAIN_ADAPTER_TO_CARD_VIEW)) {

            set_textview(meaning,example);
            next.setVisibility(View.GONE);
            previous.setVisibility(View.GONE);
          prev_next_textview.setVisibility(View.GONE);
            click_to_reveal_meaning.setVisibility(View.GONE);

        } if(extra.equals(Constants.FROM_FAVOURITE_TO_CARD_VIEW)) {
toolbar.setVisibility(View.GONE);
            Collections.shuffle(entities);
           prev_next_textview.setVisibility(View.GONE);
           // linearLayout.setVisibility(View.VISIBLE);
            writeOwnWords.setVisibility(View.INVISIBLE);
            progressview.setVisibility(View.VISIBLE);
            meaningview.setVisibility(View.GONE);
            inside_card_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   if(!opened) {
                       meaningview.setVisibility(View.VISIBLE);
                       prev_next_textview.setVisibility(View.VISIBLE);
                       next.setVisibility(View.GONE);
                       click_to_reveal_meaning.setVisibility(View.GONE);
                       previous.setVisibility(View.GONE);
                     //  linearLayout.setVisibility(View.GONE);
                       set_textview(entities.get(count_next_pressed).getMeaning_of_word(), entities.get(count_next_pressed).getExample());
                       opened=true;
                   }
                   else
                   {
                      // linearLayout.setVisibility(View.VISIBLE);
                       next.setVisibility(View.VISIBLE);
                       click_to_reveal_meaning.setVisibility(View.VISIBLE);
                       previous.setVisibility(View.VISIBLE);
                       prev_next_textview.setVisibility(View.GONE);
                       meaningview.setVisibility(View.GONE);
                       set_only_example_view_text();
                       opened=false;
                   }


                }


            });
           set_only_example_view_text();
        }


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        writeOwnWords.setSelection(writeOwnWords.getText().toString().length());

    }

    public void set_only_example_view_text() {
        String exampl = " <h2>"+ entities.get(count_next_pressed).getName_of_meaning()+"</h1>";
        example_view.setText(Html.fromHtml(exampl));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(entity!=null && !entity.getUserTypedData().equals(""))
        {

            writeOwnWords.setText( entity.getUserTypedData());
        }
    }

    public void set_textview(String meaning, String example) {
        String meanin = "<h1>Meaning :</h1> " +"<h4><i>"+ meaning+"</h4></i>";
        Log.d("meaning",meaning);
        String exampl = "<h2>Example :</h2> " +"<h5><i>"+ example+"</h5></i>";
        meaningview.setText(Html.fromHtml(meanin));
        example_view.setText(Html.fromHtml(exampl));
    }

    @Override
    public void onPause() {
        super.onPause();
        user_written_data=writeOwnWords.getText().toString();
        if(!user_written_data.equals("") && entity!=null  && database!=null) {
            entity.setUserTypedData(writeOwnWords.getText().toString());
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    database.dao().update(entity);
                }
            });
        }

    }

    private View initialize_views(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_cardview, container, false);
         //   linearLayout = v.findViewById(R.id.next_prev_buttons);
            progressview=v.findViewById(R.id.progress);
prev_next_textview=v.findViewById(R.id.prev_next_textview);
            learned_it_button=v.findViewById(R.id.learned_it_button);
            repeat_it_button=v.findViewById(R.id.repeat_again);

            next = v.findViewById(R.id.next);
            previous = v.findViewById(R.id.previous);

            inside_card_linear=v.findViewById(R.id.inside_card_linear);

            toolbar = v.findViewById(R.id.toolbar1);


click_to_reveal_meaning=v.findViewById(R.id.hint_reveal);
            meaningview = v.findViewById(R.id.saved_meanings);
            writeOwnWords=v.findViewById(R.id.write_own_content);
            example_view = v.findViewById(R.id.example);





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

if(!extra.equals(Constants.MAIN_ADAPTER_TO_CARD_VIEW))
        initialize_prev_next_listeners();

    }

    private void initialize_prev_next_listeners() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count_next_pressed < entities.size() - 1) {
                    count_next_pressed = count_next_pressed + 1;
                    if (extra.equals(Constants.TO_OPEN_PLAY_CARDS)) {
                        meaning = "<h1>Meaning :</h1> " +"<h3><i>"+ entities.get(count_next_pressed).getMeaning_of_word()+"</h3></i>";
                        example = "<h1>Meaning :</h1> " +"<h3><i>"+ entities.get(count_next_pressed).getExample()+"</h3></i>";
                        meaningview.setText(Html.fromHtml(meaning));
                        example_view.setText(Html.fromHtml(example));


                    }
                    if (extra.equals(Constants.FROM_FAVOURITE_TO_CARD_VIEW)) {
                        String exampl = " <h1><i>"+ entities.get(count_next_pressed).getName_of_meaning()+" </i></h1> " ;
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
        Log.d("hellooo","hello");
        if (item.getItemId() == android.R.id.home) {
           // getActivity().onBackPressed();
            Log.d("hello","hello");
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
                clicklistener.onclick(Constants.FROM_CARDVIEW_TO_FAVOURITE_VIEW);
        }

    }
}
