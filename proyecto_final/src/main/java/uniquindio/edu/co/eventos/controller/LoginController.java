package uniquindio.edu.co.eventos.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import uniquindio.edu.co.eventos.model.Administrador;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.model.Usuario;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private ComboBox<String> cmbTipoUsuario;

    @FXML
    private Label lblMensaje;

    private final SistemaEventos sistemaEventos = SistemaEventos.getInstancia();

    @FXML
    public void initialize() {
        cmbTipoUsuario.setItems(FXCollections.observableArrayList("Usuario", "Administrador"));
        cmbTipoUsuario.setValue("Usuario");
    }

    @FXML
    private void iniciarSesion() {
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String tipo = cmbTipoUsuario.getValue();

        if (correo == null || correo.isBlank() || contrasena == null || contrasena.isBlank()) {
            lblMensaje.setText("Debe ingresar correo y contrasena.");
            return;
        }

        if ("Usuario".equals(tipo)) {
            Usuario usuario = sistemaEventos.validarLoginUsuario(correo, contrasena);

            if (usuario != null) {
                Sesion.iniciarSesionUsuario(usuario);
                lblMensaje.setText("Inicio de sesion correcto como usuario.");
                abrirMainLayout();
            } else {
                lblMensaje.setText("Correo o contrasena incorrectos.");
            }

        } else {
            Administrador administrador = sistemaEventos.validarLoginAdministrador(correo, contrasena);
            System.out.println(administrador);
            if (administrador != null) {
                Sesion.iniciarSesionAdministrador(administrador);
                lblMensaje.setText("Inicio de sesion correcto como administrador.");
                abrirMainLayout();
            } else {
                lblMensaje.setText("Correo o contrasena incorrectos.");
            }
        }
    } 

    @FXML
    private void registrarUsuario() {
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String tipo = cmbTipoUsuario.getValue();

        if (correo == null || correo.isBlank() || contrasena == null || contrasena.isBlank()) {
            lblMensaje.setText("Ingrese correo y contrasena para registrarse.");
            return;
        }

        Usuario usuarioExistente = sistemaEventos.buscarUsuarioPorCorreo(correo);

        if (usuarioExistente != null) {
            lblMensaje.setText("Ya existe un usuario con ese correo.");
            return;
        }

        if ("Administrador".equals(tipo)) {
            Administrador nuevoAdministrador = new Administrador(
                "ADM-" + System.currentTimeMillis(),
                "Administrador Nuevo",
                correo,
                contrasena
            );
            sistemaEventos.registrarAdministrador(nuevoAdministrador);
            lblMensaje.setText("Administrador registrado correctamente.");
        } else {
            Usuario nuevoUsuario = new Usuario(
                "USU-" + System.currentTimeMillis(),
                "Usuario Nuevo",
                correo,
                "Sin telefono",
                contrasena
            );
            sistemaEventos.registrarUsuario(nuevoUsuario);
            lblMensaje.setText("Usuario registrado correctamente.");
        }
    }

    private void abrirMainLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/uniquindio/edu/co/eventos/view/MainLayout.fxml")
            );
            Scene scene = new Scene(loader.load(), 1100, 700);
            Stage stage = (Stage) txtCorreo.getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            lblMensaje.setText("No fue posible cargar la vista principal.");
        }
    }
}
