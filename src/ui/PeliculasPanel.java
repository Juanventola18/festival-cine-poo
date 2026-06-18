package ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import logic.*;
import model.cine.*;
import model.personas.*;

public class PeliculasPanel extends JPanel {

    private final GestorFestival gestor;
    private final MainFrame frame;

    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final JTextField campoBusqueda;

    public PeliculasPanel(GestorFestival gestor, MainFrame frame) {
        this.gestor = gestor;
        this.frame = frame;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // ── Toolbar del panel ──────────────────────────────────────────────────
        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JButton btnNueva = new JButton("+ Nueva película");
        JButton btnCalificar = new JButton("⭐ Calificar");
        JButton btnGanador = new JButton("🏆 Determinar ganador");
        JButton btnRefrescar = new JButton("🔄 Refrescar");
        campoBusqueda = new JTextField(16);
        campoBusqueda.setToolTipText("Buscar por título o género");

        btnNueva.addActionListener(e -> abrirFormularioPelicula());
        btnCalificar.addActionListener(e -> abrirFormularioCalificacion());
        btnGanador.addActionListener(e -> mostrarGanador());
        btnRefrescar.addActionListener(e -> refrescarTabla());
        campoBusqueda.addActionListener(e -> filtrarTabla());
        campoBusqueda.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filtrarTabla(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filtrarTabla(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarTabla(); }
        });

        norte.add(btnNueva);
        norte.add(btnCalificar);
        norte.add(btnGanador);
        norte.add(btnRefrescar);
        norte.add(new JLabel("  Buscar:"));
        norte.add(campoBusqueda);
        add(norte, BorderLayout.NORTH);

        // ── Tabla ──────────────────────────────────────────────────────────────
        String[] columnas = {"ID", "Título", "Género", "Año", "Duración (min)", "Director", "Promedio", "Calificaciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(22);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);
        tabla.getColumnModel().getColumn(4).setMaxWidth(100);
        tabla.getColumnModel().getColumn(6).setMaxWidth(80);
        tabla.getColumnModel().getColumn(7).setMaxWidth(100);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        refrescarTabla();
    }

    // ── Refrescar tabla ────────────────────────────────────────────────────────
    public void refrescarTabla() {
        modeloTabla.setRowCount(0);
        for (Pelicula p : gestor.getPeliculas()) {
            String director = "";
            if (p.getIdDirector() != 0) {
                Director d = GestorParticipantes.buscarDirectorPorId(p.getIdDirector(), gestor);
                if (d != null) director = d.getNombre() + " " + d.getApellido();
            }
            String prom = p.getCalificaciones().isEmpty()
                ? "—"
                : String.format("%.2f", p.getPromedioPuntaje());
            modeloTabla.addRow(new Object[]{
                p.getId(), p.getTitulo(), p.getGenero(), p.getAnio(),
                p.getDuracion(), director, prom, p.getCalificaciones().size()
            });
        }
        frame.actualizarStatus("Películas: " + gestor.getPeliculas().size());
    }

    // ── Filtrar tabla ──────────────────────────────────────────────────────────
    private void filtrarTabla() {
        String texto = campoBusqueda.getText().toLowerCase().trim();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1, 2));
        }
    }

    // ── Formulario nueva película ──────────────────────────────────────────────
    private void abrirFormularioPelicula() {
        JDialog dialog = new JDialog(frame, "Nueva Película", true);
        dialog.setSize(420, 360);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JTextField fTitulo   = new JTextField();
        JTextField fDuracion = new JTextField();
        JTextField fAnio     = new JTextField();
        JTextField fGenero   = new JTextField();

        form.add(new JLabel("Título:")); form.add(fTitulo);
        form.add(new JLabel("Duración (min):")); form.add(fDuracion);
        form.add(new JLabel("Año:")); form.add(fAnio);
        form.add(new JLabel("Género:")); form.add(fGenero);

        // Director combo
        JComboBox<String> cbDirector = new JComboBox<>();
        cbDirector.addItem("(Sin director)");
        for (Participante p : gestor.getParticipantes())
            if (p instanceof Director d)
                cbDirector.addItem(d.getIdParticipante() + " - " + d.getNombre() + " " + d.getApellido());
        form.add(new JLabel("Director:")); form.add(cbDirector);

        dialog.add(form, BorderLayout.CENTER);

        JPanel sur = new JPanel();
        JButton btnOk = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");
        sur.add(btnOk); sur.add(btnCancelar);
        dialog.add(sur, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dialog.dispose());
        btnOk.addActionListener(e -> {
            try {
                String titulo = fTitulo.getText().trim();
                int dur  = Integer.parseInt(fDuracion.getText().trim());
                int anio = Integer.parseInt(fAnio.getText().trim());
                String genero = fGenero.getText().trim();
                Pelicula p = new Pelicula(titulo, dur, anio, genero);

                int selDir = cbDirector.getSelectedIndex();
                if (selDir > 0) {
                    String[] partes = cbDirector.getSelectedItem().toString().split(" - ");
                    p.setIdDirector(Integer.parseInt(partes[0].trim()));
                }
                GestorPeliculas.registrar(p, gestor);
                refrescarTabla();
                frame.actualizarStatus("Película \"" + titulo + "\" registrada.");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Duración y Año deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    // ── Formulario calificación ────────────────────────────────────────────────
    private void abrirFormularioCalificacion() {
        if (gestor.getPeliculas().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay películas registradas.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean hayJurados = gestor.getUsuarios().stream().anyMatch(u -> u instanceof Jurado);
        if (!hayJurados) {
            JOptionPane.showMessageDialog(frame, "No hay jurados registrados.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(frame, "Registrar Calificación", true);
        dialog.setSize(380, 220);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JComboBox<String> cbPelicula = new JComboBox<>();
        for (Pelicula p : gestor.getPeliculas())
            cbPelicula.addItem(p.getId() + " - " + p.getTitulo());

        JComboBox<String> cbJurado = new JComboBox<>();
        for (model.personas.Usuario u : gestor.getUsuarios())
            if (u instanceof Jurado j)
                cbJurado.addItem(j.getId() + " - " + j.getNombre() + " " + j.getApellido());

        SpinnerNumberModel spinModel = new SpinnerNumberModel(5, 1, 10, 1);
        JSpinner spinPuntaje = new JSpinner(spinModel);

        form.add(new JLabel("Película:")); form.add(cbPelicula);
        form.add(new JLabel("Jurado:"));   form.add(cbJurado);
        form.add(new JLabel("Puntaje (1-10):")); form.add(spinPuntaje);
        dialog.add(form, BorderLayout.CENTER);

        JPanel sur = new JPanel();
        JButton btnOk = new JButton("Calificar");
        JButton btnCancelar = new JButton("Cancelar");
        sur.add(btnOk); sur.add(btnCancelar);
        dialog.add(sur, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dialog.dispose());
        btnOk.addActionListener(e -> {
            try {
                int idPel = Integer.parseInt(cbPelicula.getSelectedItem().toString().split(" - ")[0].trim());
                Pelicula pel = gestor.getPeliculas().stream().filter(p -> p.getId() == idPel).findFirst().orElse(null);

                int idJur = Integer.parseInt(cbJurado.getSelectedItem().toString().split(" - ")[0].trim());
                Jurado jur = GestorUsuarios.buscarJuradoPorId(idJur, gestor);

                int puntaje = (int) spinPuntaje.getValue();
                GestorPeliculas.registrarCalificacion(pel, puntaje, jur, gestor);
                refrescarTabla();
                frame.actualizarStatus("Calificación registrada para \"" + pel.getTitulo() + "\".");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    // ── Determinar ganador ─────────────────────────────────────────────────────
    private void mostrarGanador() {
        Pelicula ganadora = null;
        double mejor = -1;
        for (Pelicula p : gestor.getPeliculas()) {
            if (!p.getCalificaciones().isEmpty() && p.getPromedioPuntaje() > mejor) {
                mejor = p.getPromedioPuntaje();
                ganadora = p;
            }
        }
        if (ganadora == null) {
            JOptionPane.showMessageDialog(frame, "Aún no hay películas calificadas.", "Ganador", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame,
                "🏆 PELÍCULA GANADORA: " + ganadora.getTitulo() +
                "\n⭐ Promedio: " + String.format("%.2f", mejor),
                "Ganador del Festival", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
