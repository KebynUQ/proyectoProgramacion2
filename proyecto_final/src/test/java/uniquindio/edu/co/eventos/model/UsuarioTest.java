package uniquindio.edu.co.eventos.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Test
    public void crearUsuarioTest() {
        Usuario usuario = new Usuario(
                "USU-001",
                "Kebyn Ochoa",
                "kebyn@uq.edu.co",
                "3001234567",
                "1234"
        );

        assertEquals("USU-001", usuario.getIdUsuario());
        assertEquals("Kebyn Ochoa", usuario.getNombreCompleto());
        assertEquals("kebyn@uq.edu.co", usuario.getCorreo());
        assertEquals("3001234567", usuario.getTelefono());
    }

    @Test
    public void actualizarPerfilTest() {
        Usuario usuario = new Usuario(
                "USU-001",
                "Kebyn Ochoa",
                "kebyn@uq.edu.co",
                "3001234567",
                "1234"
        );

        usuario.actualizarPerfil("Kebyn Julián", "3112223344");

        assertEquals("Kebyn Julián", usuario.getNombreCompleto());
        assertEquals("3112223344", usuario.getTelefono());
    }
}
