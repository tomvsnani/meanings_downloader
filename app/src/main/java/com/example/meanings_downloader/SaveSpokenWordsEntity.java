package com.example.meanings_downloader;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SPOKEN_WORDS")
public class SaveSpokenWordsEntity {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSave_spoken_word() {
        return save_spoken_word;
    }

    public void setSave_spoken_word(String save_spoken_word) {
        this.save_spoken_word = save_spoken_word;
    }

    @PrimaryKey(autoGenerate = true)
    int id;
    String save_spoken_word;
}
