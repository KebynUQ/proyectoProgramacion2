package uniquindio.edu.co.eventos.model;

import uniquindio.edu.co.eventos.model.enums.EstadoPago;
import uniquindio.edu.co.eventos.patterns.behavioral.EstrategiaPago;

import java.time.LocalDateTime;

public class Pago {

    private String idPago;
    private double monto;
    private LocalDateTime fechaPago;
    private EstadoPago estadoPago;
    private String metodoPago;
    private EstrategiaPago estrategiaPago;

    public Pago() {
        this.fechaPago = LocalDateTime.now();
        this.estadoPago = EstadoPago.PENDIENTE;
    }

    public Pago(String idPago, double monto, String metodoPago,
                EstrategiaPago estrategiaPago) {
        this.idPago = idPago;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estrategiaPago = estrategiaPago;
        this.fechaPago = LocalDateTime.now();
        this.estadoPago = EstadoPago.PENDIENTE;
    }

    public boolean procesar() {
        if (estrategiaPago == null) {
            estadoPago = EstadoPago.RECHAZADO;
            return false;
        }

        boolean resultado = estrategiaPago.procesarPago(monto);

        if (resultado) {
            estadoPago = EstadoPago.APROBADO;
        } else {
            estadoPago = EstadoPago.RECHAZADO;
        }

        return resultado;
    }

    public boolean validar() {
        return estadoPago == EstadoPago.APROBADO || procesar();
    }

    public boolean procesarPago() {
        return procesar();
    }

    public String emitirComprobante() {
        return "Comprobante de pago\n"
                + "ID: " + idPago + "\n"
                + "Monto: $" + monto + "\n"
                + "Método: " + metodoPago + "\n"
                + "Estado: " + estadoPago;
    }

    public String getIdPago() {
        return idPago;
    }

    public void setIdPago(String idPago) {
        this.idPago = idPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public EstrategiaPago getEstrategiaPago() {
        return estrategiaPago;
    }

    public void setEstrategiaPago(EstrategiaPago estrategiaPago) {
        this.estrategiaPago = estrategiaPago;
    }

    @Override
    public String toString() {
        return "Pago " + idPago + " - " + estadoPago + " - $" + monto;
    }
}
