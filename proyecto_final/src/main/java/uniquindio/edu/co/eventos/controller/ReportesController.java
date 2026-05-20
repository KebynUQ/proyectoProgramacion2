package uniquindio.edu.co.eventos.controller;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Entrada;
import uniquindio.edu.co.eventos.model.Sesion;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.enums.EstadoCompra;
import uniquindio.edu.co.eventos.model.Zona;
import uniquindio.edu.co.eventos.patterns.structural.ServicioAdicional;
import uniquindio.edu.co.eventos.patterns.structural.ReporteAdapterCSV;
import uniquindio.edu.co.eventos.patterns.structural.ReporteAdapterPDF;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportesController {

    @FXML
    private TextArea txtReporte;

    @FXML
    private Label lblMensaje;

    @FXML
    private BarChart<String, Number> barChartEventos;

    @FXML
    private PieChart pieChartEstados;

    @FXML
    private LineChart<String, Number> lineChartVentas;

    private final SistemaEventos sistemaEventos = SistemaEventos.getInstancia();
    private final ReporteAdapterCSV adapterCSV = new ReporteAdapterCSV();
    private final ReporteAdapterPDF adapterPDF = new ReporteAdapterPDF();

    @FXML
    public void initialize() {
        txtReporte.setDisable(false);
    }

    @FXML
    private void generarReporteCSV() {
        if (!Sesion.haySesionActiva()) {
            lblMensaje.setText("No hay sesión activa.");
            return;
        }

        ArrayList<Compra> compras = obtenerComprasSegunSesion();
        if (compras.isEmpty()) {
            lblMensaje.setText("No hay compras para generar el reporte.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar reporte CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo CSV", "*.csv"));
        fileChooser.setInitialFileName(Sesion.esAdministrador() ? "reporte_general_compras.csv" : "reporte_compras_usuario.csv");

        File archivo = fileChooser.showSaveDialog(txtReporte.getScene().getWindow());
        if (archivo == null) {
            return;
        }

        try {
            adapterCSV.exportarArchivo(compras, archivo);
            txtReporte.setText(adapterCSV.exportar(compras));
            lblMensaje.setText("Reporte CSV generado correctamente.");
        } catch (IOException e) {
            lblMensaje.setText("No se pudo generar el reporte.");
        }
    }

    @FXML
    private void generarReportePDF() {
        if (!Sesion.haySesionActiva()) {
            lblMensaje.setText("No hay sesión activa.");
            return;
        }

        ArrayList<Compra> compras = obtenerComprasSegunSesion();
        if (compras.isEmpty()) {
            lblMensaje.setText("No hay compras para generar el reporte.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar reporte PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf"));
        fileChooser.setInitialFileName(Sesion.esAdministrador() ? "reporte_general_compras.pdf" : "reporte_compras_usuario.pdf");

        File archivo = fileChooser.showSaveDialog(txtReporte.getScene().getWindow());
        if (archivo == null) {
            return;
        }

        try {
            adapterPDF.exportarArchivo(compras, archivo, Sesion.esAdministrador(), Sesion.getUsuarioActual());
            txtReporte.setText(adapterPDF.exportar(compras));
            lblMensaje.setText("Reporte PDF generado correctamente.");
        } catch (IOException e) {
            lblMensaje.setText("No se pudo generar el reporte.");
        }
    }

    @FXML
    private void mostrarMetricas() {
        if (!Sesion.haySesionActiva()) {
            lblMensaje.setText("No hay sesión activa.");
            return;
        }

        ArrayList<Compra> compras = obtenerComprasSegunSesion();
        if (compras.isEmpty()) {
            lblMensaje.setText("No hay compras para generar el reporte.");
            txtReporte.setText("");
            return;
        }

        int totalCompras = compras.size();
        int comprasPagadas = 0;
        int comprasCanceladas = 0;
        int comprasPendientes = 0;
        double totalRecaudado = 0;
        Map<String, Integer> comprasPorEvento = new HashMap<>();
        Map<String, Double> ventasPorFecha = new HashMap<>();
        double ingresosServicios = 0;
        String ocupacionZona = "No registrado";

        for (Compra compra : compras) {
            totalRecaudado += compra.getTotal();
            String evento = compra.getEvento() == null ? "No registrado" : compra.getEvento().getNombre();
            comprasPorEvento.put(evento, comprasPorEvento.getOrDefault(evento, 0) + 1);
            String fecha = compra.getFechaCreacion() == null ? "No registrada" : compra.getFechaCreacion().toLocalDate().toString();
            ventasPorFecha.put(fecha, ventasPorFecha.getOrDefault(fecha, 0.0) + compra.getTotal());
            ingresosServicios += calcularIngresosServicios(compra);

            if (compra.getEstadoCompra() == EstadoCompra.PAGADA || compra.getEstadoCompra() == EstadoCompra.CONFIRMADA) {
                comprasPagadas++;
            } else if (compra.getEstadoCompra() == EstadoCompra.CANCELADA) {
                comprasCanceladas++;
            } else {
                comprasPendientes++;
            }
        }

        ocupacionZona = calcularOcupacionZona(compras);

        txtReporte.setText(
                "Metricas de compras\n"
                        + "Total de compras: " + totalCompras + "\n"
                        + "Compras pagadas: " + comprasPagadas + "\n"
                        + "Compras canceladas: " + comprasCanceladas + "\n"
                        + "Compras reembolsadas: " + contarPorEstado(compras, EstadoCompra.REEMBOLSADA) + "\n"
                        + "Compras pendientes: " + comprasPendientes + "\n"
                        + "Total recaudado: " + totalRecaudado + " COP\n"
                        + "Evento con mas compras: " + eventoMasComprado(comprasPorEvento) + "\n"
                        + "Ingresos por servicios adicionales: " + ingresosServicios + " COP\n"
                        + "Tasa de cancelacion: " + calcularTasaCancelacion(totalCompras, comprasCanceladas) + "%\n"
                        + "Ocupacion por zona: " + ocupacionZona
        );
        cargarCharts(comprasPorEvento, ventasPorFecha, comprasPagadas, comprasCanceladas, comprasPendientes, contarPorEstado(compras, EstadoCompra.REEMBOLSADA));
        lblMensaje.setText("Metricas actualizadas.");
    }

    private ArrayList<Compra> obtenerComprasSegunSesion() {
        if (Sesion.esAdministrador()) {
            return new ArrayList<>(sistemaEventos.getCompras());
        }

        Usuario usuario = Sesion.getUsuarioActual();
        if (usuario == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(usuario.getCompras());
    }

    private String eventoMasComprado(Map<String, Integer> comprasPorEvento) {
        String nombre = "No registrado";
        int max = 0;

        for (Map.Entry<String, Integer> entry : comprasPorEvento.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                nombre = entry.getKey();
            }
        }

        return nombre;
    }

    private double calcularIngresosServicios(Compra compra) {
        double total = 0;
        for (ServicioAdicional servicio : compra.getServiciosAdicionales()) {
            total += servicio.getPrecio();
        }
        return total;
    }

    private int contarPorEstado(ArrayList<Compra> compras, EstadoCompra estado) {
        int total = 0;
        for (Compra compra : compras) {
            if (compra.getEstadoCompra() == estado) {
                total++;
            }
        }
        return total;
    }

    private double calcularTasaCancelacion(int totalCompras, int comprasCanceladas) {
        if (totalCompras == 0) {
            return 0;
        }
        return (comprasCanceladas * 100.0) / totalCompras;
    }

    private String calcularOcupacionZona(ArrayList<Compra> compras) {
        Map<String, int[]> ocupacion = new HashMap<>();

        for (Compra compra : compras) {
            for (Entrada entrada : compra.getEntradas()) {
                Zona zona = entrada.getZona();
                if (zona == null) {
                    continue;
                }
                int[] valores = ocupacion.computeIfAbsent(zona.getNombre(), k -> new int[]{0, zona.getCapacidad()});
                if (compra.getEstadoCompra() == EstadoCompra.PAGADA || compra.getEstadoCompra() == EstadoCompra.CONFIRMADA) {
                    valores[0]++;
                }
            }
        }

        StringBuilder texto = new StringBuilder();
        for (Map.Entry<String, int[]> entry : ocupacion.entrySet()) {
            if (!texto.isEmpty()) {
                texto.append(" | ");
            }
            texto.append(entry.getKey()).append(": ").append(entry.getValue()[0]).append("/").append(entry.getValue()[1]);
        }
        return texto.isEmpty() ? "No registrada" : texto.toString();
    }

    private void cargarCharts(Map<String, Integer> comprasPorEvento, Map<String, Double> ventasPorFecha,
                              int comprasPagadas, int comprasCanceladas, int comprasPendientes, int comprasReembolsadas) {
        XYChart.Series<String, Number> serieEventos = new XYChart.Series<>();
        serieEventos.setName("Compras");
        for (Map.Entry<String, Integer> entry : comprasPorEvento.entrySet()) {
            serieEventos.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChartEventos.getData().clear();
        barChartEventos.getData().add(serieEventos);

        pieChartEstados.setData(FXCollections.observableArrayList(
                new PieChart.Data("Pagadas", comprasPagadas),
                new PieChart.Data("Canceladas", comprasCanceladas),
                new PieChart.Data("Pendientes", comprasPendientes),
                new PieChart.Data("Reembolsadas", comprasReembolsadas)
        ));

        XYChart.Series<String, Number> serieVentas = new XYChart.Series<>();
        serieVentas.setName("Ventas");
        for (Map.Entry<String, Double> entry : ventasPorFecha.entrySet()) {
            serieVentas.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        lineChartVentas.getData().clear();
        lineChartVentas.getData().add(serieVentas);
    }
}
