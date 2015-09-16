package com.github.geekarist.henripotier;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class CatalogActivity extends Activity {

    @Bind(R.id.list)
    ListView mListView;

    private CatalogAdapter mAdapter;
    private PotierApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_catalog_list);

        ButterKnife.bind(this);

        mApplication = PotierApplication.instance();
        mAdapter = new CatalogAdapter(this);

        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Download books
        mApplication.getHenriPotier().books(new Callback<List<Book>>() {
            @Override
            public void success(List<Book> books, Response response) {
                mAdapter.addAll(books);
            }

            @Override
            public void failure(RetrofitError error) {
                // UnknownException is not logged, see android.util.Log.getStackTraceString
                Timber.e(error, "Error while retrieving books");
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
