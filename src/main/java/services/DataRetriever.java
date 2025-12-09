package services;

import database.DBConnection;
import models.Category;
import models.Product;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    DBConnection db = new DBConnection();

    // Petit utilitaire
    public static Instant createInstant(int year, int month, int day) {
        return LocalDateTime.of(year, month, day, 0, 0).toInstant(ZoneOffset.UTC);
    }

    public static Instant createInstant(int year, int month, int day, int hour, int min) {
        return LocalDateTime.of(year, month, day, hour, min).toInstant(ZoneOffset.UTC);
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT id, name FROM product_category";

        try (Connection conn = db.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        }
        return list;
    }

    public List<Product> getProductList(int page, int size) throws SQLException {
        List<Product> list = new ArrayList<>();

        int offset = (page - 1) * size;
        String sql = """
                SELECT id, name, price, creation_datetime
                FROM product
                ORDER BY id
                LIMIT ? OFFSET ?
                """;

        try (Connection conn = db.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getTimestamp("creation_datetime").toInstant()
                    );
                    list.add(p);
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
            int page,
            int size) throws SQLException {

        List<Product> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT DISTINCT p.id, p.name, p.price, p.creation_datetime
            FROM product p
            LEFT JOIN product_category pc ON pc.product_id = p.id
        """);

        List<Object> values = new ArrayList<>();
        List<String> conditions = new ArrayList<>();

        if (productName != null) {
            conditions.add("p.name ILIKE ?");
            values.add("%" + productName + "%");
        }
        if (categoryName != null) {
            conditions.add("pc.name ILIKE ?");
            values.add("%" + categoryName + "%");
        }
        if (creationMin != null) {
            conditions.add("p.creation_datetime >= ?");
            values.add(Timestamp.from(creationMin));
        }
        if (creationMax != null) {
            conditions.add("p.creation_datetime <= ?");
            values.add(Timestamp.from(creationMax));
        }

        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        sql.append(" ORDER BY p.id LIMIT ? OFFSET ?");

        try (Connection conn = db.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            for (Object v : values) {
                ps.setObject(index++, v);
            }

            ps.setInt(index++, size);
            ps.setInt(index, (page - 1) * size);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getTimestamp("creation_datetime").toInstant()
                    );
                    list.add(p);
                }
            }
        }
        return list;
    }
}
