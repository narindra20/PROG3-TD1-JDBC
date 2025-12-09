import models.Category;
import models.Product;
import services.DataRetriever;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        DataRetriever retriever = new DataRetriever();

        System.out.println("=== TEST 1: Toutes les catégories ===");
        testGetAllCategories(retriever);

        System.out.println("\n=== TEST 2: Pagination ===");
        testPagination(retriever);

        System.out.println("\n=== TEST 3: Recherche avec critères ===");
        testCriteriaSearch(retriever);

        System.out.println("\n=== TEST 4: Recherche avec critères ET pagination ===");
        testCriteriaWithPagination(retriever);
    }

    private static void testGetAllCategories(DataRetriever retriever) throws SQLException {
        List<Category> categories = retriever.getAllCategories();
        System.out.println("Nombre de catégories distinctes: " + categories.size());
        for (Category cat : categories) {
            System.out.println("  - ID: " + cat.getId() + ", Nom: " + cat.getName());
        }
    }

    private static void testPagination(DataRetriever retriever) throws SQLException {
        int[][] tests = {{1, 10}, {1, 5}, {1, 3}, {2, 2}};

        for (int[] test : tests) {
            int page = test[0];
            int size = test[1];
            System.out.println("\n--- Page: " + page + ", Size: " + size + " ---");

            List<Product> products = retriever.getProductList(page, size);
            System.out.println("Nombre de produits: " + products.size());

            for (Product p : products) {
                System.out.println("  - " + p.getName() +
                        " | Catégories: " + p.getCategories() +
                        " | Prix: " + p.getPrice());
            }
        }
    }

    private static void testCriteriaSearch(DataRetriever retriever) throws SQLException {
        System.out.println("\n1. Produits contenant 'Dell':");
        List<Product> result1 = retriever.getProductsByCriteria("Dell", null, null, null, 1, 10);
        printProducts(result1);

        System.out.println("\n2. Produits de catégorie contenant 'info':");
        List<Product> result2 = retriever.getProductsByCriteria(null, "info", null, null, 1, 10);
        printProducts(result2);

        System.out.println("\n3. Produits entre février et mars 2024:");
        Instant minDate = DataRetriever.createInstant(2024, 2, 1);
        Instant maxDate = DataRetriever.createInstant(2024, 3, 1, 23, 59);
        List<Product> result3 = retriever.getProductsByCriteria(null, null, minDate, maxDate, 1, 10);
        printProducts(result3);

        System.out.println("\n4. Produits 'Samsung' de catégorie 'bureau':");
        List<Product> result4 = retriever.getProductsByCriteria("Samsung", "bureau", null, null, 1, 10);
        printProducts(result4);

        System.out.println("\n5. Tous les produits:");
        List<Product> result5 = retriever.getProductsByCriteria(null, null, null, null, 1, 10);
        printProducts(result5);
    }

    private static void testCriteriaWithPagination(DataRetriever retriever) {
        System.out.println("\n1. Tous les produits (page 1, taille 10):");
        List<Product> result1 = retriever.getProductsByCriteria(null, null, null, null, 1, 10);
        printProducts(result1);

        System.out.println("\n2. Produits 'Dell' (page 1, taille 5):");
        List<Product> result2 = retriever.getProductsByCriteria("Dell", null, null, null, 1, 5);
        printProducts(result2);

        System.out.println("\n3. Catégorie 'informatique' (page 1, taille 10):");
        List<Product> result3 = retriever.getProductsByCriteria(null, "informatique", null, null, 1, 10);
        printProducts(result3);
    }

    private static void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("  Aucun résultat");
        } else {
            for (Product p : products) {
                System.out.println("  - " + p.getName() +
                        " (ID: " + p.getId() +
                        ", Prix: " + p.getPrice() +
                        ", Catégories: " + p.getCategories() + ")");
            }
        }
    }
}
