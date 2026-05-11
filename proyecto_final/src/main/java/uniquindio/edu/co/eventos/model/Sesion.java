package uniquindio.edu.co.eventos.model;

public class Sesion {

    private static Usuario usuarioActual;
    private static Administrador administradorActual;
    private static String tipoUsuario;

    private Sesion() {
    }

    public static void iniciarSesionUsuario(Usuario usuario) {
        usuarioActual = usuario;
        administradorActual = null;
        tipoUsuario = "USUARIO";
    }

    public static void iniciarSesionAdministrador(Administrador administrador) {
        administradorActual = administrador;
        usuarioActual = null;
        tipoUsuario = "ADMINISTRADOR";
    }

    public static void cerrarSesion() {
        usuarioActual = null;
        administradorActual = null;
        tipoUsuario = null;
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
}
