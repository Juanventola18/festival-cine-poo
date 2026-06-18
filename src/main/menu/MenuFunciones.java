package main.menu;

import java.time.LocalDate;
import logic.*;
import model.cine.*;
import model.personas.*;

public class MenuFunciones {

    public static void programarFuncion(GestorFestival gestor) throws Exception {
        if (gestor.getPeliculas().isEmpty()) {
            System.out.println("Error: no hay películas registradas.");
            return;
        }
        Consola.listar(gestor.getPeliculas(), Pelicula::getTitulo);
        int idx = Consola.leerInt("Índice película");
        if (idx < 0 || idx >= gestor.getPeliculas().size()) {
            System.out.println("Error: índice inválido.");
            return;
        }
        LocalDate fecha = LocalDate.parse(Consola.leer("Fecha (AAAA-MM-DD)"));
        String hora     = Consola.leer("Hora (HH:MM)");
        GestorFunciones.programar(
            new Funcion(gestor.getPeliculas().get(idx), gestor.getSalas().get(0), fecha, hora),
            gestor
        );
        System.out.println("Función programada con éxito.");
    }

    public static void venderEntrada(GestorFestival gestor) {
        if (gestor.getCalendario().isEmpty()) {
            System.out.println("Error: no hay funciones programadas.");
            return;
        }
        Consola.listar(gestor.getCalendario(),
            f -> f.getPelicula().getTitulo() + " (" + f.getFecha() + " " + f.getHoraInicio() + ")");
        int idx = Consola.leerInt("Índice función");
        if (idx < 0 || idx >= gestor.getCalendario().size()) {
            System.out.println("Error: índice inválido.");
            return;
        }
        Funcion funcion = gestor.getCalendario().get(idx);

        String dni = Consola.leer("DNI espectador");
        Espectador espectador = GestorUsuarios.buscarEspectadorPorDni(dni, gestor);
        if (espectador == null) { System.out.println("Error: espectador no encontrado."); return; }

        // Calcular precio con descuento si tiene membresía
        double precioBase  = Funcion.getPrecioBase();
        double precioFinal = (espectador.getMembresia() != null)
            ? espectador.getMembresia().calcularDescuento(precioBase)
            : precioBase;

        System.out.println("──────────────────────────────");
        System.out.println("  Precio base:  $" + String.format("%,.0f", precioBase));
        if (espectador.getMembresia() != null) {
            System.out.println("  Membresía:    " + espectador.getMembresia().getTipo());
            System.out.println("  Precio final: $" + String.format("%,.0f", precioFinal));
        } else {
            System.out.println("  Sin membresía — precio full");
        }
        System.out.println("──────────────────────────────");

        int fila = Consola.leerInt("Fila (0-" + (funcion.getSala().getFilas() - 1) + ")");
        int col  = Consola.leerInt("Columna (0-" + (funcion.getSala().getColumnas() - 1) + ")");
        boolean ok = GestorFunciones.reservarButaca(funcion, fila, col, espectador, gestor);
        if (ok)
            System.out.println("✓ Entrada vendida. Total cobrado: $" + String.format("%,.0f", precioFinal));
        else
            System.out.println("✗ Error: butaca ya ocupada o inválida.");
    }

    public static void consultarOcupacion(GestorFestival gestor) {
        if (gestor.getCalendario().isEmpty()) {
            System.out.println("No hay funciones programadas.");
            return;
        }
        Consola.listar(gestor.getCalendario(),
            f -> f.getPelicula().getTitulo() + " (" + f.getHoraInicio() + ")");
        int idx = Consola.leerInt("Índice función");
        if (idx < 0 || idx >= gestor.getCalendario().size()) {
            System.out.println("Error: índice inválido.");
            return;
        }
        Funcion f = gestor.getCalendario().get(idx);
        System.out.println("Ocupación: " + f.getCantidadOcupados() + " / " + f.getSala().getCapacidadTotal());
    }
}
