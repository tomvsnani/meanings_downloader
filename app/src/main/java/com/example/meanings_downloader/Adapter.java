package com.example.meanings_downloader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Adapter extends ListAdapter {
    Database database = Database.Database_create(new BlankFragment().getContext());
    public ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    database.dao().delete((Entity) getItem(((Holder) viewHolder).getAdapterPosition()));
                }
            });

        }
    };
    private List<Entity> list = new ArrayList<>();
    private Entity entity;
    private Context context;
    private RecyclerView recyclerView;
    Clicklistener listener;

    protected Adapter(Context context, RecyclerView recyclerView,MainActivity blankFragment) {

        super(Entity.diffcall);
        this.context = context;
        this.recyclerView = recyclerView;
        this.listener=blankFragment;
    }

    @Override
    public void submitList(@Nullable List list) {
        super.submitList(list != null ? new ArrayList<>(list) : null);
        // recyclerView.smoothScrollToPosition(0);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

        return new Holder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        entity = (Entity) getItem(position);


        ((Holder) holder).textview_meaning.setText(entity.getName_of_meaning());

       /* if (!entity.getExample().isEmpty())
            ( (Holder)holder).textView_example.setText(HtmlCompat.fromHtml("<b> "+context.getResources().getString(R.string.example)+"</b>"+"  "+entity.getExample(),0));
        if (!entity.getMeaning_of_word().isEmpty())

            ((Holder)holder).textview_meaning.setText(HtmlCompat.fromHtml("<b> "+context.getResources().getString(R.string.meaning)+"</b>"+"  "+entity.getMeaning_of_word(),0));

        */

    }

    @Override
    public int getItemCount() {
        Log.d("countokay", "okay");
        if (!getCurrentList().isEmpty()) {
            return getCurrentList().size();
        } else
            return 0;
    }


    public interface listene {
        void onclick();

    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_example;
        TextView textview_meaning;

        public Holder(@NonNull View itemView) {
            super(itemView);
            // textView_example=itemView.findViewById(R.id.textview_example);
            textview_meaning = itemView.findViewById(R.id.textview_meaning);
            textview_meaning.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
          listener.onclick(getAdapterPosition(),(Entity) getItem(getAdapterPosition()));
        }
    }

    public interface Clicklistener{
        void onclick(int adapterposition,Entity entity);
    }

}

