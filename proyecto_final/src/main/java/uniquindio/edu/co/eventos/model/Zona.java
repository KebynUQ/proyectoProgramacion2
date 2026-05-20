package uniquindio.edu.co.eventos.model;

import uniquindio.edu.co.eventos.model.enums.EstadoAsiento;

import java.util.ArrayList;

public class Zona {

    private String idZona;
    private String nombre;
    private int capacidad;
    private double precioBase;
    private ArrayList<Asiento> asientos;

    public Zona() {
        this.asientos = new ArrayList<>();
    }

    public Zona(String idZona, String nombre, int capacidad, double precioBase) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        this.asientos = new ArrayList<>();
    }

    public void agregarAsiento(Asiento asiento) {
        if (asiento != null && asientos.size() < capacidad) {
            asientos.add(asiento);
        }
    }

    public int verOcupacion() {
        int ocupadas = 0;

        for (Asiento asiento : asientos) {
            if (asiento.getEstadoAsiento() == EstadoAsiento.VENDIDO ||
                asiento.getEstadoAsiento() == EstadoAsiento.RESERVADO) {
                ocupadas++;
            }
        }

        return ocupadas;
    }

    public int verDisponibilidad() {
        int disponibles = 0;

        for (Asiento asiento : asientos) {
            if (asiento.getEstadoAsiento() == EstadoAsiento.DISPONIBLE) {
                disponibles++;
            }
        }

        return disponibles;
    }

    public ArrayList<Asiento> consultarAsientosDisponibles() {
        ArrayList<Asiento> disponibles = new ArrayList<>();

        for (Asiento asiento : asientos) {
            if (asiento.getEstadoAsiento() == EstadoAsiento.DISPONIBLE) {
                disponibles.add(asiento);
            }
        }

        return disponibles;
    }

    public ArrayList<Asiento> consultarTodosLosAsientos() {
        return asientos;
    }

    public double calcularDisponibilidad() {
        if (asientos.isEmpty()) {
            return 0;
        }

        return (verDisponibilidad() * 100.0) / asientos.size();
    }

    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
        this.idZona = idZona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public ArrayList<Asiento> getAsientos() {
        return asientos;
    }

    public void setAsientos(ArrayList<Asiento> asientos) {
        this.asientos = asientos;
    }

    @Override
    public String toString() {
        return nombre + " - $" + precioBase;
    }
}
