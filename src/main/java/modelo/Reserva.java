package modelo;

import java.time.LocalDate;

/**
 * Modelo de la tabla Reserva
 */
public class Reserva {
    
    private Long cedulaPer;
    private Integer numeroHab;
    private LocalDate fechaLlegada;
    private LocalDate fechaSalida;
    private Integer tiempoMaxCancel;
    
    // Referencias a objetos completos (opcional para consultas con JOINs)
    private Cliente cliente;
    private Habitacion habitacion;
    
    // Constructor vacío
    public Reserva() {
    }
    
    // Constructor completo
    public Reserva(Long cedulaPer, Integer numeroHab, LocalDate fechaLlegada, 
                   LocalDate fechaSalida, Integer tiempoMaxCancel) {
        this.cedulaPer = cedulaPer;
        this.numeroHab = numeroHab;
        this.fechaLlegada = fechaLlegada;
        this.fechaSalida = fechaSalida;
        this.tiempoMaxCancel = tiempoMaxCancel;
    }
    
    // Getters y Setters
    public Long getCedulaPer() {
        return cedulaPer;
    }
    
    public void setCedulaPer(Long cedulaPer) {
        this.cedulaPer = cedulaPer;
    }
    
    public Integer getNumeroHab() {
        return numeroHab;
    }
    
    public void setNumeroHab(Integer numeroHab) {
        this.numeroHab = numeroHab;
    }
    
    public LocalDate getFechaLlegada() {
        return fechaLlegada;
    }
    
    public void setFechaLlegada(LocalDate fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }
    
    public LocalDate getFechaSalida() {
        return fechaSalida;
    }
    
    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
    
    public Integer getTiempoMaxCancel() {
        return tiempoMaxCancel;
    }
    
    public void setTiempoMaxCancel(Integer tiempoMaxCancel) {
        this.tiempoMaxCancel = tiempoMaxCancel;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Habitacion getHabitacion() {
        return habitacion;
    }
    
    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }
    
    @Override
    public String toString() {
        return "Reserva{" +
                "cedulaPer=" + cedulaPer +
                ", numeroHab=" + numeroHab +
                ", fechaLlegada=" + fechaLlegada +
                ", fechaSalida=" + fechaSalida +
                ", tiempoMaxCancel=" + tiempoMaxCancel +
                '}';
    }
}