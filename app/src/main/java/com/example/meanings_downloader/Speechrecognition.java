package com.example.meanings_downloader;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meanings_downloader.Database.Database;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;


public class Speechrecognition extends Fragment implements TextToSpeech.OnInitListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecognitionService recognitionService;
    ImageButton record_icon;
    // TextView textView1;
    SpeechRecognizer speechRecognizer;
    Intent recognizerIntent;
    TextView data;
    Thread thread;
    TextView click_to_speak_again;
    TextView click_to_listen;
    LinearLayout hide_listen_layout;
    ImageButton speak_accent_button;
    TextToSpeech textToSpeech;
    String words_spoken = "";
    LinearLayout save_spoken_words_in_db;
    Database database;
    private String mParam1;
    private String mParam2;
    ArrayList<String> voice;
    SaveSpokenWordsEntity saveSpokenWords;
Button save_words_yes;
String check_if_spoke_empty;
    public Speechrecognition() {
        // Required empty public constructor
    }


    public static Speechrecognition newInstance(String param1, String param2) {
        Speechrecognition fragment = new Speechrecognition();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
database=Database.Database_create(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_speechrecognition, container, false);

        record_icon = v.findViewById(R.id.record);
        // textView1 = v.findViewById(R.id.stop_record);
        data = v.findViewById(R.id.data);
        click_to_listen = v.findViewById(R.id.click_here_to_listen_to_an_accent);
        click_to_speak_again = v.findViewById(R.id.click_to_speak_again);
        hide_listen_layout = v.findViewById(R.id.talk_in_accent_linear_layout);
        speak_accent_button = v.findViewById(R.id.speak_in_accent);
        textToSpeech = new TextToSpeech(getContext(), this);
        save_spoken_words_in_db = v.findViewById(R.id.save_spoken_words_in_database);
        save_words_yes=v.findViewById(R.id.save_words_spoken_Yes);
        save_words_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        saveSpokenWords=new SaveSpokenWordsEntity();
                        saveSpokenWords.setSave_spoken_word(words_spoken);
                        if(voice.size()>0)
                        database.dao().insert(saveSpokenWords);
                    }
                });
            }
        });
        speak_accent_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeech.speak(words_spoken, TextToSpeech.QUEUE_FLUSH, null, null);


                    save_spoken_words_in_db.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(getContext(), Constants.CANNOT_PLAY_AUDIO, Toast.LENGTH_SHORT).show();
                }

            }
        });
//        textView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                speechRecognizer.stopListening();
//            }
//        });
        record_icon.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Log.d("readyyy", "clickk");
                data.setText("");
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 5);


            }
        });


        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d("readyyy", "w");


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());


        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d("readyyy", "ready");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d("readyyy", "beg");
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hide_listen_layout.setVisibility(View.GONE);
                                save_spoken_words_in_db.setVisibility(View.GONE);
                                click_to_speak_again.setText("Click to speak");
                                record_icon.setImageResource(R.drawable.mic_listening1);


                            }
                        });


                    }
                });
                thread.start();
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        record_icon.setImageResource(R.drawable.ic_mic_black_24dp);
                        click_to_speak_again.setText("Click to speak again");
                        if (!words_spoken.isEmpty() && voice.size()>0) {
                            hide_listen_layout.setVisibility(View.VISIBLE);
                        }

                    }
                });

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
//                Log.d("readyyy", "result");
//                ArrayList<String> voice = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//                for (String s : voice) {
//                    Log.d("readyyyy", "" + voice.size());
//                    record_icon.setText(s);


                if (!words_spoken.isEmpty() && !words_spoken.equals(" "))
                    data.setText(Html.fromHtml("<h3>The sentence or word you just spoke is </h3> :" + "<h4>" + words_spoken + "</h4>"));
                else
                    data.setText("Sorry, did not hear that please speak again");

            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                 voice = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
              voice = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for (String s : voice) {
                    words_spoken = s;

                }


            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        speechRecognizer.startListening(recognizerIntent);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {


        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.ENGLISH);
        }
    }
}
