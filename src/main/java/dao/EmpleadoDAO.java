package dao;

import modelo.Area;
import modelo.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import db.PostgreSQLConnection;

/**
 * DAO para la tabla Empleado
 * Implementa las operaciones CRUD
 */
public class EmpleadoDAO {
    
    private Connection connection;
    
    public EmpleadoDAO() throws SQLException {
        this.connection = PostgreSQLConnection.getConnector().getConn();
    }
    
    /**
     * Inserta un nuevo empleado en la base de datos
     * Este método asume que la persona ya existe en la tabla Persona
     * @param empleado objeto Empleado a insertar
     * @return true si se insertó correctamente
     */
    public boolean insert(Empleado empleado) {
        String sql = "INSERT INTO Empleado (cedulaPer, cargo, idArea) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, empleado.getCedulaPer());
            stmt.setString(2, empleado.getCargo());
            stmt.setLong(3, empleado.getIdArea());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar empleado: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Inserta un empleado completo (persona + empleado)
     * @param empleado objeto Empleado con todos los datos
     * @return true si se insertó correctamente
     */
    public boolean insertCompleto(Empleado empleado) {
        try {
            connection.setAutoCommit(false);
            
            // Insertar persona
            PersonaDAO personaDAO = new PersonaDAO();
            if (!personaDAO.insert(empleado)) {
                connection.rollback();
                return false;
            }
            
            // Insertar empleado
            if (!insert(empleado)) {
                connection.rollback();
                return false;
            }
            
            connection.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
            System.err.println("Error al insertar empleado completo: " + e.getMessage());
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
     * Actualiza un empleado existente
     * @param empleado objeto Empleado con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean update(Empleado empleado) {
        String sql = "UPDATE Empleado SET cargo = ?, idArea = ? WHERE cedulaPer = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, empleado.getCargo());
            stmt.setLong(2, empleado.getIdArea());
            stmt.setLong(3, empleado.getCedulaPer());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar empleado: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina un empleado por su cédula
     * @param cedulaPer cédula del empleado a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean delete(Long cedulaPer) {
        String sql = "DELETE FROM Empleado WHERE cedulaPer = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca un empleado por su cédula
     * @param cedulaPer cédula del empleado a buscar
     * @return objeto Empleado o null si no se encuentra
     */
    public Empleado findById(Long cedulaPer) {
        String sql = "SELECT p.*, e.cargo, e.idArea " +
                     "FROM Persona p " +
                     "INNER JOIN Empleado e ON p.cedulaPer = e.cedulaPer " +
                     "WHERE e.cedulaPer = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmpleado(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar empleado: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Obtiene todos los empleados
     * @return lista de empleados
     */
    public List<Empleado> findAll() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT p.*, e.cargo, e.idArea " +
                     "FROM Persona p " +
                     "INNER JOIN Empleado e ON p.cedulaPer = e.cedulaPer " +
                     "ORDER BY p.cedulaPer";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                empleados.add(mapResultSetToEmpleado(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener empleados: " + e.getMessage());
        }
        return empleados;
    }
    
    /**
     * Busca empleados por cargo
     * @param cargo cargo a buscar
     * @return lista de empleados con ese cargo
     */
    public List<Empleado> findByCargo(String cargo) {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT p.*, e.cargo, e.idArea " +
                     "FROM Persona p " +
                     "INNER JOIN Empleado e ON p.cedulaPer = e.cedulaPer " +
                     "WHERE e.cargo = ? " +
                     "ORDER BY p.primerApell, p.primerNom";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cargo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    empleados.add(mapResultSetToEmpleado(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por cargo: " + e.getMessage());
        }
        return empleados;
    }
    
    /**
     * Busca empleados por área
     * @param idArea ID del área
     * @return lista de empleados en esa área
     */
    public List<Empleado> findByArea(Long idArea) {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT p.*, e.cargo, e.idArea " +
                     "FROM Persona p " +
                     "INNER JOIN Empleado e ON p.cedulaPer = e.cedulaPer " +
                     "WHERE e.idArea = ? " +
                     "ORDER BY p.primerApell, p.primerNom";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idArea);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    empleados.add(mapResultSetToEmpleado(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por área: " + e.getMessage());
        }
        return empleados;
    }
    
    /**
     * Obtiene todos los empleados con información completa del área
     * @return lista de empleados con objetos Area relacionados
     */
    public List<Empleado> findAllWithDetails() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT p.*, e.cargo, e.idArea, a.nombreArea " +
                     "FROM Persona p " +
                     "INNER JOIN Empleado e ON p.cedulaPer = e.cedulaPer " +
                     "INNER JOIN Area a ON e.idArea = a.idArea " +
                     "ORDER BY p.primerApell, p.primerNom";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Empleado empleado = mapResultSetToEmpleado(rs);
                
                // Crear objeto Area relacionado
                Area area = new Area();
                area.setIdArea(rs.getLong("idArea"));
                area.setNombreArea(rs.getString("nombreArea"));
                
                empleado.setArea(area);
                empleados.add(empleado);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener empleados con detalles: " + e.getMessage());
        }
        return empleados;
    }
    
    /**
     * Mapea un ResultSet a un objeto Empleado
     * @param rs ResultSet con los datos
     * @return objeto Empleado
     */
    private Empleado mapResultSetToEmpleado(ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado();
        empleado.setCedulaPer(rs.getLong("cedulaPer"));
        empleado.setPrimerNom(rs.getString("primerNom"));
        empleado.setSegundoNom(rs.getString("segundoNom"));
        empleado.setPrimerApell(rs.getString("primerApell"));
        empleado.setSegundoApell(rs.getString("segundoApell"));
        empleado.setCalle(rs.getString("calle"));
        empleado.setCarrera(rs.getString("carrera"));
        empleado.setNumero(rs.getString("numero"));
        empleado.setComplemento(rs.getString("complemento"));
        empleado.setCargo(rs.getString("cargo"));
        empleado.setIdArea(rs.getLong("idArea"));
        return empleado;
    }
}