package com.example.shopapp.Model;

public class BookCategory {
    Integer Id;
    String Name;

    public BookCategory() {
    }

    public BookCategory(Integer id, String name) {
        Id = id;
        Name = name;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
