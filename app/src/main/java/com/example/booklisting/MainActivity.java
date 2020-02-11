package com.example.booklisting;

import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    String apiURL = "https://www.googleapis.com/books/v1/volumes";
    BookAdapter adapter;
    ListView bookListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        bookListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of books as input
        adapter = new BookAdapter(this,0, new ArrayList<Book>());

        Log.i("bookName :",bookName);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(adapter);

        bookListView.setEmptyView(findViewById(R.id.emptyStateText));


        if(!bookName.equals("")) {
            getLoaderManager().initLoader(x, null, this).forceLoad();
        }

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected book.
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current book that was clicked on
                Book currentBook = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentBook.getUrl());

                // Create a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
        ImageButton searchBtn = (ImageButton) findViewById(R.id.button);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        findViewById(R.id.progressBar).setVisibility(View.GONE);

        Log.i("test","onCreate");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("test" , "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("test" , "onResume");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("test" , "onRestart");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("test" , "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("test" , "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("test" , "onDestroy");


    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG,"onCreateLoader");
        return new BookLoader(this,queryBuilder(apiURL,bookName));
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        // Clear the adapter of previous books data
        adapter.clear();

        // If there is a valid list of {@link book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            adapter.addAll(books);
        }else {
            if (isNetworkAvailable()) {
                ((TextView) findViewById(R.id.emptyStateText)).setText("No books Found");
            } else {
                ((TextView) findViewById(R.id.emptyStateText)).setText("No Internet Connection \n please try again");
            }
            ((TextView) findViewById(R.id.emptyStateText)).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        Log.i(LOG_TAG,"onLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        // Clear the adapter of previous books data
        adapter.clear();
        Log.i(LOG_TAG,"onLoaderReset");
    }


    //global variables for search
    int x = 0;
    static String bookName ="";

    public void search(){
        EditText editText = (EditText) findViewById(R.id.book_name);
        if(!bookName.equals(editText.getText().toString()) && !bookName.equals("")) {
            getLoaderManager().destroyLoader(x);
        }
        bookName = editText.getText().toString();
        if(!bookName.isEmpty()){
            if(isNetworkAvailable()) {
                adapter.clear();
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.emptyStateText)).setVisibility(View.GONE);
                getLoaderManager().initLoader(x, null, this).forceLoad();
                //new SearchThread().execute(URL);

            }
            else {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                Toast.makeText(this,"No Internet Connection \nplease try again", Toast.LENGTH_LONG).show();
                Log.i(LOG_TAG,"No internet");
            }
        }else {
            Toast.makeText(this,"Please Enter a key word first",Toast.LENGTH_LONG).show();
        }

    }
    public String queryBuilder(String url,String bookName){
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("maxResults","40");
        uriBuilder.appendQueryParameter("projection","lite");
        uriBuilder.appendQueryParameter("q",bookName);
        return uriBuilder.toString();
    }

    private class SearchThread extends AsyncTask<String, Void, ArrayList<Book>> {
        @Override
        protected ArrayList<Book> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls[0] == null || urls[0].length() <1) {
                return null;
            }
            return new Query(urls[0]).extractBooks();
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            if(books != null || books.size()<1){

                // Clear the adapter of previous book data
                adapter.clear();

                // If there is a valid list of {@link Book}s, then add them to the adapter's
                // data set. This will trigger the ListView to update.
                if (books != null && !books.isEmpty()) {
                    adapter.addAll(books);
                }
            }
            else {
                if (isNetworkAvailable()) {
                    ((TextView) findViewById(R.id.emptyStateText)).setText("No books Found");
                } else {
                    ((TextView) findViewById(R.id.emptyStateText)).setText("No Internet Connection \nplease try again");
                }
                ((TextView) findViewById(R.id.emptyStateText)).setVisibility(View.VISIBLE);
            }
            findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    }


}
