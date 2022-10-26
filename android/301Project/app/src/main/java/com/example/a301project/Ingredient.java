package com.example.a301project;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private String id = null;
    private String name;
    private String location;
    private String bbd;
    private String category;
    private String unit;
    private int amount;

    public Ingredient(String name, int amount, String bbd, String location, String unit, String category) {
        // constructor
        this.name = name;
        this.location = location;
        this.bbd = bbd;
        this.category = category;
        this.unit = unit;
        this.amount = amount;
    }

    String getId() {return this.id;}
    String getName() {return this.name;}
    String getbbd() {return this.bbd;}
    String getLocation() {return this.location;}
    Integer getAmount() {return this.amount;}
    String getUnit() {return this.unit;}
    String getCategory() {return this.category;}

    // setters used to edit attributes for existing food object
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBbd(String bbd) {
        this.bbd = bbd;
    }

    public void setLocation(String location) {this.location = location;}

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setAmount(Integer Amount) {
        this.amount = amount;
    }

    public void setCategory(String Category) {
        this.category = category;
    }
}

