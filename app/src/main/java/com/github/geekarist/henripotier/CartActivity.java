package com.github.geekarist.henripotier;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

        mAdapter = new CartAdapter(this);
        mListView.setAdapter(mAdapter);
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

        Book purchasedBook = (Book) getIntent().getSerializableExtra("purchasedBook");
        Toast.makeText(this, getResources().getString(R.string.buy_book_msg, purchasedBook.title), Toast.LENGTH_LONG).show();
        mAdapter.add(purchasedBook);
    }
}
