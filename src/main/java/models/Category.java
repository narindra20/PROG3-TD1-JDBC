package models;

public class Category {
    public int id;
    public String name;


    public Category(int id, String name, int productId){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return id + "-" + name;
    }
}

