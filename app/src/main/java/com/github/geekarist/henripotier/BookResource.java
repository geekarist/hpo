package com.github.geekarist.henripotier;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface BookResource {

    @GET("/books")
    void books(Callback<List<Book>> doOnBooks);

    @GET("/books/{isbnValues}/commercialOffers")
    void commercialOffers(@Path("isbnValues") String isbnValues, Callback<CommercialOffers> callback);
}
