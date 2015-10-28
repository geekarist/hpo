package com.github.geekarist.henripotier;

import android.content.Context;
import android.support.test.espresso.FailureHandler;
import android.support.test.espresso.base.DefaultFailureHandler;
import android.view.View;
import android.view.ViewDebug;

import org.hamcrest.Matcher;

public class ViewDebugFailureHandler implements FailureHandler {
    private final DefaultFailureHandler delegate;
    private final CatalogActivity activity;

    public ViewDebugFailureHandler(Context targetContext, CatalogActivity activity) {
        this.activity = activity;
        delegate = new DefaultFailureHandler(targetContext);
    }

    @Override
    public void handle(Throwable error, Matcher<View> viewMatcher) {
        ViewDebug.dumpCapturedView("POTIERTEST", activity.findViewById(android.R.id.content).getRootView());
        delegate.handle(error, viewMatcher);
    }
}
