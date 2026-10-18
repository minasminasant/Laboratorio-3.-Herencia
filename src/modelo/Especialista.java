package modelo;

public abstract class Especialista extends TrabajadorMedico{
    private double comisionPorProcedimiento;
    private int procedimientosRealizados;

    public Especialista(int idEmpleado, String nombreCompleto, String departamento, int aniosExperiencia, double salarioBase, double comisionPorProcedimiento){
         super(idEmpleado, nombreCompleto, departamento, aniosExperiencia, salarioBase);
         this.comisionPorProcedimiento = comisionPorProcedimiento;
         this.procedimientosRealizados = 0;
    }

    @Override
    public double calcularSalario(){
        return getSalarioBase() + (procedimientosRealizados * comisionPorProcedimiento);
    }

    @Override
    public void registrarAtencion(TipoCita tipo, int minutos){
        if(puedeAtender(tipo)){
            procedimientosRealizados++;
        }
    }

    public void sumarProcedimientos(int cantidad){
        if(cantidad > 0){
            procedimientosRealizados += cantidad;
        }
    }

    public double getComisionPorProcedimiento(){
        return comisionPorProcedimiento;
    }

    public void setComisionPorProcedimiento(double comisionPorProcedimiento){
        if(comisionPorProcedimiento >= 0){
            this.comisionPorProcedimiento = comisionPorProcedimiento;
        }
    }

    public int getProcedimientosRealizados(){
        return procedimientosRealizados;
    }

    @Override
    public String toString(){
        return super.toString() + "| comision: " + formatoMoneda(comisionPorProcedimiento) + " | procedimiento" + procedimientosRealizados;
    }
}