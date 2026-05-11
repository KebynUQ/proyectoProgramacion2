package uniquindio.edu.co.eventos.patterns.behavioral;

import uniquindio.edu.co.eventos.model.Compra;

public class CompraCancelada implements EstadoCompraBehavior {

    @Override
    public void pagar(Compra compra) {
        System.out.println("No se puede pagar una compra cancelada.");
    }

    @Override
    public void confirmar(Compra compra) {
        System.out.println("No se puede confirmar una compra cancelada.");
    }

    @Override
    public void cancelar(Compra compra) {
        System.out.println("La compra ya está cancelada.");
    }

    @Override
    public void reembolsar(Compra compra) {
        System.out.println("No se puede reembolsar una compra cancelada.");
    }
}
