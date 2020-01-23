package com.example.meanings_downloader;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

public class bottom_recycler_adapter extends RecyclerView.Adapter {

    List<Drawable> list;
    int width;
    FrameLayout appBarLayout;
    public bottom_recycler_adapter(List<Drawable> list,int width){
        this.list=list;
        this.width=width;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.botton_recyclerview,parent,false);


        return new viewolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        ((viewolder)holder).imageButton.setImageDrawable( list.get(position));
        ((viewolder)holder).frameLayout.getLayoutParams().width=this.width;

    }

    class viewolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton imageButton;
        FrameLayout frameLayout;
        public viewolder(@NonNull View itemView) {
            super(itemView);
            imageButton=itemView.findViewById(R.id.bottombar_icons);
            frameLayout=itemView.findViewById(R.id.frame);
            imageButton.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
