package com.github.geekarist.henripotier;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

class BestCommercialOffer {
    private CartDatabaseHelper dbHelper;
    private PotierApplication.HenriPotier henriPotier;

    public BestCommercialOffer(CartDatabaseHelper dbHelper, PotierApplication.HenriPotier henriPotier) {
        this.dbHelper = dbHelper;
        this.henriPotier = henriPotier;
    }

    public void apply(final Callback<Integer> callback) {
        final List<Book> books = dbHelper.books();

        if (books.isEmpty()) {
            callback.success(0);
        } else {
            henriPotier.commercialOffers(isbnValues(books), new retrofit.Callback<CommercialOffers>() {
                @Override
                public void success(CommercialOffers commercialOffers, Response response) {
                    int bestOffer = 0;
                    for (CommercialOffers.Offer offer : commercialOffers.offers) {
                        int discount = offer.apply(books);
                        if (discount > bestOffer) {
                            bestOffer = discount;
                        }
                    }
                    callback.success(dbHelper.total() - bestOffer);
                }

                @Override
                public void failure(RetrofitError error) {
                    Timber.e(error, "Error while retrieving commercial offers");
                }
            });
        }
    }

    public interface Callback<T> {
        void success(T result);
    }

    private String isbnValues(List<Book> books) {
        StringBuilder isbnValues = new StringBuilder();
        for (Book b : books) {
            if (isbnValues.length() != 0) {
                isbnValues.append(",");
            }
            isbnValues.append(b.isbn);
        }
        return isbnValues.toString();
    }
}
