package com.example.meanings_downloader.Database;

import androidx.lifecycle.LiveData;
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

@Query("SELECT * FROM ENTITY WHERE fav_meaning=:fav_meaning_identifier")
    public LiveData<List<Entity>> load_fav_meaning(int fav_meaning_identifier);

}