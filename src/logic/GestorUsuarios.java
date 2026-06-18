package logic;

import model.personas.*;

public class GestorUsuarios {

    public static void agregar(Usuario u, GestorFestival gestor) throws Exception {
        Validador.validarDni(u.getDni());
        Validador.validarDniUnico(u.getDni(), gestor);
        Validador.validarEmail(u.getEmail());
        Validador.validarEmailUnico(u.getEmail(), gestor);
        gestor.getUsuarios().add(u);
        GestorArchivos.guardarTodo(gestor);
    }

    public static Espectador buscarEspectadorPorDni(String dni, GestorFestival gestor) {
        for (Usuario u : gestor.getUsuarios())
            if (u instanceof Espectador && u.getDni().equals(dni))
                return (Espectador) u;
        return null;
    }

    public static Jurado buscarJuradoPorId(int id, GestorFestival gestor) {
        for (Usuario u : gestor.getUsuarios())
            if (u instanceof Jurado && u.getId() == id)
                return (Jurado) u;
        return null;
    }
}