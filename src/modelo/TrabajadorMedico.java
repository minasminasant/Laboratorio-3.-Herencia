package modelo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

public abstract class TrabajadorMedico{
    protected  static final LocalTime HORA_INICIO_LABORAL = LocalTime.of(7, 0);
    protected  static final LocalTime HORA_FIN_LABORAL = LocalTime.of(19, 0);
    private int idEmpleado;
    private String nombreCompleto;
    private String departamento;
    private int aniosExperiencia;
    private double salarioBase;
    private boolean ausente;

    public TrabajadorMedico(int idEmpleado, String nombreCompleto, String departamento, int aniosExperiencia, double salarioBase){
        this.idEmpleado = idEmpleado;
        this.nombreCompleto = nombreCompleto;
        this.departamento = departamento;
        this.aniosExperiencia = aniosExperiencia;
        this.salarioBase = salarioBase;
        this.ausente = false;
    }

    public abstract double calcularSalario();

    public abstract String getTipoTrabajador();

    public abstract boolean puedeAtender(TipoCita tipo);

    public abstract void registrarAtencion(TipoCita tipo, int minutos);

    public double calcularSalario(double bonoExtra){
        return calcularSalario() + bonoExtra;
    }

    public int getCapacidadDiaria(){
        return 8;
    }

    public boolean estaEnHorario(LocalDateTime inicio, int duracionMinutos){
        LocalDateTime fin = inicio.plusMinutes(duracionMinutos);
        if(!fin.toLocalDate().equals(inicio.toLocalDate())){
            return false;
        }
        LocalTime horaInicio = inicio.toLocalTime();
        LocalTime horaFin = fin.toLocalTime();
        return !horaInicio.isBefore(HORA_INICIO_LABORAL) && !horaFin.isAfter(HORA_FIN_LABORAL);
    }

    public static String formatoMoneda(double monto){
        return String.format(Locale.US, "Q%,.2f", monto);
    }

    public String getDepartamento(){
        return departamento;
    }

    public int getIdEmpleado(){
        return idEmpleado;
    }

    public String getNombreCompleto(){
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto){
        this.nombreCompleto = nombreCompleto;
    }

    public void setDepartamento(String departamento){
        this.departamento = departamento;
    }

    public int getAniosExperiencia(){
        return aniosExperiencia;
    }

    public void setAniosExperiencia(int aniosExperiencia){
        if(aniosExperiencia >= 0){
            this.aniosExperiencia = aniosExperiencia;
        }
    }

    public double getSalarioBase(){
        return salarioBase;
    }

    public void setSalarioBase(double salarioBase){
        if(salarioBase >= 0){
            this.salarioBase = salarioBase;
        }
    }

    public boolean isAusente(){
        return ausente;
    }

    public void setAusente(boolean ausente){
        this.ausente = ausente;
    }

    @Override 
    public String toString(){
        String texto = "| " + getTipoTrabajador() + "| ID " + idEmpleado + "| " + nombreCompleto + "| Depto: " + departamento + "| Experencia:" + aniosExperiencia + "| Salario base: " + formatoMoneda(salarioBase);
        if(ausente){
            texto += "Ausente";
        } 
        return texto;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(!(obj instanceof TrabajadorMedico)){
            return false;
        }
        TrabajadorMedico otro = (TrabajadorMedico) obj;
        return this.idEmpleado == otro.idEmpleado;
    }

    @Override
    public int hashCode(){
        return Integer.hashCode(idEmpleado);
    }
}