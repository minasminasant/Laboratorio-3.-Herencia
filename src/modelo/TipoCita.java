package modelo;

public enum TipoCita{
    CONSULTA_GENERAL("Consulta general", 30),
    CIRUGIA("Cirugia", 120),
    TERAPIA("Terapia", 60),
    DIAGNOSTICO("Diagnostico", 45);

    private String nombre;
    private int duracionMinutos;

    TipoCita(String nombre, int duracionMinutos){
        this.nombre = nombre;
        this.duracionMinutos = duracionMinutos;
    }

    public String getNombre(){
        return nombre;
    }

    public int getDuracionMinutos(){
        return duracionMinutos;
    }

    @Override
    public String toString(){
        return nombre;
    }
}