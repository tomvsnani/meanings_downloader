package com.example.meanings_downloader;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.List;


public class Fav_fragment extends Fragment implements View.OnClickListener {


    RecyclerView recyclerView;
    LinearLayoutManager linearLayout;
    Fav_meaning_Adapter fav_meaning_adapter;
    List<Entity> entities;
    Adapter.Clicklistener clicklistener;
    RadioButton favouriteRadioButton;
    RadioButton cardRadioButton;

    public Fav_fragment(List<Entity> entities, Adapter.Clicklistener clicklistener) {
        this.entities = entities;
        this.clicklistener = clicklistener;

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
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerView = view.findViewById(R.id.fav_recycler);
        linearLayout = new LinearLayoutManager(getContext());
        fav_meaning_adapter = new Fav_meaning_Adapter(clicklistener);
        fav_meaning_adapter.submitList(entities);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(fav_meaning_adapter);
        favouriteRadioButton = view.findViewById(R.id.favourite_mode);
        cardRadioButton = view.findViewById(R.id.card_mode);
        favouriteRadioButton.setOnClickListener(this);
        cardRadioButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.card_mode) {
            clicklistener.onclick("card_mode");
        }

    }
}
