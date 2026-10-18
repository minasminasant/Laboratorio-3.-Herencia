package modelo;

import java.util.ArrayList;

public class Radiologo extends Especialista{
    private ArrayList<String> equiposCertificados;

    public Radiologo(int idEmpleado, String nombreCompleto, String departamento, int aniosExperiencia, double salarioBase, ArrayList<String> equiposCertificados, double tarifaPorEstudio){
        super(idEmpleado, nombreCompleto, departamento, aniosExperiencia, salarioBase, tarifaPorEstudio);
        this.equiposCertificados = equiposCertificados;
    }

    @Override
    public String getTipoTrabajador(){
        return "Radiologo";
    }

    @Override
    public boolean puedeAtender(TipoCita tipo){
        return tipo == TipoCita.DIAGNOSTICO;
    }

    public ArrayList<String> getEquiposCertificados(){
        return equiposCertificados;
    }

    public void setEquiposCertificados(ArrayList<String> equiposCertificados){
        this.equiposCertificados = equiposCertificados;
    }

    public double getTarifaPorEstudio(){
        return getComisionPorProcedimiento();
    }

    @Override
    public String toString(){
        return super.toString() + "| Equipos certificados: " + equiposCertificados;
    }
}