package services;

import database.DBConnection;
import models.Category;
import models.Product;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    private DBConnection db = new DBConnection();

    //QUESTION 1
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM product_category";

        try (Connection conn = db.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                );
                categories.add(category);
            }
        }
        return categories;
    }


    //QUESTION 2
    public List<Product> getProductList(int page, int size) throws SQLException {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * size;

        String sql = "SELECT id, name, price, creation_datetime " +
                "FROM product ORDER BY id LIMIT ? OFFSET ?";

        try (Connection conn = db.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getTimestamp("creation_datetime").toInstant()
                    );
                    products.add(product);
                }
            }
        }
        return products;
    }

    //QUESTION 3
    public List<Product> getProductsByCriteria(
            String productName,
            String categoryName,
            Instant creationMin,
            Instant creationMax
    ) throws SQLException {

        return getProductsByCriteria(productName, categoryName, creationMin, creationMax, 1, Integer.MAX_VALUE);
    }

    //QUESTION 4
    public List<Product> getProductsByCriteria(
            String productName,
            String categoryName,
            Instant creationMin,
            Instant creationMax,
            int page,
            int size
    ) throws SQLException {

        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * size;

        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT p.id, p.name, p.price, p.creation_datetime " +
                        "FROM product p " +
                        "LEFT JOIN product_category pc ON p.id = pc.product_id"
        );

        List<Object> parameters = new ArrayList<>();
        List<String> conditions = new ArrayList<>();

        if (productName != null) {
            conditions.add("p.name ILIKE ?");
            parameters.add("%" + productName + "%");
        }

        if (categoryName != null) {
            conditions.add("pc.name ILIKE ?");
            parameters.add("%" + categoryName + "%");
        }

        if (creationMin != null) {
            conditions.add("p.creation_datetime >= ?");
            parameters.add(Timestamp.from(creationMin));
        }

        if (creationMax != null) {
            conditions.add("p.creation_datetime <= ?");
            parameters.add(Timestamp.from(creationMax));
        }

        if (!conditions.isEmpty()) {
            sqlBuilder.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        sqlBuilder.append(" ORDER BY p.id LIMIT ? OFFSET ?");

        try (Connection conn = db.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {

            int idx = 1;

            for (Object param : parameters) {
                ps.setObject(idx++, param);
            }

            ps.setInt(idx++, size);
            ps.setInt(idx, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getTimestamp("creation_datetime").toInstant()
                    );
                    products.add(product);
                }
            }
        }
        return products;
    }
}


