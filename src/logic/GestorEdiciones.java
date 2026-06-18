package logic;

import model.cine.*;

public class GestorEdiciones {

    public static void registrar(Edicion e, GestorFestival gestor) {
        gestor.getEdiciones().add(e);
        GestorArchivos.guardarTodo(gestor);
    }

    public static void agregarSeccion(Edicion edicion, String nombreSeccion, GestorFestival gestor) {
        edicion.agregarSeccion(new Seccion(nombreSeccion));
        GestorArchivos.guardarTodo(gestor);
    }
}
