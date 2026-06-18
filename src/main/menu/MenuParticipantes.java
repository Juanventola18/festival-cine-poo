package main.menu;

import logic.*;
import model.personas.*;

public class MenuParticipantes {

    public static void registrarDirector(GestorFestival gestor) throws Exception {
        int id          = Consola.nextId(gestor.getParticipantes());
        String nombre   = Consola.leer("Nombre");
        String apellido = Consola.leer("Apellido");
        String nac      = Consola.leer("Nacionalidad");
        int exp         = Consola.leerInt("Experiencia (años)");
        GestorParticipantes.agregar(new Director(id, nombre, apellido, nac, exp), gestor);
        System.out.println("Director registrado con éxito.");
    }

    public static void registrarActor(GestorFestival gestor) throws Exception {
        int id          = Consola.nextId(gestor.getParticipantes());
        String nombre   = Consola.leer("Nombre");
        String apellido = Consola.leer("Apellido");
        String nac      = Consola.leer("Nacionalidad");
        String tipo     = Consola.leer("Tipo (Principal/Secundario)");
        double cachet   = Consola.leerDouble("Cachet base");
        GestorParticipantes.agregar(new Actor(id, nombre, apellido, nac, tipo, cachet), gestor);
        System.out.println("Actor registrado con éxito.");
    }
}
