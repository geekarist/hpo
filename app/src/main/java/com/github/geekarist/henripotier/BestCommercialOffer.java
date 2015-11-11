package com.github.geekarist.henripotier;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

class BestCommercialOffer {
    private Cart cart;
    private BookResource bookResource;

    public BestCommercialOffer(Cart cart, BookResource bookResource) {
        this.cart = cart;
        this.bookResource = bookResource;
    }

    public void apply(final Callback<Double> callback) {
        final List<Book> books = cart.books();

        if (books.isEmpty()) {
            callback.success(0d);
        } else {
            bookResource.commercialOffers(isbnValues(books), new retrofit.Callback<CommercialOffers>() {
                @Override
                public void success(CommercialOffers commercialOffers, Response response) {
                    double bestOffer = 0;
                    for (Offer offer : commercialOffers.offers) {
                        double discount = offer.apply(books);
                        if (discount > bestOffer) {
                            bestOffer = discount;
                        }
                    }
                    callback.success(cart.total() - bestOffer);
                }

                @Override
                public void failure(RetrofitError error) {
                    callback.error("Error while retrieving commercial offers", error);
                }
            });
        }
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

    void setBookResource(BookResource bookResource) {
        this.bookResource = bookResource;
    }

    public interface Callback<T> {
        void success(T result);

        void error(String message, Exception cause);
    }
}
