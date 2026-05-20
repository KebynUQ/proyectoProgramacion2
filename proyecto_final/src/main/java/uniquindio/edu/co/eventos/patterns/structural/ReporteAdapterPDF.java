package uniquindio.edu.co.eventos.patterns.structural;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Pago;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.enums.EstadoCompra;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReporteAdapterPDF {

    private final GeneradorReportePDF generadorReportePDF;

    public ReporteAdapterPDF() {
        this.generadorReportePDF = new GeneradorReportePDF();
    }

    public String exportar(ArrayList<Compra> compras) {
        return generadorReportePDF.generarReporte(compras);
    }

    public void exportarArchivo(ArrayList<Compra> compras, File archivo, boolean esAdministrador, Usuario usuario) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 11);
                content.setLeading(15f);
                content.newLineAtOffset(50, 740);

                escribir(content, "REPORTE DE COMPRAS");
                escribir(content, "Plataforma de Gestion de Eventos - Universidad del Quindio");
                escribir(content, esAdministrador
                        ? "Reporte general de compras del sistema"
                        : "Reporte de compras del usuario: " + valor(usuario == null ? null : usuario.getNombreCompleto()));
                escribir(content, "");

                DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                double totalRecaudado = 0;
                int pagadas = 0;
                int canceladas = 0;
                int pendientes = 0;
                Map<String, Integer> conteoEvento = new HashMap<>();

                for (Compra compra : compras) {
                    Pago pago = compra.getPago();
                    String evento = valor(compra.getEvento() == null ? null : compra.getEvento().getNombre());
                    escribir(content, "ID compra: " + valor(compra.getIdCompra()));
                    escribir(content, "Usuario: " + valor(compra.getUsuario() == null ? null : compra.getUsuario().getNombreCompleto()));
                    escribir(content, "Correo: " + valor(compra.getUsuario() == null ? null : compra.getUsuario().getCorreo()));
                    escribir(content, "Evento: " + evento);
                    escribir(content, "Fecha compra: " + (compra.getFechaCreacion() == null ? "No registrado" : compra.getFechaCreacion().format(formato)));
                    escribir(content, "Total: " + compra.getTotal() + " COP");
                    escribir(content, "Estado compra: " + valor(compra.getEstadoCompra() == null ? null : compra.getEstadoCompra().name()));
                    escribir(content, "Solicitud: " + valor(compra.getMensajeSolicitud()));
                    escribir(content, "Metodo pago: " + valor(pago == null ? null : pago.getMetodoPago()));
                    escribir(content, "Estado pago: " + valor(pago == null || pago.getEstadoPago() == null ? null : pago.getEstadoPago().name()));
                    escribir(content, "----------------------------------------");

                    totalRecaudado += compra.getTotal();
                    conteoEvento.put(evento, conteoEvento.getOrDefault(evento, 0) + 1);
                    if (compra.getEstadoCompra() == EstadoCompra.PAGADA || compra.getEstadoCompra() == EstadoCompra.CONFIRMADA) {
                        pagadas++;
                    } else if (compra.getEstadoCompra() == EstadoCompra.CANCELADA) {
                        canceladas++;
                    } else {
                        pendientes++;
                    }
                }

                escribir(content, "");
                escribir(content, "RESUMEN");
                escribir(content, "Total de compras: " + compras.size());
                escribir(content, "Total recaudado: " + totalRecaudado + " COP");
                escribir(content, "Compras pagadas: " + pagadas);
                escribir(content, "Compras canceladas: " + canceladas);
                escribir(content, "Compras pendientes: " + pendientes);
                escribir(content, "Evento con mas compras: " + eventoMasComprado(conteoEvento));

                content.endText();
            }

            document.save(archivo);
        }
    }

    private void escribir(PDPageContentStream content, String texto) throws IOException {
        content.showText(texto);
        content.newLine();
    }

    private String valor(String texto) {
        if (texto == null || texto.isBlank()) {
            return "No registrado";
        }
        return texto;
    }

    private String eventoMasComprado(Map<String, Integer> conteo) {
        String nombre = "No registrado";
        int max = 0;
        for (Map.Entry<String, Integer> entry : conteo.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                nombre = entry.getKey();
            }
        }
        return nombre;
    }
}
