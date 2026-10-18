package modelo;

public class Terapeuta extends Especialista{
    private String tipoTerapia;
    private int duracionPromedioSesion;

    public Terapeuta(int idEmpleado, String nombreCompleto, String departamento, int aniosExperiencia, double salarioBase, String tipoTerapia, int duracionPromedioSesion, double comisionPorSesion){
        super(idEmpleado, nombreCompleto, departamento, aniosExperiencia, salarioBase, comisionPorSesion);
        this.tipoTerapia = tipoTerapia;
        this.duracionPromedioSesion = duracionPromedioSesion;
    }

    @Override
    public String getTipoTrabajador(){
        return "terapeuta";
    }

    @Override
    public boolean puedeAtender(TipoCita tipo){
        return tipo == TipoCita.TERAPIA;
    }

    @Override
    public int getCapacidadDiaria(){
        return (8 * 60) / duracionPromedioSesion;
    }

    public String getTipoTerapia(){
        return tipoTerapia;
    }

    public void setTipoTerapia(String tipoTerapia){
        this.tipoTerapia = tipoTerapia;
    }

    public int getDuracionPromedioSesion(){
        return duracionPromedioSesion;
    }

    public void setDuracionPromedioSesion(int duracionPromedioSesion){
        if(duracionPromedioSesion > 0){
            this.duracionPromedioSesion = duracionPromedioSesion;
        }
    }

    @Override
    public String toString(){
        return super.toString() + "| terapia: " + tipoTerapia + "| duracion sesion: " + duracionPromedioSesion + "min";
    }
}