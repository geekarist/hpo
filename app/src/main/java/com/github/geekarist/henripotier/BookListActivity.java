package com.github.geekarist.henripotier;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends ListActivity implements DownloadCatalogTask.BooksProcessor {

    private List<Book> mCatalog = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        final BaseAdapter adapter = new BookArrayAdapter(BookListActivity.this, BookListActivity.this.mCatalog);
        setListAdapter(adapter);

        new DownloadCatalogTask(this).execute();
    }

    @Override
    public void processBooks(List<Book> books) {
        mCatalog.addAll(books);
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
