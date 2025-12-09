package modelo;

/**
 * Modelo de la tabla Empleado
 * Hereda de Persona
 */
public class Empleado extends Persona {
    
    private String cargo;
    private Long idArea;
    
    // Objeto relacionado para consultas con JOIN
    private Area area;
    
    // Constructor vacío
    public Empleado() {
        super();
    }
    
    // Constructor con cédula
    public Empleado(Long cedulaPer) {
        super();
        this.setCedulaPer(cedulaPer);
    }
    
    // Constructor completo
    public Empleado(Long cedulaPer, String primerNom, String segundoNom, 
                    String primerApell, String segundoApell, String calle, 
                    String carrera, String numero, String complemento,
                    String cargo, Long idArea) {
        super(cedulaPer, primerNom, segundoNom, primerApell, segundoApell, 
              calle, carrera, numero, complemento);
        this.cargo = cargo;
        this.idArea = idArea;
    }
    
    // Getters y Setters
    public String getCargo() {
        return cargo;
    }
    
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    public Long getIdArea() {
        return idArea;
    }
    
    public void setIdArea(Long idArea) {
        this.idArea = idArea;
    }
    
    public Area getArea() {
        return area;
    }
    
    public void setArea(Area area) {
        this.area = area;
    }
    
    @Override
    public String toString() {
        return "Empleado{" +
                "cedulaPer=" + getCedulaPer() +
                ", nombre='" + getPrimerNom() + " " + getSegundoNom() + " " + 
                getPrimerApell() + " " + getSegundoApell() + '\'' +
                ", cargo='" + cargo + '\'' +
                ", idArea=" + idArea +
                (area != null ? ", area='" + area.getNombreArea() + '\'' : "") +
                '}';
    }
}