package com.github.geekarist.henripotier;

import android.content.Context;
import android.database.Cursor;
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
    private final CartDatabaseHelper mDbHelper;

    @Bind(R.id.cartItemTitleView)
    TextView mTitleView;
    @Bind(R.id.cartItemPriceView)
    TextView mPriceView;
    @Bind(R.id.cartItemImageView)
    ImageView mImageView;

    private CartAdapter(Context context, CartDatabaseHelper helper, Cursor cursor) {
        super(context, cursor, false);
        this.mDbHelper = helper;
        this.mContext = context;
    }

    public static CartAdapter newInstance(Context context) {
        CartDatabaseHelper dbHelper = PotierApplication.instance().getDbHelper();
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

}
