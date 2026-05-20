package uniquindio.edu.co.eventos.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import uniquindio.edu.co.eventos.model.Administrador;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.Usuario;

import java.io.IOException;

public class MainController {

    private static MainController instancia;

    @FXML
    private Label lblUsuarioActual;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnMenuEventos;

    @FXML
    private Button btnMenuCompras;

    @FXML
    private Button btnMenuUsuarios;

    @FXML
    private Button btnMenuRecintos;

    @FXML
    private Button btnMenuReportes;

    @FXML
    private Button btnMenuIncidencias;

    @FXML
    private Button btnMenuNotificaciones;

    @FXML
    private Button btnMenuPerfil;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    public void initialize() {
        instancia = this;
        configurarMenuSegunSesion();
        mostrarEventos();
    }

    private void configurarMenuSegunSesion() {
        Usuario usuario = Sesion.getUsuarioActual();
        Administrador administrador = Sesion.getAdministradorActual();

        if (Sesion.esAdministrador() && administrador != null) {
            lblUsuarioActual.setText("Administrador: " + administrador.getNombreCompleto());
            btnMenuCompras.setText("Compras");
            mostrarBoton(btnMenuUsuarios, true);
            mostrarBoton(btnMenuRecintos, true);
            mostrarBoton(btnMenuReportes, true);
            mostrarBoton(btnMenuIncidencias, true);
            mostrarBoton(btnMenuNotificaciones, true);
        } else if (usuario != null) {
            lblUsuarioActual.setText("Usuario: " + usuario.getNombreCompleto());
            btnMenuCompras.setText("Mis compras");
            mostrarBoton(btnMenuUsuarios, false);
            mostrarBoton(btnMenuRecintos, false);
            mostrarBoton(btnMenuReportes, true);
            mostrarBoton(btnMenuIncidencias, false);
            mostrarBoton(btnMenuNotificaciones, true);
        } else {
            lblUsuarioActual.setText("Sin sesion activa");
            btnMenuCompras.setText("Compras");
            mostrarBoton(btnMenuUsuarios, false);
            mostrarBoton(btnMenuRecintos, false);
            mostrarBoton(btnMenuReportes, false);
            mostrarBoton(btnMenuIncidencias, false);
            mostrarBoton(btnMenuNotificaciones, false);
        }
    }

    @FXML
    private void mostrarEventos() {
        cargarVista("EventosView.fxml");
    }

    @FXML
    private void mostrarCompras() {
        cargarVista("ComprasView.fxml");
    }

    @FXML
    private void mostrarUsuarios() {
        if (!Sesion.esAdministrador()) {
            mostrarEventos();
            return;
        }
        cargarVista("UsuariosView.fxml");
    }

    @FXML
    private void mostrarRecintos() {
        if (!Sesion.esAdministrador()) {
            mostrarEventos();
            return;
        }
        cargarVista("RecintosView.fxml");
    }

    @FXML
    private void mostrarReportes() {
        cargarVista("ReportesView.fxml");
    }

    @FXML
    private void mostrarIncidencias() {
        if (!Sesion.esAdministrador()) {
            mostrarEventos();
            return;
        }
        cargarVista("IncidenciasView.fxml");
    }

    @FXML
    private void mostrarNotificaciones() {
        if (!Sesion.haySesionActiva()) {
            mostrarEventos();
            return;
        }
        cargarVista("NotificacionesView.fxml");
    }

    @FXML
    private void mostrarPerfil() {
        cargarVista("PerfilView.fxml");
    }

    @FXML
    private void cerrarSesion() {
        Sesion.cerrarSesion();

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/uniquindio/edu/co/eventos/view/LoginView.fxml")
            );
            Scene scene = new Scene(loader.load(), 900, 600);
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarVista(String vista) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/uniquindio/edu/co/eventos/view/" + vista)
            );
            Node node = loader.load();
            contentArea.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void abrirVistaCentral(String vista) {
        if (instancia != null) {
            instancia.cargarVista(vista);
        }
    }

    private void mostrarBoton(Button button, boolean visible) {
        button.setVisible(visible);
        button.setManaged(visible);
    }
}
