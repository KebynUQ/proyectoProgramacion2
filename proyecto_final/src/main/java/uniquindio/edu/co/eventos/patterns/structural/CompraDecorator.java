package uniquindio.edu.co.eventos.patterns.structural;

public abstract class CompraDecorator implements ServicioAdicional {

    protected ServicioAdicional servicioAdicional;

    public CompraDecorator(ServicioAdicional servicioAdicional) {
        this.servicioAdicional = servicioAdicional;
    }

    @Override
    public String getDescripcion() {
        if (servicioAdicional == null) {
            return "";
        }
        return servicioAdicional.getDescripcion();
    }

    @Override
    public double getPrecio() {
        if (servicioAdicional == null) {
            return 0;
        }
        return servicioAdicional.getPrecio();
    }
}
