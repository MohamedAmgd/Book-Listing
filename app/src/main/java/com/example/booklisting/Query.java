/*
 *  Copyright (C) 2020 Mohamed Amgd
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
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Query {
    String url;
    public static final String LOG_TAG = "Query";
    public Query(String url) {
        this.url = url;
    }
    public URL URLMaker(String input){
        if (input == null || input.isEmpty()){
            return null;
        }

        URL url = null;
        try {
            url = new URL(input);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    public String makeHttpRequest(URL url){
        if(url == null){
            return null;
        }
        String JSONResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(35000);
            urlConnection.connect();
            int code = urlConnection.getResponseCode();
            if(code == 200) {
                inputStream = urlConnection.getInputStream();
                JSONResponse = JSONResponseMaker(inputStream);
            }else {
                Log.e(LOG_TAG, "the response code is " + urlConnection.getResponseCode());
            }

        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                //function must handle java.io.IOException here
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSONResponse;
    }
    public String JSONResponseMaker(InputStream inputStream){
        if(inputStream == null){
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        try {
            line = bufferedReader.readLine();
            while (line != null && !line.isEmpty() ) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
    public Bitmap bitmapImageMaker(String input){
        URL url = URLMaker(input);
        if(url == null){
            return null;
        }
        Bitmap image = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                image = BitmapFactory.decodeStream(inputStream);
            }else {
                Log.e(LOG_TAG, "the response code is " + urlConnection.getResponseCode());
            }

        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return image;
    }
    public ArrayList<Book> extractBooks() {
        String JSONInput = makeHttpRequest(URLMaker(url));
        if (JSONInput == null) return null;
        // Create an empty ArrayList that we can start adding books to
        ArrayList<Book> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // build up a list of book objects with the corresponding data.
            JSONObject root =  new JSONObject(JSONInput);

            JSONArray items = root.optJSONArray("items");
            if (items == null) return null;
            for (int i = 0 ; i < items.length() ; i++){
                JSONObject currentItems = items.optJSONObject(i);
                if(currentItems != null){

                    JSONObject volumeInfo = currentItems.optJSONObject("volumeInfo");

                    String title = volumeInfo.getString("title");
                    JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                    String authors = null;
                    if(authorsArray != null){
                        authors = authorsArray.optString(0);
                        for (int j = 1; j < authorsArray.length() ; j++) {
                            authors += " , " + authorsArray.optString(j);
                        }
                    }

                    String url = volumeInfo.optString("infoLink");
                    JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
                    String imageURL = null;
                    if(imageLinks != null){
                        imageURL = imageLinks.optString("smallThumbnail");
                        if(imageURL==null) imageURL = imageLinks.optString("thumbnail");
                    }

                    if(i < 10){
                        books.add(new Book(title,authors,imageURL,url,bitmapImageMaker(imageURL)));
                    }
                    else{
                        books.add(new Book(title,authors,imageURL,url));
                    }
                }
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }

        Log.i(LOG_TAG,"extractBooks");
        // Return the list of books
        return books;
    }
}
