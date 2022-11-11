package com.example.a301project;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RecipeIngredientListAdapter extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> ingredients;
    private Context context;

    public RecipeIngredientListAdapter(Context context, ArrayList<Ingredient> ingredients) {
        super(context, 0, ingredients);

        this.ingredients = ingredients;
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
        Ingredient i = ingredients.get(position);

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.add_ingredient_row_layout, parent, false);
        }

        // Find the views by searching ID
        TextView titleTV = view.findViewById(R.id.i_nameText);
        EditText editAmount = view.findViewById(R.id.edit_amount);
        ImageButton deleteButton = view.findViewById(R.id.delete_ingredient_button);

        // Set the text to each
        titleTV.setText(i.getName());
        editAmount.setText(i.getAmount().toString());

        deleteButton.setOnClickListener(view_ -> {
            ingredients.remove(position);
            notifyDataSetChanged();
        });

        editAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i_, int i1, int i2) {
                try {
                    Double d = Double.parseDouble(editAmount.getText().toString());
                    i.setAmount(d);
                } catch (NumberFormatException e) {
                    editAmount.setText(i.getAmount().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }
}
