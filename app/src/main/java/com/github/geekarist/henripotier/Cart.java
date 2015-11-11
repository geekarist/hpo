package com.github.geekarist.henripotier;

import java.util.List;

public interface Cart {
    void insert(Book book);

    void delete(Book book);

    double total();

    List<Book> books();
}
