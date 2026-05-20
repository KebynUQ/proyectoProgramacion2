package uniquindio.edu.co.eventos.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Notificacion;
import uniquindio.edu.co.eventos.model.Recinto;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.Zona;
import uniquindio.edu.co.eventos.model.enums.EstadoAsiento;
import uniquindio.edu.co.eventos.model.enums.EstadoEvento;
import uniquindio.edu.co.eventos.model.enums.TipoIncidencia;
import uniquindio.edu.co.eventos.model.enums.TipoNotificacion;
import uniquindio.edu.co.eventos.patterns.behavioral.ObservableEvento;
import uniquindio.edu.co.eventos.patterns.behavioral.ObservadorUsuario;
import uniquindio.edu.co.eventos.patterns.creational.ConciertoFactory;
import uniquindio.edu.co.eventos.patterns.creational.ConferenciaFactory;
import uniquindio.edu.co.eventos.patterns.creational.EventoFactory;
import uniquindio.edu.co.eventos.patterns.creational.TeatroFactory;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventosController {

    private static final List<String> CATEGORIAS_EVENTO = List.of(
            "Concierto",
            "Teatro",
            "Conferencia"
    );

    private static final String CATEGORIA_TODAS = "Todas";

    @FXML
    private Label lblTituloVista;

    @FXML
    private TextField txtBuscarEvento;

    @FXML
    private ComboBox<String> cmbCategoriaFiltro;

    @FXML
    private TextField txtCiudadFiltro;

    @FXML
    private DatePicker dpFechaFiltro;

    @FXML
    private TextField txtPrecioFiltro;

    @FXML
    private VBox panelAdministracion;

    @FXML
    private TextField txtNombreEvento;

    @FXML
    private ComboBox<String> cmbCategoriaFormulario;

    @FXML
    private TextField txtCiudadEvento;

    @FXML
    private TextField txtDescripcionEvento;

    @FXML
    private TextField txtPrecioBase;

    @FXML
    private DatePicker datePickerFechaEvento;

    @FXML
    private ComboBox<Integer> cmbHoraEvento;

    @FXML
    private ComboBox<Integer> cmbMinutosEvento;

    @FXML
    private ComboBox<Recinto> cmbRecintoEvento;

    @FXML
    private HBox boxAccionesAdmin;

    @FXML
    private Button btnCrearEvento;

    @FXML
    private Button btnActualizarEvento;

    @FXML
    private Button btnEliminarEvento;

    @FXML
    private Button btnPublicarEvento;

    @FXML
    private Button btnPausarEvento;

    @FXML
    private Button btnCancelarEvento;

    @FXML
    private Button btnLimpiarFormulario;

    @FXML
    private TableView<Evento> tablaEventos;

    @FXML
    private TableColumn<Evento, String> colNombre;

    @FXML
    private TableColumn<Evento, String> colCategoria;

    @FXML
    private TableColumn<Evento, String> colCiudad;

    @FXML
    private TableColumn<Evento, Object> colFecha;

    @FXML
    private TableColumn<Evento, Object> colPrecio;

    @FXML
    private TableColumn<Evento, String> colEstado;

    @FXML
    private Button btnVerDetalle;

    @FXML
    private Button btnComprarEntrada;

    @FXML
    private HBox boxAccionesTabla;

    @FXML
    private ScrollPane scrollEventosCards;

    @FXML
    private FlowPane flowEventosCards;

    @FXML
    private Label lblMensaje;

    private final SistemaEventos sistemaEventos = SistemaEventos.getInstancia();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colCategoria.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategoria()));
        colCiudad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCiudad()));
        colFecha.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFechaHora()));
        colPrecio.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPrecioBase()));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstadoEvento().name()));

        tablaEventos.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (Sesion.esAdministrador()) {
                cargarFormularioDesdeEvento(newValue);
            }
        });

        cmbRecintoEvento.setItems(FXCollections.observableArrayList(sistemaEventos.getRecintos()));
        cargarCategoriasFijas();
        cargarOpcionesHora();
        configurarCamposNumericos();
        configurarVistaSegunRol();
        aplicarFiltroYMostrar();
    }

    @FXML
    private void filtrarEventos() {
        if (obtenerPrecioMaximoFiltro() == null && txtPrecioFiltro.getText() != null && !txtPrecioFiltro.getText().trim().isBlank()) {
            return;
        }

        aplicarFiltroYMostrar();
    }

    @FXML
    private void limpiarFiltros() {
        txtBuscarEvento.clear();
        txtCiudadFiltro.clear();
        cmbCategoriaFiltro.setValue(CATEGORIA_TODAS);
        dpFechaFiltro.setValue(null);
        txtPrecioFiltro.clear();
        aplicarFiltroYMostrar();
        lblMensaje.setText("");
    }

    @FXML
    private void verDetalleEvento() {
        Evento evento = tablaEventos.getSelectionModel().getSelectedItem();

        if (evento == null) {
            lblMensaje.setText("Seleccione un evento.");
            return;
        }

        mostrarDetalleEvento(evento);
    }

    @FXML
    private void comprarEvento() {
        Evento evento = tablaEventos.getSelectionModel().getSelectedItem();

        if (evento == null) {
            lblMensaje.setText("Seleccione un evento para comprar.");
            return;
        }

        seleccionarEventoParaCompra(evento);
    }

    @FXML
    private void crearEvento() {
        if (!validarAdministrador()) {
            return;
        }

        Evento evento = construirEventoDesdeFormulario(null);

        if (evento == null) {
            return;
        }

        sistemaEventos.agregarEvento(evento);
        aplicarFiltroYMostrar();
        limpiarFormulario();
        lblMensaje.setText("Evento creado correctamente.");
    }

    @FXML
    private void actualizarEvento() {
        if (!validarAdministrador()) {
            return;
        }

        Evento seleccionado = tablaEventos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            lblMensaje.setText("Seleccione un evento para actualizar.");
            return;
        }

        Evento actualizado = construirEventoDesdeFormulario(seleccionado);

        if (actualizado == null) {
            return;
        }

        aplicarFiltroYMostrar();
        tablaEventos.getSelectionModel().select(actualizado);
        lblMensaje.setText("Evento actualizado correctamente.");
    }

    @FXML
    private void eliminarEvento() {
        if (!validarAdministrador()) {
            return;
        }

        Evento evento = tablaEventos.getSelectionModel().getSelectedItem();

        if (evento == null) {
            lblMensaje.setText("Seleccione un evento para eliminar.");
            return;
        }

        sistemaEventos.getEventos().remove(evento);
        aplicarFiltroYMostrar();
        limpiarFormulario();
        lblMensaje.setText("Evento eliminado correctamente.");
        sistemaEventos.registrarIncidencia(TipoIncidencia.CAMBIO_ESTADO_EVENTO, "Se elimino el evento " + evento.getNombre());
    }

    @FXML
    private void publicarEvento() {
        cambiarEstadoSeleccionado(EstadoEvento.PUBLICADO);
    }

    @FXML
    private void pausarEvento() {
        cambiarEstadoSeleccionado(EstadoEvento.PAUSADO);
    }

    @FXML
    private void cancelarEvento() {
        cambiarEstadoSeleccionado(EstadoEvento.CANCELADO);
    }

    @FXML
    private void limpiarFormulario() {
        txtNombreEvento.clear();
        cmbCategoriaFormulario.setValue(null);
        txtCiudadEvento.clear();
        txtDescripcionEvento.clear();
        txtPrecioBase.clear();
        datePickerFechaEvento.setValue(null);
        cmbHoraEvento.setValue(18);
        cmbMinutosEvento.setValue(0);
        cmbRecintoEvento.setValue(null);
        tablaEventos.getSelectionModel().clearSelection();
        lblMensaje.setText("");
    }

    private void cambiarEstadoSeleccionado(EstadoEvento estado) {
        if (!validarAdministrador()) {
            return;
        }

        Evento evento = tablaEventos.getSelectionModel().getSelectedItem();

        if (evento == null) {
            lblMensaje.setText("Seleccione un evento.");
            return;
        }

        evento.cambiarEstado(estado);
        tablaEventos.refresh();
        lblMensaje.setText("Estado actualizado a " + estado + ".");
        if (estado == EstadoEvento.CANCELADO) {
            sistemaEventos.registrarIncidencia(TipoIncidencia.CANCELACION_EVENTO, "Se cancelo el evento " + evento.getNombre());
        } else {
            sistemaEventos.registrarIncidencia(TipoIncidencia.CAMBIO_ESTADO_EVENTO, "El evento " + evento.getNombre() + " cambio a " + estado);
        }
        notificarCambioEstadoEvento(evento, estado);
    }

    private void cargarEventos() {
        tablaEventos.setItems(FXCollections.observableArrayList(obtenerEventosVisibles()));
    }

    private ArrayList<Evento> obtenerEventosVisibles() {
        ArrayList<Evento> eventos = new ArrayList<>();

        for (Evento evento : sistemaEventos.getEventos()) {
            if (Sesion.esAdministrador() || evento.getEstadoEvento() == EstadoEvento.PUBLICADO) {
                eventos.add(evento);
            }
        }

        return eventos;
    }

    private void configurarVistaSegunRol() {
        if (Sesion.esAdministrador()) {
            mostrarVistaAdministrador();
        } else {
            mostrarVistaUsuario();
        }
    }

    private void cargarFormularioDesdeEvento(Evento evento) {
        if (evento == null) {
            return;
        }

        txtNombreEvento.setText(evento.getNombre());
        cmbCategoriaFormulario.setValue(evento.getCategoria());
        txtCiudadEvento.setText(evento.getCiudad());
        txtDescripcionEvento.setText(evento.getDescripcion());
        txtPrecioBase.setText(String.valueOf(evento.getPrecioBase()));
        if (evento.getFechaHora() != null) {
            datePickerFechaEvento.setValue(evento.getFechaHora().toLocalDate());
            cmbHoraEvento.setValue(evento.getFechaHora().getHour());
            int minutosEvento = evento.getFechaHora().getMinute();
            if (!cmbMinutosEvento.getItems().contains(minutosEvento)) {
                cmbMinutosEvento.getItems().add(minutosEvento);
                FXCollections.sort(cmbMinutosEvento.getItems());
            }
            cmbMinutosEvento.setValue(minutosEvento);
        } else {
            datePickerFechaEvento.setValue(null);
            cmbHoraEvento.setValue(18);
            cmbMinutosEvento.setValue(0);
        }
        cmbRecintoEvento.setValue(evento.getRecinto());
    }

    private Evento construirEventoDesdeFormulario(Evento eventoExistente) {
        String nombre = txtNombreEvento.getText() == null ? "" : txtNombreEvento.getText().trim();
        String categoria = cmbCategoriaFormulario.getValue();
        String ciudad = txtCiudadEvento.getText() == null ? "" : txtCiudadEvento.getText().trim();
        String descripcion = txtDescripcionEvento.getText() == null ? "" : txtDescripcionEvento.getText().trim();
        String precioTexto = txtPrecioBase.getText() == null ? "" : txtPrecioBase.getText().trim();
        LocalDate fecha = datePickerFechaEvento.getValue();
        Integer hora = cmbHoraEvento.getValue();
        Integer minutos = cmbMinutosEvento.getValue();
        Recinto recinto = cmbRecintoEvento.getValue();

        if (nombre.isBlank()) {
            lblMensaje.setText("Debe ingresar el nombre del evento.");
            return null;
        }

        if (categoria == null || categoria.isBlank()) {
            lblMensaje.setText("Debe seleccionar una categoria.");
            return null;
        }

        if (ciudad.isBlank()) {
            lblMensaje.setText("Debe ingresar la ciudad del evento.");
            return null;
        }

        if (precioTexto.isBlank()) {
            lblMensaje.setText("El precio base no puede estar vacio.");
            return null;
        }

        if (descripcion.isBlank()) {
            lblMensaje.setText("Debe ingresar la descripcion del evento.");
            return null;
        }

        if (recinto == null) {
            lblMensaje.setText("Debe seleccionar un recinto.");
            return null;
        }

        if (fecha == null) {
            lblMensaje.setText("Debe seleccionar una fecha.");
            return null;
        }

        if (hora == null) {
            lblMensaje.setText("Debe seleccionar una hora.");
            return null;
        }

        if (minutos == null) {
            lblMensaje.setText("Debe seleccionar los minutos.");
            return null;
        }

        double precioBase;
        try {
            precioBase = Double.parseDouble(precioTexto);
        } catch (NumberFormatException e) {
            lblMensaje.setText("El precio base debe ser un numero valido.");
            return null;
        }

        if (precioBase <= 0) {
            lblMensaje.setText("El precio base debe ser mayor que cero.");
            return null;
        }

        LocalDateTime fechaHora = LocalDateTime.of(fecha, LocalTime.of(hora, minutos));

        if (fechaHora.isBefore(LocalDateTime.now())) {
            lblMensaje.setText("No puede crear o actualizar un evento con fecha anterior a la actual.");
            return null;
        }

        Evento evento = eventoExistente == null
                ? crearEventoConFactory(nombre, categoria, descripcion, ciudad, fechaHora, recinto, precioBase)
                : eventoExistente;

        evento.setNombre(nombre);
        evento.setCategoria(categoria);
        evento.setCiudad(ciudad);
        evento.setDescripcion(descripcion);
        evento.setPrecioBase(precioBase);
        evento.setFechaHora(fechaHora);
        evento.setRecinto(recinto);

        return evento;
    }

    private Evento crearEventoConFactory(String nombre, String categoria, String descripcion, String ciudad,
                                         LocalDateTime fechaHora, Recinto recinto, double precioBase) {
        EventoFactory factory;
        if ("Teatro".equalsIgnoreCase(categoria)) {
            factory = new TeatroFactory();
        } else if ("Conferencia".equalsIgnoreCase(categoria)) {
            factory = new ConferenciaFactory();
        } else {
            factory = new ConciertoFactory();
        }

        return factory.crearEvento(
                "EVE-" + System.currentTimeMillis(),
                nombre,
                descripcion,
                ciudad,
                fechaHora,
                recinto,
                precioBase
        );
    }

    private boolean validarAdministrador() {
        if (!Sesion.esAdministrador()) {
            lblMensaje.setText("Acceso denegado. Esta opcion es solo para administradores.");
            return false;
        }
        return true;
    }

    private void cargarOpcionesHora() {
        ArrayList<Integer> horas = new ArrayList<>();
        ArrayList<Integer> minutos = new ArrayList<>();

        for (int i = 0; i <= 23; i++) {
            horas.add(i);
        }

        for (int i = 0; i <= 55; i += 5) {
            minutos.add(i);
        }

        cmbHoraEvento.setItems(FXCollections.observableArrayList(horas));
        cmbMinutosEvento.setItems(FXCollections.observableArrayList(minutos));
        cmbHoraEvento.setValue(18);
        cmbMinutosEvento.setValue(0);
    }

    private void cargarCategoriasFijas() {
        ArrayList<String> categoriasFiltro = new ArrayList<>();
        categoriasFiltro.add(CATEGORIA_TODAS);
        categoriasFiltro.addAll(CATEGORIAS_EVENTO);

        cmbCategoriaFiltro.setItems(FXCollections.observableArrayList(categoriasFiltro));
        cmbCategoriaFiltro.setValue(CATEGORIA_TODAS);
        cmbCategoriaFormulario.setItems(FXCollections.observableArrayList(CATEGORIAS_EVENTO));
    }

    private void configurarCamposNumericos() {
        txtPrecioBase.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }

            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                txtPrecioBase.setText(oldValue);
            }
        });

    }

    private void mostrarVistaAdministrador() {
        lblTituloVista.setText("Gestion de Eventos");
        panelAdministracion.setVisible(true);
        panelAdministracion.setManaged(true);
        boxAccionesAdmin.setVisible(true);
        boxAccionesAdmin.setManaged(true);
        tablaEventos.setVisible(true);
        tablaEventos.setManaged(true);
        boxAccionesTabla.setVisible(false);
        boxAccionesTabla.setManaged(false);
        scrollEventosCards.setVisible(false);
        scrollEventosCards.setManaged(false);
    }

    private void mostrarVistaUsuario() {
        lblTituloVista.setText("Eventos Disponibles");
        panelAdministracion.setVisible(false);
        panelAdministracion.setManaged(false);
        boxAccionesAdmin.setVisible(false);
        boxAccionesAdmin.setManaged(false);
        tablaEventos.setVisible(false);
        tablaEventos.setManaged(false);
        boxAccionesTabla.setVisible(false);
        boxAccionesTabla.setManaged(false);
        scrollEventosCards.setVisible(true);
        scrollEventosCards.setManaged(true);
        limpiarFormulario();
    }

    private void aplicarFiltroYMostrar() {
        ArrayList<Evento> eventos = obtenerEventosFiltrados();
        actualizarVistaConEventos(eventos);
    }

    private ArrayList<Evento> obtenerEventosFiltrados() {
        ArrayList<Evento> resultado = new ArrayList<>();
        Double precioMaximo = obtenerPrecioMaximoFiltro();

        if (precioMaximo == null && txtPrecioFiltro.getText() != null && !txtPrecioFiltro.getText().trim().isBlank()) {
            return resultado;
        }

        for (Evento evento : obtenerEventosVisibles()) {
            if (cumpleFiltros(evento, precioMaximo)) {
                resultado.add(evento);
            }
        }

        lblMensaje.setText("Se encontraron " + resultado.size() + " eventos.");
        return resultado;
    }

    private boolean cumpleFiltros(Evento evento, Double precioMaximo) {
        String texto = txtBuscarEvento.getText() == null ? "" : txtBuscarEvento.getText().trim().toLowerCase();
        String categoria = cmbCategoriaFiltro.getValue();
        String ciudad = txtCiudadFiltro.getText() == null ? "" : txtCiudadFiltro.getText().trim().toLowerCase();
        LocalDate fechaFiltro = dpFechaFiltro.getValue();

        boolean coincideTexto = texto.isEmpty() || evento.getNombre().toLowerCase().contains(texto);
        boolean coincideCategoria = categoria == null
                || categoria.isBlank()
                || CATEGORIA_TODAS.equals(categoria)
                || normalizarTexto(evento.getCategoria()).equals(normalizarTexto(categoria));
        boolean coincideCiudad = ciudad.isEmpty() || evento.getCiudad().toLowerCase().contains(ciudad);
        boolean coincideFecha = fechaFiltro == null
                || (evento.getFechaHora() != null && evento.getFechaHora().toLocalDate().equals(fechaFiltro));
        boolean coincidePrecio = precioMaximo == null || evento.getPrecioBase() <= precioMaximo;

        return coincideTexto && coincideCategoria && coincideCiudad && coincideFecha && coincidePrecio;
    }

    private Double obtenerPrecioMaximoFiltro() {
        String precioTexto = txtPrecioFiltro.getText() == null ? "" : txtPrecioFiltro.getText().trim();

        if (precioTexto.isBlank()) {
            return null;
        }

        double precioMaximo;
        try {
            precioMaximo = Double.parseDouble(precioTexto);
        } catch (NumberFormatException e) {
            lblMensaje.setText("El precio debe ser un numero valido.");
            return null;
        }

        if (precioMaximo < 0) {
            lblMensaje.setText("El precio no puede ser negativo.");
            return null;
        }

        return precioMaximo;
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }

        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return normalizado.replaceAll("\\p{M}", "").toLowerCase();
    }

    private void actualizarVistaConEventos(List<Evento> eventos) {
        if (Sesion.esAdministrador()) {
            cargarTablaEventos(eventos);
        } else {
            cargarCardsEventos(eventos);
        }
    }

    private void cargarTablaEventos(List<Evento> eventos) {
        tablaEventos.setItems(FXCollections.observableArrayList(eventos));
    }

    private void cargarCardsEventos(List<Evento> eventos) {
        flowEventosCards.getChildren().clear();

        for (Evento evento : eventos) {
            flowEventosCards.getChildren().add(crearCardEvento(evento));
        }
    }

    private VBox crearCardEvento(Evento evento) {
        Label lblNombre = new Label(evento.getNombre());
        lblNombre.getStyleClass().add("event-card-title");
        lblNombre.setWrapText(true);

        Label lblCategoria = new Label("Categoria: " + evento.getCategoria());
        Label lblCiudad = new Label("Ciudad: " + evento.getCiudad());
        Label lblFecha = new Label("Fecha: " + (evento.getFechaHora() == null ? "Sin fecha" : evento.getFechaHora()));
        Label lblPrecio = new Label("Precio: " + evento.getPrecioBase() + " COP");
        lblPrecio.getStyleClass().add("bold-label");
        Label lblEstado = new Label("Estado: " + evento.getEstadoEvento().name());

        Button btnDetalleCard = new Button("Ver detalle");
        btnDetalleCard.getStyleClass().add("secondary-button");
        btnDetalleCard.setOnAction(event -> mostrarDetalleEvento(evento));

        Button btnComprarCard = new Button("Comprar entrada");
        btnComprarCard.getStyleClass().add("primary-button");
        btnComprarCard.setOnAction(event -> seleccionarEventoParaCompra(evento));

        HBox acciones = new HBox(10, btnDetalleCard, btnComprarCard);
        acciones.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(10, lblNombre, lblCategoria, lblCiudad, lblFecha, lblPrecio, lblEstado, acciones);
        card.setPadding(new Insets(16));
        card.setPrefWidth(260);
        card.setMinWidth(260);
        card.getStyleClass().add("event-card");
        VBox.setVgrow(acciones, Priority.NEVER);
        return card;
    }

    private void mostrarDetalleEvento(Evento evento) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalle del evento");
        alert.setHeaderText(evento.getNombre());

        String detalle = construirDetalleEvento(evento);
        alert.setContentText(detalle);
        alert.showAndWait();
    }

    private String construirDetalleEvento(Evento evento) {
        Recinto recinto = evento.getRecinto();
        String fecha = evento.getFechaHora() == null
                ? "Sin fecha"
                : evento.getFechaHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String nombreRecinto = recinto == null ? "Sin recinto" : recinto.getNombre();
        String direccionRecinto = recinto == null || recinto.getDireccion() == null || recinto.getDireccion().isBlank()
                ? "Sin direccion registrada"
                : recinto.getDireccion();
        int aforoTotal = calcularAforoTotal(recinto);
        String zonasDetalle = construirDetalleZonas(recinto);

        return "Nombre: " + evento.getNombre() + "\n"
                + "Categoria: " + valorTexto(evento.getCategoria()) + "\n"
                + "Ciudad: " + valorTexto(evento.getCiudad()) + "\n"
                + "Fecha: " + fecha + "\n\n"
                + "Descripcion:\n"
                + valorTexto(evento.getDescripcion()) + "\n\n"
                + "Lugar:\n"
                + nombreRecinto + "\n"
                + "Direccion: " + direccionRecinto + "\n\n"
                + "Aforo total:\n"
                + aforoTotal + " personas\n\n"
                + "Precio base:\n"
                + evento.getPrecioBase() + " COP\n\n"
                + "Zonas y precios:\n"
                + zonasDetalle + "\n\n"
                + "Reglas:\n"
                + "- " + valorTexto(evento.getPoliticaCancelacion()) + "\n"
                + "- " + valorTexto(evento.getPoliticaReembolso()) + "\n"
                + "- " + valorTexto(evento.getReglasGenerales()) + "\n\n"
                + "Estado:\n"
                + evento.getEstadoEvento().name();
    }

    private int calcularAforoTotal(Recinto recinto) {
        if (recinto == null || recinto.getZonas() == null || recinto.getZonas().isEmpty()) {
            return 0;
        }

        int aforo = 0;
        for (Zona zona : recinto.getZonas()) {
            aforo += zona.getCapacidad();
        }
        return aforo;
    }

    private String construirDetalleZonas(Recinto recinto) {
        if (recinto == null || recinto.getZonas() == null || recinto.getZonas().isEmpty()) {
            return "- Sin zonas registradas";
        }

        StringBuilder detalle = new StringBuilder();

        for (Zona zona : recinto.getZonas()) {
            int disponibles = obtenerDisponiblesZona(zona);
            detalle.append("- ")
                    .append(zona.getNombre())
                    .append(" | Capacidad: ")
                    .append(zona.getCapacidad())
                    .append(" | Disponibles: ")
                    .append(disponibles)
                    .append(" | Precio: ")
                    .append(zona.getPrecioBase())
                    .append(" COP")
                    .append("\n");
        }

        return detalle.toString().trim();
    }

    private int obtenerDisponiblesZona(Zona zona) {
        if (zona.getAsientos() == null || zona.getAsientos().isEmpty()) {
            return zona.getCapacidad();
        }

        int disponibles = 0;
        for (int i = 0; i < zona.getAsientos().size(); i++) {
            if (zona.getAsientos().get(i).getEstadoAsiento() == EstadoAsiento.DISPONIBLE) {
                disponibles++;
            }
        }
        return disponibles;
    }

    private String valorTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return "No definido";
        }
        return texto;
    }

    private void notificarCambioEstadoEvento(Evento evento, EstadoEvento estado) {
        if (evento == null) {
            return;
        }

        ObservableEvento observableEvento = new ObservableEvento(evento);
        for (Usuario usuario : sistemaEventos.getUsuarios()) {
            observableEvento.agregarObservador(new ObservadorUsuario(usuario));
        }

        for (Usuario usuario : sistemaEventos.getUsuarios()) {
            if (usuario.getIdUsuario() == null) {
                continue;
            }
            Notificacion notificacion = new Notificacion(
                    "NOT-" + System.currentTimeMillis() + "-" + usuario.getIdUsuario(),
                    "Cambio de estado del evento",
                    "El evento " + evento.getNombre() + " cambió a estado " + estado + ".",
                    usuario.getIdUsuario(),
                    TipoNotificacion.EVENTO
            );
            observableEvento.notificarObservadores(notificacion);
        }
    }

    private void seleccionarEventoParaCompra(Evento evento) {
        if (Sesion.esAdministrador()) {
            lblMensaje.setText("La compra de entradas es solo para usuarios.");
            return;
        }

        Sesion.setEventoSeleccionadoParaCompra(evento);
        MainController.abrirVistaCentral("ComprarEntradaView.fxml");
    }
}
