package com.example.meanings_downloader;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;



public class Fav_meaning_Adapter extends ListAdapter<Entity,RecyclerView.ViewHolder>
{

    Entity entity;
    Adapter.Clicklistener clicklistener;


    protected Fav_meaning_Adapter(Adapter.Clicklistener clicklistener) {
        super(Entity.diffcall);
        this.clicklistener=clicklistener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_meaning_row_layout,parent,false);
        return new viewholder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

entity=getItem(position);
        ((viewholder) holder).meaning_title.setText(entity.getName_of_meaning());
    }


class viewholder extends  RecyclerView.ViewHolder implements View.OnClickListener {
    TextView meaning_title;
    public viewholder(@NonNull View itemView) {
        super(itemView);
        meaning_title=itemView.findViewById(R.id.fav_textview_meaning);
        meaning_title.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Log.d("clicker","clicked");
        clicklistener.onclick(getAdapterPosition(),getItem(getAdapterPosition()),v);
    }
}

}
