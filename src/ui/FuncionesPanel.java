package ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import logic.*;
import model.cine.*;
import model.personas.*;

public class FuncionesPanel extends JPanel {

    private final GestorFestival gestor;
    private final MainFrame frame;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    public FuncionesPanel(GestorFestival gestor, MainFrame frame) {
        this.gestor = gestor;
        this.frame = frame;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // ── Toolbar ────────────────────────────────────────────────────────────
        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JButton btnNueva = new JButton("+ Programar función");
        JButton btnVender = new JButton("🎟 Vender entrada");
        JButton btnOcupacion = new JButton("📊 Ver ocupación");
        JButton btnRefrescar = new JButton("🔄 Refrescar");

        btnNueva.addActionListener(e -> abrirFormularioProgramar());
        btnVender.addActionListener(e -> abrirFormularioVenta());
        btnOcupacion.addActionListener(e -> mostrarOcupacion());
        btnRefrescar.addActionListener(e -> refrescarTabla());

        norte.add(btnNueva);
        norte.add(btnVender);
        norte.add(btnOcupacion);
        norte.add(btnRefrescar);
        add(norte, BorderLayout.NORTH);

        // ── Tabla ──────────────────────────────────────────────────────────────
        String[] cols = {"Película", "Fecha", "Hora", "Sala", "Ocupación", "Disponibles", "% Lleno"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(22);
        tabla.getColumnModel().getColumn(3).setMaxWidth(50);
        tabla.getColumnModel().getColumn(4).setMaxWidth(80);
        tabla.getColumnModel().getColumn(5).setMaxWidth(90);
        tabla.getColumnModel().getColumn(6).setMaxWidth(70);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        refrescarTabla();
    }

    public void refrescarTabla() {
        modeloTabla.setRowCount(0);
        for (Funcion f : gestor.getCalendario()) {
            int cap  = f.getSala().getCapacidadTotal();
            int ocup = f.getCantidadOcupados();
            int disp = cap - ocup;
            int pct  = cap == 0 ? 0 : (ocup * 100 / cap);
            modeloTabla.addRow(new Object[]{
                f.getPelicula().getTitulo(), f.getFecha(), f.getHoraInicio(),
                f.getSala().getNumeroSala(),
                ocup + "/" + cap, disp, pct + "%"
            });
        }
        frame.actualizarStatus("Funciones programadas: " + gestor.getCalendario().size());
    }

    // ── Programar función ──────────────────────────────────────────────────────
    private void abrirFormularioProgramar() {
        if (gestor.getPeliculas().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay películas registradas.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JDialog dialog = new JDialog(frame, "Programar Función", true);
        dialog.setSize(380, 220);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JComboBox<String> cbPelicula = new JComboBox<>();
        for (Pelicula p : gestor.getPeliculas())
            cbPelicula.addItem(p.getId() + " - " + p.getTitulo());

        JTextField fFecha = new JTextField(LocalDate.now().plusDays(1).toString());
        JTextField fHora  = new JTextField("18:00");

        form.add(new JLabel("Película:")); form.add(cbPelicula);
        form.add(new JLabel("Fecha (AAAA-MM-DD):")); form.add(fFecha);
        form.add(new JLabel("Hora (HH:MM):")); form.add(fHora);
        dialog.add(form, BorderLayout.CENTER);

        JPanel sur = new JPanel();
        JButton btnOk = new JButton("Programar");
        JButton btnCancel = new JButton("Cancelar");
        sur.add(btnOk); sur.add(btnCancel);
        dialog.add(sur, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnOk.addActionListener(e -> {
            try {
                int idPel = Integer.parseInt(cbPelicula.getSelectedItem().toString().split(" - ")[0].trim());
                Pelicula pel = gestor.getPeliculas().stream().filter(p -> p.getId() == idPel).findFirst().orElse(null);
                LocalDate fecha = LocalDate.parse(fFecha.getText().trim());
                String hora = fHora.getText().trim();
                Funcion f = new Funcion(pel, gestor.getSalas().get(0), fecha, hora);
                GestorFunciones.programar(f, gestor);
                refrescarTabla();
                frame.actualizarStatus("Función programada: " + pel.getTitulo() + " el " + fecha);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setVisible(true);
    }

    // ── Vender entrada ─────────────────────────────────────────────────────────
    private void abrirFormularioVenta() {
        if (gestor.getCalendario().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay funciones programadas.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JDialog dialog = new JDialog(frame, "Vender Entrada", true);
        dialog.setSize(400, 280);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JComboBox<String> cbFuncion = new JComboBox<>();
        for (Funcion f : gestor.getCalendario())
            cbFuncion.addItem(f.getPelicula().getTitulo() + " | " + f.getFecha() + " " + f.getHoraInicio());

        JTextField fDni = new JTextField();
        JTextField fFila = new JTextField("0");
        JTextField fCol  = new JTextField("0");

        form.add(new JLabel("Función:")); form.add(cbFuncion);
        form.add(new JLabel("DNI espectador:")); form.add(fDni);
        form.add(new JLabel("Fila (0-4):")); form.add(fFila);
        form.add(new JLabel("Columna (0-4):")); form.add(fCol);

        JLabel lblPrecio = new JLabel("Precio: —");
        lblPrecio.setFont(lblPrecio.getFont().deriveFont(Font.BOLD));
        form.add(new JLabel("")); form.add(lblPrecio);

        // Actualizar precio al cambiar DNI
        fDni.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                Espectador esp = GestorUsuarios.buscarEspectadorPorDni(fDni.getText().trim(), gestor);
                if (esp != null) {
                    double precio = esp.getMembresia() != null
                        ? esp.getMembresia().calcularDescuento(Funcion.getPrecioBase())
                        : Funcion.getPrecioBase();
                    String memb = esp.getMembresia() != null ? " (" + esp.getMembresia().getTipo() + ")" : "";
                    lblPrecio.setText("Precio: $" + String.format("%,.0f", precio) + memb);
                } else if (!fDni.getText().isBlank()) {
                    lblPrecio.setText("Espectador no encontrado");
                }
            }
        });

        dialog.add(form, BorderLayout.CENTER);

        JPanel sur = new JPanel();
        JButton btnOk = new JButton("Vender");
        JButton btnCancel = new JButton("Cancelar");
        sur.add(btnOk); sur.add(btnCancel);
        dialog.add(sur, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnOk.addActionListener(e -> {
            try {
                int idxFun = cbFuncion.getSelectedIndex();
                Funcion funcion = gestor.getCalendario().get(idxFun);
                Espectador esp = GestorUsuarios.buscarEspectadorPorDni(fDni.getText().trim(), gestor);
                if (esp == null) throw new Exception("Espectador no encontrado.");
                int fila = Integer.parseInt(fFila.getText().trim());
                int col  = Integer.parseInt(fCol.getText().trim());

                double precio = esp.getMembresia() != null
                    ? esp.getMembresia().calcularDescuento(Funcion.getPrecioBase())
                    : Funcion.getPrecioBase();

                boolean ok = GestorFunciones.reservarButaca(funcion, fila, col, esp, gestor);
                if (ok) {
                    refrescarTabla();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(frame,
                        "✅ Entrada vendida\nPelícula: " + funcion.getPelicula().getTitulo() +
                        "\nFila: " + fila + " — Col: " + col +
                        "\nTotal: $" + String.format("%,.0f", precio),
                        "Venta exitosa", JOptionPane.INFORMATION_MESSAGE);
                    frame.actualizarStatus("Entrada vendida para " + esp.getNombre() + ".");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Butaca ya ocupada o inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Fila y columna deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setVisible(true);
    }

    // ── Ocupación con JProgressBar ─────────────────────────────────────────────
    private void mostrarOcupacion() {
        if (gestor.getCalendario().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay funciones registradas.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JDialog dialog = new JDialog(frame, "Ocupación de Funciones", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(frame);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        for (Funcion f : gestor.getCalendario()) {
            int cap  = f.getSala().getCapacidadTotal();
            int ocup = f.getCantidadOcupados();
            int pct  = cap == 0 ? 0 : (ocup * 100 / cap);

            JPanel fila = new JPanel(new BorderLayout(8, 2));
            fila.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
            fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            JLabel lbl = new JLabel(f.getPelicula().getTitulo() + " — " + f.getFecha() + " " + f.getHoraInicio());
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 12f));

            JProgressBar bar = new JProgressBar(0, 100);
            bar.setValue(pct);
            bar.setString(ocup + "/" + cap + " (" + pct + "%)");
            bar.setStringPainted(true);
            if (pct >= 80) bar.setForeground(new Color(200, 60, 60));
            else if (pct >= 50) bar.setForeground(new Color(220, 160, 0));
            else bar.setForeground(new Color(60, 160, 60));

            fila.add(lbl, BorderLayout.NORTH);
            fila.add(bar, BorderLayout.CENTER);
            panel.add(fila);
            panel.add(Box.createVerticalStrut(4));
        }

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialog.dispose());
        JPanel sur = new JPanel();
        sur.add(btnCerrar);

        dialog.add(new JScrollPane(panel), BorderLayout.CENTER);
        dialog.add(sur, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
