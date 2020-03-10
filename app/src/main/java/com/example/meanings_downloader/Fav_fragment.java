package com.example.meanings_downloader;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.meanings_downloader.Database.Entity;

import java.util.List;


public class Fav_fragment extends Fragment implements View.OnClickListener {


    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayout;
    private Fav_meaning_Adapter fav_meaning_adapter;
    private List entities;
    private Adapter.Clicklistener clicklistener;
    private RadioButton favouriteRadioButton;
    private RadioButton cardRadioButton;
    String extra;

    Fav_fragment(List entities, Adapter.Clicklistener clicklistener,String extra) {
        this.entities = entities;
        this.clicklistener = clicklistener;
        this.extra=extra;

    }



    public Fav_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return initialize_views(inflater, container);
    }

    private View initialize_views(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerView = view.findViewById(R.id.fav_recycler);
        linearLayout = new LinearLayoutManager(getContext());
        fav_meaning_adapter = new Fav_meaning_Adapter(clicklistener,extra,getContext());
        fav_meaning_adapter.submitList(entities);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(fav_meaning_adapter);

        return view;
    }


    @Override
    public void onClick(View v) {




    }
}
