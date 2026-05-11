package uniquindio.edu.co.eventos.model;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.enums.TipoIncidencia;

import static org.junit.jupiter.api.Assertions.*;

public class IncidenciaTest {

    @Test
    public void crearIncidenciaTest() {
        Incidencia incidencia = new Incidencia(
                "INC-001",
                TipoIncidencia.ERROR_PAGO,
                "Error al procesar el pago"
        );

        assertEquals("INC-001", incidencia.getIdIncidencia());
        assertEquals(TipoIncidencia.ERROR_PAGO, incidencia.getTipo());
        assertEquals("Error al procesar el pago", incidencia.getDescripcion());
        assertNotNull(incidencia.getFecha());
    }
}
