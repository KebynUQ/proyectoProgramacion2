package uniquindio.edu.co.eventos.patterns.structural;

public class AccesoPreferencial extends CompraDecorator {

    public AccesoPreferencial(ServicioAdicional servicioAdicional) {
        super(servicioAdicional);
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Acceso preferencial";
    }

    @Override
    public double getPrecio() {
        return super.getPrecio() + 15000;
    }
}
