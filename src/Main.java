import controlador.GestorCitas;
import controlador.GestorPersonal;
import controlador.HospitalManager;
import controlador.Notificador;
import vista.VistaConsola;

public class Main{
    public static void main(String[] args) {
        Notificador notificador = new Notificador();
        GestorPersonal gestorPersonal = new GestorPersonal();
        GestorCitas gestorCitas = new GestorCitas(gestorPersonal, notificador);
        HospitalManager manager = new HospitalManager(gestorPersonal, gestorCitas);
        VistaConsola vista = new VistaConsola(manager);
        vista.iniciar();
    }
}