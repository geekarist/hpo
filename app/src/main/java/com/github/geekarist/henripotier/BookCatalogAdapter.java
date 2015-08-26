package com.github.geekarist.henripotier;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

class BookCatalogAdapter extends BaseAdapter {
    private final List<Book> mCatalog;
    private final Context mContext;

    @Bind(R.id.titleView)
    TextView mTitleView;
    @Bind(R.id.priceView)
    TextView mPriceView;
    @Bind(R.id.imageView)
    ImageView mImageView;
    @Bind(R.id.add_to_cart)
    Button mAddToCartButton;

    public BookCatalogAdapter(Context context) {
        mCatalog = new ArrayList<>();
        mContext = context;
        Picasso.with(context).setIndicatorsEnabled(BuildConfig.DEBUG);
    }

    @Override
    public int getCount() {
        return mCatalog.size();
    }

    @Override
    public Object getItem(int position) {
        return mCatalog.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addAll(List<Book> books) {
        mCatalog.clear();
        mCatalog.addAll(books);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.activity_book_item, null);
        }

        ButterKnife.bind(this, v);

        final Book b = mCatalog.get(position);

        mTitleView.setText(b.title);
        mPriceView.setText(parent.getResources().getString(R.string.price, b.price));
        Picasso.with(mContext).load(b.cover).placeholder(R.drawable.book_cover_placeholder).into(mImageView);

        mAddToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CartActivity.class);
                intent.putExtra("purchasedBook", b);
                mContext.startActivity(intent);
            }
        });

        return v;
    }

    public void clear() {
        mCatalog.clear();
        notifyDataSetChanged();
    }
}
