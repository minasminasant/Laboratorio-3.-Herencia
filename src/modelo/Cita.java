package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Cita{
    public static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private int idCita;
    private String nombrePaciente;
    private TrabajadorMedico trabajador;
    private LocalDateTime fechaHora;
    private TipoCita tipo;
    private EstadoCita estado;
    private ArrayList<RegistroCambio> historial;

    public Cita(int idCita, String nombrePaciente, TrabajadorMedico trabajador, LocalDateTime fechaHora, TipoCita tipo){
        this.idCita = idCita;
        this.nombrePaciente = nombrePaciente;
        this.trabajador = trabajador;
        this.fechaHora = fechaHora;
        this.tipo = tipo;
        this.estado = EstadoCita.PROGRAMADA;
        this.historial = new ArrayList<RegistroCambio>();
    }

    public LocalDateTime getFechaFin(){
        return fechaHora.plusMinutes(tipo.getDuracionMinutos());
    }

    public boolean seSolapaCon(LocalDateTime inicio, LocalDateTime fin){
        return fechaHora.isBefore(fin) && inicio.isBefore(getFechaFin());
    }

    public void reagendar(LocalDateTime nuevaFecha, String motivo){
        reagendar(nuevaFecha,this.trabajador, motivo);
    }

    public void reagendar(LocalDateTime nuevaFecha, TrabajadorMedico nuevoTrabajador, String motivo){
        RegistroCambio registro = new RegistroCambio(this.fechaHora, nuevaFecha, this.trabajador.getNombreCompleto(), nuevoTrabajador.getNombreCompleto(), motivo);
        historial.add(registro);
        this.fechaHora = nuevaFecha;
        this.trabajador = nuevoTrabajador;
        this.estado = EstadoCita.REAGENDADA;
    }

    public int getIdCita(){
        return idCita;
    }

    public String getNombrePaciente(){
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente){
        this.nombrePaciente = nombrePaciente;
    }

    public TrabajadorMedico getTrabajador(){
        return trabajador;
    }

    public LocalDateTime getFechaHora(){
        return fechaHora;
    }

    public TipoCita getTipo(){
        return tipo;
    }

    public EstadoCita getEstado(){
        return estado;
    }

    public void setEstado(EstadoCita estado){
        this.estado = estado;
    }

    public ArrayList<RegistroCambio> getHistorial(){
        return historial;
    }

    public int getCantidadReagendamientos(){
        return historial.size();
    }

    @Override
    public String toString(){
        return "Cita # " + idCita + "| " + nombrePaciente + "| " + tipo + "| " + fechaHora.format(Cita.FORMATO_FECHA_HORA) + " a " + getFechaFin().toLocalTime() + "| Atiende: " + trabajador.getNombreCompleto() + " [ " + trabajador.getTipoTrabajador() + "] " + "| Estado: " + estado;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(!(obj instanceof Cita)){
            return false;
        }
        Cita otra = (Cita) obj;
        return this.idCita == otra.idCita;
    }

    @Override
    public int hashCode(){
        return Integer.hashCode(idCita);
    }
}