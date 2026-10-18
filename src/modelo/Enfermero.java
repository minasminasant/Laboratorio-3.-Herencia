package modelo;

import java.time.LocalDateTime;

public class Enfermero extends TrabajadorMedico{
    private static final double PORCENTAJE_BONO_NOCTURNO = 0.25;
    private TipoTurno tipoTurno;
    private String nivelCertificacion;
    private int atencionesAsistidas;

    public Enfermero(int idEmpleado, String nombreCompleto, String departamento, int aniosExperiencia, double salarioBase, TipoTurno tipoTurno, String nivelCertificacion){
        super(idEmpleado, nombreCompleto, departamento, aniosExperiencia, salarioBase);
        this.tipoTurno = tipoTurno;
        this.nivelCertificacion = nivelCertificacion;
        this.atencionesAsistidas = 0;
    }

    @Override
    public double calcularSalario(){
        double salario = getSalarioBase();
        if(tipoTurno == TipoTurno.NOCTURNO){
            salario += getSalarioBase() * PORCENTAJE_BONO_NOCTURNO;
        }
        return salario;
    }

    @Override
    public String getTipoTrabajador(){
        return "Enfermero";
    }

    @Override
    public boolean puedeAtender(TipoCita tipo){
        return tipo == TipoCita.CONSULTA_GENERAL;
    }

    @Override
    public void registrarAtencion(TipoCita tipo, int minutos){
        atencionesAsistidas++;
    }

    @Override
    public int getCapacidadDiaria(){
        return 10;
    }

    @Override
    public boolean estaEnHorario(LocalDateTime inicio, int duracionMinutos){
        if(tipoTurno == TipoTurno.DIURNO){
            return super.estaEnHorario(inicio, duracionMinutos);
        }
        LocalDateTime fin = inicio.plusMinutes(duracionMinutos);
        boolean empiezaEnLaNoche = !inicio.toLocalTime().isBefore(HORA_FIN_LABORAL);
        boolean terminaEnLaMadrugada = !fin.toLocalTime().isAfter(HORA_INICIO_LABORAL);
        return empiezaEnLaNoche || terminaEnLaMadrugada;
    }

    public TipoTurno geTipoTurno(){
        return tipoTurno;
    }

    public void setTipoTurno(TipoTurno tipoTurno){
        this.tipoTurno = tipoTurno;
    }

    public String getNivelCertificacion(){
        return nivelCertificacion;
    }

    public void setNivelCertificacion(String nivelCertificacion){
        this.nivelCertificacion = nivelCertificacion;
    }

    public int getAtencionesAsistidas(){
        return atencionesAsistidas;
    }

    @Override
    public String toString(){
        return super.toString() + "| Turno: " + tipoTurno + "| Certificacion: " + nivelCertificacion;
    }
}