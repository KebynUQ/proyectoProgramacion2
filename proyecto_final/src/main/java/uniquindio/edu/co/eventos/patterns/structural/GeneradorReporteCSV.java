package uniquindio.edu.co.eventos.patterns.structural;

import uniquindio.edu.co.eventos.model.Compra;

import java.util.ArrayList;

public class GeneradorReporteCSV implements GeneradorReporte {

    @Override
    public String generarReporte(ArrayList<Compra> compras) {
        StringBuilder reporte = new StringBuilder();

        reporte.append("ID_COMPRA,USUARIO,TOTAL,ESTADO\n");

        for (Compra compra : compras) {
            reporte.append(compra.getIdCompra()).append(",");
            reporte.append(compra.getUsuario().getNombreCompleto()).append(",");
            reporte.append(compra.getTotal()).append(",");
            reporte.append(compra.getEstadoCompra()).append("\n");
        }

        return reporte.toString();
    }
}
