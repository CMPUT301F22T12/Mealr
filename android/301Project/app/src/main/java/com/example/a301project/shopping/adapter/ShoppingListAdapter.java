package com.example.a301project.shopping.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a301project.R;
import com.example.a301project.base.BaseAdapter;
import com.example.a301project.shopping.entity.ShoppingItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ShoppingListAdapter extends BaseAdapter {
    public ShoppingListAdapter(List<?> dataList, Context context) {
        super(dataList, context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.shopping_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        ShoppingItem shoppingItem = (ShoppingItem) dataList.get(position);
        holder.tv_shopping_item_ingredient.setText(shoppingItem.getIngredient());
        holder.tv_shopping_item_amount.setText(shoppingItem.getAmount());
        holder.cb_shopping_item.setChecked( shoppingItem.isSelected());

        holder.cb_shopping_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                shoppingItem.setSelected(b);
            }
        });
        holder.tv_shopping_item_unit.setText(shoppingItem.getUnit());
        holder.tv_shopping_item_category.setText(shoppingItem.getCategory());
        holder.ll_shopping_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClickListener(view,position);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_shopping_item_ingredient;
        TextView tv_shopping_item_amount;
        CheckBox cb_shopping_item;
        TextView tv_shopping_item_unit;
        TextView tv_shopping_item_category;
        LinearLayout ll_shopping_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_shopping_item_ingredient = itemView.findViewById(R.id.tv_shopping_item_ingredient);
            tv_shopping_item_amount = itemView.findViewById(R.id.tv_shopping_item_amount);
            cb_shopping_item = itemView.findViewById(R.id.cb_shopping_item);
            tv_shopping_item_unit = itemView.findViewById(R.id.tv_shopping_item_unit);
            tv_shopping_item_category = itemView.findViewById(R.id.tv_shopping_item_category);
            ll_shopping_item = itemView.findViewById(R.id.ll_shopping_item);
        }




    }
}
