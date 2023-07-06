package com.example.roomdemo.info;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface InfoDao {

    @Insert
    void insert(Info info);

    @Delete
    void delete(Info info);

    @Update
    void update(Info info);

    @Query("SELECT * FROM " + InfoDatabase.TABLE_NAME)
    List<Info> query();
//    LiveData<Info> query();

    @Query("SELECT * FROM " + InfoDatabase.TABLE_NAME + " WHERE _id IN (:id)")
    List<Info> queryById(int id);

    @Query("SELECT * FROM " + InfoDatabase.TABLE_NAME + " WHERE name LIKE :name AND release_year = :year LIMIT 1")
    List<Info> queryByName(String name, int year);

}
