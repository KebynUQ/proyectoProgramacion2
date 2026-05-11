package uniquindio.edu.co.eventos.model;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.patterns.behavioral.PagoSimulado;

import static org.junit.jupiter.api.Assertions.*;

public class PagoTest {

    @Test
    public void procesarPagoExitosoTest() {
        Pago pago = new Pago(
                "PAG-001",
                50000,
                "Pago simulado",
                new PagoSimulado()
        );

        boolean resultado = pago.procesarPago();

        assertTrue(resultado);
    }

    @Test
    public void procesarPagoFallidoTest() {
        Pago pago = new Pago(
                "PAG-002",
                0,
                "Pago simulado",
                new PagoSimulado()
        );

        boolean resultado = pago.procesarPago();

        assertFalse(resultado);
    }
}
