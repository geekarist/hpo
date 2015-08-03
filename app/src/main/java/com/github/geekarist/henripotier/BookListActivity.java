package com.github.geekarist.henripotier;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookListActivity extends ListActivity {

    private static List<Map<String, String>> BOOKS = new ArrayList<>();

    static {
        addBook("Henri Potier 1", "35 EUR");
        addBook("Henri Potier 2", "32 EUR");
    }

    private static void addBook(String value, String value2) {
        HashMap<String, String> book2 = new HashMap<>();
        book2.put("title", value);
        book2.put("price", value2);
        BOOKS.add(book2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        ListAdapter adapter = new SimpleAdapter(
                this,
                BOOKS,
                R.layout.activity_book_item,
                new String[]{"title", "price"},
                new int[]{R.id.textView, R.id.textView2});
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
