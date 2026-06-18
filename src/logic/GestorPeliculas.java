package logic;

import model.cine.*;
import model.personas.Jurado;

public class GestorPeliculas {

    public static void registrar(Pelicula p, GestorFestival gestor) throws Exception {
        Validador.validarTituloUnico(p.getTitulo(), gestor);
        Validador.validarDuracion(p.getDuracion());
        Validador.validarAnio(p.getAnio());
        p.setId(gestor.getPeliculas().size() + 1);
        gestor.getPeliculas().add(p);
        GestorArchivos.guardarTodo(gestor);
    }

    public static void registrarCalificacion(Pelicula p, int puntaje, Jurado jurado, GestorFestival gestor) throws Exception {
        Validador.validarPuntaje(puntaje);
        for (Calificacion c : p.getCalificaciones())
            if (c.getMencionEspecial() != null && c.getMencionEspecial().contains("ID:" + jurado.getId()))
                throw new Exception("El jurado " + jurado.getNombre() + " ya calificó esta película.");
        Calificacion c = new Calificacion(puntaje, p);
        c.setMencionEspecial("Jurado: " + jurado.getNombre() + " " + jurado.getApellido() + " ID:" + jurado.getId());
        p.agregarCalificacion(c);
        GestorArchivos.guardarTodo(gestor);
    }

    public static Pelicula buscarPorTitulo(String titulo, GestorFestival gestor) {
        for (Pelicula p : gestor.getPeliculas())
            if (p.getTitulo().equalsIgnoreCase(titulo)) return p;
        return null;
    }

    public static void determinarGanador(GestorFestival gestor) {
        System.out.println("\n--- RESULTADOS DEL FESTIVAL ---");
        Pelicula ganadora = null;
        double mejor = -1.0;
        boolean hayCalificaciones = false;
        for (Pelicula p : gestor.getPeliculas()) {
            if (!p.getCalificaciones().isEmpty()) {
                hayCalificaciones = true;
                double prom = p.getPromedioPuntaje();
                if (prom > mejor) { mejor = prom; ganadora = p; }
            }
        }
        if (!hayCalificaciones)
            System.out.println("Aún no hay películas calificadas.");
        else if (ganadora != null) {
            System.out.println("🏆 PELÍCULA GANADORA: " + ganadora.getTitulo());
            System.out.println("⭐ Promedio final: " + String.format("%.2f", mejor));
        }
    }
}