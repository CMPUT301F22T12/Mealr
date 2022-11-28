package com.example.a301project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Recipe> recipeExpandable;
    private HashMap<String, ArrayList<String>> expandableListDetail;

    public RecipeExpandableListAdapter(Context context, ArrayList<Recipe> recipeExpandable,HashMap<String, ArrayList<String>> expandableListDetail ) {
        this.context = context;
        this.recipeExpandable = recipeExpandable;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return recipeExpandable.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return expandableListDetail.get(recipeExpandable.get(i).getTitle()).size();
    }

    @Override
    public Object getGroup(int i) {
        return recipeExpandable.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.expandableListDetail.get(recipeExpandable.get(i).getTitle())
                .get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
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

        Recipe recipe = recipeExpandable.get(i);
        TextView ingredientName = view.findViewById(R.id.mealplan_expandablegroup_title);
        TextView amount = view.findViewById(R.id.mealplan_expandablegroup_number);

        ingredientName.setText(recipe.getTitle());
        amount.setText(recipe.getServings().toString());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.ingredient_mp_child, parent, false);
        }
        //Ingredient ingredient = ingredientExpandable.get(i);
        String childtext = (String) getChild(i,i1);
        TextView ingredientChild = view.findViewById(R.id.i_childText);
        ingredientChild.setText(childtext);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
