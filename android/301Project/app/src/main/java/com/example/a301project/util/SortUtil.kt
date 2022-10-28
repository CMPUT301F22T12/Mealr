package com.example.a301project.util

import com.example.a301project.shopping.entity.ShoppingItem


object SortUtil {
    fun sortShoppingItemByIngredient(dataList:MutableList<ShoppingItem>){
        dataList.sortBy { it.ingredient }
    }

    fun sortShoppingItemByCategory(dataList:MutableList<ShoppingItem>){
       dataList.sortBy { it.category }
    }

}