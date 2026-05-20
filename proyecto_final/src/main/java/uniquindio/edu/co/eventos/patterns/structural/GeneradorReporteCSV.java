package uniquindio.edu.co.eventos.patterns.structural;

import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Pago;

import java.util.ArrayList;

public class GeneradorReporteCSV implements GeneradorReporte {

    @Override
    public String generarReporte(ArrayList<Compra> compras) {
        StringBuilder reporte = new StringBuilder();

        reporte.append("ID_COMPRA,USUARIO,CORREO,EVENTO,FECHA_COMPRA,TOTAL,ESTADO_COMPRA,SOLICITUD,METODO_PAGO,ESTADO_PAGO\n");

        for (Compra compra : compras) {
            Pago pago = compra.getPago();
            reporte.append(valor(compra.getIdCompra())).append(",");
            reporte.append(valor(compra.getUsuario() == null ? null : compra.getUsuario().getNombreCompleto())).append(",");
            reporte.append(valor(compra.getUsuario() == null ? null : compra.getUsuario().getCorreo())).append(",");
            reporte.append(valor(compra.getEvento() == null ? null : compra.getEvento().getNombre())).append(",");
            reporte.append(valor(compra.getFechaCreacion() == null ? null : compra.getFechaCreacion().toString())).append(",");
            reporte.append(compra.getTotal()).append(",");
            reporte.append(valor(compra.getEstadoCompra() == null ? null : compra.getEstadoCompra().name())).append(",");
            reporte.append(valor(compra.getMensajeSolicitud())).append(",");
            reporte.append(valor(pago == null ? null : pago.getMetodoPago())).append(",");
            reporte.append(valor(pago == null || pago.getEstadoPago() == null ? null : pago.getEstadoPago().name())).append("\n");
        }

        return reporte.toString();
    }

    private String valor(String texto) {
        if (texto == null || texto.isBlank()) {
            return "No registrado";
        }
        return texto.replace(",", " ");
    }
}
