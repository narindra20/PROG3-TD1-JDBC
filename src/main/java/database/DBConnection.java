package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String url = "jdbc:postgresql://localhost:5432/product_management_db";
    private static final String user = "product_manager_user";
    private static final String password = "123456";

    public Connection getDBConnection() throws SQLException{
        return DriverManager.getConnection(url, user, password);
    }
}
