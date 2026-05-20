package uniquindio.edu.co.eventos.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.Incidencia;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.model.enums.TipoIncidencia;

import java.util.ArrayList;

public class IncidenciasController {

    @FXML
    private ComboBox<TipoIncidencia> cmbTipoIncidencia;

    @FXML
    private DatePicker dpFechaIncidencia;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TableView<Incidencia> tablaIncidencias;

    @FXML
    private TableColumn<Incidencia, String> colIdIncidencia;

    @FXML
    private TableColumn<Incidencia, String> colTipo;

    @FXML
    private TableColumn<Incidencia, String> colDescripcion;

    @FXML
    private TableColumn<Incidencia, Object> colFecha;

    @FXML
    private Label lblMensaje;

    private final SistemaEventos sistemaEventos = SistemaEventos.getInstancia();

    @FXML
    public void initialize() {
        cmbTipoIncidencia.setItems(FXCollections.observableArrayList(TipoIncidencia.values()));

        colIdIncidencia.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdIncidencia()));
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipo().name()));
        colDescripcion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));
        colFecha.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFecha()));

        if (!Sesion.esAdministrador()) {
            cmbTipoIncidencia.setDisable(true);
            txtDescripcion.setDisable(true);
            tablaIncidencias.setDisable(true);
            tablaIncidencias.setItems(FXCollections.observableArrayList());
            lblMensaje.setText("Acceso denegado. Vista disponible solo para administradores.");
            return;
        }

        cargarIncidencias();
    }

    @FXML
    private void filtrarPorTipo() {
        if (!Sesion.esAdministrador()) {
            lblMensaje.setText("Acceso denegado.");
            return;
        }

        TipoIncidencia tipo = cmbTipoIncidencia.getValue();

        if (tipo == null) {
            cargarIncidencias();
            return;
        }

        ArrayList<Incidencia> resultado = new ArrayList<>();

        for (Incidencia incidencia : sistemaEventos.getIncidencias()) {
            if (incidencia.getTipo() == tipo) {
                resultado.add(incidencia);
            }
        }

        tablaIncidencias.setItems(FXCollections.observableArrayList(resultado));
        lblMensaje.setText("Incidencias filtradas: " + resultado.size());
    }

    @FXML
    private void cargarIncidencias() {
        if (!Sesion.esAdministrador()) {
            lblMensaje.setText("Acceso denegado.");
            tablaIncidencias.setItems(FXCollections.observableArrayList());
            return;
        }

        tablaIncidencias.setItems(FXCollections.observableArrayList(sistemaEventos.getIncidencias()));
        lblMensaje.setText("Incidencias cargadas.");
    }

    @FXML
    private void filtrarPorFecha() {
        if (!Sesion.esAdministrador()) {
            lblMensaje.setText("Acceso denegado.");
            return;
        }

        if (dpFechaIncidencia.getValue() == null) {
            cargarIncidencias();
            return;
        }

        ArrayList<Incidencia> resultado = new ArrayList<>();
        for (Incidencia incidencia : sistemaEventos.getIncidencias()) {
            if (incidencia.getFecha() != null && incidencia.getFecha().toLocalDate().equals(dpFechaIncidencia.getValue())) {
                resultado.add(incidencia);
            }
        }

        tablaIncidencias.setItems(FXCollections.observableArrayList(resultado));
        lblMensaje.setText("Incidencias filtradas por fecha: " + resultado.size());
    }

    @FXML
    private void registrarIncidencia() {
        if (!Sesion.esAdministrador()) {
            lblMensaje.setText("Acceso denegado.");
            return;
        }

        String descripcion = txtDescripcion.getText();
        TipoIncidencia tipo = cmbTipoIncidencia.getValue();

        if (descripcion == null || descripcion.isBlank()) {
            lblMensaje.setText("Ingrese una descripcion.");
            return;
        }

        if (tipo == null) {
            tipo = TipoIncidencia.OTRO;
        }

        Incidencia incidencia = new Incidencia(
                "INC-" + System.currentTimeMillis(),
                tipo,
                descripcion
        );

        sistemaEventos.agregarIncidencia(incidencia);
        txtDescripcion.clear();
        cargarIncidencias();
        lblMensaje.setText("Incidencia registrada.");
    }
}
