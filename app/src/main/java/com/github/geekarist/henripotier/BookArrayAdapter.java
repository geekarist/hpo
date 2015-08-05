package com.github.geekarist.henripotier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

class BookArrayAdapter extends ArrayAdapter<Book> {
    private final List<Book> mCatalog;

    public BookArrayAdapter(BookListActivity context, List<Book> catalog) {
        super(context, R.layout.activity_book_item, catalog);
        this.mCatalog = catalog;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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

        Button addToCartButton = (Button) v.findViewById(R.id.add_to_cart);
        final View finalV = v;
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(finalV.getContext(), "You want " + mCatalog.get(position).title, Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }
}
