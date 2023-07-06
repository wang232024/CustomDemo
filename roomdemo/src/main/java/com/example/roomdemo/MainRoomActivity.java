package com.example.roomdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.roomdemo.book.BookDatabase;
import com.example.roomdemo.info.Info;
import com.example.roomdemo.info.InfoDatabase;
import com.example.roomdemo.note.Note;
import com.example.roomdemo.note.NoteDatabase;
import com.example.roomdemo.song.MusicDatabase;
import com.example.util.KLog;

import java.util.Date;
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
        setContentView(R.layout.activity_main_room);

        mBookDatabase = Room.databaseBuilder(this, BookDatabase.class,"book_database")
                .allowMainThreadQueries()
                .build();

//        MusicDatabase mDB = Room.databaseBuilder(getApplicationContext(), MusicDatabase.class, "xx-db").allowMainThreadQueries().build();
        mMusicDatabase = Room.databaseBuilder(this, MusicDatabase.class, "xx-db").allowMainThreadQueries().build();

//        findViewById(R.id.insert).setOnClickListener(v -> {
//            new Thread(() -> {
//                Song s1 = new Song("xxxxx", 1991);
//                mDB.songDao().insert(s1);
//            }).start();
//        });
//        findViewById(R.id.query).setOnClickListener(v -> {
//            new Thread(() -> {
//                List<Song> list = mDB.songDao().loadAll();
//                for (Song s : list) {
//                    Log.i("TAG", "当前项在库中的id:" + s.getId() + ",歌曲名：" + s.getName() + ",发行年代：" + s.getReleaseYear());
//                }
//            }).start();
//        });

        Button btnInsert = findViewById(R.id.insert);
        Button btnDelete = findViewById(R.id.delete);
        Button btnUpdate = findViewById(R.id.update);
        Button btnQuery = findViewById(R.id.query);
        btnInsert.setOnClickListener(mOnClickListener);
        btnDelete.setOnClickListener(mOnClickListener);
        btnUpdate.setOnClickListener(mOnClickListener);
        btnQuery.setOnClickListener(mOnClickListener);

        mNoteDatabase = NoteDatabase.getInstance(this);

        mInfoDatabase = Room.databaseBuilder(this, InfoDatabase.class, "database_info")
                .allowMainThreadQueries()   // 这个是强制运行在主线程操作，这个一般不推荐，都是需要重开子线程来操作的
                .build();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Note note;
            int viewId = view.getId();
            if (viewId == R.id.insert) {
//                    note = new Note();
////                    note.setNote_id(index++);
//                    note.setContent("--->" + index);
//                    note.setTitle("title:" + index);
//                    mNoteDatabase.getNoteDao().insertNote(note);

//                    Book book = new Book(0, "小说", 29.0);
//                    mBookDatabase.getDao().insertBook(book);

                Info info = new Info();
                info.setName("test");
                info.setUrl("http://www.test.com/");
                info.setPrice(100);
                info.setReleaseYear(2008);
                info.setContent("content");
                info.setDate(new Date(System.currentTimeMillis()));
                mInfoDatabase.infoDao().insert(info);
            } else if (viewId == R.id.delete) {

            } else if (viewId == R.id.update) {

            } else if (viewId == R.id.query) {
//                    List<Note> list = mNoteDatabase.getNoteDao().getNotes();
//                    for (int i = 0; i < list.size(); i++) {
//                        note = list.get(i);
//                        KLog.i("[" + i + "]:" + note.toString());
//                    }

//                    List<Note> list = mBookDatabase.getDao()
//                    for (int i = 0; i < list.size(); i++) {
//                        note = list.get(i);
//                        KLog.i("[" + i + "]:" + note.toString());
//                    }

                List<Info> list = mInfoDatabase.infoDao().query();
                KLog.i("list.size:" + list.size());
                for (int i = 0; i < list.size(); i++) {
                    KLog.i("[" + i + "]:" + list.get(i).toString());
                }
            }
        }
    };

}