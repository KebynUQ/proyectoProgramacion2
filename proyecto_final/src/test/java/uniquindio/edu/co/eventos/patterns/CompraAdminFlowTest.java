package uniquindio.edu.co.eventos.patterns;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.Asiento;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Recinto;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.Zona;
import uniquindio.edu.co.eventos.model.enums.EstadoCompra;
import uniquindio.edu.co.eventos.model.enums.TipoSolicitudCompra;
import uniquindio.edu.co.eventos.patterns.structural.CompraFacade;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CompraAdminFlowTest {

    @Test
    public void solicitudPagoYConfirmacionAdminTest() {
        SistemaEventos sistema = SistemaEventos.getInstancia();
        sistema.getCompras().clear();
        sistema.getPagos().clear();

        Usuario usuario = new Usuario("USU-300", "Luis", "luis@uq.edu.co", "300", "1234");
        Recinto recinto = new Recinto("REC-300", "Auditorio", "Norte", "Armenia");
        Zona zona = new Zona("ZON-300", "VIP", 5, 80000);
        Asiento asiento = new Asiento("ASI-300", "A", 1);
        zona.agregarAsiento(asiento);
        recinto.agregarZona(zona);
        Evento evento = new Evento("EVE-300", "Concierto", "Concierto", "Desc", "Armenia", LocalDateTime.now().plusDays(2), recinto, 80000);

        CompraFacade facade = new CompraFacade();
        Compra compra = facade.crearCompraPendiente(usuario, evento, zona, asiento, new ArrayList<>());

        assertNotNull(compra);
        assertTrue(facade.solicitarConfirmacionPago(compra));
        assertEquals(TipoSolicitudCompra.SOLICITUD_CONFIRMACION_PAGO, compra.getTipoSolicitud());
        assertTrue(facade.confirmarPagoAdmin(compra));
        assertEquals(EstadoCompra.CONFIRMADA, compra.getEstadoCompra());
    }
}
