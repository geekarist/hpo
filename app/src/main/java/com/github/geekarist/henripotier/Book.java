package com.github.geekarist.henripotier;

import java.io.Serializable;

public final class Book implements Serializable {
    final String isbn;
    final String title;
    final Integer price;
    final String cover;

    public Book(String isbn, String title, Integer price, String cover) {
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.cover = cover;
    }
}

