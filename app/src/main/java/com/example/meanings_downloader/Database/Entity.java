package com.example.meanings_downloader.Database;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@androidx.room.Entity(tableName = "Entity")
public class Entity {
    public static DiffUtil.ItemCallback<Entity> diffcall = new DiffUtil.ItemCallback<Entity>() {

        @Override
        public boolean areItemsTheSame(@NonNull Entity oldItem, @NonNull Entity newItem) {
            return oldItem.getId() == newItem.getId();
        }


        @Override
        public boolean areContentsTheSame(@NonNull Entity oldItem, @NonNull Entity newItem) {
            boolean boolen = oldItem.equals(newItem);
            return boolen;
        }
    };
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name_of_meaning;
    @ColumnInfo(name = "meaning")
    private String meaning_of_word;
    private String example;
    private String userTypedData;
    private String Parts_of_speech;
    private String sound;
    private boolean learned_words;

    public Boolean getLearnedset() {
        return isLearnedset;
    }

    public void setLearnedset(Boolean learnedset) {
        isLearnedset = learnedset;
    }

    private boolean repeat_words;
    @ColumnInfo(name = "fav_meaning")

    private Integer fav_meaning;

   Boolean isLearnedset;



    Integer set_number;

    public Integer getSet_number() {
        return set_number;
    }

    public void setSet_number(Integer set_number) {
        this.set_number = set_number;
    }

    public Entity() {
    }

    @Ignore
    public Entity(int id, String name_of_meaning, String meaning_of_word, String example) {
        this.id = id;
        this.name_of_meaning = name_of_meaning;
        this.meaning_of_word = meaning_of_word;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getUserTypedData() {
        return userTypedData;
    }

    public void setUserTypedData(String userTypedData) {
        this.userTypedData = userTypedData;
    }

    public String getParts_of_speech() {
        return Parts_of_speech;
    }

    public void setParts_of_speech(String parts_of_speech) {
        Parts_of_speech = parts_of_speech;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public boolean isLearned_words() {
        return learned_words;
    }

    public void setLearned_words(boolean learned_words) {
        this.learned_words = learned_words;
    }

    public boolean isRepeat_words() {
        return repeat_words;
    }

    public void setRepeat_words(boolean repeat_words) {
        this.repeat_words = repeat_words;
    }

    ;

    public Integer getFav_meaning() {
        return fav_meaning;
    }

    public void setFav_meaning(Integer fav_meaning) {
        this.fav_meaning = fav_meaning;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeaning_of_word() {
        return meaning_of_word;
    }

    public void setMeaning_of_word(String meaning_of_word) {
        this.meaning_of_word = meaning_of_word;
    }

    public String getName_of_meaning() {
        return name_of_meaning;
    }

    public void setName_of_meaning(String name_of_meaning) {
        this.name_of_meaning = name_of_meaning;
    }

    public boolean equals(Entity obj) {
        if (obj == this)
            return true;
        Entity objec = (Entity) obj;
        return objec.getId() == this.getId() && objec.getName_of_meaning().equals(this.getName_of_meaning());
    }
}
