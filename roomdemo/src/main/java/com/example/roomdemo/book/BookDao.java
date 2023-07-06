package com.example.roomdemo.book;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

// Dao相当一个用于操作数据库的接口，一般命名为class+Dao
@Dao
public interface BookDao {
    //这里用SQL语句作为注解，让系统为我们生成对应的代码来匹配我们的方法
    @Insert
    void insertBook(Book book);

    @Update
    void updateBook(Book book);

    @Delete
    void deleteBool(Book book);

//    @Query("select bookName from Book")
//    List<Book> queryAllBooksByName();
//
//    //还可以返回LiveData类型
//    @Query("select * from Book")
//    LiveData<List<Book>> queryAllBooks();
}
