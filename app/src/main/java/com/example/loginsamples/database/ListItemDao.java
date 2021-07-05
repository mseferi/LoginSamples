package com.example.loginsamples.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.loginsamples.entity.ListItem;

import java.util.List;

@Dao
public interface ListItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ListItem> listItems);

    @Delete
    void delete(ListItem listItem);

    @Query("SELECT * FROM ListItem")
    List<ListItem> getAll();
}
