package model.cine;

import model.personas.Espectador;

public class Ticket {
    private Funcion funcion;
    private Espectador espectador;
    private double precioFinal;

    public Ticket(Funcion funcion, Espectador espectador, double precioFinal) {
        this.funcion = funcion;
        this.espectador = espectador;
        this.precioFinal = precioFinal;
    }

    public Funcion getFuncion()                      { return this.funcion; }
    public Espectador getEspectador()                { return this.espectador; }
    public double getPrecioFinal()                   { return this.precioFinal; }
    public void setFuncion(Funcion f)                { this.funcion = f; }
    public void setEspectador(Espectador e)          { this.espectador = e; }
    public void setPrecioFinal(double p)             { this.precioFinal = p; }
}
