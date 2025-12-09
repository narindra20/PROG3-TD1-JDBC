package models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Product {

    private int id;
    private String name;
    private double price;
    private Instant creationDateTime;
    private List<Category> categories = new ArrayList<>();

    public Product(int id, String name, double price, Instant creationDateTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.creationDateTime = creationDateTime;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public Instant getCreationDateTime() { return creationDateTime; }

    public List<Category> getCategories() {
        return categories;
    }

    public void addCategory(Category c) {
        categories.add(c);
    }

    @Override
    public String toString() {
        return id + "-" + name + "-" + price + "-" + creationDateTime + " Categories: " + categories;
    }
}
