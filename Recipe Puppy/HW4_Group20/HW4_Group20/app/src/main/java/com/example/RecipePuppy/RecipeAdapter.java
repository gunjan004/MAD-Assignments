package com.example.RecipePuppy;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    ArrayList<Recipe> recipesData;

    public RecipeAdapter(ArrayList<Recipe> recipesData) {
        this.recipesData = recipesData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final int index = i;

        final View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_list, viewGroup, false);
        TextView textViewTitle = view.findViewById(R.id.textViewTitleValue);
        TextView textViewIngredients = view.findViewById(R.id.textViewIngredientValue);
        TextView textViewURL = view.findViewById(R.id.textView_URLValue);
        ImageView imageViewIcon = view.findViewById(R.id.imageView_Recipe);


        final Recipe recipe = recipesData.get(index);

        textViewTitle.setText(recipe.title);
        textViewIngredients.setText(recipe.ingredients);
        textViewURL.setTextColor(Color.BLUE);
        textViewURL.setPaintFlags(textViewURL.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
        textViewURL.setClickable(true);
        textViewURL.setText(recipe.url);

        textViewURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo","url in recipe adapter"+recipe.url);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().getApplicationContext().startActivity(intent);
            }
        });


        if(recipe.image.equals("")) {

            imageViewIcon.setImageBitmap(null);
        } else {
            Picasso.get().load(recipe.image).placeholder(R.drawable.loading).into(imageViewIcon);
        }

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Recipe recipe = recipesData.get(i);
        viewHolder.titleTextView.setText(recipe.title);
        viewHolder.ingredientsTextView.setText(recipe.ingredients);
        viewHolder.urlTextView.setText(recipe.url);


        if(recipe.image.equals("")) {

            viewHolder.thumbnailImageView.setImageBitmap(null);
        } else {

            Picasso.get().load(recipe.image).placeholder(R.drawable.loading).into(viewHolder.thumbnailImageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return recipesData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, ingredientsTextView, urlTextView;
        ImageView thumbnailImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.textViewTitleValue);
            ingredientsTextView = itemView.findViewById(R.id.textViewIngredientValue);
            urlTextView = itemView.findViewById(R.id.textView_URLValue);
            thumbnailImageView = itemView.findViewById(R.id.imageView_Recipe);
        }
    }
}
