package com.github.geekarist.henripotier;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

class BookCatalogAdapter extends BaseAdapter {
    @Bind(R.id.titleView)
    TextView mTitleView;
    @Bind(R.id.priceView)
    TextView mPriceView;
    @Bind(R.id.imageView)
    ImageView mImageView;
    @Bind(R.id.add_to_cart)
    Button mAddToCartButton;
    private List<Book> mCatalog;

    public BookCatalogAdapter(Activity context) {
        mCatalog = new ArrayList<>();
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

    public void clear() {
        mCatalog.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Book> books) {
        mCatalog.addAll(books);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.activity_book_item, null);
        }

        ButterKnife.bind(this, v);

        Book b = mCatalog.get(position);

        mTitleView.setText(b.title);
        mPriceView.setText(parent.getResources().getString(R.string.price, b.price));
        Picasso.with(v.getContext()).load(b.cover).placeholder(R.drawable.book_cover_placeholder).into(mImageView);

        final View finalV = v;
        mAddToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(finalV.getContext(), "You want " + mCatalog.get(position).title, Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }
}
