package com.example.roomdemo.song;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDao {
    @Query("SELECT * FROM song")
    List<Song> loadAll();

    @Query("SELECT * FROM song WHERE id IN (:songIds)")
    List<Song> loadAllBySongId(int... songIds);

    @Query("SELECT * FROM song WHERE name LIKE :name AND release_year = :year LIMIT 1")
    Song loadOneByNameAndReleaseYear(String name, int year);

    @Insert
    void insertAll(Song... songs);

    @Insert
    void insert(Song song);

    @Delete
    void delete(Song song);
}
