package modelo;

public class DoctorGeneral extends TrabajadorMedico{
    private String especializacion;
    private int capacidadPacientesPorDia;
    private double tarifaConsulta;
    private int consultasRealizadas;

    public DoctorGeneral(int idEmpleado, String nombreCompleto, String departamento, int aniosExperiencia, double salarioBase, String especializacion, int capacidadPacientesPorDia, double tarifaConsulta){
        super(idEmpleado, nombreCompleto, departamento, aniosExperiencia, salarioBase);
        this.especializacion = especializacion;
        this.capacidadPacientesPorDia = capacidadPacientesPorDia;
        this.tarifaConsulta = tarifaConsulta;
        this.consultasRealizadas = 0;
    }

    @Override
    public double calcularSalario(){
        return getSalarioBase() + (consultasRealizadas * tarifaConsulta);
    }

    @Override
    public String getTipoTrabajador(){
        return "Doctro General";
    }

    @Override
    public boolean  puedeAtender(TipoCita tipo){
        return tipo == TipoCita.CONSULTA_GENERAL;
    }

    @Override
    public void registrarAtencion(TipoCita tipo, int minutos){
        if(tipo == TipoCita.CONSULTA_GENERAL){
            consultasRealizadas++;
        }
    }

    @Override
    public int getCapacidadDiaria(){
        return capacidadPacientesPorDia;
    }

    public String getEspecializacion(){
        return especializacion;
    }

    public void setEspecializacion(String especializacion){
        this.especializacion = especializacion;
    }

    public int getCapacidadPacientesPorDia(){
        return capacidadPacientesPorDia;
    }

    public void setCapacidadPacientesPorDia(int capacidadPacientesPorDia){
        if(capacidadPacientesPorDia > 0){
            this.capacidadPacientesPorDia = capacidadPacientesPorDia;
        }
    }

    public double getTarifaConsulta(){
        return tarifaConsulta;
    }

    public void setTarifaConsulta(double tarifaConsulta){
        if(tarifaConsulta >= 0){
            this.tarifaConsulta = tarifaConsulta;
        }
    }

    public int getConsultasRealizadas(){
        return consultasRealizadas;
    }

    @Override
    public String toString(){
        return super.toString() + "| Especializacion: " + especializacion + "| Pacientes: " + capacidadPacientesPorDia + "| Tarifas consulta: " + formatoMoneda(tarifaConsulta) + "| consultas realizadas: " + consultasRealizadas;
    }
}