package com.github.geekarist.henripotier;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;

public class BookView extends View {
    private final Context mContext;
    private final Notifiable mNotifiable;

    @Bind(R.id.cart_item_title_view)
    TextView mTitleView;
    @Bind(R.id.cart_item_price_view)
    TextView mPriceView;
    @Bind(R.id.cart_item_image_view)
    ImageView mImageView;
    @Bind(R.id.remove_from_cart)
    Button removeItemButton;

    public BookView(Context context, Notifiable notifiable) {
        super(context);
        mContext = context;
        mNotifiable = notifiable;
    }

    public void xxx(final Book book) {
        final CartDatabaseHelper mDbHelper = PotierApplication.instance().getDbHelper();

        mTitleView.setText(book.title);
        mPriceView.setText(mContext.getResources().getString(R.string.price, book.price));
        Picasso.with(mContext).load(book.cover).resize(200, 200).centerInside()
                .placeholder(R.drawable.book_cover_placeholder).into(mImageView);

        removeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PotierApplication.instance().getDbHelper().delete(book);
                Cursor cursor = mDbHelper.createCursor();
                mNotifiable.notifyChange();
            }
        });
    }
}
