/*
 *  Copyright 2020 Mohamed Amgd
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
