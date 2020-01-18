package com.example.meanings_downloader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@androidx.room.Database(entities = {Entity.class},version = 3,exportSchema = false)
public abstract class Database extends RoomDatabase {
    private static Database INSTANCE;
    private static Object object=new Object();
    private static String DATABASE_NAME="MEANING";

    private static Migration migration2_3=new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
database.execSQL("ALTER TABLE 'Entity' ADD COLUMN 'fav_meaning' INTEGER NOT NULL DEFAULT 0");
        }
    };
    public static  Database Database_create(Context context){
        if(INSTANCE==null){
        synchronized (object){
            INSTANCE= Room.databaseBuilder(context,Database.class,DATABASE_NAME).addMigrations(migration2_3).build() ;
        }
        }
        return INSTANCE;
    }
public abstract Dao dao();
}
