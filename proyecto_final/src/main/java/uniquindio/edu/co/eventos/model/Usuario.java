package uniquindio.edu.co.eventos.model;

import java.util.ArrayList;

public class Usuario {

    private String idUsuario;
    private String nombreCompleto;
    private String correo;
    private String telefono;
    private String contrasena;
    private ArrayList<Compra> compras;
    private ArrayList<String> notificaciones;

    public Usuario() {
        this.compras = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    public Usuario(String idUsuario, String nombreCompleto, String correo, String telefono, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.compras = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    public void inscribirse() {
        System.out.println("Usuario inscrito: " + nombreCompleto);
    }

    public boolean accederSistema(String correo, String contrasena) {
        return this.correo.equals(correo) && this.contrasena.equals(contrasena);
    }

    public void modificarPerfil(String nombreCompleto, String telefono) {
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
    }

    public void actualizarPerfil(String nombreCompleto, String telefono) {
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
    }

    public ArrayList<Compra> consultarCompras() {
        return compras;
    }

    public String exportarReporte() {
        return "Reporte del usuario: " + nombreCompleto + " - Compras: " + compras.size();
    }

    public void agregarCompra(Compra compra) {
        if (compra != null) {
            compras.add(compra);
        }
    }

    public void recibirNotificacion(String mensaje) {
        notificaciones.add(mensaje);
    }

    public void recibirNotificacion(Notificacion notificacion) {
        if (notificacion != null) {
            recibirNotificacion(notificacion.getTitulo() + ": " + notificacion.getMensaje());
        }
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public ArrayList<Compra> getCompras() {
        return compras;
    }

    public void setCompras(ArrayList<Compra> compras) {
        this.compras = compras;
    }

    public ArrayList<String> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(ArrayList<String> notificaciones) {
        this.notificaciones = notificaciones;
    }

    @Override
    public String toString() {
        return nombreCompleto + " - " + correo;
    }
}
