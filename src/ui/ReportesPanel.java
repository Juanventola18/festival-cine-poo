package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import logic.GestorFestival;
import logic.GestorParticipantes;
import model.cine.*;
import model.personas.*;

public class ReportesPanel extends JPanel {

    private final GestorFestival gestor;
    private final JTextArea areaTexto;

    public ReportesPanel(GestorFestival gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // ── Botones ────────────────────────────────────────────────────────────
        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JButton btnRecaudacion = new JButton("💰 Recaudación");
        JButton btnRanking     = new JButton("🏅 Ranking películas");
        JButton btnHistorial   = new JButton("📋 Historial funciones");
        JButton btnResumen     = new JButton("📊 Resumen general");
        JButton btnGanador     = new JButton("🏆 Ganador del Festival");

        btnRecaudacion.addActionListener(e -> mostrarRecaudacion());
        btnRanking.addActionListener(e -> mostrarRanking());
        btnHistorial.addActionListener(e -> mostrarHistorial());
        btnResumen.addActionListener(e -> mostrarResumen());
        btnGanador.addActionListener(e -> mostrarGanador());

        norte.add(btnRecaudacion);
        norte.add(btnRanking);
        norte.add(btnHistorial);
        norte.add(btnResumen);
        norte.add(btnGanador);
        add(norte, BorderLayout.NORTH);

        // ── Área de texto ──────────────────────────────────────────────────────
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaTexto.setMargin(new Insets(8, 8, 8, 8));
        add(new JScrollPane(areaTexto), BorderLayout.CENTER);

        mostrarResumen();
    }

    // ── Recaudación ────────────────────────────────────────────────────────────
    private void mostrarRecaudacion() {
        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════════════════════\n");
        sb.append("          REPORTE DE RECAUDACIÓN              \n");
        sb.append("══════════════════════════════════════════════\n");
        if (gestor.getCalendario().isEmpty()) {
            sb.append("  No hay funciones registradas.\n");
        } else {
            double total = 0;
            for (Funcion f : gestor.getCalendario()) {
                int ent = f.getCantidadOcupados();
                double rec = ent * Funcion.getPrecioBase();
                total += rec;
                sb.append(String.format("  %-30s %s %s\n", f.getPelicula().getTitulo(),
                    "| " + f.getFecha(), "| Entradas: " + ent + " | $" + String.format("%,.0f", rec)));
            }
            sb.append("──────────────────────────────────────────────\n");
            sb.append(String.format("  TOTAL GENERAL: $%,.0f\n", total));
        }
        sb.append("══════════════════════════════════════════════\n");
        areaTexto.setText(sb.toString());
    }

    // ── Ranking ────────────────────────────────────────────────────────────────
    private void mostrarRanking() {
        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════════════════════\n");
        sb.append("           RANKING DE PELÍCULAS               \n");
        sb.append("══════════════════════════════════════════════\n");

        List<Pelicula> lista = new ArrayList<>(gestor.getPeliculas());
        lista.sort((a, b) -> Double.compare(b.getPromedioPuntaje(), a.getPromedioPuntaje()));

        if (lista.isEmpty()) {
            sb.append("  No hay películas registradas.\n");
        } else {
            int pos = 1;
            for (Pelicula p : lista) {
                String prom = p.getCalificaciones().isEmpty() ? "sin calificar" : String.format("%.2f", p.getPromedioPuntaje());
                sb.append(String.format("  %2d. %-25s | %s | Puntaje: %s | %d calificación(es)\n",
                    pos++, p.getTitulo(), p.getGenero() + " " + p.getAnio(), prom, p.getCalificaciones().size()));
                if (p.getIdDirector() != 0) {
                    Director d = GestorParticipantes.buscarDirectorPorId(p.getIdDirector(), gestor);
                    if (d != null) sb.append("      Director: ").append(d.getNombre()).append(" ").append(d.getApellido()).append("\n");
                }
            }
        }
        sb.append("══════════════════════════════════════════════\n");
        areaTexto.setText(sb.toString());
    }

    // ── Historial ─────────────────────────────────────────────────────────────
    private void mostrarHistorial() {
        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════════════════════\n");
        sb.append("           HISTORIAL DE FUNCIONES             \n");
        sb.append("══════════════════════════════════════════════\n");

        List<Funcion> lista = new ArrayList<>(gestor.getCalendario());
        lista.sort(Comparator.comparing(Funcion::getFecha));
        java.time.LocalDate hoy = java.time.LocalDate.now();

        sb.append("  — PASADAS —\n");
        boolean hayPasadas = false;
        for (Funcion f : lista) {
            if (f.getFecha().isBefore(hoy)) {
                sb.append(String.format("  %s %s | %-20s | %d/%d | $%,.0f\n",
                    f.getFecha(), f.getHoraInicio(), f.getPelicula().getTitulo(),
                    f.getCantidadOcupados(), f.getSala().getCapacidadTotal(),
                    f.getCantidadOcupados() * Funcion.getPrecioBase()));
                hayPasadas = true;
            }
        }
        if (!hayPasadas) sb.append("  Ninguna.\n");

        sb.append("\n  — PRÓXIMAS —\n");
        boolean hayProximas = false;
        for (Funcion f : lista) {
            if (!f.getFecha().isBefore(hoy)) {
                sb.append(String.format("  %s %s | %-20s | Disponibles: %d\n",
                    f.getFecha(), f.getHoraInicio(), f.getPelicula().getTitulo(),
                    f.getSala().getCapacidadTotal() - f.getCantidadOcupados()));
                hayProximas = true;
            }
        }
        if (!hayProximas) sb.append("  Ninguna.\n");
        sb.append("══════════════════════════════════════════════\n");
        areaTexto.setText(sb.toString());
    }

    // ── Resumen ────────────────────────────────────────────────────────────────
    private void mostrarResumen() {
        long espectadores = gestor.getUsuarios().stream().filter(u -> u instanceof Espectador).count();
        long jurados = gestor.getUsuarios().stream().filter(u -> u instanceof Jurado).count();
        long directores = gestor.getParticipantes().stream().filter(p -> p instanceof Director).count();
        long actores = gestor.getParticipantes().stream().filter(p -> p instanceof Actor).count();
        double totalRec = gestor.getCalendario().stream().mapToDouble(f -> f.getCantidadOcupados() * Funcion.getPrecioBase()).sum();
        int totalEntradas = gestor.getCalendario().stream().mapToInt(Funcion::getCantidadOcupados).sum();

        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════════════════════\n");
        sb.append("              RESUMEN GENERAL                 \n");
        sb.append("══════════════════════════════════════════════\n");
        sb.append(String.format("  Películas registradas:  %d\n", gestor.getPeliculas().size()));
        sb.append(String.format("  Funciones programadas:  %d\n", gestor.getCalendario().size()));
        sb.append(String.format("  Espectadores:           %d\n", espectadores));
        sb.append(String.format("  Jurados:                %d\n", jurados));
        sb.append(String.format("  Directores:             %d\n", directores));
        sb.append(String.format("  Actores:                %d\n", actores));
        sb.append(String.format("  Ediciones:              %d\n", gestor.getEdiciones().size()));
        sb.append("──────────────────────────────────────────────\n");
        sb.append(String.format("  Entradas vendidas:      %d\n", totalEntradas));
        sb.append(String.format("  Recaudación total:      $%,.0f\n", totalRec));
        sb.append("══════════════════════════════════════════════\n");
        areaTexto.setText(sb.toString());
    }

    // ── Ganador ────────────────────────────────────────────────────────────────
    private void mostrarGanador() {
        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════════════════════\n");
        sb.append("         GANADOR DEL FESTIVAL                 \n");
        sb.append("══════════════════════════════════════════════\n");

        Pelicula ganadora = null;
        double mejor = -1;
        for (Pelicula p : gestor.getPeliculas()) {
            if (!p.getCalificaciones().isEmpty() && p.getPromedioPuntaje() > mejor) {
                mejor = p.getPromedioPuntaje();
                ganadora = p;
            }
        }
        if (ganadora == null) {
            sb.append("  Aún no hay películas calificadas.\n");
        } else {
            sb.append("  🏆 PELÍCULA GANADORA: ").append(ganadora.getTitulo()).append("\n");
            sb.append(String.format("  ⭐ Promedio final:    %.2f\n", mejor));
            sb.append("  Calificaciones:       ").append(ganadora.getCalificaciones().size()).append("\n");
            if (ganadora.getIdDirector() != 0) {
                Director d = GestorParticipantes.buscarDirectorPorId(ganadora.getIdDirector(), gestor);
                if (d != null) sb.append("  Director:             ").append(d.getNombre()).append(" ").append(d.getApellido()).append("\n");
            }
        }
        sb.append("══════════════════════════════════════════════\n");
        areaTexto.setText(sb.toString());
    }
}
