package dao;

import modelo.TelefonoPer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import db.PostgreSQLConnection;

/**
 * DAO para la tabla TelefonoPer
 * Implementa las operaciones CRUD
 */
public class TelefonoPerDAO {
    
    private Connection connection;
    
    public TelefonoPerDAO() throws SQLException {
        this.connection = PostgreSQLConnection.getConnector().getConn();
    }
    
    /**
     * Inserta un nuevo teléfono para una persona
     * @param telefono objeto TelefonoPer a insertar
     * @return true si se insertó correctamente
     */
    public boolean insert(TelefonoPer telefono) {
        String sql = "INSERT INTO TelefonoPer (cedulaPer, telefonoPer) VALUES (?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, telefono.getCedulaPer());
            stmt.setLong(2, telefono.getTelefonoPer());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar teléfono: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Inserta múltiples teléfonos para una persona
     * @param cedulaPer cédula de la persona
     * @param telefonos lista de números telefónicos
     * @return true si todos se insertaron correctamente
     */
    public boolean insertMultiple(Long cedulaPer, List<Long> telefonos) {
        String sql = "INSERT INTO TelefonoPer (cedulaPer, telefonoPer) VALUES (?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            
            for (Long telefono : telefonos) {
                stmt.setLong(1, cedulaPer);
                stmt.setLong(2, telefono);
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
            System.err.println("Error al insertar múltiples teléfonos: " + e.getMessage());
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
     * Elimina un teléfono específico de una persona
     * @param cedulaPer cédula de la persona
     * @param telefonoPer número de teléfono a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean delete(Long cedulaPer, Long telefonoPer) {
        String sql = "DELETE FROM TelefonoPer WHERE cedulaPer = ? AND telefonoPer = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            stmt.setLong(2, telefonoPer);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar teléfono: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina todos los teléfonos de una persona
     * @param cedulaPer cédula de la persona
     * @return true si se eliminaron correctamente
     */
    public boolean deleteAllByPersona(Long cedulaPer) {
        String sql = "DELETE FROM TelefonoPer WHERE cedulaPer = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar teléfonos: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca todos los teléfonos de una persona
     * @param cedulaPer cédula de la persona
     * @return lista de teléfonos
     */
    public List<Long> findByPersona(Long cedulaPer) {
        List<Long> telefonos = new ArrayList<>();
        String sql = "SELECT telefonoPer FROM TelefonoPer WHERE cedulaPer = ? ORDER BY telefonoPer";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    telefonos.add(rs.getLong("telefonoPer"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar teléfonos: " + e.getMessage());
        }
        return telefonos;
    }
    
    /**
     * Busca todos los registros de teléfonos de una persona
     * @param cedulaPer cédula de la persona
     * @return lista de objetos TelefonoPer
     */
    public List<TelefonoPer> findTelefonosCompletos(Long cedulaPer) {
        List<TelefonoPer> telefonos = new ArrayList<>();
        String sql = "SELECT * FROM TelefonoPer WHERE cedulaPer = ? ORDER BY telefonoPer";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    telefonos.add(mapResultSetToTelefonoPer(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar teléfonos completos: " + e.getMessage());
        }
        return telefonos;
    }
    
    /**
     * Obtiene todos los registros de teléfonos
     * @return lista de todos los teléfonos
     */
    public List<TelefonoPer> findAll() {
        List<TelefonoPer> telefonos = new ArrayList<>();
        String sql = "SELECT * FROM TelefonoPer ORDER BY cedulaPer, telefonoPer";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                telefonos.add(mapResultSetToTelefonoPer(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener teléfonos: " + e.getMessage());
        }
        return telefonos;
    }
    
    /**
     * Verifica si un teléfono existe para una persona
     * @param cedulaPer cédula de la persona
     * @param telefonoPer número de teléfono
     * @return true si existe
     */
    public boolean exists(Long cedulaPer, Long telefonoPer) {
        String sql = "SELECT COUNT(*) FROM TelefonoPer WHERE cedulaPer = ? AND telefonoPer = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            stmt.setLong(2, telefonoPer);
            
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
     * Actualiza los teléfonos de una persona (elimina los existentes e inserta los nuevos)
     * @param cedulaPer cédula de la persona
     * @param nuevosTelefonos lista de nuevos teléfonos
     * @return true si se actualizó correctamente
     */
    public boolean updateTelefonos(Long cedulaPer, List<Long> nuevosTelefonos) {
        try {
            connection.setAutoCommit(false);
            
            // Eliminar teléfonos existentes
            deleteAllByPersona(cedulaPer);
            
            // Insertar nuevos teléfonos
            if (!nuevosTelefonos.isEmpty()) {
                insertMultiple(cedulaPer, nuevosTelefonos);
            }
            
            connection.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
            System.err.println("Error al actualizar teléfonos: " + e.getMessage());
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
     * Mapea un ResultSet a un objeto TelefonoPer
     * @param rs ResultSet con los datos
     * @return objeto TelefonoPer
     */
    private TelefonoPer mapResultSetToTelefonoPer(ResultSet rs) throws SQLException {
        TelefonoPer telefono = new TelefonoPer();
        telefono.setCedulaPer(rs.getLong("cedulaPer"));
        telefono.setTelefonoPer(rs.getLong("telefonoPer"));
        return telefono;
    }
}