package com.github.geekarist.henripotier;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.GET;

public class BookListActivity extends ListActivity {

    public static final String ENDPOINT = "http://henri-potier.xebia.fr";

    private static final String TAG = "HenriPotierBooks";
    private List<Book> mCatalog = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        final BaseAdapter adapter = new BookArrayAdapter(BookListActivity.this, BookListActivity.this.mCatalog);
        setListAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        downloadCatalog();
    }

    private void downloadCatalog() {
        // Setup REST service
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        HenriPotier henriPotier = restAdapter.create(HenriPotier.class);

        // Download books
        henriPotier.books(new Callback<List<Book>>() {
            @Override
            public void success(List<Book> books, Response response) {
                mCatalog.clear();
                mCatalog.addAll(books);

                // Download covers
                for (final Book b : books) {
                    Picasso.with(BookListActivity.this).setIndicatorsEnabled(BuildConfig.DEBUG);
                    Picasso.with(BookListActivity.this).load(b.cover).fetch(new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            BaseAdapter adapter = (BaseAdapter) getListView().getAdapter();
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError() {
                            Log.e(TAG, "Error while fetching book cover [" + b.cover + "]");
                        }
                    });
                }
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

    private interface HenriPotier {
        @GET("/books")
        void books(Callback<List<Book>> doOnBooks);
    }

}
