package uniquindio.edu.co.eventos.patterns.structural;

public class Parqueadero extends CompraDecorator {

    public Parqueadero(ServicioAdicional servicioAdicional) {
        super(servicioAdicional);
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Parqueadero";
    }

    @Override
    public double getPrecio() {
        return super.getPrecio() + 10000;
    }
}
