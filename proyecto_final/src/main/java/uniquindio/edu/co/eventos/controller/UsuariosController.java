package uniquindio.edu.co.eventos.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.model.Usuario;

import java.util.ArrayList;

public class UsuariosController {

    @FXML
    private TextField txtBuscarUsuario;

    @FXML
    private TextField txtNombreUsuario;

    @FXML
    private TextField txtCorreoUsuario;

    @FXML
    private TextField txtTelefonoUsuario;

    @FXML
    private TextField txtContrasenaUsuario;

    @FXML
    private TableView<Usuario> tablaUsuarios;

    @FXML
    private TableColumn<Usuario, String> colIdUsuario;

    @FXML
    private TableColumn<Usuario, String> colNombre;

    @FXML
    private TableColumn<Usuario, String> colCorreo;

    @FXML
    private TableColumn<Usuario, String> colTelefono;

    @FXML
    private Label lblMensaje;

    private final SistemaEventos sistemaEventos = SistemaEventos.getInstancia();

    @FXML
    public void initialize() {
        colIdUsuario.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdUsuario()));
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreCompleto()));
        colCorreo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCorreo()));
        colTelefono.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefono()));

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> cargarFormulario(newValue));

        if (!Sesion.esAdministrador()) {
            tablaUsuarios.setItems(FXCollections.observableArrayList());
            lblMensaje.setText("Acceso denegado. Vista disponible solo para administradores.");
            deshabilitarVista();
            return;
        }

        cargarUsuarios();
    }

    @FXML
    private void buscarUsuario() {
        if (!Sesion.esAdministrador()) {
            lblMensaje.setText("Acceso denegado.");
            return;
        }

        String texto = txtBuscarUsuario.getText() == null ? "" : txtBuscarUsuario.getText().trim().toLowerCase();
        ArrayList<Usuario> resultado = new ArrayList<>();

        for (Usuario usuario : sistemaEventos.getUsuarios()) {
            if (usuario.getNombreCompleto().toLowerCase().contains(texto)
                    || usuario.getCorreo().toLowerCase().contains(texto)) {
                resultado.add(usuario);
            }
        }

        tablaUsuarios.setItems(FXCollections.observableArrayList(resultado));
        lblMensaje.setText("Usuarios encontrados: " + resultado.size());
    }

    @FXML
    private void cargarUsuarios() {
        if (!Sesion.esAdministrador()) {
            lblMensaje.setText("Acceso denegado.");
            return;
        }

        tablaUsuarios.setItems(FXCollections.observableArrayList(sistemaEventos.getUsuarios()));
        lblMensaje.setText("Tabla actualizada.");
    }

    @FXML
    private void crearUsuario() {
        if (!validarFormulario(true)) {
            return;
        }

        if (sistemaEventos.buscarUsuarioPorCorreo(txtCorreoUsuario.getText().trim()) != null
                || sistemaEventos.buscarAdministradorPorCorreo(txtCorreoUsuario.getText().trim()) != null) {
            lblMensaje.setText("Ya existe un usuario con ese correo.");
            return;
        }

        Usuario usuario = new Usuario(
                "USU-" + System.currentTimeMillis(),
                txtNombreUsuario.getText().trim(),
                txtCorreoUsuario.getText().trim(),
                txtTelefonoUsuario.getText().trim(),
                txtContrasenaUsuario.getText().trim()
        );

        sistemaEventos.registrarUsuario(usuario);
        cargarUsuarios();
        limpiarFormulario();
        lblMensaje.setText("Usuario creado correctamente.");
    }

    @FXML
    private void actualizarUsuario() {
        Usuario usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuario == null) {
            lblMensaje.setText("Seleccione un usuario para actualizar.");
            return;
        }

        if (!validarFormulario(false)) {
            return;
        }

        boolean actualizado = sistemaEventos.actualizarUsuario(
                usuario,
                txtNombreUsuario.getText().trim(),
                txtCorreoUsuario.getText().trim(),
                txtTelefonoUsuario.getText().trim(),
                txtContrasenaUsuario.getText().trim()
        );

        if (!actualizado) {
            lblMensaje.setText("Ya existe un usuario con ese correo.");
            return;
        }

        cargarUsuarios();
        lblMensaje.setText("Usuario actualizado correctamente.");
    }

    @FXML
    private void eliminarUsuario() {
        Usuario usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuario == null) {
            lblMensaje.setText("Seleccione un usuario para eliminar.");
            return;
        }

        sistemaEventos.eliminarUsuario(usuario);
        cargarUsuarios();
        limpiarFormulario();
        lblMensaje.setText("Usuario eliminado correctamente.");
    }

    @FXML
    private void limpiarFormulario() {
        txtNombreUsuario.clear();
        txtCorreoUsuario.clear();
        txtTelefonoUsuario.clear();
        txtContrasenaUsuario.clear();
        tablaUsuarios.getSelectionModel().clearSelection();
    }

    @FXML
    private void verComprasUsuario() {
        Usuario usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        lblMensaje.setText(usuario == null ? "Seleccione un usuario." : "Compras registradas: " + usuario.getCompras().size());
    }

    private boolean validarFormulario(boolean validarContrasena) {
        if (txtNombreUsuario.getText() == null || txtNombreUsuario.getText().isBlank()) {
            lblMensaje.setText("Debe ingresar el nombre del usuario.");
            return false;
        }
        if (txtCorreoUsuario.getText() == null || txtCorreoUsuario.getText().isBlank()) {
            lblMensaje.setText("Debe ingresar el correo del usuario.");
            return false;
        }
        if (txtTelefonoUsuario.getText() == null || txtTelefonoUsuario.getText().isBlank()) {
            lblMensaje.setText("Debe ingresar el telefono del usuario.");
            return false;
        }
        if (validarContrasena && (txtContrasenaUsuario.getText() == null || txtContrasenaUsuario.getText().isBlank())) {
            lblMensaje.setText("Debe ingresar la contrasena del usuario.");
            return false;
        }
        return true;
    }

    private void cargarFormulario(Usuario usuario) {
        if (usuario == null) {
            return;
        }
        txtNombreUsuario.setText(usuario.getNombreCompleto());
        txtCorreoUsuario.setText(usuario.getCorreo());
        txtTelefonoUsuario.setText(usuario.getTelefono());
        txtContrasenaUsuario.setText(usuario.getContrasena());
    }

    private void deshabilitarVista() {
        txtBuscarUsuario.setDisable(true);
        txtNombreUsuario.setDisable(true);
        txtCorreoUsuario.setDisable(true);
        txtTelefonoUsuario.setDisable(true);
        txtContrasenaUsuario.setDisable(true);
        tablaUsuarios.setDisable(true);
    }
}
