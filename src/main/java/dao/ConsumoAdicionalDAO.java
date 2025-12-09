package dao;


import modelo.ConsumoAdicional;
import modelo.Servicio;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import db.PostgreSQLConnection;

/**
 * DAO para la tabla ConsumoAdicional
 * Implementa las operaciones CRUD
 */
public class ConsumoAdicionalDAO {
    
    private Connection connection;
    
    public ConsumoAdicionalDAO() throws SQLException {
        this.connection = PostgreSQLConnection.getConnector().getConn();
    }
    
    /**
     * Inserta un nuevo consumo adicional
     * @param consumo objeto ConsumoAdicional a insertar
     * @return true si se insertó correctamente
     */
    public boolean insert(ConsumoAdicional consumo) {
        String sql = "INSERT INTO ConsumoAdicional (fechaConsumo, horaConsumo, fechaLlegada, " +
                     "numeroHab, cedulaPer, idServicio) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(consumo.getFechaConsumo()));
            stmt.setTime(2, Time.valueOf(consumo.getHoraConsumo()));
            stmt.setDate(3, Date.valueOf(consumo.getFechaLlegada()));
            stmt.setInt(4, consumo.getNumeroHab());
            stmt.setLong(5, consumo.getCedulaPer());
            stmt.setLong(6, consumo.getIdServicio());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar consumo adicional: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina un consumo adicional específico
     * @param fechaConsumo fecha del consumo
     * @param horaConsumo hora del consumo
     * @param fechaLlegada fecha de llegada de la reserva
     * @param numeroHab número de habitación
     * @param cedulaPer cédula del cliente
     * @param idServicio ID del servicio
     * @return true si se eliminó correctamente
     */
    public boolean delete(LocalDate fechaConsumo, LocalTime horaConsumo, LocalDate fechaLlegada,
                         Integer numeroHab, Long cedulaPer, Long idServicio) {
        String sql = "DELETE FROM ConsumoAdicional WHERE fechaConsumo = ? AND horaConsumo = ? " +
                     "AND fechaLlegada = ? AND numeroHab = ? AND cedulaPer = ? AND idServicio = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(fechaConsumo));
            stmt.setTime(2, Time.valueOf(horaConsumo));
            stmt.setDate(3, Date.valueOf(fechaLlegada));
            stmt.setInt(4, numeroHab);
            stmt.setLong(5, cedulaPer);
            stmt.setLong(6, idServicio);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar consumo adicional: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca un consumo adicional específico
     * @return objeto ConsumoAdicional o null si no se encuentra
     */
    public ConsumoAdicional findById(LocalDate fechaConsumo, LocalTime horaConsumo, 
                                     LocalDate fechaLlegada, Integer numeroHab, 
                                     Long cedulaPer, Long idServicio) {
        String sql = "SELECT * FROM ConsumoAdicional WHERE fechaConsumo = ? AND horaConsumo = ? " +
                     "AND fechaLlegada = ? AND numeroHab = ? AND cedulaPer = ? AND idServicio = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(fechaConsumo));
            stmt.setTime(2, Time.valueOf(horaConsumo));
            stmt.setDate(3, Date.valueOf(fechaLlegada));
            stmt.setInt(4, numeroHab);
            stmt.setLong(5, cedulaPer);
            stmt.setLong(6, idServicio);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToConsumoAdicional(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar consumo adicional: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Obtiene todos los consumos adicionales
     * @return lista de consumos
     */
    public List<ConsumoAdicional> findAll() {
        List<ConsumoAdicional> consumos = new ArrayList<>();
        String sql = "SELECT * FROM ConsumoAdicional ORDER BY fechaConsumo DESC, horaConsumo DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                consumos.add(mapResultSetToConsumoAdicional(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener consumos adicionales: " + e.getMessage());
        }
        return consumos;
    }
    
    /**
     * Busca consumos por reserva
     * @param cedulaPer cédula del cliente
     * @param numeroHab número de habitación
     * @param fechaLlegada fecha de llegada
     * @return lista de consumos de esa reserva
     */
    public List<ConsumoAdicional> findByReserva(Long cedulaPer, Integer numeroHab, 
                                                LocalDate fechaLlegada) {
        List<ConsumoAdicional> consumos = new ArrayList<>();
        String sql = "SELECT * FROM ConsumoAdicional WHERE cedulaPer = ? AND numeroHab = ? " +
                     "AND fechaLlegada = ? ORDER BY fechaConsumo, horaConsumo";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            stmt.setInt(2, numeroHab);
            stmt.setDate(3, Date.valueOf(fechaLlegada));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consumos.add(mapResultSetToConsumoAdicional(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar consumos por reserva: " + e.getMessage());
        }
        return consumos;
    }
    
    /**
     * Busca consumos por cliente
     * @param cedulaPer cédula del cliente
     * @return lista de consumos del cliente
     */
    public List<ConsumoAdicional> findByCliente(Long cedulaPer) {
        List<ConsumoAdicional> consumos = new ArrayList<>();
        String sql = "SELECT * FROM ConsumoAdicional WHERE cedulaPer = ? " +
                     "ORDER BY fechaConsumo DESC, horaConsumo DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consumos.add(mapResultSetToConsumoAdicional(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar consumos por cliente: " + e.getMessage());
        }
        return consumos;
    }
    
    /**
     * Busca consumos por servicio
     * @param idServicio ID del servicio
     * @return lista de consumos de ese servicio
     */
    public List<ConsumoAdicional> findByServicio(Long idServicio) {
        List<ConsumoAdicional> consumos = new ArrayList<>();
        String sql = "SELECT * FROM ConsumoAdicional WHERE idServicio = ? " +
                     "ORDER BY fechaConsumo DESC, horaConsumo DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idServicio);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consumos.add(mapResultSetToConsumoAdicional(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar consumos por servicio: " + e.getMessage());
        }
        return consumos;
    }
    
    /**
     * Busca consumos por fecha
     * @param fecha fecha del consumo
     * @return lista de consumos en esa fecha
     */
    public List<ConsumoAdicional> findByFecha(LocalDate fecha) {
        List<ConsumoAdicional> consumos = new ArrayList<>();
        String sql = "SELECT * FROM ConsumoAdicional WHERE fechaConsumo = ? " +
                     "ORDER BY horaConsumo";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(fecha));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consumos.add(mapResultSetToConsumoAdicional(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar consumos por fecha: " + e.getMessage());
        }
        return consumos;
    }
    
    /**
     * Obtiene consumos con información completa del servicio
     * @return lista de consumos con objetos Servicio relacionados
     */
    public List<ConsumoAdicional> findAllWithDetails() {
        List<ConsumoAdicional> consumos = new ArrayList<>();
        String sql = "SELECT c.*, s.nomServicio, s.contenidoServicio, s.costoServicio " +
                     "FROM ConsumoAdicional c " +
                     "INNER JOIN Servicio s ON c.idServicio = s.idServicio " +
                     "ORDER BY c.fechaConsumo DESC, c.horaConsumo DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ConsumoAdicional consumo = mapResultSetToConsumoAdicional(rs);
                
                // Crear objeto Servicio relacionado
                Servicio servicio = new Servicio();
                servicio.setIdServicio(rs.getLong("idServicio"));
                servicio.setNomServicio(rs.getString("nomServicio"));
                servicio.setContenidoServicio(rs.getString("contenidoServicio"));
                servicio.setCostoServicio(rs.getBigDecimal("costoServicio"));
                
                consumo.setServicio(servicio);
                consumos.add(consumo);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener consumos con detalles: " + e.getMessage());
        }
        return consumos;
    }
    
    /**
     * Calcula el total de consumos de una reserva
     * @param cedulaPer cédula del cliente
     * @param numeroHab número de habitación
     * @param fechaLlegada fecha de llegada
     * @return total de consumos
     */
    public double calcularTotalReserva(Long cedulaPer, Integer numeroHab, LocalDate fechaLlegada) {
        String sql = "SELECT SUM(s.costoServicio) as total " +
                     "FROM ConsumoAdicional c " +
                     "INNER JOIN Servicio s ON c.idServicio = s.idServicio " +
                     "WHERE c.cedulaPer = ? AND c.numeroHab = ? AND c.fechaLlegada = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            stmt.setInt(2, numeroHab);
            stmt.setDate(3, Date.valueOf(fechaLlegada));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular total de reserva: " + e.getMessage());
        }
        return 0.0;
    }
    
    /**
     * Obtiene estadísticas de consumo por servicio
     * @return mapa con ID de servicio y cantidad de consumos
     */
    public List<Object[]> getEstadisticasConsumo() {
        List<Object[]> estadisticas = new ArrayList<>();
        String sql = "SELECT s.idServicio, s.nomServicio, COUNT(*) as cantidad, " +
                     "SUM(s.costoServicio) as total " +
                     "FROM ConsumoAdicional c " +
                     "INNER JOIN Servicio s ON c.idServicio = s.idServicio " +
                     "GROUP BY s.idServicio, s.nomServicio " +
                     "ORDER BY cantidad DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] stats = new Object[4];
                stats[0] = rs.getLong("idServicio");
                stats[1] = rs.getString("nomServicio");
                stats[2] = rs.getInt("cantidad");
                stats[3] = rs.getDouble("total");
                estadisticas.add(stats);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener estadísticas: " + e.getMessage());
        }
        return estadisticas;
    }
    
    /**
     * Mapea un ResultSet a un objeto ConsumoAdicional
     * @param rs ResultSet con los datos
     * @return objeto ConsumoAdicional
     */
    private ConsumoAdicional mapResultSetToConsumoAdicional(ResultSet rs) throws SQLException {
        ConsumoAdicional consumo = new ConsumoAdicional();
        consumo.setFechaConsumo(rs.getDate("fechaConsumo").toLocalDate());
        consumo.setHoraConsumo(rs.getTime("horaConsumo").toLocalTime());
        consumo.setFechaLlegada(rs.getDate("fechaLlegada").toLocalDate());
        consumo.setNumeroHab(rs.getInt("numeroHab"));
        consumo.setCedulaPer(rs.getLong("cedulaPer"));
        consumo.setIdServicio(rs.getLong("idServicio"));
        return consumo;
    }
}