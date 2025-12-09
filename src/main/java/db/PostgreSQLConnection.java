package db;

/**
 * Implementación concreta de DBConnection para PostgreSQL
 * Implementa el patrón Singleton
 */
public class PostgreSQLConnection extends DBConnection {
    
    private static PostgreSQLConnection instancia;
    
    /*
     * Por ser un caso académico se dejan enunciadas en el código,
     * pero deben ser protegidas, puede ser como variables de entorno,
     * secretos, o desde un archivo codificado.
     */
    private static final String HOST = "localhost";
    private static final String PORT = "5432";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Abby.0309";
    private static final String DATABASE = "hotel";
    
    /**
     * Constructor privado para implementar Singleton
     */
    private PostgreSQLConnection() {
        url = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;
        props.setProperty("user", USER);
        props.setProperty("password", PASSWORD);
    }
    
    /**
     * Obtiene la instancia única de la conexión (Singleton)
     * @return instancia única de PostgreSQLConnection
     */
    public static PostgreSQLConnection getConnector() {
        if (instancia == null) {
            instancia = new PostgreSQLConnection();
        }
        return instancia;
    }
}