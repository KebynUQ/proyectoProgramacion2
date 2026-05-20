package uniquindio.edu.co.eventos.patterns.structural;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Entrada;
import uniquindio.edu.co.eventos.model.Pago;
import uniquindio.edu.co.eventos.patterns.structural.ServicioAdicional;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ComprobantePagoPDF {

    private ComprobantePagoPDF() {
    }

    public static void generar(Compra compra, File archivo) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            List<String> lineas = construirLineas(compra);

            try (PDPageContentStream contenido = new PDPageContentStream(document, page)) {
                contenido.beginText();
                contenido.setFont(PDType1Font.HELVETICA, 11);
                contenido.setLeading(15f);
                contenido.newLineAtOffset(50, 740);

                for (String linea : lineas) {
                    contenido.showText(linea);
                    contenido.newLine();
                }
                contenido.endText();
            }

            document.save(archivo);
        }
    }

    private static List<String> construirLineas(Compra compra) {
        List<String> lineas = new ArrayList<>();
        Entrada entrada = compra.getEntradas().isEmpty() ? null : compra.getEntradas().get(0);
        Pago pago = compra.getPago();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        lineas.add("COMPROBANTE DE PAGO SIMULADO");
        lineas.add("");
        lineas.add("Plataforma de Gestion de Eventos");
        lineas.add("Universidad del Quindio");
        lineas.add("");

        lineas.add("Datos de la compra:");
        lineas.add("ID de compra: " + valor(compra.getIdCompra()));
        lineas.add("Fecha de compra: " + (compra.getFechaCreacion() == null ? "No definida" : compra.getFechaCreacion().format(formato)));
        lineas.add("Estado de compra: " + compra.getEstadoCompra());
        lineas.add("Total pagado: " + compra.getTotal() + " COP");
        lineas.add("");

        lineas.add("Datos del usuario:");
        lineas.add("Nombre: " + valor(compra.getUsuario() == null ? null : compra.getUsuario().getNombreCompleto()));
        lineas.add("Correo: " + valor(compra.getUsuario() == null ? null : compra.getUsuario().getCorreo()));
        lineas.add("Telefono: " + valor(compra.getUsuario() == null ? null : compra.getUsuario().getTelefono()));
        lineas.add("");

        lineas.add("Datos del evento:");
        lineas.add("Nombre: " + valor(compra.getEvento() == null ? null : compra.getEvento().getNombre()));
        lineas.add("Categoria: " + valor(compra.getEvento() == null ? null : compra.getEvento().getCategoria()));
        lineas.add("Ciudad: " + valor(compra.getEvento() == null ? null : compra.getEvento().getCiudad()));
        lineas.add("Fecha y hora: " + (compra.getEvento() == null || compra.getEvento().getFechaHora() == null
                ? "No definida"
                : compra.getEvento().getFechaHora().format(formato)));
        lineas.add("Recinto: " + (compra.getEvento() == null || compra.getEvento().getRecinto() == null
                ? "No definido"
                : compra.getEvento().getRecinto().getNombre()));
        lineas.add("");

        lineas.add("Datos de la entrada:");
        lineas.add("ID de entrada: " + valor(entrada == null ? null : entrada.getIdEntrada()));
        lineas.add("Zona: " + (entrada == null || entrada.getZona() == null ? "No definida" : entrada.getZona().getNombre()));
        lineas.add("Asiento: " + (entrada == null || entrada.getAsiento() == null
                ? "No definido"
                : entrada.getAsiento().getFila() + "-" + entrada.getAsiento().getNumero()));
        lineas.add("Precio final: " + (entrada == null ? 0 : entrada.getPrecioFinal()) + " COP");
        lineas.add("Estado de entrada: " + (entrada == null ? "No definido" : entrada.getEstadoEntrada()));
        lineas.add("");

        lineas.add("Datos del pago:");
        lineas.add("ID del pago: " + valor(pago == null ? null : pago.getIdPago()));
        lineas.add("Metodo de pago: " + valor(pago == null ? null : pago.getMetodoPago()));
        lineas.add("Estado del pago: " + (pago == null ? "No definido" : pago.getEstadoPago()));
        lineas.add("Fecha del pago: " + (pago == null || pago.getFechaPago() == null ? "No definida" : pago.getFechaPago().format(formato)));
        lineas.add("Monto pagado: " + (pago == null ? 0 : pago.getMonto()) + " COP");
        lineas.add("");

        lineas.add("Servicios adicionales:");
        if (compra.getServiciosAdicionales().isEmpty()) {
            lineas.add("Sin servicios adicionales.");
        } else {
            for (ServicioAdicional servicio : compra.getServiciosAdicionales()) {
                lineas.add("- " + servicio.getClass().getSimpleName());
            }
        }
        lineas.add("");
        lineas.add("Este comprobante corresponde a una transaccion simulada realizada dentro");
        lineas.add("del proyecto academico de Programacion II.");
        return lineas;
    }

    private static String valor(String texto) {
        if (texto == null || texto.isBlank()) {
            return "No definido";
        }
        return texto;
    }
}
