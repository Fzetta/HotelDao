package modelo;

/**
 * Modelo de la tabla Correo
 * Representa la relación entre Cliente y sus correos electrónicos
 */
public class Correo {
    
    private Long cedulaPer;
    private String correo;
    
    // Constructor vacío
    public Correo() {
    }
    
    // Constructor completo
    public Correo(Long cedulaPer, String correo) {
        this.cedulaPer = cedulaPer;
        this.correo = correo;
    }
    
    // Getters y Setters
    public Long getCedulaPer() {
        return cedulaPer;
    }
    
    public void setCedulaPer(Long cedulaPer) {
        this.cedulaPer = cedulaPer;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    @Override
    public String toString() {
        return "Correo{" +
                "cedulaPer=" + cedulaPer +
                ", correo='" + correo + '\'' +
                '}';
    }
}