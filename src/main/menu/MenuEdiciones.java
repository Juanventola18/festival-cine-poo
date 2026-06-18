package main.menu;

import java.time.LocalDate;
import logic.*;
import model.cine.*;

public class MenuEdiciones {

    public static void registrarEdicion(GestorFestival gestor) {
        int ano = Consola.leerInt("Año de la edición");
        GestorEdiciones.registrar(new Edicion(ano, LocalDate.now()), gestor);
        System.out.println("Edición registrada con éxito.");
    }

    public static void crearSeccion(GestorFestival gestor) {
        if (gestor.getEdiciones().isEmpty()) {
            System.out.println("Error: no hay ediciones registradas.");
            return;
        }
        Consola.listar(gestor.getEdiciones(), e -> "Edición " + e.getNumeroEdicion() + " (" + e.getFecha() + ")");
        int idx = Consola.leerInt("Índice edición");
        if (idx < 0 || idx >= gestor.getEdiciones().size()) {
            System.out.println("Error: índice inválido.");
            return;
        }
        String nombre = Consola.leer("Nombre de la sección");
        GestorEdiciones.agregarSeccion(gestor.getEdiciones().get(idx), nombre, gestor);
        System.out.println("Sección creada con éxito.");
    }
}
