package uniquindio.edu.co.eventos.patterns.behavioral;

import uniquindio.edu.co.eventos.model.Compra;

public class CompraReembolsada implements EstadoCompraBehavior {

    @Override
    public void pagar(Compra compra) {
        System.out.println("No se puede pagar una compra reembolsada.");
    }

    @Override
    public void confirmar(Compra compra) {
        System.out.println("No se puede confirmar una compra reembolsada.");
    }

    @Override
    public void cancelar(Compra compra) {
        System.out.println("No se puede cancelar una compra reembolsada.");
    }

    @Override
    public void reembolsar(Compra compra) {
        System.out.println("La compra ya fue reembolsada.");
    }
}
