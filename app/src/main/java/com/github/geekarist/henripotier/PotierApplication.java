package com.github.geekarist.henripotier;

import android.app.Application;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Path;
import timber.log.Timber;

public class PotierApplication extends Application {

    public static final String EXTRA_PURCHASED = "purchasedBook";

    private static PotierApplication instance;

    private HenriPotier mHenriPotier;
    private CartDatabaseHelper mDbHelper;

    public PotierApplication() {
        super();
        instance = this;
    }

    public static PotierApplication instance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        this.mHenriPotier = createRestAdapter(BuildConfig.HENRI_POTIER_URL);
        this.mDbHelper = new CartDatabaseHelper(this, null, null, 1);
    }

    private HenriPotier createRestAdapter(String url) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(url)
                .setClient(new OkClient(new OkHttpClient()))
                .build();

        return restAdapter.create(HenriPotier.class);
    }

    public HenriPotier getHenriPotier() {
        return mHenriPotier;
    }

    public CartDatabaseHelper getDbHelper() {
        return mDbHelper;
    }

    public void changeHenriPotierUrl(HttpUrl url) {
        mHenriPotier = createRestAdapter(url.toString());
    }

    public interface HenriPotier {

        @GET("/books")
        void books(Callback<List<Book>> doOnBooks);

        @GET("/books/{isbnValues}/commercialOffers")
        void commercialOffers(@Path("isbnValues") String isbnValues, Callback<CommercialOffers> callback);
    }

}
