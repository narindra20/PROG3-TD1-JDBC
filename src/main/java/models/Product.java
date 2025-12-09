package models;

import java.time.Instant;

public class Product{
    public int id;
    public String name;
    public Instant creationDateTime;
    public Category category;

    public Product(int id, String name, double creationDateTime, Instant category){
        this.id = id;
        this.name = name;
        this.creationDateTime = creationDateTime;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getCreationDateTime() {
        return creationDateTime;
    }

    public Category getCategory() {
        return category;
    }

    public String getCategoryName(){
        if (category == null) return null;
        return category.name;
    }

    @Override
    public String toString(){
        return id + '-' + name + '-' + creationDateTime + " Category : " + getCategoryName();
    }

}

