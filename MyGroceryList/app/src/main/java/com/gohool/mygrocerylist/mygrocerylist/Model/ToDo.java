package com.gohool.mygrocerylist.mygrocerylist.Model;

public class ToDo {

    private String name;
    private String dateItemAdded;
    private int id;

    public ToDo() {

    }

    public ToDo(String name, String dateItemAdded, int id) {
        this.name = name;
        this.dateItemAdded = dateItemAdded;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
