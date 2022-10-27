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

public class CustomList extends ArrayAdapter<Ingredient> {
    // custom array list containing Ingredient
    private ArrayList<Ingredient> ingredients;
    private Context context;

    public CustomList(Context context, ArrayList<Ingredient> ingredients) {
        // constructor
        super(context,0,ingredients);
        this.ingredients = ingredients;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.ingredient_content, parent,false);
        }
        // list view to display 3 attributes of each food object
        Ingredient ingredient = ingredients.get(position);
        TextView ingredientName = view.findViewById(R.id.ingredient_text);
        TextView locationName = view.findViewById(R.id.location_text);
        TextView bbdName = view.findViewById(R.id.bbd_text);
        TextView amountName = view.findViewById(R.id.amount_text);
        TextView unitName = view.findViewById(R.id.unit_text);
        TextView categoryName = view.findViewById(R.id.category_text);

        ingredientName.setText(ingredient.getName());
        locationName.setText(ingredient.getLocation());
        bbdName.setText(ingredient.getbbd());
        amountName.setText(ingredient.getAmount().toString());
        unitName.setText(ingredient.getUnit());
        categoryName.setText(ingredient.getCategory());
        return view;
    }
}