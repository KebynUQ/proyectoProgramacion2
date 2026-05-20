package uniquindio.edu.co.eventos.model;

import uniquindio.edu.co.eventos.model.enums.EstadoCompra;
import uniquindio.edu.co.eventos.model.enums.TipoSolicitudCompra;
import uniquindio.edu.co.eventos.patterns.structural.ServicioAdicional;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Compra {

    private String idCompra;
    private Usuario usuario;
    private Evento evento;
    private LocalDateTime fechaCreacion;
    private double total;
    private EstadoCompra estadoCompra;
    private ArrayList<Entrada> entradas;
    private ArrayList<ServicioAdicional> serviciosAdicionales;
    private Pago pago;
    private TipoSolicitudCompra tipoSolicitud;
    private String mensajeSolicitud;

    public Compra() {
        this.fechaCreacion = LocalDateTime.now();
        this.estadoCompra = EstadoCompra.CREADA;
        this.entradas = new ArrayList<>();
        this.serviciosAdicionales = new ArrayList<>();
        this.tipoSolicitud = TipoSolicitudCompra.SIN_SOLICITUD;
        this.mensajeSolicitud = "Sin solicitud";
    }

    public Compra(String idCompra, Usuario usuario, Evento evento) {
        this.idCompra = idCompra;
        this.usuario = usuario;
        this.evento = evento;
        this.fechaCreacion = LocalDateTime.now();
        this.estadoCompra = EstadoCompra.CREADA;
        this.entradas = new ArrayList<>();
        this.serviciosAdicionales = new ArrayList<>();
        this.total = 0;
        this.tipoSolicitud = TipoSolicitudCompra.SIN_SOLICITUD;
        this.mensajeSolicitud = "Sin solicitud";
    }

    public void agregarEntrada(Entrada entrada) {
        if (entrada != null) {
            entradas.add(entrada);
            obtenerTotal();
        }
    }

    public void eliminarEntrada(Entrada entrada) {
        if (entrada != null) {
            entradas.remove(entrada);
            obtenerTotal();
        }
    }

    public void agregarServicio(ServicioAdicional servicio) {
        if (servicio != null) {
            serviciosAdicionales.add(servicio);
            obtenerTotal();
        }
    }

    public double obtenerTotal() {
        double suma = 0;

        for (Entrada entrada : entradas) {
            suma += entrada.getPrecioFinal();
        }

        for (ServicioAdicional servicio : serviciosAdicionales) {
            suma += servicio.getPrecio();
        }

        this.total = suma;
        return total;
    }

    public void realizarPago(Pago pago) {
        if (pago != null && pago.validar()) {
            this.pago = pago;
            this.estadoCompra = EstadoCompra.PAGADA;
        }
    }

    public void pagar(Pago pago) {
        realizarPago(pago);
    }

    public void confirmarPago() {
        if (estadoCompra == EstadoCompra.PAGADA || estadoCompra == EstadoCompra.PENDIENTE_PAGO) {
            estadoCompra = EstadoCompra.CONFIRMADA;
            activarEntradas();
        }
    }

    public void anular() {
        this.estadoCompra = EstadoCompra.CANCELADA;

        for (Entrada entrada : entradas) {
            entrada.anular();
        }
    }

    public void cancelar() {
        anular();
    }

    public double calcularTotal() {
        return obtenerTotal();
    }

    public void cambiarEstado(EstadoCompra estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public boolean puedeModificar() {
        return estadoCompra == EstadoCompra.CREADA
                || estadoCompra == EstadoCompra.PENDIENTE_PAGO
                || estadoCompra == EstadoCompra.PENDIENTE_CONFIRMACION;
    }

    public boolean puedeCancelar() {
        return estadoCompra == EstadoCompra.CREADA
                || estadoCompra == EstadoCompra.PENDIENTE_PAGO
                || estadoCompra == EstadoCompra.PENDIENTE_CONFIRMACION;
    }

    public boolean puedePagar() {
        return estadoCompra == EstadoCompra.CREADA
                || estadoCompra == EstadoCompra.PENDIENTE_PAGO
                || estadoCompra == EstadoCompra.PENDIENTE_CONFIRMACION;
    }

    public void activarEntradas() {
        for (Entrada entrada : entradas) {
            entrada.activar();
        }
    }

    public boolean tieneSolicitudPendiente() {
        return tipoSolicitud != null && tipoSolicitud != TipoSolicitudCompra.SIN_SOLICITUD;
    }

    public void limpiarSolicitud() {
        this.tipoSolicitud = TipoSolicitudCompra.SIN_SOLICITUD;
        this.mensajeSolicitud = "Sin solicitud";
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public EstadoCompra getEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(EstadoCompra estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public ArrayList<Entrada> getEntradas() {
        return entradas;
    }

    public void setEntradas(ArrayList<Entrada> entradas) {
        this.entradas = entradas;
    }

    public ArrayList<ServicioAdicional> getServiciosAdicionales() {
        return serviciosAdicionales;
    }

    public void setServiciosAdicionales(ArrayList<ServicioAdicional> serviciosAdicionales) {
        this.serviciosAdicionales = serviciosAdicionales;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public TipoSolicitudCompra getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(TipoSolicitudCompra tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public String getMensajeSolicitud() {
        return mensajeSolicitud;
    }

    public void setMensajeSolicitud(String mensajeSolicitud) {
        this.mensajeSolicitud = mensajeSolicitud;
    }

    @Override
    public String toString() {
        return "Compra " + idCompra + " - " + estadoCompra + " - $" + total;
    }
}
