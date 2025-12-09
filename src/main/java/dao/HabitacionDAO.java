package dao;


import modelo.Habitacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import db.PostgreSQLConnection;

/**
 * DAO para la tabla Habitacion
 * Implementa las operaciones CRUD
 */
public class HabitacionDAO {
    
    private Connection connection;
    
    public HabitacionDAO() throws SQLException {
        this.connection = PostgreSQLConnection.getConnector().getConn();
    }
    
    /**
     * Inserta una nueva habitación en la base de datos
     * @param habitacion objeto Habitacion a insertar
     * @return true si se insertó correctamente
     */
    public boolean insert(Habitacion habitacion) {
        String sql = "INSERT INTO Habitacion (numeroHab, categoria, estadoHab, precioNoche) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, habitacion.getNumeroHab());
            stmt.setString(2, habitacion.getCategoria());
            stmt.setString(3, habitacion.getEstadoHab());
            stmt.setBigDecimal(4, habitacion.getPrecioNoche());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar habitación: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza una habitación existente
     * @param habitacion objeto Habitacion con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean update(Habitacion habitacion) {
        String sql = "UPDATE Habitacion SET categoria = ?, estadoHab = ?, " +
                     "precioNoche = ? WHERE numeroHab = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, habitacion.getCategoria());
            stmt.setString(2, habitacion.getEstadoHab());
            stmt.setBigDecimal(3, habitacion.getPrecioNoche());
            stmt.setInt(4, habitacion.getNumeroHab());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar habitación: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina una habitación por su número
     * @param numeroHab número de la habitación a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean delete(Integer numeroHab) {
        String sql = "DELETE FROM Habitacion WHERE numeroHab = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, numeroHab);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar habitación: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca una habitación por su número
     * @param numeroHab número de la habitación a buscar
     * @return objeto Habitacion o null si no se encuentra
     */
    public Habitacion findById(Integer numeroHab) {
        String sql = "SELECT * FROM Habitacion WHERE numeroHab = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, numeroHab);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHabitacion(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar habitación: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Obtiene todas las habitaciones
     * @return lista de habitaciones
     */
    public List<Habitacion> findAll() {
        List<Habitacion> habitaciones = new ArrayList<>();
        String sql = "SELECT * FROM Habitacion ORDER BY numeroHab";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                habitaciones.add(mapResultSetToHabitacion(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener habitaciones: " + e.getMessage());
        }
        return habitaciones;
    }
    
    /**
     * Busca habitaciones por categoría
     * @param categoria categoría a buscar
     * @return lista de habitaciones que coinciden
     */
    public List<Habitacion> findByCategoria(String categoria) {
        List<Habitacion> habitaciones = new ArrayList<>();
        String sql = "SELECT * FROM Habitacion WHERE categoria = ? ORDER BY numeroHab";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categoria);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    habitaciones.add(mapResultSetToHabitacion(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por categoría: " + e.getMessage());
        }
        return habitaciones;
    }
    
    /**
     * Busca habitaciones disponibles
     * @return lista de habitaciones disponibles
     */
    public List<Habitacion> findDisponibles() {
        List<Habitacion> habitaciones = new ArrayList<>();
        String sql = "SELECT * FROM Habitacion WHERE estadoHab = 'Disponible' ORDER BY numeroHab";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                habitaciones.add(mapResultSetToHabitacion(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener habitaciones disponibles: " + e.getMessage());
        }
        return habitaciones;
    }
    
    /**
     * Actualiza el estado de una habitación
     * @param numeroHab número de la habitación
     * @param nuevoEstado nuevo estado de la habitación
     * @return true si se actualizó correctamente
     */
    public boolean updateEstado(Integer numeroHab, String nuevoEstado) {
        String sql = "UPDATE Habitacion SET estadoHab = ? WHERE numeroHab = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, numeroHab);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Habitacion
     * @param rs ResultSet con los datos
     * @return objeto Habitacion
     */
    private Habitacion mapResultSetToHabitacion(ResultSet rs) throws SQLException {
        Habitacion habitacion = new Habitacion();
        habitacion.setNumeroHab(rs.getInt("numeroHab"));
        habitacion.setCategoria(rs.getString("categoria"));
        habitacion.setEstadoHab(rs.getString("estadoHab"));
        habitacion.setPrecioNoche(rs.getBigDecimal("precioNoche"));
        return habitacion;
    }
}