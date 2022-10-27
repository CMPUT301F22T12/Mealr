package com.example.a301project.shopping.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.a301project.R
import com.example.a301project.base.BaseAdapter
import com.example.a301project.shopping.entity.ShoppingItem

/**
 * @ClassName: DownloadListAdapter
 * @Description: java类作用描述
 * @Author: ping
 * @CreateDate: 2022/9/5 5:55 下午
 */
class ShoppingListAdapter(dataList: MutableList<ShoppingItem>, context: Context) : BaseAdapter(dataList, context) {



    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val view: View = layoutInflater.inflate(R.layout.shopping_item, viewGroup, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        val shoppingItem = dataList[position] as ShoppingItem

        holder.tv_shopping_item_ingredient.text = shoppingItem.ingredient
        holder.tv_shopping_item_amount.text = shoppingItem.amount
        holder.cb_shopping_item.isChecked = shoppingItem.isSelected
        holder.cb_shopping_item.setOnCheckedChangeListener { compoundButton, b ->
            shoppingItem.isSelected = b
        }
        holder.tv_shopping_item_unit.text = shoppingItem.unit
        holder.tv_shopping_item_category.text = shoppingItem.category

        holder.ll_shopping_item.setOnClickListener {
            onItemClickListener?.onClickListener(it,position)
        }
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_shopping_item_ingredient: TextView
        var tv_shopping_item_amount: TextView
        var cb_shopping_item: CheckBox
        var tv_shopping_item_unit: TextView
        var tv_shopping_item_category: TextView
        var ll_shopping_item:LinearLayout

        init {
            tv_shopping_item_ingredient = itemView.findViewById(R.id.tv_shopping_item_ingredient)
            tv_shopping_item_amount = itemView.findViewById(R.id.tv_shopping_item_amount)
            cb_shopping_item = itemView.findViewById(R.id.cb_shopping_item)
            tv_shopping_item_unit = itemView.findViewById(R.id.tv_shopping_item_unit)
            tv_shopping_item_category = itemView.findViewById(R.id.tv_shopping_item_category)
            ll_shopping_item = itemView.findViewById(R.id.ll_shopping_item)
        }
    }

}
