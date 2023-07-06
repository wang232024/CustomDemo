package com.example.roomdemo.info;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.roomdemo.note.DateRoomConverter;

@Database(entities = {Info.class}, version = 1)
@TypeConverters({DateRoomConverter.class})
public abstract class InfoDatabase extends RoomDatabase {
    public static final String TABLE_NAME = "table_info";

    public abstract InfoDao infoDao();
}
