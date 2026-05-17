package uniquindio.edu.co.eventos.model;

public class Sesion {

    private static Usuario usuarioActual;
    private static Administrador administradorActual;
    private static String tipoUsuario;
    private static Evento eventoSeleccionadoParaCompra;

    private Sesion() {
    }

    public static void iniciarSesionUsuario(Usuario usuario) {
        usuarioActual = usuario;
        administradorActual = null;
        tipoUsuario = "USUARIO";
        eventoSeleccionadoParaCompra = null;
    }

    public static void iniciarSesionAdministrador(Administrador administrador) {
        administradorActual = administrador;
        usuarioActual = null;
        tipoUsuario = "ADMINISTRADOR";
        eventoSeleccionadoParaCompra = null;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
        administradorActual = null;
        tipoUsuario = null;
        eventoSeleccionadoParaCompra = null;
    }

    public static boolean haySesionActiva() {
        return usuarioActual != null || administradorActual != null;
    }

    public static boolean esUsuario() {
        return "USUARIO".equals(tipoUsuario);
    }

    public static boolean esAdministrador() {
        return "ADMINISTRADOR".equals(tipoUsuario);
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static Administrador getAdministradorActual() {
        return administradorActual;
    }

    public static String getTipoUsuario() {
        return tipoUsuario;
    }

    public static Evento getEventoSeleccionadoParaCompra() {
        return eventoSeleccionadoParaCompra;
    }

    public static void setEventoSeleccionadoParaCompra(Evento evento) {
        eventoSeleccionadoParaCompra = evento;
    }
}
