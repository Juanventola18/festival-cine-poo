package model.cine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Edicion {
    private int numeroEdicion;
    private LocalDate fecha;
    private List<Seccion> secciones = new ArrayList<>();

    public Edicion(int numeroEdicion, LocalDate fecha) {
        this.numeroEdicion = numeroEdicion;
        this.fecha = fecha;
    }

    public void agregarSeccion(Seccion s) { this.secciones.add(s); }

    public void determinarGanador() {
        System.out.println("\n=== GANADORES EDICIÓN " + this.numeroEdicion + " ===");
        if (this.secciones.isEmpty()) { System.out.println("No hay secciones."); return; }
        for (Seccion s : this.secciones) s.determinarGanadorDeSeccion();
    }

    public int getNumeroEdicion()                     { return this.numeroEdicion; }
    public void setNumeroEdicion(int n)               { this.numeroEdicion = n; }
    public LocalDate getFecha()                       { return this.fecha; }
    public void setFecha(LocalDate f)                 { this.fecha = f; }
    public List<Seccion> getSecciones()               { return this.secciones; }
    public void setSecciones(List<Seccion> secciones) { this.secciones = secciones; }
    public int getAno()                               { return this.numeroEdicion; }
}
