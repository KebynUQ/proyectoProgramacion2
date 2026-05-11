package uniquindio.edu.co.eventos.patterns;

import org.junit.jupiter.api.Test;
import uniquindio.edu.co.eventos.model.SistemaEventos;

import static org.junit.jupiter.api.Assertions.*;

public class SistemaEventosTest {

    @Test
    public void singletonDebeRetornarMismaInstanciaTest() {
        SistemaEventos sistema1 = SistemaEventos.getInstancia();
        SistemaEventos sistema2 = SistemaEventos.getInstancia();

        assertSame(sistema1, sistema2);
    }
}
