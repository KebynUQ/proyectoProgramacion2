package uniquindio.edu.co.eventos.model;

import uniquindio.edu.co.eventos.model.enums.EstadoEvento;

import java.time.LocalDateTime;

public class Evento {

    private String idEvento;
    private String nombre;
    private String categoria;
    private String descripcion;
    private String ciudad;
    private LocalDateTime fechaHora;
    private EstadoEvento estadoEvento;
    private Recinto recinto;
    private double precioBase;
    private String politicaCancelacion;
    private String politicaReembolso;
    private String reglasGenerales;

    public Evento() {
        this.estadoEvento = EstadoEvento.BORRADOR;
        this.politicaCancelacion = "Cancelacion permitida hasta 24 horas antes del evento.";
        this.politicaReembolso = "Reembolso sujeto a politica del evento.";
        this.reglasGenerales = "La entrada debe presentarse activa el dia del evento.";
    }

    public Evento(String idEvento, String nombre, String categoria, String descripcion,
                  String ciudad, LocalDateTime fechaHora, Recinto recinto, double precioBase) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.fechaHora = fechaHora;
        this.recinto = recinto;
        this.precioBase = precioBase;
        this.estadoEvento = EstadoEvento.BORRADOR;
        this.politicaCancelacion = "Cancelacion permitida hasta 24 horas antes del evento.";
        this.politicaReembolso = "Reembolso sujeto a politica del evento.";
        this.reglasGenerales = "La entrada debe presentarse activa el dia del evento.";
    }

    public void habilitar() {
        this.estadoEvento = EstadoEvento.PUBLICADO;
    }

    public void suspender() {
        this.estadoEvento = EstadoEvento.PAUSADO;
    }

    public void anular() {
        this.estadoEvento = EstadoEvento.CANCELADO;
    }

    public boolean verDisponibilidad() {
        return estadoEvento == EstadoEvento.PUBLICADO && recinto != null;
    }

    public void publicar() {
        this.estadoEvento = EstadoEvento.PUBLICADO;
    }

    public void pausar() {
        this.estadoEvento = EstadoEvento.PAUSADO;
    }

    public void cancelar() {
        this.estadoEvento = EstadoEvento.CANCELADO;
    }

    public void cambiarEstado(EstadoEvento estadoEvento) {
        this.estadoEvento = estadoEvento;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public EstadoEvento getEstadoEvento() {
        return estadoEvento;
    }

    public void setEstadoEvento(EstadoEvento estadoEvento) {
        this.estadoEvento = estadoEvento;
    }

    public Recinto getRecinto() {
        return recinto;
    }

    public void setRecinto(Recinto recinto) {
        this.recinto = recinto;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public String getPoliticaCancelacion() {
        return politicaCancelacion;
    }

    public void setPoliticaCancelacion(String politicaCancelacion) {
        this.politicaCancelacion = politicaCancelacion;
    }

    public String getPoliticaReembolso() {
        return politicaReembolso;
    }

    public void setPoliticaReembolso(String politicaReembolso) {
        this.politicaReembolso = politicaReembolso;
    }

    public String getReglasGenerales() {
        return reglasGenerales;
    }

    public void setReglasGenerales(String reglasGenerales) {
        this.reglasGenerales = reglasGenerales;
    }

    @Override
    public String toString() {
        return nombre + " - " + categoria + " - " + ciudad;
    }
}
