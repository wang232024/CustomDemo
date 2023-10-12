package com.example.roomdemo.note;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    String TABLE_NAME = "room_table_note";

    @Query("SELECT * FROM " + TABLE_NAME)
    List<Note> getNotes();

    /*
     * Insert the object in database
     * @param note, object to be inserted
     */
    @Insert
    long insertNote(Note note);

    /*
     * update the object in database
     * @param note, object to be updated
     */
    @Update
    void updateNote(Note repos);

    /*
     * delete the object from database
     * @param note, object to be deleted
     */
    @Delete
    void deleteNote(Note note);

    // Note... is varargs, here note is an array
    /*
     * delete list of objects from database
     * @param note, array of oject to be deleted
     */
    @Delete
    void deleteNotes(Note... note);

}
