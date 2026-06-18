package model.cine;

public class Calificacion {
    private int puntaje;
    private Pelicula pelicula;
    private String mencionEspecial;

    public Calificacion(int puntaje, Pelicula pelicula) {
        this.puntaje = puntaje;
        this.pelicula = pelicula;
        this.mencionEspecial = "";
    }

    public int getPuntaje()                        { return this.puntaje; }
    public Pelicula getPelicula()                  { return this.pelicula; }
    public String getMencionEspecial()             { return this.mencionEspecial; }
    public void setMencionEspecial(String mencion) { this.mencionEspecial = mencion; }
    public void setPuntaje(int puntaje)            { this.puntaje = puntaje; }
}