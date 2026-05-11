package uniquindio.edu.co.eventos.model;

import uniquindio.edu.co.eventos.model.enums.EstadoEntrada;

public class Entrada {

    private String idEntrada;
    private Zona zona;
    private Asiento asiento;
    private double precioFinal;
    private EstadoEntrada estadoEntrada;

    public Entrada() {
        this.estadoEntrada = EstadoEntrada.GENERADA;
    }

    public Entrada(String idEntrada, Zona zona, Asiento asiento, double precioFinal) {
        this.idEntrada = idEntrada;
        this.zona = zona;
        this.asiento = asiento;
        this.precioFinal = precioFinal;
        this.estadoEntrada = EstadoEntrada.GENERADA;
    }

    public void generar() {
        this.estadoEntrada = EstadoEntrada.GENERADA;
    }

    public void activar() {
        this.estadoEntrada = EstadoEntrada.ACTIVA;
    }

    public void usar() {
        if (estadoEntrada == EstadoEntrada.ACTIVA) {
            this.estadoEntrada = EstadoEntrada.USADO;
        }
    }

    public void anular() {
        this.estadoEntrada = EstadoEntrada.ANULADO;
    }

    public String getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(String idEntrada) {
        this.idEntrada = idEntrada;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public EstadoEntrada getEstadoEntrada() {
        return estadoEntrada;
    }

    public void setEstadoEntrada(EstadoEntrada estadoEntrada) {
        this.estadoEntrada = estadoEntrada;
    }

    @Override
    public String toString() {
        return "Entrada " + idEntrada + " - " + zona.getNombre() + " - $" + precioFinal;
    }
}
