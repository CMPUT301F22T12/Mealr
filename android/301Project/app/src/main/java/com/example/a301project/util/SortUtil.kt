package com.example.a301project.util

import com.example.a301project.shopping.entity.ShoppingItem

/**
 * @ClassName: SortUtil
 * @Description: java类作用描述
 * @Author: ping
 * @CreateDate: 2022/10/27 6:34 下午
 */
object SortUtil {
    fun sortShoppingItemByIngredient(dataList:MutableList<ShoppingItem>){
        dataList.sortBy { it.ingredient }
    }

    fun sortShoppingItemByCategory(dataList:MutableList<ShoppingItem>){
       dataList.sortBy { it.category }
    }

}