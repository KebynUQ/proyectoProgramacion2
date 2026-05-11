package uniquindio.edu.co.eventos.patterns;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.patterns.structural.AccesoPreferencial;
import uniquindio.edu.co.eventos.patterns.structural.Parqueadero;
import uniquindio.edu.co.eventos.patterns.structural.ServicioAdicional;
import uniquindio.edu.co.eventos.patterns.structural.ServicioVIP;

import static org.junit.jupiter.api.Assertions.*;

public class CompraDecoratorTest {

    @Test
    public void agregarServiciosAdicionalesTest() {
        ServicioAdicional servicio = null;

        servicio = new ServicioVIP(servicio);
        servicio = new Parqueadero(servicio);
        servicio = new AccesoPreferencial(servicio);

        assertTrue(servicio.getPrecio() > 0);
        assertTrue(servicio.getDescripcion().contains("Servicio VIP"));
        assertTrue(servicio.getDescripcion().contains("Parqueadero"));
        assertTrue(servicio.getDescripcion().contains("Acceso preferencial"));
    }
}
