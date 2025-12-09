package modelo;

import java.util.List;

/**
 * Modelo de la tabla Cliente
 * Hereda de Persona
 */
public class Cliente extends Persona {
    
    private List<String> correos;
    
    // Constructor vacío
    public Cliente() {
        super();
    }
    
    // Constructor con cédula
    public Cliente(Long cedulaPer) {
        super();
        this.setCedulaPer(cedulaPer);
    }
    
    // Constructor completo
    public Cliente(Long cedulaPer, String primerNom, String segundoNom, 
                   String primerApell, String segundoApell, String calle, 
                   String carrera, String numero, String complemento,
                   List<String> correos) {
        super(cedulaPer, primerNom, segundoNom, primerApell, segundoApell, 
              calle, carrera, numero, complemento);
        this.correos = correos;
    }
    
    // Getters y Setters
    public List<String> getCorreos() {
        return correos;
    }
    
    public void setCorreos(List<String> correos) {
        this.correos = correos;
    }
    
    @Override
    public String toString() {
        return "Cliente{" +
                "cedulaPer=" + getCedulaPer() +
                ", nombre='" + getPrimerNom() + " " + getSegundoNom() + " " + 
                getPrimerApell() + " " + getSegundoApell() + '\'' +
                ", correos=" + correos +
                '}';
    }
}