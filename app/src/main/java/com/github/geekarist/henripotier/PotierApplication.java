package com.github.geekarist.henripotier;

import android.app.Application;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import timber.log.Timber;

public class PotierApplication extends Application {

    public static final String EXTRA_PURCHASED = "purchasedBook";

    private static PotierApplication instance;

    private BookResource mBookResource;
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

        this.mBookResource = createRestAdapter(BuildConfig.HENRI_POTIER_URL);
        this.mDbHelper = new CartDatabaseHelper(this, null, null, 1);
    }

    private BookResource createRestAdapter(String url) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(url)
                .setClient(new OkClient(new OkHttpClient()))
                .build();

        return restAdapter.create(BookResource.class);
    }

    public BookResource getBookResource() {
        return mBookResource;
    }

    public CartDatabaseHelper getDbHelper() {
        return mDbHelper;
    }

    public void changeHenriPotierUrl(HttpUrl url) {
        mBookResource = createRestAdapter(url.toString());
    }

}
