package com.github.geekarist.henripotier;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CartAdapter extends CursorAdapter {

    private final Context mContext;
    private final DatabaseHelper mDbHelper;

    @Bind(R.id.cartItemTitleView)
    TextView mTitleView;
    @Bind(R.id.cartItemPriceView)
    TextView mPriceView;
    @Bind(R.id.cartItemImageView)
    ImageView mImageView;

    private CartAdapter(Context context, DatabaseHelper helper, Cursor cursor) {
        super(context, cursor, false);
        this.mDbHelper = helper;
        this.mContext = context;
    }

    public static CartAdapter newInstance(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, null, null, 1);
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM book", null);
        return new CartAdapter(context, dbHelper, cursor);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView2(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.activity_cart_item, null);

        }
        ButterKnife.bind(this, view);
        Book book = (Book) getItem(position);
        mTitleView.setText(book.title);
        mPriceView.setText(mContext.getResources().getString(R.string.price, book.price));
        Picasso.with(mContext).load(book.cover).placeholder(R.drawable.book_cover_placeholder).into(mImageView);
        return view;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return inflater.inflate(R.layout.activity_cart_item, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ButterKnife.bind(this, view);
        Book book = mDbHelper.getBook(cursor);
        mTitleView.setText(book.title);
        mPriceView.setText(mContext.getResources().getString(R.string.price, book.price));
        Picasso.with(mContext).load(book.cover).placeholder(R.drawable.book_cover_placeholder).into(mImageView);
    }

    public void add(Book purchasedBook) {
        mDbHelper.insert(purchasedBook);
        Cursor cursor = mDbHelper.getReadableDatabase().rawQuery("SELECT * FROM book", null);
        swapCursor(cursor);
        notifyDataSetChanged();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
}
