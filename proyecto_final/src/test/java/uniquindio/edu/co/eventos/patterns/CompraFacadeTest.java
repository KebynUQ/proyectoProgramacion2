package uniquindio.edu.co.eventos.patterns;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.Asiento;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Recinto;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.Zona;
import uniquindio.edu.co.eventos.model.enums.EstadoAsiento;
import uniquindio.edu.co.eventos.patterns.behavioral.PagoSimulado;
import uniquindio.edu.co.eventos.patterns.structural.CompraFacade;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CompraFacadeTest {

    @Test
    public void realizarCompraConFacadeTest() {
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
        zona.agregarAsiento(asiento);

        CompraFacade facade = new CompraFacade();

        Compra compra = facade.realizarCompra(
                usuario,
                evento,
                zona,
                asiento,
                new PagoSimulado()
        );

        assertNotNull(compra);
        assertEquals(EstadoAsiento.VENDIDO, asiento.getEstadoAsiento());
        assertTrue(compra.getTotal() > 0);
    }
}
