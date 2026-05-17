package uniquindio.edu.co.eventos.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import uniquindio.edu.co.eventos.model.Administrador;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.Usuario;

public class PerfilController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtTelefono;

    @FXML
    private Button btnActualizarPerfil;

    @FXML
    private Label lblHistorialCompras;

    @FXML
    private TableView<Compra> tablaHistorialCompras;

    @FXML
    private TableColumn<Compra, String> colIdCompra;

    @FXML
    private TableColumn<Compra, String> colEvento;

    @FXML
    private TableColumn<Compra, Object> colTotal;

    @FXML
    private TableColumn<Compra, String> colEstado;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
        colIdCompra.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdCompra()));
        colEvento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEvento().getNombre()));
        colTotal.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTotal()));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstadoCompra().name()));

        configurarCamposNumericos();
        cargarPerfil();
    }

    @FXML
    private void actualizarPerfil() {
        Usuario usuario = Sesion.getUsuarioActual();
        Administrador administrador = Sesion.getAdministradorActual();

        if (usuario != null) {
            if (txtTelefono.getText() == null || txtTelefono.getText().trim().isBlank()) {
                lblMensaje.setText("El telefono debe contener solo numeros.");
                return;
            }

            usuario.actualizarPerfil(txtNombre.getText(), txtTelefono.getText());
            usuario.setCorreo(txtCorreo.getText());
            lblMensaje.setText("Perfil actualizado.");
            cargarPerfil();
            return;
        }

        if (administrador != null) {
            administrador.setNombreCompleto(txtNombre.getText());
            administrador.setCorreo(txtCorreo.getText());
            lblMensaje.setText("Perfil actualizado.");
            cargarPerfil();
            return;
        }

        lblMensaje.setText("No hay una sesion activa.");
    }

    private void cargarPerfil() {
        Usuario usuario = Sesion.getUsuarioActual();
        Administrador administrador = Sesion.getAdministradorActual();

        if (usuario != null) {
            txtNombre.setText(usuario.getNombreCompleto());
            txtCorreo.setText(usuario.getCorreo());
            txtTelefono.setText(usuario.getTelefono());
            txtTelefono.setDisable(false);
            mostrarHistorialCompras(true);
            tablaHistorialCompras.setItems(FXCollections.observableArrayList(usuario.getCompras()));
            return;
        }

        if (administrador != null) {
            txtNombre.setText(administrador.getNombreCompleto());
            txtCorreo.setText(administrador.getCorreo());
            txtTelefono.setText("No aplica");
            txtTelefono.setDisable(true);
            mostrarHistorialCompras(false);
            tablaHistorialCompras.setItems(FXCollections.observableArrayList());
            lblMensaje.setText("Perfil de administrador cargado.");
            return;
        }

        lblMensaje.setText("No hay una sesion activa.");
        tablaHistorialCompras.setItems(FXCollections.observableArrayList());
    }

    private void mostrarHistorialCompras(boolean visible) {
        lblHistorialCompras.setVisible(visible);
        lblHistorialCompras.setManaged(visible);
        tablaHistorialCompras.setVisible(visible);
        tablaHistorialCompras.setManaged(visible);
    }

    private void configurarCamposNumericos() {
        txtTelefono.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }

            if (!newValue.matches("\\d*")) {
                txtTelefono.setText(oldValue);
            }
        });
    }
}
