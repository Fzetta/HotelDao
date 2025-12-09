package db;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.*;
import modelo.*;

/**
 * Aplicación GUI para el Sistema de Gestión Hotelera
 * Proporciona operaciones CRUD para las entidades principales
 */
public class HotelApp {

    // DAOs
    private PersonaDAO personaDAO;
    private ClienteDAO clienteDAO;
    private HabitacionDAO habitacionDAO;
    private ReservaDAO reservaDAO;
    private ServicioDAO servicioDAO;
    private AreaDAO areaDAO;
    private EmpleadoDAO empleadoDAO;

    // Frame principal
    private final JFrame frame;

    // ==================== PERSONA ====================
    private final JTable personaTable;
    private final DefaultTableModel personaModel;
    private final JTextField personaCedulaField = new JTextField();
    private final JTextField personaPrimerNomField = new JTextField();
    private final JTextField personaSegundoNomField = new JTextField();
    private final JTextField personaPrimerApellField = new JTextField();
    private final JTextField personaSegundoApellField = new JTextField();
    private final JTextField personaCalleField = new JTextField();
    private final JTextField personaCarreraField = new JTextField();
    private final JTextField personaNumeroField = new JTextField();
    private final JTextField personaComplementoField = new JTextField();

    // ==================== CLIENTE ====================
    private final JTable clienteTable;
    private final DefaultTableModel clienteModel;
    private final JTextField clienteCedulaField = new JTextField();
    private final JTextField clientePrimerNomField = new JTextField();
    private final JTextField clienteSegundoNomField = new JTextField();
    private final JTextField clientePrimerApellField = new JTextField();
    private final JTextField clienteSegundoApellField = new JTextField();
    private final JTextField clienteCorreosField = new JTextField();

    // ==================== HABITACION ====================
    private final JTable habitacionTable;
    private final DefaultTableModel habitacionModel;
    private final JTextField habitacionNumeroField = new JTextField();
    private final JTextField habitacionCategoriaField = new JTextField();
    private final JTextField habitacionEstadoField = new JTextField();
    private final JTextField habitacionPrecioField = new JTextField();

    // ==================== RESERVA ====================
    private final JTable reservaTable;
    private final DefaultTableModel reservaModel;
    private final JTextField reservaCedulaField = new JTextField();
    private final JTextField reservaHabitacionField = new JTextField();
    private final JTextField reservaFechaLlegadaField = new JTextField();
    private final JTextField reservaFechaSalidaField = new JTextField();
    private final JTextField reservaTiempoCancelField = new JTextField();

    // ==================== SERVICIO ====================
    private final JTable servicioTable;
    private final DefaultTableModel servicioModel;
    private final JTextField servicioIdField = new JTextField();
    private final JTextField servicioNombreField = new JTextField();
    private final JTextField servicioContenidoField = new JTextField();
    private final JTextField servicioCostoField = new JTextField();

    // ==================== AREA ====================
    private final JTable areaTable;
    private final DefaultTableModel areaModel;
    private final JTextField areaIdField = new JTextField();
    private final JTextField areaNombreField = new JTextField();

    // ==================== EMPLEADO ====================
    private final JTable empleadoTable;
    private final DefaultTableModel empleadoModel;
    private final JTextField empleadoCedulaField = new JTextField();
    private final JTextField empleadoPrimerNomField = new JTextField();
    private final JTextField empleadoSegundoNomField = new JTextField();
    private final JTextField empleadoPrimerApellField = new JTextField();
    private final JTextField empleadoSegundoApellField = new JTextField();
    private final JTextField empleadoCargoField = new JTextField();
    private final JTextField empleadoAreaField = new JTextField();

    public HotelApp() {
        // Inicializar DAOs
        try {
            personaDAO = new PersonaDAO();
            clienteDAO = new ClienteDAO();
            habitacionDAO = new HabitacionDAO();
            reservaDAO = new ReservaDAO();
            servicioDAO = new ServicioDAO();
            areaDAO = new AreaDAO();
            empleadoDAO = new EmpleadoDAO();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage());
            System.exit(1);
        }

        frame = new JFrame("🏨 Sistema de Gestión Hotelera");

        // Inicializar modelos de tablas
        personaModel = new DefaultTableModel(new Object[]{
                "Cédula", "Primer Nom", "Segundo Nom", "Primer Apell", "Segundo Apell", "Dirección"
        }, 0);
        personaTable = new JTable(personaModel);

        clienteModel = new DefaultTableModel(new Object[]{
                "Cédula", "Nombre Completo", "Correos"
        }, 0);
        clienteTable = new JTable(clienteModel);

        habitacionModel = new DefaultTableModel(new Object[]{
                "Número", "Categoría", "Estado", "Precio/Noche"
        }, 0);
        habitacionTable = new JTable(habitacionModel);

        reservaModel = new DefaultTableModel(new Object[]{
                "Cédula Cliente", "Habitación", "Fecha Llegada", "Fecha Salida", "Cancelación (hrs)"
        }, 0);
        reservaTable = new JTable(reservaModel);

        servicioModel = new DefaultTableModel(new Object[]{
                "ID", "Nombre", "Descripción", "Costo"
        }, 0);
        servicioTable = new JTable(servicioModel);

        areaModel = new DefaultTableModel(new Object[]{
                "ID", "Nombre Área"
        }, 0);
        areaTable = new JTable(areaModel);

        empleadoModel = new DefaultTableModel(new Object[]{
                "Cédula", "Nombre", "Cargo", "Área"
        }, 0);
        empleadoTable = new JTable(empleadoModel);

        // Crear pestañas
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("👤 Personas", crearPanelPersona());
        tabs.addTab("🧑‍💼 Clientes", crearPanelCliente());
        tabs.addTab("🛏️ Habitaciones", crearPanelHabitacion());
        tabs.addTab("📅 Reservas", crearPanelReserva());
        tabs.addTab("🛎️ Servicios", crearPanelServicio());
        tabs.addTab("🏢 Áreas", crearPanelArea());
        tabs.addTab("👷 Empleados", crearPanelEmpleado());

        frame.setLayout(new BorderLayout());
        frame.add(tabs, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);

        // Cargar datos iniciales
        refrescarPersonas();
        refrescarClientes();
        refrescarHabitaciones();
        refrescarReservas();
        refrescarServicios();
        refrescarAreas();
        refrescarEmpleados();
    }

    // ==================== PANEL PERSONA ====================
    private JPanel crearPanelPersona() {
        JPanel form = new JPanel(new GridLayout(9, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Cédula:"));
        form.add(personaCedulaField);
        form.add(new JLabel("Primer Nombre:"));
        form.add(personaPrimerNomField);
        form.add(new JLabel("Segundo Nombre:"));
        form.add(personaSegundoNomField);
        form.add(new JLabel("Primer Apellido:"));
        form.add(personaPrimerApellField);
        form.add(new JLabel("Segundo Apellido:"));
        form.add(personaSegundoApellField);
        form.add(new JLabel("Calle:"));
        form.add(personaCalleField);
        form.add(new JLabel("Carrera:"));
        form.add(personaCarreraField);
        form.add(new JLabel("Número:"));
        form.add(personaNumeroField);
        form.add(new JLabel("Complemento:"));
        form.add(personaComplementoField);

        JButton crearBtn = new JButton("Crear");
        JButton actualizarBtn = new JButton("Actualizar");
        JButton eliminarBtn = new JButton("Eliminar");
        JButton refrescarBtn = new JButton("Refrescar");

        crearBtn.addActionListener(e -> { crearPersona(); refrescarPersonas(); });
        actualizarBtn.addActionListener(e -> { actualizarPersona(); refrescarPersonas(); });
        eliminarBtn.addActionListener(e -> { eliminarPersona(); refrescarPersonas(); });
        refrescarBtn.addActionListener(e -> refrescarPersonas());

        // Llenar campos al seleccionar fila
        personaTable.getSelectionModel().addListSelectionListener(e -> {
            int row = personaTable.getSelectedRow();
            if (row >= 0) {
                personaCedulaField.setText(personaModel.getValueAt(row, 0).toString());
                personaPrimerNomField.setText(personaModel.getValueAt(row, 1).toString());
                personaSegundoNomField.setText(personaModel.getValueAt(row, 2).toString());
                personaPrimerApellField.setText(personaModel.getValueAt(row, 3).toString());
                personaSegundoApellField.setText(personaModel.getValueAt(row, 4).toString());
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(crearBtn);
        buttons.add(actualizarBtn);
        buttons.add(eliminarBtn);
        buttons.add(refrescarBtn);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(personaTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private Persona personaFromFields() {
        return new Persona(
                Long.parseLong(personaCedulaField.getText().trim()),
                personaPrimerNomField.getText().trim(),
                personaSegundoNomField.getText().trim(),
                personaPrimerApellField.getText().trim(),
                personaSegundoApellField.getText().trim(),
                personaCalleField.getText().trim(),
                personaCarreraField.getText().trim(),
                personaNumeroField.getText().trim(),
                personaComplementoField.getText().trim()
        );
    }

    private void crearPersona() {
        try {
            if (personaDAO.insert(personaFromFields())) {
                JOptionPane.showMessageDialog(frame, "Persona creada exitosamente");
                limpiarCamposPersona();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void actualizarPersona() {
        try {
            if (personaDAO.update(personaFromFields())) {
                JOptionPane.showMessageDialog(frame, "Persona actualizada exitosamente");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void eliminarPersona() {
        try {
            Long cedula = Long.parseLong(personaCedulaField.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "¿Está seguro de eliminar la persona con cédula " + cedula + "?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (personaDAO.delete(cedula)) {
                    JOptionPane.showMessageDialog(frame, "Persona eliminada exitosamente");
                    limpiarCamposPersona();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void refrescarPersonas() {
        personaModel.setRowCount(0);
        for (Persona p : personaDAO.findAll()) {
            personaModel.addRow(new Object[]{
                    p.getCedulaPer(),
                    p.getPrimerNom(),
                    p.getSegundoNom(),
                    p.getPrimerApell(),
                    p.getSegundoApell(),
                    p.getCalle() + " " + p.getCarrera() + " " + p.getNumero()
            });
        }
    }

    private void limpiarCamposPersona() {
        personaCedulaField.setText("");
        personaPrimerNomField.setText("");
        personaSegundoNomField.setText("");
        personaPrimerApellField.setText("");
        personaSegundoApellField.setText("");
        personaCalleField.setText("");
        personaCarreraField.setText("");
        personaNumeroField.setText("");
        personaComplementoField.setText("");
    }

    // ==================== PANEL CLIENTE ====================
    private JPanel crearPanelCliente() {
        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Cédula:"));
        form.add(clienteCedulaField);
        form.add(new JLabel("Primer Nombre:"));
        form.add(clientePrimerNomField);
        form.add(new JLabel("Segundo Nombre:"));
        form.add(clienteSegundoNomField);
        form.add(new JLabel("Primer Apellido:"));
        form.add(clientePrimerApellField);
        form.add(new JLabel("Segundo Apellido:"));
        form.add(clienteSegundoApellField);
        form.add(new JLabel("Correos (separados por coma):"));
        form.add(clienteCorreosField);

        JButton crearBtn = new JButton("Crear");
        JButton eliminarBtn = new JButton("Eliminar");
        JButton refrescarBtn = new JButton("Refrescar");

        crearBtn.addActionListener(e -> { crearCliente(); refrescarClientes(); });
        eliminarBtn.addActionListener(e -> { eliminarCliente(); refrescarClientes(); });
        refrescarBtn.addActionListener(e -> refrescarClientes());

        clienteTable.getSelectionModel().addListSelectionListener(e -> {
            int row = clienteTable.getSelectedRow();
            if (row >= 0) {
                clienteCedulaField.setText(clienteModel.getValueAt(row, 0).toString());
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(crearBtn);
        buttons.add(eliminarBtn);
        buttons.add(refrescarBtn);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(clienteTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void crearCliente() {
        try {
            String[] correosArr = clienteCorreosField.getText().trim().split(",");
            java.util.List<String> correos = new java.util.ArrayList<>();
            for (String c : correosArr) {
                if (!c.trim().isEmpty()) correos.add(c.trim());
            }

            Cliente cliente = new Cliente(
                    Long.parseLong(clienteCedulaField.getText().trim()),
                    clientePrimerNomField.getText().trim(),
                    clienteSegundoNomField.getText().trim(),
                    clientePrimerApellField.getText().trim(),
                    clienteSegundoApellField.getText().trim(),
                    "", "", "", "",
                    correos
            );

            if (clienteDAO.insertCompleto(cliente)) {
                JOptionPane.showMessageDialog(frame, "Cliente creado exitosamente");
                limpiarCamposCliente();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void eliminarCliente() {
        try {
            Long cedula = Long.parseLong(clienteCedulaField.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "¿Está seguro de eliminar el cliente con cédula " + cedula + "?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (clienteDAO.delete(cedula)) {
                    JOptionPane.showMessageDialog(frame, "Cliente eliminado exitosamente");
                    limpiarCamposCliente();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void refrescarClientes() {
        clienteModel.setRowCount(0);
        for (Cliente c : clienteDAO.findAll()) {
            clienteModel.addRow(new Object[]{
                    c.getCedulaPer(),
                    c.getPrimerNom() + " " + c.getPrimerApell(),
                    c.getCorreos() != null ? String.join(", ", c.getCorreos()) : ""
            });
        }
    }

    private void limpiarCamposCliente() {
        clienteCedulaField.setText("");
        clientePrimerNomField.setText("");
        clienteSegundoNomField.setText("");
        clientePrimerApellField.setText("");
        clienteSegundoApellField.setText("");
        clienteCorreosField.setText("");
    }

    // ==================== PANEL HABITACION ====================
    private JPanel crearPanelHabitacion() {
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Número Habitación:"));
        form.add(habitacionNumeroField);
        form.add(new JLabel("Categoría:"));
        form.add(habitacionCategoriaField);
        form.add(new JLabel("Estado:"));
        form.add(habitacionEstadoField);
        form.add(new JLabel("Precio por Noche:"));
        form.add(habitacionPrecioField);

        JButton crearBtn = new JButton("Crear");
        JButton actualizarBtn = new JButton("Actualizar");
        JButton eliminarBtn = new JButton("Eliminar");
        JButton refrescarBtn = new JButton("Refrescar");

        crearBtn.addActionListener(e -> { crearHabitacion(); refrescarHabitaciones(); });
        actualizarBtn.addActionListener(e -> { actualizarHabitacion(); refrescarHabitaciones(); });
        eliminarBtn.addActionListener(e -> { eliminarHabitacion(); refrescarHabitaciones(); });
        refrescarBtn.addActionListener(e -> refrescarHabitaciones());

        habitacionTable.getSelectionModel().addListSelectionListener(e -> {
            int row = habitacionTable.getSelectedRow();
            if (row >= 0) {
                habitacionNumeroField.setText(habitacionModel.getValueAt(row, 0).toString());
                habitacionCategoriaField.setText(habitacionModel.getValueAt(row, 1).toString());
                habitacionEstadoField.setText(habitacionModel.getValueAt(row, 2).toString());
                habitacionPrecioField.setText(habitacionModel.getValueAt(row, 3).toString());
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(crearBtn);
        buttons.add(actualizarBtn);
        buttons.add(eliminarBtn);
        buttons.add(refrescarBtn);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(habitacionTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void crearHabitacion() {
        try {
            Habitacion hab = new Habitacion(
                    Integer.parseInt(habitacionNumeroField.getText().trim()),
                    habitacionCategoriaField.getText().trim(),
                    habitacionEstadoField.getText().trim(),
                    new BigDecimal(habitacionPrecioField.getText().trim())
            );
            if (habitacionDAO.insert(hab)) {
                JOptionPane.showMessageDialog(frame, "Habitación creada exitosamente");
                limpiarCamposHabitacion();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void actualizarHabitacion() {
        try {
            Habitacion hab = new Habitacion(
                    Integer.parseInt(habitacionNumeroField.getText().trim()),
                    habitacionCategoriaField.getText().trim(),
                    habitacionEstadoField.getText().trim(),
                    new BigDecimal(habitacionPrecioField.getText().trim())
            );
            if (habitacionDAO.update(hab)) {
                JOptionPane.showMessageDialog(frame, "Habitación actualizada exitosamente");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void eliminarHabitacion() {
        try {
            int numero = Integer.parseInt(habitacionNumeroField.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "¿Está seguro de eliminar la habitación " + numero + "?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (habitacionDAO.delete(numero)) {
                    JOptionPane.showMessageDialog(frame, "Habitación eliminada exitosamente");
                    limpiarCamposHabitacion();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void refrescarHabitaciones() {
        habitacionModel.setRowCount(0);
        for (Habitacion h : habitacionDAO.findAll()) {
            habitacionModel.addRow(new Object[]{
                    h.getNumeroHab(),
                    h.getCategoria(),
                    h.getEstadoHab(),
                    h.getPrecioNoche()
            });
        }
    }

    private void limpiarCamposHabitacion() {
        habitacionNumeroField.setText("");
        habitacionCategoriaField.setText("");
        habitacionEstadoField.setText("");
        habitacionPrecioField.setText("");
    }

    // ==================== PANEL RESERVA ====================
    private JPanel crearPanelReserva() {
        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Cédula Cliente:"));
        form.add(reservaCedulaField);
        form.add(new JLabel("Número Habitación:"));
        form.add(reservaHabitacionField);
        form.add(new JLabel("Fecha Llegada (YYYY-MM-DD):"));
        form.add(reservaFechaLlegadaField);
        form.add(new JLabel("Fecha Salida (YYYY-MM-DD):"));
        form.add(reservaFechaSalidaField);
        form.add(new JLabel("Tiempo Cancelación (horas):"));
        form.add(reservaTiempoCancelField);

        JButton crearBtn = new JButton("Crear");
        JButton eliminarBtn = new JButton("Eliminar");
        JButton refrescarBtn = new JButton("Refrescar");

        crearBtn.addActionListener(e -> { crearReserva(); refrescarReservas(); });
        eliminarBtn.addActionListener(e -> { eliminarReserva(); refrescarReservas(); });
        refrescarBtn.addActionListener(e -> refrescarReservas());

        reservaTable.getSelectionModel().addListSelectionListener(e -> {
            int row = reservaTable.getSelectedRow();
            if (row >= 0) {
                reservaCedulaField.setText(reservaModel.getValueAt(row, 0).toString());
                reservaHabitacionField.setText(reservaModel.getValueAt(row, 1).toString());
                reservaFechaLlegadaField.setText(reservaModel.getValueAt(row, 2).toString());
                reservaFechaSalidaField.setText(reservaModel.getValueAt(row, 3).toString());
                reservaTiempoCancelField.setText(reservaModel.getValueAt(row, 4).toString());
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(crearBtn);
        buttons.add(eliminarBtn);
        buttons.add(refrescarBtn);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(reservaTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void crearReserva() {
        try {
            Reserva reserva = new Reserva(
                    Long.parseLong(reservaCedulaField.getText().trim()),
                    Integer.parseInt(reservaHabitacionField.getText().trim()),
                    LocalDate.parse(reservaFechaLlegadaField.getText().trim()),
                    LocalDate.parse(reservaFechaSalidaField.getText().trim()),
                    Integer.parseInt(reservaTiempoCancelField.getText().trim())
            );
            if (reservaDAO.insert(reserva)) {
                JOptionPane.showMessageDialog(frame, "Reserva creada exitosamente");
                limpiarCamposReserva();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void eliminarReserva() {
        try {
            Long cedula = Long.parseLong(reservaCedulaField.getText().trim());
            int habitacion = Integer.parseInt(reservaHabitacionField.getText().trim());
            LocalDate fecha = LocalDate.parse(reservaFechaLlegadaField.getText().trim());

            int confirm = JOptionPane.showConfirmDialog(frame,
                    "¿Está seguro de eliminar esta reserva?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (reservaDAO.delete(cedula, habitacion, fecha)) {
                    JOptionPane.showMessageDialog(frame, "Reserva eliminada exitosamente");
                    limpiarCamposReserva();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void refrescarReservas() {
        reservaModel.setRowCount(0);
        for (Reserva r : reservaDAO.findAll()) {
            reservaModel.addRow(new Object[]{
                    r.getCedulaPer(),
                    r.getNumeroHab(),
                    r.getFechaLlegada(),
                    r.getFechaSalida(),
                    r.getTiempoMaxCancel()
            });
        }
    }

    private void limpiarCamposReserva() {
        reservaCedulaField.setText("");
        reservaHabitacionField.setText("");
        reservaFechaLlegadaField.setText("");
        reservaFechaSalidaField.setText("");
        reservaTiempoCancelField.setText("");
    }

    // ==================== PANEL SERVICIO ====================
    private JPanel crearPanelServicio() {
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("ID Servicio:"));
        form.add(servicioIdField);
        form.add(new JLabel("Nombre:"));
        form.add(servicioNombreField);
        form.add(new JLabel("Descripción:"));
        form.add(servicioContenidoField);
        form.add(new JLabel("Costo:"));
        form.add(servicioCostoField);

        JButton crearBtn = new JButton("Crear");
        JButton actualizarBtn = new JButton("Actualizar");
        JButton eliminarBtn = new JButton("Eliminar");
        JButton refrescarBtn = new JButton("Refrescar");

        crearBtn.addActionListener(e -> { crearServicio(); refrescarServicios(); });
        actualizarBtn.addActionListener(e -> { actualizarServicio(); refrescarServicios(); });
        eliminarBtn.addActionListener(e -> { eliminarServicio(); refrescarServicios(); });
        refrescarBtn.addActionListener(e -> refrescarServicios());

        servicioTable.getSelectionModel().addListSelectionListener(e -> {
            int row = servicioTable.getSelectedRow();
            if (row >= 0) {
                servicioIdField.setText(servicioModel.getValueAt(row, 0).toString());
                servicioNombreField.setText(servicioModel.getValueAt(row, 1).toString());
                servicioContenidoField.setText(servicioModel.getValueAt(row, 2).toString());
                servicioCostoField.setText(servicioModel.getValueAt(row, 3).toString());
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(crearBtn);
        buttons.add(actualizarBtn);
        buttons.add(eliminarBtn);
        buttons.add(refrescarBtn);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(servicioTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void crearServicio() {
        try {
            Servicio servicio = new Servicio(
                    Long.parseLong(servicioIdField.getText().trim()),
                    servicioNombreField.getText().trim(),
                    servicioContenidoField.getText().trim(),
                    new BigDecimal(servicioCostoField.getText().trim())
            );
            if (servicioDAO.insert(servicio)) {
                JOptionPane.showMessageDialog(frame, "Servicio creado exitosamente");
                limpiarCamposServicio();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void actualizarServicio() {
        try {
            Servicio servicio = new Servicio(
                    Long.parseLong(servicioIdField.getText().trim()),
                    servicioNombreField.getText().trim(),
                    servicioContenidoField.getText().trim(),
                    new BigDecimal(servicioCostoField.getText().trim())
            );
            if (servicioDAO.update(servicio)) {
                JOptionPane.showMessageDialog(frame, "Servicio actualizado exitosamente");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void eliminarServicio() {
        try {
            Long id = Long.parseLong(servicioIdField.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "¿Está seguro de eliminar el servicio " + id + "?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (servicioDAO.delete(id)) {
                    JOptionPane.showMessageDialog(frame, "Servicio eliminado exitosamente");
                    limpiarCamposServicio();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void refrescarServicios() {
        servicioModel.setRowCount(0);
        for (Servicio s : servicioDAO.findAll()) {
            servicioModel.addRow(new Object[]{
                    s.getIdServicio(),
                    s.getNomServicio(),
                    s.getContenidoServicio(),
                    s.getCostoServicio()
            });
        }
    }

    private void limpiarCamposServicio() {
        servicioIdField.setText("");
        servicioNombreField.setText("");
        servicioContenidoField.setText("");
        servicioCostoField.setText("");
    }

    // ==================== PANEL AREA ====================
    private JPanel crearPanelArea() {
        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("ID Área:"));
        form.add(areaIdField);
        form.add(new JLabel("Nombre Área:"));
        form.add(areaNombreField);

        JButton crearBtn = new JButton("Crear");
        JButton actualizarBtn = new JButton("Actualizar");
        JButton eliminarBtn = new JButton("Eliminar");
        JButton refrescarBtn = new JButton("Refrescar");

        crearBtn.addActionListener(e -> { crearArea(); refrescarAreas(); });
        actualizarBtn.addActionListener(e -> { actualizarArea(); refrescarAreas(); });
        eliminarBtn.addActionListener(e -> { eliminarArea(); refrescarAreas(); });
        refrescarBtn.addActionListener(e -> refrescarAreas());

        areaTable.getSelectionModel().addListSelectionListener(e -> {
            int row = areaTable.getSelectedRow();
            if (row >= 0) {
                areaIdField.setText(areaModel.getValueAt(row, 0).toString());
                areaNombreField.setText(areaModel.getValueAt(row, 1).toString());
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(crearBtn);
        buttons.add(actualizarBtn);
        buttons.add(eliminarBtn);
        buttons.add(refrescarBtn);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void crearArea() {
        try {
            Area area = new Area(
                    Long.parseLong(areaIdField.getText().trim()),
                    areaNombreField.getText().trim()
            );
            if (areaDAO.insert(area)) {
                JOptionPane.showMessageDialog(frame, "Área creada exitosamente");
                limpiarCamposArea();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void actualizarArea() {
        try {
            Area area = new Area(
                    Long.parseLong(areaIdField.getText().trim()),
                    areaNombreField.getText().trim()
            );
            if (areaDAO.update(area)) {
                JOptionPane.showMessageDialog(frame, "Área actualizada exitosamente");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void eliminarArea() {
        try {
            Long id = Long.parseLong(areaIdField.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "¿Está seguro de eliminar el área " + id + "?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (areaDAO.delete(id)) {
                    JOptionPane.showMessageDialog(frame, "Área eliminada exitosamente");
                    limpiarCamposArea();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void refrescarAreas() {
        areaModel.setRowCount(0);
        for (Area a : areaDAO.findAll()) {
            areaModel.addRow(new Object[]{
                    a.getIdArea(),
                    a.getNombreArea()
            });
        }
    }

    private void limpiarCamposArea() {
        areaIdField.setText("");
        areaNombreField.setText("");
    }

    // ==================== PANEL EMPLEADO ====================
    private JPanel crearPanelEmpleado() {
        JPanel form = new JPanel(new GridLayout(7, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Cédula:"));
        form.add(empleadoCedulaField);
        form.add(new JLabel("Primer Nombre:"));
        form.add(empleadoPrimerNomField);
        form.add(new JLabel("Segundo Nombre:"));
        form.add(empleadoSegundoNomField);
        form.add(new JLabel("Primer Apellido:"));
        form.add(empleadoPrimerApellField);
        form.add(new JLabel("Segundo Apellido:"));
        form.add(empleadoSegundoApellField);
        form.add(new JLabel("Cargo:"));
        form.add(empleadoCargoField);
        form.add(new JLabel("ID Área:"));
        form.add(empleadoAreaField);

        JButton crearBtn = new JButton("Crear");
        JButton actualizarBtn = new JButton("Actualizar");
        JButton eliminarBtn = new JButton("Eliminar");
        JButton refrescarBtn = new JButton("Refrescar");

        crearBtn.addActionListener(e -> { crearEmpleado(); refrescarEmpleados(); });
        actualizarBtn.addActionListener(e -> { actualizarEmpleado(); refrescarEmpleados(); });
        eliminarBtn.addActionListener(e -> { eliminarEmpleado(); refrescarEmpleados(); });
        refrescarBtn.addActionListener(e -> refrescarEmpleados());

        empleadoTable.getSelectionModel().addListSelectionListener(e -> {
            int row = empleadoTable.getSelectedRow();
            if (row >= 0) {
                empleadoCedulaField.setText(empleadoModel.getValueAt(row, 0).toString());
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(crearBtn);
        buttons.add(actualizarBtn);
        buttons.add(eliminarBtn);
        buttons.add(refrescarBtn);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(empleadoTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void crearEmpleado() {
        try {
            Empleado empleado = new Empleado(
                    Long.parseLong(empleadoCedulaField.getText().trim()),
                    empleadoPrimerNomField.getText().trim(),
                    empleadoSegundoNomField.getText().trim(),
                    empleadoPrimerApellField.getText().trim(),
                    empleadoSegundoApellField.getText().trim(),
                    "", "", "", "",
                    empleadoCargoField.getText().trim(),
                    Long.parseLong(empleadoAreaField.getText().trim())
            );
            if (empleadoDAO.insertCompleto(empleado)) {
                JOptionPane.showMessageDialog(frame, "Empleado creado exitosamente");
                limpiarCamposEmpleado();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void actualizarEmpleado() {
        try {
            Empleado empleado = new Empleado(
                    Long.parseLong(empleadoCedulaField.getText().trim()),
                    empleadoPrimerNomField.getText().trim(),
                    empleadoSegundoNomField.getText().trim(),
                    empleadoPrimerApellField.getText().trim(),
                    empleadoSegundoApellField.getText().trim(),
                    "", "", "", "",
                    empleadoCargoField.getText().trim(),
                    Long.parseLong(empleadoAreaField.getText().trim())
            );
            if (empleadoDAO.update(empleado)) {
                JOptionPane.showMessageDialog(frame, "Empleado actualizado exitosamente");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void eliminarEmpleado() {
        try {
            Long cedula = Long.parseLong(empleadoCedulaField.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "¿Está seguro de eliminar el empleado con cédula " + cedula + "?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (empleadoDAO.delete(cedula)) {
                    JOptionPane.showMessageDialog(frame, "Empleado eliminado exitosamente");
                    limpiarCamposEmpleado();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void refrescarEmpleados() {
        empleadoModel.setRowCount(0);
        for (Empleado e : empleadoDAO.findAll()) {
            empleadoModel.addRow(new Object[]{
                    e.getCedulaPer(),
                    e.getPrimerNom() + " " + e.getPrimerApell(),
                    e.getCargo(),
                    e.getIdArea()
            });
        }
    }

    private void limpiarCamposEmpleado() {
        empleadoCedulaField.setText("");
        empleadoPrimerNomField.setText("");
        empleadoSegundoNomField.setText("");
        empleadoPrimerApellField.setText("");
        empleadoSegundoApellField.setText("");
        empleadoCargoField.setText("");
        empleadoAreaField.setText("");
    }

    // ==================== MOSTRAR APLICACIÓN ====================
    public void mostrar() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    public static void main(String[] args) {
        new HotelApp().mostrar();
    }
}