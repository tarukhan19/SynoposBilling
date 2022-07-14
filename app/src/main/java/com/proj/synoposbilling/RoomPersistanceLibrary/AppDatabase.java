package com.proj.synoposbilling.RoomPersistanceLibrary;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {KutumbDTO.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
