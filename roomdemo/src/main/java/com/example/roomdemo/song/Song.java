package com.example.roomdemo.song;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "song")
public class Song {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    @ColumnInfo(name = "release_year")
    private int releaseYear;

    public Song(String name, int releaseYear) {
        this.name = name;
        this.releaseYear = releaseYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
}
