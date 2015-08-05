package com.github.geekarist.henripotier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class BookArrayAdapter extends ArrayAdapter<Book> {
    private final List<Book> mCatalog;

    public BookArrayAdapter(BookListActivity context, List<Book> catalog) {
        super(context, R.layout.activity_book_item, catalog);
        this.mCatalog = catalog;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.activity_book_item, null);
        }

        Book b = mCatalog.get(position);

        TextView titleView = (TextView) v.findViewById(R.id.titleView);
        titleView.setText(b.title);

        TextView priceView = (TextView) v.findViewById(R.id.priceView);
        priceView.setText(String.valueOf(b.price) + " EUR");

        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        imageView.setImageBitmap(b.coverBitmap);

        return v;
    }
}
