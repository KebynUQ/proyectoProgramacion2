package uniquindio.edu.co.eventos.patterns.structural;

import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Pago;

import java.util.ArrayList;

public class GeneradorReportePDF implements GeneradorReporte {

    @Override
    public String generarReporte(ArrayList<Compra> compras) {
        StringBuilder reporte = new StringBuilder();

        reporte.append("REPORTE PDF SIMULADO\n");
        reporte.append("====================\n");

        for (Compra compra : compras) {
            Pago pago = compra.getPago();
            reporte.append("Compra: ").append(compra.getIdCompra()).append("\n");
            reporte.append("Usuario: ").append(compra.getUsuario() == null ? "No registrado" : compra.getUsuario().getNombreCompleto()).append("\n");
            reporte.append("Correo: ").append(compra.getUsuario() == null ? "No registrado" : compra.getUsuario().getCorreo()).append("\n");
            reporte.append("Evento: ").append(compra.getEvento() == null ? "No registrado" : compra.getEvento().getNombre()).append("\n");
            reporte.append("Fecha compra: ").append(compra.getFechaCreacion() == null ? "No registrado" : compra.getFechaCreacion()).append("\n");
            reporte.append("Total: $").append(compra.getTotal()).append("\n");
            reporte.append("Estado: ").append(compra.getEstadoCompra()).append("\n");
            reporte.append("Solicitud: ").append(compra.getMensajeSolicitud() == null ? "No registrado" : compra.getMensajeSolicitud()).append("\n");
            reporte.append("Metodo pago: ").append(pago == null ? "No registrado" : pago.getMetodoPago()).append("\n");
            reporte.append("Estado pago: ").append(pago == null || pago.getEstadoPago() == null ? "No registrado" : pago.getEstadoPago()).append("\n");
            reporte.append("--------------------\n");
        }

        return reporte.toString();
    }
}
