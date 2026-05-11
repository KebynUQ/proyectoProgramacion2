package uniquindio.edu.co.eventos.patterns.behavioral;

public interface Observable {

    void agregarObservador(Observador observador);

    void eliminarObservador(Observador observador);

    void notificarObservadores(String mensaje);
}
