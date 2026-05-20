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
        System.out.println("No se puede cancelar una compra ya pagada.");
    }

    @Override
    public void reembolsar(Compra compra) {
        compra.cambiarEstado(EstadoCompra.REEMBOLSADA);
    }
}
