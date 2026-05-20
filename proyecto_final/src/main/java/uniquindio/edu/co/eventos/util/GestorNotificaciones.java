package uniquindio.edu.co.eventos.util;

import uniquindio.edu.co.eventos.model.Notificacion;
import uniquindio.edu.co.eventos.patterns.behavioral.Observable;
import uniquindio.edu.co.eventos.patterns.behavioral.Observador;

import java.util.ArrayList;

public class GestorNotificaciones implements Observable {

    private static GestorNotificaciones instancia;

    private final ArrayList<Notificacion> notificaciones;
    private final ArrayList<Observador> observadores;

    private GestorNotificaciones() {
        this.notificaciones = new ArrayList<>();
        this.observadores = new ArrayList<>();
    }

    public static GestorNotificaciones getInstancia() {
        if (instancia == null) {
            instancia = new GestorNotificaciones();
        }
        return instancia;
    }

    public void guardarNotificacion(Notificacion notificacion) {
        if (notificacion != null) {
            notificaciones.add(notificacion);
        }
    }

    public ArrayList<Notificacion> listarPorUsuario(String idUsuario) {
        ArrayList<Notificacion> resultado = new ArrayList<>();
        if (idUsuario == null || idUsuario.isBlank()) {
            return resultado;
        }

        for (Notificacion notificacion : notificaciones) {
            if (idUsuario.equals(notificacion.getIdUsuarioDestino())) {
                resultado.add(notificacion);
            }
        }
        return resultado;
    }

    public boolean marcarComoLeida(String idNotificacion) {
        if (idNotificacion == null || idNotificacion.isBlank()) {
            return false;
        }

        for (Notificacion notificacion : notificaciones) {
            if (idNotificacion.equals(notificacion.getIdNotificacion())) {
                notificacion.setLeida(true);
                return true;
            }
        }
        return false;
    }

    public int contarNoLeidas(String idUsuario) {
        int contador = 0;
        for (Notificacion notificacion : listarPorUsuario(idUsuario)) {
            if (!notificacion.isLeida()) {
                contador++;
            }
        }
        return contador;
    }

    public void limpiarNotificaciones(String idUsuario) {
        if (idUsuario == null || idUsuario.isBlank()) {
            return;
        }

        ArrayList<Notificacion> copia = new ArrayList<>(notificaciones);
        for (Notificacion notificacion : copia) {
            if (idUsuario.equals(notificacion.getIdUsuarioDestino())) {
                notificaciones.remove(notificacion);
            }
        }
    }

    public void limpiarLeidas(String idUsuario) {
        if (idUsuario == null || idUsuario.isBlank()) {
            return;
        }

        ArrayList<Notificacion> copia = new ArrayList<>(notificaciones);
        for (Notificacion notificacion : copia) {
            if (idUsuario.equals(notificacion.getIdUsuarioDestino()) && notificacion.isLeida()) {
                notificaciones.remove(notificacion);
            }
        }
    }

    @Override
    public void agregarObservador(Observador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void eliminarObservador(Observador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores(Notificacion notificacion) {
        for (Observador observador : observadores) {
            observador.actualizar(notificacion);
        }
    }
}
