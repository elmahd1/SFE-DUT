package ccis.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {
    private static final String URL = "jdbc:sqlite:src/main/ressources/db.sqlite";  // Change the path here

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
