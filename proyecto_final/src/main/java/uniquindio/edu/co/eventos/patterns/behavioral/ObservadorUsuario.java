package uniquindio.edu.co.eventos.patterns.behavioral;

import uniquindio.edu.co.eventos.model.Usuario;

public class ObservadorUsuario implements Observador {

    private Usuario usuario;

    public ObservadorUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public void actualizar(String mensaje) {
        if (usuario != null) {
            usuario.recibirNotificacion(mensaje);
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
