package uniquindio.edu.co.eventos.model;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.enums.TipoNotificacion;
import uniquindio.edu.co.eventos.util.GestorNotificaciones;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class NotificacionTest {

    @Test
    public void crearNotificacionYVerificarDatosTest() {
        Notificacion notificacion = new Notificacion(
                "NOT-T1",
                "Titulo prueba",
                "Mensaje prueba",
                "USU-T1",
                TipoNotificacion.SISTEMA
        );

        assertEquals("NOT-T1", notificacion.getIdNotificacion());
        assertEquals("Titulo prueba", notificacion.getTitulo());
        assertEquals("Mensaje prueba", notificacion.getMensaje());
        assertEquals("USU-T1", notificacion.getIdUsuarioDestino());
        assertEquals(TipoNotificacion.SISTEMA, notificacion.getTipoNotificacion());
        assertFalse(notificacion.isLeida());
        assertNotNull(notificacion.getFecha());
    }

    @Test
    public void marcarComoLeidaYListarPorUsuarioTest() {
        GestorNotificaciones gestor = GestorNotificaciones.getInstancia();
        String idUsuario = "USU-NOT-01";

        gestor.limpiarNotificaciones(idUsuario);

        Notificacion notificacion = new Notificacion(
                "NOT-T2",
                "Compra",
                "Compra creada",
                idUsuario,
                TipoNotificacion.COMPRA
        );
        gestor.guardarNotificacion(notificacion);

        ArrayList<Notificacion> lista = gestor.listarPorUsuario(idUsuario);
        assertEquals(1, lista.size());
        assertFalse(lista.get(0).isLeida());

        boolean marcada = gestor.marcarComoLeida("NOT-T2");
        assertTrue(marcada);
        assertEquals(0, gestor.contarNoLeidas(idUsuario));

        gestor.limpiarNotificaciones(idUsuario);
    }
}
