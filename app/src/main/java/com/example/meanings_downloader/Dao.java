package com.example.meanings_downloader;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Insert
    public Long insert(Entity entity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void update(Entity entity);

    @Delete
    public void delete(Entity entity);

    @Query("SELECT *FROM Entity")
    public LiveData<List<Entity>> load_all_data();



}