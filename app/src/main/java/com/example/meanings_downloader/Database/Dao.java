package com.example.meanings_downloader.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.meanings_downloader.SaveSpokenWordsEntity;
import com.example.meanings_downloader.Setnum_entity;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Insert
    public Long insert(Entity entity);
    @Insert
    public Long insert(SaveSpokenWordsEntity entity);
    @Insert
    public Long insert_setnumat_present(Setnum_entity entity);
    @Update
    public void update_setnum_at_present(Setnum_entity entity);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void update(Entity entity);

    @Delete
    public void delete(Entity entity);

    @Query("SELECT *FROM Entity ORDER BY name DESC")
    public LiveData<List<Entity>> load_all_data();

@Query("SELECT * FROM ENTITY WHERE fav_meaning=:fav_meaning_identifier")
    public LiveData<List<Entity>> load_fav_meaning(int fav_meaning_identifier);

@Query("SELECT * FROM ENTITY WHERE learned_words=:islearned ")
    LiveData<List<Entity>> load_learned_words(boolean islearned);

@Query("SELECT *FROM ENTITY WHERE repeat_words=:isrepeat")
    LiveData<List<Entity>> load_repeat_words(boolean isrepeat);

@Query("SELECT *FROM SPOKEN_WORDS")
    LiveData<List<SaveSpokenWordsEntity>> load_spoken_words();

    @Query("SELECT set_num_at_present FROM SETNUMBER WHERE id=:i")
   int load_setnum_at_present(int i);
@Query("SELECT * FROM Entity WHERE isLearnedset=:learnedset")
    LiveData<List<Entity>> getLearned_set(Boolean learnedset);

    @Query("UPDATE  Entity  SET isLearnedset=:learned  WHERE id=:id")
   void update_set_at_id(Boolean learned,int id);

    @Query("UPDATE  Entity  SET set_number=:set_num WHERE id=:id")
    void update_setnum_at_id(int set_num,int id);

    @Query("SELECT *FROM ENTITY WHERE set_number=:set_nu")
    List<Entity> load_set_partticular_data(int set_nu);






}