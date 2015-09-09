package com.github.geekarist.henripotier;

import android.app.Activity;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends Activity implements CursorAdaptable {

    public static final int LOADER_ID = 0;

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

        getLoaderManager().initLoader(LOADER_ID, null, new CartLoaderCallbacks(this, this));
    }

    @Override
    public void onCursorLoaded(Cursor data) {
        mAdapter = new CartAdapter(CartActivity.this, PotierApplication.instance().getDbHelper(), data);
        mListView.setAdapter(mAdapter);
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                updateTotal();
            }
        });

        Book purchasedBook = (Book) getIntent().getSerializableExtra(PotierApplication.EXTRA_PURCHASED);
        mAdapter.add(purchasedBook);
    }

    @OnClick(R.id.continue_shopping)
    void continueShopping() {
        finish();
    }

    private void updateTotal() {
        mTotalView.setText(getResources().getString(
                R.string.cart_total, PotierApplication.instance().getDbHelper().total()));
    }

}
