package com.github.geekarist.henripotier;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

class CartLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
    private final Context mContext;
    private final CursorAdaptable mAdaptable;

    public CartLoaderCallbacks(Context context, CursorAdaptable adaptable) {
        this.mContext = context;
        this.mAdaptable = adaptable;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext) {
            @Override
            public Cursor loadInBackground() {
                return PotierApplication.instance().getDbHelper().createCursor();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdaptable.adaptCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdaptable.releaseCursor();
    }
}
