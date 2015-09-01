package com.github.geekarist.henripotier;

import android.content.Intent;

public class CartActivityTest extends android.test.ActivityUnitTestCase<CartActivity> {

    public CartActivityTest() {
        super(CartActivity.class);
    }

    public void testShouldDisplayPurchasedBook() {
        // Given
        Intent purchaseBookIntent = new Intent();
        purchaseBookIntent.putExtra("purchasedBook", new Book(0, "isbn", "title", 5, "cover"));

        // When
        startActivity(purchaseBookIntent, null, null);

        // Then
        // TODO wait for Loader to be finished
        // assertEquals(1, getActivity().mListView.getCount());
    }
}