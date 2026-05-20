package uniquindio.edu.co.eventos.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import uniquindio.edu.co.eventos.model.Administrador;
import uniquindio.edu.co.eventos.model.Notificacion;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.util.GestorNotificaciones;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NotificacionesController {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    private TableView<Notificacion> tablaNotificaciones;

    @FXML
    private TableColumn<Notificacion, String> colTitulo;

    @FXML
    private TableColumn<Notificacion, String> colMensaje;

    @FXML
    private TableColumn<Notificacion, String> colFecha;

    @FXML
    private TableColumn<Notificacion, String> colTipo;

    @FXML
    private TableColumn<Notificacion, String> colEstado;

    @FXML
    private Label lblMensaje;

    private final GestorNotificaciones gestorNotificaciones = GestorNotificaciones.getInstancia();

    @FXML
    public void initialize() {
        colTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitulo()));
        colMensaje.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMensaje()));
        colFecha.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getFecha() == null ? "Sin fecha" : data.getValue().getFecha().format(FORMATO_FECHA)
        ));
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getTipoNotificacion() == null ? "SISTEMA" : data.getValue().getTipoNotificacion().name()
        ));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isLeida() ? "Leida" : "No leida"));

        cargarNotificaciones();
    }

    @FXML
    private void verDetalle() {
        Notificacion notificacion = tablaNotificaciones.getSelectionModel().getSelectedItem();
        if (notificacion == null) {
            lblMensaje.setText("Seleccione una notificacion.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalle de notificacion");
        alert.setHeaderText(notificacion.getTitulo());
        alert.setContentText(
                "Mensaje: " + notificacion.getMensaje() + "\n"
                        + "Fecha: " + (notificacion.getFecha() == null ? "Sin fecha" : notificacion.getFecha().format(FORMATO_FECHA)) + "\n"
                        + "Tipo: " + (notificacion.getTipoNotificacion() == null ? "SISTEMA" : notificacion.getTipoNotificacion().name()) + "\n"
                        + "Estado: " + (notificacion.isLeida() ? "Leida" : "No leida")
        );
        alert.showAndWait();
    }

    @FXML
    private void marcarComoLeida() {
        Notificacion notificacion = tablaNotificaciones.getSelectionModel().getSelectedItem();
        if (notificacion == null) {
            lblMensaje.setText("Seleccione una notificacion.");
            return;
        }

        boolean exito = gestorNotificaciones.marcarComoLeida(notificacion.getIdNotificacion());
        if (!exito) {
            lblMensaje.setText("No fue posible marcar la notificacion.");
            return;
        }

        cargarNotificaciones();
        lblMensaje.setText("Notificacion marcada como leida.");
    }

    @FXML
    private void actualizarNotificaciones() {
        cargarNotificaciones();
        lblMensaje.setText("Notificaciones actualizadas.");
    }

    @FXML
    private void limpiarLeidas() {
        String idDestino = obtenerIdSesionActual();
        if (idDestino == null) {
            tablaNotificaciones.setItems(FXCollections.observableArrayList(new ArrayList<>()));
            lblMensaje.setText("No hay sesion activa.");
            return;
        }

        gestorNotificaciones.limpiarLeidas(idDestino);
        cargarNotificaciones();
        lblMensaje.setText("Notificaciones leidas eliminadas.");
    }

    private void cargarNotificaciones() {
        String idDestino = obtenerIdSesionActual();
        if (idDestino == null) {
            tablaNotificaciones.setItems(FXCollections.observableArrayList(new ArrayList<>()));
            lblMensaje.setText("No hay sesion activa.");
            return;
        }

        ArrayList<Notificacion> notificaciones = gestorNotificaciones.listarPorUsuario(idDestino);
        tablaNotificaciones.setItems(FXCollections.observableArrayList(notificaciones));
        lblMensaje.setText("Tienes " + gestorNotificaciones.contarNoLeidas(idDestino) + " notificaciones sin leer.");
    }

    private String obtenerIdSesionActual() {
        Usuario usuario = Sesion.getUsuarioActual();
        if (usuario != null) {
            return usuario.getIdUsuario();
        }

        Administrador administrador = Sesion.getAdministradorActual();
        if (administrador != null) {
            return administrador.getIdAdministrador();
        }

        return null;
    }
}
