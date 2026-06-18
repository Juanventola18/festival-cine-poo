package model.personas;

import model.membresias.Membresia;

public class Espectador extends Usuario {
    private Membresia membresia;
    private int puntosAcumulados;

    public Espectador(int idUser, String nombre, String apellido, String dni, String email) {
        super(idUser, nombre, apellido, dni, email);
        this.puntosAcumulados = 0;
        this.membresia = null;
    }

    public void setMembresia(Membresia membresia) { this.membresia = membresia; }
    public Membresia getMembresia()               { return this.membresia; }
    public void sumarPuntos(int puntos)           { this.puntosAcumulados += puntos; }
    public int getPuntosAcumulados()              { return this.puntosAcumulados; }

    public String toArchivo() {
        String tipo = (membresia != null) ? membresia.getTipo() : "Ninguna";
        return idUser + ";" + nombre + ";" + apellido + ";" + dni + ";" + email + ";" + puntosAcumulados + ";" + tipo;
    }

    @Override public String getRol() { return "Espectador"; }
}
