package com.github.geekarist.henripotier;

import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloagCatalogTask extends AsyncTask<URL, Integer, List<HashMap<String, Object>>> {

    public interface BooksHandler {
        void process(List<HashMap<String, Object>> books);
    }

    private BooksHandler booksHandler;

    public DownloagCatalogTask(BooksHandler handler) {
        booksHandler = handler;
    }

    private static HashMap<String, Object> createBook(String value, String value2, int value3) {
        HashMap<String, Object> book2 = new HashMap<>();
        book2.put("title", value);
        book2.put("price", value2);
        book2.put("cover", value3);
        return book2;
    }

    @Override
    protected List<HashMap<String, Object>> doInBackground(URL... urls) {
        List<HashMap<String, Object>> books = new ArrayList<>();
        books.add(createBook("Henri Potier 1", "35 EUR", R.drawable.hp1));
        books.add(createBook("Henri Potier 2", "32 EUR", R.drawable.hp2));
        return books;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, Object>> result) {
        booksHandler.process(result);
    }
}
