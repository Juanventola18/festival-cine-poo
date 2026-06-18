package model.cine;

import java.util.ArrayList;
import java.util.List;

public class Seccion {
    private String nombreSeccion;
    private List<Pelicula> peliculas;

    public Seccion(String nombreSeccion) {
        this.nombreSeccion = nombreSeccion;
        this.peliculas = new ArrayList<>();
    }

    public void agregarPelicula(Pelicula p) { this.peliculas.add(p); }

    public void determinarGanadorDeSeccion() {
        System.out.println("[Sección: " + this.nombreSeccion + "] Evaluando promedios...");
        if (this.peliculas.isEmpty()) {
            System.out.println("  --> No hay películas cargadas en esta sección.");
            return;
        }
        Pelicula ganadora = this.peliculas.get(0);
        for (Pelicula p : this.peliculas)
            if (p.getPuntajeFinal() > ganadora.getPuntajeFinal()) ganadora = p;
        if (ganadora.getPuntajeFinal() == 0.0)
            System.out.println("  --> Ninguna película recibió calificaciones todavía.");
        else
            System.out.println("  --> 🏆 GANADORA: \"" + ganadora.getTitulo() + "\" con " + ganadora.getPuntajeFinal() + " pts.");
    }

    public List<Pelicula> getPeliculas() { return this.peliculas; }
    public String getNombreSeccion()     { return this.nombreSeccion; }
}
