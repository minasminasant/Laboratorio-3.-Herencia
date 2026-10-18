package controlador;

import java.util.ArrayList;
import modelo.TipoCita;
import modelo.TrabajadorMedico;

public class GestorPersonal{
    private ArrayList<TrabajadorMedico> personal;
    private int siguienteIdEmpleado;

    public GestorPersonal(){
        this.personal = new ArrayList<TrabajadorMedico>();
        this.siguienteIdEmpleado = 1;
    }

    public int generarIdEmpleado(){
        int id = siguienteIdEmpleado;
        siguienteIdEmpleado++;
        return id;
    }

    public boolean contratar(TrabajadorMedico trabajador){
        if(buscarPorId(trabajador.getIdEmpleado()) != null){
            return false;
        }
        personal.add(trabajador);
        return true;
    }

    public TrabajadorMedico buscarPorId(int idEmpleado){
        for(int i = 0; i < personal.size(); i++){
            if(personal.get(i).getIdEmpleado() == idEmpleado){
                return personal.get(i);
            }
        }
        return null;
    }

    public ArrayList<TrabajadorMedico> buscarPorTipoCita(TipoCita tipo){
        ArrayList<TrabajadorMedico> resultado = new ArrayList<TrabajadorMedico>();
        for(int i = 0; i < personal.size(); i++){
            if(personal.get(i).puedeAtender(tipo)){
            resultado.add(personal.get(i));
            }
        }
        return resultado;
    }

    public ArrayList<String> obtenerDepartamentos(){
        ArrayList<String> departamentos = new ArrayList<String>();
        for(int i = 0; i < personal.size(); i++){
            String depto = personal.get(i).getDepartamento();
            boolean yaEsta = false;
            for(int j = 0; j < departamentos.size(); j++){
                if(departamentos.get(j).equalsIgnoreCase(depto)){
                    yaEsta = true;
                }
            }
            if(!yaEsta){
                departamentos.add(depto);
            }
        }
        return departamentos;
    }

    public ArrayList<TrabajadorMedico> buscarPorDepartamento(String departamento){
        ArrayList<TrabajadorMedico> resultado = new ArrayList<TrabajadorMedico>();
        for(int i = 0; i < personal.size(); i++){
            if(personal.get(i).getDepartamento().equalsIgnoreCase(departamento)){
                resultado.add(personal.get(i));
            }
        }
        return resultado;
    }

    public double calcularNominaDepartamento(String departamento){
        double total = 0;
        ArrayList<TrabajadorMedico> delDepartamento = buscarPorDepartamento(departamento);
        for(int i = 0; i < delDepartamento.size(); i++){
            total += delDepartamento.get(i).calcularSalario();
        }
        return total;
    }

    public double calcularNominaTotal(){
        double total = 0;
        for(int i = 0; i < personal.size(); i++){
            total += personal.get(i).calcularSalario();
        }
        return total;
    }

    public ArrayList<TrabajadorMedico> getPersonal(){
        return personal;
    }
}