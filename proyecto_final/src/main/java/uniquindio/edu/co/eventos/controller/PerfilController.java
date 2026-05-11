package uniquindio.edu.co.eventos.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

        cargarPerfil();
    }

    @FXML
    private void actualizarPerfil() {
        Usuario usuario = Sesion.getUsuarioActual();

        if (usuario == null) {
            lblMensaje.setText("No hay un usuario activo.");
            return;
        }

        usuario.actualizarPerfil(txtNombre.getText(), txtTelefono.getText());
        usuario.setCorreo(txtCorreo.getText());
        lblMensaje.setText("Perfil actualizado.");
        cargarPerfil();
    }

    private void cargarPerfil() {
        Usuario usuario = Sesion.getUsuarioActual();

        if (usuario == null) {
            lblMensaje.setText("Perfil disponible solo para usuarios.");
            tablaHistorialCompras.setItems(FXCollections.observableArrayList());
            return;
        }

        txtNombre.setText(usuario.getNombreCompleto());
        txtCorreo.setText(usuario.getCorreo());
        txtTelefono.setText(usuario.getTelefono());
        tablaHistorialCompras.setItems(FXCollections.observableArrayList(usuario.getCompras()));
    }
}
