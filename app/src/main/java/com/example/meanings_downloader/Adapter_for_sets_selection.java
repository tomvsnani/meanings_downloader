package com.example.meanings_downloader;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meanings_downloader.Database.Entity;

import java.util.List;

public class Adapter_for_sets_selection extends RecyclerView.Adapter {
    int number_of_sets;
    List<Integer> entities;
    Adapter.Clicklistener clicklistener;

    public Adapter_for_sets_selection(int number_of_sets, List<Integer> entities, Adapter.Clicklistener clicklistener){
        this.number_of_sets=number_of_sets;
        this.entities=entities;
        this.clicklistener=clicklistener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_selecting_sets,parent,false);
        return new viewholder(v);
    }

    class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView set_selectiontextview;
        TextView set_count_textview;
        LinearLayout rootview;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            set_selectiontextview=itemView.findViewById(R.id.set_selection);
            set_count_textview=itemView.findViewById(R.id.set_count_textview);
            rootview=itemView.findViewById(R.id.root_linear_for_set_selection);
            rootview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("numberofsets","clicked_here");
            clicklistener.onclick(Constants.LEARN_WORDS,"",entities.get(getAdapterPosition()));

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("numberofse",entities.toString());
        ((viewholder)holder).set_count_textview.setText(String.valueOf(entities.get(position)));

    }

    @Override
    public int getItemCount() {
        Log.d("numberofsets",String.valueOf(number_of_sets));
        return number_of_sets;
    }
}
