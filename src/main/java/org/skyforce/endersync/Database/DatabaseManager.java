package org.skyforce.endersync.Database;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseManager {
    private Connection connection;
    private final String url;
    private final String user;
    private final String password;

    public DatabaseManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void checkAndReconnect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
    }

    public Connection getConnection() throws SQLException {
        checkAndReconnect();
        return connection;
    }

    public void executeUpdate(String query, Object... params) throws SQLException {
        checkAndReconnect();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
        }
    }

    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        checkAndReconnect();
        PreparedStatement ps = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps.executeQuery();
    }

    public static DatabaseManager fromConfig(String configFilePath) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = DatabaseManager.class.getClassLoader().getResourceAsStream(configFilePath)) {
            Map<String, Object> config = yaml.load(inputStream);
            Map<String, String> databaseConfig = (Map<String, String>) config.get("database");
            String url = "jdbc:mysql://" + databaseConfig.get("host") + ":" + databaseConfig.get("port") + "/" + databaseConfig.get("databasename");
            String user = databaseConfig.get("user");
            String password = databaseConfig.get("password");
            return new DatabaseManager(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
