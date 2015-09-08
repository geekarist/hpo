package com.github.geekarist.henripotier;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CartAdapter extends CursorAdapter {

    private final Context mContext;
    private final CartDatabaseHelper mDbHelper;

    @Bind(R.id.cart_book_view)
    BookView mBookView;

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
        mBookView.setBook(book);
        mBookView.setChooseButtonLabel(R.string.remove_from_cart);
        mBookView.setOnChooseButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PotierApplication.instance().getDbHelper().delete(mBookView.getBook());
                notifyChange();
            }
        });
    }

    public void add(Book purchasedBook) {
        mDbHelper.insert(purchasedBook);
        notifyChange();
    }

    public void notifyChange() {
        Cursor cursor = mDbHelper.createCursor();
        swapCursor(cursor);
        notifyDataSetChanged();
    }

}
