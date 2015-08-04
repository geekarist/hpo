package com.github.geekarist.henripotier;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookListActivity extends ListActivity implements DownloagCatalogTask.BooksHandler {

    private List<Map<String, Object>> catalog = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        // TODO load bitmaps efficiently: http://goo.gl/UNDKd
        final BaseAdapter adapter = new SimpleAdapter(
                this,
                catalog,
                R.layout.activity_book_item,
                new String[]{"title", "price", "cover"},
                new int[]{R.id.textView, R.id.textView2, R.id.imageView});
        setListAdapter(adapter);

        try {
            new DownloagCatalogTask(this).execute(new URL("http://henri-potier.xebia.fr/books"));
        } catch (MalformedURLException e) {
            Toast.makeText(this, R.string.error_retrieving_catalog, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void process(List<HashMap<String, Object>> books) {
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
