package model.personas;

import model.cine.Pelicula;
import java.util.ArrayList;
import java.util.List;

public class Director extends Participante {
    private int experienciaAnos;
    private List<Pelicula> peliculasDirigidas;

    public Director(int idParticipante, String nombre, String apellido, String nacionalidad, int experienciaAnos) {
        super(idParticipante, nombre, apellido, nacionalidad);
        this.experienciaAnos = experienciaAnos;
        this.peliculasDirigidas = new ArrayList<>();
    }

    public void agregarPeliculaDirigida(Pelicula p) { this.peliculasDirigidas.add(p); }

    public void dirigir(Pelicula pelicula) {
        System.out.println("[Director: " + getNombre() + " " + getApellido() + "] Rodando: \"" + pelicula.getTitulo() + "\".");
        if (!this.peliculasDirigidas.contains(pelicula)) this.peliculasDirigidas.add(pelicula);
    }

    public int getExperienciaAnos()                         { return this.experienciaAnos; }
    public List<Pelicula> getPeliculasDirigidas()           { return this.peliculasDirigidas; }
    public void setExperienciaAnos(int e)                   { this.experienciaAnos = e; }
    public void setPeliculasDirigidas(List<Pelicula> lista) { this.peliculasDirigidas = lista; }
}
