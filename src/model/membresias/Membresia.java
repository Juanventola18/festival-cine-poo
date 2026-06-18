package model.membresias;

import java.time.LocalDate;

public abstract class Membresia {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;

    public Membresia(LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public abstract double calcularDescuento(double precioBase);
    public abstract String getTipo();

    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin()    { return fechaFin; }
    public String getEstado()         { return estado; }
}
