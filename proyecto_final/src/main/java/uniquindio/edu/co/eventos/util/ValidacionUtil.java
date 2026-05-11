package uniquindio.edu.co.eventos.util;

public class ValidacionUtil {

    private ValidacionUtil() {
    }

    public static boolean campoVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public static boolean correoValido(String correo) {
        return correo != null && correo.contains("@") && correo.contains(".");
    }

    public static boolean numeroPositivo(double numero) {
        return numero > 0;
    }

    public static boolean contrasenaValida(String contrasena) {
        return contrasena != null && contrasena.length() >= 4;
    }
}
