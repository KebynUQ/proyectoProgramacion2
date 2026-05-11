package uniquindio.edu.co.eventos.patterns;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Recinto;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.patterns.creational.CompraBuilder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CompraBuilderTest {

    @Test
    public void construirCompraConBuilderTest() {
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

        Compra compra = new CompraBuilder()
                .conId("COM-001")
                .conUsuario(usuario)
                .conEvento(evento)
                .build();

        assertNotNull(compra);
        assertEquals("COM-001", compra.getIdCompra());
        assertEquals(usuario, compra.getUsuario());
        assertEquals(evento, compra.getEvento());
    }
}
