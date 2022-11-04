package com.example.a301project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Class for a RecipeList adapter that handles displaying Recipe objects to
 * recipe activity
 */
public class RecipeListAdapter extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> recipes;
    private Context context;

    /**
     * Constructor for RecipeListAdapter using its attributes
     * @param context {@link Context} the context for this adapter
     * @param recipes {@link ArrayList} array list containing recipe objects
     */
    public RecipeListAdapter(Context context, ArrayList<Recipe> recipes) {
        super(context, 0, recipes);

        this.recipes = recipes;
        this.context = context;
    }

    /**
     * Method for creating a view that will appear in the recipe adapter
     * @param position {@link Integer} the position of the current view
     * @param convertView {@link View} the reused view to be retrieved
     * @param parent {@link ViewGroup} the collection of views that contains current view
     * @return a view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Recipe r = recipes.get(position);

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.recipe_row_layout, parent, false);
        }

        // Find the views by searching textbox ID
        TextView titleTV = view.findViewById(R.id.r_titleTextView);
        TextView prepTimeTV = view.findViewById(R.id.r_prepTimeTextView);
        TextView servingsTV = view.findViewById(R.id.r_servingsTextView);
        TextView categoryTV = view.findViewById(R.id.r_categoryTextView);
        ImageView imageView = view.findViewById(R.id.r_imageview);

        // Set the text to each textbox
        titleTV.setText(r.getTitle());
        prepTimeTV.setText(r.getPrepTime() + " min");
        servingsTV.setText(r.getServings().toString() + " Servings");
        categoryTV.setText(r.getCategory());
        if (r.getPhoto() != null && !r.getPhoto().isEmpty()) {
            Picasso.get().load(r.getPhoto()).into(imageView);
            imageView.setClipToOutline(true);
        }

        return view;
    }
}

