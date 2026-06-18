package ui;

import javax.swing.*;
import java.awt.*;
import logic.GestorArchivos;
import logic.GestorFestival;
import model.cine.Sala;

public class MainFrame extends JFrame {

    private final GestorFestival gestor;
    private final JTabbedPane tabbedPane;
    private final JLabel statusBar;

    public MainFrame() {
        this.gestor = new GestorFestival();
        GestorArchivos.cargarTodo(gestor);
        if (gestor.getSalas().isEmpty()) gestor._addSala(new Sala(1, 5, 5));

        setTitle("Festival de Cine — Sistema de Gestión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ── Menú ──────────────────────────────────────────────────────────────
        setJMenuBar(crearMenuBar());

        // ── Toolbar ───────────────────────────────────────────────────────────
        add(crearToolBar(), BorderLayout.NORTH);

        // ── Barra de estado ANTES de las pestañas ─────────────────────────────
        statusBar = new JLabel("  Sistema cargado.");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        add(statusBar, BorderLayout.SOUTH);

        // ── Pestañas principales ───────────────────────────────────────────────
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("🎬 Películas",    new PeliculasPanel(gestor, this));
        tabbedPane.addTab("🎭 Participantes", new ParticipantesPanel(gestor, this));
        tabbedPane.addTab("📅 Funciones",    new FuncionesPanel(gestor, this));
        tabbedPane.addTab("👤 Usuarios",     new UsuariosPanel(gestor, this));
        tabbedPane.addTab("📊 Reportes",     new ReportesPanel(gestor));
        add(tabbedPane, BorderLayout.CENTER);

        actualizarStatus("Sistema cargado.");
        setVisible(true);
    }

    // ── Actualizar barra de estado ─────────────────────────────────────────────
    public void actualizarStatus(String mensaje) {
        statusBar.setText("  " + mensaje + "    |    " + resumenRapido());
    }

    private String resumenRapido() {
        long esp = gestor.getUsuarios().stream()
            .filter(u -> u instanceof model.personas.Espectador).count();
        return "Películas: " + gestor.getPeliculas().size()
             + "  |  Funciones: " + gestor.getCalendario().size()
             + "  |  Espectadores: " + esp;
    }

    // ── Menú bar ──────────────────────────────────────────────────────────────
    private JMenuBar crearMenuBar() {
        JMenuBar mb = new JMenuBar();

        JMenu mArchivo = new JMenu("Archivo");
        JMenuItem miGuardar = new JMenuItem("Guardar todo");
        miGuardar.addActionListener(e -> {
            GestorArchivos.guardarTodo(gestor);
            actualizarStatus("Datos guardados correctamente.");
            JOptionPane.showMessageDialog(this, "Datos guardados.", "Guardar", JOptionPane.INFORMATION_MESSAGE);
        });
        JMenuItem miSalir = new JMenuItem("Salir");
        miSalir.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(this, "¿Guardar antes de salir?", "Salir", JOptionPane.YES_NO_CANCEL_OPTION);
            if (op == JOptionPane.YES_OPTION) GestorArchivos.guardarTodo(gestor);
            if (op != JOptionPane.CANCEL_OPTION) System.exit(0);
        });
        mArchivo.add(miGuardar);
        mArchivo.addSeparator();
        mArchivo.add(miSalir);

        JMenu mFestival = new JMenu("Festival");
        JMenuItem miEdiciones = new JMenuItem("Gestionar Ediciones");
        miEdiciones.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        mFestival.add(miEdiciones);

        JMenu mPeliculas = new JMenu("Películas");
        JMenuItem miPel = new JMenuItem("Ver películas");
        miPel.addActionListener(e -> tabbedPane.setSelectedIndex(0));
        JMenuItem miDirectores = new JMenuItem("Directores");
        miDirectores.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        JMenuItem miActores = new JMenuItem("Actores");
        miActores.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        mPeliculas.add(miPel);
        mPeliculas.addSeparator();
        mPeliculas.add(miDirectores);
        mPeliculas.add(miActores);

        JMenu mFunciones = new JMenu("Funciones");
        JMenuItem miFun = new JMenuItem("Ver funciones");
        miFun.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        JMenuItem miEntradas = new JMenuItem("Vender entrada");
        miEntradas.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        mFunciones.add(miFun);
        mFunciones.add(miEntradas);

        JMenu mUsuarios = new JMenu("Usuarios");
        JMenuItem miEsp = new JMenuItem("Espectadores");
        miEsp.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        JMenuItem miJur = new JMenuItem("Jurados");
        miJur.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        mUsuarios.add(miEsp);
        mUsuarios.add(miJur);

        JMenu mReportes = new JMenu("Reportes");
        JMenuItem miRep = new JMenuItem("Ver reportes");
        miRep.addActionListener(e -> tabbedPane.setSelectedIndex(4));
        mReportes.add(miRep);

        JMenu mAyuda = new JMenu("Ayuda");
        JMenuItem miAcerca = new JMenuItem("Acerca de...");
        miAcerca.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Festival de Cine — Sistema de Gestión\nEtapa 3 — Java Swing\nPOO — UADE",
            "Acerca de", JOptionPane.INFORMATION_MESSAGE));
        mAyuda.add(miAcerca);

        mb.add(mArchivo);
        mb.add(mFestival);
        mb.add(mPeliculas);
        mb.add(mFunciones);
        mb.add(mUsuarios);
        mb.add(mReportes);
        mb.add(mAyuda);
        return mb;
    }

    private void addTabItem(JMenu menu, String label, int tabIndex) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(e -> tabbedPane.setSelectedIndex(tabIndex));
        menu.add(item);
    }

    private JToolBar crearToolBar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.add(botonToolbar("🎬 Películas",     () -> tabbedPane.setSelectedIndex(0)));
        tb.add(botonToolbar("🎭 Participantes", () -> tabbedPane.setSelectedIndex(1)));
        tb.add(botonToolbar("📅 Funciones",     () -> tabbedPane.setSelectedIndex(2)));
        tb.add(botonToolbar("👤 Usuarios",      () -> tabbedPane.setSelectedIndex(3)));
        tb.add(botonToolbar("📊 Reportes",      () -> tabbedPane.setSelectedIndex(4)));
        tb.addSeparator();
        tb.add(botonToolbar("💾 Guardar", () -> {
            GestorArchivos.guardarTodo(gestor);
            actualizarStatus("Datos guardados.");
        }));
        return tb;
    }

    private JButton botonToolbar(String label, Runnable accion) {
        JButton b = new JButton(label);
        b.setFocusPainted(false);
        b.addActionListener(e -> accion.run());
        return b;
    }

    public GestorFestival getGestor() { return gestor; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
