package com.github.geekarist.henripotier;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Path;

import static org.mockito.BDDMockito.given;

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

        fakeCart = Mockito.mock(Cart.class);
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
        allCommercialOffers.offers.add(new Offer(Offer.Type.minus, 10));
        allCommercialOffers.offers.add(new Offer(Offer.Type.minus, 11));
        allBooks.add(new Book(0, "isbn", "title", 20, "cover"));
        given(fakeCart.total()).willReturn(20);

        // When
        final Integer[] bestDiscount = {null};
        bestCommercialOffer.apply(new BestCommercialOffer.Callback<Integer>() {
            @Override
            public void success(Integer discount) {
                bestDiscount[0] = discount;
            }
        });

        // Then
        Assertions.assertThat(bestDiscount[0]).isEqualTo(9);
    }
}