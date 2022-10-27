package com.example.a301project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a301project.base.BaseAdapter;
import com.example.a301project.databinding.ActivityShoppingListBinding;
import com.example.a301project.shopping.adapter.ShoppingListAdapter;
import com.example.a301project.shopping.entity.ShoppingItem;
import com.example.a301project.util.SortUtil;
import com.example.a301project.util.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShoppingListActivity extends NavActivity  {

    ActivityShoppingListBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityShoppingListBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());


        // Set the correct button to be selected
        bottomNav.getMenu().findItem(R.id.action_shopping_list).setChecked(true);

        initViews();
    }

    List<ShoppingItem> shoppingItemList;
    ShoppingListAdapter shoppingListAdapter;


    private void initViews(){
        shoppingItemList = new ArrayList<>();
        shoppingListAdapter = new ShoppingListAdapter(shoppingItemList,this);
        viewBinding.rvShoppingList.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.rvShoppingList.setAdapter(shoppingListAdapter);
        viewBinding.tvShoppingAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示添加item 弹出
                showAddItemDialog(null,1);
            }
        });
        shoppingListAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClickListener(View view, int position) {
                //修改
                showAddItemDialog(shoppingItemList.get(position),2);
            }
        });
        viewBinding.tvShoppingSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //按照ingredient 排序
                SortUtil.INSTANCE.sortShoppingItemByIngredient(shoppingItemList);
                shoppingListAdapter.notifyDataSetChanged();
            }
        });
        viewBinding.tvShoppingCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortUtil.INSTANCE.sortShoppingItemByCategory(shoppingItemList);
                shoppingListAdapter.notifyDataSetChanged();
            }
        });

        viewBinding.tvShoppingAddStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFoodDialog();
            }
        });
    }

    private void showAddItemDialog(ShoppingItem shoppingItem,int type){
        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.shopping_add_item_dialog,null);
        TextView tv_add_item_title = view.findViewById(R.id.tv_add_item_title);
        EditText et_add_item_ingredient = view.findViewById(R.id.et_add_item_ingredient);
        EditText et_add_item_amount = view.findViewById(R.id.et_add_item_amount);
        EditText et_add_item_unit = view.findViewById(R.id.et_add_item_unit);
        EditText et_add_item_category = view.findViewById(R.id.et_add_item_category);
        if(shoppingItem != null){
            tv_add_item_title.setText("Edit Item");
            et_add_item_ingredient.setText(shoppingItem.getIngredient());
            et_add_item_amount.setText(shoppingItem.getAmount());
            et_add_item_unit.setText(shoppingItem.getUnit());
            et_add_item_category.setText(shoppingItem.getCategory());
        }

        TextView tv_shopping_add_item_dialog_black = view.findViewById(R.id.tv_shopping_add_item_dialog_black);
        TextView tv_shopping_add_item_dialog_confirm = view.findViewById(R.id.tv_shopping_add_item_dialog_confirm);
        tv_shopping_add_item_dialog_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_shopping_add_item_dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShoppingItem shoppingItemD = new ShoppingItem();
                shoppingItemD.setIngredient(et_add_item_ingredient.getText().toString());
                shoppingItemD.setAmount(et_add_item_amount.getText().toString());
                shoppingItemD.setUnit(et_add_item_unit.getText().toString());
                shoppingItemD.setCategory(et_add_item_category.getText().toString());
                if(TextUtils.isEmpty(shoppingItemD.getIngredient())){
                    ToastUtil.show("Ingredient cannot be null");
                    return;
                }
                if(TextUtils.isEmpty(shoppingItemD.getAmount())){
                    ToastUtil.show("Amount cannot be null");
                    return;
                }
                if(TextUtils.isEmpty(shoppingItemD.getUnit())){
                    ToastUtil.show("Unit cannot be null");
                    return;
                }
                if(TextUtils.isEmpty(shoppingItemD.getCategory())){
                    ToastUtil.show("Category cannot be null");
                    return;
                }
                if(type == 1){
                    shoppingItemList.add(shoppingItemD);
                }else{
                    shoppingItem.setIngredient(shoppingItemD.getIngredient());
                    shoppingItem.setAmount(shoppingItemD.getAmount());
                    shoppingItem.setUnit(shoppingItemD.getUnit());
                    shoppingItem.setCategory(shoppingItemD.getCategory());
                }

                shoppingListAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        // 将对话框的大小按屏幕大小的百分比设置
        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth() * 0.8); //设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }


    private void showAddFoodDialog( ){
        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.add_food_dialog,null);
        EditText et_add_food_name = view.findViewById(R.id.et_add_food_name);
        EditText et_add_food_description = view.findViewById(R.id.et_add_food_description);
        TextView tv_add_food_date = view.findViewById(R.id.tv_add_food_date);
        EditText et_add_food_location = view.findViewById(R.id.et_add_food_location);
        EditText et_add_food_amount = view.findViewById(R.id.et_add_food_amount);
        EditText et_add_food_unit = view.findViewById(R.id.et_add_food_unit);
        EditText et_add_food_category = view.findViewById(R.id.et_add_food_category);

        tv_add_food_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcalendar = Calendar.getInstance();     //  获取当前时间    —   年、月、日
                int year = mcalendar.get(Calendar.YEAR);         //  得到当前年
                int month = mcalendar.get(Calendar.MONTH);       //  得到当前月
                final int day = mcalendar.get(Calendar.DAY_OF_MONTH);  //  得到当前日

                new DatePickerDialog(ShoppingListActivity.this, new DatePickerDialog.OnDateSetListener() {      //  日期选择对话框
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //  这个方法是得到选择后的 年，月，日，分别对应着三个参数 — year、month、dayOfMonth
                        tv_add_food_date.setText(year+"-"+month+"-"+dayOfMonth);
                    }
                },year,month,day).show();
            }
        });

        TextView tv_add_food_dialog_cancel = view.findViewById(R.id.tv_add_food_dialog_cancel);
        TextView tv_add_food_dialog_done = view.findViewById(R.id.tv_add_food_dialog_done);
        tv_add_food_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_add_food_dialog_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showSuccessDialog();
            }
        });

        dialog.setContentView(view);
        // 将对话框的大小按屏幕大小的百分比设置
        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth() * 0.8); //设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }


    private void showSuccessDialog(){
        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.success_dialog,null);
        TextView tv_success_dialog_done = view.findViewById(R.id.tv_success_dialog_done);
        tv_success_dialog_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        // 将对话框的大小按屏幕大小的百分比设置
        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth() * 0.8); //设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

}