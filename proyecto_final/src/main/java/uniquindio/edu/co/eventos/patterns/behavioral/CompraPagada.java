package uniquindio.edu.co.eventos.patterns.behavioral;

import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.enums.EstadoCompra;

public class CompraPagada implements EstadoCompraBehavior {

    @Override
    public void pagar(Compra compra) {
        System.out.println("La compra ya está pagada.");
    }

    @Override
    public void confirmar(Compra compra) {
        compra.cambiarEstado(EstadoCompra.CONFIRMADA);
    }

    @Override
    public void cancelar(Compra compra) {
        compra.cambiarEstado(EstadoCompra.CANCELADA);
    }

    @Override
    public void reembolsar(Compra compra) {
        compra.cambiarEstado(EstadoCompra.REEMBOLSADA);
    }
}
