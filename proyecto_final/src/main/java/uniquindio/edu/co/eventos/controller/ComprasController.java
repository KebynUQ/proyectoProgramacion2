package uniquindio.edu.co.eventos.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Entrada;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.model.Usuario;

import java.util.ArrayList;

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
    private TableColumn<Compra, String> colZona;

    @FXML
    private TableColumn<Compra, String> colAsiento;

    @FXML
    private TableColumn<Compra, Object> colTotal;

    @FXML
    private TableColumn<Compra, String> colEstado;

    @FXML
    private TableColumn<Compra, Object> colFecha;

    private final SistemaEventos sistemaEventos = SistemaEventos.getInstancia();

    @FXML
    public void initialize() {
        colIdCompra.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdCompra()));
        colUsuario.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsuario().getNombreCompleto()));
        colEvento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEvento().getNombre()));
        colZona.setCellValueFactory(data -> new SimpleStringProperty(obtenerZona(data.getValue())));
        colAsiento.setCellValueFactory(data -> new SimpleStringProperty(obtenerAsiento(data.getValue())));
        colTotal.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTotal()));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstadoCompra().name()));
        colFecha.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFechaCreacion()));

        configurarVistaSegunSesion();
        cargarComprasSegunSesion();
    }

    private void configurarVistaSegunSesion() {
        boolean esAdministrador = Sesion.esAdministrador();

        lblTituloVista.setText(esAdministrador ? "Compras Registradas" : "Mis compras");
        colUsuario.setVisible(esAdministrador);
        colUsuario.setResizable(esAdministrador);
    }

    private void cargarComprasSegunSesion() {
        if (Sesion.esAdministrador()) {
            tablaCompras.setItems(FXCollections.observableArrayList(sistemaEventos.getCompras()));
            lblMensaje.setText(sistemaEventos.getCompras().isEmpty() ? "No hay compras registradas." : "");
            return;
        }

        Usuario usuario = Sesion.getUsuarioActual();
        ArrayList<Compra> comprasUsuario = usuario == null ? new ArrayList<>() : usuario.getCompras();
        tablaCompras.setItems(FXCollections.observableArrayList(comprasUsuario));
        lblMensaje.setText(comprasUsuario.isEmpty() ? "No tienes compras registradas." : "");
    }

    private String obtenerZona(Compra compra) {
        Entrada entrada = obtenerPrimeraEntrada(compra);
        return entrada == null || entrada.getZona() == null ? "Sin zona" : entrada.getZona().getNombre();
    }

    private String obtenerAsiento(Compra compra) {
        Entrada entrada = obtenerPrimeraEntrada(compra);

        if (entrada == null || entrada.getAsiento() == null) {
            return "Sin asiento";
        }

        return entrada.getAsiento().getFila() + "-" + entrada.getAsiento().getNumero();
    }

    private Entrada obtenerPrimeraEntrada(Compra compra) {
        return compra.getEntradas().isEmpty() ? null : compra.getEntradas().get(0);
    }
}
