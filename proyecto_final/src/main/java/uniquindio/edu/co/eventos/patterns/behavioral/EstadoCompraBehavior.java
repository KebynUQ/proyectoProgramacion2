package uniquindio.edu.co.eventos.patterns.behavioral;

import uniquindio.edu.co.eventos.model.Compra;

public interface EstadoCompraBehavior {

    void pagar(Compra compra);

    void confirmar(Compra compra);

    void cancelar(Compra compra);

    void reembolsar(Compra compra);
}
