package com.github.geekarist.henripotier;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CartActivity extends Activity {

    @Bind(R.id.continue_shopping)
    Button continueShoppingButton;
    @Bind(R.id.cart_list)
    ListView mListView;

    private CartAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        ButterKnife.bind(this);

        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(CartActivity.this) {
                    @Override
                    public Cursor loadInBackground() {
                        // TODO Observe db change: http://stackoverflow.com/a/27385022/1665730
                        return PotierApplication.instance().getDbHelper().createCursor();
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                mAdapter = new CartAdapter(CartActivity.this, PotierApplication.instance().getDbHelper(), data);
                mListView.setAdapter(mAdapter);

                Book purchasedBook = (Book) getIntent().getSerializableExtra("purchasedBook");
                mAdapter.add(purchasedBook);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                // TODO
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
