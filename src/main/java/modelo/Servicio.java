package modelo;

import java.math.BigDecimal;

/**
 * Modelo de la tabla Servicio
 */
public class Servicio {
    
    private Long idServicio;
    private String nomServicio;
    private String contenidoServicio;
    private BigDecimal costoServicio;
    
    // Constructor vacío
    public Servicio() {
    }
    
    // Constructor completo
    public Servicio(Long idServicio, String nomServicio, String contenidoServicio, 
                    BigDecimal costoServicio) {
        this.idServicio = idServicio;
        this.nomServicio = nomServicio;
        this.contenidoServicio = contenidoServicio;
        this.costoServicio = costoServicio;
    }
    
    // Getters y Setters
    public Long getIdServicio() {
        return idServicio;
    }
    
    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }
    
    public String getNomServicio() {
        return nomServicio;
    }
    
    public void setNomServicio(String nomServicio) {
        this.nomServicio = nomServicio;
    }
    
    public String getContenidoServicio() {
        return contenidoServicio;
    }
    
    public void setContenidoServicio(String contenidoServicio) {
        this.contenidoServicio = contenidoServicio;
    }
    
    public BigDecimal getCostoServicio() {
        return costoServicio;
    }
    
    public void setCostoServicio(BigDecimal costoServicio) {
        this.costoServicio = costoServicio;
    }
    
    @Override
    public String toString() {
        return "Servicio{" +
                "idServicio=" + idServicio +
                ", nomServicio='" + nomServicio + '\'' +
                ", contenidoServicio='" + contenidoServicio + '\'' +
                ", costoServicio=" + costoServicio +
                '}';
    }
}