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

public class ShoppingListAdapter extends ArrayAdapter<ShoppingItem> {
    private ArrayList<ShoppingItem> shoppingItems;
    private Context context;


    public ShoppingListAdapter(Context context, ArrayList<ShoppingItem> shoppingItems) {
        super(context, 0, shoppingItems);

        this.shoppingItems = shoppingItems;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ShoppingItem s = shoppingItems.get(position);

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.shopping_row_layout, parent, false);
        }

        TextView shoppingItemName = view.findViewById(R.id.s_nameText);
        TextView amountName = view.findViewById(R.id.s_amountText);
        TextView unitName = view.findViewById(R.id.s_unitText);
        TextView categoryName = view.findViewById(R.id.s_categoryText);

        shoppingItemName.setText(s.getName());
        amountName.setText(s.getAmount().toString());
        unitName.setText(s.getUnit());
        categoryName.setText(s.getCategory());

        return view;
    }
}
