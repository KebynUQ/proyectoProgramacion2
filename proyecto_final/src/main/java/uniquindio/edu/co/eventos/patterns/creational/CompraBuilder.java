package uniquindio.edu.co.eventos.patterns.creational;

import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Entrada;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.patterns.structural.ServicioAdicional;

public class CompraBuilder {

    private Compra compra;

    public CompraBuilder() {
        this.compra = new Compra();
    }

    public CompraBuilder conId(String idCompra) {
        compra.setIdCompra(idCompra);
        return this;
    }

    public CompraBuilder conUsuario(Usuario usuario) {
        compra.setUsuario(usuario);
        return this;
    }

    public CompraBuilder conEvento(Evento evento) {
        compra.setEvento(evento);
        return this;
    }

    public CompraBuilder agregarEntrada(Entrada entrada) {
        compra.agregarEntrada(entrada);
        return this;
    }

    public CompraBuilder agregarServicio(ServicioAdicional servicio) {
        compra.agregarServicio(servicio);
        return this;
    }

    public Compra build() {
        compra.calcularTotal();
        return compra;
    }
}
