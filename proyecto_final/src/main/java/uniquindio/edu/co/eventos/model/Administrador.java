package uniquindio.edu.co.eventos.model;

public class Administrador {

    private String idAdministrador;
    private String nombreCompleto;
    private String correo;
    private String contrasena;

    public Administrador() {
    }

    public Administrador(String idAdministrador, String nombreCompleto, String correo, String contrasena) {
        this.idAdministrador = idAdministrador;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public boolean accederSistema(String correo, String contrasena) {
        return this.correo.equals(correo) && this.contrasena.equals(contrasena);
    }

    public void gestionarUsuarios() {
        System.out.println("Gestionando usuarios...");
    }

    public void gestionarEventos() {
        System.out.println("Gestionando eventos...");
    }

    public void gestionarRecintos() {
        System.out.println("Gestionando recintos...");
    }

    public void gestionarCompras() {
        System.out.println("Gestionando compras...");
    }

    public void registrarIncidencia() {
        System.out.println("Registrando incidencia...");
    }

    public void consultarMetricas() {
        System.out.println("Consultando métricas...");
    }

    public String getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(String idAdministrador) {
        this.idAdministrador = idAdministrador;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return nombreCompleto + " - " + correo;
    }
}
