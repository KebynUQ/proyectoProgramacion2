package uniquindio.edu.co.eventos.model;

import uniquindio.edu.co.eventos.model.enums.TipoIncidencia;
import uniquindio.edu.co.eventos.model.enums.TipoNotificacion;
import uniquindio.edu.co.eventos.util.GestorNotificaciones;

import java.util.ArrayList;

public class SistemaEventos {

    private static SistemaEventos instancia;

    private ArrayList<Usuario> usuarios;
    private ArrayList<Administrador> administradores;
    private ArrayList<Evento> eventos;
    private ArrayList<Recinto> recintos;
    private ArrayList<Compra> compras;
    private ArrayList<Pago> pagos;
    private ArrayList<Incidencia> incidencias;

    private SistemaEventos() {
        this.usuarios = new ArrayList<>();
        this.administradores = new ArrayList<>();
        this.eventos = new ArrayList<>();
        this.recintos = new ArrayList<>();
        this.compras = new ArrayList<>();
        this.pagos = new ArrayList<>();
        this.incidencias = new ArrayList<>();
    }

    public static SistemaEventos getInstancia() {
        if (instancia == null) {
            instancia = new SistemaEventos();
        }
        return instancia;
    }

    public void registrarUsuario(Usuario usuario) {
        if (usuario != null && buscarUsuarioPorCorreo(usuario.getCorreo()) == null) {
            usuarios.add(usuario);
        }
    }

    public void registrarAdministrador(Administrador administrador) {
        if (administrador != null && buscarAdministradorPorCorreo(administrador.getCorreo()) == null) {
            administradores.add(administrador);
        }
    }

    public Usuario validarLoginUsuario(String correo, String contrasena) {
        for (Usuario usuario : usuarios) {
            if (usuario.accederSistema(correo, contrasena)) {
                return usuario;
            }
        }
        return null;
    }

    public Administrador validarLoginAdministrador(String correo, String contrasena) {
        for (Administrador administrador : administradores) {
            if (administrador.accederSistema(correo, contrasena)) {
                return administrador;
            }
        }
        return null;
    }

    public void agregarEvento(Evento evento) {
        if (evento != null) {
            eventos.add(evento);
        }
    }

    public void agregarRecinto(Recinto recinto) {
        if (recinto != null) {
            recintos.add(recinto);
        }
    }

    public void agregarCompra(Compra compra) {
        if (compra != null) {
            compras.add(compra);

            if (compra.getUsuario() != null) {
                compra.getUsuario().agregarCompra(compra);
            }
        }
    }

    public void agregarPago(Pago pago) {
        if (pago != null) {
            pagos.add(pago);
        }
    }

    public void agregarIncidencia(Incidencia incidencia) {
        if (incidencia != null) {
            incidencias.add(incidencia);
        }
    }

    public boolean actualizarUsuario(Usuario usuarioOriginal, String nombre, String correo, String telefono, String contrasena) {
        if (usuarioOriginal == null) {
            return false;
        }

        Usuario existente = buscarUsuarioPorCorreo(correo);
        if (existente != null && existente != usuarioOriginal) {
            return false;
        }

        usuarioOriginal.setNombreCompleto(nombre);
        usuarioOriginal.setCorreo(correo);
        usuarioOriginal.setTelefono(telefono);
        if (contrasena != null && !contrasena.isBlank()) {
            usuarioOriginal.setContrasena(contrasena);
        }
        return true;
    }

    public boolean eliminarUsuario(Usuario usuario) {
        return usuario != null && usuarios.remove(usuario);
    }

    public boolean eliminarEvento(Evento evento) {
        return evento != null && eventos.remove(evento);
    }

    public boolean eliminarRecinto(Recinto recinto) {
        return recinto != null && recintos.remove(recinto);
    }

    public Administrador buscarAdministradorPorCorreo(String correo) {
        for (Administrador administrador : administradores) {
            if (administrador.getCorreo().equalsIgnoreCase(correo)) {
                return administrador;
            }
        }
        return null;
    }

    public ArrayList<Evento> listarEventos() {
        return eventos;
    }

    public ArrayList<Usuario> listarUsuarios() {
        return usuarios;
    }

    public ArrayList<Compra> listarCompras() {
        return compras;
    }

    public ArrayList<Incidencia> listarIncidencias() {
        return incidencias;
    }

    public ArrayList<Evento> buscarEventosPorCiudad(String ciudad) {
        ArrayList<Evento> resultado = new ArrayList<>();

        for (Evento evento : eventos) {
            if (evento.getCiudad().equalsIgnoreCase(ciudad)) {
                resultado.add(evento);
            }
        }

        return resultado;
    }

    public ArrayList<Evento> buscarEventosPorCategoria(String categoria) {
        ArrayList<Evento> resultado = new ArrayList<>();

        for (Evento evento : eventos) {
            if (evento.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(evento);
            }
        }

        return resultado;
    }

    public ArrayList<Evento> buscarEventosPorPrecioMaximo(double precioMaximo) {
        ArrayList<Evento> resultado = new ArrayList<>();

        for (Evento evento : eventos) {
            if (evento.getPrecioBase() <= precioMaximo) {
                resultado.add(evento);
            }
        }

        return resultado;
    }

    public Usuario buscarUsuarioPorCorreo(String correo) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(correo)) {
                return usuario;
            }
        }
        return null;
    }

    public Evento buscarEventoPorId(String idEvento) {
        for (Evento evento : eventos) {
            if (evento.getIdEvento().equals(idEvento)) {
                return evento;
            }
        }
        return null;
    }

    public Compra buscarCompraPorId(String idCompra) {
        for (Compra compra : compras) {
            if (compra.getIdCompra().equals(idCompra)) {
                return compra;
            }
        }
        return null;
    }

    public Recinto buscarRecintoPorId(String idRecinto) {
        for (Recinto recinto : recintos) {
            if (recinto.getIdRecinto().equals(idRecinto)) {
                return recinto;
            }
        }
        return null;
    }

    public void registrarIncidencia(TipoIncidencia tipo, String descripcion) {
        agregarIncidencia(new Incidencia("INC-" + System.currentTimeMillis(), tipo, descripcion));

        for (Administrador administrador : administradores) {
            if (administrador.getIdAdministrador() == null) {
                continue;
            }
            GestorNotificaciones.getInstancia().guardarNotificacion(
                    new Notificacion(
                            "NOT-" + System.currentTimeMillis() + "-" + administrador.getIdAdministrador(),
                            "Incidencia registrada",
                            descripcion,
                            administrador.getIdAdministrador(),
                            TipoNotificacion.INCIDENCIA
                    )
            );
        }
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public ArrayList<Administrador> getAdministradores() {
        return administradores;
    }

    public void setAdministradores(ArrayList<Administrador> administradores) {
        this.administradores = administradores;
    }

    public ArrayList<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(ArrayList<Evento> eventos) {
        this.eventos = eventos;
    }

    public ArrayList<Recinto> getRecintos() {
        return recintos;
    }

    public void setRecintos(ArrayList<Recinto> recintos) {
        this.recintos = recintos;
    }

    public ArrayList<Compra> getCompras() {
        return compras;
    }

    public void setCompras(ArrayList<Compra> compras) {
        this.compras = compras;
    }

    public ArrayList<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(ArrayList<Pago> pagos) {
        this.pagos = pagos;
    }

    public ArrayList<Incidencia> getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(ArrayList<Incidencia> incidencias) {
        this.incidencias = incidencias;
    }
}
