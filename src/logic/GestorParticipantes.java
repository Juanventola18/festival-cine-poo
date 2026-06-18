package logic;

import model.personas.*;

public class GestorParticipantes {

    public static void agregar(Participante p, GestorFestival gestor) throws Exception {
        Validador.validarNombre(p.getNombre(), "Nombre");
        Validador.validarNombre(p.getApellido(), "Apellido");
        if (p instanceof Actor a) Validador.validarCachet(a.getCachetBase());
        if (p instanceof Director d) Validador.validarExperiencia(d.getExperienciaAnos());
        gestor.getParticipantes().add(p);
        GestorArchivos.guardarTodo(gestor);
    }

    public static Director buscarDirectorPorId(int id, GestorFestival gestor) {
        for (Participante p : gestor.getParticipantes())
            if (p instanceof Director d && d.getIdParticipante() == id) return d;
        return null;
    }

    public static Actor buscarActorPorId(int id, GestorFestival gestor) {
        for (Participante p : gestor.getParticipantes())
            if (p instanceof Actor a && a.getIdParticipante() == id) return a;
        return null;
    }
}
