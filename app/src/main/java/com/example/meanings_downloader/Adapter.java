package com.example.meanings_downloader;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends ListAdapter {
    private List<Entity>list=new ArrayList<>();
    private Entity entity;
    private Context context;
    private  RecyclerView recyclerView;

    protected Adapter(Context context,RecyclerView recyclerView) {

        super(Entity.diffcall);
        this.context=context;
        this.recyclerView=recyclerView;
    }

    @Override
    public void submitList(@Nullable List list) {
        super.submitList(list != null ? new ArrayList<>(list) : null);
        recyclerView.smoothScrollToPosition(0);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);

        return new Holder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        entity=(Entity) getItem(position);

        if (!entity.getExample().isEmpty())
            ( (Holder)holder).textView_example.setText(HtmlCompat.fromHtml("<b> "+context.getResources().getString(R.string.example)+"</b>"+"  "+entity.getExample(),0));
        if (!entity.getMeaning_of_word().isEmpty())

            ((Holder)holder).textview_meaning.setText(HtmlCompat.fromHtml("<b> "+context.getResources().getString(R.string.meaning)+"</b>"+"  "+entity.getMeaning_of_word(),0));

    }

    class Holder extends RecyclerView.ViewHolder{
        TextView textView_example;
        TextView textview_meaning;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textView_example=itemView.findViewById(R.id.textview_example);
            textview_meaning=itemView.findViewById(R.id.textview_meaning);

        }
    }



    @Override
    public int getItemCount() {
Log.d("countokay","okay");
        if(!getCurrentList().isEmpty()) {
            return getCurrentList().size() ;
        } else
            return 0;
    }


}

