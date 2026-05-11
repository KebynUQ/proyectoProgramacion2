package uniquindio.edu.co.eventos.patterns;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Recinto;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.enums.EstadoEvento;
import uniquindio.edu.co.eventos.patterns.behavioral.ObservableEvento;
import uniquindio.edu.co.eventos.patterns.behavioral.ObservadorUsuario;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ObserverTest {

    @Test
    public void agregarObservadorYNotificarTest() {
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

        ObservableEvento observableEvento = new ObservableEvento(evento);
        ObservadorUsuario observadorUsuario = new ObservadorUsuario(usuario);

        observableEvento.agregarObservador(observadorUsuario);
        observableEvento.cambiarEstadoEvento(EstadoEvento.CANCELADO);

        assertEquals(1, observableEvento.getObservadores().size());
        assertEquals(EstadoEvento.CANCELADO, evento.getEstadoEvento());
    }
}
