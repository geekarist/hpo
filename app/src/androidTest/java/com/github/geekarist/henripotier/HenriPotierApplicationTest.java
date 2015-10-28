package com.github.geekarist.henripotier;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import com.squareup.spoon.Spoon;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import timber.log.Timber;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

// TODO disable analytics in test runner: http://goo.gl/LVedbO
@SuppressWarnings("unchecked")
public class HenriPotierApplicationTest extends ActivityInstrumentationTestCase2<CatalogActivity> {
    private final MockWebServer fakeWebServer;
    private CatalogActivity mActivity;
    private HttpUrl fakeHenriPotierUrl;

    public HenriPotierApplicationTest() throws IOException {
        super(CatalogActivity.class);

        fakeWebServer = new MockWebServer();
        fakeWebServer.start();
        fakeHenriPotierUrl = fakeWebServer.url("/");
        PotierApplication.instance().changeHenriPotierUrl(fakeHenriPotierUrl);
        fakeWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                if (request.getPath().equals("/books")) {
                    return new MockResponse().setBody(replaceQuotes("[\n" +
                            "  {\n" +
                            "    'isbn': 'c8fabf68-8374-48fe-a7ea-a00ccd07afff',\n" +
                            "    'title': 'Henri Potier à la truc des sorciers',\n" +
                            "    'price': 35\n" +
                            "  },\n" +
                            "  {\n" +
                            "    'isbn': 'a460afed-e5e7-4e39-a39d-c885c05db861',\n" +
                            "    'title': 'Henri Potier et la Chambre des secrets',\n" +
                            "    'price': 30\n" +
                            "  }" +
                            "]"));
                } else if (request.getPath().equals("/books/c8fabf68-8374-48fe-a7ea-a00ccd07afff/commercialOffers")) {
                    // Offer for first book
                    return (new MockResponse().setBody(replaceQuotes("{\n" +
                            "  'offers': [\n" +
                            "    {\n" +
                            "      'type': 'percentage',\n" +
                            "      'value': 4\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}")));
                } else if (request.getPath().equals("/books/"
                        + urlEncode("c8fabf68-8374-48fe-a7ea-a00ccd07afff,a460afed-e5e7-4e39-a39d-c885c05db861")
                        + "/commercialOffers")) {
                    // Offers for both books
                    return (new MockResponse().setBody(replaceQuotes("{\n" +
                            "  'offers': [\n" +
                            "    {\n" +
                            "      'type': 'percentage',\n" +
                            "      'value': 4\n" +
                            "    },\n" +
                            "    {\n" +
                            "      'type': 'minus',\n" +
                            "      'value': 15\n" +
                            "    },\n" +
                            "    {\n" +
                            "      'type': 'slice',\n" +
                            "      'sliceValue': 100,\n" +
                            "      'value': 12\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}")));
                }
                return new MockResponse().setResponseCode(404);
            }
        });
    }

    private static Activity getActivity(View view) {
        Context context = view.getContext();
        while (!(context instanceof Activity)) {
            if (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            } else {
                throw new IllegalStateException("Got a context of class "
                        + context.getClass()
                        + " and I don't know how to get the Activity from it");
            }
        }
        return (Activity) context;
    }

    @NonNull
    private String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String replaceQuotes(String s) {
        return s.replaceAll("'", "\"");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    public void testShouldAllowBuyingBooks() throws InterruptedException, IOException {
        // Wait for catalog
        Espresso.onView(isRoot()).perform(new WaitFor(3000));

        // TODO check catalog list size: http://stackoverflow.com/a/30361345/1665730

        // Choose first book
        Espresso.onData(instanceOf(Book.class))
                .inAdapterView(allOf(withId(R.id.list), isDisplayed()))
                .atPosition(0)
                .onChildView(withId(R.id.book_selection_button))
                .check(matches(isDisplayed()))
                .perform(click());

        // Cart should be displayed
        Espresso.onView(withId(R.id.cart_list))
                .check(matches(isDisplayed()));

        captureScreen();
        Espresso.onView(withId(R.id.total)).check(matches(withText("33.60 EUR")));

        Espresso.pressBack();

        // Choose second book
        Espresso.onData(instanceOf(Book.class))
                .inAdapterView(allOf(withId(R.id.list), isDisplayed()))
                .atPosition(1)
                .onChildView(withId(R.id.book_selection_button))
                .check(matches(isDisplayed()))
                .perform(click());

        // Cart should be displayed
        Espresso.onView(withId(R.id.cart_list))
                .check(matches(isDisplayed()));

        // First book should be in Cart
        Espresso.onData(instanceOf(Cursor.class))
                .inAdapterView(allOf(withId(R.id.cart_list), isDisplayed()))
                .atPosition(0)
                .check(matches(isDisplayed()));

        // Second book should be in Cart
        Espresso.onData(instanceOf(Cursor.class))
                .inAdapterView(allOf(withId(R.id.cart_list), isDisplayed()))
                .atPosition(1)
                .check(matches(isDisplayed()));

        // Check total amount
        Espresso.onView(withId(R.id.total))
                .check(matches(withText("50.00 EUR")));

        // TODO check name of books
        // TODO check discount

        // Remove first book from cart
        Espresso.onData(instanceOf(Cursor.class))
                .inAdapterView(allOf(withId(R.id.cart_list), isDisplayed()))
                .atPosition(0)
                .onChildView(withId(R.id.book_selection_button))
                .check(matches(isDisplayed()))
                .perform(click());

        // Remove second book from cart
        Espresso.onData(instanceOf(Cursor.class))
                .inAdapterView(allOf(withId(R.id.cart_list), isDisplayed()))
                .atPosition(0)
                .onChildView(withId(R.id.book_selection_button))
                .check(matches(isDisplayed()))
                .perform(click());

        // Amount should be 0
        Espresso.onView(withId(R.id.total))
                .check(matches(withText("0.00 EUR")));

        // TODO Cart should be empty
    }

    private void captureScreen() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        final String testClass = trace[3].getClassName();
        final String testMethod = trace[3].getMethodName();

        // Check total
        Espresso.onView(ViewMatchers.isRoot()).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return Matchers.anything();
            }

            @Override
            public String getDescription() {
                return "Taking a screenshot";
            }

            @Override
            public void perform(UiController uiController, View view) {
                File captureFile = Spoon.screenshot(getActivity(view), "PotierApplicationTest", testClass, testMethod);
                Timber.i("Screen captured to: %s", captureFile.getPath());
            }
        });
    }

    private static class WaitFor implements ViewAction {

        private int time;

        public WaitFor(int time) {
            this.time = time;
        }

        @Override
        public Matcher<View> getConstraints() {
            return isRoot();
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