package model.cine;

import java.util.ArrayList;
import java.util.List;

public class Sala {
    private int numeroSala;
    private int filas;
    private int columnas;
    private int capacidadTotal;
    private List<Butaca> butacas;

    public Sala(int numeroSala, int filas, int columnas) {
        this.numeroSala = numeroSala;
        this.filas = filas;
        this.columnas = columnas;
        this.capacidadTotal = filas * columnas;
        this.butacas = new ArrayList<>();
        int nro = 1;
        for (int i = 1; i <= filas; i++)
            for (int j = 1; j <= columnas; j++)
                this.butacas.add(new Butaca(i, j, nro++));
    }

    public Butaca buscarButaca(int fila, int columna) {
        for (Butaca b : this.butacas)
            if (b.getFila() == fila && b.getColumna() == columna) return b;
        return null;
    }

    public boolean hayLugar() {
        for (Butaca b : this.butacas)
            if (b.estaLibre()) return true;
        return false;
    }

    public int getNumeroSala()       { return this.numeroSala; }
    public int getNumero()           { return this.numeroSala; }
    public int getFilas()            { return this.filas; }
    public int getColumnas()         { return this.columnas; }
    public int getCapacidadTotal()   { return this.capacidadTotal; }
    public List<Butaca> getButacas() { return this.butacas; }

    public void setNumeroSala(int n)       { this.numeroSala = n; }
    public void setFilas(int f)            { this.filas = f; }
    public void setColumnas(int c)         { this.columnas = c; }
    public void setCapacidadTotal(int c)   { this.capacidadTotal = c; }
    public void setButacas(List<Butaca> b) { this.butacas = b; }
}
