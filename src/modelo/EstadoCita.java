package modelo;

public enum EstadoCita{
    PROGRAMADA,
    CONFIRMADA,
    EN_PROGRESO,
    COMPLETADA,
    CANCELADA,
    REAGENDADA;

    public boolean ocupaAgenda(){
        return this != CANCELADA;
    }

    public boolean puedeReagendarse(){
        return this == PROGRAMADA || this == CONFIRMADA || this == REAGENDADA;
    }
}