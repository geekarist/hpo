package com.github.geekarist.henripotier;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends ListActivity implements DownloagCatalogTask.BooksHandler {

    private List<Book> catalog = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        final BaseAdapter adapter = new ArrayAdapter<Book>(this, R.layout.activity_book_item, catalog) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = inflater.inflate(R.layout.activity_book_item, null);
                }

                Book b = catalog.get(position);

                TextView titleView = (TextView) v.findViewById(R.id.titleView);
                titleView.setText(b.title);

                TextView priceView = (TextView) v.findViewById(R.id.priceView);
                priceView.setText(String.valueOf(b.price));

                ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
                imageView.setImageBitmap(b.coverBitmap);

                return v;
            }
        };
        setListAdapter(adapter);

        try {
            new DownloagCatalogTask(this).execute(new URL("http://henri-potier.xebia.fr"));
        } catch (MalformedURLException e) {
            Toast.makeText(this, R.string.error_retrieving_catalog, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void process(List<Book> books) {
        catalog.addAll(books);
        BaseAdapter adapter = (BaseAdapter) getListView().getAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
