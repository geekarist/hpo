package com.github.geekarist.henripotier;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CartAdapter extends CursorAdapter {

    private final Context mContext;
    private final CartDatabaseHelper mDbHelper;

    // TODO snake case
    @Bind(R.id.cartItemTitleView)
    TextView mTitleView;
    @Bind(R.id.cartItemPriceView)
    TextView mPriceView;
    @Bind(R.id.cartItemImageView)
    ImageView mImageView;
    @Bind(R.id.remove_from_cart)
    Button removeItemButton;

    public CartAdapter(Context context, CartDatabaseHelper helper, Cursor cursor) {
        super(context, cursor, false);
        this.mDbHelper = helper;
        this.mContext = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.activity_cart_item, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ButterKnife.bind(this, view);
        final Book book = mDbHelper.getBook(cursor);
        mTitleView.setText(book.title);
        mPriceView.setText(mContext.getResources().getString(R.string.price, book.price));
        Picasso.with(mContext).load(book.cover).resize(200, 200).centerInside()
                .placeholder(R.drawable.book_cover_placeholder).into(mImageView);

        removeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PotierApplication.instance().getDbHelper().delete(book);
                Cursor cursor = mDbHelper.createCursor();
                swapCursor(cursor);
                notifyDataSetChanged();
            }
        });
    }

    public void add(Book purchasedBook) {
        mDbHelper.insert(purchasedBook);
        Cursor cursor = mDbHelper.createCursor();
        swapCursor(cursor);
        notifyDataSetChanged();
    }

}
