import services.DataRetriever;
import models.Category;
import models.Product;

import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        DataRetriever dr = new DataRetriever();

        System.out.println("=========================================");
        System.out.println("         TEST DATARETRIEVER");
        System.out.println("=========================================");

        try {

            // QUESTION 1 : Récupérer toutes les catégories
            System.out.println("\n--- QUESTION 1 : getAllCategories() ---");
            List<Category> categories = dr.getAllCategories();
            categories.forEach(System.out::println);


            // QUESTION 2 : Pagination des produits
            System.out.println("\n--- QUESTION 2 : getProductList(page=1, size=5) ---");
            List<Product> productsPage1 = dr.getProductList(1, 5);
            productsPage1.forEach(System.out::println);


            // QUESTION 3 : Filtre sans pagination
            System.out.println("\n--- QUESTION 3 : getProductsByCriteria(name=\"a\", category=null, min=null, max=null) ---");
            List<Product> filtered = dr.getProductsByCriteria(
                    "a",            // product name search
                    null,           // category
                    null,           // creationMin
                    null            // creationMax
            );
            filtered.forEach(System.out::println);


            // QUESTION 4 : Filtre + pagination
            System.out.println("\n--- QUESTION 4 : getProductsByCriteria(...) avec pagination ---");
            List<Product> filteredPaged = dr.getProductsByCriteria(
                    null,                 // name
                    null,                 // category
                    Instant.parse("2020-01-01T00:00:00Z"), // creationMin
                    Instant.now(),        // creationMax
                    1,                    // page
                    3                     // size
            );
            filteredPaged.forEach(System.out::println);


        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n=== FIN DU TEST ===");
    }
}

