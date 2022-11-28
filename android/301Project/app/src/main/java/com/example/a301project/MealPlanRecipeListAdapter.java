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

/***
 * A class for rendering custom Array Adapters to display when a recipe is selected in meal plan's add fragment
 */
public class MealPlanRecipeListAdapter extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> recipes;
    private Context context;

    public MealPlanRecipeListAdapter(Context context, ArrayList<Recipe> recipes) {
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
        Recipe i = recipes.get(position);

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.add_ingredient_row_layout, parent, false);
        }

        // Find the views by searching ID
        TextView titleTV = view.findViewById(R.id.i_nameText);
        EditText editServings = view.findViewById(R.id.edit_amount);
        ImageButton deleteButton = view.findViewById(R.id.delete_ingredient_button);

        // Set the text to each
        titleTV.setText(i.getTitle());
        editServings.setText(i.getServings().toString());

        deleteButton.setOnClickListener(view_ -> {
            recipes.remove(position);
            notifyDataSetChanged();
        });

        editServings.addTextChangedListener(new TextWatcher() {
            /**
             * method for when text changes before, on, and after in editServings text box
             * nothing happens in this method because nothing needs to happen before text changes
             * @param charSequence {@link CharSequence} the text charSequence
             * @param i
             * @param i1
             * @param i2
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // nothing happens
            }

            /**
             * Method for when the text of editServings is changed, change the text to reference the new one
             * @param charSequence {@link CharSequence} the text charSequence
             * @param i_ {@link int} position index
             * @param i1
             * @param i2
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i_, int i1, int i2) {
                try {
                    Long d = Long.parseLong(editServings.getText().toString());
                    i.setServings(d);
                } catch (NumberFormatException e) {
                    editServings.setText(i.getServings().toString());
                }
            }

            /**
             * method for after the text has been changed
             * @param editable {@link Editable} the Edit text box containing new text
             */
            @Override
            public void afterTextChanged(Editable editable) {
                // nothing happens
            }
        });
        return view;
    }
}
