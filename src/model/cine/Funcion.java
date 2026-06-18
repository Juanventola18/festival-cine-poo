package model.cine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.personas.Usuario;

public class Funcion {
    private static int contadorIds = 1;
    private static final double PRECIO_BASE = 5000.0;
    private int id;
    private LocalDate fecha;
    private String horaInicio;
    private Sala sala;
    private Pelicula pelicula;
    private List<Usuario> asistentes;
    private boolean[][] butacasOcupadas;

    public Funcion(Pelicula pelicula, Sala sala, LocalDate fecha, String horaInicio) {
        this.id = contadorIds++;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.asistentes = new ArrayList<>();
        this.butacasOcupadas = new boolean[sala.getFilas()][sala.getColumnas()];
    }

    public static double getPrecioBase() { return PRECIO_BASE; }

    public String toArchivo() {
        String base = fecha.toString() + ";" + horaInicio + ";" + sala.getNumeroSala() + ";" + pelicula.getTitulo();
        StringBuilder ocupadas = new StringBuilder();
        for (int i = 0; i < butacasOcupadas.length; i++)
            for (int j = 0; j < butacasOcupadas[i].length; j++)
                if (butacasOcupadas[i][j])
                    ocupadas.append("|").append(i).append(",").append(j);
        return base + ocupadas.toString();
    }

    public boolean reservarButaca(int fila, int col, Usuario usuario) {
        if (fila < 0 || fila >= sala.getFilas() || col < 0 || col >= sala.getColumnas()) return false;
        if (butacasOcupadas[fila][col]) return false;
        butacasOcupadas[fila][col] = true;
        if (usuario != null) this.asistentes.add(usuario);
        return true;
    }

    public int getCantidadOcupados() {
        int count = 0;
        for (boolean[] fila : butacasOcupadas)
            for (boolean b : fila)
                if (b) count++;
        return count;
    }

    public int getId()            { return id; }
    public Pelicula getPelicula() { return pelicula; }
    public Sala getSala()         { return sala; }
    public LocalDate getFecha()   { return fecha; }
    public String getHoraInicio() { return horaInicio; }
}
