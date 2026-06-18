package model.personas;

public abstract class Usuario {
    protected int idUser;
    protected String nombre;
    protected String apellido;
    protected String dni;
    protected String email;

    public Usuario(int idUser, String nombre, String apellido, String dni, String email) {
        this.idUser = idUser;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
    }

    public String getNombre()   { return this.nombre; }
    public String getApellido() { return this.apellido; }
    public String getDni()      { return this.dni; }
    public String getEmail()    { return this.email; }
    public int getId()          { return this.idUser; }

    public abstract String getRol();
}
