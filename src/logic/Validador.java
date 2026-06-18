package logic;

import java.time.LocalDate;

public class Validador {

    public static void validarNombre(String nombre, String campo) throws Exception {
        if (nombre == null || nombre.trim().length() < 2)
            throw new Exception(campo + " inválido (mínimo 2 caracteres).");
        if (nombre.trim().length() > 50)
            throw new Exception(campo + " inválido (máximo 50 caracteres).");
        if (!nombre.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]+"))
            throw new Exception(campo + " inválido (solo letras y espacios).");
    }

    public static void validarDni(String dni) throws Exception {
        if (dni == null || !dni.matches("\\d{10}"))
            throw new Exception("El DNI debe tener exactamente 10 dígitos numéricos.");
    }

    public static void validarEmail(String email) throws Exception {
        if (email == null || !email.trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            throw new Exception("El email no tiene un formato válido (ejemplo: usuario@dominio.com).");
    }

    public static void validarDniUnico(String dni, GestorFestival gestor) throws Exception {
        for (model.personas.Usuario u : gestor.getUsuarios())
            if (u.getDni().equalsIgnoreCase(dni.trim()))
                throw new Exception("Ya existe un usuario con ese DNI.");
    }

    public static void validarEmailUnico(String email, GestorFestival gestor) throws Exception {
        if (email == null || email.trim().isEmpty()) return;
        for (model.personas.Usuario u : gestor.getUsuarios())
            if (u.getEmail() != null && u.getEmail().equalsIgnoreCase(email.trim()))
                throw new Exception("El email ya está registrado por otro usuario.");
    }

    public static void validarTituloUnico(String titulo, GestorFestival gestor) throws Exception {
        for (model.cine.Pelicula p : gestor.getPeliculas())
            if (p.getTitulo().equalsIgnoreCase(titulo.trim()))
                throw new Exception("Ya existe una película con ese título.");
    }

    public static void validarDuracion(int duracion) throws Exception {
        if (duracion < 1)
            throw new Exception("La duración debe ser al menos 1 minuto.");
        if (duracion > 600)
            throw new Exception("La duración no puede superar 600 minutos.");
    }

    public static void validarAnio(int anio) throws Exception {
        int anioActual = LocalDate.now().getYear();
        if (anio < 1888)
            throw new Exception("El año no puede ser anterior a 1888 (inicio del cine).");
        if (anio > anioActual)
            throw new Exception("El año no puede ser futuro.");
    }

    public static void validarHora(String hora) throws Exception {
        if (hora == null || !hora.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"))
            throw new Exception("Hora inválida. Usá el formato HH:MM (ejemplo: 14:30).");
    }

    public static void validarFechaFutura(LocalDate fecha) throws Exception {
        if (fecha.isBefore(LocalDate.now()))
            throw new Exception("La fecha debe ser hoy o en el futuro.");
    }

    public static void validarCachet(double cachet) throws Exception {
        if (cachet <= 0)
            throw new Exception("El cachet debe ser mayor a 0.");
    }

    public static void validarExperiencia(int anos) throws Exception {
        if (anos < 0)
            throw new Exception("Los años de experiencia no pueden ser negativos.");
        if (anos > 80)
            throw new Exception("Los años de experiencia no pueden superar 80.");
    }

    public static void validarPuntaje(int puntaje) throws Exception {
        if (puntaje < 1 || puntaje > 10)
            throw new Exception("El puntaje debe ser entre 1 y 10.");
    }
}
