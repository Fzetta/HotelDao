package modelo;

/**
 * Modelo de la tabla TelefonoPer
 * Representa la relación entre Persona y sus teléfonos
 */
public class TelefonoPer {
    
    private Long cedulaPer;
    private Long telefonoPer;
    
    // Constructor vacío
    public TelefonoPer() {
    }
    
    // Constructor completo
    public TelefonoPer(Long cedulaPer, Long telefonoPer) {
        this.cedulaPer = cedulaPer;
        this.telefonoPer = telefonoPer;
    }
    
    // Getters y Setters
    public Long getCedulaPer() {
        return cedulaPer;
    }
    
    public void setCedulaPer(Long cedulaPer) {
        this.cedulaPer = cedulaPer;
    }
    
    public Long getTelefonoPer() {
        return telefonoPer;
    }
    
    public void setTelefonoPer(Long telefonoPer) {
        this.telefonoPer = telefonoPer;
    }
    
    @Override
    public String toString() {
        return "TelefonoPer{" +
                "cedulaPer=" + cedulaPer +
                ", telefonoPer=" + telefonoPer +
                '}';
    }
}