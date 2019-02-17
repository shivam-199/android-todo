package com.example.finaltodo;

public class ToDo {

    private String name;
    private String dateItemAdded;
    private String id;

    public ToDo() {

    }

    public ToDo(String name, String dateItemAdded, String id) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
