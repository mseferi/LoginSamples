package com.example.loginsamples.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.loginsamples.entity.ListItem;

@Database(entities = {ListItem.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {
    public abstract ListItemDao listItemDao();
}
