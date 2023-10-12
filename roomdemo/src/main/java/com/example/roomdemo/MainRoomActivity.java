package com.example.roomdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.roomdemo.book.Book;
import com.example.roomdemo.book.BookDatabase;
import com.example.roomdemo.info.InfoDatabase;
import com.example.roomdemo.note.Note;
import com.example.roomdemo.note.NoteDatabase;
import com.example.roomdemo.song.MusicDatabase;
import com.example.util.KLog;

import java.util.List;

public class MainRoomActivity extends AppCompatActivity {
    private InfoDatabase mInfoDatabase;
    private NoteDatabase mNoteDatabase;
    private BookDatabase mBookDatabase;
    private MusicDatabase mMusicDatabase;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_database);

        Button btn_database_insert = findViewById(R.id.btn_database_insert);
        Button btn_database_delete = findViewById(R.id.btn_database_delete);
        Button btn_database_update = findViewById(R.id.btn_database_update);
        Button btn_database_query = findViewById(R.id.btn_database_query);

        btn_database_insert.setOnClickListener(mOnClickListener);
        btn_database_delete.setOnClickListener(mOnClickListener);
        btn_database_update.setOnClickListener(mOnClickListener);
        btn_database_query.setOnClickListener(mOnClickListener);

        mBookDatabase = Room.databaseBuilder(this, BookDatabase.class,"room_book.db")
                .allowMainThreadQueries()
                .build();

        mMusicDatabase = Room.databaseBuilder(this, MusicDatabase.class, "room_music.db").allowMainThreadQueries().build();

        mNoteDatabase = NoteDatabase.getInstance(this);

        mInfoDatabase = Room.databaseBuilder(this, InfoDatabase.class, "room_info.db")
                .allowMainThreadQueries()   // 这个是强制运行在主线程操作，这个一般不推荐，都是需要重开子线程来操作的
                .build();
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if (R.id.btn_database_insert == viewId) {
//                    note = new Note();
////                    note.setNote_id(index++);
//                    note.setContent("--->" + index);
//                    note.setTitle("title:" + index);
//                    mNoteDatabase.getNoteDao().insertNote(note);

                Book book = new Book(0, "小说", 29.0);
                mBookDatabase.getDao().insertBook(book);

//                Info info = new Info();
//                info.setName("test");
//                info.setUrl("http://www.test.com/");
//                info.setPrice(100);
//                info.setReleaseYear(2008);
//                info.setContent("content");
//                info.setDate(new Date(System.currentTimeMillis()));
//                mInfoDatabase.infoDao().insert(info);
            } else if (R.id.btn_database_delete == viewId) {

            } else if (R.id.btn_database_update == viewId) {

            } else if (R.id.btn_database_query == viewId) {
//                    List<Note> list = mNoteDatabase.getNoteDao().getNotes();
//                    for (int i = 0; i < list.size(); i++) {
//                        note = list.get(i);
//                        KLog.i("[" + i + "]:" + note.toString());
//                    }

                List<Book> list = mBookDatabase.getDao().queryAllBooksByName("小说");
                for (int i = 0; i < list.size(); i++) {
                    Book book = list.get(i);
                    KLog.i("[" + i + "]:" + book.toString());
                }

//                List<Info> list = mInfoDatabase.infoDao().query();
//                KLog.i("list.size:" + list.size());
//                for (int i = 0; i < list.size(); i++) {
//                    KLog.i("[" + i + "]:" + list.get(i).toString());
//                }
            }
        }
    };

}