package com.example.meanings_downloader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends ListAdapter {
    private List<Entity>list=new ArrayList<>();

    protected Adapter(@NonNull DiffUtil.ItemCallback diffCallback) {
        super(Entity.diffcall);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent);

        return new Holder(v);
    }
    public void setdata(List<Entity> list){
        this.list=list;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    class Holder extends RecyclerView.ViewHolder{
        TextView textView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.recycler_textview);

        }
    }



    @Override
    public int getItemCount() {

        if(getItemCount()>0)
            return getItemCount();
        else
            return 0;
    }


}

