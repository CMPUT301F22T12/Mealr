package com.example.a301project.util;

import com.example.a301project.shopping.entity.ShoppingItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SortUtil {


    public static  void sortShoppingItemByIngredient(List<ShoppingItem> dataList){
        Collections.sort(dataList, new Comparator<ShoppingItem>() {
            @Override
            public int compare(ShoppingItem o1, ShoppingItem o2) {
                return o1.getIngredient().compareTo(o2.getIngredient());
            }
        });
    }

    public static  void sortShoppingItemByCategory(List<ShoppingItem> dataList){
        Collections.sort(dataList, new Comparator<ShoppingItem>() {
            @Override
            public int compare(ShoppingItem o1, ShoppingItem o2) {
                return o1.getCategory().compareTo(o2.getCategory());
            }
        });
    }
}
