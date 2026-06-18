package logic;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.cine.*;
import model.membresias.*;
import model.personas.*;

public class GestorArchivos {

    private static final String RUTA = "datos/";
    private static final String LOG  = RUTA + "registro.txt";

    public static void guardarTodo(GestorFestival gestor) {
        crearCarpetaSiNoExiste();
        guardarPeliculas(gestor);
        guardarEspectadores(gestor);
        guardarJurados(gestor);
        guardarFunciones(gestor);
        guardarEdiciones(gestor);
        guardarDirectores(gestor);
        guardarActores(gestor);
        guardarEvaluaciones(gestor);
        log("===== GUARDADO COMPLETO DEL SISTEMA =====");
    }

    public static void cargarTodo(GestorFestival gestor) {
        crearCarpetaSiNoExiste();
        gestor.getPeliculas().clear();
        gestor.getUsuarios().clear();
        gestor.getCalendario().clear();
        gestor.getEdiciones().clear();
        gestor.getParticipantes().clear();
        leerPeliculas(gestor);
        leerEspectadores(gestor);
        leerJurados(gestor);
        leerFunciones(gestor);
        leerEdiciones(gestor);
        leerDirectoresYActores(gestor);
        log("===== CARGA COMPLETA DEL SISTEMA =====");
    }

    private static void crearCarpetaSiNoExiste() {
        File carpeta = new File(RUTA);
        if (!carpeta.exists()) carpeta.mkdirs();
    }

    private static void guardarPeliculas(GestorFestival gestor) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA + "peliculas.txt"))) {
            for (Pelicula p : gestor.getPeliculas()) {
                StringBuilder sb = new StringBuilder();
                sb.append(p.getId()).append(";")
                  .append(p.getTitulo()).append(";")
                  .append(p.getDuracion()).append(";")
                  .append(p.getAnio()).append(";")
                  .append(p.getGenero()).append(";")
                  .append(p.getIdDirector()).append(";");
                StringBuilder actores = new StringBuilder();
                for (int id : p.getIdsActores()) actores.append(id).append(",");
                sb.append(actores).append(";");
                if (!p.getCalificaciones().isEmpty()) {
                    for (Calificacion c : p.getCalificaciones()) {
                        sb.append(c.getPuntaje());
                        if (c.getMencionEspecial() != null && !c.getMencionEspecial().isBlank())
                            sb.append(",").append(c.getMencionEspecial());
                        sb.append("|");
                    }
                }
                pw.println(sb.toString());
            }
            System.out.println("[Guardado] peliculas.txt -> " + gestor.getPeliculas().size() + " película(s).");
        } catch (IOException e) {
            System.err.println("Error al guardar peliculas.txt: " + e.getMessage());
        }
    }

    private static void guardarEspectadores(GestorFestival gestor) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA + "espectadores.txt"))) {
            int count = 0;
            for (Usuario u : gestor.getUsuarios()) {
                if (u instanceof Espectador esp) {
                    pw.println(esp.toArchivo());
                    count++;
                }
            }
            System.out.println("[Guardado] espectadores.txt -> " + count + " espectador(es).");
        } catch (IOException e) {
            System.err.println("Error al guardar espectadores.txt: " + e.getMessage());
        }
    }

    private static void guardarJurados(GestorFestival gestor) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA + "jurados.txt"))) {
            int count = 0;
            for (Usuario u : gestor.getUsuarios()) {
                if (u instanceof Jurado j) {
                    pw.println(j.toArchivo());
                    count++;
                }
            }
            System.out.println("[Guardado] jurados.txt -> " + count + " jurado(s).");
        } catch (IOException e) {
            System.err.println("Error al guardar jurados.txt: " + e.getMessage());
        }
    }

    private static void guardarFunciones(GestorFestival gestor) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA + "funciones.txt"))) {
            for (Funcion f : gestor.getCalendario())
                pw.println(f.toArchivo());
            System.out.println("[Guardado] funciones.txt -> " + gestor.getCalendario().size() + " función(es).");
        } catch (IOException e) {
            System.err.println("Error al guardar funciones.txt: " + e.getMessage());
        }
    }

    private static void guardarEdiciones(GestorFestival gestor) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA + "ediciones.txt"))) {
            for (Edicion e : gestor.getEdiciones()) {
                StringBuilder sb = new StringBuilder();
                sb.append(e.getNumeroEdicion()).append(";").append(e.getFecha());
                for (Seccion s : e.getSecciones())
                    sb.append(";").append(s.getNombreSeccion());
                pw.println(sb.toString());
            }
            System.out.println("[Guardado] ediciones.txt -> " + gestor.getEdiciones().size() + " edición(es).");
        } catch (IOException e) {
            System.err.println("Error al guardar ediciones.txt: " + e.getMessage());
        }
    }

    private static void guardarDirectores(GestorFestival gestor) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA + "directores.txt"))) {
            int count = 0;
            for (Participante p : gestor.getParticipantes()) {
                if (p instanceof Director d) {
                    pw.println(d.getIdParticipante() + ";" + d.getNombre() + ";" + d.getApellido() + ";" +
                               d.getNacionalidad() + ";" + d.getExperienciaAnos());
                    count++;
                }
            }
            System.out.println("[Guardado] directores.txt -> " + count + " director(es).");
        } catch (IOException e) {
            System.err.println("Error al guardar directores.txt: " + e.getMessage());
        }
    }

    private static void guardarActores(GestorFestival gestor) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA + "actores.txt"))) {
            int count = 0;
            for (Participante p : gestor.getParticipantes()) {
                if (p instanceof Actor a) {
                    pw.println(a.getIdParticipante() + ";" + a.getNombre() + ";" + a.getApellido() + ";" +
                               a.getNacionalidad() + ";" + a.getTipoActor() + ";" + a.getCachetBase());
                    count++;
                }
            }
            System.out.println("[Guardado] actores.txt -> " + count + " actor(es).");
        } catch (IOException e) {
            System.err.println("Error al guardar actores.txt: " + e.getMessage());
        }
    }

    private static void guardarEvaluaciones(GestorFestival gestor) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA + "evaluaciones.txt"))) {
            int total = 0;
            for (Pelicula p : gestor.getPeliculas()) {
                if (p.getCalificaciones().isEmpty()) continue;
                pw.println("=== " + p.getTitulo() + " (promedio: " + String.format("%.2f", p.getPromedioPuntaje()) + ") ===");
                for (Calificacion c : p.getCalificaciones()) {
                    String linea = "  Puntaje: " + c.getPuntaje();
                    if (c.getMencionEspecial() != null && !c.getMencionEspecial().isBlank())
                        linea += " | " + c.getMencionEspecial();
                    pw.println(linea);
                    total++;
                }
            }
            System.out.println("[Guardado] evaluaciones.txt -> " + total + " evaluación(es).");
        } catch (IOException e) {
            System.err.println("Error al guardar evaluaciones.txt: " + e.getMessage());
        }
    }

    private static void leerPeliculas(GestorFestival gestor) {
        for (String linea : leerLineas(RUTA + "peliculas.txt")) {
            String[] p = linea.split(";");
            if (p.length < 5) continue;
            try {
                Pelicula peli = new Pelicula(p[1].trim(), Integer.parseInt(p[2].trim()),
                                             Integer.parseInt(p[3].trim()), p[4].trim());
                peli.setId(Integer.parseInt(p[0].trim()));
                if (p.length > 5 && !p[5].isBlank())
                    try { peli.setIdDirector(Integer.parseInt(p[5].trim())); } catch (NumberFormatException ignored) {}
                if (p.length > 6 && !p[6].isBlank())
                    for (String idStr : p[6].split(","))
                        if (!idStr.isBlank())
                            try { peli.agregarIdActor(Integer.parseInt(idStr.trim())); } catch (NumberFormatException ignored) {}
                if (p.length > 7 && !p[7].isBlank()) {
                    for (String cData : p[7].split("\\|")) {
                        if (cData.isBlank()) continue;
                        String[] d = cData.split(",");
                        Calificacion c = new Calificacion(Integer.parseInt(d[0].trim()), peli);
                        if (d.length > 1) c.setMencionEspecial(d[1].trim());
                        peli.agregarCalificacion(c);
                    }
                }
                gestor._addPelicula(peli);
            } catch (Exception e) {
                System.err.println("Error al leer película: " + linea + " -> " + e.getMessage());
            }
        }
        System.out.println("[Cargado] " + gestor.getPeliculas().size() + " película(s).");
    }

    private static void leerEspectadores(GestorFestival gestor) {
        for (String linea : leerLineas(RUTA + "espectadores.txt")) {
            String[] p = linea.split(";");
            if (p.length < 5) continue;
            try {
                Espectador e = new Espectador(
                    Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim()
                );
                if (p.length > 5)
                    try { e.sumarPuntos(Integer.parseInt(p[5].trim())); } catch (NumberFormatException ignored) {}
                if (p.length > 6) {
                    String tipo = p[6].trim();
                    LocalDate hoy = LocalDate.now();
                    if (tipo.equalsIgnoreCase("Estándar") || tipo.equalsIgnoreCase("Estandar"))
                        e.setMembresia(new MembresiaEstandar(hoy, hoy.plusYears(1), "activa"));
                    else if (tipo.equalsIgnoreCase("Platino"))
                        e.setMembresia(new MembresiaPlatino(hoy, hoy.plusYears(1), "activa"));
                }
                gestor._addUsuario(e);
            } catch (Exception e) {
                System.err.println("Error al leer espectador: " + linea + " -> " + e.getMessage());
            }
        }
        System.out.println("[Cargado] " + gestor.getUsuarios().size() + " espectador(es).");
    }

    private static void leerJurados(GestorFestival gestor) {
        for (String linea : leerLineas(RUTA + "jurados.txt")) {
            String[] p = linea.split(";");
            if (p.length < 6) continue;
            try {
                gestor._addUsuario(new Jurado(
                    Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(),
                    p[3].trim(), p[4].trim(), p[5].trim()
                ));
            } catch (Exception e) {
                System.err.println("Error al leer jurado: " + linea + " -> " + e.getMessage());
            }
        }
        System.out.println("[Cargado] " + gestor.getUsuarios().size() + " usuario(s) en total.");
    }

    private static void leerFunciones(GestorFestival gestor) {
        if (gestor.getSalas().isEmpty()) gestor._addSala(new Sala(1, 5, 5));
        for (String linea : leerLineas(RUTA + "funciones.txt")) {
            String[] partes = linea.split(";");
            if (partes.length < 4) continue;
            try {
                String   fechaStr = partes[0].trim();
                String   hora     = partes[1].trim();
                String[] trozos   = partes[3].split("\\|");
                String   titulo   = trozos[0].trim();
                Pelicula pelFound = null;
                for (Pelicula peli : gestor.getPeliculas())
                    if (peli.getTitulo().trim().equalsIgnoreCase(titulo)) { pelFound = peli; break; }
                if (pelFound == null) continue;
                Funcion f = new Funcion(pelFound, gestor.getSalas().get(0), LocalDate.parse(fechaStr), hora);
                for (int i = 1; i < trozos.length; i++) {
                    String[] coords = trozos[i].split(",");
                    if (coords.length == 2)
                        f.reservarButaca(Integer.parseInt(coords[0].trim()), Integer.parseInt(coords[1].trim()), null);
                }
                gestor._addFuncion(f);
            } catch (Exception e) {
                System.err.println("Error al leer función: " + linea + " -> " + e.getMessage());
            }
        }
        System.out.println("[Cargado] " + gestor.getCalendario().size() + " función(es).");
    }

    private static void leerEdiciones(GestorFestival gestor) {
        for (String linea : leerLineas(RUTA + "ediciones.txt")) {
            String[] p = linea.split(";");
            if (p.length < 2) continue;
            try {
                Edicion e = new Edicion(Integer.parseInt(p[0].trim()), LocalDate.parse(p[1].trim()));
                for (int i = 2; i < p.length; i++)
                    if (!p[i].isBlank()) e.agregarSeccion(new Seccion(p[i].trim()));
                gestor._addEdicion(e);
            } catch (Exception e) {
                System.err.println("Error al leer edición: " + linea + " -> " + e.getMessage());
            }
        }
        System.out.println("[Cargado] " + gestor.getEdiciones().size() + " edición(es).");
    }

    private static void leerDirectoresYActores(GestorFestival gestor) {
        for (String linea : leerLineas(RUTA + "directores.txt")) {
            String[] p = linea.split(";");
            if (p.length < 5) continue;
            try {
                gestor._addParticipante(new Director(
                    Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(),
                    p[3].trim(), Integer.parseInt(p[4].trim())
                ));
            } catch (Exception e) {
                System.err.println("Error al leer director: " + linea + " -> " + e.getMessage());
            }
        }
        for (String linea : leerLineas(RUTA + "actores.txt")) {
            String[] p = linea.split(";");
            if (p.length < 6) continue;
            try {
                gestor._addParticipante(new Actor(
                    Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(),
                    p[3].trim(), p[4].trim(), Double.parseDouble(p[5].trim())
                ));
            } catch (Exception e) {
                System.err.println("Error al leer actor: " + linea + " -> " + e.getMessage());
            }
        }
        System.out.println("[Cargado] " + gestor.getParticipantes().size() + " participante(s).");
    }

    private static void log(String mensaje) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG, true))) {
            pw.println("[" + ts + "] " + mensaje);
        } catch (IOException e) {
            System.err.println("Error al escribir en registro.txt: " + e.getMessage());
        }
    }

    private static List<String> leerLineas(String nombre) {
        File file = new File(nombre);
        if (!file.exists()) return List.of();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            return br.lines().filter(l -> !l.isBlank()).toList();
        } catch (Exception e) {
            System.err.println("Error al leer " + nombre + ": " + e.getMessage());
            return List.of();
        }
    }
}
