package uniquindio.edu.co.eventos.patterns;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Recinto;
import uniquindio.edu.co.eventos.patterns.creational.ConciertoFactory;
import uniquindio.edu.co.eventos.patterns.creational.EventoFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EventoFactoryTest {

    @Test
    public void crearEventoConFactoryTest() {
        Recinto recinto = new Recinto(
                "REC-001",
                "Auditorio UQ",
                "Carrera 15",
                "Armenia"
        );

        EventoFactory factory = new ConciertoFactory();

        Evento evento = factory.crearEvento(
                "EVE-001",
                "Concierto Universitario",
                "Evento musical",
                "Armenia",
                LocalDateTime.now().plusDays(5),
                recinto,
                30000
        );

        assertNotNull(evento);
        assertEquals("Concierto", evento.getCategoria());
        assertEquals("Concierto Universitario", evento.getNombre());
    }
}
