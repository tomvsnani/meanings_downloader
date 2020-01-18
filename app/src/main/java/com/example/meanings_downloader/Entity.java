package com.example.meanings_downloader;


import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@androidx.room.Entity(tableName = "Entity")
public class Entity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name_of_meaning;

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    @ColumnInfo(name = "meaning")
    private String meaning_of_word;
    private String example;


    public Integer getFav_meaning() {
        return fav_meaning;
    }

    public void setFav_meaning(Integer fav_meaning) {
        this.fav_meaning = fav_meaning;
    }

    @ColumnInfo(name="fav_meaning")

    private Integer fav_meaning;

    public Entity(){};
@Ignore
public Entity(int id,String name_of_meaning,String meaning_of_word,String example){
    this.id=id;
    this.name_of_meaning=name_of_meaning;
    this.meaning_of_word=meaning_of_word;
}





    public void setName_of_meaning(String name_of_meaning) {
        this.name_of_meaning = name_of_meaning;
    }

    public void setMeaning_of_word(String meaning_of_word) {
        this.meaning_of_word = meaning_of_word;
    }

    public void setId(int id) {
        this.id = id;
    }


    public static DiffUtil.ItemCallback<Entity> diffcall = new DiffUtil.ItemCallback<Entity>() {

        @Override
        public boolean areItemsTheSame(@NonNull Entity oldItem, @NonNull Entity newItem) {
            return oldItem.getId() ==newItem.getId();
        }


        @Override
        public boolean areContentsTheSame(@NonNull Entity oldItem, @NonNull Entity newItem) {
            boolean boolen=oldItem.equals(newItem);
            return boolen;
        }
    };


    public int getId() {
        return id;
    }

    public String getMeaning_of_word() {
        return meaning_of_word;
    }

    public String getName_of_meaning() {
        return name_of_meaning;
    }


    public boolean equals(Entity obj) {
        if(obj==this)
            return true;
        Entity objec=(Entity)obj;
        return objec.getId()==this.getId() && objec.getName_of_meaning().equals(this.getName_of_meaning());
    }
}
