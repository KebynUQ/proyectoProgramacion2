package uniquindio.edu.co.eventos.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Entrada;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.enums.EstadoCompra;
import uniquindio.edu.co.eventos.model.enums.TipoSolicitudCompra;
import uniquindio.edu.co.eventos.patterns.structural.ComprobantePagoPDF;
import uniquindio.edu.co.eventos.patterns.structural.CompraFacade;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComprasController {

    @FXML
    private Label lblTituloVista;

    @FXML
    private Label lblMensaje;

    @FXML
    private TableView<Compra> tablaCompras;

    @FXML
    private TableColumn<Compra, String> colIdCompra;

    @FXML
    private TableColumn<Compra, String> colUsuario;

    @FXML
    private TableColumn<Compra, String> colEvento;

    @FXML
    private TableColumn<Compra, Object> colTotal;

    @FXML
    private TableColumn<Compra, String> colEstado;

    @FXML
    private TableColumn<Compra, String> colSolicitud;

    @FXML
    private HBox boxAccionesUsuario;

    @FXML
    private HBox boxAccionesAdmin;

    @FXML
    private HBox boxFiltros;

    @FXML
    private TextField txtFiltroEvento;

    @FXML
    private DatePicker dpFiltroFecha;

    @FXML
    private ComboBox<String> cmbFiltroEstado;

    private final SistemaEventos sistemaEventos = SistemaEventos.getInstancia();
    private final CompraFacade compraFacade = new CompraFacade();

    @FXML
    public void initialize() {
        colIdCompra.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdCompra()));
        colUsuario.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsuario().getNombreCompleto()));
        colEvento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEvento().getNombre()));
        colTotal.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTotal()));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstadoCompra().name()));
        colSolicitud.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMensajeSolicitud()));
        cmbFiltroEstado.setItems(FXCollections.observableArrayList(
                "Todas",
                EstadoCompra.CREADA.name(),
                EstadoCompra.PENDIENTE_CONFIRMACION.name(),
                EstadoCompra.PAGADA.name(),
                EstadoCompra.CONFIRMADA.name(),
                EstadoCompra.CANCELADA.name(),
                EstadoCompra.REEMBOLSADA.name()
        ));
        cmbFiltroEstado.setValue("Todas");

        configurarVistaSegunSesion();
        cargarComprasSegunSesion();
    }

    private void configurarVistaSegunSesion() {
        boolean esAdministrador = Sesion.esAdministrador();
        lblTituloVista.setText(esAdministrador ? "Gestion de compras" : "Mis compras");
        colUsuario.setVisible(esAdministrador);
        colUsuario.setResizable(esAdministrador);
        boxAccionesAdmin.setVisible(esAdministrador);
        boxAccionesAdmin.setManaged(esAdministrador);
        boxAccionesUsuario.setVisible(!esAdministrador);
        boxAccionesUsuario.setManaged(!esAdministrador);
        boxFiltros.setVisible(!esAdministrador);
        boxFiltros.setManaged(!esAdministrador);
    }

    private void cargarComprasSegunSesion() {
        if (Sesion.esAdministrador()) {
            tablaCompras.setItems(FXCollections.observableArrayList(sistemaEventos.getCompras()));
            return;
        }

        Usuario usuario = Sesion.getUsuarioActual();
        if (usuario == null) {
            tablaCompras.setItems(FXCollections.observableArrayList(new ArrayList<>()));
            lblMensaje.setText("No hay usuario en sesión.");
            return;
        }

        ArrayList<Compra> comprasUsuario = sistemaEventos.getCompras().stream()
                .filter(compra -> compra.getUsuario() != null
                        && compra.getUsuario().getIdUsuario() != null
                        && compra.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                .collect(Collectors.toCollection(ArrayList::new));
        tablaCompras.setItems(FXCollections.observableArrayList(comprasUsuario));
        lblMensaje.setText("");
    }

    @FXML
    private void filtrarCompras() {
        if (Sesion.esAdministrador()) {
            return;
        }

        Usuario usuario = Sesion.getUsuarioActual();
        if (usuario == null) {
            tablaCompras.setItems(FXCollections.observableArrayList(new ArrayList<>()));
            lblMensaje.setText("No hay usuario en sesión.");
            return;
        }

        String textoEvento = txtFiltroEvento.getText() == null ? "" : txtFiltroEvento.getText().trim().toLowerCase();
        LocalDate fechaFiltro = dpFiltroFecha.getValue();
        String estadoFiltro = cmbFiltroEstado.getValue() == null ? "Todas" : cmbFiltroEstado.getValue();

        List<Compra> comprasFiltradas = sistemaEventos.getCompras().stream()
                .filter(compra -> compra.getUsuario() != null
                        && compra.getUsuario().getIdUsuario() != null
                        && compra.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                .filter(compra -> textoEvento.isEmpty()
                        || (compra.getEvento() != null
                        && compra.getEvento().getNombre() != null
                        && compra.getEvento().getNombre().toLowerCase().contains(textoEvento)))
                .filter(compra -> fechaFiltro == null
                        || (compra.getFechaCreacion() != null
                        && compra.getFechaCreacion().toLocalDate().equals(fechaFiltro)))
                .filter(compra -> "Todas".equals(estadoFiltro)
                        || (compra.getEstadoCompra() != null
                        && compra.getEstadoCompra().name().equals(estadoFiltro)))
                .collect(Collectors.toList());

        tablaCompras.setItems(FXCollections.observableArrayList(comprasFiltradas));
        if (comprasFiltradas.isEmpty()) {
            lblMensaje.setText("No se encontraron compras con esos filtros.");
        } else {
            lblMensaje.setText("Filtro aplicado correctamente.");
        }
    }

    @FXML
    private void limpiarFiltros() {
        txtFiltroEvento.clear();
        dpFiltroFecha.setValue(null);
        cmbFiltroEstado.setValue("Todas");
        cargarComprasSegunSesion();
    }

    @FXML
    private void verDetalleCompra() {
        Compra compra = obtenerCompraSeleccionada();
        if (compra == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalle de compra");
        alert.setHeaderText(compra.getIdCompra());
        alert.setContentText(
                "Usuario: " + compra.getUsuario().getNombreCompleto() + "\n"
                        + "Evento: " + compra.getEvento().getNombre() + "\n"
                        + "Zona: " + obtenerZona(compra) + "\n"
                        + "Asiento: " + obtenerAsiento(compra) + "\n"
                        + "Total: " + compra.getTotal() + " COP\n"
                        + "Estado: " + compra.getEstadoCompra() + "\n"
                        + "Solicitud: " + compra.getMensajeSolicitud()
        );
        alert.showAndWait();
    }

    @FXML
    private void solicitarModificacion() {
        Compra compra = obtenerCompraSeleccionada();
        if (compra == null) {
            return;
        }
        if (compra.getEstadoCompra() == EstadoCompra.PAGADA || compra.getEstadoCompra() == EstadoCompra.CONFIRMADA) {
            lblMensaje.setText("No puedes solicitar modificación de una compra ya pagada.");
            return;
        }
        compraFacade.solicitarModificacion(compra);
        recargarYMensaje("Solicitud de modificación enviada.");
    }

    @FXML
    private void solicitarCancelacion() {
        Compra compra = obtenerCompraSeleccionada();
        if (compra == null) {
            return;
        }
        if (compra.getEstadoCompra() == EstadoCompra.CANCELADA) {
            lblMensaje.setText("La compra ya está cancelada.");
            return;
        }
        compraFacade.solicitarCancelacion(compra);
        recargarYMensaje("Solicitud de cancelación enviada.");
    }

    @FXML
    private void solicitarConfirmacionPago() {
        Compra compra = obtenerCompraSeleccionada();
        if (compra == null) {
            return;
        }
        if (compra.getEstadoCompra() == EstadoCompra.CANCELADA) {
            lblMensaje.setText("No puedes solicitar pago de una compra cancelada.");
            return;
        }
        compraFacade.solicitarConfirmacionPago(compra);
        recargarYMensaje("Solicitud de confirmación de pago enviada.");
    }

    @FXML
    private void aprobarModificacion() {
        Compra compra = obtenerCompraSeleccionada();
        if (compra == null) {
            return;
        }

        Sesion.setCompraSeleccionadaParaEditar(compra);
        Sesion.setEventoSeleccionadoParaCompra(compra.getEvento());
        MainController.abrirVistaCentral("ComprarEntradaView.fxml");
    }

    @FXML
    private void aprobarCancelacion() {
        Compra compra = obtenerCompraSeleccionada();
        if (compra == null) {
            return;
        }
        if (compra.getTipoSolicitud() != TipoSolicitudCompra.SOLICITUD_CANCELACION) {
            lblMensaje.setText("No hay solicitud de cancelación pendiente.");
            return;
        }
        boolean exito = compraFacade.aprobarCancelacion(compra);
        if (!exito) {
            lblMensaje.setText("No fue posible aprobar la cancelación.");
            return;
        }
        recargarYMensaje("Cancelación aprobada correctamente.");
    }

    @FXML
    private void confirmarPagoComoAdmin() {
        Compra compra = obtenerCompraSeleccionada();
        if (compra == null) {
            return;
        }
        if (compra.getTipoSolicitud() != TipoSolicitudCompra.SOLICITUD_CONFIRMACION_PAGO) {
            lblMensaje.setText("No hay solicitud de confirmación de pago pendiente.");
            return;
        }
        boolean exito = compraFacade.confirmarPagoAdmin(compra);
        if (!exito) {
            lblMensaje.setText("No fue posible confirmar el pago.");
            return;
        }
        recargarYMensaje("Pago confirmado correctamente.");
    }

    @FXML
    private void cancelarCompraComoAdmin() {
        Compra compra = obtenerCompraSeleccionada();
        if (compra == null) {
            return;
        }

        boolean exito = compraFacade.cancelarCompraComoAdmin(compra);
        if (!exito) {
            lblMensaje.setText("No fue posible cancelar la compra.");
            return;
        }
        recargarYMensaje("Compra cancelada correctamente.");
    }

    @FXML
    private void registrarReembolso() {
        Compra compra = obtenerCompraSeleccionada();
        if (compra == null) {
            return;
        }

        boolean exito = compraFacade.registrarReembolso(compra);
        if (!exito) {
            lblMensaje.setText("No fue posible registrar el reembolso.");
            return;
        }
        recargarYMensaje("Reembolso registrado correctamente.");
    }

    @FXML
    private void rechazarSolicitud() {
        Compra compra = obtenerCompraSeleccionada();
        if (compra == null) {
            return;
        }
        compraFacade.rechazarSolicitud(compra);
        recargarYMensaje("Solicitud rechazada.");
    }

    @FXML
    private void descargarComprobante() {
        Compra compra = obtenerCompraSeleccionada();
        if (compra == null) {
            return;
        }

        if (compra.getEstadoCompra() != EstadoCompra.PAGADA && compra.getEstadoCompra() != EstadoCompra.CONFIRMADA) {
            lblMensaje.setText("Solo se puede descargar comprobante de una compra pagada o confirmada.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar comprobante de pago");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf")
        );
        fileChooser.setInitialFileName("comprobante_" + compra.getIdCompra() + ".pdf");

        File archivo = fileChooser.showSaveDialog(tablaCompras.getScene().getWindow());
        if (archivo == null) {
            return;
        }

        try {
            ComprobantePagoPDF.generar(compra, archivo);
            lblMensaje.setText("Comprobante descargado correctamente.");
        } catch (IOException e) {
            lblMensaje.setText("No fue posible generar el comprobante.");
        }
    }

    private Compra obtenerCompraSeleccionada() {
        Compra compra = tablaCompras.getSelectionModel().getSelectedItem();
        if (compra == null) {
            lblMensaje.setText("Debe seleccionar una compra.");
        }
        return compra;
    }

    private void recargarYMensaje(String mensaje) {
        cargarComprasSegunSesion();
        lblMensaje.setText(mensaje);
    }

    private String obtenerZona(Compra compra) {
        Entrada entrada = compra.getEntradas().isEmpty() ? null : compra.getEntradas().get(0);
        return entrada == null || entrada.getZona() == null ? "Sin zona" : entrada.getZona().getNombre();
    }

    private String obtenerAsiento(Compra compra) {
        Entrada entrada = compra.getEntradas().isEmpty() ? null : compra.getEntradas().get(0);
        if (entrada == null || entrada.getAsiento() == null) {
            return "Sin asiento";
        }
        return entrada.getAsiento().getFila() + "-" + entrada.getAsiento().getNumero();
    }
}
