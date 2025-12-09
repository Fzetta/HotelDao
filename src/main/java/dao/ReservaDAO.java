package dao;

import modelo.Cliente;
import modelo.Habitacion;
import modelo.Reserva;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import db.PostgreSQLConnection;

/**
 * DAO para la tabla Reserva
 * Implementa las operaciones CRUD
 */
public class ReservaDAO {
    
    private Connection connection;
    
    public ReservaDAO() throws SQLException {
        this.connection = PostgreSQLConnection.getConnector().getConn();
    }
    
    /**
     * Inserta una nueva reserva en la base de datos
     * @param reserva objeto Reserva a insertar
     * @return true si se insertó correctamente
     */
    public boolean insert(Reserva reserva) {
        String sql = "INSERT INTO Reserva (cedulaPer, numeroHab, fechaLlegada, " +
                     "fechaSalida, tiempoMaxCancel) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, reserva.getCedulaPer());
            stmt.setInt(2, reserva.getNumeroHab());
            stmt.setDate(3, Date.valueOf(reserva.getFechaLlegada()));
            stmt.setDate(4, Date.valueOf(reserva.getFechaSalida()));
            stmt.setInt(5, reserva.getTiempoMaxCancel());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar reserva: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza una reserva existente
     * @param reserva objeto Reserva con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean update(Reserva reserva, Long oldCedulaPer, Integer oldNumeroHab, 
                         LocalDate oldFechaLlegada) {
        String sql = "UPDATE Reserva SET cedulaPer = ?, numeroHab = ?, " +
                     "fechaLlegada = ?, fechaSalida = ?, tiempoMaxCancel = ? " +
                     "WHERE cedulaPer = ? AND numeroHab = ? AND fechaLlegada = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, reserva.getCedulaPer());
            stmt.setInt(2, reserva.getNumeroHab());
            stmt.setDate(3, Date.valueOf(reserva.getFechaLlegada()));
            stmt.setDate(4, Date.valueOf(reserva.getFechaSalida()));
            stmt.setInt(5, reserva.getTiempoMaxCancel());
            stmt.setLong(6, oldCedulaPer);
            stmt.setInt(7, oldNumeroHab);
            stmt.setDate(8, Date.valueOf(oldFechaLlegada));
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar reserva: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza solo las fechas y tiempo de cancelación
     * @param cedulaPer cédula del cliente
     * @param numeroHab número de habitación
     * @param fechaLlegada fecha de llegada
     * @param nuevaFechaSalida nueva fecha de salida
     * @param nuevoTiempoCancel nuevo tiempo de cancelación
     * @return true si se actualizó correctamente
     */
    public boolean updateFechas(Long cedulaPer, Integer numeroHab, LocalDate fechaLlegada,
                               LocalDate nuevaFechaSalida, Integer nuevoTiempoCancel) {
        String sql = "UPDATE Reserva SET fechaSalida = ?, tiempoMaxCancel = ? " +
                     "WHERE cedulaPer = ? AND numeroHab = ? AND fechaLlegada = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(nuevaFechaSalida));
            stmt.setInt(2, nuevoTiempoCancel);
            stmt.setLong(3, cedulaPer);
            stmt.setInt(4, numeroHab);
            stmt.setDate(5, Date.valueOf(fechaLlegada));
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar fechas: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina una reserva
     * @param cedulaPer cédula del cliente
     * @param numeroHab número de habitación
     * @param fechaLlegada fecha de llegada
     * @return true si se eliminó correctamente
     */
    public boolean delete(Long cedulaPer, Integer numeroHab, LocalDate fechaLlegada) {
        String sql = "DELETE FROM Reserva WHERE cedulaPer = ? AND numeroHab = ? " +
                     "AND fechaLlegada = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            stmt.setInt(2, numeroHab);
            stmt.setDate(3, Date.valueOf(fechaLlegada));
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar reserva: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca una reserva específica
     * @param cedulaPer cédula del cliente
     * @param numeroHab número de habitación
     * @param fechaLlegada fecha de llegada
     * @return objeto Reserva o null si no se encuentra
     */
    public Reserva findById(Long cedulaPer, Integer numeroHab, LocalDate fechaLlegada) {
        String sql = "SELECT * FROM Reserva WHERE cedulaPer = ? AND numeroHab = ? " +
                     "AND fechaLlegada = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            stmt.setInt(2, numeroHab);
            stmt.setDate(3, Date.valueOf(fechaLlegada));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReserva(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reserva: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Obtiene todas las reservas
     * @return lista de reservas
     */
    public List<Reserva> findAll() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM Reserva ORDER BY fechaLlegada DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservas.add(mapResultSetToReserva(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener reservas: " + e.getMessage());
        }
        return reservas;
    }
    
    /**
     * Busca reservas por cliente
     * @param cedulaPer cédula del cliente
     * @return lista de reservas del cliente
     */
    public List<Reserva> findByCliente(Long cedulaPer) {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM Reserva WHERE cedulaPer = ? ORDER BY fechaLlegada DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapResultSetToReserva(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reservas por cliente: " + e.getMessage());
        }
        return reservas;
    }
    
    /**
     * Busca reservas por habitación
     * @param numeroHab número de habitación
     * @return lista de reservas de la habitación
     */
    public List<Reserva> findByHabitacion(Integer numeroHab) {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM Reserva WHERE numeroHab = ? ORDER BY fechaLlegada DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, numeroHab);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapResultSetToReserva(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reservas por habitación: " + e.getMessage());
        }
        return reservas;
    }
    
    /**
     * Busca reservas activas (fecha de salida >= hoy)
     * @return lista de reservas activas
     */
    public List<Reserva> findReservasActivas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM Reserva WHERE fechaSalida >= CURRENT_DATE " +
                     "ORDER BY fechaLlegada";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservas.add(mapResultSetToReserva(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener reservas activas: " + e.getMessage());
        }
        return reservas;
    }
    
    /**
     * Obtiene reservas con información completa de cliente y habitación
     * @return lista de reservas con objetos relacionados
     */
    public List<Reserva> findAllWithDetails() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT r.*, " +
                     "p.primerNom, p.segundoNom, p.primerApell, p.segundoApell, " +
                     "h.categoria, h.estadoHab, h.precioNoche " +
                     "FROM Reserva r " +
                     "INNER JOIN Cliente c ON r.cedulaPer = c.cedulaPer " +
                     "INNER JOIN Persona p ON c.cedulaPer = p.cedulaPer " +
                     "INNER JOIN Habitacion h ON r.numeroHab = h.numeroHab " +
                     "ORDER BY r.fechaLlegada DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Reserva reserva = mapResultSetToReserva(rs);
                
                // Crear objetos relacionados
                Cliente cliente = new Cliente();
                cliente.setCedulaPer(rs.getLong("cedulaPer"));
                cliente.setPrimerNom(rs.getString("primerNom"));
                cliente.setSegundoNom(rs.getString("segundoNom"));
                cliente.setPrimerApell(rs.getString("primerApell"));
                cliente.setSegundoApell(rs.getString("segundoApell"));
                
                Habitacion habitacion = new Habitacion();
                habitacion.setNumeroHab(rs.getInt("numeroHab"));
                habitacion.setCategoria(rs.getString("categoria"));
                habitacion.setEstadoHab(rs.getString("estadoHab"));
                habitacion.setPrecioNoche(rs.getBigDecimal("precioNoche"));
                
                reserva.setCliente(cliente);
                reserva.setHabitacion(habitacion);
                reservas.add(reserva);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener reservas con detalles: " + e.getMessage());
        }
        return reservas;
    }
    
    /**
     * Mapea un ResultSet a un objeto Reserva
     * @param rs ResultSet con los datos
     * @return objeto Reserva
     */
    private Reserva mapResultSetToReserva(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva();
        reserva.setCedulaPer(rs.getLong("cedulaPer"));
        reserva.setNumeroHab(rs.getInt("numeroHab"));
        reserva.setFechaLlegada(rs.getDate("fechaLlegada").toLocalDate());
        reserva.setFechaSalida(rs.getDate("fechaSalida").toLocalDate());
        reserva.setTiempoMaxCancel(rs.getInt("tiempoMaxCancel"));
        return reserva;
    }
}