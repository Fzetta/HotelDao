package dao;

import modelo.Persona;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import db.PostgreSQLConnection;

/**
 * DAO para la tabla Persona
 * Implementa las operaciones CRUD
 */
public class PersonaDAO {
    
    private Connection connection;
    
    public PersonaDAO() throws SQLException {
        this.connection = PostgreSQLConnection.getConnector().getConn();
    }
    
    /**
     * Inserta una nueva persona en la base de datos
     * @param persona objeto Persona a insertar
     * @return true si se insertó correctamente
     */
    public boolean insert(Persona persona) {
        String sql = "INSERT INTO Persona (cedulaPer, primerNom, segundoNom, " +
                     "primerApell, segundoApell, calle, carrera, numero, complemento) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, persona.getCedulaPer());
            stmt.setString(2, persona.getPrimerNom());
            stmt.setString(3, persona.getSegundoNom());
            stmt.setString(4, persona.getPrimerApell());
            stmt.setString(5, persona.getSegundoApell());
            stmt.setString(6, persona.getCalle());
            stmt.setString(7, persona.getCarrera());
            stmt.setString(8, persona.getNumero());
            stmt.setString(9, persona.getComplemento());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar persona: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza una persona existente
     * @param persona objeto Persona con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean update(Persona persona) {
        String sql = "UPDATE Persona SET primerNom = ?, segundoNom = ?, " +
                     "primerApell = ?, segundoApell = ?, calle = ?, carrera = ?, " +
                     "numero = ?, complemento = ? WHERE cedulaPer = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, persona.getPrimerNom());
            stmt.setString(2, persona.getSegundoNom());
            stmt.setString(3, persona.getPrimerApell());
            stmt.setString(4, persona.getSegundoApell());
            stmt.setString(5, persona.getCalle());
            stmt.setString(6, persona.getCarrera());
            stmt.setString(7, persona.getNumero());
            stmt.setString(8, persona.getComplemento());
            stmt.setLong(9, persona.getCedulaPer());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar persona: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina una persona por su cédula
     * @param cedulaPer cédula de la persona a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean delete(Long cedulaPer) {
        String sql = "DELETE FROM Persona WHERE cedulaPer = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar persona: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca una persona por su cédula
     * @param cedulaPer cédula de la persona a buscar
     * @return objeto Persona o null si no se encuentra
     */
    public Persona findById(Long cedulaPer) {
        String sql = "SELECT * FROM Persona WHERE cedulaPer = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaPer);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPersona(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar persona: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Obtiene todas las personas
     * @return lista de personas
     */
    public List<Persona> findAll() {
        List<Persona> personas = new ArrayList<>();
        String sql = "SELECT * FROM Persona ORDER BY cedulaPer";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                personas.add(mapResultSetToPersona(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener personas: " + e.getMessage());
        }
        return personas;
    }
    
    /**
     * Busca personas por apellido
     * @param apellido apellido a buscar
     * @return lista de personas que coinciden
     */
    public List<Persona> findByApellido(String apellido) {
        List<Persona> personas = new ArrayList<>();
        String sql = "SELECT * FROM Persona WHERE primerApell LIKE ? OR segundoApell LIKE ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String pattern = "%" + apellido + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    personas.add(mapResultSetToPersona(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por apellido: " + e.getMessage());
        }
        return personas;
    }
    
    /**
     * Mapea un ResultSet a un objeto Persona
     * @param rs ResultSet con los datos
     * @return objeto Persona
     */
    private Persona mapResultSetToPersona(ResultSet rs) throws SQLException {
        Persona persona = new Persona();
        persona.setCedulaPer(rs.getLong("cedulaPer"));
        persona.setPrimerNom(rs.getString("primerNom"));
        persona.setSegundoNom(rs.getString("segundoNom"));
        persona.setPrimerApell(rs.getString("primerApell"));
        persona.setSegundoApell(rs.getString("segundoApell"));
        persona.setCalle(rs.getString("calle"));
        persona.setCarrera(rs.getString("carrera"));
        persona.setNumero(rs.getString("numero"));
        persona.setComplemento(rs.getString("complemento"));
        return persona;
    }
}