package uniquindio.edu.co.eventos.patterns.creational;

import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Recinto;

import java.time.LocalDateTime;

public class TeatroFactory implements EventoFactory {

    @Override
    public Evento crearEvento(String idEvento, String nombre, String descripcion,
                              String ciudad, LocalDateTime fechaHora,
                              Recinto recinto, double precioBase) {

        return new Evento(
                idEvento,
                nombre,
                "Teatro",
                descripcion,
                ciudad,
                fechaHora,
                recinto,
                precioBase
        );
    }
}
