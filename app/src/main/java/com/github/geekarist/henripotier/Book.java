package com.github.geekarist.henripotier;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public final class Book implements Parcelable {
    final int id;
    final String isbn;
    final String title;
    final Integer price;
    final String cover;

    public Book(int id, String isbn, String title, Integer price, String cover) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.cover = cover;
    }

    public Book(Parcel source) {
        id = source.readInt();
        isbn = source.readString();
        title = source.readString();
        price = source.readInt();
        cover = source.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(isbn);
        dest.writeString(title);
        dest.writeInt(price);
        dest.writeString(cover);
    }
}

