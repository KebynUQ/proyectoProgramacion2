package uniquindio.edu.co.eventos.patterns.structural;

import uniquindio.edu.co.eventos.model.Asiento;
import uniquindio.edu.co.eventos.model.Administrador;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Entrada;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Incidencia;
import uniquindio.edu.co.eventos.model.Notificacion;
import uniquindio.edu.co.eventos.model.Pago;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.Zona;
import uniquindio.edu.co.eventos.model.enums.EstadoAsiento;
import uniquindio.edu.co.eventos.model.enums.EstadoCompra;
import uniquindio.edu.co.eventos.model.enums.EstadoPago;
import uniquindio.edu.co.eventos.model.enums.TipoIncidencia;
import uniquindio.edu.co.eventos.model.enums.TipoNotificacion;
import uniquindio.edu.co.eventos.model.enums.TipoSolicitudCompra;
import uniquindio.edu.co.eventos.patterns.behavioral.EstrategiaPago;
import uniquindio.edu.co.eventos.patterns.behavioral.PagoSimulado;
import uniquindio.edu.co.eventos.patterns.creational.CompraBuilder;
import uniquindio.edu.co.eventos.util.GestorNotificaciones;

import java.util.ArrayList;
import java.util.List;

public class CompraFacade {

    private final SistemaEventos sistemaEventos;

    public CompraFacade() {
        this.sistemaEventos = SistemaEventos.getInstancia();
    }

    public Compra crearCompraPendiente(Usuario usuario, Evento evento, Zona zona, Asiento asiento, List<ServicioAdicional> servicios) {
        if (usuario == null || evento == null || zona == null || asiento == null) {
            registrarIncidencia("Datos incompletos para crear la compra");
            return null;
        }

        if (!asiento.estaDisponible()) {
            sistemaEventos.registrarIncidencia(TipoIncidencia.DOBLE_COMPRA, "Intento de compra sobre asiento no disponible " + asiento.getIdAsiento());
            return null;
        }

        asiento.reservar();
        Entrada entrada = new Entrada("ENT-" + System.currentTimeMillis(), zona, asiento, zona.getPrecioBase());

        CompraBuilder builder = new CompraBuilder()
                .conId("COM-" + System.currentTimeMillis())
                .conUsuario(usuario)
                .conEvento(evento)
                .agregarEntrada(entrada);

        if (servicios != null) {
            for (ServicioAdicional servicio : servicios) {
                builder.agregarServicio(servicio);
            }
        }

        Compra compra = builder.build();
        compra.cambiarEstado(EstadoCompra.PENDIENTE_CONFIRMACION);
        compra.limpiarSolicitud();
        sistemaEventos.agregarCompra(compra);
        sistemaEventos.registrarIncidencia(TipoIncidencia.CAMBIO_ESTADO_COMPRA, "Se creo la compra " + compra.getIdCompra() + " en estado " + compra.getEstadoCompra());
        notificarUsuarioCompra(
                compra,
                "Compra creada",
                "Tu compra " + compra.getIdCompra() + " fue creada en estado " + compra.getEstadoCompra() + ".",
                TipoNotificacion.COMPRA
        );
        return compra;
    }

    public boolean modificarCompra(Compra compra, Zona nuevaZona, Asiento nuevoAsiento, List<ServicioAdicional> nuevosServicios,
                                   String metodoPago, boolean modificadaPorAdministrador) {
        if (compra == null || nuevaZona == null || nuevoAsiento == null) {
            return false;
        }

        if (compra.getEstadoCompra() == EstadoCompra.CANCELADA
                || compra.getEstadoCompra() == EstadoCompra.PAGADA
                || compra.getEstadoCompra() == EstadoCompra.CONFIRMADA) {
            return false;
        }

        Entrada entradaActual = compra.getEntradas().isEmpty() ? null : compra.getEntradas().get(0);
        Asiento asientoActual = entradaActual == null ? null : entradaActual.getAsiento();

        if (asientoActual != null && !asientoActual.getIdAsiento().equals(nuevoAsiento.getIdAsiento())) {
            if (!nuevoAsiento.estaDisponible()) {
                return false;
            }
            if (asientoActual.getEstadoAsiento() != EstadoAsiento.VENDIDO) {
                asientoActual.liberar();
            }
            nuevoAsiento.reservar();
        }

        if (entradaActual == null) {
            compra.agregarEntrada(new Entrada("ENT-" + System.currentTimeMillis(), nuevaZona, nuevoAsiento, nuevaZona.getPrecioBase()));
        } else {
            entradaActual.setZona(nuevaZona);
            entradaActual.setAsiento(nuevoAsiento);
            entradaActual.setPrecioFinal(nuevaZona.getPrecioBase());
        }

        compra.setServiciosAdicionales(new ArrayList<>());
        if (nuevosServicios != null) {
            for (ServicioAdicional servicio : nuevosServicios) {
                compra.agregarServicio(servicio);
            }
        }
        compra.cambiarEstado(EstadoCompra.PENDIENTE_CONFIRMACION);
        compra.limpiarSolicitud();
        if (compra.getPago() != null && metodoPago != null && !metodoPago.isBlank()) {
            compra.getPago().setMetodoPago(metodoPago);
        }
        compra.calcularTotal();
        if (modificadaPorAdministrador) {
            sistemaEventos.registrarIncidencia(TipoIncidencia.CAMBIO_ESTADO_COMPRA,
                    "El administrador modifico la compra " + compra.getIdCompra() + ".");
            notificarUsuarioCompra(
                    compra,
                    "Compra modificada por administrador",
                    "La compra " + compra.getIdCompra() + " fue modificada por el administrador.",
                    TipoNotificacion.COMPRA
            );
        } else {
            sistemaEventos.registrarIncidencia(TipoIncidencia.CAMBIO_ESTADO_COMPRA,
                    "Se modifico la compra " + compra.getIdCompra());
        }
        return true;
    }

    public boolean solicitarModificacion(Compra compra) {
        if (compra == null) {
            return false;
        }
        compra.setTipoSolicitud(TipoSolicitudCompra.SOLICITUD_MODIFICACION);
        compra.setMensajeSolicitud("El usuario solicita modificar esta compra.");
        notificarAdministradores(
                "Solicitud de modificacion",
                "El usuario solicito modificar la compra " + compra.getIdCompra() + ".",
                TipoNotificacion.SISTEMA
        );
        return true;
    }

    public boolean solicitarCancelacion(Compra compra) {
        if (compra == null) {
            return false;
        }
        compra.setTipoSolicitud(TipoSolicitudCompra.SOLICITUD_CANCELACION);
        compra.setMensajeSolicitud("El usuario solicita cancelar esta compra.");
        notificarAdministradores(
                "Solicitud de cancelacion",
                "El usuario solicito cancelar la compra " + compra.getIdCompra() + ".",
                TipoNotificacion.SISTEMA
        );
        return true;
    }

    public boolean solicitarConfirmacionPago(Compra compra) {
        if (compra == null) {
            return false;
        }
        compra.setTipoSolicitud(TipoSolicitudCompra.SOLICITUD_CONFIRMACION_PAGO);
        compra.setMensajeSolicitud("El usuario espera confirmacion de pago.");
        notificarAdministradores(
                "Solicitud de confirmacion de pago",
                "El usuario solicito confirmacion de pago para la compra " + compra.getIdCompra() + ".",
                TipoNotificacion.SISTEMA
        );
        return true;
    }

    public boolean aprobarModificacion(Compra compra) {
        if (compra == null || compra.getTipoSolicitud() != TipoSolicitudCompra.SOLICITUD_MODIFICACION) {
            return false;
        }
        return true;
    }

    public boolean aprobarCancelacion(Compra compra) {
        if (compra == null || compra.getTipoSolicitud() != TipoSolicitudCompra.SOLICITUD_CANCELACION) {
            return false;
        }

        Entrada entrada = compra.getEntradas().isEmpty() ? null : compra.getEntradas().get(0);
        if (entrada != null) {
            if (entrada.getAsiento() != null) {
                entrada.getAsiento().liberar();
            }
            entrada.anular();
        }

        compra.cancelar();
        compra.limpiarSolicitud();
        sistemaEventos.registrarIncidencia(TipoIncidencia.CANCELACION_COMPRA, "Se cancelo la compra " + compra.getIdCompra());
        notificarUsuarioCompra(
                compra,
                "Solicitud de cancelacion aprobada",
                "La cancelacion de tu compra " + compra.getIdCompra() + " fue aprobada.",
                TipoNotificacion.COMPRA
        );
        return true;
    }

    public boolean confirmarPagoAdmin(Compra compra) {
        if (compra == null || compra.getTipoSolicitud() != TipoSolicitudCompra.SOLICITUD_CONFIRMACION_PAGO) {
            return false;
        }
        if (compra.getEstadoCompra() == EstadoCompra.CANCELADA) {
            return false;
        }

        Pago pago = compra.getPago();
        if (pago == null) {
            pago = new Pago("PAG-" + System.currentTimeMillis(), compra.getTotal(), "Simulado", new PagoSimulado());
            pago.setEstadoPago(EstadoPago.APROBADO);
            compra.setPago(pago);
            sistemaEventos.agregarPago(pago);
        } else {
            pago.setEstadoPago(EstadoPago.APROBADO);
        }

        compra.cambiarEstado(EstadoCompra.CONFIRMADA);
        compra.activarEntradas();
        Entrada entrada = compra.getEntradas().isEmpty() ? null : compra.getEntradas().get(0);
        if (entrada != null && entrada.getAsiento() != null) {
            entrada.getAsiento().vender();
        }

        compra.limpiarSolicitud();
        sistemaEventos.registrarIncidencia(TipoIncidencia.CAMBIO_ESTADO_COMPRA, "Se confirmo el pago de la compra " + compra.getIdCompra());
        notificarUsuarioCompra(
                compra,
                "Pago confirmado",
                "El pago de la compra " + compra.getIdCompra() + " fue confirmado por el administrador.",
                TipoNotificacion.PAGO
        );
        return true;
    }

    public void rechazarSolicitud(Compra compra) {
        if (compra != null) {
            compra.limpiarSolicitud();
            notificarUsuarioCompra(
                    compra,
                    "Solicitud rechazada",
                    "Tu solicitud asociada a la compra " + compra.getIdCompra() + " fue rechazada.",
                    TipoNotificacion.SISTEMA
            );
        }
    }

    public boolean cancelarCompraComoAdmin(Compra compra) {
        if (compra == null || compra.getEstadoCompra() == EstadoCompra.REEMBOLSADA) {
            return false;
        }

        Entrada entrada = compra.getEntradas().isEmpty() ? null : compra.getEntradas().get(0);
        if (entrada != null) {
            if (entrada.getAsiento() != null && entrada.getAsiento().getEstadoAsiento() != uniquindio.edu.co.eventos.model.enums.EstadoAsiento.VENDIDO) {
                entrada.getAsiento().liberar();
            }
            entrada.anular();
        }

        compra.cancelar();
        compra.limpiarSolicitud();
        sistemaEventos.registrarIncidencia(TipoIncidencia.CANCELACION_COMPRA, "El administrador cancelo la compra " + compra.getIdCompra());
        notificarUsuarioCompra(
                compra,
                "Compra cancelada",
                "El administrador cancelo la compra " + compra.getIdCompra() + ".",
                TipoNotificacion.COMPRA
        );
        return true;
    }

    public boolean registrarReembolso(Compra compra) {
        if (compra == null || (compra.getEstadoCompra() != EstadoCompra.PAGADA && compra.getEstadoCompra() != EstadoCompra.CONFIRMADA)) {
            return false;
        }

        compra.cambiarEstado(EstadoCompra.REEMBOLSADA);
        if (compra.getPago() != null) {
            compra.getPago().setEstadoPago(EstadoPago.RECHAZADO);
        }

        Entrada entrada = compra.getEntradas().isEmpty() ? null : compra.getEntradas().get(0);
        if (entrada != null) {
            entrada.anular();
        }

        sistemaEventos.registrarIncidencia(TipoIncidencia.REEMBOLSO, "Se registro reembolso para la compra " + compra.getIdCompra());
        notificarUsuarioCompra(
                compra,
                "Reembolso registrado",
                "Se registro un reembolso para tu compra " + compra.getIdCompra() + ".",
                TipoNotificacion.PAGO
        );
        return true;
    }

    public boolean confirmarPagoCompra(Compra compra, String metodoPago, EstrategiaPago estrategiaPago) {
        if (compra == null || estrategiaPago == null) {
            return false;
        }
        Pago pago = new Pago("PAG-" + System.currentTimeMillis(), compra.getTotal(), metodoPago, estrategiaPago);
        boolean aprobado = pago.procesarPago();
        if (!aprobado) {
            sistemaEventos.registrarIncidencia(TipoIncidencia.ERROR_PAGO, "Fallo el pago de la compra " + compra.getIdCompra());
            return false;
        }
        compra.pagar(pago);
        compra.activarEntradas();
        Entrada entrada = compra.getEntradas().isEmpty() ? null : compra.getEntradas().get(0);
        if (entrada != null && entrada.getAsiento() != null) {
            entrada.getAsiento().vender();
        }
        sistemaEventos.agregarPago(pago);
        sistemaEventos.registrarIncidencia(TipoIncidencia.CAMBIO_ESTADO_COMPRA, "La compra " + compra.getIdCompra() + " fue pagada.");
        notificarUsuarioCompra(
                compra,
                "Compra pagada",
                "La compra " + compra.getIdCompra() + " fue pagada correctamente.",
                TipoNotificacion.PAGO
        );
        return true;
    }

    public Compra realizarCompra(Usuario usuario, Evento evento, Zona zona, Asiento asiento, EstrategiaPago estrategiaPago) {
        Compra compra = crearCompraPendiente(usuario, evento, zona, asiento, new ArrayList<>());
        if (compra == null) {
            return null;
        }
        boolean exito = confirmarPagoCompra(compra, "Pago simulado", estrategiaPago);
        if (!exito) {
            compra.cancelar();
        }
        return compra;
    }

    private void registrarIncidencia(String descripcion) {
        Incidencia incidencia = new Incidencia(
                "INC-" + System.currentTimeMillis(),
                TipoIncidencia.ERROR_PAGO,
                descripcion
        );
        sistemaEventos.agregarIncidencia(incidencia);
    }

    private void notificarUsuarioCompra(Compra compra, String titulo, String mensaje, TipoNotificacion tipoNotificacion) {
        if (compra == null || compra.getUsuario() == null || compra.getUsuario().getIdUsuario() == null) {
            return;
        }

        Notificacion notificacion = new Notificacion(
                "NOT-" + System.currentTimeMillis() + "-" + compra.getUsuario().getIdUsuario(),
                titulo,
                mensaje,
                compra.getUsuario().getIdUsuario(),
                tipoNotificacion
        );
        GestorNotificaciones.getInstancia().guardarNotificacion(notificacion);
        compra.getUsuario().recibirNotificacion(notificacion);
    }

    private void notificarAdministradores(String titulo, String mensaje, TipoNotificacion tipoNotificacion) {
        for (Administrador administrador : sistemaEventos.getAdministradores()) {
            if (administrador.getIdAdministrador() == null) {
                continue;
            }
            GestorNotificaciones.getInstancia().guardarNotificacion(
                    new Notificacion(
                            "NOT-" + System.currentTimeMillis() + "-" + administrador.getIdAdministrador(),
                            titulo,
                            mensaje,
                            administrador.getIdAdministrador(),
                            tipoNotificacion
                    )
            );
        }
    }
}
