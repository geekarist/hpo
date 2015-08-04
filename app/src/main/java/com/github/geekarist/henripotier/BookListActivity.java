package com.github.geekarist.henripotier;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookListActivity extends ListActivity {

    private static List<Map<String, Object>> BOOKS = new ArrayList<>();

    static {
        addBook("Henri Potier 1", "35 EUR", R.drawable.hp1);
        addBook("Henri Potier 2", "32 EUR", R.drawable.hp2);
    }

    private static void addBook(String value, String value2, int value3) {
        HashMap<String, Object> book2 = new HashMap<>();
        book2.put("title", value);
        book2.put("price", value2);
        book2.put("cover", value3);
        BOOKS.add(book2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        // TODO load bitmaps efficiently: http://goo.gl/UNDKd
        ListAdapter adapter = new SimpleAdapter(
                this,
                BOOKS,
                R.layout.activity_book_item,
                new String[]{"title", "price", "cover"},
                new int[]{R.id.textView, R.id.textView2, R.id.imageView});
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
