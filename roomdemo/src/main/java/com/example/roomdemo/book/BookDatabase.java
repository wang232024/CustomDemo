package com.example.roomdemo.book;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Book.class }, version = 1)
public abstract class BookDatabase extends RoomDatabase {
    //我们对数据的操作为了避免被滥用实例化，使用单例模式，singleTon，并且加锁
    private static BookDatabase INSTANCE;
    //创建一个方法，这个方法返回一个BookDatabase
    public static BookDatabase getDatabase(Context context){
        if (INSTANCE == null){
            //如果为创建这个数据库，那么就呼叫builder来创建数据库
            INSTANCE = Room.databaseBuilder(context, BookDatabase.class,"BOOK_DATABASE")
                    .allowMainThreadQueries()//这个是强制运行在主线程操作，这个一般不推荐，都是需要重开子线程来操作的
                    .build();
        }
        return INSTANCE;
    }

    //另外我们还需要通过一个抽象方法来获取Dao
    public abstract BookDao getDao();

}
