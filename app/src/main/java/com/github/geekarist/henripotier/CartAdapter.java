package com.github.geekarist.henripotier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CartAdapter extends BaseAdapter {

    private final List<Book> mCartItems = new ArrayList<>();
    private final Context mContext;

    @Bind(R.id.cartItemTitleView)
    TextView mTitleView;
    @Bind(R.id.cartItemPriceView)
    TextView mPriceView;
    @Bind(R.id.cartItemImageView)
    ImageView mImageView;

    public CartAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mCartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mCartItems.get(position);
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

    public void add(Book purchasedBook) {
        mCartItems.add(purchasedBook);
        notifyDataSetChanged();
    }
}
