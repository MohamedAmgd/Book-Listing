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

import android.content.Context;
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

        TextView name = listItemView.findViewById(R.id.name);
        name.setText(currentBook.getName());

        TextView author = listItemView.findViewById(R.id.author);
        author.setText(currentBook.getAuthor());

        image = listItemView.findViewById(R.id.list_item_image);
        if(currentBook.getImageUrl() != null && currentBook.getImage() != null) {
            image.setImageBitmap(currentBook.getImage());
        }
        else {
            image.setImageResource(R.drawable.no_cover);
        }
        return listItemView;
    }


}
