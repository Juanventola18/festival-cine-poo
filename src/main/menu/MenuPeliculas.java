package main.menu;

import logic.*;
import model.cine.*;
import model.personas.*;

public class MenuPeliculas {

    public static void registrarPelicula(GestorFestival gestor) throws Exception {
        String titulo = Consola.leer("Título");
        int duracion  = Consola.leerInt("Duración (min)");
        int anio      = Consola.leerInt("Año");
        String genero = Consola.leer("Género");
        Pelicula p    = new Pelicula(titulo, duracion, anio, genero);

        boolean hayDirectores = false;
        for (Participante part : gestor.getParticipantes())
            if (part instanceof Director) { hayDirectores = true; break; }

        if (hayDirectores) {
            System.out.println("\n— Directores disponibles —");
            for (Participante part : gestor.getParticipantes())
                if (part instanceof Director d)
                    System.out.println("  ID: " + d.getIdParticipante() + " | " + d.getNombre() + " " + d.getApellido());
            String inputDir = Consola.leer("ID del director (Enter para omitir)");
            if (!inputDir.isBlank()) {
                try {
                    int idDir = Integer.parseInt(inputDir.trim());
                    Director dir = GestorParticipantes.buscarDirectorPorId(idDir, gestor);
                    if (dir != null) {
                        p.setIdDirector(dir.getIdParticipante());
                        System.out.println("Director asignado: " + dir.getNombre() + " " + dir.getApellido());
                    } else System.out.println("Director no encontrado, se omite.");
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido, se omite.");
                }
            }
        } else {
            System.out.println("No hay directores registrados.");
        }

        boolean hayActores = false;
        for (Participante part : gestor.getParticipantes())
            if (part instanceof Actor) { hayActores = true; break; }

        if (hayActores) {
            System.out.println("\n— Actores disponibles —");
            for (Participante part : gestor.getParticipantes())
                if (part instanceof Actor a)
                    System.out.println("  ID: " + a.getIdParticipante() + " | " + a.getNombre() + " " + a.getApellido() + " | " + a.getTipoActor());
            System.out.println("IDs de actores separados por coma (Enter para omitir):");
            String inputActores = Consola.leer("IDs");
            if (!inputActores.isBlank()) {
                for (String idStr : inputActores.split(",")) {
                    try {
                        int idAct = Integer.parseInt(idStr.trim());
                        Actor actor = GestorParticipantes.buscarActorPorId(idAct, gestor);
                        if (actor != null) {
                            p.agregarIdActor(actor.getIdParticipante());
                            System.out.println("Actor agregado: " + actor.getNombre() + " " + actor.getApellido());
                        } else System.out.println("Actor ID " + idAct + " no encontrado.");
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido: " + idStr);
                    }
                }
            }
        } else {
            System.out.println("No hay actores registrados.");
        }

        GestorPeliculas.registrar(p, gestor);
        System.out.println("Película registrada con éxito.");
    }

    public static void registrarCalificacion(GestorFestival gestor) throws Exception {
        if (gestor.getPeliculas().isEmpty()) {
            System.out.println("Error: no hay películas registradas.");
            return;
        }
        System.out.println("\n— Películas disponibles —");
        Consola.listar(gestor.getPeliculas(),
            p -> p.getId() + ". " + p.getTitulo() + " (" + p.getGenero() + ", " + p.getAnio() + ")" +
                 (p.getCalificaciones().isEmpty() ? "" : " | Promedio: " + String.format("%.2f", p.getPromedioPuntaje())));

        String titulo = Consola.leer("Título de la película a calificar");
        Pelicula p = GestorPeliculas.buscarPorTitulo(titulo, gestor);
        if (p == null) { System.out.println("Error: película no encontrada."); return; }

        System.out.println("\n— Jurados registrados —");
        boolean hayJurados = false;
        for (Usuario u : gestor.getUsuarios()) {
            if (u instanceof Jurado j) {
                System.out.println("  ID: " + j.getId() + " | " + j.getNombre() + " " + j.getApellido() + " | Especialidad: " + j.getEspecialidad());
                hayJurados = true;
            }
        }
        if (!hayJurados) { System.out.println("Error: no hay jurados registrados."); return; }

        int idJurado = Consola.leerInt("ID del jurado");
        Jurado jurado = GestorUsuarios.buscarJuradoPorId(idJurado, gestor);
        if (jurado == null) { System.out.println("Error: jurado no encontrado."); return; }

        int puntaje = Consola.leerInt("Puntaje (1-10)");
        GestorPeliculas.registrarCalificacion(p, puntaje, jurado, gestor);
        System.out.println("Calificación registrada. Promedio actual: " + String.format("%.2f", p.getPromedioPuntaje()));
    }

    public static void consultarPromedio(GestorFestival gestor) {
        if (gestor.getPeliculas().isEmpty()) {
            System.out.println("No hay películas registradas.");
            return;
        }
        Consola.listar(gestor.getPeliculas(),
            p -> p.getTitulo() + " | Promedio: " + String.format("%.2f", p.getPromedioPuntaje()));
        Pelicula p = GestorPeliculas.buscarPorTitulo(Consola.leer("Título"), gestor);
        if (p == null) { System.out.println("Error: película no encontrada."); return; }
        System.out.println("Promedio de \"" + p.getTitulo() + "\": " + String.format("%.2f", p.getPromedioPuntaje()));
    }

    public static void determinarGanadores(GestorFestival gestor) {
        GestorPeliculas.determinarGanador(gestor);
    }
}
