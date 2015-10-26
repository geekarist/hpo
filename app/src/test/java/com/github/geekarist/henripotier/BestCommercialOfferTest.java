package com.github.geekarist.henripotier;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.Path;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class BestCommercialOfferTest {
    BestCommercialOffer bestCommercialOffer;

    private BookResource fakeBookResource;
    private Cart fakeCart;

    private CommercialOffers allCommercialOffers;
    private List<Book> allBooks;

    @Before
    public void setup() {
        allBooks = new ArrayList<>();
        allCommercialOffers = new CommercialOffers();
        allCommercialOffers.offers = new ArrayList<>();

        fakeCart = mock(Cart.class);
        given(fakeCart.books()).willReturn(allBooks);

        fakeBookResource = new BookResource() {
            @Override
            public void books(Callback<List<Book>> doOnBooks) {
                doOnBooks.success(allBooks, null);
            }

            @Override
            public void commercialOffers(@Path("isbnValues") String isbnValues, Callback<CommercialOffers> callback) {
                callback.success(allCommercialOffers, null);
            }
        };

        bestCommercialOffer = new BestCommercialOffer(fakeCart, fakeBookResource);
    }

    @Test
    public void shouldApplyBestOffer() throws Exception {
        // Given
        allCommercialOffers.offers.add(new Offer(Offer.Type.minus, 10.2));
        allCommercialOffers.offers.add(new Offer(Offer.Type.minus, 11.4));
        allBooks.add(new Book(0, "isbn", "title", 20.5, "cover"));
        given(fakeCart.total()).willReturn(20.5);

        // When
        final Double[] bestDiscount = {null};
        bestCommercialOffer.apply(new BestCommercialOffer.Callback<Double>() {
            @Override
            public void success(Double discount) {
                bestDiscount[0] = discount;
            }

            @Override
            public void error(String message, Exception cause) {
                Assertions.fail(message, cause);
            }
        });

        // Then
        Assertions.assertThat(bestDiscount[0]).isEqualTo(9.1);
    }

    @Test
    public void shouldNotApplyOfferOnError() throws Exception {
        // Given
        allCommercialOffers.offers.add(new Offer(Offer.Type.minus, 5));
        allBooks.add(new Book(0, "isbn", "title", 20d, "cover"));
        given(fakeCart.total()).willReturn(20d);
        fakeBookResource = new BookResource() {
            @Override
            public void books(Callback<List<Book>> doOnBooks) {
                doOnBooks.failure(null);
            }

            @Override
            public void commercialOffers(@Path("isbnValues") String isbnValues, Callback<CommercialOffers> callback) {
                callback.failure(mock(RetrofitError.class));
            }
        };
        bestCommercialOffer.setBookResource(fakeBookResource);

        // When
        final Exception[] errorCause = new Exception[1];
        bestCommercialOffer.apply(new BestCommercialOffer.Callback<Double>() {
            @Override
            public void success(Double discount) {
                Assertions.fail("Error should have occured");
            }

            @Override
            public void error(String message, Exception cause) {
                errorCause[0] = cause;
            }
        });

        // Then
        Assertions.assertThat(errorCause[0]).isInstanceOf(RetrofitError.class);
    }
}