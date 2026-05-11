package uniquindio.edu.co.eventos.model;

import uniquindio.edu.co.eventos.model.enums.TipoIncidencia;

import java.time.LocalDateTime;

public class Incidencia {

    private String idIncidencia;
    private TipoIncidencia tipo;
    private String descripcion;
    private LocalDateTime fecha;

    public Incidencia() {
        this.fecha = LocalDateTime.now();
    }

    public Incidencia(String idIncidencia, TipoIncidencia tipo, String descripcion) {
        this.idIncidencia = idIncidencia;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
    }

    public void guardarRegistro() {
        System.out.println("Incidencia registrada: " + descripcion);
    }

    public String verDetalle() {
        return "Incidencia: " + tipo + "\n"
                + "Descripción: " + descripcion + "\n"
                + "Fecha: " + fecha;
    }

    public boolean buscarPorFecha(LocalDateTime fechaBuscada) {
        if (fechaBuscada == null || fecha == null) {
            return false;
        }

        return fecha.toLocalDate().equals(fechaBuscada.toLocalDate());
    }

    public boolean buscarPorTipo(TipoIncidencia tipoBuscado) {
        return this.tipo == tipoBuscado;
    }

    public String getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(String idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public TipoIncidencia getTipo() {
        return tipo;
    }

    public void setTipo(TipoIncidencia tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return tipo + " - " + descripcion;
    }
}
