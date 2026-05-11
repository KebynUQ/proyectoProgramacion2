package uniquindio.edu.co.eventos.patterns.behavioral;

import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.enums.EstadoCompra;

public class CompraCreada implements EstadoCompraBehavior {

    @Override
    public void pagar(Compra compra) {
        compra.cambiarEstado(EstadoCompra.PAGADA);
    }

    @Override
    public void confirmar(Compra compra) {
        System.out.println("No se puede confirmar una compra que todavía no ha sido pagada.");
    }

    @Override
    public void cancelar(Compra compra) {
        compra.cambiarEstado(EstadoCompra.CANCELADA);
    }

    @Override
    public void reembolsar(Compra compra) {
        System.out.println("No se puede reembolsar una compra que no ha sido pagada.");
    }
}
