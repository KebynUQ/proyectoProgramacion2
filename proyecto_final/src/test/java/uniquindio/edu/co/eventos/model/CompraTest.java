package uniquindio.edu.co.eventos.model;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.enums.EstadoCompra;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CompraTest {

    @Test
    public void crearCompraYCalcularTotalTest() {
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

        Zona zona = new Zona("ZON-001", "VIP", 10, 80000);
        Asiento asiento = new Asiento("ASI-001", "A", 1);

        Entrada entrada = new Entrada(
                "ENT-001",
                zona,
                asiento,
                zona.getPrecioBase()
        );

        Compra compra = new Compra("COM-001", usuario, evento);
        compra.agregarEntrada(entrada);
        compra.calcularTotal();

        assertEquals(80000, compra.getTotal());
        assertEquals(1, compra.getEntradas().size());
    }

    @Test
    public void cancelarCompraTest() {
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
        compra.cancelar();

        assertEquals(EstadoCompra.CANCELADA, compra.getEstadoCompra());
    }
}
