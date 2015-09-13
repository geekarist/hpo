package com.github.geekarist.henripotier;

import android.database.Cursor;

import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class CartDatabaseHelperTest {

    @Test
    public void shouldMapBook() throws Exception {
        CartDatabaseHelper dbHelper = new CartDatabaseHelper(null, null, null, 0);
        Cursor cursorMock = Mockito.mock(Cursor.class);
        given(cursorMock.getInt(0)).willReturn(42);
        given(cursorMock.getString(1)).willReturn("isbn");
        given(cursorMock.getString(2)).willReturn("title");
        given(cursorMock.getInt(3)).willReturn(51);
        given(cursorMock.getString(4)).willReturn("cover");

        Book book = dbHelper.getBook(cursorMock);

        assertThat(book.id).isEqualTo(42);
        assertThat(book.isbn).isEqualTo("isbn");
        assertThat(book.title).isEqualTo("title");
        assertThat(book.price).isEqualTo(51);
        assertThat(book.cover).isEqualTo("cover");
    }
}