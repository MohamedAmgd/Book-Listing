package com.example.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class BookLoader extends AsyncTaskLoader<ArrayList<Book>>{
    private String url;

    final String LOG_TAG = BookLoader.class.getName();

    public BookLoader(Context context , String url) {
        super(context);
        this.url = url;
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        Log.i(LOG_TAG , "loadInBackground");
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (url == null || url.isEmpty()) {
            return null;
        }
        return new Query(url).extractBooks();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i(LOG_TAG,"onStartLoading");
    }
}
