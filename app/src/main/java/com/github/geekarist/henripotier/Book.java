package com.github.geekarist.henripotier;

public final class Book {
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

