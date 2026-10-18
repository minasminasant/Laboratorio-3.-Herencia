package modelo;

import java.time.LocalDateTime;

public class RegistroCambio{
    private LocalDateTime momentoDelCambio;
    private LocalDateTime fechaAnterior;
    private LocalDateTime fechaNueva;
    private String trabajadorAnterior;
    private String trabajadorNuevo;
    private String motivo;

    public RegistroCambio(LocalDateTime fechaAnterior, LocalDateTime fechaNueva, String trabajadorAnterior, String trabajadorNuevo, String motivo){
        this.momentoDelCambio = LocalDateTime.now();
        this.fechaAnterior = fechaAnterior;
        this.fechaNueva = fechaNueva;
        this.trabajadorAnterior = trabajadorAnterior;
        this.trabajadorNuevo = trabajadorNuevo;
        this.motivo = motivo;
    }

    public LocalDateTime getMomentoDelCambio(){
        return momentoDelCambio;
    }

    public LocalDateTime getFechaAnterior(){
        return fechaAnterior;
    }

    public LocalDateTime getFechaNueva(){
        return fechaNueva;
    }

    public String getTrabajadorAnterior(){
        return trabajadorAnterior;
    }

    public String getTrabajadorNuevo(){
        return trabajadorNuevo;
    }

    public String getMotivo(){
        return motivo;
    }

    @Override
    public String toString(){
        String texto = "Registrado " + momentoDelCambio.format(Cita.FORMATO_FECHA_HORA) + "| " + fechaAnterior.format(Cita.FORMATO_FECHA_HORA) + "| " + fechaNueva.format(Cita.FORMATO_FECHA_HORA); 
        if(trabajadorAnterior.equals(trabajadorNuevo)){
            texto += "| Trabajador: " +  trabajadorNuevo;
        }
        else{
            texto += "| Trabajador: " + trabajadorAnterior + "|" + trabajadorNuevo;
        }
        return texto + "| Motivo: " + motivo;
    }

}