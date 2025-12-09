package dao;

import modelo.Servicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import db.PostgreSQLConnection;

/**
 * DAO para la tabla Servicio
 * Implementa las operaciones CRUD
 */
public class ServicioDAO {
    
    private Connection connection;
    
    public ServicioDAO() throws SQLException {
        this.connection = PostgreSQLConnection.getConnector().getConn();
    }
    
    /**
     * Inserta un nuevo servicio en la base de datos
     * @param servicio objeto Servicio a insertar
     * @return true si se insertó correctamente
     */
    public boolean insert(Servicio servicio) {
        String sql = "INSERT INTO Servicio (idServicio, nomServicio, contenidoServicio, " +
                     "costoServicio) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, servicio.getIdServicio());
            stmt.setString(2, servicio.getNomServicio());
            stmt.setString(3, servicio.getContenidoServicio());
            stmt.setBigDecimal(4, servicio.getCostoServicio());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar servicio: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza un servicio existente
     * @param servicio objeto Servicio con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean update(Servicio servicio) {
        String sql = "UPDATE Servicio SET nomServicio = ?, contenidoServicio = ?, " +
                     "costoServicio = ? WHERE idServicio = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, servicio.getNomServicio());
            stmt.setString(2, servicio.getContenidoServicio());
            stmt.setBigDecimal(3, servicio.getCostoServicio());
            stmt.setLong(4, servicio.getIdServicio());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar servicio: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina un servicio por su ID
     * @param idServicio ID del servicio a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean delete(Long idServicio) {
        String sql = "DELETE FROM Servicio WHERE idServicio = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idServicio);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar servicio: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca un servicio por su ID
     * @param idServicio ID del servicio a buscar
     * @return objeto Servicio o null si no se encuentra
     */
    public Servicio findById(Long idServicio) {
        String sql = "SELECT * FROM Servicio WHERE idServicio = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idServicio);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToServicio(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar servicio: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Obtiene todos los servicios
     * @return lista de servicios
     */
    public List<Servicio> findAll() {
        List<Servicio> servicios = new ArrayList<>();
        String sql = "SELECT * FROM Servicio ORDER BY idServicio";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                servicios.add(mapResultSetToServicio(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener servicios: " + e.getMessage());
        }
        return servicios;
    }
    
    /**
     * Busca servicios por nombre (búsqueda parcial)
     * @param nombre nombre a buscar
     * @return lista de servicios que coinciden
     */
    public List<Servicio> findByNombre(String nombre) {
        List<Servicio> servicios = new ArrayList<>();
        String sql = "SELECT * FROM Servicio WHERE nomServicio LIKE ? ORDER BY nomServicio";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    servicios.add(mapResultSetToServicio(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por nombre: " + e.getMessage());
        }
        return servicios;
    }
    
    /**
     * Mapea un ResultSet a un objeto Servicio
     * @param rs ResultSet con los datos
     * @return objeto Servicio
     */
    private Servicio mapResultSetToServicio(ResultSet rs) throws SQLException {
        Servicio servicio = new Servicio();
        servicio.setIdServicio(rs.getLong("idServicio"));
        servicio.setNomServicio(rs.getString("nomServicio"));
        servicio.setContenidoServicio(rs.getString("contenidoServicio"));
        servicio.setCostoServicio(rs.getBigDecimal("costoServicio"));
        return servicio;
    }
}