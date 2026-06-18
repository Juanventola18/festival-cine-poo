package main.menu;

import logic.GestorFestival;
import logic.GestorParticipantes;
import model.cine.Funcion;
import model.cine.Pelicula;
import model.personas.Actor;
import model.personas.Director;
import model.personas.Espectador;
import model.personas.Jurado;
import model.personas.Participante;
import model.personas.Usuario;

public class MenuReportes {

    public static void reporteRecaudacion(GestorFestival gestor) {
        if (gestor.getCalendario().isEmpty()) {
            System.out.println("No hay funciones programadas.");
            return;
        }
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("              REPORTE DE RECAUDACIÓN           ");
        System.out.println("══════════════════════════════════════════════");
        double totalGeneral = 0;
        for (Funcion f : gestor.getCalendario()) {
            int entradas = f.getCantidadOcupados();
            double recaudacion = entradas * Funcion.getPrecioBase();
            totalGeneral += recaudacion;
            System.out.println("  " + f.getPelicula().getTitulo() +
                               " (" + f.getFecha() + " " + f.getHoraInicio() + ")" +
                               " | Entradas: " + entradas +
                               " | Recaudación: $" + String.format("%,.0f", recaudacion));
        }
        System.out.println("──────────────────────────────────────────────");
        System.out.println("  TOTAL GENERAL: $" + String.format("%,.0f", totalGeneral));
        System.out.println("══════════════════════════════════════════════");
    }

    public static void rankingPeliculas(GestorFestival gestor) {
        if (gestor.getPeliculas().isEmpty()) {
            System.out.println("No hay películas registradas.");
            return;
        }
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("            RANKING DE PELÍCULAS               ");
        System.out.println("══════════════════════════════════════════════");

        java.util.List<Pelicula> lista = new java.util.ArrayList<>(gestor.getPeliculas());
        for (int i = 0; i < lista.size() - 1; i++)
            for (int j = 0; j < lista.size() - 1 - i; j++)
                if (lista.get(j).getPromedioPuntaje() < lista.get(j + 1).getPromedioPuntaje()) {
                    Pelicula temp = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, temp);
                }

        int posicion = 1;
        for (Pelicula p : lista) {
            String promedio = p.getCalificaciones().isEmpty()
                ? "sin calificar"
                : String.format("%.2f", p.getPromedioPuntaje());
            System.out.println("  " + posicion + ". " + p.getTitulo() +
                               " (" + p.getGenero() + ", " + p.getAnio() + ")" +
                               " | Promedio: " + promedio +
                               " | Calificaciones: " + p.getCalificaciones().size());
            if (p.getIdDirector() != 0) {
                Director d = GestorParticipantes.buscarDirectorPorId(p.getIdDirector(), gestor);
                if (d != null) System.out.println("     Director: " + d.getNombre() + " " + d.getApellido());
            }
            posicion++;
        }
        System.out.println("══════════════════════════════════════════════");
    }

    public static void historialFunciones(GestorFestival gestor) {
        if (gestor.getCalendario().isEmpty()) {
            System.out.println("No hay funciones registradas.");
            return;
        }
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("            HISTORIAL DE FUNCIONES             ");
        System.out.println("══════════════════════════════════════════════");

        java.util.List<Funcion> lista = new java.util.ArrayList<>(gestor.getCalendario());
        lista.sort((a, b) -> a.getFecha().compareTo(b.getFecha()));

        java.time.LocalDate hoy = java.time.LocalDate.now();
        System.out.println("  — PASADAS —");
        boolean hayPasadas = false;
        for (Funcion f : lista) {
            if (f.getFecha().isBefore(hoy)) {
                System.out.println("  " + f.getFecha() + " " + f.getHoraInicio() +
                                   " | " + f.getPelicula().getTitulo() +
                                   " | Ocupación: " + f.getCantidadOcupados() + "/" + f.getSala().getCapacidadTotal() +
                                   " | Recaudación: $" + String.format("%,.0f", f.getCantidadOcupados() * Funcion.getPrecioBase()));
                hayPasadas = true;
            }
        }
        if (!hayPasadas) System.out.println("  Ninguna.");

        System.out.println("  — PRÓXIMAS —");
        boolean hayProximas = false;
        for (Funcion f : lista) {
            if (!f.getFecha().isBefore(hoy)) {
                System.out.println("  " + f.getFecha() + " " + f.getHoraInicio() +
                                   " | " + f.getPelicula().getTitulo() +
                                   " | Butacas disponibles: " + (f.getSala().getCapacidadTotal() - f.getCantidadOcupados()));
                hayProximas = true;
            }
        }
        if (!hayProximas) System.out.println("  Ninguna.");
        System.out.println("══════════════════════════════════════════════");
    }

    public static void resumenGeneral(GestorFestival gestor) {
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("              RESUMEN GENERAL                  ");
        System.out.println("══════════════════════════════════════════════");
        System.out.println("  Películas registradas:  " + gestor.getPeliculas().size());
        System.out.println("  Funciones programadas:  " + gestor.getCalendario().size());

        int espectadores = 0, jurados = 0;
        for (Usuario u : gestor.getUsuarios()) {
            if (u instanceof Espectador) espectadores++;
            if (u instanceof Jurado) jurados++;
        }
        System.out.println("  Espectadores:           " + espectadores);
        System.out.println("  Jurados:                " + jurados);

        int directores = 0, actores = 0;
        for (Participante p : gestor.getParticipantes()) {
            if (p instanceof Director) directores++;
            if (p instanceof Actor) actores++;
        }
        System.out.println("  Directores:             " + directores);
        System.out.println("  Actores:                " + actores);

        double totalRecaudado = 0;
        int totalEntradas = 0;
        for (Funcion f : gestor.getCalendario()) {
            totalEntradas += f.getCantidadOcupados();
            totalRecaudado += f.getCantidadOcupados() * Funcion.getPrecioBase();
        }
        System.out.println("  Entradas vendidas:      " + totalEntradas);
        System.out.println("  Recaudación total:      $" + String.format("%,.0f", totalRecaudado));
        System.out.println("══════════════════════════════════════════════");
    }
}