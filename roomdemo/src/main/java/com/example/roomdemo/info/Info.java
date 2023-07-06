package com.example.roomdemo.info;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = InfoDatabase.TABLE_NAME)
public class Info implements Parcelable {

    @PrimaryKey(autoGenerate = true)    //自增长的行号id
    private int _id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "release_year")
    private int releaseYear;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    private String content;

    private String url;

    private Date date;

    public Info() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    protected Info(Parcel in) {
        _id = in.readInt();
        name = in.readString();
        price = in.readDouble();
        releaseYear = in.readInt();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(_id);
        parcel.writeString(name);
        parcel.writeDouble(price);
        parcel.writeInt(releaseYear);
        parcel.writeString(content);
        parcel.writeString(url);
    }

    @Override
    public String toString() {
        return "Info{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", releaseYear=" + releaseYear +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", date=" + date +
                '}';
    }
}
