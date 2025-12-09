package dao;

import modelo.Correo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import db.PostgreSQLConnection;

/**
 * DAO para la tabla Correo
 * Implementa las operaciones CRUD
 */
public class CorreoDAO {
    
    private Connection connection;
    
    public CorreoDAO() throws SQLException {
        this.connection = PostgreSQLConnection.getConnector().getConn();
    }
    
    /**
     * Inserta un nuevo correo para un cliente
     * @param correo objeto Correo a insertar
     * @return true si se insertó correctamente
     */
    public boolean insert(Correo correo) {
        String sql = "INSERT INTO Correo (cedulaPer, correo) VALUES (?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, correo.getCedulaPer());
            stmt.setString(2, correo.getCorreo());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar correo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Inserta múltiples correos para un cliente
     * @param cedulaPer cédula del cliente
     * @param correos lista de correos electrónicos
     * @return true si todos se insertaron correctamente
     */
    public boolean insertMultiple(Long cedulaPer, List<String> correos) {
        String sql = "INSERT INTO Correo (cedulaPer, correo) VALUES (?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            
            for (String correo : correos) {
                stmt.setLong(1, cedulaPer);
                stmt.setString(2, correo);
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            connection.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
            System.err.println("Error al insertar múltiples correos: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restaurar autocommit: " + e.getMessage());
            }
        }
    }
    
    /**
     * Elimina un correo específico de un cliente
     * @param cedulaPer cédula del cliente
     * @param correo correo electrónico a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean delete(Long cedulaPer, String correo) {
        String sql = "DELETE FROM Correo WHERE cedulaPer = ? AND correo = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            stmt.setString(2, correo);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar correo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina todos los correos de un cliente
     * @param cedulaPer cédula del cliente
     * @return true si se eliminaron correctamente
     */
    public boolean deleteAllByCliente(Long cedulaPer) {
        String sql = "DELETE FROM Correo WHERE cedulaPer = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar correos: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca todos los correos de un cliente
     * @param cedulaPer cédula del cliente
     * @return lista de correos
     */
    public List<String> findByCliente(Long cedulaPer) {
        List<String> correos = new ArrayList<>();
        String sql = "SELECT correo FROM Correo WHERE cedulaPer = ? ORDER BY correo";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    correos.add(rs.getString("correo"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar correos: " + e.getMessage());
        }
        return correos;
    }
    
    /**
     * Busca todos los registros de correos de un cliente
     * @param cedulaPer cédula del cliente
     * @return lista de objetos Correo
     */
    public List<Correo> findCorreosCompletos(Long cedulaPer) {
        List<Correo> correos = new ArrayList<>();
        String sql = "SELECT * FROM Correo WHERE cedulaPer = ? ORDER BY correo";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    correos.add(mapResultSetToCorreo(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar correos completos: " + e.getMessage());
        }
        return correos;
    }
    
    /**
     * Obtiene todos los registros de correos
     * @return lista de todos los correos
     */
    public List<Correo> findAll() {
        List<Correo> correos = new ArrayList<>();
        String sql = "SELECT * FROM Correo ORDER BY cedulaPer, correo";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                correos.add(mapResultSetToCorreo(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener correos: " + e.getMessage());
        }
        return correos;
    }
    
    /**
     * Verifica si un correo existe para un cliente
     * @param cedulaPer cédula del cliente
     * @param correo correo electrónico
     * @return true si existe
     */
    public boolean exists(Long cedulaPer, String correo) {
        String sql = "SELECT COUNT(*) FROM Correo WHERE cedulaPer = ? AND correo = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            stmt.setString(2, correo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Actualiza los correos de un cliente (elimina los existentes e inserta los nuevos)
     * @param cedulaPer cédula del cliente
     * @param nuevosCorreos lista de nuevos correos
     * @return true si se actualizó correctamente
     */
    public boolean updateCorreos(Long cedulaPer, List<String> nuevosCorreos) {
        try {
            connection.setAutoCommit(false);
            
            // Eliminar correos existentes
            deleteAllByCliente(cedulaPer);
            
            // Insertar nuevos correos
            if (!nuevosCorreos.isEmpty()) {
                insertMultiple(cedulaPer, nuevosCorreos);
            }
            
            connection.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
            System.err.println("Error al actualizar correos: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restaurar autocommit: " + e.getMessage());
            }
        }
    }
    
    /**
     * Busca clientes por dominio de correo
     * @param dominio dominio del correo (ej: "gmail.com")
     * @return lista de cédulas de clientes
     */
    public List<Long> findClientesByDominio(String dominio) {
        List<Long> cedulas = new ArrayList<>();
        String sql = "SELECT DISTINCT cedulaPer FROM Correo WHERE correo LIKE ? ORDER BY cedulaPer";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%@" + dominio);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cedulas.add(rs.getLong("cedulaPer"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por dominio: " + e.getMessage());
        }
        return cedulas;
    }
    
    /**
     * Mapea un ResultSet a un objeto Correo
     * @param rs ResultSet con los datos
     * @return objeto Correo
     */
    private Correo mapResultSetToCorreo(ResultSet rs) throws SQLException {
        Correo correo = new Correo();
        correo.setCedulaPer(rs.getLong("cedulaPer"));
        correo.setCorreo(rs.getString("correo"));
        return correo;
    }
}