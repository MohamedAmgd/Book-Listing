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

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class Book {
    private String name , author , imageUrl , url;
    private Bitmap image;

    public Book(String name, String author, String imageUrl , String url) {
        this.name = name;
        this.author = author;
        this.imageUrl = imageUrl;
        this.url =url;
        new ImageThread().execute(imageUrl);
    }
    public Book(String name, String author, String imageUrl , String url , Bitmap image) {
        this.name = name;
        this.author = author;
        this.imageUrl = imageUrl;
        this.url =url;
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Bitmap getImage() {
        return image;
    }

    private class ImageThread extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls[0] == null || urls[0].length() <1) {
                return null;
            }
            return new Query("").bitmapImageMaker(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            image = bitmap;
        }


    }
}
