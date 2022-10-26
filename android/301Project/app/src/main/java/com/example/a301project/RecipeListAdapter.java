package com.example.a301project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RecipeListAdapter extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> recipes;
    private Context context;


    public RecipeListAdapter(Context context, ArrayList<Recipe> recipes) {
        super(context, 0, recipes);

        this.recipes = recipes;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Recipe r = recipes.get(position);

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.recipe_row_layout, parent, false);
        }

        // Find the views
        TextView titleTV = view.findViewById(R.id.r_titleTextView);
        TextView prepTimeTV = view.findViewById(R.id.r_prepTimeTextView);
        TextView servingsTV = view.findViewById(R.id.r_servingsTextView);
        TextView categoryTV = view.findViewById(R.id.r_categoryTextView);

        // Set the text
        titleTV.setText(r.getTitle());
        prepTimeTV.setText(r.getPrepTime().toString());
        servingsTV.setText(r.getServings().toString());
        categoryTV.setText(r.getCategory());

        return view;
    }
}

