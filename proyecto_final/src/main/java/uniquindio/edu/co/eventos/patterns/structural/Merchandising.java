package uniquindio.edu.co.eventos.patterns.structural;

public class Merchandising extends CompraDecorator {

    public Merchandising(ServicioAdicional servicioAdicional) {
        super(servicioAdicional);
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Merchandising";
    }

    @Override
    public double getPrecio() {
        return super.getPrecio() + 18000;
    }
}
