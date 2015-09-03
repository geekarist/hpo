package com.github.geekarist.henripotier;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

public class BookListActivityTest extends ActivityInstrumentationTestCase2<BookListActivity> {
    private BookListActivity mActivity;

    public BookListActivityTest() {
        super(BookListActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    public void testShouldDisplayListOfBooks() {
        onData(instanceOf(Book.class))
                .inAdapterView(allOf(withId(R.id.list), isDisplayed()))
                .atPosition(0).check(matches(isDisplayed()));
    }
}