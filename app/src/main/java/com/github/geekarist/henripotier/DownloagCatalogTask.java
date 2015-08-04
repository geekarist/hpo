package com.github.geekarist.henripotier;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.http.GET;

public class DownloagCatalogTask extends AsyncTask<URL, Integer, List<Book>> {

    private static final String TAG = "PotierDownload";

    public interface BooksHandler {
        void process(List<Book> books);
    }

    private BooksHandler booksHandler;

    public DownloagCatalogTask(BooksHandler handler) {
        booksHandler = handler;
    }

    private static HashMap<String, Object> createBook(String value, String value2, int value3) {
        HashMap<String, Object> book2 = new HashMap<>();
        book2.put("title", value);
        book2.put("price", value2);
        book2.put("coverUrl", value3);
        return book2;
    }

    protected List<HashMap<String, Object>> doInBackground2(URL... urls) {
        List<HashMap<String, Object>> books = new ArrayList<>();
        books.add(createBook("Henri Potier 1", "35 EUR", R.drawable.hp1));
        books.add(createBook("Henri Potier 2", "32 EUR", R.drawable.hp2));
        return books;
    }

    @Override
    protected List<Book> doInBackground(URL... urls) {
        URL url = urls[0];
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(url.toString()).build();
        HenriPotier henriPotier = restAdapter.create(HenriPotier.class);
        List<Book> books = henriPotier.books();

        // Download covers
        for (Book b : books) {
            try {
                InputStream in = new URL(b.cover).openStream();
                b.coverBitmap = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                Log.e(TAG, "Error while downloading book cover [" + b.cover + "]", e);
                e.printStackTrace();
            }
        }

        return books;
    }

    @Override
    protected void onPostExecute(List<Book> result) {
        booksHandler.process(result);
    }

    private interface HenriPotier {
        @GET("/books") List<Book> books();
    }

}
