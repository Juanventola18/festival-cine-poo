package logic;

import java.util.ArrayList;
import java.util.List;
import model.cine.*;
import model.personas.*;

public class GestorFestival {
    private List<Edicion>      ediciones;
    private List<Pelicula>     peliculas;
    private List<Participante> participantes;
    private List<Usuario>      usuarios;
    private List<Sala>         salas;
    private List<Funcion>      calendario;

    public GestorFestival() {
        this.ediciones     = new ArrayList<>();
        this.peliculas     = new ArrayList<>();
        this.participantes = new ArrayList<>();
        this.usuarios      = new ArrayList<>();
        this.salas         = new ArrayList<>();
        this.calendario    = new ArrayList<>();
    }

    public void _addPelicula(Pelicula p) {
        if (p.getId() == 0) p.setId(this.peliculas.size() + 1);
        this.peliculas.add(p);
    }
    public void _addUsuario(Usuario u)           { this.usuarios.add(u); }
    public void _addParticipante(Participante p) { this.participantes.add(p); }
    public void _addFuncion(Funcion f)           { this.calendario.add(f); }
    public void _addEdicion(Edicion e)           { this.ediciones.add(e); }
    public void _addSala(Sala s)                 { this.salas.add(s); }

    public List<Edicion>      getEdiciones()     { return ediciones; }
    public List<Pelicula>     getPeliculas()      { return peliculas; }
    public List<Participante> getParticipantes()  { return participantes; }
    public List<Usuario>      getUsuarios()       { return usuarios; }
    public List<Sala>         getSalas()          { return salas; }
    public List<Funcion>      getCalendario()     { return calendario; }
}
