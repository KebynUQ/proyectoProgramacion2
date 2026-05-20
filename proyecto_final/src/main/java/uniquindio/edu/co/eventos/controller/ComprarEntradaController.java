package uniquindio.edu.co.eventos.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import uniquindio.edu.co.eventos.model.Administrador;
import uniquindio.edu.co.eventos.model.Asiento;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Entrada;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.Zona;
import uniquindio.edu.co.eventos.model.enums.TipoSolicitudCompra;
import uniquindio.edu.co.eventos.patterns.structural.AccesoPreferencial;
import uniquindio.edu.co.eventos.patterns.structural.CompraFacade;
import uniquindio.edu.co.eventos.patterns.structural.Merchandising;
import uniquindio.edu.co.eventos.patterns.structural.Parqueadero;
import uniquindio.edu.co.eventos.patterns.structural.SeguroCancelacion;
import uniquindio.edu.co.eventos.patterns.structural.ServicioVIP;
import uniquindio.edu.co.eventos.patterns.structural.ServicioAdicional;

import java.util.ArrayList;
import java.util.List;

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

    @FXML
    private Button btnGuardarCompra;

    private Evento eventoSeleccionado;
    private Compra compraEnEdicion;
    private boolean edicionSolicitudUsuario;

    @FXML
    public void initialize() {
        cmbMetodoPago.setItems(FXCollections.observableArrayList("Tarjeta", "PSE", "Simulado"));
        cmbZona.valueProperty().addListener((obs, oldValue, newValue) -> cargarAsientos(newValue));

        compraEnEdicion = Sesion.esAdministrador() ? Sesion.getCompraSeleccionadaParaEditar() : null;
        if (compraEnEdicion != null) {
            edicionSolicitudUsuario = compraEnEdicion.getTipoSolicitud() == TipoSolicitudCompra.SOLICITUD_MODIFICACION;
            cargarDatosCompraEnEdicion(compraEnEdicion);
            btnGuardarCompra.setText(edicionSolicitudUsuario ? "Aprobar modificación" : "Guardar cambios");
        } else {
            cargarDatosEvento(Sesion.getEventoSeleccionadoParaCompra());
            btnGuardarCompra.setText("Confirmar compra");
        }
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
    private void guardarCompra() {
        Usuario usuario = Sesion.getUsuarioActual();
        Administrador administrador = Sesion.getAdministradorActual();
        boolean haySesion = usuario != null || administrador != null;
        Zona zona = cmbZona.getValue();
        Asiento asiento = cmbAsiento.getValue();
        String metodoPago = cmbMetodoPago.getValue();

        if (!validarCamposBase(haySesion, zona, asiento, metodoPago)) {
            return;
        }

        CompraFacade facade = new CompraFacade();
        List<ServicioAdicional> servicios = obtenerServiciosSeleccionados();
        Compra compra;

        if (compraEnEdicion != null) {
            boolean actualizado = facade.modificarCompra(compraEnEdicion, zona, asiento, servicios, metodoPago, true);
            if (!actualizado) {
                lblMensaje.setText("No se pudo modificar la compra seleccionada.");
                return;
            }
            compra = compraEnEdicion;
        } else {
            if (!asiento.estaDisponible()) {
                lblMensaje.setText("El asiento seleccionado ya no esta disponible.");
                return;
            }

            compra = facade.crearCompraPendiente(usuario, eventoSeleccionado, zona, asiento, servicios);
            if (compra == null) {
                lblMensaje.setText("No fue posible crear la compra.");
                return;
            }
        }

        lblTotal.setText(compra.getTotal() + " COP");
        if (compraEnEdicion == null) {
            lblMensaje.setText("Compra guardada. Puedes enviar solicitudes desde Mis compras.");
        } else if (edicionSolicitudUsuario) {
            lblMensaje.setText("Modificación aprobada correctamente.");
        } else {
            lblMensaje.setText("Compra actualizada correctamente por el administrador.");
        }
        Sesion.limpiarCompraSeleccionadaParaEditar();
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

        if (compraEnEdicion != null) {
            Entrada entrada = compraEnEdicion.getEntradas().isEmpty() ? null : compraEnEdicion.getEntradas().get(0);
            if (entrada != null && entrada.getAsiento() != null && entrada.getZona() != null
                    && entrada.getZona().getIdZona().equals(zona.getIdZona())) {
                if (!cmbAsiento.getItems().contains(entrada.getAsiento())) {
                    cmbAsiento.getItems().add(entrada.getAsiento());
                }
                cmbAsiento.setValue(entrada.getAsiento());
            }
        }
    }

    private List<ServicioAdicional> obtenerServiciosSeleccionados() {
        List<ServicioAdicional> servicios = new ArrayList<>();

        if (chkVIP.isSelected()) {
            servicios.add(new ServicioVIP(null));
        }
        if (chkSeguro.isSelected()) {
            servicios.add(new SeguroCancelacion(null));
        }
        if (chkMerchandising.isSelected()) {
            servicios.add(new Merchandising(null));
        }
        if (chkParqueadero.isSelected()) {
            servicios.add(new Parqueadero(null));
        }
        if (chkAcceso.isSelected()) {
            servicios.add(new AccesoPreferencial(null));
        }

        return servicios;
    }

    private boolean validarCamposBase(boolean haySesion, Zona zona, Asiento asiento, String metodoPago) {
        if (eventoSeleccionado == null) {
            lblMensaje.setText("No hay evento seleccionado para comprar.");
            return false;
        }

        if (!haySesion) {
            lblMensaje.setText("Debe iniciar sesion para comprar.");
            return false;
        }

        if (zona == null) {
            lblMensaje.setText("Debe seleccionar una zona.");
            return false;
        }

        if (asiento == null) {
            lblMensaje.setText("Debe seleccionar un asiento.");
            return false;
        }

        if (metodoPago == null || metodoPago.isBlank()) {
            lblMensaje.setText("Debe seleccionar un metodo de pago.");
            return false;
        }

        return true;
    }

    private void cargarDatosCompraEnEdicion(Compra compra) {
        this.eventoSeleccionado = compra.getEvento();
        cargarDatosEvento(eventoSeleccionado);

        Entrada entrada = compra.getEntradas().isEmpty() ? null : compra.getEntradas().get(0);
        if (entrada != null) {
            cmbZona.setValue(entrada.getZona());
            cargarAsientos(entrada.getZona());
            cmbAsiento.setValue(entrada.getAsiento());
        }

        marcarServicios(compra);
        lblTotal.setText(compra.getTotal() + " COP");
        if (compra.getPago() != null && compra.getPago().getMetodoPago() != null) {
            cmbMetodoPago.setValue(compra.getPago().getMetodoPago());
        }
    }

    private void marcarServicios(Compra compra) {
        chkVIP.setSelected(false);
        chkSeguro.setSelected(false);
        chkMerchandising.setSelected(false);
        chkParqueadero.setSelected(false);
        chkAcceso.setSelected(false);

        for (ServicioAdicional servicio : compra.getServiciosAdicionales()) {
            String nombre = servicio.getClass().getSimpleName();
            if ("ServicioVIP".equals(nombre)) {
                chkVIP.setSelected(true);
            } else if ("SeguroCancelacion".equals(nombre)) {
                chkSeguro.setSelected(true);
            } else if ("Merchandising".equals(nombre)) {
                chkMerchandising.setSelected(true);
            } else if ("Parqueadero".equals(nombre)) {
                chkParqueadero.setSelected(true);
            } else if ("AccesoPreferencial".equals(nombre)) {
                chkAcceso.setSelected(true);
            }
        }
    }
}
