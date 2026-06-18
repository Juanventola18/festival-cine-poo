package model.personas;

public abstract class Participante {
    private int idParticipante;
    private String nombre;
    private String apellido;
    private String nacionalidad;
    private int edad;

    public Participante(int idParticipante, String nombre, String apellido, String nacionalidad) {
        this.idParticipante = idParticipante;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nacionalidad = nacionalidad;
        this.edad = 30;
    }

    public Participante(int idParticipante, String nombre, String apellido, String nacionalidad, int edad) {
        this.idParticipante = idParticipante;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nacionalidad = nacionalidad;
        this.edad = edad;
    }

    public String getNombre()       { return this.nombre; }
    public String getApellido()     { return this.apellido; }
    public int getEdad()            { return this.edad; }
    public int getIdParticipante()  { return this.idParticipante; }
    public String getNacionalidad() { return this.nacionalidad; }
}
