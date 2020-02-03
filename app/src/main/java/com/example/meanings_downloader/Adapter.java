package com.example.meanings_downloader;

import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meanings_downloader.Database.Database;
import com.example.meanings_downloader.Database.Entity;
import com.example.meanings_downloader.MainActivity;
import com.example.meanings_downloader.MainFragment;
import com.example.meanings_downloader.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Adapter extends ListAdapter {
    public List<String> list = new ArrayList<>();
    private Database database = Database.Database_create(new MainFragment().getContext());
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
    private ImageButton imageView;

    private Clicklistener listener;

    private Entity entity;
    private String[] divide_parts_of_speech;


    public Adapter(MainActivity blankFragment) {

        super(Entity.diffcall);
        this.listener = blankFragment;

    }

    @Override
    public void submitList(@Nullable List list) {
        super.submitList(list != null ? new ArrayList<Entity>(list) : null);
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
//        Log.d("entityyy",entity.getName_of_meaning());


            ((Holder) holder).textView_meaning.setText(Html.fromHtml(entity.getName_of_meaning()));
           // ((Holder) holder).parts_of_speech_textView.setText(Html.fromHtml(" ( <small><small><font color=#cb32c9>" + entity.getParts_of_speech() + "</font>" + "</small></small>)"));
            if (entity.getFav_meaning() == 0)
                ((Holder) holder).fav_meaning_image_textView.setImageResource(R.drawable.likes_on_itemview);
            else
                ((Holder) holder).fav_meaning_image_textView.setImageResource(R.drawable.likesfill);


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
        public void onclick(int adapterposition, Entity entity, View view);

        public void onclick(String extra,String category,int num_of_words);
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton fav_meaning_image_textView;
        TextView textView_meaning;
        TextView parts_of_speech_textView;
        LinearLayout rootLayout;
        private ImageButton shareButton;


        Holder(@NonNull View itemView) {
            super(itemView);
            // textView_example=itemView.findViewById(R.id.textview_example);
            textView_meaning = itemView.findViewById(R.id.textview_meaning);
            shareButton=itemView.findViewById(R.id.share);
            parts_of_speech_textView = itemView.findViewById(R.id.partsof_speech_text_view);
            fav_meaning_image_textView = itemView.findViewById(R.id.add_fav_meaning);
            rootLayout = itemView.findViewById(R.id.rootlayout_main_fragment);
            rootLayout.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.audio_play);
            shareButton.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    MainFragment.get((Entity) getCurrentList().get(getAdapterPosition()));
                    menu.add(Menu.NONE, v.getId(), Menu.FIRST, "Share");
                    menu.add(Menu.NONE, v.getId(), Menu.FIRST, "Add to unknown words");
                }
            });
            imageView.setOnClickListener(this);
            fav_meaning_image_textView.setOnClickListener(this);
            //  textView_meaning.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            listener.onclick(getAdapterPosition(), (Entity) getItem(getAdapterPosition()), v);
        }
    }

}

