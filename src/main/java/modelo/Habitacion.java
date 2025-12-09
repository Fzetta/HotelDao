package modelo;

import java.math.BigDecimal;

/**
 * Modelo de la tabla Habitacion
 */
public class Habitacion {
    
    private Integer numeroHab;
    private String categoria;
    private String estadoHab;
    private BigDecimal precioNoche;
    
    // Constructor vacío
    public Habitacion() {
    }
    
    // Constructor completo
    public Habitacion(Integer numeroHab, String categoria, String estadoHab, 
                      BigDecimal precioNoche) {
        this.numeroHab = numeroHab;
        this.categoria = categoria;
        this.estadoHab = estadoHab;
        this.precioNoche = precioNoche;
    }
    
    // Getters y Setters
    public Integer getNumeroHab() {
        return numeroHab;
    }
    
    public void setNumeroHab(Integer numeroHab) {
        this.numeroHab = numeroHab;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getEstadoHab() {
        return estadoHab;
    }
    
    public void setEstadoHab(String estadoHab) {
        this.estadoHab = estadoHab;
    }
    
    public BigDecimal getPrecioNoche() {
        return precioNoche;
    }
    
    public void setPrecioNoche(BigDecimal precioNoche) {
        this.precioNoche = precioNoche;
    }
    
    @Override
    public String toString() {
        return "Habitacion{" +
                "numeroHab=" + numeroHab +
                ", categoria='" + categoria + '\'' +
                ", estadoHab='" + estadoHab + '\'' +
                ", precioNoche=" + precioNoche +
                '}';
    }
}