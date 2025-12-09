package dao;


import modelo.Area;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import db.PostgreSQLConnection;

/**
 * DAO para la tabla Area
 * Implementa las operaciones CRUD
 */
public class AreaDAO {
    
    private Connection connection;
    
    public AreaDAO() throws SQLException {
        this.connection = PostgreSQLConnection.getConnector().getConn();
    }
    
    /**
     * Inserta una nueva área en la base de datos
     * @param area objeto Area a insertar
     * @return true si se insertó correctamente
     */
    public boolean insert(Area area) {
        String sql = "INSERT INTO Area (idArea, nombreArea) VALUES (?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, area.getIdArea());
            stmt.setString(2, area.getNombreArea());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar área: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza un área existente
     * @param area objeto Area con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean update(Area area) {
        String sql = "UPDATE Area SET nombreArea = ? WHERE idArea = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, area.getNombreArea());
            stmt.setLong(2, area.getIdArea());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar área: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina un área por su ID
     * @param idArea ID del área a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean delete(Long idArea) {
        String sql = "DELETE FROM Area WHERE idArea = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idArea);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar área: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca un área por su ID
     * @param idArea ID del área a buscar
     * @return objeto Area o null si no se encuentra
     */
    public Area findById(Long idArea) {
        String sql = "SELECT * FROM Area WHERE idArea = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idArea);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToArea(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar área: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Obtiene todas las áreas
     * @return lista de áreas
     */
    public List<Area> findAll() {
        List<Area> areas = new ArrayList<>();
        String sql = "SELECT * FROM Area ORDER BY idArea";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                areas.add(mapResultSetToArea(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener áreas: " + e.getMessage());
        }
        return areas;
    }
    
    /**
     * Busca áreas por nombre
     * @param nombre nombre a buscar
     * @return lista de áreas que coinciden
     */
    public List<Area> findByNombre(String nombre) {
        List<Area> areas = new ArrayList<>();
        String sql = "SELECT * FROM Area WHERE nombreArea LIKE ? ORDER BY nombreArea";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    areas.add(mapResultSetToArea(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por nombre: " + e.getMessage());
        }
        return areas;
    }
    
    /**
     * Mapea un ResultSet a un objeto Area
     * @param rs ResultSet con los datos
     * @return objeto Area
     */
    private Area mapResultSetToArea(ResultSet rs) throws SQLException {
        Area area = new Area();
        area.setIdArea(rs.getLong("idArea"));
        area.setNombreArea(rs.getString("nombreArea"));
        return area;
    }
}