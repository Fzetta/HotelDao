package modelo;

/**
 * Modelo de la tabla Area
 */
public class Area {
    
    private Long idArea;
    private String nombreArea;
    
    // Constructor vacío
    public Area() {
    }
    
    // Constructor completo
    public Area(Long idArea, String nombreArea) {
        this.idArea = idArea;
        this.nombreArea = nombreArea;
    }
    
    // Getters y Setters
    public Long getIdArea() {
        return idArea;
    }
    
    public void setIdArea(Long idArea) {
        this.idArea = idArea;
    }
    
    public String getNombreArea() {
        return nombreArea;
    }
    
    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }
    
    @Override
    public String toString() {
        return "Area{" +
                "idArea=" + idArea +
                ", nombreArea='" + nombreArea + '\'' +
                '}';
    }
}