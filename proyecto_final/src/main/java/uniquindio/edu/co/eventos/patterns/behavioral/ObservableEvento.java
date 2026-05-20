package uniquindio.edu.co.eventos.patterns.behavioral;

import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Notificacion;
import uniquindio.edu.co.eventos.model.enums.EstadoEvento;
import uniquindio.edu.co.eventos.model.enums.TipoNotificacion;

import java.util.ArrayList;

public class ObservableEvento implements Observable {

    private Evento evento;
    private ArrayList<Observador> observadores;

    public ObservableEvento(Evento evento) {
        this.evento = evento;
        this.observadores = new ArrayList<>();
    }

    @Override
    public void agregarObservador(Observador observador) {
        if (observador != null) {
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

    public void cambiarEstadoEvento(EstadoEvento estadoEvento, String idUsuarioDestino) {
        if (evento != null) {
            evento.cambiarEstado(estadoEvento);
            Notificacion notificacion = new Notificacion(
                    "NOT-" + System.currentTimeMillis(),
                    "Cambio de estado de evento",
                    "El evento " + evento.getNombre() + " cambió su estado a " + estadoEvento + ".",
                    idUsuarioDestino,
                    TipoNotificacion.EVENTO
            );
            notificarObservadores(notificacion);
        }
    }

    public void cambiarEstadoEvento(EstadoEvento estadoEvento) {
        cambiarEstadoEvento(estadoEvento, "");
    }

    public Evento getEvento() {
        return evento;
    }

    public ArrayList<Observador> getObservadores() {
        return observadores;
    }
}
