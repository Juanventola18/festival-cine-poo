package model.personas;

public class Actor extends Participante {
    private String tipoActor;
    private double cachetBase;

    public Actor(int idParticipante, String nombre, String apellido, String nacionalidad, String tipoActor, double cachetBase) {
        super(idParticipante, nombre, apellido, nacionalidad);
        this.tipoActor = tipoActor;
        this.cachetBase = cachetBase;
    }

    public double obtenerCachet() {
        return "Principal".equalsIgnoreCase(this.tipoActor) ? this.cachetBase * 1.20 : this.cachetBase;
    }

    public String getTipoActor()                 { return this.tipoActor; }
    public double getCachetBase()                { return this.cachetBase; }
    public void setTipoActor(String tipoActor)   { this.tipoActor = tipoActor; }
    public void setCachetBase(double cachetBase) { this.cachetBase = cachetBase; }
}
