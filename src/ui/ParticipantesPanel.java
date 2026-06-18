package ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import logic.*;
import model.personas.*;

public class ParticipantesPanel extends JPanel {

    private final GestorFestival gestor;
    private final MainFrame frame;

    private final DefaultTableModel modeloDirectores;
    private final DefaultTableModel modeloActores;

    public ParticipantesPanel(GestorFestival gestor, MainFrame frame) {
        this.gestor = gestor;
        this.frame = frame;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTabbedPane tabs = new JTabbedPane();

        // ── Tab Directores ─────────────────────────────────────────────────────
        modeloDirectores = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellido", "Nacionalidad", "Experiencia (años)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaDir = new JTable(modeloDirectores);
        tablaDir.setRowHeight(22);
        tablaDir.getColumnModel().getColumn(0).setMaxWidth(40);

        JPanel panelDir = new JPanel(new BorderLayout(4, 4));
        JButton btnNuevoDir = new JButton("+ Agregar Director");
        btnNuevoDir.addActionListener(e -> abrirFormularioDirector());
        JPanel northDir = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northDir.add(btnNuevoDir);
        panelDir.add(northDir, BorderLayout.NORTH);
        panelDir.add(new JScrollPane(tablaDir), BorderLayout.CENTER);
        tabs.addTab("🎬 Directores", panelDir);

        // ── Tab Actores ────────────────────────────────────────────────────────
        modeloActores = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellido", "Nacionalidad", "Tipo", "Cachet Base"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaAct = new JTable(modeloActores);
        tablaAct.setRowHeight(22);
        tablaAct.getColumnModel().getColumn(0).setMaxWidth(40);

        JPanel panelAct = new JPanel(new BorderLayout(4, 4));
        JButton btnNuevoAct = new JButton("+ Agregar Actor");
        btnNuevoAct.addActionListener(e -> abrirFormularioActor());
        JPanel northAct = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northAct.add(btnNuevoAct);
        panelAct.add(northAct, BorderLayout.NORTH);
        panelAct.add(new JScrollPane(tablaAct), BorderLayout.CENTER);
        tabs.addTab("🎭 Actores", panelAct);

        add(tabs, BorderLayout.CENTER);
        refrescarTablas();
    }

    public void refrescarTablas() {
        modeloDirectores.setRowCount(0);
        modeloActores.setRowCount(0);
        for (Participante p : gestor.getParticipantes()) {
            if (p instanceof Director d)
                modeloDirectores.addRow(new Object[]{d.getIdParticipante(), d.getNombre(), d.getApellido(), d.getNacionalidad(), d.getExperienciaAnos()});
            else if (p instanceof Actor a)
                modeloActores.addRow(new Object[]{a.getIdParticipante(), a.getNombre(), a.getApellido(), a.getNacionalidad(), a.getTipoActor(), String.format("$%,.0f", a.getCachetBase())});
        }
    }

    // ── Formulario Director ────────────────────────────────────────────────────
    private void abrirFormularioDirector() {
        JDialog dialog = new JDialog(frame, "Nuevo Director", true);
        dialog.setSize(360, 250);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JTextField fNombre = new JTextField();
        JTextField fApellido = new JTextField();
        JTextField fNac = new JTextField();
        JTextField fExp = new JTextField();
        form.add(new JLabel("Nombre:")); form.add(fNombre);
        form.add(new JLabel("Apellido:")); form.add(fApellido);
        form.add(new JLabel("Nacionalidad:")); form.add(fNac);
        form.add(new JLabel("Experiencia (años):")); form.add(fExp);
        dialog.add(form, BorderLayout.CENTER);

        JPanel sur = new JPanel();
        JButton btnOk = new JButton("Registrar");
        JButton btnCancel = new JButton("Cancelar");
        sur.add(btnOk); sur.add(btnCancel);
        dialog.add(sur, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnOk.addActionListener(e -> {
            try {
                int id = gestor.getParticipantes().size() + 1;
                Director d = new Director(id, fNombre.getText().trim(), fApellido.getText().trim(),
                    fNac.getText().trim(), Integer.parseInt(fExp.getText().trim()));
                GestorParticipantes.agregar(d, gestor);
                refrescarTablas();
                frame.actualizarStatus("Director " + d.getNombre() + " registrado.");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Experiencia debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setVisible(true);
    }

    // ── Formulario Actor ───────────────────────────────────────────────────────
    private void abrirFormularioActor() {
        JDialog dialog = new JDialog(frame, "Nuevo Actor", true);
        dialog.setSize(360, 280);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JTextField fNombre = new JTextField();
        JTextField fApellido = new JTextField();
        JTextField fNac = new JTextField();
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Principal", "Secundario"});
        JTextField fCachet = new JTextField();
        form.add(new JLabel("Nombre:")); form.add(fNombre);
        form.add(new JLabel("Apellido:")); form.add(fApellido);
        form.add(new JLabel("Nacionalidad:")); form.add(fNac);
        form.add(new JLabel("Tipo:")); form.add(cbTipo);
        form.add(new JLabel("Cachet base:")); form.add(fCachet);
        dialog.add(form, BorderLayout.CENTER);

        JPanel sur = new JPanel();
        JButton btnOk = new JButton("Registrar");
        JButton btnCancel = new JButton("Cancelar");
        sur.add(btnOk); sur.add(btnCancel);
        dialog.add(sur, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnOk.addActionListener(e -> {
            try {
                int id = gestor.getParticipantes().size() + 1;
                Actor a = new Actor(id, fNombre.getText().trim(), fApellido.getText().trim(),
                    fNac.getText().trim(), cbTipo.getSelectedItem().toString(),
                    Double.parseDouble(fCachet.getText().trim()));
                GestorParticipantes.agregar(a, gestor);
                refrescarTablas();
                frame.actualizarStatus("Actor " + a.getNombre() + " registrado.");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Cachet debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setVisible(true);
    }
}
