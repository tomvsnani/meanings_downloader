package com.example.meanings_downloader;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SETNUMBER")
public class Setnum_entity {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSet_num_at_present() {
        return set_num_at_present;
    }

    public void setSet_num_at_present(int set_num_at_present) {
        this.set_num_at_present = set_num_at_present;
    }

    @PrimaryKey(autoGenerate = true)
   private int id;
  private  int set_num_at_present;
}
