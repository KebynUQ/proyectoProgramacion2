package uniquindio.edu.co.eventos.patterns;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Recinto;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.enums.EstadoCompra;
import uniquindio.edu.co.eventos.patterns.behavioral.CompraCreada;
import uniquindio.edu.co.eventos.patterns.behavioral.CompraPagada;
import uniquindio.edu.co.eventos.patterns.behavioral.EstadoCompraBehavior;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class StateCompraTest {

    @Test
    public void cambiarEstadoDeCompraTest() {
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

        EstadoCompraBehavior estadoCreada = new CompraCreada();
        estadoCreada.pagar(compra);

        assertEquals(EstadoCompra.PAGADA, compra.getEstadoCompra());

        EstadoCompraBehavior estadoPagada = new CompraPagada();
        estadoPagada.confirmar(compra);

        assertEquals(EstadoCompra.CONFIRMADA, compra.getEstadoCompra());
    }
}
