package uniquindio.edu.co.eventos.patterns.behavioral;

public class PagoTarjeta implements EstrategiaPago {

    @Override
    public boolean procesarPago(double monto) {
        return monto > 0;
    }
}
