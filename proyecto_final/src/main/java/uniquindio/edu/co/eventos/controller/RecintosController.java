package uniquindio.edu.co.eventos.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import uniquindio.edu.co.eventos.model.Asiento;
import uniquindio.edu.co.eventos.model.Recinto;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.model.Zona;
import uniquindio.edu.co.eventos.model.enums.EstadoAsiento;
import uniquindio.edu.co.eventos.model.enums.TipoIncidencia;

import java.util.ArrayList;

public class RecintosController {

    @FXML
    private TextField txtNombreRecinto;
    @FXML
    private TextField txtDireccionRecinto;
    @FXML
    private TextField txtCiudadRecinto;
    @FXML
    private TextField txtNombreZona;
    @FXML
    private TextField txtCapacidadZona;
    @FXML
    private TextField txtPrecioZona;
    @FXML
    private TableView<Recinto> tablaRecintos;
    @FXML
    private TableView<Zona> tablaZonas;
    @FXML
    private TableView<Asiento> tablaAsientos;
    @FXML
    private TableColumn<Recinto, String> colIdRecinto;
    @FXML
    private TableColumn<Recinto, String> colNombreRecinto;
    @FXML
    private TableColumn<Recinto, String> colDireccionRecinto;
    @FXML
    private TableColumn<Recinto, String> colCiudadRecinto;
    @FXML
    private TableColumn<Zona, String> colIdZona;
    @FXML
    private TableColumn<Zona, String> colNombreZona;
    @FXML
    private TableColumn<Zona, Object> colCapacidadZona;
    @FXML
    private TableColumn<Zona, Object> colPrecioZona;
    @FXML
    private TableColumn<Asiento, String> colIdAsiento;
    @FXML
    private TableColumn<Asiento, String> colFilaAsiento;
    @FXML
    private TableColumn<Asiento, Object> colNumeroAsiento;
    @FXML
    private TableColumn<Asiento, String> colEstadoAsiento;
    @FXML
    private Label lblMensaje;

    private final SistemaEventos sistemaEventos = SistemaEventos.getInstancia();

    @FXML
    public void initialize() {
        colIdRecinto.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdRecinto()));
        colNombreRecinto.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colDireccionRecinto.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDireccion()));
        colCiudadRecinto.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCiudad()));

        colIdZona.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdZona()));
        colNombreZona.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colCapacidadZona.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCapacidad()));
        colPrecioZona.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPrecioBase()));

        colIdAsiento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdAsiento()));
        colFilaAsiento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFila()));
        colNumeroAsiento.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNumero()));
        colEstadoAsiento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstadoAsiento().name()));

        tablaRecintos.getSelectionModel().selectedItemProperty().addListener((obs, o, recinto) -> {
            cargarFormularioRecinto(recinto);
            cargarZonas();
        });
        tablaZonas.getSelectionModel().selectedItemProperty().addListener((obs, o, zona) -> {
            cargarFormularioZona(zona);
            cargarAsientos();
        });

        if (!Sesion.esAdministrador()) {
            lblMensaje.setText("Acceso denegado. Vista disponible solo para administradores.");
            return;
        }

        configurarCamposNumericos();
        cargarRecintos();
    }

    @FXML
    private void crearRecinto() {
        if (!validarRecinto()) {
            return;
        }
        Recinto recinto = new Recinto(
                "REC-" + System.currentTimeMillis(),
                txtNombreRecinto.getText().trim(),
                txtDireccionRecinto.getText().trim(),
                txtCiudadRecinto.getText().trim()
        );
        sistemaEventos.agregarRecinto(recinto);
        cargarRecintos();
        lblMensaje.setText("Recinto creado correctamente.");
    }

    @FXML
    private void actualizarRecinto() {
        Recinto recinto = tablaRecintos.getSelectionModel().getSelectedItem();
        if (recinto == null) {
            lblMensaje.setText("Seleccione un recinto.");
            return;
        }
        if (!validarRecinto()) {
            return;
        }
        recinto.setNombre(txtNombreRecinto.getText().trim());
        recinto.setDireccion(txtDireccionRecinto.getText().trim());
        recinto.setCiudad(txtCiudadRecinto.getText().trim());
        cargarRecintos();
        lblMensaje.setText("Recinto actualizado correctamente.");
    }

    @FXML
    private void eliminarRecinto() {
        Recinto recinto = tablaRecintos.getSelectionModel().getSelectedItem();
        if (recinto == null) {
            lblMensaje.setText("Seleccione un recinto.");
            return;
        }
        sistemaEventos.eliminarRecinto(recinto);
        tablaZonas.setItems(FXCollections.observableArrayList());
        tablaAsientos.setItems(FXCollections.observableArrayList());
        cargarRecintos();
        lblMensaje.setText("Recinto eliminado correctamente.");
    }

    @FXML
    private void crearZona() {
        Recinto recinto = tablaRecintos.getSelectionModel().getSelectedItem();
        if (recinto == null) {
            lblMensaje.setText("Seleccione un recinto para agregar la zona.");
            return;
        }
        if (!validarZona()) {
            return;
        }
        int capacidad = Integer.parseInt(txtCapacidadZona.getText().trim());
        double precio = Double.parseDouble(txtPrecioZona.getText().trim());
        Zona zona = new Zona("ZON-" + System.currentTimeMillis(), txtNombreZona.getText().trim(), capacidad, precio);
        for (int i = 1; i <= capacidad; i++) {
            zona.agregarAsiento(new Asiento(zona.getIdZona() + "-ASI-" + i, "A", i));
        }
        recinto.agregarZona(zona);
        cargarZonas();
        lblMensaje.setText("Zona creada correctamente.");
    }

    @FXML
    private void actualizarZona() {
        Zona zona = tablaZonas.getSelectionModel().getSelectedItem();
        if (zona == null) {
            lblMensaje.setText("Seleccione una zona.");
            return;
        }
        if (!validarZona()) {
            return;
        }
        zona.setNombre(txtNombreZona.getText().trim());
        zona.setCapacidad(Integer.parseInt(txtCapacidadZona.getText().trim()));
        zona.setPrecioBase(Double.parseDouble(txtPrecioZona.getText().trim()));
        cargarZonas();
        lblMensaje.setText("Zona actualizada correctamente.");
    }

    @FXML
    private void eliminarZona() {
        Recinto recinto = tablaRecintos.getSelectionModel().getSelectedItem();
        Zona zona = tablaZonas.getSelectionModel().getSelectedItem();
        if (recinto == null || zona == null) {
            lblMensaje.setText("Seleccione una zona.");
            return;
        }
        recinto.eliminarZona(zona);
        tablaAsientos.setItems(FXCollections.observableArrayList());
        cargarZonas();
        lblMensaje.setText("Zona eliminada correctamente.");
    }

    @FXML
    private void cargarZonas() {
        Recinto recinto = tablaRecintos.getSelectionModel().getSelectedItem();
        if (recinto == null) {
            tablaZonas.setItems(FXCollections.observableArrayList());
            return;
        }
        tablaZonas.setItems(FXCollections.observableArrayList(recinto.getZonas()));
    }

    @FXML
    private void cargarAsientos() {
        Zona zona = tablaZonas.getSelectionModel().getSelectedItem();
        if (zona == null) {
            tablaAsientos.setItems(FXCollections.observableArrayList());
            return;
        }
        tablaAsientos.setItems(FXCollections.observableArrayList(zona.consultarTodosLosAsientos()));
    }

    @FXML
    private void bloquearAsiento() {
        Asiento asiento = tablaAsientos.getSelectionModel().getSelectedItem();
        if (asiento == null) {
            lblMensaje.setText("Seleccione un asiento.");
            return;
        }
        asiento.inhabilitar();
        sistemaEventos.registrarIncidencia(TipoIncidencia.BLOQUEO_ASIENTO, "Se bloqueo el asiento " + asiento.getIdAsiento());
        tablaAsientos.refresh();
        lblMensaje.setText("Asiento bloqueado.");
    }

    @FXML
    private void liberarAsiento() {
        Asiento asiento = tablaAsientos.getSelectionModel().getSelectedItem();
        if (asiento == null) {
            lblMensaje.setText("Seleccione un asiento.");
            return;
        }
        if (asiento.getEstadoAsiento() == EstadoAsiento.VENDIDO) {
            lblMensaje.setText("Un asiento vendido no puede liberarse directamente.");
            return;
        }
        asiento.liberar();
        tablaAsientos.refresh();
        lblMensaje.setText("Asiento liberado.");
    }

    @FXML
    private void disponibilizarAsiento() {
        Asiento asiento = tablaAsientos.getSelectionModel().getSelectedItem();
        if (asiento == null) {
            lblMensaje.setText("Seleccione un asiento.");
            return;
        }
        if (asiento.getEstadoAsiento() == EstadoAsiento.VENDIDO) {
            lblMensaje.setText("Un asiento vendido no puede pasar a disponible.");
            return;
        }
        asiento.setEstadoAsiento(EstadoAsiento.DISPONIBLE);
        tablaAsientos.refresh();
        lblMensaje.setText("Asiento marcado como disponible.");
    }

    private void cargarRecintos() {
        tablaRecintos.setItems(FXCollections.observableArrayList(sistemaEventos.getRecintos()));
    }

    private boolean validarRecinto() {
        if (txtNombreRecinto.getText() == null || txtNombreRecinto.getText().isBlank()) {
            lblMensaje.setText("Debe ingresar el nombre del recinto.");
            return false;
        }
        if (txtDireccionRecinto.getText() == null || txtDireccionRecinto.getText().isBlank()) {
            lblMensaje.setText("Debe ingresar la direccion del recinto.");
            return false;
        }
        if (txtCiudadRecinto.getText() == null || txtCiudadRecinto.getText().isBlank()) {
            lblMensaje.setText("Debe ingresar la ciudad del recinto.");
            return false;
        }
        return true;
    }

    private boolean validarZona() {
        if (txtNombreZona.getText() == null || txtNombreZona.getText().isBlank()) {
            lblMensaje.setText("Debe ingresar el nombre de la zona.");
            return false;
        }
        if (txtCapacidadZona.getText() == null || txtCapacidadZona.getText().isBlank()) {
            lblMensaje.setText("Debe ingresar la capacidad de la zona.");
            return false;
        }
        if (txtPrecioZona.getText() == null || txtPrecioZona.getText().isBlank()) {
            lblMensaje.setText("Debe ingresar el precio base de la zona.");
            return false;
        }
        return true;
    }

    private void cargarFormularioRecinto(Recinto recinto) {
        if (recinto == null) {
            return;
        }
        txtNombreRecinto.setText(recinto.getNombre());
        txtDireccionRecinto.setText(recinto.getDireccion());
        txtCiudadRecinto.setText(recinto.getCiudad());
    }

    private void cargarFormularioZona(Zona zona) {
        if (zona == null) {
            return;
        }
        txtNombreZona.setText(zona.getNombre());
        txtCapacidadZona.setText(String.valueOf(zona.getCapacidad()));
        txtPrecioZona.setText(String.valueOf(zona.getPrecioBase()));
    }

    private void configurarCamposNumericos() {
        txtCapacidadZona.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() && !newValue.matches("\\d*")) {
                txtCapacidadZona.setText(oldValue);
            }
        });
        txtPrecioZona.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() && !newValue.matches("\\d*(\\.\\d*)?")) {
                txtPrecioZona.setText(oldValue);
            }
        });
    }
}
