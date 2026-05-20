package uniquindio.edu.co.eventos.model;

import uniquindio.edu.co.eventos.model.enums.TipoNotificacion;

import java.time.LocalDateTime;

public class Notificacion {

    private String idNotificacion;
    private String titulo;
    private String mensaje;
    private LocalDateTime fecha;
    private boolean leida;
    private String idUsuarioDestino;
    private TipoNotificacion tipoNotificacion;

    public Notificacion() {
        this.fecha = LocalDateTime.now();
        this.leida = false;
        this.tipoNotificacion = TipoNotificacion.SISTEMA;
    }

    public Notificacion(String idNotificacion, String titulo, String mensaje, String idUsuarioDestino, TipoNotificacion tipoNotificacion) {
        this.idNotificacion = idNotificacion;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
        this.leida = false;
        this.idUsuarioDestino = idUsuarioDestino;
        this.tipoNotificacion = tipoNotificacion;
    }

    public String getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(String idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public String getIdUsuarioDestino() {
        return idUsuarioDestino;
    }

    public void setIdUsuarioDestino(String idUsuarioDestino) {
        this.idUsuarioDestino = idUsuarioDestino;
    }

    public TipoNotificacion getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }
}
