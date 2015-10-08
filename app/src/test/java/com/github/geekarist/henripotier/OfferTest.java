package com.github.geekarist.henripotier;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class OfferTest {
    @Test
    public void shouldApplyMinus() {
        // Given
        Offer offer = new Offer(Offer.Type.minus, 10);
        List<Book> books = Collections.singletonList(new Book(1, "isbn1", "title1", 50, "cover1"));
        // When
        int amount = offer.apply(books);
        // Then
        Assertions.assertThat(amount).isEqualTo(10);
    }

    @Test
    public void shouldApplyPercentage() {
        // Given
        Offer offer = new Offer(Offer.Type.percentage, 10);
        List<Book> books = Collections.singletonList(new Book(1, "isbn1", "title1", 50, "cover1"));
        // When
        int amount = offer.apply(books);
        // Then
        Assertions.assertThat(amount).isEqualTo(5);
    }

    @Test
    public void shouldApplySlice() {
        // Given
        Offer offer = new Offer(Offer.Type.slice, 10, 5);
        List<Book> books = Collections.singletonList(new Book(1, "isbn1", "title1", 49, "cover1"));
        // When
        int amount = offer.apply(books);
        // Then
        Assertions.assertThat(amount).isEqualTo(20);
    }
}