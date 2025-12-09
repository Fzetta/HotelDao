package modelo;

/**
 * Modelo de la tabla Persona
 */
public class Persona {
    
    private Long cedulaPer;
    private String primerNom;
    private String segundoNom;
    private String primerApell;
    private String segundoApell;
    private String calle;
    private String carrera;
    private String numero;
    private String complemento;
    
    // Constructor vacío
    public Persona() {
    }
    
    // Constructor completo
    public Persona(Long cedulaPer, String primerNom, String segundoNom, 
                   String primerApell, String segundoApell, String calle, 
                   String carrera, String numero, String complemento) {
        this.cedulaPer = cedulaPer;
        this.primerNom = primerNom;
        this.segundoNom = segundoNom;
        this.primerApell = primerApell;
        this.segundoApell = segundoApell;
        this.calle = calle;
        this.carrera = carrera;
        this.numero = numero;
        this.complemento = complemento;
    }
    
    // Getters y Setters
    public Long getCedulaPer() {
        return cedulaPer;
    }
    
    public void setCedulaPer(Long cedulaPer) {
        this.cedulaPer = cedulaPer;
    }
    
    public String getPrimerNom() {
        return primerNom;
    }
    
    public void setPrimerNom(String primerNom) {
        this.primerNom = primerNom;
    }
    
    public String getSegundoNom() {
        return segundoNom;
    }
    
    public void setSegundoNom(String segundoNom) {
        this.segundoNom = segundoNom;
    }
    
    public String getPrimerApell() {
        return primerApell;
    }
    
    public void setPrimerApell(String primerApell) {
        this.primerApell = primerApell;
    }
    
    public String getSegundoApell() {
        return segundoApell;
    }
    
    public void setSegundoApell(String segundoApell) {
        this.segundoApell = segundoApell;
    }
    
    public String getCalle() {
        return calle;
    }
    
    public void setCalle(String calle) {
        this.calle = calle;
    }
    
    public String getCarrera() {
        return carrera;
    }
    
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
    
    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public String getComplemento() {
        return complemento;
    }
    
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
    
    @Override
    public String toString() {
        return "Persona{" +
                "cedulaPer=" + cedulaPer +
                ", primerNom='" + primerNom + '\'' +
                ", segundoNom='" + segundoNom + '\'' +
                ", primerApell='" + primerApell + '\'' +
                ", segundoApell='" + segundoApell + '\'' +
                ", dirección='" + calle + " " + carrera + " " + numero + '\'' +
                ", complemento='" + complemento + '\'' +
                '}';
    }
}