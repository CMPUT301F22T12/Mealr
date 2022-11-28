package com.example.a301project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IngredientExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Ingredient> ingredientExpandable;
    private HashMap<String, ArrayList<String>> expandableListDetail;

    public IngredientExpandableListAdapter(Context context, ArrayList<Ingredient> ingredientExpandable,HashMap<String, ArrayList<String>> expandableListDetail ) {
        this.context = context;
        this.ingredientExpandable = ingredientExpandable;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return ingredientExpandable.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return expandableListDetail.get(ingredientExpandable.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return ingredientExpandable.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.expandableListDetail.get(this.ingredientExpandable.get(i))
                .get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return Long.parseLong(this.ingredientExpandable.get(i).getId());
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.expandable_group, parent, false);
        }

        Ingredient ingredient = ingredientExpandable.get(i);
        TextView ingredientName = view.findViewById(R.id.mealplan_expandablegroup_title);
        TextView amount = view.findViewById(R.id.mealplan_expandablegroup_number);

        ingredientName.setText(ingredient.getName());
        amount.setText(ingredient.getAmount().toString());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.ingredient_mp_child, parent, false);
        }
        Ingredient ingredient = ingredientExpandable.get(i);
        TextView location = view.findViewById(R.id.i_locationText);
        TextView category = view.findViewById(R.id.i_categoryText);
        TextView expiry = view.findViewById(R.id.i_bbdText);

        location.setText(ingredient.getLocation());
        category.setText(ingredient.getCategory());
        expiry.setText(ingredient.getbbd());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
