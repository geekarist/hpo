package com.github.geekarist.henripotier;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CartActivity extends Activity {

    @Bind(R.id.continue_shopping)
    Button continueShoppingButton;
    @Bind(R.id.cart_list)
    ListView mListView;
    @Bind(R.id.total)
    TextView mTotalView;

    private CartAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(CartActivity.this) {
                    @Override
                    public Cursor loadInBackground() {
                        return PotierApplication.instance().getDbHelper().createCursor();
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                mAdapter = new CartAdapter(CartActivity.this, PotierApplication.instance().getDbHelper(), data);
                mListView.setAdapter(mAdapter);
                mAdapter.registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        updateTotal();
                    }
                });

                Book purchasedBook = (Book) getIntent().getSerializableExtra("purchasedBook");
                mAdapter.add(purchasedBook);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                // TODO
            }
        });

        continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateTotal() {
        mTotalView.setText(getResources().getString(
                R.string.cart_total, PotierApplication.instance().getDbHelper().total()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        getLoaderManager().destroyLoader(0);
    }
}
