package com.example.meanings_downloader;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class inner_meanings_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String meaning;
    private String example;
    private TextView meaningview;
    private TextView example_view;
    Toolbar toolbar;



        public inner_meanings_fragment() {
        // Required empty public constructor
    }


    public static inner_meanings_fragment newInstance(String param1, String param2) {
        inner_meanings_fragment fragment = new inner_meanings_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onclickid","ininner");
        if (getArguments() != null) {
            meaning = getArguments().getString(ARG_PARAM1);
            example = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_inner_meanings_fragment, container, false);
        meaningview=v.findViewById(R.id.saved_meanings);
        example_view=v.findViewById(R.id.example);
        meaningview.setText(meaning);
        example_view.setText(example);
        toolbar=v.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return v;
    }







}
