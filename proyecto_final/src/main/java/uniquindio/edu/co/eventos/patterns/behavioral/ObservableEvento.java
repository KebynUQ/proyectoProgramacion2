package uniquindio.edu.co.eventos.patterns.behavioral;

import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.enums.EstadoEvento;

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
    public void notificarObservadores(String mensaje) {
        for (Observador observador : observadores) {
            observador.actualizar(mensaje);
        }
    }

    public void cambiarEstadoEvento(EstadoEvento estadoEvento) {
        if (evento != null) {
            evento.cambiarEstado(estadoEvento);
            notificarObservadores("El evento " + evento.getNombre() + " cambió su estado a " + estadoEvento);
        }
    }

    public Evento getEvento() {
        return evento;
    }

    public ArrayList<Observador> getObservadores() {
        return observadores;
    }
}
