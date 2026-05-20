package uniquindio.edu.co.eventos.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SistemaEventosAdminTest {

    @Test
    public void actualizarYEliminarUsuarioTest() {
        SistemaEventos sistema = SistemaEventos.getInstancia();
        sistema.getUsuarios().clear();

        Usuario usuario = new Usuario("USU-900", "Ana", "ana@uq.edu.co", "300", "1234");
        sistema.registrarUsuario(usuario);

        boolean actualizado = sistema.actualizarUsuario(usuario, "Ana Maria", "ana.maria@uq.edu.co", "301", "5678");

        assertTrue(actualizado);
        assertEquals("Ana Maria", usuario.getNombreCompleto());
        assertTrue(sistema.eliminarUsuario(usuario));
    }
}
