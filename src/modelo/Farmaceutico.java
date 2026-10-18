package modelo;

public class Farmaceutico extends Especialista{
    private int limitePrescripcionesDia;
    private boolean licenciaSustanciasControladas;

    public Farmaceutico(int idEmpleado, String nombreCompleto, String departamento, int aniosExperiencia, double salarioBase, int limitePrescripcionesDia, boolean licenciaSustanciasControladas, double comisionPorPrescripcion){
        super(idEmpleado, nombreCompleto, departamento, aniosExperiencia, salarioBase, comisionPorPrescripcion);
        this.limitePrescripcionesDia = limitePrescripcionesDia;
        this.licenciaSustanciasControladas = licenciaSustanciasControladas;
    }

    @Override
    public String getTipoTrabajador(){
        return "Farmaceutico";
    }

    @Override
    public boolean puedeAtender(TipoCita tipo){
        return false;
    }

    public boolean despacharPrescripciones(int cantidad){
        if(cantidad <= 0 || cantidad > limitePrescripcionesDia){
            return false;
        }
        sumarProcedimientos(cantidad);
        return true;
    }

    @Override
    public int getCapacidadDiaria(){
        return limitePrescripcionesDia;
    }

    public int getLimitePrescripcionesDia(){
        return limitePrescripcionesDia;
    }

    public void setLimitePrescripcionesDia(int limitePrescripcionesDia){
        if(limitePrescripcionesDia > 0){
            this.limitePrescripcionesDia = limitePrescripcionesDia;
        }
    }

    public boolean isLicenciaSustanciasControladas(){
        return licenciaSustanciasControladas;
    }

    public void setLicenciaSustanciasControladas(boolean licenciaSustanciasControladas){
        this.licenciaSustanciasControladas = licenciaSustanciasControladas;
    }

    @Override
    public String toString(){
        return super.toString() + "| Limite: " + limitePrescripcionesDia + "| licencua sustancias controladas: " + (licenciaSustanciasControladas ? "SI" : "NO");
    }
}