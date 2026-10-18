package controlador;

public class Notificador{
    private boolean activo = true;

    public void notificar(String destinatario, String mensaje){
        if(activo){
            System.out.println("(Notificacion para" + destinatario + ") " + mensaje);
        }
    }

    public boolean isActivo(){
        return activo;
    }

    public void setActivo(boolean activo){
        this.activo = activo;
    }
}