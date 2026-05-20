package uniquindio.edu.co.eventos.model;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.enums.EstadoCompra;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PanelMetricasTest {

    @Test
    public void calcularMetricasBasicasTest() {
        Usuario usuario = new Usuario("USU-500", "Sara", "sara@uq.edu.co", "300", "1234");
        Recinto recinto = new Recinto("REC-500", "Teatro", "Centro", "Armenia");
        Evento evento = new Evento("EVE-500", "Obra", "Teatro", "Desc", "Armenia", LocalDateTime.now().plusDays(3), recinto, 50000);

        Compra compra = new Compra("COM-500", usuario, evento);
        compra.setTotal(50000);
        compra.setEstadoCompra(EstadoCompra.CONFIRMADA);

        ArrayList<Compra> compras = new ArrayList<>();
        compras.add(compra);

        PanelMetricas panel = new PanelMetricas();
        panel.calcularMetricas(compras, new ArrayList<>());

        assertTrue(panel.visualizarMetricas().contains("Total compras: 1"));
    }
}
