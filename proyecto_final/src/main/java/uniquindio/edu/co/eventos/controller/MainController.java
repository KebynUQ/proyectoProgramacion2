package uniquindio.edu.co.eventos.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.Usuario;

import java.io.IOException;

public class MainController {

    @FXML
    private Label lblUsuarioActual;

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        Usuario usuario = Sesion.getUsuarioActual();

        if (usuario != null) {
            lblUsuarioActual.setText(usuario.getNombreCompleto());
        } else if (Sesion.getAdministradorActual() != null) {
            lblUsuarioActual.setText(Sesion.getAdministradorActual().getNombreCompleto());
        } else {
            lblUsuarioActual.setText("Sin sesion activa");
        }

        mostrarEventos();
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
        cargarVista("UsuariosView.fxml");
    }

    @FXML
    private void mostrarReportes() {
        cargarVista("ReportesView.fxml");
    }

    @FXML
    private void mostrarIncidencias() {
        cargarVista("IncidenciasView.fxml");
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

    private void cargarVista(String vista) {
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
}
