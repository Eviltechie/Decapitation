package to.joe.decapitation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {

    private Connection connection;

    public MySQL(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    public PreparedStatement getFreshPreparedStatementColdFromTheRefrigerator(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    public PreparedStatement getFreshPreparedStatementWithGeneratedKeys(String query) throws SQLException {
        return connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    }
}
