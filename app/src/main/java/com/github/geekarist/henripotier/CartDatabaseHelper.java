package com.github.geekarist.henripotier;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timber.log.Timber;

class CartDatabaseHelper extends SQLiteOpenHelper {
    public CartDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE book (_id INTEGER PRIMARY KEY AUTOINCREMENT, isbn TEXT, title TEXT, price INTEGER, cover TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS book");
    }

    public void insert(Book book) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.execSQL("INSERT INTO book (isbn, title, price, cover) VALUES (?, ?, ?, ?)",
                    new Object[]{book.isbn, book.title, book.price, book.cover});
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Timber.e("Error while inserting book", e);
        } finally {
            db.endTransaction();
        }
    }

    public Book getBook(Cursor cursor) {
        String isbn = cursor.getString(1);
        String title = cursor.getString(2);
        Integer price = cursor.getInt(3);
        String cover = cursor.getString(4);
        return new Book(isbn, title, price, cover);
    }
}
