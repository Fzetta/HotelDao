package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Modelo de la tabla ConsumoAdicional
 * Representa el consumo de servicios por parte de una reserva
 */
public class ConsumoAdicional {
    
    private LocalDate fechaConsumo;
    private LocalTime horaConsumo;
    private LocalDate fechaLlegada;
    private Integer numeroHab;
    private Long cedulaPer;
    private Long idServicio;
    
    // Objetos relacionados para consultas con JOIN
    private Reserva reserva;
    private Servicio servicio;
    
    // Constructor vacío
    public ConsumoAdicional() {
    }
    
    // Constructor completo
    public ConsumoAdicional(LocalDate fechaConsumo, LocalTime horaConsumo, 
                           LocalDate fechaLlegada, Integer numeroHab, 
                           Long cedulaPer, Long idServicio) {
        this.fechaConsumo = fechaConsumo;
        this.horaConsumo = horaConsumo;
        this.fechaLlegada = fechaLlegada;
        this.numeroHab = numeroHab;
        this.cedulaPer = cedulaPer;
        this.idServicio = idServicio;
    }
    
    // Getters y Setters
    public LocalDate getFechaConsumo() {
        return fechaConsumo;
    }
    
    public void setFechaConsumo(LocalDate fechaConsumo) {
        this.fechaConsumo = fechaConsumo;
    }
    
    public LocalTime getHoraConsumo() {
        return horaConsumo;
    }
    
    public void setHoraConsumo(LocalTime horaConsumo) {
        this.horaConsumo = horaConsumo;
    }
    
    public LocalDate getFechaLlegada() {
        return fechaLlegada;
    }
    
    public void setFechaLlegada(LocalDate fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }
    
    public Integer getNumeroHab() {
        return numeroHab;
    }
    
    public void setNumeroHab(Integer numeroHab) {
        this.numeroHab = numeroHab;
    }
    
    public Long getCedulaPer() {
        return cedulaPer;
    }
    
    public void setCedulaPer(Long cedulaPer) {
        this.cedulaPer = cedulaPer;
    }
    
    public Long getIdServicio() {
        return idServicio;
    }
    
    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }
    
    public Reserva getReserva() {
        return reserva;
    }
    
    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }
    
    public Servicio getServicio() {
        return servicio;
    }
    
    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }
    
    @Override
    public String toString() {
        return "ConsumoAdicional{" +
                "fechaConsumo=" + fechaConsumo +
                ", horaConsumo=" + horaConsumo +
                ", fechaLlegada=" + fechaLlegada +
                ", numeroHab=" + numeroHab +
                ", cedulaPer=" + cedulaPer +
                ", idServicio=" + idServicio +
                (servicio != null ? ", servicio='" + servicio.getNomServicio() + '\'' : "") +
                '}';
    }
}