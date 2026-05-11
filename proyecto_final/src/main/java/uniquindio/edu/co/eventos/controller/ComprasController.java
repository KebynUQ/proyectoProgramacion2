package uniquindio.edu.co.eventos.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import uniquindio.edu.co.eventos.model.Asiento;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.Zona;
import uniquindio.edu.co.eventos.patterns.behavioral.EstrategiaPago;
import uniquindio.edu.co.eventos.patterns.behavioral.PagoPSE;
import uniquindio.edu.co.eventos.patterns.behavioral.PagoSimulado;
import uniquindio.edu.co.eventos.patterns.behavioral.PagoTarjeta;
import uniquindio.edu.co.eventos.patterns.structural.AccesoPreferencial;
import uniquindio.edu.co.eventos.patterns.structural.CompraFacade;
import uniquindio.edu.co.eventos.patterns.structural.Merchandising;
import uniquindio.edu.co.eventos.patterns.structural.Parqueadero;
import uniquindio.edu.co.eventos.patterns.structural.SeguroCancelacion;
import uniquindio.edu.co.eventos.patterns.structural.ServicioVIP;

public class ComprasController {

    @FXML
    private ComboBox<Evento> cmbEvento;

    @FXML
    private ComboBox<Zona> cmbZona;

    @FXML
    private ComboBox<Asiento> cmbAsiento;

    @FXML
    private ComboBox<String> cmbMetodoPago;

    @FXML
    private CheckBox chkVIP;

    @FXML
    private CheckBox chkSeguro;

    @FXML
    private CheckBox chkMerchandising;

    @FXML
    private CheckBox chkParqueadero;

    @FXML
    private CheckBox chkAcceso;

    @FXML
    private Label lblTotal;

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

    private final SistemaEventos sistemaEventos = SistemaEventos.getInstancia();

    @FXML
    public void initialize() {
        colIdCompra.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdCompra()));
        colUsuario.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsuario().getNombreCompleto()));
        colEvento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEvento().getNombre()));
        colTotal.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTotal()));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstadoCompra().name()));

        cmbEvento.setItems(FXCollections.observableArrayList(sistemaEventos.getEventos()));
        cmbMetodoPago.setItems(FXCollections.observableArrayList("Tarjeta", "PSE", "Simulado"));
        cmbMetodoPago.setValue("Simulado");

        cmbEvento.valueProperty().addListener((obs, oldValue, newValue) -> cargarZonas(newValue));
        cmbZona.valueProperty().addListener((obs, oldValue, newValue) -> cargarAsientos(newValue));

        tablaCompras.setItems(FXCollections.observableArrayList(sistemaEventos.getCompras()));
    }

    @FXML
    private void calcularTotal() {
        Zona zona = cmbZona.getValue();

        if (zona == null) {
            lblMensaje.setText("Seleccione una zona.");
            return;
        }

        double total = zona.getPrecioBase();

        if (chkVIP.isSelected()) {
            total += new ServicioVIP(null).getPrecio();
        }
        if (chkSeguro.isSelected()) {
            total += new SeguroCancelacion(null).getPrecio();
        }
        if (chkMerchandising.isSelected()) {
            total += new Merchandising(null).getPrecio();
        }
        if (chkParqueadero.isSelected()) {
            total += new Parqueadero(null).getPrecio();
        }
        if (chkAcceso.isSelected()) {
            total += new AccesoPreferencial(null).getPrecio();
        }

        lblTotal.setText("$" + total);
        lblMensaje.setText("Total calculado correctamente.");
    }

    @FXML
    private void confirmarCompra() {
        Usuario usuario = Sesion.getUsuarioActual();
        Evento evento = cmbEvento.getValue();
        Zona zona = cmbZona.getValue();
        Asiento asiento = cmbAsiento.getValue();

        if (usuario == null) {
            lblMensaje.setText("Debe iniciar sesion como usuario para comprar.");
            return;
        }

        EstrategiaPago estrategiaPago = crearEstrategiaPago();
        CompraFacade facade = new CompraFacade();
        Compra compra = facade.realizarCompra(usuario, evento, zona, asiento, estrategiaPago);

        if (compra == null) {
            lblMensaje.setText("No fue posible completar la compra.");
            return;
        }

        if (chkVIP.isSelected()) {
            compra.agregarServicio(new ServicioVIP(null));
        }
        if (chkSeguro.isSelected()) {
            compra.agregarServicio(new SeguroCancelacion(null));
        }
        if (chkMerchandising.isSelected()) {
            compra.agregarServicio(new Merchandising(null));
        }
        if (chkParqueadero.isSelected()) {
            compra.agregarServicio(new Parqueadero(null));
        }
        if (chkAcceso.isSelected()) {
            compra.agregarServicio(new AccesoPreferencial(null));
        }

        compra.calcularTotal();
        tablaCompras.setItems(FXCollections.observableArrayList(sistemaEventos.getCompras()));
        tablaCompras.refresh();
        lblTotal.setText("$" + compra.getTotal());
        lblMensaje.setText("Compra registrada correctamente.");
    }

    private void cargarZonas(Evento evento) {
        if (evento == null || evento.getRecinto() == null) {
            cmbZona.getItems().clear();
            cmbAsiento.getItems().clear();
            return;
        }

        cmbZona.setItems(FXCollections.observableArrayList(evento.getRecinto().getZonas()));
        cmbAsiento.getItems().clear();
    }

    private void cargarAsientos(Zona zona) {
        if (zona == null) {
            cmbAsiento.getItems().clear();
            return;
        }

        cmbAsiento.setItems(FXCollections.observableArrayList(zona.consultarAsientosDisponibles()));
    }

    private EstrategiaPago crearEstrategiaPago() {
        String metodo = cmbMetodoPago.getValue();

        if ("Tarjeta".equalsIgnoreCase(metodo)) {
            return new PagoTarjeta();
        }

        if ("PSE".equalsIgnoreCase(metodo)) {
            return new PagoPSE();
        }

        return new PagoSimulado();
    }
}
