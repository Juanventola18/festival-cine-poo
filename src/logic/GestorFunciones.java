package logic;

import model.cine.*;
import model.personas.Espectador;
import exception.FuncionSuperpuestaException;

public class GestorFunciones {

    public static void programar(Funcion nueva, GestorFestival gestor) throws Exception {
        Validador.validarFechaFutura(nueva.getFecha());
        Validador.validarHora(nueva.getHoraInicio());
        int inicioN = horaAMinutos(nueva.getHoraInicio());
        int finN    = inicioN + nueva.getPelicula().getDuracion();
        for (Funcion existente : gestor.getCalendario()) {
            if (existente.getSala().equals(nueva.getSala()) && existente.getFecha().equals(nueva.getFecha())) {
                int inicioE = horaAMinutos(existente.getHoraInicio());
                int finE    = inicioE + existente.getPelicula().getDuracion();
                if (inicioN < finE && finN > inicioE)
                    throw new FuncionSuperpuestaException("La sala está ocupada en ese rango horario.");
            }
        }
        gestor.getCalendario().add(nueva);
        GestorArchivos.guardarTodo(gestor);
    }

    public static boolean reservarButaca(Funcion funcion, int fila, int col, Espectador espectador, GestorFestival gestor) {
        boolean ok = funcion.reservarButaca(fila, col, espectador);
        if (ok) GestorArchivos.guardarTodo(gestor);
        return ok;
    }

    private static int horaAMinutos(String hora) {
        String[] p = hora.split(":");
        return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]);
    }
}
