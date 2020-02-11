package com.example.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class ImageLoader extends AsyncTaskLoader<Bitmap> {
    final String LOG_TAG = ImageLoader.class.getName();

    private String url;
    /**
     * @param context
     * @param url
     */
    public ImageLoader(Context context ,String url) {
        super(context);
        this.url = url;
    }

    @Override
    public Bitmap loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (url == null || url.length() <1) {
            return null;
        }
        return new Query("").bitmapImageMaker(url);
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i(LOG_TAG,"onStartLoading");
    }
}
