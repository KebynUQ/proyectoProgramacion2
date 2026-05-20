package uniquindio.edu.co.eventos.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecintoZonaTest {

    @Test
    public void agregarYEliminarZonaTest() {
        Recinto recinto = new Recinto("REC-100", "Coliseo", "Centro", "Armenia");
        Zona zona = new Zona("ZON-100", "VIP", 5, 70000);

        recinto.agregarZona(zona);
        assertEquals(1, recinto.getZonas().size());

        recinto.eliminarZona(zona);
        assertTrue(recinto.getZonas().isEmpty());
    }
}
