package com.github.geekarist.henripotier;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BookListActivity extends ListActivity {

    private static final String TAG = "HenriPotierBooks";

    private BaseAdapter mAdapter;
    private PotierApplication mApplication;
    private List<Book> mCatalog = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        mApplication = PotierApplication.instance();
        mAdapter = new BookArrayAdapter(BookListActivity.this, mCatalog);
        setListAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Download books
        mApplication.getRestAdapter().books(new Callback<List<Book>>() {
            @Override
            public void success(List<Book> books, Response response) {
                mCatalog.clear();
                mCatalog.addAll(books);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error while retrieving books: " + error);
            }
        });
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
