package main.menu;

import logic.GestorFestival;
import logic.GestorParticipantes;
import model.cine.*;
import model.personas.*;

public class MenuListados {

    public static void listarPeliculas(GestorFestival gestor) {
        if (gestor.getPeliculas().isEmpty()) {
            System.out.println("No hay películas registradas.");
            return;
        }
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("                  PELÍCULAS                   ");
        System.out.println("══════════════════════════════════════════════");
        for (Pelicula p : gestor.getPeliculas()) {
            System.out.println("  ID: " + p.getId() + " | " + p.getTitulo() +
                               " | " + p.getGenero() + " | " + p.getAnio() +
                               " | " + p.getDuracion() + " min" +
                               " | Promedio: " + (p.getCalificaciones().isEmpty()
                                   ? "sin calificar"
                                   : String.format("%.2f", p.getPromedioPuntaje())));
            if (p.getIdDirector() != 0) {
                Director d = GestorParticipantes.buscarDirectorPorId(p.getIdDirector(), gestor);
                if (d != null) System.out.println("     Director: " + d.getNombre() + " " + d.getApellido());
            }
            if (!p.getIdsActores().isEmpty()) {
                System.out.print("     Actores: ");
                for (int i = 0; i < p.getIdsActores().size(); i++) {
                    Actor a = GestorParticipantes.buscarActorPorId(p.getIdsActores().get(i), gestor);
                    if (a != null) {
                        System.out.print(a.getNombre() + " " + a.getApellido() + " (" + a.getTipoActor() + ")");
                        if (i < p.getIdsActores().size() - 1) System.out.print(", ");
                    }
                }
                System.out.println();
            }
        }
        System.out.println("══════════════════════════════════════════════");
    }

    public static void listarEspectadores(GestorFestival gestor) {
        boolean hay = false;
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("                 ESPECTADORES                 ");
        System.out.println("══════════════════════════════════════════════");
        for (Usuario u : gestor.getUsuarios()) {
            if (u instanceof Espectador e) {
                System.out.println("  ID: " + e.getId() +
                                   " | " + e.getNombre() + " " + e.getApellido() +
                                   " | DNI: " + e.getDni() +
                                   " | Email: " + e.getEmail() +
                                   " | Membresía: " + (e.getMembresia() != null ? e.getMembresia().getTipo() : "Ninguna") +
                                   " | Puntos: " + e.getPuntosAcumulados());
                hay = true;
            }
        }
        if (!hay) System.out.println("  No hay espectadores registrados.");
        System.out.println("══════════════════════════════════════════════");
    }

    public static void listarJurados(GestorFestival gestor) {
        boolean hay = false;
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("                   JURADOS                    ");
        System.out.println("══════════════════════════════════════════════");
        for (Usuario u : gestor.getUsuarios()) {
            if (u instanceof Jurado j) {
                System.out.println("  ID: " + j.getId() +
                                   " | " + j.getNombre() + " " + j.getApellido() +
                                   " | DNI: " + j.getDni() +
                                   " | Email: " + j.getEmail() +
                                   " | Especialidad: " + j.getEspecialidad());
                hay = true;
            }
        }
        if (!hay) System.out.println("  No hay jurados registrados.");
        System.out.println("══════════════════════════════════════════════");
    }

    public static void listarDirectores(GestorFestival gestor) {
        boolean hay = false;
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("                  DIRECTORES                  ");
        System.out.println("══════════════════════════════════════════════");
        for (Participante p : gestor.getParticipantes()) {
            if (p instanceof Director d) {
                System.out.println("  ID: " + d.getIdParticipante() +
                                   " | " + d.getNombre() + " " + d.getApellido() +
                                   " | Nacionalidad: " + d.getNacionalidad() +
                                   " | Experiencia: " + d.getExperienciaAnos() + " años");
                hay = true;
            }
        }
        if (!hay) System.out.println("  No hay directores registrados.");
        System.out.println("══════════════════════════════════════════════");
    }

    public static void listarActores(GestorFestival gestor) {
        boolean hay = false;
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("                    ACTORES                   ");
        System.out.println("══════════════════════════════════════════════");
        for (Participante p : gestor.getParticipantes()) {
            if (p instanceof Actor a) {
                System.out.println("  ID: " + a.getIdParticipante() +
                                   " | " + a.getNombre() + " " + a.getApellido() +
                                   " | Nacionalidad: " + a.getNacionalidad() +
                                   " | Tipo: " + a.getTipoActor() +
                                   " | Cachet: $" + String.format("%,.0f", a.getCachetBase()));
                hay = true;
            }
        }
        if (!hay) System.out.println("  No hay actores registrados.");
        System.out.println("══════════════════════════════════════════════");
    }

    public static void listarFunciones(GestorFestival gestor) {
        if (gestor.getCalendario().isEmpty()) {
            System.out.println("No hay funciones programadas.");
            return;
        }
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("                   FUNCIONES                  ");
        System.out.println("══════════════════════════════════════════════");
        for (Funcion f : gestor.getCalendario()) {
            System.out.println("  " + f.getPelicula().getTitulo() +
                               " | Fecha: " + f.getFecha() +
                               " | Hora: " + f.getHoraInicio() +
                               " | Sala: " + f.getSala().getNumeroSala() +
                               " | Ocupación: " + f.getCantidadOcupados() +
                               "/" + f.getSala().getCapacidadTotal());
        }
        System.out.println("══════════════════════════════════════════════");
    }

    public static void listarTodo(GestorFestival gestor) {
        listarPeliculas(gestor);
        listarDirectores(gestor);
        listarActores(gestor);
        listarJurados(gestor);
        listarEspectadores(gestor);
        listarFunciones(gestor);
    }
}
