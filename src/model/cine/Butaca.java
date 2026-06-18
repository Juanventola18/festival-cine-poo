package model.cine;

import exception.ButacaOcupadaException;

public class Butaca {
    private int fila;
    private int columna;
    private int numero;
    private EstadoReserva estado;

    public Butaca(int fila, int columna, int numero) {
        this.fila = fila;
        this.columna = columna;
        this.numero = numero;
        this.estado = EstadoReserva.LIBRE;
    }

    public void reservarButaca() throws ButacaOcupadaException {
        if (!this.estaLibre())
            throw new ButacaOcupadaException("La butaca Nro " + this.numero + " ya se encuentra OCUPADA.");
        this.estado = EstadoReserva.OCUPADO;
    }

    public boolean estaLibre()  { return this.estado == EstadoReserva.LIBRE; }
    public boolean isOcupada()  { return this.estado == EstadoReserva.OCUPADO; }

    public void liberarButaca() {
        this.estado = EstadoReserva.LIBRE;
        System.out.println("[Butaca] La butaca " + this.numero + " ahora está LIBRE.");
    }

    public String consultarEstado() {
        return "Butaca Nro: " + this.numero + " [Fila: " + this.fila + ", Col: " + this.columna + "] -> Estado: " + this.estado;
    }

    public int getNumero()  { return this.numero; }
    public int getFila()    { return this.fila; }
    public int getColumna() { return this.columna; }

    public void setFila(int fila)               { this.fila = fila; }
    public void setColumna(int columna)         { this.columna = columna; }
    public void setNumero(int numero)           { this.numero = numero; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }
}