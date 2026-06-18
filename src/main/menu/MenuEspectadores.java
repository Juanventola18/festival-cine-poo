package main.menu;

import java.time.LocalDate;
import logic.*;
import model.membresias.*;
import model.personas.*;

public class MenuEspectadores {

    public static void registrarEspectador(GestorFestival gestor) throws Exception {
        int id          = Consola.nextId(gestor.getUsuarios());
        String nombre   = Consola.leer("Nombre");
        Validador.validarNombre(nombre, "Nombre");
        String apellido = Consola.leer("Apellido");
        Validador.validarNombre(apellido, "Apellido");
        String dni      = Consola.leer("DNI (9 dígitos)");
        String email    = Consola.leer("Email");

        Espectador esp = new Espectador(id, nombre, apellido, dni, email);

        System.out.println("Membresía:");
        System.out.println("  1. Sin membresía");
        System.out.println("  2. Estándar (10% descuento)");
        System.out.println("  3. Platino  (25% descuento)");
        int opMemb = Consola.leerInt("Elegí una opción");

        LocalDate hoy = LocalDate.now();
        switch (opMemb) {
            case 2 -> esp.setMembresia(new MembresiaEstandar(hoy, hoy.plusYears(1), "activa"));
            case 3 -> esp.setMembresia(new MembresiaPlatino(hoy, hoy.plusYears(1), "activa"));
            default -> System.out.println("Sin membresía asignada.");
        }

        GestorUsuarios.agregar(esp, gestor);
        System.out.println("Espectador registrado. ID: " + id +
            (esp.getMembresia() != null ? " | Membresía: " + esp.getMembresia().getTipo() : ""));
    }

    public static void registrarJurado(GestorFestival gestor) throws Exception {
        int id              = Consola.nextId(gestor.getUsuarios());
        String nombre       = Consola.leer("Nombre");
        Validador.validarNombre(nombre, "Nombre");
        String apellido     = Consola.leer("Apellido");
        Validador.validarNombre(apellido, "Apellido");
        String dni          = Consola.leer("DNI (9 dígitos)");
        String email        = Consola.leer("Email");
        String especialidad = Consola.leer("Especialidad");
        GestorUsuarios.agregar(new Jurado(id, nombre, apellido, dni, email, especialidad), gestor);
        System.out.println("Jurado registrado. ID: " + id);
    }
}
