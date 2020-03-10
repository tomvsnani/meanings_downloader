package com.example.meanings_downloader;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.ArraySet;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.meanings_downloader.Database.Database;
import com.example.meanings_downloader.Database.Entity;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Card_Fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Adapter.Clicklistener clicklistener;
    private static Entity entity;
    TextView click_to_reveal_meaning;
    ConstraintLayout inside_card_linear;
    SaveSpokenWordsEntity saveSpokenWordsEntity = new SaveSpokenWordsEntity();
    Set<String> learned_words_set = new HashSet<>();
    EditText writeOwnWords;
    Button learned_it_button;
    Button repeat_it_button;
    LinearLayout prev_next_textview;
    LinearLayout cardview_meaning_linearlayout;
    ViewGroup progressview;
    Database database;
    String user_written_data = "";
    TextView words_learnt;
    TextView words_checked;
    boolean opened = false;
    Set<Entity> set_to_store_group_of_words_together_as_a_set = new HashSet<>();
    String parts_of_speech_from_main_fragment;
    String sound_from_main_fragment;
    String name_of_meaning_from_main_fragemnt;
    String category;
    int num_of_words;
    SeekBar seekbar;
    int count_words_learned = 0;
    List<Entity> total_words;
    Entity entity_to_update_setnum;
    private int count_next_pressed = 0;
    private ImageButton next;
    private ImageButton previous;
    private String extra;
    private List<Entity> entities;
    // private LinearLayout linearLayout;
    private String meaning;
    private String example;
    private TextView meaningview;
    private TextView example_view;
    private ScrollView scrollView;
    private Toolbar toolbar;
    ExecutorService executorService;
    Setnum_entity setnum_entity;


    public Card_Fragment() {
        // Required empty public constructor
    }

    Card_Fragment(List<Entity> entities, String extra, Adapter.Clicklistener clicklistener, String category, int no_of_words, List<Entity> total_words,Setnum_entity entity) {
        this.extra = extra;
        this.entities = entities;
        Card_Fragment.clicklistener = clicklistener;
        this.category = category;
        this.num_of_words = no_of_words;
        this.total_words = total_words;
        this.setnum_entity=entity;
        Log.d("setnummmtt", String.valueOf(entity.getSet_num_at_present()));

    }

    static Card_Fragment newInstance(String name_of_meaning, String param1, String param2, String parts_of_speech, String sound, String extra, Adapter.Clicklistener clicklistener, Entity entiy) {
        Log.d("clicker", "clickededdee");
        Card_Fragment fragment = new Card_Fragment();
        Card_Fragment.clicklistener = clicklistener;
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString("parts_of_speech", parts_of_speech);
        args.putString("name_of_meaning", name_of_meaning);
        args.putString("sound", sound);
        args.putString(Constants.KEY_TO_CHECK_EXTRAFIELD_IN_CARDVIEW, extra);
        fragment.setArguments(args);
        entity = entiy;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            meaning = getArguments().getString(ARG_PARAM1);
            example = getArguments().getString(ARG_PARAM2);
            extra = getArguments().getString(Constants.KEY_TO_CHECK_EXTRAFIELD_IN_CARDVIEW);
            parts_of_speech_from_main_fragment = getArguments().getString("parts_of_speech");
            sound_from_main_fragment = getArguments().getString("sound");
            name_of_meaning_from_main_fragemnt = getArguments().getString("name_of_meaning");


            setHasOptionsMenu(true);


        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        executorService=Executors.newSingleThreadExecutor();
        if (savedInstanceState != null) {
            extra = savedInstanceState.getString("extra");
        }
        View v = initialize_views(inflater, container);
        seekbar.setMax(30);


        words_checked.setText("Words Seen :" + count_next_pressed + 1 + "/" + 30);
        words_learnt.setText("Words Learnt :" + count_words_learned + "/" + 30);
        database = Database.Database_create(getActivity().getApplicationContext());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("hey");
        Log.d("extras", extra);
        if (extra.equals(Constants.TO_OPEN_PLAY_CARDS)) {
            //linearLayout.setVisibility(View.VISIBLE);

            meaning = "<strong> <h4>Meaning :</h4></strong> " + entities.get(0).getMeaning_of_word();
            example = "<b> <h4> Example : </h4> </b> " + entities.get(0).getExample() + "";
            meaningview.setText(meaning);
            example_view.setText(example);


        } else if (extra.equals(Constants.MAIN_ADAPTER_TO_CARD_VIEW)) {
            Log.d("extras", "enteredhere");
            set_textview(name_of_meaning_from_main_fragemnt, meaning, example, parts_of_speech_from_main_fragment, sound_from_main_fragment);
            next.setVisibility(View.GONE);
            previous.setVisibility(View.GONE);
            prev_next_textview.setVisibility(View.GONE);
            click_to_reveal_meaning.setVisibility(View.GONE);

        } else {

            Log.d("extras", "same");
            toolbar.setVisibility(View.GONE);

            // Collections.shuffle(entities);

            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    for (int k = 0; k < 30; k++) {

                        entity_to_update_setnum=  entities.get(k);
                        entity_to_update_setnum.setLearnedset(true);
                        entity_to_update_setnum.setSet_number(setnum_entity.getSet_num_at_present()+1);
                        Log.d("setnumh", String.valueOf(entity_to_update_setnum.getSet_number()));
                        database.dao().update(entity_to_update_setnum);

                        Log.d("setnummm1", String.valueOf(setnum_entity.getSet_num_at_present()));

//                        database.dao().update_setnum_at_id(entity_to_update_setnum.getSet_number_maintainer() + 1,entity_to_update_setnum.getId());
//                       database.dao().update_set_at_id(true,entities.get(k).getId());


//                            Log.d("obtainedk",String.valueOf(k));
//                              set_to_store_group_of_words_together_as_a_set.add(entities.get(k));

                    }
                    setnum_entity.setSet_num_at_present(setnum_entity.getSet_num_at_present()+1);
                    database.dao().update_setnum_at_present(setnum_entity);

                }
            });


            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Log.d("sizeeee",String.valueOf(total_words.size()));

                    for (int k = 0; k < total_words.size(); k++) {
                        //  total_words.get(k).setSet_number(total_words.get(k).getSet_number_maintainer() + 1);
                      //  total_words.get(k).setSet_number_maintainer(total_words.get(k).getSet_number_maintainer() + 1);
                        database.dao().update_setnum_at_id(setnum_entity.getSet_num_at_present()+1,total_words.get(k).getId());
                    }


                }
            });

            prev_next_textview.setVisibility(View.GONE);
            // linearLayout.setVisibility(View.VISIBLE);
            writeOwnWords.setVisibility(View.INVISIBLE);
            progressview.setVisibility(View.VISIBLE);
            meaningview.setVisibility(View.GONE);
            set_only_example_view_text();
            inside_card_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entities.size() > 0) {
                        if (!opened) {
                            cardview_meaning_linearlayout.getLayoutParams().width = getActivity().getResources().getDisplayMetrics().widthPixels - 20;
                            meaningview.setVisibility(View.VISIBLE);
                            prev_next_textview.setVisibility(View.VISIBLE);
                            next.setVisibility(View.GONE);
                            click_to_reveal_meaning.setVisibility(View.GONE);
                            previous.setVisibility(View.GONE);
                            //  linearLayout.setVisibility(View.GONE);
                            set_textview(entities.get(count_next_pressed).getName_of_meaning(), entities.get(count_next_pressed).getMeaning_of_word(), entities.get(count_next_pressed).getExample(), entities.get(count_next_pressed).getParts_of_speech(), entities.get(count_next_pressed).getSound());
                            opened = true;
                        } else {
                            // linearLayout.setVisibility(View.VISIBLE);
                            show_name_of_meaningonly_cardview();
                        }
                    }

                }


            });

        }


        return v;
    }

    public void show_name_of_meaningonly_cardview() {
        cardview_meaning_linearlayout.getLayoutParams().width = getActivity().getResources().getDisplayMetrics().widthPixels - 202;
        next.setVisibility(View.VISIBLE);
        click_to_reveal_meaning.setVisibility(View.VISIBLE);
        previous.setVisibility(View.VISIBLE);
        prev_next_textview.setVisibility(View.GONE);
        meaningview.setVisibility(View.GONE);
        set_only_example_view_text();
        opened = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        writeOwnWords.setSelection(writeOwnWords.getText().toString().length());

    }

    public void set_only_example_view_text() {
        String exampl = " <h2>" + entities.get(count_next_pressed).getName_of_meaning() + "</h1>";
        example_view.setText(Html.fromHtml(exampl));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (entity != null && !entity.getUserTypedData().equals("")) {

            writeOwnWords.setText(entity.getUserTypedData());
        }
    }

    public void set_textview(String name_of_meaning, String meaning, String example, String parts_of_speech_from_main_fragment, String sound_from_main_fragment) {
        String meanin = "<h1><u>" + name_of_meaning.toUpperCase() + " </u> : &nbsp<small>(" + parts_of_speech_from_main_fragment + ")  &nbsp<i>  " + sound_from_main_fragment + "</i></small>" + "</h1>   " + "<h3>Meaning :</h3> <h5><i>" + meaning + "</h5></i>";
        Log.d("meaning", meaning);
        String exampl = "<h3>Example :</h3> " + "<h5><i>" + example + "</h5></i>";
        meaningview.setText(Html.fromHtml(meanin));
        example_view.setText(Html.fromHtml(exampl));
    }

    @Override
    public void onPause() {
        super.onPause();
        user_written_data = writeOwnWords.getText().toString();
        if (!user_written_data.equals("") && entity != null && database != null) {
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
        progressview = v.findViewById(R.id.progress);
        words_checked = v.findViewById(R.id.number_of_words_gone_through);
        words_learnt = v.findViewById(R.id.words_learnt);
        prev_next_textview = v.findViewById(R.id.prev_next_textview);
        learned_it_button = v.findViewById(R.id.learned_it_button);
        repeat_it_button = v.findViewById(R.id.repeat_again);
        seekbar = v.findViewById(R.id.seekbar);
        next = v.findViewById(R.id.next);
        previous = v.findViewById(R.id.previous);

        inside_card_linear = v.findViewById(R.id.inside_card_linear);

        toolbar = v.findViewById(R.id.toolbar1);


        click_to_reveal_meaning = v.findViewById(R.id.hint_reveal);
        meaningview = v.findViewById(R.id.saved_meanings);
        writeOwnWords = v.findViewById(R.id.write_own_content);
        example_view = v.findViewById(R.id.example);
        cardview_meaning_linearlayout = v.findViewById(R.id.cardview_meaning_linearlayout);


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

        if (!extra.equals(Constants.MAIN_ADAPTER_TO_CARD_VIEW))
            initialize_prev_next_listeners();

        learned_it_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Added to learned words", Toast.LENGTH_SHORT).show();
                if (count_words_learned < 30)
                    learned_words_set.add(entities.get(count_next_pressed).getName_of_meaning());
                count_words_learned = count_words_learned + 1;

                words_learnt.setText("Words Learnt :" + learned_words_set.size() + "/" + 30);
                if (entities.get(count_next_pressed) == entities.get(entities.size() - 1)) {
                    Toast.makeText(getContext(), "Congractulations you learned every word in this set", Toast.LENGTH_SHORT).show();
                    click_to_reveal_meaning.setText("This set is finished ");
                } else {
                    final Entity entity = entities.get(count_next_pressed);
                    entity.setLearned_words(true);
                    show_next_word(v);
                    show_name_of_meaningonly_cardview();

                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            database.dao().update(entity);
                        }
                    });
                }
            }
        });


        repeat_it_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Added to repeat words", Toast.LENGTH_SHORT).show();

                final Entity entity = entities.get(count_next_pressed);
                entity.setRepeat_words(true);
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.dao().update(entity);
                    }
                });
            }
        });

    }

    private void initialize_prev_next_listeners() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_next_word(v);
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count_next_pressed > 0) {
                    count_next_pressed = count_next_pressed - 1;
                    String exampl = " <h1><i>" + entities.get(count_next_pressed).getName_of_meaning() + "</i></h1> ";
                    example_view.setText(Html.fromHtml(exampl));
                }

            }
        });
    }

    public void show_next_word(View v) {
        if (count_next_pressed < 30 - 1) {

            if (v.getId() == R.id.learned_it_button)
                count_next_pressed = count_next_pressed + 1;
            else {
                seekbar.setProgress(seekbar.getProgress() + 1);
                count_next_pressed = count_next_pressed + 1;
                words_checked.setText("Words Seen :" + (count_next_pressed + 1) + "/" + 30);
                if (extra.equals(Constants.TO_OPEN_PLAY_CARDS)) {
                    meaning = "<h1>Meaning :</h1> " + "<h3><i>" + entities.get(count_next_pressed).getMeaning_of_word() + "</h3></i>";
                    example = "<h1>Meaning :</h1> " + "<h3><i>" + entities.get(count_next_pressed).getExample() + "</h3></i>";
                    meaningview.setText(Html.fromHtml(meaning));
                    example_view.setText(Html.fromHtml(example));


                }
                if (extra.equals(Constants.FROM_FAVOURITE_TO_CARD_VIEW)) {
                    String exampl = " <h1><i>" + entities.get(count_next_pressed).getName_of_meaning() + " </i></h1> ";
                    example_view.setText(Html.fromHtml(exampl));
                }
            }

        } else
            click_to_reveal_meaning.setText("This  is the last word in this set  ");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("hellooo", "hello");
        if (item.getItemId() == android.R.id.home) {
            // getActivity().onBackPressed();
            Log.d("hello", "hello");
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
                clicklistener.onclick(Constants.FROM_CARDVIEW_TO_FAVOURITE_VIEW, "nothing", 1);
        }

    }
}
