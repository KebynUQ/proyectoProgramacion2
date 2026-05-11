package uniquindio.edu.co.eventos.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.model.enums.EstadoEvento;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class EventosController {

    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<String> cmbCategoria;

    @FXML
    private TextField txtCiudad;

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

        cargarCategorias();
        cargarEventos();
    }

    @FXML
    private void filtrarEventos() {
        String texto = txtBuscar.getText() == null ? "" : txtBuscar.getText().trim().toLowerCase();
        String categoria = cmbCategoria.getValue();
        String ciudad = txtCiudad.getText() == null ? "" : txtCiudad.getText().trim().toLowerCase();

        ArrayList<Evento> resultado = new ArrayList<>();

        for (Evento evento : sistemaEventos.getEventos()) {
            boolean coincideTexto = texto.isEmpty() || evento.getNombre().toLowerCase().contains(texto);
            boolean coincideCategoria = categoria == null || categoria.isBlank() || evento.getCategoria().equalsIgnoreCase(categoria);
            boolean coincideCiudad = ciudad.isEmpty() || evento.getCiudad().toLowerCase().contains(ciudad);

            if (coincideTexto && coincideCategoria && coincideCiudad) {
                resultado.add(evento);
            }
        }

        tablaEventos.setItems(FXCollections.observableArrayList(resultado));
        lblMensaje.setText("Se encontraron " + resultado.size() + " eventos.");
    }

    @FXML
    private void limpiarFiltros() {
        txtBuscar.clear();
        txtCiudad.clear();
        cmbCategoria.setValue(null);
        cargarEventos();
        lblMensaje.setText("");
    }

    @FXML
    private void verDetalleEvento() {
        Evento evento = tablaEventos.getSelectionModel().getSelectedItem();

        if (evento == null) {
            lblMensaje.setText("Seleccione un evento.");
            return;
        }

        lblMensaje.setText(evento.getDescripcion());
    }

    @FXML
    private void comprarEvento() {
        Evento evento = tablaEventos.getSelectionModel().getSelectedItem();
        lblMensaje.setText(evento == null ? "Seleccione un evento para comprar." : "Evento listo para compra: " + evento.getNombre());
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

    private void cambiarEstadoSeleccionado(EstadoEvento estado) {
        Evento evento = tablaEventos.getSelectionModel().getSelectedItem();

        if (evento == null) {
            lblMensaje.setText("Seleccione un evento.");
            return;
        }

        evento.cambiarEstado(estado);
        tablaEventos.refresh();
        lblMensaje.setText("Estado actualizado a " + estado + ".");
    }

    private void cargarEventos() {
        tablaEventos.setItems(FXCollections.observableArrayList(sistemaEventos.getEventos()));
    }

    private void cargarCategorias() {
        LinkedHashSet<String> categorias = new LinkedHashSet<>();

        for (Evento evento : sistemaEventos.getEventos()) {
            categorias.add(evento.getCategoria());
        }

        cmbCategoria.setItems(FXCollections.observableArrayList(categorias));
    }
}
