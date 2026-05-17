package uniquindio.edu.co.eventos.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import uniquindio.edu.co.eventos.model.Asiento;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Sesion;
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

public class ComprarEntradaController {

    @FXML
    private Label lblEventoSeleccionado;

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

    private Evento eventoSeleccionado;

    @FXML
    public void initialize() {
        cmbMetodoPago.setItems(FXCollections.observableArrayList("Tarjeta", "PSE", "Simulado"));
        cmbZona.valueProperty().addListener((obs, oldValue, newValue) -> cargarAsientos(newValue));
        cargarDatosEvento(Sesion.getEventoSeleccionadoParaCompra());
    }

    public void cargarDatosEvento(Evento evento) {
        this.eventoSeleccionado = evento;

        if (evento == null) {
            lblEventoSeleccionado.setText("Sin evento seleccionado");
            cmbZona.getItems().clear();
            cmbAsiento.getItems().clear();
            lblMensaje.setText("No hay evento seleccionado para comprar.");
            return;
        }

        lblEventoSeleccionado.setText(evento.getNombre());

        if (evento.getRecinto() != null) {
            cmbZona.setItems(FXCollections.observableArrayList(evento.getRecinto().getZonas()));
        }
    }

    @FXML
    private void calcularTotal() {
        Zona zona = cmbZona.getValue();

        if (eventoSeleccionado == null) {
            lblMensaje.setText("No hay evento seleccionado para comprar.");
            return;
        }

        if (zona == null) {
            lblMensaje.setText("Debe seleccionar una zona.");
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

        lblTotal.setText(total + " COP");
        lblMensaje.setText("Total calculado correctamente.");
    }

    @FXML
    private void confirmarCompra() {
        Usuario usuario = Sesion.getUsuarioActual();
        Zona zona = cmbZona.getValue();
        Asiento asiento = cmbAsiento.getValue();
        String metodoPago = cmbMetodoPago.getValue();

        if (eventoSeleccionado == null) {
            lblMensaje.setText("No hay evento seleccionado para comprar.");
            return;
        }

        if (usuario == null) {
            lblMensaje.setText("Debe iniciar sesion para comprar.");
            return;
        }

        if (zona == null) {
            lblMensaje.setText("Debe seleccionar una zona.");
            return;
        }

        if (asiento == null) {
            lblMensaje.setText("Debe seleccionar un asiento.");
            return;
        }

        if (metodoPago == null || metodoPago.isBlank()) {
            lblMensaje.setText("Debe seleccionar un metodo de pago.");
            return;
        }

        if (!asiento.estaDisponible()) {
            lblMensaje.setText("El asiento seleccionado ya no esta disponible.");
            return;
        }

        EstrategiaPago estrategiaPago = crearEstrategiaPago(metodoPago);
        CompraFacade facade = new CompraFacade();
        Compra compra = facade.realizarCompra(usuario, eventoSeleccionado, zona, asiento, estrategiaPago);

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
        lblTotal.setText(compra.getTotal() + " COP");
        lblMensaje.setText("Compra realizada correctamente.");
        Sesion.setEventoSeleccionadoParaCompra(null);
        MainController.abrirVistaCentral("ComprasView.fxml");
    }

    @FXML
    private void volver() {
        MainController.abrirVistaCentral("EventosView.fxml");
    }

    private void cargarAsientos(Zona zona) {
        if (zona == null) {
            cmbAsiento.getItems().clear();
            return;
        }

        cmbAsiento.setItems(FXCollections.observableArrayList(zona.consultarAsientosDisponibles()));
    }

    private EstrategiaPago crearEstrategiaPago(String metodo) {
        if ("Tarjeta".equalsIgnoreCase(metodo)) {
            return new PagoTarjeta();
        }

        if ("PSE".equalsIgnoreCase(metodo)) {
            return new PagoPSE();
        }

        return new PagoSimulado();
    }
}
