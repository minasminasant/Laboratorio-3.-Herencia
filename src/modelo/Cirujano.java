package modelo;

import java.util.ArrayList;

public class Cirujano extends TrabajadorMedico{
    private ArrayList<String> tiposOperaciones;
    private int horasCirugiaDisponibles;
    private double bonoRiesgo;
    private double tarifaPorHora;
    private double horasOperadas;

    public Cirujano(int  idEmpleado, String nombreCompleto, String departamento, int aniosExperiencia, double salarioBase, ArrayList<String> tiposOperaciones, int horasCirugiaDisponibles, double bonoRiesgo, double tarifaPorHora){
        super(idEmpleado, nombreCompleto, departamento, aniosExperiencia, salarioBase);
        this.tiposOperaciones = tiposOperaciones;
        this.horasCirugiaDisponibles = horasCirugiaDisponibles;
        this.bonoRiesgo = bonoRiesgo;
        this.tarifaPorHora = tarifaPorHora;
        this.horasOperadas = 0;
    }

    @Override
    public double calcularSalario(){
        return getSalarioBase() + (horasOperadas * tarifaPorHora) + bonoRiesgo;
    }

    @Override
    public String getTipoTrabajador(){
        return "Cirujano";
    }

    @Override
    public boolean puedeAtender(TipoCita tipo){
        return tipo == TipoCita.CIRUGIA;
    }

    @Override
    public void registrarAtencion(TipoCita tipo, int minutos){
        if(tipo == TipoCita.CIRUGIA){
            horasOperadas += minutos / 60.0;
        }
    }

    @Override
    public int getCapacidadDiaria(){
        return (horasCirugiaDisponibles * 60) / TipoCita.CIRUGIA.getDuracionMinutos();
    }

    public ArrayList<String> getTipoOperaciones(){
        return tiposOperaciones;
    }

    public void setTiposOperaciones(ArrayList<String> tiposOperaciones){
        this.tiposOperaciones = tiposOperaciones;
    }

    public int getHorasCirugiaDisponibles(){
        return horasCirugiaDisponibles;
    }

    public void setHorasCirugiaDisponibles(int horasCirugiaDisponibles){
        if(horasCirugiaDisponibles > 0){
            this.horasCirugiaDisponibles = horasCirugiaDisponibles;
        }
    }

    public double getBonoRiesgo(){
        return bonoRiesgo;
    }

    public void setBonoRiesgo(double bonoRiesgo){
        if(bonoRiesgo >= 0){
            this.bonoRiesgo = bonoRiesgo;
        }
    }

    public double getTarifaPorHora(){
        return tarifaPorHora;
    }

    public void setTarifaPorHora(double tarifaPorHora){
        if(tarifaPorHora >= 0){
            this.tarifaPorHora = tarifaPorHora;
        }
    }
    public double getHorasOperadas(){
        return horasOperadas;
    }

    @Override
    public String toString(){
        return super.toString() + "| Operaciones: " + tiposOperaciones + "| Horas cirugia: " + horasCirugiaDisponibles + "| Bono Riesgo: "  +  formatoMoneda(bonoRiesgo) + " | Tarifas: " + formatoMoneda(tarifaPorHora) + "| Horas operadas: " + horasOperadas;
    }
}