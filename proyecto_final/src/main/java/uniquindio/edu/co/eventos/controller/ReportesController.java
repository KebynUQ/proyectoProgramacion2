package uniquindio.edu.co.eventos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.PanelMetricas;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.patterns.structural.GeneradorReporte;
import uniquindio.edu.co.eventos.patterns.structural.GeneradorReporteCSV;
import uniquindio.edu.co.eventos.patterns.structural.GeneradorReportePDF;

public class ReportesController {

    @FXML
    private TextArea txtReporte;

    @FXML
    private Label lblMensaje;

    private final SistemaEventos sistemaEventos = SistemaEventos.getInstancia();

    @FXML
    public void initialize() {
        if (!Sesion.esAdministrador()) {
            txtReporte.setDisable(true);
            txtReporte.setText("");
            lblMensaje.setText("Acceso denegado. Vista disponible solo para administradores.");
        }
    }

    @FXML
    private void generarReporteCSV() {
        if (!Sesion.esAdministrador()) {
            lblMensaje.setText("Acceso denegado.");
            return;
        }

        GeneradorReporte generador = new GeneradorReporteCSV();
        txtReporte.setText(generador.generarReporte(sistemaEventos.getCompras()));
        lblMensaje.setText("Reporte CSV generado.");
    }

    @FXML
    private void generarReportePDF() {
        if (!Sesion.esAdministrador()) {
            lblMensaje.setText("Acceso denegado.");
            return;
        }

        GeneradorReporte generador = new GeneradorReportePDF();
        txtReporte.setText(generador.generarReporte(sistemaEventos.getCompras()));
        lblMensaje.setText("Reporte PDF generado.");
    }

    @FXML
    private void mostrarMetricas() {
        if (!Sesion.esAdministrador()) {
            lblMensaje.setText("Acceso denegado.");
            return;
        }

        PanelMetricas panelMetricas = new PanelMetricas();
        panelMetricas.calcularMetricas(sistemaEventos.getCompras(), sistemaEventos.getIncidencias());
        txtReporte.setText(panelMetricas.visualizarMetricas());
        lblMensaje.setText("Metricas actualizadas.");
    }
}
