package com.github.geekarist.henripotier;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BookTest {

    @Test
    public void shouldWriteToParcel() {
        // Given
        Parcel dest = Parcel.obtain();
        Book book = new Book(42, "isbn", "title", 51d, "cover");

        // When
        book.writeToParcel(dest, 0);
        dest.setDataPosition(0);

        // Then
        assertThat(dest.readInt()).isEqualTo(42);
        assertThat(dest.readString()).isEqualTo("isbn");
        assertThat(dest.readString()).isEqualTo("title");
        assertThat(dest.readDouble()).isEqualTo(51);
        assertThat(dest.readString()).isEqualTo("cover");
    }

    @Test
    public void shouldCreateFromParcel() {
        // Given
        Parcel source = Parcel.obtain();
        source.writeInt(42);
        source.writeString("isbn");
        source.writeString("title");
        source.writeDouble(51);
        source.writeString("cover");
        source.setDataPosition(0);

        // When
        Book book = new Book(source);

        // Then
        assertThat(book.id).isEqualTo(42);
        assertThat(book.isbn).isEqualTo("isbn");
        assertThat(book.title).isEqualTo("title");
        assertThat(book.price).isEqualTo(51);
        assertThat(book.cover).isEqualTo("cover");
    }

}