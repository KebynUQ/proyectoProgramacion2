package uniquindio.edu.co.eventos.model;

import uniquindio.edu.co.eventos.model.enums.EstadoAsiento;

public class Asiento {

    private String idAsiento;
    private String fila;
    private int numero;
    private EstadoAsiento estadoAsiento;

    public Asiento() {
        this.estadoAsiento = EstadoAsiento.DISPONIBLE;
    }

    public Asiento(String idAsiento, String fila, int numero) {
        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
        this.estadoAsiento = EstadoAsiento.DISPONIBLE;
    }

    public void apartar() {
        if (estadoAsiento == EstadoAsiento.DISPONIBLE) {
            estadoAsiento = EstadoAsiento.RESERVADO;
        }
    }

    public void reservar() {
        apartar();
    }

    public void comercializar() {
        if (estadoAsiento == EstadoAsiento.RESERVADO || estadoAsiento == EstadoAsiento.DISPONIBLE) {
            estadoAsiento = EstadoAsiento.VENDIDO;
        }
    }

    public void vender() {
        comercializar();
    }

    public void inhabilitar() {
        if (estadoAsiento != EstadoAsiento.VENDIDO) {
            estadoAsiento = EstadoAsiento.BLOQUEADO;
        }
    }

    public void desocupar() {
        if (estadoAsiento != EstadoAsiento.VENDIDO) {
            estadoAsiento = EstadoAsiento.DISPONIBLE;
        }
    }

    public void liberar() {
        desocupar();
    }

    public boolean estaDisponible() {
        return estadoAsiento == EstadoAsiento.DISPONIBLE;
    }

    public boolean estaBloqueado() {
        return estadoAsiento == EstadoAsiento.BLOQUEADO;
    }

    public String getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(String idAsiento) {
        this.idAsiento = idAsiento;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public EstadoAsiento getEstadoAsiento() {
        return estadoAsiento;
    }

    public void setEstadoAsiento(EstadoAsiento estadoAsiento) {
        this.estadoAsiento = estadoAsiento;
    }

    @Override
    public String toString() {
        return "Fila " + fila + " - Asiento " + numero + " (" + estadoAsiento + ")";
    }
}
