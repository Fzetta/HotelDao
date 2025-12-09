package db;

import dao.*;
import modelo.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * Clase principal para probar las operaciones CRUD
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== SISTEMA DE GESTIÓN HOTELERA ===\n");
            
            // Probar operaciones con Persona
            testPersonaDAO();
            
            // Probar operaciones con Cliente
            testClienteDAO();
            
            // Probar operaciones con Habitación
            testHabitacionDAO();
            
            // Probar operaciones con Servicio
            testServicioDAO();
            
            // Probar operaciones con Reserva
            testReservaDAO();
            
            // Probar operaciones con Area
            testAreaDAO();
            
            // Probar operaciones con Empleado
            testEmpleadoDAO();
            
            // Probar operaciones con Teléfono
            testTelefonoPerDAO();
            
            // Probar operaciones con Correo
            testCorreoDAO();
            
            // Probar operaciones con ConsumoAdicional
            testConsumoAdicionalDAO();
            
            // Cerrar conexión
            PostgreSQLConnection.getConnector().closeConnection();
            
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Prueba las operaciones CRUD de PersonaDAO
     */
    private static void testPersonaDAO() throws SQLException {
        System.out.println("--- Pruebas de PersonaDAO ---");
        PersonaDAO personaDAO = new PersonaDAO();
        
        // CREATE - Insertar una nueva persona
        Persona nuevaPersona = new Persona(
            12345678L,
            "Juan",
            "Carlos",
            "Pérez",
            "González",
            "Calle 10",
            "Carrera 20",
            "30-45",
            "Apto 301"
        );
        
        if (personaDAO.insert(nuevaPersona)) {
            System.out.println("✓ Persona insertada exitosamente");
        }
        
        // READ - Buscar persona por ID
        Persona personaEncontrada = personaDAO.findById(12345678L);
        if (personaEncontrada != null) {
            System.out.println("✓ Persona encontrada: " + personaEncontrada);
        }
        
        // UPDATE - Actualizar persona
        nuevaPersona.setSegundoNom("Alberto");
        if (personaDAO.update(nuevaPersona)) {
            System.out.println("✓ Persona actualizada exitosamente");
        }
        
        // READ ALL - Listar todas las personas
        List<Persona> todasPersonas = personaDAO.findAll();
        System.out.println("✓ Total de personas en la base de datos: " + todasPersonas.size());
        
        // Buscar por apellido
        List<Persona> personasPorApellido = personaDAO.findByApellido("Pérez");
        System.out.println("✓ Personas con apellido 'Pérez': " + personasPorApellido.size());
        
        System.out.println();
    }
    
    /**
     * Prueba las operaciones CRUD de AreaDAO
     */
    private static void testAreaDAO() throws SQLException {
        System.out.println("--- Pruebas de AreaDAO ---");
        AreaDAO areaDAO = new AreaDAO();
        
        // CREATE - Insertar una nueva área
        Area nuevaArea = new Area(10L, "Tecnología");
        
        if (areaDAO.insert(nuevaArea)) {
            System.out.println("✓ Área insertada exitosamente");
        }
        
        // READ - Buscar área por ID
        Area areaEncontrada = areaDAO.findById(10L);
        if (areaEncontrada != null) {
            System.out.println("✓ Área encontrada: " + areaEncontrada);
        }
        
        // UPDATE - Actualizar área
        nuevaArea.setNombreArea("Tecnología e Innovación");
        if (areaDAO.update(nuevaArea)) {
            System.out.println("✓ Área actualizada exitosamente");
        }
        
        // READ ALL - Listar todas las áreas
        List<Area> todasAreas = areaDAO.findAll();
        System.out.println("✓ Total de áreas: " + todasAreas.size());
        
        // Buscar por nombre
        List<Area> areasPorNombre = areaDAO.findByNombre("Tecnología");
        System.out.println("✓ Áreas que contienen 'Tecnología': " + areasPorNombre.size());
        
        System.out.println();
    }
    
    /**
     * Prueba las operaciones CRUD de EmpleadoDAO
     */
    private static void testEmpleadoDAO() throws SQLException {
        System.out.println("--- Pruebas de EmpleadoDAO ---");
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();
        
        // CREATE - Insertar un nuevo empleado completo
        Empleado nuevoEmpleado = new Empleado(
            99887766L,
            "Carlos",
            "Eduardo",
            "Martínez",
            "Silva",
            "Calle 80",
            "Carrera 25",
            "15-20",
            "Apto 502",
            "Recepcionista",
            1L  // ID del área
        );
        
        if (empleadoDAO.insertCompleto(nuevoEmpleado)) {
            System.out.println("✓ Empleado insertado exitosamente");
        }
        
        // READ - Buscar empleado por ID
        Empleado empleadoEncontrado = empleadoDAO.findById(99887766L);
        if (empleadoEncontrado != null) {
            System.out.println("✓ Empleado encontrado: " + empleadoEncontrado);
        }
        
        // UPDATE - Actualizar empleado
        nuevoEmpleado.setCargo("Jefe de Recepción");
        if (empleadoDAO.update(nuevoEmpleado)) {
            System.out.println("✓ Empleado actualizado exitosamente");
        }
        
        // Buscar por cargo
        List<Empleado> empleadosPorCargo = empleadoDAO.findByCargo("Recepcionista");
        System.out.println("✓ Empleados con cargo 'Recepcionista': " + empleadosPorCargo.size());
        
        // Buscar por área
        List<Empleado> empleadosPorArea = empleadoDAO.findByArea(1L);
        System.out.println("✓ Empleados en área 1: " + empleadosPorArea.size());
        
        // Obtener empleados con detalles
        List<Empleado> empleadosConDetalles = empleadoDAO.findAllWithDetails();
        System.out.println("✓ Total de empleados con detalles: " + empleadosConDetalles.size());
        
        if (!empleadosConDetalles.isEmpty()) {
            Empleado primerEmpleado = empleadosConDetalles.get(0);
            System.out.println("  Primer empleado:");
            System.out.println("  - Nombre: " + primerEmpleado.getPrimerNom() + " " + 
                             primerEmpleado.getPrimerApell());
            System.out.println("  - Cargo: " + primerEmpleado.getCargo());
            if (primerEmpleado.getArea() != null) {
                System.out.println("  - Área: " + primerEmpleado.getArea().getNombreArea());
            }
        }
        
        System.out.println();
    }
    
    /**
     * Prueba las operaciones CRUD de TelefonoPerDAO
     */
    private static void testTelefonoPerDAO() throws SQLException {
        System.out.println("--- Pruebas de TelefonoPerDAO ---");
        TelefonoPerDAO telefonoDAO = new TelefonoPerDAO();
        
        // INSERT - Insertar teléfonos para una persona
        List<Long> telefonos = Arrays.asList(3001234567L, 3107654321L, 6012345678L);
        
        if (telefonoDAO.insertMultiple(12345678L, telefonos)) {
            System.out.println("✓ Teléfonos insertados exitosamente");
        }
        
        // READ - Buscar teléfonos de una persona
        List<Long> telefonosEncontrados = telefonoDAO.findByPersona(12345678L);
        System.out.println("✓ Teléfonos encontrados: " + telefonosEncontrados.size());
        for (Long tel : telefonosEncontrados) {
            System.out.println("  - " + tel);
        }
        
        // UPDATE - Actualizar teléfonos
        List<Long> nuevosTelefonos = Arrays.asList(3009876543L, 6017654321L);
        if (telefonoDAO.updateTelefonos(12345678L, nuevosTelefonos)) {
            System.out.println("✓ Teléfonos actualizados exitosamente");
        }
        
        // Verificar existencia
        boolean existe = telefonoDAO.exists(12345678L, 3009876543L);
        System.out.println("✓ Teléfono existe: " + existe);
        
        System.out.println();
    }
    
    /**
     * Prueba las operaciones CRUD de CorreoDAO
     */
    private static void testCorreoDAO() throws SQLException {
        System.out.println("--- Pruebas de CorreoDAO ---");
        CorreoDAO correoDAO = new CorreoDAO();
        
        // INSERT - Insertar correos para un cliente
        List<String> correos = Arrays.asList(
            "cliente@email.com",
            "cliente@trabajo.com",
            "cliente@personal.com"
        );
        
        if (correoDAO.insertMultiple(87654321L, correos)) {
            System.out.println("✓ Correos insertados exitosamente");
        }
        
        // READ - Buscar correos de un cliente
        List<String> correosEncontrados = correoDAO.findByCliente(87654321L);
        System.out.println("✓ Correos encontrados: " + correosEncontrados.size());
        for (String correo : correosEncontrados) {
            System.out.println("  - " + correo);
        }
        
        // UPDATE - Actualizar correos
        List<String> nuevosCorreos = Arrays.asList(
            "nuevo@email.com",
            "nuevo@trabajo.com"
        );
        if (correoDAO.updateCorreos(87654321L, nuevosCorreos)) {
            System.out.println("✓ Correos actualizados exitosamente");
        }
        
        // Buscar por dominio
        List<Long> clientesPorDominio = correoDAO.findClientesByDominio("email.com");
        System.out.println("✓ Clientes con correo @email.com: " + clientesPorDominio.size());
        
        System.out.println();
    }
    
    /**
     * Prueba las operaciones CRUD de ConsumoAdicionalDAO
     */
    private static void testConsumoAdicionalDAO() throws SQLException {
        System.out.println("--- Pruebas de ConsumoAdicionalDAO ---");
        ConsumoAdicionalDAO consumoDAO = new ConsumoAdicionalDAO();
        
        // CREATE - Insertar un nuevo consumo
        ConsumoAdicional nuevoConsumo = new ConsumoAdicional(
            LocalDate.now(),
            LocalTime.of(14, 30),
            LocalDate.of(2025, 12, 15),
            101,
            87654321L,
            100L  // ID del servicio Spa Premium
        );
        
        if (consumoDAO.insert(nuevoConsumo)) {
            System.out.println("✓ Consumo adicional insertado exitosamente");
        }
        
        // READ - Buscar consumos por reserva
        List<ConsumoAdicional> consumosReserva = consumoDAO.findByReserva(
            87654321L,
            101,
            LocalDate.of(2025, 12, 15)
        );
        System.out.println("✓ Consumos de la reserva: " + consumosReserva.size());
        
        // Buscar consumos por cliente
        List<ConsumoAdicional> consumosCliente = consumoDAO.findByCliente(87654321L);
        System.out.println("✓ Total de consumos del cliente: " + consumosCliente.size());
        
        // Buscar consumos por servicio
        List<ConsumoAdicional> consumosServicio = consumoDAO.findByServicio(100L);
        System.out.println("✓ Consumos del servicio Spa: " + consumosServicio.size());
        
        // Obtener consumos con detalles
        List<ConsumoAdicional> consumosConDetalles = consumoDAO.findAllWithDetails();
        System.out.println("✓ Total de consumos con detalles: " + consumosConDetalles.size());
        
        if (!consumosConDetalles.isEmpty()) {
            ConsumoAdicional primerConsumo = consumosConDetalles.get(0);
            System.out.println("  Primer consumo:");
            System.out.println("  - Fecha: " + primerConsumo.getFechaConsumo());
            System.out.println("  - Hora: " + primerConsumo.getHoraConsumo());
            if (primerConsumo.getServicio() != null) {
                System.out.println("  - Servicio: " + primerConsumo.getServicio().getNomServicio());
                System.out.println("  - Costo: $" + primerConsumo.getServicio().getCostoServicio());
            }
        }
        
        // Calcular total de consumos de una reserva
        double total = consumoDAO.calcularTotalReserva(
            87654321L,
            101,
            LocalDate.of(2025, 12, 15)
        );
        System.out.println("✓ Total de consumos de la reserva: $" + total);
        
        // Obtener estadísticas
        List<Object[]> estadisticas = consumoDAO.getEstadisticasConsumo();
        System.out.println("✓ Estadísticas de consumo:");
        for (Object[] stat : estadisticas) {
            System.out.println("  - " + stat[1] + ": " + stat[2] + 
                             " consumos, Total: $" + stat[3]);
        }
        
        System.out.println();
    }
    
    /**
     * Prueba las operaciones CRUD de ClienteDAO
     */
    private static void testClienteDAO() throws SQLException {
        System.out.println("--- Pruebas de ClienteDAO ---");
        ClienteDAO clienteDAO = new ClienteDAO();
        
        // CREATE - Insertar un nuevo cliente completo
        Cliente nuevoCliente = new Cliente(
            87654321L,
            "María",
            "Fernanda",
            "Rodríguez",
            "López",
            "Calle 50",
            "Carrera 15",
            "25-30",
            null,
            Arrays.asList("maria.rodriguez@email.com", "mrodriguez@trabajo.com")
        );
        
        if (clienteDAO.insertCompleto(nuevoCliente)) {
            System.out.println("✓ Cliente insertado exitosamente");
        }
        
        // READ - Buscar cliente por ID
        Cliente clienteEncontrado = clienteDAO.findById(87654321L);
        if (clienteEncontrado != null) {
            System.out.println("✓ Cliente encontrado: " + clienteEncontrado);
            System.out.println("  Correos: " + clienteEncontrado.getCorreos());
        }
        
        // READ ALL - Listar todos los clientes
        List<Cliente> todosClientes = clienteDAO.findAll();
        System.out.println("✓ Total de clientes en la base de datos: " + todosClientes.size());
        
        System.out.println();
    }
    
    /**
     * Prueba las operaciones CRUD de HabitacionDAO
     */
    private static void testHabitacionDAO() throws SQLException {
        System.out.println("--- Pruebas de HabitacionDAO ---");
        HabitacionDAO habitacionDAO = new HabitacionDAO();
        
        // CREATE - Insertar una nueva habitación
        Habitacion nuevaHabitacion = new Habitacion(
            101,
            "Suite",
            "Disponible",
            new BigDecimal("250000")
        );
        
        if (habitacionDAO.insert(nuevaHabitacion)) {
            System.out.println("✓ Habitación insertada exitosamente");
        }
        
        // READ - Buscar habitación por número
        Habitacion habitacionEncontrada = habitacionDAO.findById(101);
        if (habitacionEncontrada != null) {
            System.out.println("✓ Habitación encontrada: " + habitacionEncontrada);
        }
        
        // UPDATE - Actualizar estado de la habitación
        if (habitacionDAO.updateEstado(101, "No disponible")) {
            System.out.println("✓ Estado de habitación actualizado");
        }
        
        // Buscar habitaciones disponibles
        List<Habitacion> habitacionesDisponibles = habitacionDAO.findDisponibles();
        System.out.println("✓ Habitaciones disponibles: " + habitacionesDisponibles.size());
        
        // Buscar por categoría
        List<Habitacion> suites = habitacionDAO.findByCategoria("Suite");
        System.out.println("✓ Total de Suites: " + suites.size());
        
        System.out.println();
    }
    
    /**
     * Prueba las operaciones CRUD de ServicioDAO
     */
    private static void testServicioDAO() throws SQLException {
        System.out.println("--- Pruebas de ServicioDAO ---");
        ServicioDAO servicioDAO = new ServicioDAO();
        
        // CREATE - Insertar un nuevo servicio
        Servicio nuevoServicio = new Servicio(
            100L,
            "Spa Premium",
            "Masajes relajantes y terapias corporales",
            new BigDecimal("150000")
        );
        
        if (servicioDAO.insert(nuevoServicio)) {
            System.out.println("✓ Servicio insertado exitosamente");
        }
        
        // READ - Buscar servicio por ID
        Servicio servicioEncontrado = servicioDAO.findById(100L);
        if (servicioEncontrado != null) {
            System.out.println("✓ Servicio encontrado: " + servicioEncontrado);
        }
        
        // UPDATE - Actualizar servicio
        nuevoServicio.setCostoServicio(new BigDecimal("180000"));
        if (servicioDAO.update(nuevoServicio)) {
            System.out.println("✓ Servicio actualizado exitosamente");
        }
        
        // READ ALL - Listar todos los servicios
        List<Servicio> todosServicios = servicioDAO.findAll();
        System.out.println("✓ Total de servicios: " + todosServicios.size());
        
        // Buscar por nombre
        List<Servicio> serviciosPorNombre = servicioDAO.findByNombre("Spa");
        System.out.println("✓ Servicios que contienen 'Spa': " + serviciosPorNombre.size());
        
        System.out.println();
    }
    
    /**
     * Prueba las operaciones CRUD de ReservaDAO
     */
    private static void testReservaDAO() throws SQLException {
        System.out.println("--- Pruebas de ReservaDAO ---");
        ReservaDAO reservaDAO = new ReservaDAO();
        
        // CREATE - Insertar una nueva reserva
        Reserva nuevaReserva = new Reserva(
            87654321L,  // Cliente creado anteriormente
            101,        // Habitación creada anteriormente
            LocalDate.of(2025, 12, 15),
            LocalDate.of(2025, 12, 20),
            48
        );
        
        if (reservaDAO.insert(nuevaReserva)) {
            System.out.println("✓ Reserva insertada exitosamente");
        }
        
        // READ - Buscar reserva específica
        Reserva reservaEncontrada = reservaDAO.findById(
            87654321L,
            101,
            LocalDate.of(2025, 12, 15)
        );
        if (reservaEncontrada != null) {
            System.out.println("✓ Reserva encontrada: " + reservaEncontrada);
        }
        
        // UPDATE - Actualizar fechas de la reserva
        if (reservaDAO.updateFechas(
            87654321L,
            101,
            LocalDate.of(2025, 12, 15),
            LocalDate.of(2025, 12, 22),
            72
        )) {
            System.out.println("✓ Fechas de reserva actualizadas");
        }
        
        // Buscar reservas por cliente
        List<Reserva> reservasCliente = reservaDAO.findByCliente(87654321L);
        System.out.println("✓ Reservas del cliente: " + reservasCliente.size());
        
        // Buscar reservas activas
        List<Reserva> reservasActivas = reservaDAO.findReservasActivas();
        System.out.println("✓ Reservas activas: " + reservasActivas.size());
        
        // Obtener reservas con detalles completos
        List<Reserva> reservasConDetalles = reservaDAO.findAllWithDetails();
        System.out.println("✓ Total de reservas con detalles: " + reservasConDetalles.size());
        
        if (!reservasConDetalles.isEmpty()) {
            Reserva primeraReserva = reservasConDetalles.get(0);
            System.out.println("  Primera reserva con detalles:");
            System.out.println("  - Cliente: " + primeraReserva.getCliente().getPrimerNom() + 
                             " " + primeraReserva.getCliente().getPrimerApell());
            System.out.println("  - Habitación: " + primeraReserva.getHabitacion().getNumeroHab() + 
                             " (" + primeraReserva.getHabitacion().getCategoria() + ")");
            System.out.println("  - Precio por noche: $" + 
                             primeraReserva.getHabitacion().getPrecioNoche());
        }
        
        System.out.println();
    }
}