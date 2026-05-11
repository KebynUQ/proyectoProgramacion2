package uniquindio.edu.co.eventos.patterns.structural;

import uniquindio.edu.co.eventos.model.Compra;

import java.util.ArrayList;

public class GeneradorReportePDF implements GeneradorReporte {

    @Override
    public String generarReporte(ArrayList<Compra> compras) {
        StringBuilder reporte = new StringBuilder();

        reporte.append("REPORTE PDF SIMULADO\n");
        reporte.append("====================\n");

        for (Compra compra : compras) {
            reporte.append("Compra: ").append(compra.getIdCompra()).append("\n");
            reporte.append("Usuario: ").append(compra.getUsuario().getNombreCompleto()).append("\n");
            reporte.append("Total: $").append(compra.getTotal()).append("\n");
            reporte.append("Estado: ").append(compra.getEstadoCompra()).append("\n");
            reporte.append("--------------------\n");
        }

        return reporte.toString();
    }
}
