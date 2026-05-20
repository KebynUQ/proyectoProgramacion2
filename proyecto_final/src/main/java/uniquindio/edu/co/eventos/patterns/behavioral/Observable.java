package uniquindio.edu.co.eventos.patterns.behavioral;

import uniquindio.edu.co.eventos.model.Notificacion;

public interface Observable {

    void agregarObservador(Observador observador);

    void eliminarObservador(Observador observador);

    void notificarObservadores(Notificacion notificacion);
}
