package services;

import database.DBConnection;
import models.Category;
import models.Product;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    DBConnection db = new DBConnection();

    public List<Category> getAllCategories() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT id, name, product_id FROM product_category";

        try (Connection conn = db.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("product_id")
                ));
            }
        }
        return list;
    }

    public List<Product> getProductList(int page, int size) throws SQLException {
        List<Product> list = new ArrayList<>();

        int offset = (page - 1) * size;
        String sql = "SELECT id, name, price, creation_datetime FROM product " +
                "ORDER BY id LIMIT ? OFFSET ?";

        try (Connection conn = db.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getTimestamp("creation_datetime").toInstant()
                    ));
                }
            }
        }
        return list;
    }

    public List<Product> getProductsByCriteria(
            String productName,
            String categoryName,
            Instant creationMin,
            Instant creationMax,
            int i, int i1) throws SQLException {

        List<Product> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT p.id, p.name, p.price, p.creation_datetime " +
                        "FROM product p " +
                        "LEFT JOIN product_category pc ON pc.product_id = p.id "
        );

        List<Object> params = new ArrayList<>();
        List<String> conditions = new ArrayList<>();

        if (productName != null) {
            conditions.add("p.name ILIKE ?");
            params.add("%" + productName + "%");
        }
        if (categoryName != null) {
            conditions.add("pc.name ILIKE ?");
            params.add("%" + categoryName + "%");
        }
        if (creationMin != null) {
            conditions.add("p.creation_datetime >= ?");
            params.add(Timestamp.from(creationMin));
        }
        if (creationMax != null) {
            conditions.add("p.creation_datetime <= ?");
            params.add(Timestamp.from(creationMax));
        }

        if (!conditions.isEmpty()) {
            sql.append(" WHERE ");
            sql.append(String.join(" AND ", conditions));
        }

        sql.append(" ORDER BY p.id");

        try (Connection conn = db.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getTimestamp("creation_datetime").toInstant()
                    ));
                }
            }
        }

        return list;
    }
}




