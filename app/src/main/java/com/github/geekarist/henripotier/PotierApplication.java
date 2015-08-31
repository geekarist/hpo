package com.github.geekarist.henripotier;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import timber.log.Timber;

public class PotierApplication extends Application {

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

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BuildConfig.HENRI_POTIER_URL)
                .setClient(new OkClient(new OkHttpClient()))
                .build();

        this.mHenriPotier = restAdapter.create(HenriPotier.class);
        this.mDbHelper = new CartDatabaseHelper(this, null, null, 1);
    }

    public HenriPotier getHenriPotier() {
        return mHenriPotier;
    }

    public CartDatabaseHelper getDbHelper() {
        return mDbHelper;
    }

    public interface HenriPotier {
        @GET("/books")
        void books(Callback<List<Book>> doOnBooks);
    }

}
