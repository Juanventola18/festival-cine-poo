package ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import logic.*;
import model.cine.*;
import model.membresias.*;
import model.personas.*;

public class UsuariosPanel extends JPanel {

    private final GestorFestival gestor;
    private final MainFrame frame;

    private final DefaultTableModel modeloEsp;
    private final DefaultTableModel modeloJur;
    private final DefaultTableModel modeloEdi;

    public UsuariosPanel(GestorFestival gestor, MainFrame frame) {
        this.gestor = gestor;
        this.frame = frame;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTabbedPane tabs = new JTabbedPane();

        // ── Espectadores ───────────────────────────────────────────────────────
        modeloEsp = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellido", "DNI", "Email", "Membresía", "Puntos"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaEsp = new JTable(modeloEsp);
        tablaEsp.setRowHeight(22);

        JPanel panEsp = new JPanel(new BorderLayout(4, 4));
        JButton btnNuevoEsp = new JButton("+ Registrar Espectador");
        btnNuevoEsp.addActionListener(e -> abrirFormularioEspectador());
        JPanel northEsp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northEsp.add(btnNuevoEsp);
        panEsp.add(northEsp, BorderLayout.NORTH);
        panEsp.add(new JScrollPane(tablaEsp), BorderLayout.CENTER);
        tabs.addTab("👤 Espectadores", panEsp);

        // ── Jurados ────────────────────────────────────────────────────────────
        modeloJur = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellido", "DNI", "Email", "Especialidad"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaJur = new JTable(modeloJur);
        tablaJur.setRowHeight(22);

        JPanel panJur = new JPanel(new BorderLayout(4, 4));
        JButton btnNuevoJur = new JButton("+ Registrar Jurado");
        btnNuevoJur.addActionListener(e -> abrirFormularioJurado());
        JPanel northJur = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northJur.add(btnNuevoJur);
        panJur.add(northJur, BorderLayout.NORTH);
        panJur.add(new JScrollPane(tablaJur), BorderLayout.CENTER);
        tabs.addTab("⚖️ Jurados", panJur);

        // ── Ediciones ──────────────────────────────────────────────────────────
        modeloEdi = new DefaultTableModel(new String[]{"Nro. Edición", "Fecha", "Secciones"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaEdi = new JTable(modeloEdi);
        tablaEdi.setRowHeight(22);

        JPanel panEdi = new JPanel(new BorderLayout(4, 4));
        JPanel northEdi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNuevaEdi = new JButton("+ Nueva Edición");
        JButton btnSeccion = new JButton("+ Agregar Sección");
        btnNuevaEdi.addActionListener(e -> abrirFormularioEdicion());
        btnSeccion.addActionListener(e -> abrirFormularioSeccion());
        northEdi.add(btnNuevaEdi);
        northEdi.add(btnSeccion);
        panEdi.add(northEdi, BorderLayout.NORTH);
        panEdi.add(new JScrollPane(tablaEdi), BorderLayout.CENTER);
        tabs.addTab("📅 Ediciones", panEdi);

        add(tabs, BorderLayout.CENTER);
        refrescarTablas();
    }

    public void refrescarTablas() {
        modeloEsp.setRowCount(0);
        modeloJur.setRowCount(0);
        modeloEdi.setRowCount(0);

        for (Usuario u : gestor.getUsuarios()) {
            if (u instanceof Espectador e)
                modeloEsp.addRow(new Object[]{e.getId(), e.getNombre(), e.getApellido(), e.getDni(), e.getEmail(),
                    e.getMembresia() != null ? e.getMembresia().getTipo() : "Ninguna", e.getPuntosAcumulados()});
            else if (u instanceof Jurado j)
                modeloJur.addRow(new Object[]{j.getId(), j.getNombre(), j.getApellido(), j.getDni(), j.getEmail(), j.getEspecialidad()});
        }
        for (Edicion ed : gestor.getEdiciones()) {
            String secciones = ed.getSecciones().stream()
                .map(Seccion::getNombreSeccion)
                .reduce((a, b) -> a + ", " + b).orElse("—");
            modeloEdi.addRow(new Object[]{ed.getNumeroEdicion(), ed.getFecha(), secciones});
        }
    }

    // ── Formulario Espectador ──────────────────────────────────────────────────
    private void abrirFormularioEspectador() {
        JDialog dialog = new JDialog(frame, "Nuevo Espectador", true);
        dialog.setSize(380, 290);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JTextField fNombre = new JTextField();
        JTextField fApellido = new JTextField();
        JTextField fDni = new JTextField();
        JTextField fEmail = new JTextField();
        JComboBox<String> cbMemb = new JComboBox<>(new String[]{"Sin membresía", "Estándar (10% desc.)", "Platino (25% desc.)"});
        form.add(new JLabel("Nombre:")); form.add(fNombre);
        form.add(new JLabel("Apellido:")); form.add(fApellido);
        form.add(new JLabel("DNI (9 dígitos):")); form.add(fDni);
        form.add(new JLabel("Email:")); form.add(fEmail);
        form.add(new JLabel("Membresía:")); form.add(cbMemb);
        dialog.add(form, BorderLayout.CENTER);

        JPanel sur = new JPanel();
        JButton btnOk = new JButton("Registrar");
        JButton btnCancel = new JButton("Cancelar");
        sur.add(btnOk); sur.add(btnCancel);
        dialog.add(sur, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnOk.addActionListener(e -> {
            try {
                int id = gestor.getUsuarios().size() + 1;
                Espectador esp = new Espectador(id, fNombre.getText().trim(), fApellido.getText().trim(),
                    fDni.getText().trim(), fEmail.getText().trim());
                LocalDate hoy = LocalDate.now();
                switch (cbMemb.getSelectedIndex()) {
                    case 1 -> esp.setMembresia(new MembresiaEstandar(hoy, hoy.plusYears(1), "activa"));
                    case 2 -> esp.setMembresia(new MembresiaPlatino(hoy, hoy.plusYears(1), "activa"));
                }
                GestorUsuarios.agregar(esp, gestor);
                refrescarTablas();
                frame.actualizarStatus("Espectador " + esp.getNombre() + " registrado.");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setVisible(true);
    }

    // ── Formulario Jurado ──────────────────────────────────────────────────────
    private void abrirFormularioJurado() {
        JDialog dialog = new JDialog(frame, "Nuevo Jurado", true);
        dialog.setSize(380, 260);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JTextField fNombre = new JTextField();
        JTextField fApellido = new JTextField();
        JTextField fDni = new JTextField();
        JTextField fEmail = new JTextField();
        JTextField fEsp = new JTextField();
        form.add(new JLabel("Nombre:")); form.add(fNombre);
        form.add(new JLabel("Apellido:")); form.add(fApellido);
        form.add(new JLabel("DNI (9 dígitos):")); form.add(fDni);
        form.add(new JLabel("Email:")); form.add(fEmail);
        form.add(new JLabel("Especialidad:")); form.add(fEsp);
        dialog.add(form, BorderLayout.CENTER);

        JPanel sur = new JPanel();
        JButton btnOk = new JButton("Registrar");
        JButton btnCancel = new JButton("Cancelar");
        sur.add(btnOk); sur.add(btnCancel);
        dialog.add(sur, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnOk.addActionListener(e -> {
            try {
                int id = gestor.getUsuarios().size() + 1;
                Jurado j = new Jurado(id, fNombre.getText().trim(), fApellido.getText().trim(),
                    fDni.getText().trim(), fEmail.getText().trim(), fEsp.getText().trim());
                GestorUsuarios.agregar(j, gestor);
                refrescarTablas();
                frame.actualizarStatus("Jurado " + j.getNombre() + " registrado.");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setVisible(true);
    }

    // ── Formulario Edición ─────────────────────────────────────────────────────
    private void abrirFormularioEdicion() {
        String input = JOptionPane.showInputDialog(frame, "Número de edición (año):", "Nueva Edición", JOptionPane.PLAIN_MESSAGE);
        if (input == null || input.isBlank()) return;
        try {
            int num = Integer.parseInt(input.trim());
            GestorEdiciones.registrar(new Edicion(num, LocalDate.now()), gestor);
            refrescarTablas();
            frame.actualizarStatus("Edición " + num + " registrada.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "El número de edición debe ser un entero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Formulario Sección ─────────────────────────────────────────────────────
    private void abrirFormularioSeccion() {
        if (gestor.getEdiciones().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay ediciones registradas.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] edOps = gestor.getEdiciones().stream()
            .map(e -> "Edición " + e.getNumeroEdicion() + " (" + e.getFecha() + ")")
            .toArray(String[]::new);
        String selEd = (String) JOptionPane.showInputDialog(frame, "Seleccioná la edición:",
            "Agregar Sección", JOptionPane.PLAIN_MESSAGE, null, edOps, edOps[0]);
        if (selEd == null) return;

        int idxEd = java.util.Arrays.asList(edOps).indexOf(selEd);
        Edicion edicion = gestor.getEdiciones().get(idxEd);

        String nombre = JOptionPane.showInputDialog(frame, "Nombre de la sección:");
        if (nombre == null || nombre.isBlank()) return;
        GestorEdiciones.agregarSeccion(edicion, nombre.trim(), gestor);
        refrescarTablas();
        frame.actualizarStatus("Sección \"" + nombre + "\" agregada a edición " + edicion.getNumeroEdicion() + ".");
    }
}
