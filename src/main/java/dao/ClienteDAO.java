package dao;


import modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import db.PostgreSQLConnection;

/**
 * DAO para la tabla Cliente
 * Implementa las operaciones CRUD
 */
public class ClienteDAO {
    
    private Connection connection;
    
    public ClienteDAO() throws SQLException {
        this.connection = PostgreSQLConnection.getConnector().getConn();
    }
    
    /**
     * Inserta un nuevo cliente en la base de datos
     * Este método asume que la persona ya existe en la tabla Persona
     * @param cliente objeto Cliente a insertar
     * @return true si se insertó correctamente
     */
    public boolean insert(Cliente cliente) {
        String sql = "INSERT INTO Cliente (cedulaPer) VALUES (?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cliente.getCedulaPer());
            
            int rowsAffected = stmt.executeUpdate();
            
            // Insertar correos si existen
            if (rowsAffected > 0 && cliente.getCorreos() != null) {
                insertCorreos(cliente.getCedulaPer(), cliente.getCorreos());
            }
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Inserta un cliente completo (persona + cliente + correos)
     * @param cliente objeto Cliente con todos los datos
     * @return true si se insertó correctamente
     */
    public boolean insertCompleto(Cliente cliente) {
        try {
            connection.setAutoCommit(false);
            
            // Insertar persona
            PersonaDAO personaDAO = new PersonaDAO();
            if (!personaDAO.insert(cliente)) {
                connection.rollback();
                return false;
            }
            
            // Insertar cliente
            if (!insert(cliente)) {
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
            System.err.println("Error al insertar cliente completo: " + e.getMessage());
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
     * Elimina un cliente por su cédula
     * @param cedulaPer cédula del cliente a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean delete(Long cedulaPer) {
        String sql = "DELETE FROM Cliente WHERE cedulaPer = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca un cliente por su cédula
     * @param cedulaPer cédula del cliente a buscar
     * @return objeto Cliente o null si no se encuentra
     */
    public Cliente findById(Long cedulaPer) {
        String sql = "SELECT p.*, c.cedulaPer as cliente_cedula " +
                     "FROM Persona p " +
                     "INNER JOIN Cliente c ON p.cedulaPer = c.cedulaPer " +
                     "WHERE c.cedulaPer = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = mapResultSetToCliente(rs);
                    cliente.setCorreos(getCorreos(cedulaPer));
                    return cliente;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Obtiene todos los clientes
     * @return lista de clientes
     */
    public List<Cliente> findAll() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT p.*, c.cedulaPer as cliente_cedula " +
                     "FROM Persona p " +
                     "INNER JOIN Cliente c ON p.cedulaPer = c.cedulaPer " +
                     "ORDER BY p.cedulaPer";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Cliente cliente = mapResultSetToCliente(rs);
                cliente.setCorreos(getCorreos(cliente.getCedulaPer()));
                clientes.add(cliente);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener clientes: " + e.getMessage());
        }
        return clientes;
    }
    
    /**
     * Inserta correos para un cliente
     * @param cedulaPer cédula del cliente
     * @param correos lista de correos
     */
    private void insertCorreos(Long cedulaPer, List<String> correos) {
        String sql = "INSERT INTO Correo (cedulaPer, correo) VALUES (?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (String correo : correos) {
                stmt.setLong(1, cedulaPer);
                stmt.setString(2, correo);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar correos: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene los correos de un cliente
     * @param cedulaPer cédula del cliente
     * @return lista de correos
     */
    private List<String> getCorreos(Long cedulaPer) {
        List<String> correos = new ArrayList<>();
        String sql = "SELECT correo FROM Correo WHERE cedulaPer = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    correos.add(rs.getString("correo"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener correos: " + e.getMessage());
        }
        return correos;
    }
    
    /**
     * Mapea un ResultSet a un objeto Cliente
     * @param rs ResultSet con los datos
     * @return objeto Cliente
     */
    private Cliente mapResultSetToCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setCedulaPer(rs.getLong("cedulaPer"));
        cliente.setPrimerNom(rs.getString("primerNom"));
        cliente.setSegundoNom(rs.getString("segundoNom"));
        cliente.setPrimerApell(rs.getString("primerApell"));
        cliente.setSegundoApell(rs.getString("segundoApell"));
        cliente.setCalle(rs.getString("calle"));
        cliente.setCarrera(rs.getString("carrera"));
        cliente.setNumero(rs.getString("numero"));
        cliente.setComplemento(rs.getString("complemento"));
        return cliente;
    }
}