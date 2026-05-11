package uniquindio.edu.co.eventos.patterns.structural;

public class SeguroCancelacion extends CompraDecorator {

    public SeguroCancelacion(ServicioAdicional servicioAdicional) {
        super(servicioAdicional);
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Seguro de cancelación";
    }

    @Override
    public double getPrecio() {
        return super.getPrecio() + 12000;
    }
}
