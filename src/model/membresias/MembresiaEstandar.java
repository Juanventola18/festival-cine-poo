package model.membresias;

import java.time.LocalDate;

public class MembresiaEstandar extends Membresia {
    public MembresiaEstandar(LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        super(fechaInicio, fechaFin, estado);
    }

    @Override public double calcularDescuento(double precioBase) { return precioBase * 0.90; }
    @Override public String getTipo() { return "Estándar"; }
}