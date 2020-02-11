package com.example.booklisting;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Book> objects) {
        super(context, resource, objects);
    }


    Book currentBook;
    ImageView image;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item , parent , false);
        }

        currentBook = getItem(position);

        TextView name = (TextView) listItemView.findViewById(R.id.name);
        name.setText(currentBook.getName());

        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(currentBook.getAuthor());

        image = (ImageView) listItemView.findViewById(R.id.list_item_image);
        if(currentBook.getImageUrl() != null) {
            if(currentBook.getImage() != null){
                image.setImageBitmap(currentBook.getImage());
            }
            else {

            }
        }
        else {
            image.setImageResource(R.drawable.no_cover);
        }
        return listItemView;
    }


}
