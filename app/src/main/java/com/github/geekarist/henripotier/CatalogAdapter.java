package com.github.geekarist.henripotier;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

class CatalogAdapter extends BaseAdapter {

    private final List<Book> mCatalog;
    private final Context mContext;

    @Bind(R.id.book_view)
    BookView mBookView;

    public CatalogAdapter(Context context) {
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
            v = inflater.inflate(R.layout.activity_catalog_item, null);
        }

        ButterKnife.bind(this, v);

        final Book b = mCatalog.get(position);
        mBookView.setBook(b);
        mBookView.setChooseButtonLabel(R.string.add_to_cart);
        mBookView.setOnChooseButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CartActivity.class);
                intent.putExtra(PotierApplication.EXTRA_PURCHASED, b);
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
