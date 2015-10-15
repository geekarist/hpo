package com.github.geekarist.henripotier;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

// TODO disable analytics in test runner: http://goo.gl/LVedbO
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
        Espresso.onView(ViewMatchers.isRoot()).perform(new WaitFor(3000));

        // TODO check catalog list size: http://stackoverflow.com/a/30361345/1665730

        // Choose first book
        Espresso.onData(Matchers.instanceOf(Book.class))
                .inAdapterView(Matchers.allOf(ViewMatchers.withId(R.id.list), ViewMatchers.isDisplayed()))
                .atPosition(0)
                .onChildView(ViewMatchers.withId(R.id.book_selection_button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click());

        // Cart should be displayed
        Espresso.onView(ViewMatchers.withId(R.id.cart_list))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check total
        Espresso.onView(ViewMatchers.withId(R.id.total))
                .check(ViewAssertions.matches(ViewMatchers.withText("Total: 33.60 EUR")));

        Espresso.pressBack();

        // Choose second book
        Espresso.onData(Matchers.instanceOf(Book.class))
                .inAdapterView(Matchers.allOf(ViewMatchers.withId(R.id.list), ViewMatchers.isDisplayed()))
                .atPosition(1)
                .onChildView(ViewMatchers.withId(R.id.book_selection_button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click());

        // Cart should be displayed
        Espresso.onView(ViewMatchers.withId(R.id.cart_list))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // First book should be in Cart
        Espresso.onData(Matchers.instanceOf(Cursor.class))
                .inAdapterView(Matchers.allOf(ViewMatchers.withId(R.id.cart_list), ViewMatchers.isDisplayed()))
                .atPosition(0)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Second book should be in Cart
        Espresso.onData(Matchers.instanceOf(Cursor.class))
                .inAdapterView(Matchers.allOf(ViewMatchers.withId(R.id.cart_list), ViewMatchers.isDisplayed()))
                .atPosition(1)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check total amount
        Espresso.onView(ViewMatchers.withId(R.id.total))
                .check(ViewAssertions.matches(ViewMatchers.withText("Total: 50.00 EUR")));

        // TODO check name of books
        // TODO check discount

        // Remove first book from cart
        Espresso.onData(Matchers.instanceOf(Cursor.class))
                .inAdapterView(Matchers.allOf(ViewMatchers.withId(R.id.cart_list), ViewMatchers.isDisplayed()))
                .atPosition(0)
                .onChildView(ViewMatchers.withId(R.id.book_selection_button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click());

        // Remove second book from cart
        Espresso.onData(Matchers.instanceOf(Cursor.class))
                .inAdapterView(Matchers.allOf(ViewMatchers.withId(R.id.cart_list), ViewMatchers.isDisplayed()))
                .atPosition(0)
                .onChildView(ViewMatchers.withId(R.id.book_selection_button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click());

        // Amount should be 0
        Espresso.onView(ViewMatchers.withId(R.id.total))
                .check(ViewAssertions.matches(ViewMatchers.withText("Total: 0.00 EUR")));

        // TODO Cart should be empty
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