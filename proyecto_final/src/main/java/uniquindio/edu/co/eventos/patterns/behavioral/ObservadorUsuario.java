package uniquindio.edu.co.eventos.patterns.behavioral;

import uniquindio.edu.co.eventos.model.Notificacion;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.util.GestorNotificaciones;

public class ObservadorUsuario implements Observador {

    private Usuario usuario;

    public ObservadorUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public void actualizar(Notificacion notificacion) {
        if (usuario == null || notificacion == null) {
            return;
        }

        if (usuario.getIdUsuario() != null && usuario.getIdUsuario().equals(notificacion.getIdUsuarioDestino())) {
            GestorNotificaciones.getInstancia().guardarNotificacion(notificacion);
            usuario.recibirNotificacion(notificacion);
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
