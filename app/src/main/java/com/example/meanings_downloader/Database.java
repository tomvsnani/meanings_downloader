package com.example.meanings_downloader;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Entity.class},version = 2,exportSchema = false)
public abstract class Database extends RoomDatabase {
    private static Database INSTANCE;
    private static Object object=new Object();
    private static String DATABASE_NAME="MEANING";
    public static  Database Database_create(Context context){
        if(INSTANCE==null){
        synchronized (object){
            INSTANCE= Room.databaseBuilder(context,Database.class,DATABASE_NAME).fallbackToDestructiveMigration().build() ;
        }
        }
        return INSTANCE;
    }
public abstract Dao dao();
}
