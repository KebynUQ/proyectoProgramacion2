package uniquindio.edu.co.eventos.patterns.behavioral;

import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.enums.EstadoCompra;

public class CompraConfirmada implements EstadoCompraBehavior {

    @Override
    public void pagar(Compra compra) {
        System.out.println("La compra ya fue pagada y confirmada.");
    }

    @Override
    public void confirmar(Compra compra) {
        System.out.println("La compra ya está confirmada.");
    }

    @Override
    public void cancelar(Compra compra) {
        System.out.println("No se puede cancelar una compra confirmada.");
    }

    @Override
    public void reembolsar(Compra compra) {
        compra.cambiarEstado(EstadoCompra.REEMBOLSADA);
    }
}
