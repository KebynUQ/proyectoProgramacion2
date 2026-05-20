package uniquindio.edu.co.eventos.patterns;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.Asiento;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Notificacion;
import uniquindio.edu.co.eventos.model.Recinto;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.Zona;
import uniquindio.edu.co.eventos.model.enums.EstadoEvento;
import uniquindio.edu.co.eventos.model.enums.TipoNotificacion;
import uniquindio.edu.co.eventos.patterns.behavioral.ObservableEvento;
import uniquindio.edu.co.eventos.patterns.behavioral.ObservadorUsuario;
import uniquindio.edu.co.eventos.patterns.behavioral.PagoSimulado;
import uniquindio.edu.co.eventos.patterns.structural.CompraFacade;
import uniquindio.edu.co.eventos.util.GestorNotificaciones;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ObserverNotificacionTest {

    @Test
    public void cambioEstadoEventoGeneraNotificacionTest() {
        GestorNotificaciones gestor = GestorNotificaciones.getInstancia();

        Usuario usuario = new Usuario("USU-OBS-1", "Ana", "ana@uq.edu.co", "3001112233", "1234");
        gestor.limpiarNotificaciones(usuario.getIdUsuario());

        Recinto recinto = new Recinto("REC-OBS-1", "Auditorio", "Norte", "Armenia");
        Evento evento = new Evento(
                "EVE-OBS-1",
                "Evento test",
                "Concierto",
                "Prueba observer",
                "Armenia",
                LocalDateTime.now().plusDays(5),
                recinto,
                25000
        );

        ObservableEvento observableEvento = new ObservableEvento(evento);
        observableEvento.agregarObservador(new ObservadorUsuario(usuario));

        Notificacion notificacion = new Notificacion(
                "NOT-OBS-1",
                "Evento actualizado",
                "El evento cambió de estado.",
                usuario.getIdUsuario(),
                TipoNotificacion.EVENTO
        );

        evento.cambiarEstado(EstadoEvento.PAUSADO);
        observableEvento.notificarObservadores(notificacion);

        ArrayList<Notificacion> notificaciones = gestor.listarPorUsuario(usuario.getIdUsuario());
        assertFalse(notificaciones.isEmpty());
        assertEquals("NOT-OBS-1", notificaciones.get(notificaciones.size() - 1).getIdNotificacion());
        assertEquals(EstadoEvento.PAUSADO, evento.getEstadoEvento());

        gestor.limpiarNotificaciones(usuario.getIdUsuario());
    }

    @Test
    public void cambioEstadoCompraGeneraNotificacionUsuarioTest() {
        GestorNotificaciones gestor = GestorNotificaciones.getInstancia();
        CompraFacade facade = new CompraFacade();

        Usuario usuario = new Usuario("USU-OBS-2", "Luis", "luis@uq.edu.co", "3011112233", "1234");
        gestor.limpiarNotificaciones(usuario.getIdUsuario());

        Recinto recinto = new Recinto("REC-OBS-2", "Coliseo", "Centro", "Armenia");
        Zona zona = new Zona("ZON-OBS-2", "General", 5, 30000);
        Asiento asiento = new Asiento("ASI-OBS-2", "A", 1);
        zona.agregarAsiento(asiento);
        recinto.agregarZona(zona);

        Evento evento = new Evento(
                "EVE-OBS-2",
                "Evento compra test",
                "Conferencia",
                "Prueba compra",
                "Armenia",
                LocalDateTime.now().plusDays(3),
                recinto,
                30000
        );

        Compra compra = facade.crearCompraPendiente(usuario, evento, zona, asiento, new ArrayList<>());
        assertNotNull(compra);

        boolean pago = facade.confirmarPagoCompra(compra, "Simulado", new PagoSimulado());
        assertTrue(pago);

        ArrayList<Notificacion> notificaciones = gestor.listarPorUsuario(usuario.getIdUsuario());
        assertFalse(notificaciones.isEmpty());
        assertTrue(notificaciones.stream().anyMatch(n -> "Compra pagada".equals(n.getTitulo())));

        gestor.limpiarNotificaciones(usuario.getIdUsuario());
    }
}
