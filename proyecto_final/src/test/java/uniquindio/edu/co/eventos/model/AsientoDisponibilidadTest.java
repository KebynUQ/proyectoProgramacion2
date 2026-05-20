package uniquindio.edu.co.eventos.model;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.enums.EstadoAsiento;

import static org.junit.jupiter.api.Assertions.*;

public class AsientoDisponibilidadTest {

    @Test
    public void bloquearYLiberarAsientoTest() {
        Asiento asiento = new Asiento("ASI-100", "A", 1);

        asiento.inhabilitar();
        assertEquals(EstadoAsiento.BLOQUEADO, asiento.getEstadoAsiento());

        asiento.liberar();
        assertEquals(EstadoAsiento.DISPONIBLE, asiento.getEstadoAsiento());
    }

    @Test
    public void asientoVendidoNoSeLiberaTest() {
        Asiento asiento = new Asiento("ASI-101", "A", 2);
        asiento.vender();
        asiento.liberar();

        assertEquals(EstadoAsiento.VENDIDO, asiento.getEstadoAsiento());
    }
}
