package com.github.geekarist.henripotier;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BookView extends LinearLayout {
    @Bind(R.id.book_title_view)
    TextView mTitleView;
    @Bind(R.id.book_price_view)
    TextView mPriceView;
    @Bind(R.id.book_image_view)
    ImageView mImageView;
    @Bind(R.id.book_selection_button)
    Button mButton;

    private Book mBook;

    public BookView(Context context, AttributeSet attributes) {
        super(context, attributes);
        LayoutInflater.from(context).inflate(R.layout.book, this, true);
        ButterKnife.bind(this);
    }

    public Book getBook() {
        return mBook;
    }

    public void setBook(Book book) {
        this.mBook = book;

        mTitleView.setText(book.title);
        mPriceView.setText(getContext().getResources().getString(R.string.price, book.price));
        Picasso.with(getContext()).load(book.cover).resize(200, 200).centerInside()
                .placeholder(R.drawable.book_cover_placeholder).into(mImageView);
    }

    public void setOnChooseButtonListener(OnClickListener onClickListener) {
        mButton.setOnClickListener(onClickListener);
    }

    public void setChooseButtonLabel(int resId) {
        mButton.setText(resId);
    }
}
