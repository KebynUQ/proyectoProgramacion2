package uniquindio.edu.co.eventos.model;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.enums.EstadoEvento;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EventoTest {

    @Test
    public void crearEventoTest() {
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

        assertEquals("EVE-001", evento.getIdEvento());
        assertEquals("Concierto Universitario", evento.getNombre());
        assertEquals("Concierto", evento.getCategoria());
        assertEquals("Armenia", evento.getCiudad());
        assertEquals(30000, evento.getPrecioBase());
    }

    @Test
    public void cambiarEstadoEventoTest() {
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

        evento.cambiarEstado(EstadoEvento.PUBLICADO);

        assertEquals(EstadoEvento.PUBLICADO, evento.getEstadoEvento());
    }
}
