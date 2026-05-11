package uniquindio.edu.co.eventos.patterns.structural;

public class ServicioVIP extends CompraDecorator {

    public ServicioVIP(ServicioAdicional servicioAdicional) {
        super(servicioAdicional);
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Servicio VIP";
    }

    @Override
    public double getPrecio() {
        return super.getPrecio() + 25000;
    }
}
