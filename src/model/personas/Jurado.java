package model.personas;

public class Jurado extends Usuario {
    private String especialidad;

    public Jurado(int idUser, String nombre, String apellido, String dni, String email, String especialidad) {
        super(idUser, nombre, apellido, dni, email);
        this.especialidad = especialidad;
    }

    public String getEspecialidad()       { return this.especialidad; }
    public void setEspecialidad(String e) { this.especialidad = e; }

    public String toArchivo() {
        return idUser + ";" + nombre + ";" + apellido + ";" + dni + ";" + email + ";" + especialidad;
    }

    @Override public String getRol() { return "Jurado"; }
}
