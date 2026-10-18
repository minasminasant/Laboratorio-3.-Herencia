package controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import modelo.Cita;
import modelo.EstadoCita;
import modelo.Farmaceutico;
import modelo.TipoCita;
import modelo.TrabajadorMedico;


public class HospitalManager{
    private GestorPersonal gestorPersonal;
    private GestorCitas gestorCitas;

    public HospitalManager(GestorPersonal gestorPersonal, GestorCitas gestorCitas){
        this.gestorPersonal = gestorPersonal;
        this.gestorCitas = gestorCitas;
    }

    public int[] simularCrisisPersonal(TrabajadorMedico trabajador){
        trabajador.setAusente(true);
        return gestorCitas.redistribuirCitas(trabajador, "Ausencia de " + trabajador.getNombreCompleto());
    }

    public void reincorporarTrabajador(TrabajadorMedico trabajador){
        trabajador.setAusente(false);
    }

    public Cita atenderEmergenciaQuirurgica(String paciente, LocalDateTime inicio){
        LocalDateTime fin = inicio.plusMinutes(TipoCita.CIRUGIA.getDuracionMinutos());
        TrabajadorMedico elegido = null;
        int menosConflictos = Integer.MAX_VALUE;
        ArrayList<TrabajadorMedico> cirujanos = gestorPersonal.buscarPorTipoCita(TipoCita.CIRUGIA);
        for(int i = 0; i < cirujanos.size(); i++){
            TrabajadorMedico c = cirujanos.get(i);
            if(c.isAusente()){
                continue;
            }
            ArrayList<Cita> choques = gestorCitas.buscarCitasEnConflicto(c, inicio, fin, null);
            if(hayCitaEnProgreso(choques)){
                continue;
            }
            if(choques.size() < menosConflictos){
                menosConflictos = choques.size();
                elegido = c;
            }
        }
        if(elegido == null){
            return null;
        }
        ArrayList<Cita> afectadas = gestorCitas.buscarCitasEnConflicto(elegido, inicio, fin, null);
        Cita urgente = gestorCitas.agregarCitaUrgente(paciente, elegido, inicio, TipoCita.CIRUGIA);
        for(int i = 0; i < afectadas.size(); i++){
            Cita afectada = afectadas.get(i);
            boolean movida = gestorCitas.reagendarAutomaticamente(afectada, "Emergencia quirurgica");
            if(!movida){
                gestorCitas.cancelarPorMotivo(afectada, "Emergencia quirurgica, sin espacio para poder reagendar");
            }
        }
        return urgente;
    }

    private boolean hayCitaEnProgreso(ArrayList<Cita> lista){
        for(int i = 0; i < lista.size(); i++){
            if(lista.get(i).getEstado() == EstadoCita.EN_PROGRESO){
                return true;
            }
        }
        return false;
    }

    public String registrarPrescripciones(int idEmpleado, int cantidad){
        TrabajadorMedico t = gestorPersonal.buscarPorId(idEmpleado);
        if(t == null){
            return "No existe un trabajador con el ID: " + idEmpleado;
        }
        if(!(t instanceof Farmaceutico)){
            return t.getNombreCompleto() + " no es farmaceutico";
        }
        Farmaceutico f = (Farmaceutico) t;
        if(!f.despacharPrescripciones(cantidad)){
            return "La cantidad debe ser mas que 0";
        }
        return null;
    }

    public double calcularUtilizacion(TrabajadorMedico trabajador, LocalDate dia){
        int capacidad = trabajador.getCapacidadDiaria();
        if(capacidad <= 0){
            return 0;
        }
        int citasDelDia = gestorCitas.contarCitasDelDia(trabajador, dia, null);
        return (citasDelDia * 100.0) / capacidad;
    }

    public GestorPersonal getGestorPersonal(){
        return gestorPersonal;
    }

    public GestorCitas getGestorCitas(){
        return gestorCitas;
    }
}