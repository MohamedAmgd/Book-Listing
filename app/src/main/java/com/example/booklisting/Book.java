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

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
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
