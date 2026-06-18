package model.cine;

import java.util.ArrayList;
import java.util.List;

public class Pelicula {
    private int id;
    private String titulo;
    private int duracion;
    private int anio;
    private String genero;
    private List<Calificacion> calificaciones;
    private int idDirector;
    private List<Integer> idsActores;

    public Pelicula(String titulo, int duracion, int anio, String genero) {
        this.titulo = titulo;
        this.duracion = duracion;
        this.anio = anio;
        this.genero = genero;
        this.calificaciones = new ArrayList<>();
        this.idsActores = new ArrayList<>();
        this.idDirector = 0;
    }

    public String toArchivo() {
        StringBuilder actIds = new StringBuilder();
        for (int idAct : idsActores) actIds.append(idAct).append(",");
        return id + ";" + titulo + ";" + duracion + ";" + anio + ";" + genero + ";" + idDirector + ";" + actIds.toString();
    }

    public double getPuntajeFinal()                 { return getPromedioPuntaje(); }
    public void setId(int id)                       { this.id = id; }
    public int getId()                              { return id; }
    public String getTitulo()                       { return titulo; }
    public int getDuracion()                        { return duracion; }
    public int getAnio()                            { return anio; }
    public String getGenero()                       { return genero; }
    public int getIdDirector()                      { return idDirector; }
    public void setIdDirector(int idDirector)       { this.idDirector = idDirector; }
    public List<Integer> getIdsActores()            { return idsActores; }
    public void agregarIdActor(int id)              { this.idsActores.add(id); }
    public List<Calificacion> getCalificaciones()   { return calificaciones; }
    public void agregarCalificacion(Calificacion c) { this.calificaciones.add(c); }

    public double getPromedioPuntaje() {
        if (calificaciones.isEmpty()) return 0.0;
        double suma = 0;
        for (Calificacion c : calificaciones) suma += c.getPuntaje();
        return suma / calificaciones.size();
    }
}