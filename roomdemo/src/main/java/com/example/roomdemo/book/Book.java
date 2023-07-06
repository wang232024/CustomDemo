package com.example.roomdemo.book;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Book") //这里不写就默认将类名作为表名
public class Book {
    @PrimaryKey(autoGenerate = true)//自增长的行号id
    private  int id;

    @ColumnInfo(name = "bookName") //这个标注可以理解为列,后面的名称通常是来匹配网络传输的数据的
    private String bookName;

    @ColumnInfo(name = "price")
    private double price;

    //还需要一个有参构造
    public Book(int id, String bookName, double price) {
        this.id = id;
        this.bookName = bookName;
        this.price = price;
    }

    //注意，一个类只能有一个有参构造，如果添加无参或者其他方法，要用@Ignore标志这个字段，意思是不将这个作为列
    //比如
    @Ignore
    public Book() {
    }

    //还需要对应的get和set

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
