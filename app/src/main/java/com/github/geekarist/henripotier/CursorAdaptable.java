package com.github.geekarist.henripotier;

import android.database.Cursor;

public interface CursorAdaptable {
    void onCursorLoaded(Cursor data);
}
