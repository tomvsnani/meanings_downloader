package com.example.meanings_downloader;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meanings_downloader.Database.Entity;

import java.util.Locale;


public class Fav_meaning_Adapter extends ListAdapter implements TextToSpeech.OnInitListener {
    String extra;

    Entity entity;
    Adapter.Clicklistener clicklistener;
    SaveSpokenWordsEntity saveSpokenWords;
    Context context;
    TextToSpeech textToSpeech;


    protected Fav_meaning_Adapter(Adapter.Clicklistener clicklistener, String extra, Context context) {
        super(Entity.diffcall);
        this.clicklistener = clicklistener;
        this.extra = extra;
        this.context=context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_meaning_row_layout, parent, false);
     textToSpeech  =new TextToSpeech(context,this);
        return new viewholder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
if(extra.equals("speech"))
{
    saveSpokenWords=(SaveSpokenWordsEntity) getItem(position);
    ((viewholder) holder).meaning_title.setText(saveSpokenWords.getSave_spoken_word());
}
else {
    entity = (Entity) getItem(position);
    ((viewholder) holder).meaning_title.setText(entity.getName_of_meaning());
}

    }

    @Override
    public void onInit(int status) {
        if(status==TextToSpeech.SUCCESS){
            textToSpeech.setLanguage(Locale.ENGLISH);
        }
    }


    class viewholder extends RecyclerView.ViewHolder {
        TextView meaning_title;
        ImageView play_audio;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            meaning_title = itemView.findViewById(R.id.fav_textview_meaning);
            play_audio=itemView.findViewById(R.id.fav_audio_play);
            if (!extra.equals("speech")) {
                meaning_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        clicklistener.onclick(getAdapterPosition(), (Entity) getItem(getAdapterPosition()), v);
                    }

                });
            }
                play_audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("play_clicked","clicked");
                        if(extra.equals("speech"))
                        {
                            Log.d("play_clicked","clickeddd");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                textToSpeech.speak( ((SaveSpokenWordsEntity)(getItem(getAdapterPosition()))).getSave_spoken_word(),TextToSpeech.QUEUE_FLUSH,null,null);
                            }

                        }
                        else {
                            Log.d("play_clicked","clickedddd");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                textToSpeech.speak(((Entity) (getItem(getAdapterPosition()))).getName_of_meaning(), TextToSpeech.QUEUE_FLUSH, null, null);
                            }
                        }


                    }
                });




        }
    }

}
