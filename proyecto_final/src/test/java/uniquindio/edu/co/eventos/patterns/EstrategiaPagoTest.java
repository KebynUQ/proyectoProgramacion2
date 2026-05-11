package uniquindio.edu.co.eventos.patterns;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.patterns.behavioral.EstrategiaPago;
import uniquindio.edu.co.eventos.patterns.behavioral.PagoPSE;
import uniquindio.edu.co.eventos.patterns.behavioral.PagoSimulado;
import uniquindio.edu.co.eventos.patterns.behavioral.PagoTarjeta;

import static org.junit.jupiter.api.Assertions.*;

public class EstrategiaPagoTest {

    @Test
    public void pagoTarjetaTest() {
        EstrategiaPago estrategia = new PagoTarjeta();

        assertTrue(estrategia.procesarPago(50000));
    }

    @Test
    public void pagoPSETest() {
        EstrategiaPago estrategia = new PagoPSE();

        assertTrue(estrategia.procesarPago(50000));
    }

    @Test
    public void pagoSimuladoTest() {
        EstrategiaPago estrategia = new PagoSimulado();

        assertTrue(estrategia.procesarPago(50000));
    }

    @Test
    public void pagoMontoCeroTest() {
        EstrategiaPago estrategia = new PagoSimulado();

        assertFalse(estrategia.procesarPago(0));
    }
}
