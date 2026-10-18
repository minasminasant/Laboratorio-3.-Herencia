package vista;

import controlador.DatosPrueba;
import controlador.GestorCitas;
import controlador.GestorPersonal;
import controlador.HospitalManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import modelo.Cirujano;
import modelo.Cita;
import modelo.DoctorGeneral;
import modelo.Enfermero;
import modelo.EstadoCita;
import modelo.Farmaceutico;
import modelo.RegistroCambio;
import modelo.Radiologo;
import modelo.Terapeuta;
import modelo.TipoCita;
import modelo.TipoTurno;
import modelo.TrabajadorMedico;

public class VistaConsola {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private HospitalManager manager;
    private GestorPersonal gestorPersonal;
    private GestorCitas gestorCitas;
    private Scanner scanner;

    public VistaConsola(HospitalManager manager) {
        this.manager = manager;
        this.gestorPersonal = manager.getGestorPersonal();
        this.gestorCitas = manager.getGestorCitas();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("==============================================");
        System.out.println("   SISTEMA INTEGRAL DE GESTIÓN HOSPITALARIA");
        System.out.println("==============================================");
        System.out.println("Consejo: use la opción 18 para cargar datos de prueba.");

        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");
            System.out.println();
            switch (opcion) {
                case 1: contratarTrabajador(); break;
                case 2: mostrarReportePersonal(); break;
                case 3: registrarPrescripciones(); break;
                case 4: programarCitaManual(); break;
                case 5: programarCitaAutomatica(); break;
                case 6: cambiarEstadoCita(); break;
                case 7: reagendarCitaManual(); break;
                case 8: mostrarCitasPorEstado(); break;
                case 9: mostrarCitasPorTrabajador(); break;
                case 10: gestionarConflictos(); break;
                case 11: simularCrisisPersonal(); break;
                case 12: reincorporarTrabajador(); break;
                case 13: atenderEmergencia(); break;
                case 14: mostrarNominaPorDepartamento(); break;
                case 15: mostrarAnalisisFinanciero(); break;
                case 16: mostrarAnalisisUtilizacion(); break;
                case 17: mostrarHistorialReagendamientos(); break;
                case 18: cargarDatosPrueba(); break;
                case 0: System.out.println("Gracias por usar el sistema. ¡Hasta pronto!"); break;
                default: System.out.println("Opción inválida. Elija un número del menú.");
            }
            if (opcion != 0) {
                pausar();
            }
        }
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("------------------ MENÚ PRINCIPAL ------------------");
        System.out.println(" PERSONAL");
        System.out.println("   1. Contratar trabajador");
        System.out.println("   2. Reporte de personal");
        System.out.println("   3. Registrar prescripciones (farmacéutico)");
        System.out.println(" CITAS");
        System.out.println("   4. Programar cita (elegir trabajador)");
        System.out.println("   5. Programar cita con asignación inteligente");
        System.out.println("   6. Cambiar estado de una cita");
        System.out.println("   7. Reagendar una cita");
        System.out.println("   8. Reporte de citas por estado");
        System.out.println("   9. Reporte de citas por trabajador");
        System.out.println(" OPERACIONES");
        System.out.println("  10. Gestión de conflictos (detectar y resolver)");
        System.out.println("  11. Crisis de personal (ausencia de un trabajador)");
        System.out.println("  12. Reincorporar trabajador");
        System.out.println("  13. Emergencia quirúrgica");
        System.out.println(" REPORTES");
        System.out.println("  14. Nómina por departamento");
        System.out.println("  15. Análisis financiero");
        System.out.println("  16. Análisis de utilización");
        System.out.println("  17. Historial de reagendamientos");
        System.out.println(" DATOS");
        System.out.println("  18. Cargar datos de prueba");
        System.out.println("   0. Salir");
        System.out.println("----------------------------------------------------");
    }

    private void contratarTrabajador() {
        titulo("CONTRATAR TRABAJADOR");
        System.out.println("Tipos de trabajador:");
        System.out.println("  1. Doctor General");
        System.out.println("  2. Cirujano");
        System.out.println("  3. Enfermero/a");
        System.out.println("  4. Radiólogo");
        System.out.println("  5. Farmacéutico");
        System.out.println("  6. Terapeuta");
        int tipo = leerEnteroEnRango("Tipo: ", 1, 6);

        String nombre = leerTexto("Nombre completo: ");
        String departamento = leerTexto("Departamento: ");
        int anios = leerEnteroEnRango("Años de experiencia: ", 0, 60);
        double salarioBase = leerDouble("Salario base (Q): ");
        int id = gestorPersonal.generarIdEmpleado();

        TrabajadorMedico nuevo = null;
        if (tipo == 1) {
            String especializacion = leerTexto("Especialización: ");
            int capacidad = leerEnteroEnRango("Pacientes por día: ", 1, 40);
            double tarifa = leerDouble("Tarifa por consulta (Q): ");
            nuevo = new DoctorGeneral(id, nombre, departamento, anios, salarioBase, especializacion, capacidad, tarifa);
        } else if (tipo == 2) {
            ArrayList<String> operaciones = leerLista("Tipos de operaciones (separadas por coma): ");
            int horas = leerEnteroEnRango("Horas de cirugía disponibles por día: ", 1, 16);
            double bono = leerDouble("Bono por riesgo (Q): ");
            double tarifaHora = leerDouble("Tarifa por hora de cirugía (Q): ");
            nuevo = new Cirujano(id, nombre, departamento, anios, salarioBase, operaciones, horas, bono, tarifaHora);
        } else if (tipo == 3) {
            int turno = leerEnteroEnRango("Turno (1 = Diurno, 2 = Nocturno): ", 1, 2);
            String certificacion = leerTexto("Nivel de certificación: ");
            TipoTurno tipoTurno = (turno == 1) ? TipoTurno.DIURNO : TipoTurno.NOCTURNO;
            nuevo = new Enfermero(id, nombre, departamento, anios, salarioBase, tipoTurno, certificacion);
        } else if (tipo == 4) {
            ArrayList<String> equipos = leerLista("Equipos certificados (separados por coma): ");
            double tarifaEstudio = leerDouble("Tarifa por estudio (Q): ");
            nuevo = new Radiologo(id, nombre, departamento, anios, salarioBase, equipos, tarifaEstudio);
        } else if (tipo == 5) {
            int limite = leerEnteroEnRango("Límite de prescripciones por día: ", 1, 500);
            boolean licencia = leerSiNo("¿Tiene licencia para sustancias controladas? (s/n): ");
            double comision = leerDouble("Comisión por prescripción (Q): ");
            nuevo = new Farmaceutico(id, nombre, departamento, anios, salarioBase, limite, licencia, comision);
        } else {
            String terapia = leerTexto("Tipo de terapia: ");
            int duracion = leerEnteroEnRango("Duración promedio de sesión (minutos): ", 15, 180);
            double comisionSesion = leerDouble("Comisión por sesión (Q): ");
            nuevo = new Terapeuta(id, nombre, departamento, anios, salarioBase, terapia, duracion, comisionSesion);
        }

        if (gestorPersonal.contratar(nuevo)) {
            System.out.println("\nTrabajador contratado con éxito:");
            System.out.println(nuevo);
        } else {
            System.out.println("No se pudo contratar: el ID ya existe.");
        }
    }

    private void mostrarReportePersonal() {
        titulo("REPORTE DE PERSONAL");
        ArrayList<TrabajadorMedico> personal = gestorPersonal.getPersonal();
        if (personal.isEmpty()) {
            System.out.println("No hay personal registrado todavía.");
            return;
        }
        for (int i = 0; i < personal.size(); i++) {
            System.out.println(personal.get(i));
            System.out.println();
        }
        System.out.println("Total de trabajadores: " + personal.size());
    }

    private void registrarPrescripciones() {
        titulo("REGISTRAR PRESCRIPCIONES");
        mostrarListaCorta(gestorPersonal.getPersonal());
        int id = leerEntero("ID del farmacéutico: ");
        int cantidad = leerEntero("Cantidad de prescripciones despachadas: ");
        String error = manager.registrarPrescripciones(id, cantidad);
        if (error == null) {
            System.out.println("Prescripciones registradas correctamente.");
        } else {
            System.out.println("Error: " + error);
        }
    }

    private void programarCitaManual() {
        titulo("PROGRAMAR CITA");
        if (gestorPersonal.getPersonal().isEmpty()) {
            System.out.println("Primero debe haber personal registrado.");
            return;
        }
        String paciente = leerTexto("Nombre del paciente: ");
        TipoCita tipo = leerTipoCita();

        ArrayList<TrabajadorMedico> candidatos = gestorPersonal.buscarPorTipoCita(tipo);
        if (candidatos.isEmpty()) {
            System.out.println("No hay trabajadores que puedan atender citas de tipo " + tipo + ".");
            return;
        }
        System.out.println("\nTrabajadores que pueden atender " + tipo + ":");
        mostrarListaCorta(candidatos);
        TrabajadorMedico trabajador = leerTrabajadorDe(candidatos);
        LocalDateTime fecha = leerFechaHora("Fecha y hora (dd/MM/yyyy HH:mm): ");

        Cita cita = gestorCitas.programarCita(paciente, trabajador, fecha, tipo);
        if (cita != null) {
            System.out.println("\nCita programada:\n" + cita);
        } else {
            System.out.println("\nNo se pudo programar: " + gestorCitas.getUltimoError());
        }
    }

    private void programarCitaAutomatica() {
        titulo("PROGRAMAR CITA (ASIGNACIÓN INTELIGENTE)");
        String paciente = leerTexto("Nombre del paciente: ");
        TipoCita tipo = leerTipoCita();
        LocalDateTime fecha = leerFechaHora("Fecha y hora (dd/MM/yyyy HH:mm): ");
        System.out.print("Departamento (ENTER para cualquiera): ");
        String departamento = scanner.nextLine().trim();

        Cita cita = gestorCitas.programarCitaAutomatica(paciente, tipo, fecha, departamento);
        if (cita != null) {
            System.out.println("\nEl sistema asignó la cita:\n" + cita);
        } else {
            System.out.println("\nNo se pudo programar: " + gestorCitas.getUltimoError());
        }
    }

    private void cambiarEstadoCita() {
        titulo("CAMBIAR ESTADO DE UNA CITA");
        if (gestorCitas.getCitas().isEmpty()) {
            System.out.println("No hay citas registradas.");
            return;
        }
        int id = leerEntero("ID de la cita: ");
        Cita cita = gestorCitas.buscarCitaPorId(id);
        if (cita == null) {
            System.out.println("No existe la cita #" + id + ".");
            return;
        }
        System.out.println(cita);
        System.out.println("\nNuevo estado:");
        System.out.println("  1. CONFIRMADA");
        System.out.println("  2. EN_PROGRESO");
        System.out.println("  3. COMPLETADA");
        System.out.println("  4. CANCELADA");
        int opcion = leerEnteroEnRango("Opción: ", 1, 4);
        EstadoCita nuevo = EstadoCita.CONFIRMADA;
        if (opcion == 2) {
            nuevo = EstadoCita.EN_PROGRESO;
        } else if (opcion == 3) {
            nuevo = EstadoCita.COMPLETADA;
        } else if (opcion == 4) {
            nuevo = EstadoCita.CANCELADA;
        }

        if (gestorCitas.cambiarEstado(id, nuevo)) {
            System.out.println("\nEstado actualizado:\n" + cita);
        } else {
            System.out.println("\nNo se pudo cambiar: " + gestorCitas.getUltimoError());
        }
    }

    private void reagendarCitaManual() {
        titulo("REAGENDAR CITA");
        if (gestorCitas.getCitas().isEmpty()) {
            System.out.println("No hay citas registradas.");
            return;
        }
        int id = leerEntero("ID de la cita: ");
        Cita cita = gestorCitas.buscarCitaPorId(id);
        if (cita == null) {
            System.out.println("No existe la cita #" + id + ".");
            return;
        }
        System.out.println(cita);
        LocalDateTime nuevaFecha = leerFechaHora("Nueva fecha y hora (dd/MM/yyyy HH:mm): ");
        String motivo = leerTexto("Motivo del cambio: ");

        if (gestorCitas.reagendarCita(id, nuevaFecha, motivo)) {
            System.out.println("\nCita reagendada:\n" + cita);
            return;
        }

        System.out.println("\nNo se pudo reagendar: " + gestorCitas.getUltimoError());
        if (!cita.getEstado().puedeReagendarse()) {
            return;
        }
        if (leerSiNo("¿Desea que el sistema busque el siguiente espacio libre? (s/n): ")) {
            if (gestorCitas.reagendarAutomaticamente(cita, nuevaFecha, motivo)) {
                System.out.println("\nCita reagendada automáticamente:\n" + cita);
            } else {
                System.out.println("\nNo se pudo: " + gestorCitas.getUltimoError());
            }
        }
    }

    private void mostrarCitasPorEstado() {
        titulo("CITAS POR ESTADO");
        if (gestorCitas.getCitas().isEmpty()) {
            System.out.println("No hay citas registradas.");
            return;
        }
        EstadoCita[] estados = EstadoCita.values();
        for (int i = 0; i < estados.length; i++) {
            ArrayList<Cita> lista = gestorCitas.buscarPorEstado(estados[i]);
            System.out.println("--- " + estados[i] + " (" + lista.size() + ") ---");
            for (int j = 0; j < lista.size(); j++) {
                System.out.println("  " + lista.get(j));
            }
        }
        System.out.println("\nTotal de citas: " + gestorCitas.getCitas().size());
    }

    private void mostrarCitasPorTrabajador() {
        titulo("CITAS POR TRABAJADOR");
        boolean hayCitas = false;
        ArrayList<TrabajadorMedico> personal = gestorPersonal.getPersonal();
        for (int i = 0; i < personal.size(); i++) {
            ArrayList<Cita> lista = gestorCitas.buscarPorTrabajador(personal.get(i));
            if (!lista.isEmpty()) {
                hayCitas = true;
                System.out.println("--- " + personal.get(i).getNombreCompleto() + " ("
                        + personal.get(i).getTipoTrabajador() + ") - " + lista.size() + " cita(s) ---");
                for (int j = 0; j < lista.size(); j++) {
                    System.out.println("  " + lista.get(j));
                }
            }
        }
        if (!hayCitas) {
            System.out.println("No hay citas registradas.");
        }
    }

    private void gestionarConflictos() {
        titulo("GESTIÓN DE CONFLICTOS");
        ArrayList<Cita> conflictos = gestorCitas.detectarConflictos();
        if (conflictos.isEmpty()) {
            System.out.println("No se detectaron conflictos de horario.");
            return;
        }
        System.out.println("Se detectaron " + conflictos.size() + " cita(s) en conflicto:");
        for (int i = 0; i < conflictos.size(); i++) {
            System.out.println("  " + conflictos.get(i));
        }
        if (leerSiNo("\n¿Resolver automáticamente? (s/n): ")) {
            int resueltos = gestorCitas.resolverConflictos();
            System.out.println("\nConflictos resueltos: " + resueltos + " de " + conflictos.size());
        }
    }

    private void simularCrisisPersonal() {
        titulo("CRISIS DE PERSONAL");
        ArrayList<TrabajadorMedico> personal = gestorPersonal.getPersonal();
        if (personal.isEmpty()) {
            System.out.println("No hay personal registrado.");
            return;
        }
        mostrarListaCorta(personal);
        TrabajadorMedico trabajador = leerTrabajadorDe(personal);
        if (!leerSiNo("¿Confirma que " + trabajador.getNombreCompleto() + " estará ausente? (s/n): ")) {
            System.out.println("Operación cancelada.");
            return;
        }
        int[] resultado = manager.simularCrisisPersonal(trabajador);
        System.out.println("\n" + trabajador.getNombreCompleto() + " fue marcado como AUSENTE.");
        System.out.println("Citas redistribuidas: " + resultado[0]);
        System.out.println("Citas sin espacio disponible: " + resultado[1]);
    }

    private void reincorporarTrabajador() {
        titulo("REINCORPORAR TRABAJADOR");
        ArrayList<TrabajadorMedico> ausentes = new ArrayList<TrabajadorMedico>();
        for (int i = 0; i < gestorPersonal.getPersonal().size(); i++) {
            if (gestorPersonal.getPersonal().get(i).isAusente()) {
                ausentes.add(gestorPersonal.getPersonal().get(i));
            }
        }
        if (ausentes.isEmpty()) {
            System.out.println("No hay trabajadores ausentes.");
            return;
        }
        mostrarListaCorta(ausentes);
        TrabajadorMedico trabajador = leerTrabajadorDe(ausentes);
        manager.reincorporarTrabajador(trabajador);
        System.out.println(trabajador.getNombreCompleto() + " ya está disponible nuevamente.");
    }

    private void atenderEmergencia() {
        titulo("EMERGENCIA QUIRÚRGICA");
        String paciente = leerTexto("Nombre del paciente: ");
        LocalDateTime fecha = leerFechaHora("Fecha y hora de la cirugía (dd/MM/yyyy HH:mm): ");
        Cita urgente = manager.atenderEmergenciaQuirurgica(paciente, fecha);
        if (urgente == null) {
            System.out.println("\nNo hay ningún cirujano disponible para esa hora.");
        } else {
            System.out.println("\nCirugía de emergencia agendada:\n" + urgente);
            System.out.println("Las citas que chocaban fueron reagendadas automáticamente.");
        }
    }

    private void mostrarNominaPorDepartamento() {
        titulo("NÓMINA POR DEPARTAMENTO");
        ArrayList<String> departamentos = gestorPersonal.obtenerDepartamentos();
        if (departamentos.isEmpty()) {
            System.out.println("No hay personal registrado.");
            return;
        }
        for (int i = 0; i < departamentos.size(); i++) {
            String depto = departamentos.get(i);
            System.out.println("=== " + depto.toUpperCase() + " ===");
            ArrayList<TrabajadorMedico> lista = gestorPersonal.buscarPorDepartamento(depto);
            for (int j = 0; j < lista.size(); j++) {
                TrabajadorMedico t = lista.get(j);
                // POLIMORFISMO: cada tipo calcula su salario con su propia fórmula
                System.out.printf("  %-24s %-14s %s%n", t.getNombreCompleto(), t.getTipoTrabajador(),
                        TrabajadorMedico.formatoMoneda(t.calcularSalario()));
            }
            System.out.println("  Subtotal: " + TrabajadorMedico.formatoMoneda(gestorPersonal.calcularNominaDepartamento(depto)));
            System.out.println();
        }
        System.out.println("NÓMINA TOTAL DEL HOSPITAL: " + TrabajadorMedico.formatoMoneda(gestorPersonal.calcularNominaTotal()));
    }

    private void mostrarAnalisisFinanciero() {
        titulo("ANÁLISIS FINANCIERO");
        ArrayList<String> departamentos = gestorPersonal.obtenerDepartamentos();
        if (departamentos.isEmpty()) {
            System.out.println("No hay personal registrado.");
            return;
        }
        double total = gestorPersonal.calcularNominaTotal();
        String deptoMasCostoso = "";
        double mayorMonto = -1;

        System.out.printf("%-20s %-10s %-16s %-16s %-8s%n", "Departamento", "Personal", "Nómina", "Promedio", "% total");
        for (int i = 0; i < departamentos.size(); i++) {
            String depto = departamentos.get(i);
            double nomina = gestorPersonal.calcularNominaDepartamento(depto);
            int cantidad = gestorPersonal.buscarPorDepartamento(depto).size();
            double promedio = nomina / cantidad;
            double porcentaje = (total > 0) ? (nomina * 100.0 / total) : 0;
            System.out.printf("%-20s %-10d %-16s %-16s %5.1f%%%n", depto, cantidad,
                    TrabajadorMedico.formatoMoneda(nomina), TrabajadorMedico.formatoMoneda(promedio), porcentaje);
            if (nomina > mayorMonto) {
                mayorMonto = nomina;
                deptoMasCostoso = depto;
            }
        }
        System.out.println("\nNómina total: " + TrabajadorMedico.formatoMoneda(total));
        System.out.println("Departamento con mayor costo: " + deptoMasCostoso + " (" + TrabajadorMedico.formatoMoneda(mayorMonto) + ")");
    }

    private void mostrarAnalisisUtilizacion() {
        titulo("ANÁLISIS DE UTILIZACIÓN");
        LocalDate dia = leerFecha("Día a analizar (dd/MM/yyyy, ENTER = mañana): ", LocalDate.now().plusDays(1));
        System.out.println("\nUtilización del día " + dia.format(FORMATO_FECHA) + ":\n");

        ArrayList<TrabajadorMedico> personal = gestorPersonal.getPersonal();
        if (personal.isEmpty()) {
            System.out.println("No hay personal registrado.");
            return;
        }
        System.out.printf("%-24s %-14s %-8s %-9s %-12s%n", "Trabajador", "Tipo", "Citas", "Minutos", "Utilización");
        for (int i = 0; i < personal.size(); i++) {
            TrabajadorMedico t = personal.get(i);
            int citasDia = gestorCitas.contarCitasDelDia(t, dia, null);
            int minutos = gestorCitas.contarMinutosOcupados(t, dia);
            double porcentaje = manager.calcularUtilizacion(t, dia);
            System.out.printf("%-24s %-14s %-8s %-9d %5.1f%% %s%n", t.getNombreCompleto(), t.getTipoTrabajador(),
                    citasDia + "/" + t.getCapacidadDiaria(), minutos, porcentaje, dibujarBarra(porcentaje));
        }
        System.out.println("\nLeyenda: ##### = 100% de la capacidad diaria. Menos de 30% = subutilizado.");
    }

    private String dibujarBarra(double porcentaje) {
        int llenos = (int) Math.round(porcentaje / 10.0);
        if (llenos > 10) {
            llenos = 10;
        }
        String barra = "[";
        for (int i = 0; i < 10; i++) {
            barra += (i < llenos) ? "#" : ".";
        }
        return barra + "]";
    }

    private void mostrarHistorialReagendamientos() {
        titulo("HISTORIAL DE REAGENDAMIENTOS");
        ArrayList<Cita> conHistorial = gestorCitas.buscarConHistorial();
        if (conHistorial.isEmpty()) {
            System.out.println("Ninguna cita ha sido reagendada todavía.");
            return;
        }
        for (int i = 0; i < conHistorial.size(); i++) {
            Cita cita = conHistorial.get(i);
            System.out.println(cita);
            System.out.println("  Reagendamientos: " + cita.getCantidadReagendamientos());
            ArrayList<RegistroCambio> historial = cita.getHistorial();
            for (int j = 0; j < historial.size(); j++) {
                System.out.println("   " + (j + 1) + ") " + historial.get(j));
            }
            System.out.println();
        }
    }

    private void cargarDatosPrueba() {
        titulo("CARGAR DATOS DE PRUEBA");
        if (!gestorPersonal.getPersonal().isEmpty()) {
            System.out.println("Ya hay personal cargado. Reinicie el programa para cargar los datos de prueba.");
            return;
        }
        DatosPrueba datos = new DatosPrueba();
        LocalDate dia = datos.cargar(manager);
        System.out.println("Datos cargados:");
        System.out.println("  - " + gestorPersonal.getPersonal().size() + " trabajadores médicos");
        System.out.println("  - " + gestorCitas.getCitas().size() + " citas para el día " + dia.format(FORMATO_FECHA));
    }

    private void titulo(String texto) {
        System.out.println("=== " + texto + " ===");
    }

    private void pausar() {
        System.out.print("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }

    /** Muestra una línea corta por trabajador: ID, nombre, tipo y departamento. */
    private void mostrarListaCorta(ArrayList<TrabajadorMedico> lista) {
        for (int i = 0; i < lista.size(); i++) {
            TrabajadorMedico t = lista.get(i);
            System.out.println("  ID " + t.getIdEmpleado() + " - " + t.getNombreCompleto()
                    + " (" + t.getTipoTrabajador() + ", " + t.getDepartamento() + ")"
                    + (t.isAusente() ? " [AUSENTE]" : ""));
        }
    }

    private String leerTexto(String mensaje) {
        String texto = "";
        while (texto.isEmpty()) {
            System.out.print(mensaje);
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("  Este dato no puede quedar vacío.");
            }
        }
        return texto;
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("  Escriba un número entero válido.");
            }
        }
    }

    private int leerEnteroEnRango(String mensaje, int minimo, int maximo) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= minimo && valor <= maximo) {
                return valor;
            }
            System.out.println("  El valor debe estar entre " + minimo + " y " + maximo + ".");
        }
    }

    private double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim().replace(',', '.');
            try {
                double valor = Double.parseDouble(texto);
                if (valor >= 0) {
                    return valor;
                }
                System.out.println("  El valor no puede ser negativo.");
            } catch (NumberFormatException e) {
                System.out.println("  Escriba un número válido (ejemplo: 1500.50).");
            }
        }
    }

    private boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim().toLowerCase();
            if (texto.equals("s") || texto.equals("si") || texto.equals("sí")) {
                return true;
            }
            if (texto.equals("n") || texto.equals("no")) {
                return false;
            }
            System.out.println("  Responda con s o n.");
        }
    }

    /** Lee una lista separada por comas y la convierte en ArrayList. */
    private ArrayList<String> leerLista(String mensaje) {
        String texto = leerTexto(mensaje);
        String[] partes = texto.split(",");
        ArrayList<String> lista = new ArrayList<String>();
        for (int i = 0; i < partes.length; i++) {
            if (!partes[i].trim().isEmpty()) {
                lista.add(partes[i].trim());
            }
        }
        return lista;
    }

    private LocalDateTime leerFechaHora(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            try {
                return LocalDateTime.parse(texto, Cita.FORMATO_FECHA_HORA);
            } catch (DateTimeParseException e) {
                System.out.println("  Formato inválido. Ejemplo: 15/03/2026 09:30");
            }
        }
    }

    private LocalDate leerFecha(String mensaje, LocalDate porDefecto) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                return porDefecto;
            }
            try {
                return LocalDate.parse(texto, FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("  Formato inválido. Ejemplo: 15/03/2026");
            }
        }
    }

    private TipoCita leerTipoCita() {
        TipoCita[] tipos = TipoCita.values();
        System.out.println("Tipos de cita:");
        for (int i = 0; i < tipos.length; i++) {
            System.out.println("  " + (i + 1) + ". " + tipos[i] + " (" + tipos[i].getDuracionMinutos() + " min)");
        }
        int opcion = leerEnteroEnRango("Tipo de cita: ", 1, tipos.length);
        return tipos[opcion - 1];
    }

    private TrabajadorMedico leerTrabajadorDe(ArrayList<TrabajadorMedico> lista) {
        while (true) {
            int id = leerEntero("ID del trabajador: ");
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getIdEmpleado() == id) {
                    return lista.get(i);
                }
            }
            System.out.println("  Ese ID no está en la lista mostrada.");
        }
    }
}