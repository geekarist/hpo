package com.github.geekarist.henripotier;

import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class CatalogActivityTest extends ActivityInstrumentationTestCase2<CatalogActivity> {
    private CatalogActivity mActivity;

    public CatalogActivityTest() {
        super(CatalogActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    public void testShouldDisplayListOfBooks() throws InterruptedException {
        Espresso.onView(ViewMatchers.isRoot()).perform(new WaitFor(3000));

        Espresso.onData(Matchers.instanceOf(Book.class))
                .inAdapterView(Matchers.allOf(ViewMatchers.withId(R.id.list), ViewMatchers.isDisplayed()))
                .atPosition(0)
                .onChildView(ViewMatchers.withId(R.id.book_selection_button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click());


        Espresso.onView(ViewMatchers.withId(R.id.cart_list))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onData(Matchers.instanceOf(Cursor.class))
                .inAdapterView(Matchers.allOf(ViewMatchers.withId(R.id.cart_list), ViewMatchers.isDisplayed()))
                .atPosition(0)
                .onChildView(ViewMatchers.withId(R.id.book_selection_button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click());
    }

    private static class WaitFor implements ViewAction {

        private int time;

        public WaitFor(int time) {
            this.time = time;
        }

        @Override
        public Matcher<View> getConstraints() {
            return ViewMatchers.isRoot();
        }

        @Override
        public String getDescription() {
            return "wait for " + time + " seconds";
        }

        @Override
        public void perform(UiController uiController, View view) {
            uiController.loopMainThreadForAtLeast(time);
        }
    }
}