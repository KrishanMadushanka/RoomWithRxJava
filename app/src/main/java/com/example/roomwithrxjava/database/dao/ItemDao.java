package com.example.roomwithrxjava.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.roomwithrxjava.database.entity.Item;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM item")
    Single<List<Item>> getAll();

    @Insert
    Single<Long> insertItem(Item item);

    @Query("DELETE FROM item")
    Single<Integer> delete();
}
