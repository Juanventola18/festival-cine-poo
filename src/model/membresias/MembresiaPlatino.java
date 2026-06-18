package model.membresias;

import java.time.LocalDate;

public class MembresiaPlatino extends Membresia {
    public MembresiaPlatino(LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        super(fechaInicio, fechaFin, estado);
    }

    @Override public double calcularDescuento(double precioBase) { return precioBase * 0.75; }
    @Override public String getTipo() { return "Platino"; }
}
