package controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import modelo.Cirujano;
import modelo.DoctorGeneral;
import modelo.Enfermero;
import modelo.Farmaceutico;
import modelo.Radiologo;
import modelo.Terapeuta;
import modelo.TipoCita;
import modelo.TipoTurno;
import modelo.TrabajadorMedico;

public class DatosPrueba{
    private ArrayList<String> lista(String... elementos){
        ArrayList<String> resultado = new ArrayList<String>();
        for(int i = 0; i < elementos.length; i++){
            resultado.add(elementos[i]);
        }
        return resultado;
    }

    public LocalDate cargar(HospitalManager manager){
        GestorPersonal personal = manager.getGestorPersonal();
        GestorCitas citas = manager.getGestorCitas();

        DoctorGeneral d1 = new DoctorGeneral(personal.generarIdEmpleado(), "Dr. Morales", "Medicina general", 12, 9000, "Medicina interna", 8, 150);
        DoctorGeneral d2 = new DoctorGeneral(personal.generarIdEmpleado(), "Dr. Santiago", "Pediatra", 8, 8500, "Pediatra", 6, 200);
        DoctorGeneral d3 = new DoctorGeneral(personal.generarIdEmpleado(), "Dr. Luis", "Medicina general", 15, 10000, "Cardiologo", 6, 3000);

        Cirujano c1 = new Cirujano(personal.generarIdEmpleado(), "Dr. Roberto", "Cirugía", 20, 15000, lista("Apendicectomía", "Hernia"), 8, 1500, 400);
        Cirujano c2 = new Cirujano(personal.generarIdEmpleado(), "Dra. Maria", "Cirugía", 14, 14000, lista("Cardiovascular", "Bypass"), 6, 2000, 450);
        Cirujano c3 = new Cirujano(personal.generarIdEmpleado(), "Dr. Minas", "Cirugía", 10, 13000, lista("Traumatologia", "Ortopedia"), 8, 1200, 380);

        Enfermero e1 = new Enfermero(personal.generarIdEmpleado(), "Ana", "Enfermeria", 6, 5000, TipoTurno.DIURNO, "Licenciada");
        Enfermero e2 = new Enfermero(personal.generarIdEmpleado(), "Elizabeth", "Enfermeria", 4, 4000, TipoTurno.DIURNO, "Tecnico");
        Enfermero e3 = new Enfermero(personal.generarIdEmpleado(), "Valentina", "Enfermeria", 9, 3000, TipoTurno.DIURNO, "Especialista");

        Radiologo r1 = new Radiologo(personal.generarIdEmpleado(), "Dr. Fernando", "Radiologia", 11, 11000, lista("rayos x", "tomografo"), 250);
        Radiologo r2 = new Radiologo(personal.generarIdEmpleado(), "Dr. Ortiz", "Radiologia", 7, 10000, lista("Resonancia", "Ultrasonido"), 280);

        Farmaceutico f1 = new Farmaceutico(personal.generarIdEmpleado(), "Karla", "Farmacia", 5, 6500, 80, true, 10);
        Farmaceutico f2 = new Farmaceutico(personal.generarIdEmpleado(), "Pedro", "Farmacia", 2, 6000, 60, false, 8);

        Terapeuta t1 = new Terapeuta(personal.generarIdEmpleado(), "Elena", "Rehabilitacion", 8, 7000, "Fisica", 60, 120);
        Terapeuta t2 = new Terapeuta(personal.generarIdEmpleado(), "Mario", "Rehabilitacion", 6, 6500, "Respiracion", 43, 100);
        Terapeuta t3 = new Terapeuta(personal.generarIdEmpleado(), "Rosa", "Rehabilitacion", 10, 6000, "Ocupacional", 60, 130);

        TrabajadorMedico[] todos = {
            d1, d2, d3 , c1, c2, c3, e1, e2, e3, r1, r2, f1, f2, t1, t2, t3
        };
        for(int i = 0; i < todos.length; i++){
            personal.contratar(todos[i]);
        }
        for(int i = 0; i < 40; i++){
            d1.registrarAtencion(TipoCita.CONSULTA_GENERAL, 30);
        }
        for(int i = 0; i < 55 ; i++){
            d2.registrarAtencion(TipoCita.CONSULTA_GENERAL, 30);
        }
        for(int i = 0; i < 30; i++){
            d3.registrarAtencion(TipoCita.CONSULTA_GENERAL, 30);
        }
        for(int i = 0; i < 6; i++){
            c1.registrarAtencion(TipoCita.CIRUGIA, 120);
        }
        for(int i = 0; i < 4; i++){
            c2.registrarAtencion(TipoCita.CIRUGIA, 120);
        }
        for(int i = 0; i < 5; i++){
            c3.registrarAtencion(TipoCita.CIRUGIA, 120);
        }
        for(int i = 0; i < 35; i++){
            r1.registrarAtencion(TipoCita.DIAGNOSTICO, 45);
        }
        for(int i = 0; i < 28; i++){
            r2.registrarAtencion(TipoCita.DIAGNOSTICO, 45);
        }
        f1.despacharPrescripciones(80);
        f1.despacharPrescripciones(60);
        f2.despacharPrescripciones(55);
        for(int i = 0; i < 22; i++){
            t1.registrarAtencion(TipoCita.TERAPIA, 60);
        }
        for(int i = 0; i < 18; i++){
            t2.registrarAtencion(TipoCita.TERAPIA, 45);
        }
        for(int i = 0; i < 25; i++){
            t3.registrarAtencion(TipoCita.TERAPIA, 60);
        }

        LocalDate dia = LocalDate.now().plusDays(1);

        Object[][] agenda = {
            {"Juan", TipoCita.CONSULTA_GENERAL, 8, 0},
            {"Maria", TipoCita.CONSULTA_GENERAL, 8, 0},
            {"Carlos", TipoCita.CONSULTA_GENERAL, 8, 0},
            {"Ana", TipoCita.CONSULTA_GENERAL, 8, 30},
            {"Diego", TipoCita.CONSULTA_GENERAL, 9, 0},
            {"Laura", TipoCita.CONSULTA_GENERAL, 9, 0},
            {"Pablo", TipoCita.CIRUGIA, 8, 0},
            {"Sandra", TipoCita.CIRUGIA, 8, 0},
            {"Hector", TipoCita.CIRUGIA, 10, 30},
            {"Gabriel", TipoCita.CIRUGIA, 13, 0},
            {"Ricardo", TipoCita.TERAPIA, 9, 0},
            {"Tania", TipoCita.TERAPIA, 9, 0},
            {"Oscar", TipoCita.TERAPIA, 10, 0},
            {"Beatriz", TipoCita.TERAPIA, 11, 0},
            {"Julio", TipoCita.DIAGNOSTICO, 8, 0},
            {"Irma", TipoCita.DIAGNOSTICO, 8, 0},
            {"Esteban", TipoCita.DIAGNOSTICO, 9, 0},
            {"Valeria", TipoCita.DIAGNOSTICO, 9, 30},
            {"Myrnor", TipoCita.CONSULTA_GENERAL, 9, 30},
            {"Bernardo", TipoCita.CONSULTA_GENERAL, 10, 0}
        };

        citas.getNotificador().setActivo(false);
        for(int i = 0; i < agenda.length; i++){
            String paciente = (String) agenda[i][0];
            TipoCita tipo = (TipoCita) agenda [i][1];
            int hora = (Integer) agenda[i][2];
            int minuto = (Integer) agenda[i][3];
            LocalDateTime inicio = LocalDateTime.of(dia, LocalTime.of(hora, minuto));
            citas.programarCitaAutomatica(paciente, tipo, inicio, "");
        }
        citas.getNotificador().setActivo(true);
        return dia;
    }
}