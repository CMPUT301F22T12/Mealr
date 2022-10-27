package com.example.a301project.shopping.entity;

import java.io.Serializable;

/**
 * @ClassName: ShoppingItem
 * @Description: java类作用描述
 * @Author: ping
 * @CreateDate: 2022/10/27 5:33 下午
 */
public class ShoppingItem implements Serializable {
    private String ingredient;

    private String amount;

    private String unit;

    private String category;

    private boolean selected;

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
