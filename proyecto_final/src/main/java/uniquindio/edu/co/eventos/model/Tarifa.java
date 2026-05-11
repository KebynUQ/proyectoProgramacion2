package uniquindio.edu.co.eventos.model;

public class Tarifa {

    private String idTarifa;
    private String nombre;
    private double porcentaje;
    private double valorFijo;

    public Tarifa() {
    }

    public Tarifa(String idTarifa, String nombre, double porcentaje, double valorFijo) {
        this.idTarifa = idTarifa;
        this.nombre = nombre;
        this.porcentaje = porcentaje;
        this.valorFijo = valorFijo;
    }

    public double obtenerPrecio(double precioBase) {
        double incrementoPorcentaje = precioBase * porcentaje / 100;
        return precioBase + incrementoPorcentaje + valorFijo;
    }

    public String consultarPrecio(double precioBase) {
        return nombre + ": $" + obtenerPrecio(precioBase);
    }

    public String getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(String idTarifa) {
        this.idTarifa = idTarifa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public double getValorFijo() {
        return valorFijo;
    }

    public void setValorFijo(double valorFijo) {
        this.valorFijo = valorFijo;
    }

    @Override
    public String toString() {
        return nombre + " - " + porcentaje + "% + $" + valorFijo;
    }
}
