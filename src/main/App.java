package main;

import logic.*;
import main.menu.*;
import model.cine.Sala;

public class App {
    private static final GestorFestival gestor = new GestorFestival();

    public static void main(String[] args) {
        GestorArchivos.cargarTodo(gestor);
        if (gestor.getSalas().isEmpty()) gestor._addSala(new Sala(1, 5, 5));

        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            try {
                String input = Consola.getTeclado().nextLine();
                if (input.isEmpty()) continue;
                opcion = Integer.parseInt(input);
                switch (opcion) {
                    case 1  -> MenuEdiciones.registrarEdicion(gestor);
                    case 2  -> MenuPeliculas.registrarPelicula(gestor);
                    case 3  -> MenuParticipantes.registrarDirector(gestor);
                    case 4  -> MenuParticipantes.registrarActor(gestor);
                    case 5  -> MenuEdiciones.crearSeccion(gestor);
                    case 6  -> MenuFunciones.programarFuncion(gestor);
                    case 7  -> MenuEspectadores.registrarEspectador(gestor);
                    case 8  -> MenuFunciones.venderEntrada(gestor);
                    case 9  -> MenuFunciones.consultarOcupacion(gestor);
                    case 10 -> MenuPeliculas.registrarCalificacion(gestor);
                    case 11 -> MenuPeliculas.consultarPromedio(gestor);
                    case 12 -> MenuPeliculas.determinarGanadores(gestor);
                    case 13 -> MenuEspectadores.registrarJurado(gestor);
                    case 14 -> MenuListados.listarPeliculas(gestor);
                    case 15 -> MenuListados.listarEspectadores(gestor);
                    case 16 -> MenuListados.listarJurados(gestor);
                    case 17 -> MenuListados.listarDirectores(gestor);
                    case 18 -> MenuListados.listarActores(gestor);
                    case 19 -> MenuListados.listarFunciones(gestor);
                    case 20 -> MenuListados.listarTodo(gestor);
                    case 21 -> MenuReportes.reporteRecaudacion(gestor);
                    case 22 -> MenuReportes.rankingPeliculas(gestor);
                    case 23 -> MenuReportes.historialFunciones(gestor);
                    case 24 -> MenuReportes.resumenGeneral(gestor);
                    case 0  -> System.out.println("¡Hasta luego!");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        Consola.getTeclado().close();
    }

    private static void mostrarMenu() {
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("        FESTIVAL DE CINE — MENÚ PRINCIPAL      ");
        System.out.println("══════════════════════════════════════════════");
        System.out.println("  — REGISTROS —");
        System.out.println("   1. Edición          2. Película");
        System.out.println("   3. Director         4. Actor");
        System.out.println("   5. Sección          7. Espectador");
        System.out.println("  13. Jurado");
        System.out.println("  — FUNCIONES —");
        System.out.println("   6. Programar        8. Vender entrada");
        System.out.println("   9. Ocupación");
        System.out.println("  — EVALUACIONES —");
        System.out.println("  10. Registrar       11. Promedio");
        System.out.println("  12. Ganadores");
        System.out.println("  — LISTADOS —");
        System.out.println("  14. Películas       15. Espectadores");
        System.out.println("  16. Jurados         17. Directores");
        System.out.println("  18. Actores         19. Funciones");
        System.out.println("  20. Ver todo");
        System.out.println("  — REPORTES —");
        System.out.println("  21. Recaudación     22. Ranking");
        System.out.println("  23. Historial       24. Resumen general");
        System.out.println("──────────────────────────────────────────────");
        System.out.println("   0. Salir");
        System.out.println("══════════════════════════════════════════════");
        System.out.print("  Opción: ");
    }
}