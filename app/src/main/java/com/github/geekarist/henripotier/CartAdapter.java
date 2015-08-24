package com.github.geekarist.henripotier;

import android.content.Context;
import android.database.Cursor;
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
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("SELECT * FROM book", null);
        return new CartAdapter(context, dbHelper, cursor);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
    }

    public void add(Book purchasedBook) {
        mDbHelper.insert(purchasedBook);
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
            db.execSQL("INSERT INTO book (isbn, title, price, cover) VALUES (?, ?, ?, ?)",
                    new Object[]{book.isbn, book.title, book.price, book.cover});
        }
    }
}
