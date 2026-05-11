package uniquindio.edu.co.eventos.patterns;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Recinto;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.patterns.structural.GeneradorReporte;
import uniquindio.edu.co.eventos.patterns.structural.GeneradorReporteCSV;
import uniquindio.edu.co.eventos.patterns.structural.GeneradorReportePDF;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ReporteTest {

    @Test
    public void generarReporteCSVTest() {
        ArrayList<Compra> compras = crearComprasDePrueba();

        GeneradorReporte generador = new GeneradorReporteCSV();
        String reporte = generador.generarReporte(compras);

        assertNotNull(reporte);
        assertTrue(reporte.contains("ID_COMPRA"));
        assertTrue(reporte.contains("COM-001"));
    }

    @Test
    public void generarReportePDFTest() {
        ArrayList<Compra> compras = crearComprasDePrueba();

        GeneradorReporte generador = new GeneradorReportePDF();
        String reporte = generador.generarReporte(compras);

        assertNotNull(reporte);
        assertTrue(reporte.contains("REPORTE PDF SIMULADO"));
        assertTrue(reporte.contains("COM-001"));
    }

    private ArrayList<Compra> crearComprasDePrueba() {
        Usuario usuario = new Usuario(
                "USU-001",
                "Kebyn Ochoa",
                "kebyn@uq.edu.co",
                "3001234567",
                "1234"
        );

        Recinto recinto = new Recinto(
                "REC-001",
                "Auditorio UQ",
                "Carrera 15",
                "Armenia"
        );

        Evento evento = new Evento(
                "EVE-001",
                "Concierto Universitario",
                "Concierto",
                "Evento musical",
                "Armenia",
                LocalDateTime.now().plusDays(5),
                recinto,
                30000
        );

        Compra compra = new Compra("COM-001", usuario, evento);
        compra.calcularTotal();

        ArrayList<Compra> compras = new ArrayList<>();
        compras.add(compra);

        return compras;
    }
}
