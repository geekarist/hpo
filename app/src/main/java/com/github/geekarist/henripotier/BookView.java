package com.github.geekarist.henripotier;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;

public class BookView extends LinearLayout {
    @Bind(R.id.cart_item_title_view)
    TextView mTitleView;
    @Bind(R.id.cart_item_price_view)
    TextView mPriceView;
    @Bind(R.id.cart_item_image_view)
    ImageView mImageView;
    @Bind(R.id.remove_from_cart)
    Button removeItemButton;

    private Book mBook;

    public BookView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public Book getBook() {
        return mBook;
    }

    public void setBook(Book book) {
        this.mBook = book;

        final CartDatabaseHelper mDbHelper = PotierApplication.instance().getDbHelper();

        mTitleView.setText(book.title);
        mPriceView.setText(getContext().getResources().getString(R.string.price, book.price));
        Picasso.with(getContext()).load(book.cover).resize(200, 200).centerInside()
                .placeholder(R.drawable.book_cover_placeholder).into(mImageView);
    }
}
