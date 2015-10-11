package com.github.geekarist.henripotier;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

class CartDatabaseHelper extends SQLiteOpenHelper implements Cart {
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

    private void execInTransaction(String sql, String message, Object[] bindArgs) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.execSQL(sql, bindArgs);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Timber.e(message, e);
        } finally {
            db.endTransaction();
        }
    }

    public Cursor createCursor() {
        return getReadableDatabase().rawQuery("SELECT * FROM book", null);
    }

    public Book getBook(Cursor cursor) {
        int id = cursor.getInt(0);
        String isbn = cursor.getString(1);
        String title = cursor.getString(2);
        Integer price = cursor.getInt(3);
        String cover = cursor.getString(4);
        return new Book(id, isbn, title, price, cover);
    }

    @Override
    public void insert(Book book) {
        execInTransaction("INSERT INTO book (isbn, title, price, cover) VALUES (?, ?, ?, ?)",
                "Error while inserting book", new Object[]{book.isbn, book.title, book.price, book.cover});
    }

    @Override
    public void delete(Book book) {
        execInTransaction("DELETE FROM book WHERE book._id = ?", "Error while deleting book", new Object[]{book.id});
    }

    @Override
    public int total() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT SUM(price) FROM book", null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return 0;
        }
        return cursor.getInt(0);
    }

    @Override
    public List<Book> books() {
        List<Book> result = new ArrayList<>();
        Cursor cursor = createCursor();
        boolean hasNext = cursor.moveToFirst();
        while (hasNext) {
            result.add(getBook(cursor));
            hasNext = cursor.moveToNext();
        }
        return result;
    }
}
