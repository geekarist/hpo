package com.github.geekarist.henripotier;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;

public class DownloadCatalogTask extends AsyncTask<Void, Integer, List<Book>> {

    public static final String ENDPOINT = "http://henri-potier.xebia.fr";
    private static final String TAG = "PotierDownload";
    private BooksProcessor booksProcessor;

    public DownloadCatalogTask(BooksProcessor handler) {
        booksProcessor = handler;
    }

    @Override
    protected List<Book> doInBackground(Void... params) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
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
        booksProcessor.processBooks(result);
    }

    public interface BooksProcessor {
        void processBooks(List<Book> books);
    }

    private interface HenriPotier {
        @GET("/books") List<Book> books();
    }

}
