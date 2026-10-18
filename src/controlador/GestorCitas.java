package controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import modelo.Cita;
import modelo.EstadoCita;
import modelo.TipoCita;
import modelo.TrabajadorMedico;

public class GestorCitas{
    private static final int PASO_BUSQUEDA_MINUTOS = 30;
    private static final int DIAS_MAXIMOS_BUSQUEDA = 3;
    private ArrayList<Cita> citas;
    private GestorPersonal gestorPersonal;
    private Notificador notificador;
    private int siguienteIdCita;
    private String ultimoError;


    public GestorCitas(GestorPersonal gestorPersonal, Notificador notificador){
        this.citas = new ArrayList<Cita>();
        this.gestorPersonal = gestorPersonal;
        this.notificador = notificador;
        this.siguienteIdCita = 1;
        this.ultimoError = "";
    }

    public String verificarDisponibilidad(TrabajadorMedico trabajador, TipoCita tipo, LocalDateTime inicio, Cita citaIgnorada){
        if(trabajador.isAusente()){
            return trabajador.getNombreCompleto() + "Esta ausente";
        }
        if(!trabajador.puedeAtender(tipo)){
            return trabajador.getNombreCompleto() + "[" + trabajador.getTipoTrabajador() + "] No puede atender citas de tipo:" + tipo;
        }
        if(!trabajador.estaEnHorario(inicio, tipo.getDuracionMinutos())){
            return "la hora no esta en el horario laboral de " + trabajador.getNombreCompleto();
        }
        LocalDateTime fin = inicio.plusMinutes(tipo.getDuracionMinutos());
        ArrayList<Cita> choques = buscarCitasEnConflicto(trabajador, inicio, fin, citaIgnorada);
        if(!choques.isEmpty()){
            Cita primera = choques.get(0);
            return trabajador.getNombreCompleto() + "ya tiene una  cita:" + primera.getIdCita() + "en ese horario [" + primera.getFechaHora().format(Cita.FORMATO_FECHA_HORA) + "]";
        }
        int citasDelDia = contarCitasDelDia(trabajador, inicio.toLocalDate(), citaIgnorada);
        if(citasDelDia >= trabajador.getCapacidadDiaria()){
            return trabajador.getNombreCompleto() + "ya esta al maximo de capacidad diaria [" + trabajador.getCapacidadDiaria() + "citas]";
        }
        return null;
    }

    public ArrayList<Cita> buscarCitasEnConflicto(TrabajadorMedico trabajador, LocalDateTime inicio, LocalDateTime fin, Cita citaIgnorada){
        ArrayList<Cita> resultado = new ArrayList<Cita>();
        for(int i = 0; i < citas.size(); i++){
            Cita c = citas.get(i);
            if(c == citaIgnorada){
                continue;
            }
            if(c.getTrabajador().equals(trabajador) && c.getEstado().ocupaAgenda() && c.seSolapaCon(inicio, fin)){
                resultado.add(c);
            }
        }
        return resultado;
    }

    public int contarCitasDelDia(TrabajadorMedico trabajador, LocalDate dia, Cita citaIgnorada){
        int contador = 0;
        for(int i = 0; i < citas.size(); i++){
            Cita c = citas.get(i);
            if(c != citaIgnorada && c.getTrabajador(). equals(trabajador) && c.getEstado().ocupaAgenda() && c.getFechaHora().toLocalDate().equals(dia)){
                contador++;
            }
        }
        return contador;
    }

    public int contarMinutosOcupados(TrabajadorMedico trabajador, LocalDate dia){
        int minutos = 0;
        for(int i = 0; i < citas.size(); i++){
            Cita c = citas.get(i);
            if(c.getTrabajador().equals(trabajador) && c.getEstado().ocupaAgenda() && c.getFechaHora().toLocalDate().equals(dia)){
                minutos += c.getTipo().getDuracionMinutos();
            }
        }
        return minutos;
    }

    public TrabajadorMedico buscarTrabajadorDisponible(TipoCita tipo, LocalDateTime inicio){
        return buscarTrabajadorDisponible(tipo, inicio, "");
    }

    public TrabajadorMedico buscarTrabajadorDisponible(TipoCita tipo, LocalDateTime inicio, String departamento){
        TrabajadorMedico mejor = null;
        int menorCarga = Integer.MAX_VALUE;
        ArrayList<TrabajadorMedico> personal = gestorPersonal.getPersonal();
        for(int i = 0; i < personal.size(); i++){
            TrabajadorMedico t = personal.get(i);
            if(!departamento.isEmpty() && !t.getDepartamento().equalsIgnoreCase(departamento)){
                continue;
            }
            if(verificarDisponibilidad(t, tipo, inicio, null) == null){
                int carga = contarCitasDelDia(t, inicio.toLocalDate(), null);
                if(carga < menorCarga){
                    menorCarga = carga;
                    mejor = t;
                }
            }
        }
        return mejor;
    }
    
    private TrabajadorMedico buscarotroTrabajador(Cita cita, LocalDateTime inicio){
        TrabajadorMedico mejor = null;
        int menorCarga = Integer.MAX_VALUE;
        ArrayList<TrabajadorMedico> personal = gestorPersonal.getPersonal();
        for(int i = 0; i < personal.size(); i++){
            TrabajadorMedico t = personal.get(i);
            if(t.equals(cita.getTrabajador())){
                continue;
            }
            if(verificarDisponibilidad(t, cita.getTipo(), inicio, cita) == null){
                int carga = contarCitasDelDia(t, inicio.toLocalDate(), cita);
                if(carga < menorCarga){
                    menorCarga = carga;
                    mejor = t;
                }
            }
        }
        return mejor;
    }

    public Cita programarCita(String paciente, TrabajadorMedico trabajador, LocalDateTime inicio, TipoCita tipo){
        String motivo = verificarDisponibilidad(trabajador, tipo, inicio, null);
        if(motivo != null){
            ultimoError = motivo;
            return null;
        }
        Cita nueva = new Cita(siguienteIdCita, paciente, trabajador, inicio, tipo);
        siguienteIdCita++;
        citas.add(nueva);
        notificador.notificar(paciente, "Su cita #" + nueva.getIdCita() + "[" + tipo + "] a sido programado para el" + inicio.format(Cita.FORMATO_FECHA_HORA) + " con " + trabajador.getNombreCompleto());
        notificador.notificar(trabajador.getNombreCompleto(), "Nueva cita #" + nueva.getIdCita() + " con el paciente " + paciente);
        return nueva;
    }

    public Cita programarCitaAutomatica(String paciente, TipoCita tipo, LocalDateTime inicio, String departamento){
        TrabajadorMedico elegido = buscarTrabajadorDisponible(tipo, inicio, departamento);
        if(elegido == null){
            ultimoError = "No hay personal disponible para la cita de tipo " + tipo + "en el horario";
            return null;
        }
        return programarCita(paciente, elegido, inicio, tipo);
    }

    public Cita agregarCitaUrgente(String paciente, TrabajadorMedico trabajador, LocalDateTime inicio, TipoCita tipo){
        Cita urgente = new Cita(siguienteIdCita, paciente, trabajador, inicio, tipo);
        siguienteIdCita++;
        urgente.setEstado(EstadoCita.CONFIRMADA);
        citas.add(urgente);
        notificador.notificar(paciente, "emergencia: cita # " + urgente.getIdCita() + "confirmar para el " + inicio.format(Cita.FORMATO_FECHA_HORA) + " con " + trabajador.getNombreCompleto());
        return urgente;
    }

    private boolean transicionValida(EstadoCita actual, EstadoCita nuevo){
        if(actual == EstadoCita.PROGRAMADA || actual == EstadoCita.REAGENDADA){
            return nuevo == EstadoCita.CONFIRMADA || nuevo == EstadoCita.CANCELADA;
        }
        if(actual == EstadoCita.CONFIRMADA){
            return nuevo == EstadoCita.EN_PROGRESO || nuevo == EstadoCita.CANCELADA;
        }
        if(actual == EstadoCita.EN_PROGRESO){
            return nuevo == EstadoCita.COMPLETADA;
        }
        return false;
    }

    public boolean cambiarEstado(int idCita, EstadoCita nuevoEstado){
        Cita cita = buscarCitaPorId(idCita);
        if(cita == null){
            ultimoError = "no existe la cita # " + idCita;
            return false;
        }
        if(!transicionValida(cita.getEstado(), nuevoEstado)){
            ultimoError = "No se puede pasar de " + cita.getEstado() + " a " + nuevoEstado;
            return false;
        }
        cita.setEstado(nuevoEstado);
        if(nuevoEstado == EstadoCita.COMPLETADA){
            cita.getTrabajador().registrarAtencion(cita.getTipo(), cita.getTipo().getDuracionMinutos());
        }
        if(nuevoEstado == EstadoCita.CANCELADA){
            notificador.notificar(cita.getNombrePaciente(), "Su cita # " + idCita + "fue cancelada");
        }
        return true;
    }

    public boolean reagendarCita(int idCita, LocalDateTime nuevaFecha, String motivo){
        Cita cita = buscarCitaPorId(idCita);
        if(cita == null){
            ultimoError = "No existe la cita #" + idCita;
            return false;
        }
        return reagendarCita(idCita, nuevaFecha,cita.getTrabajador(), motivo);
    }

    public boolean reagendarCita(int idCita, LocalDateTime nuevaFecha,TrabajadorMedico nuevoTrabajador, String motivo){
        Cita cita = buscarCitaPorId(idCita);
        if(cita == null){
            ultimoError = "No existe la cita #" + idCita;
            return false;
        }
        if(!cita.getEstado().puedeReagendarse()){
            ultimoError = "la cita # " + idCita + " esta " + cita.getEstado() + " y no se puede reagendar";
            return false;
        }
        String motivoError = verificarDisponibilidad(nuevoTrabajador, cita.getTipo(), nuevaFecha, cita);
        if(motivoError != null){
            ultimoError = motivoError;
            return false;
        }
        aplicarReagendamiento(cita, nuevaFecha, nuevoTrabajador, motivo);
        return true;
    }

    private void aplicarReagendamiento(Cita cita, LocalDateTime nuevafecha, TrabajadorMedico nuevoTrabajador, String motivo){
        TrabajadorMedico anterior = cita.getTrabajador();
        LocalDateTime fechaAnteior = cita.getFechaHora();
        if(anterior.equals(nuevoTrabajador)){
            cita.reagendar(nuevafecha, motivo);
        }
        else{
            cita.reagendar(nuevafecha, nuevoTrabajador, motivo);
        }

        if(fechaAnteior.equals(nuevafecha)){
            notificador.notificar(cita.getNombrePaciente(), "Su cita # " + cita.getIdCita() + " del " + nuevafecha.format(Cita.FORMATO_FECHA_HORA) + " sera atendida por " + nuevoTrabajador.getNombreCompleto() + " motivo:" + motivo);
        }
        else{
            notificador.notificar(cita.getNombrePaciente(), "su cita # " + cita.getIdCita() + "paso del " + fechaAnteior.format(Cita.FORMATO_FECHA_HORA) + " al " + nuevafecha.format(Cita.FORMATO_FECHA_HORA) + "con" + nuevoTrabajador.getNombreCompleto() + "motivo:" + motivo); 
        }
        notificador.notificar(nuevoTrabajador.getNombreCompleto(), "se le asigno la cita # " + cita.getIdCita() + "el " + nuevafecha.format(Cita.FORMATO_FECHA_HORA));
        if(!anterior.equals(nuevoTrabajador)){
            notificador.notificar(anterior.getNombreCompleto(), "se le retiro la cita #" + cita.getIdCita());
        }
    }

    public boolean reagendarAutomaticamente(Cita cita, String motivo){
        return reagendarAutomaticamente(cita, cita.getFechaHora(), motivo);
    }

    public boolean reagendarAutomaticamente(Cita cita, LocalDateTime desde, String motivo){
        if(!cita.getEstado().puedeReagendarse()){
            ultimoError = "la cita #" + cita.getIdCita() + " esta " + cita.getEstado() + " y no se puede reagendar";
            return false;
        }
        int pasos = (DIAS_MAXIMOS_BUSQUEDA * 24 * 60) / PASO_BUSQUEDA_MINUTOS;
        for(int i = 0; i <= pasos; i++){
            LocalDateTime candidata = desde.plusMinutes(i * PASO_BUSQUEDA_MINUTOS);
            if(verificarDisponibilidad(cita.getTrabajador(), cita.getTipo(), candidata, cita)== null){
                if(!candidata.equals(cita.getFechaHora())){
                    aplicarReagendamiento(cita, candidata, cita.getTrabajador(), motivo);
                }
                return true;
            }
            TrabajadorMedico otro = buscarotroTrabajador(cita, candidata);
            if(otro != null){
                aplicarReagendamiento(cita, candidata, otro, motivo);
                return true;
            }
        }
        ultimoError = "No se encontro un espacio libre para la cita #" + cita.getIdCita() +" en los proximos " + DIAS_MAXIMOS_BUSQUEDA + " dias";
        return false;
    }

    public ArrayList<Cita> detectarConflictos(){
        ArrayList<Cita> enConflicto = new ArrayList<Cita>();
        for( int i = 0; i < citas.size(); i++){
            Cita a = citas.get(i);
            if(!a.getEstado().ocupaAgenda()){
                continue;
            }
            if(a.getTrabajador().isAusente() && a.getEstado().puedeReagendarse()){
                if(!enConflicto.contains(a)){
                    enConflicto.add(a);
                }
            }
            for(int j = i + 1; j < citas.size(); j++){
                Cita b = citas.get(j);
                if(b.getEstado().ocupaAgenda() && a.getTrabajador().equals(b.getTrabajador()) && a.seSolapaCon(b.getFechaHora(), b.getFechaFin())){
                    if(!enConflicto.contains(b)){
                        enConflicto.add(b);
                    }
                }
            }
        }
        return enConflicto;
    }

    public int resolverConflictos(){
        ArrayList<Cita> conflictos = detectarConflictos();
        int resueltos = 0;
        for(int i = 0; i < conflictos.size(); i++){
            if(reagendarAutomaticamente(conflictos.get(i), "resolver de manra automatica el conflicto")){
                resueltos++;
            }
        }
        return resueltos;
    }

    public int[] redistribuirCitas(TrabajadorMedico trabajador, String motivo){
        int exitosas = 0;
        int fallidas = 0;
        ArrayList<Cita> copia = new ArrayList<Cita>(citas);
        for(int i = 0; i < copia.size(); i++){
            Cita c = copia.get(i);
            if(c.getTrabajador().equals(trabajador) && c.getEstado().puedeReagendarse()){
                if(reagendarAutomaticamente(c, motivo)){
                    exitosas++;
                }
                else{
                    fallidas++;
                    notificador.notificar(c.getNombrePaciente(), "No se encontro espacio para su cita # " + c.getIdCita() + " sera contactado por el hospital");
                }
            }
        }
        return new int[] {
            exitosas, fallidas
        };
    }

    public void cancelarPorMotivo(Cita cita, String motivo){
        cita.setEstado(EstadoCita.CANCELADA);
        notificador.notificar(cita.getNombrePaciente(), "Su cita # " + cita.getIdCita() + " fue cancelada| Motivo:" + motivo);
    }

    public Cita buscarCitaPorId(int idCita){
        for(int i = 0; i < citas.size(); i++){
            if(citas.get(i).getIdCita() == idCita){
                return citas.get(i);
            }
        }
        return null;
    }

    public ArrayList<Cita> buscarPorEstado(EstadoCita estado){
        ArrayList<Cita> resultado = new ArrayList<Cita>();
        for(int i = 0; i < citas.size(); i++){
            if(citas.get(i).getEstado() == estado){
            resultado.add(citas.get(i));
            }
        }
        return resultado;
    }

    public ArrayList<Cita> buscarPorTrabajador(TrabajadorMedico trabajador){
        ArrayList<Cita> resultado = new ArrayList<Cita>();
        for(int i = 0; i < citas.size(); i++){
            if(citas.get(i).getTrabajador().equals(trabajador)){
                resultado.add(citas.get(i));
            }
        }
        return resultado;
    }

    public ArrayList<Cita> buscarConHistorial(){
        ArrayList<Cita> resultado = new ArrayList<Cita>();
        for(int i = 0; i < citas.size(); i++){
            if(citas.get(i).getCantidadReagendamientos() > 0){
                resultado.add(citas.get(i));
            }
        }
        return resultado;
    }

    public ArrayList<Cita> getCitas(){
        return citas;
    }

    public Notificador getNotificador(){
        return notificador;
    }

    public String getUltimoError(){
        return ultimoError;
    }
}