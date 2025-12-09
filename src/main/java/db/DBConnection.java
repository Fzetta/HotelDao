package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase abstracta base para gestionar conexiones a bases de datos
 * Implementa el patrón Template Method
 */
public abstract class DBConnection {
    
    protected String url;
    private Connection conn;
    protected final Properties props = new Properties();
    
    // Static block to load the driver once
    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver PostgreSQL cargado exitosamente");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver PostgreSQL no encontrado");
            throw new RuntimeException("Driver PostgreSQL no encontrado", e);
        }
    }
    
    /**
     * Obtiene una conexión a la base de datos
     * @return Connection objeto de conexión
     * @throws SQLException si hay un error en la conexión
     */
    public Connection getConn() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(url, props);
            System.out.println("Conexión establecida exitosamente");
        }
        return conn;
    }
    
    /**
     * Cierra la conexión si está abierta
     */
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
                System.out.println("Conexión cerrada exitosamente");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}