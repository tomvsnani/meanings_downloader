package com.example.meanings_downloader;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Adapter extends ListAdapter {
    List<String> list = new ArrayList<>();
    Database database = Database.Database_create(new MainFragment().getContext());
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
    ImageView imageView;
    Clicklistener listener;

    private Entity entity;


    protected Adapter( MainActivity blankFragment) {

        super(Entity.diffcall);
        this.listener = blankFragment;

    }

    @Override
    public void submitList(@Nullable List list) {
        super.submitList(list != null ? new ArrayList<>(list) : null);
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
        list.add(entity.getName_of_meaning());
        Log.d("listsize",String.valueOf(list));

        String sound = "<small>" + entity.getSound() + "</small>";
        ((Holder) holder).textview_meaning.setText(Html.fromHtml(entity.getName_of_meaning() + "  &#160   <small><sup><sup><font color=#cb32c9>" + entity.getParts_of_speech() + "</font></sup></sup>" + "</small>" + "    &ensp &ensp" + sound));
        if (entity.getFav_meaning() == 0)
            ((Holder) holder).fav_meaning_image.setImageResource(R.drawable.likes);
        else
            ((Holder) holder).fav_meaning_image.setImageResource(R.drawable.likesfill);

    }

    @Override
    public int getItemCount() {
        Log.d("countokay", "okay");
        if (!getCurrentList().isEmpty()) {
            return getCurrentList().size();
        } else
            return 0;
    }


    public interface Clicklistener {
        void onclick(int adapterposition, Entity entity, View view);

        void onclick(String extra);
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView fav_meaning_image;
        TextView textview_meaning;

        public Holder(@NonNull View itemView) {
            super(itemView);
            // textView_example=itemView.findViewById(R.id.textview_example);
            textview_meaning = itemView.findViewById(R.id.textview_meaning);
            fav_meaning_image = itemView.findViewById(R.id.add_fav_meaning);
            imageView = itemView.findViewById(R.id.audio_play);
            imageView.setOnClickListener(this);
            fav_meaning_image.setOnClickListener(this);
            textview_meaning.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            listener.onclick(getAdapterPosition(), (Entity) getItem(getAdapterPosition()), v);
        }
    }

}

