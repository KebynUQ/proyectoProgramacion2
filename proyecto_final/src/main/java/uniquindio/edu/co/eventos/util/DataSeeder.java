package uniquindio.edu.co.eventos.util;

import uniquindio.edu.co.eventos.model.Administrador;
import uniquindio.edu.co.eventos.model.Asiento;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Entrada;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Incidencia;
import uniquindio.edu.co.eventos.model.Notificacion;
import uniquindio.edu.co.eventos.model.Recinto;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.Zona;
import uniquindio.edu.co.eventos.model.enums.EstadoEvento;
import uniquindio.edu.co.eventos.model.enums.TipoIncidencia;
import uniquindio.edu.co.eventos.model.enums.TipoNotificacion;
import uniquindio.edu.co.eventos.patterns.creational.ConciertoFactory;
import uniquindio.edu.co.eventos.patterns.creational.ConferenciaFactory;
import uniquindio.edu.co.eventos.patterns.creational.TeatroFactory;

import java.time.LocalDateTime;

public class DataSeeder {

    private DataSeeder() {
    }

    public static void cargarDatosIniciales() {
        SistemaEventos sistema = SistemaEventos.getInstancia();

        if (!sistema.getUsuarios().isEmpty() || !sistema.getEventos().isEmpty()) {
            return;
        }

        cargarUsuarios(sistema);
        cargarEventos(sistema);
        cargarIncidencias(sistema);
        cargarNotificacionesIniciales(sistema);
    }

    private static void cargarUsuarios(SistemaEventos sistema) {
        Usuario usuario1 = new Usuario(
                "USU-001",
                "Usuario de Prueba",
                "cliente@uq.edu.co",
                "3001234567",
                "1234"
        );

        Usuario usuario2 = new Usuario(
                "USU-002",
                "Laura Gomez",
                "laura@uq.edu.co",
                "3112223344",
                "1234"
        );

        Usuario usuario3 = new Usuario(
                "USU-003",
                "Carlos Ramirez",
                "carlos@uq.edu.co",
                "3225556677",
                "1234"
        );

        Administrador administrador = new Administrador(
                "ADM-001",
                "Administrador UQ",
                "admin@uq.edu.co",
                "1234"
        );

        sistema.registrarUsuario(usuario1);
        sistema.registrarUsuario(usuario2);
        sistema.registrarUsuario(usuario3);
        sistema.registrarAdministrador(administrador);
    }

    private static void cargarEventos(SistemaEventos sistema) {
        Recinto recintoPrincipal = new Recinto(
                "REC-001",
                "Auditorio Universidad del Quindio",
                "Carrera 15 Calle 12 Norte",
                "Armenia"
        );

        Zona zonaVIP = new Zona("ZON-001", "VIP", 10, 80000);
        Zona zonaPreferencial = new Zona("ZON-002", "Preferencial", 15, 50000);
        Zona zonaGeneral = new Zona("ZON-003", "General", 20, 30000);

        agregarAsientos(zonaVIP, "A", 10);
        agregarAsientos(zonaPreferencial, "B", 15);
        agregarAsientos(zonaGeneral, "C", 20);

        recintoPrincipal.agregarZona(zonaVIP);
        recintoPrincipal.agregarZona(zonaPreferencial);
        recintoPrincipal.agregarZona(zonaGeneral);

        sistema.agregarRecinto(recintoPrincipal);

        ConciertoFactory conciertoFactory = new ConciertoFactory();
        TeatroFactory teatroFactory = new TeatroFactory();
        ConferenciaFactory conferenciaFactory = new ConferenciaFactory();

        Evento concierto = conciertoFactory.crearEvento(
                "EVE-001",
                "Concierto Universitario",
                "Evento musical para la comunidad universitaria.",
                "Armenia",
                LocalDateTime.now().plusDays(10),
                recintoPrincipal,
                30000
        );
        concierto.setPoliticaCancelacion("Cancelacion permitida hasta 24 horas antes del evento.");
        concierto.setPoliticaReembolso("Reembolso sujeto a revision del estado de la compra.");
        concierto.setReglasGenerales("La entrada debe presentarse activa el dia del evento.");

        Evento teatro = teatroFactory.crearEvento(
                "EVE-002",
                "Obra de Teatro",
                "Presentacion artistica en el auditorio principal.",
                "Armenia",
                LocalDateTime.now().plusDays(15),
                recintoPrincipal,
                25000
        );
        teatro.setPoliticaCancelacion("Cancelacion permitida hasta 12 horas antes del evento.");
        teatro.setPoliticaReembolso("Reembolso del 80% si se solicita dentro del plazo.");
        teatro.setReglasGenerales("No se permite el ingreso una vez iniciada la funcion.");

        Evento conferencia = conferenciaFactory.crearEvento(
                "EVE-003",
                "Conferencia de Tecnologia",
                "Charla academica sobre programacion y tecnologia.",
                "Armenia",
                LocalDateTime.now().plusDays(20),
                recintoPrincipal,
                20000
        );
        conferencia.setPoliticaCancelacion("Cancelacion permitida hasta 48 horas antes del evento.");
        conferencia.setPoliticaReembolso("Reembolso total por cancelacion oficial del organizador.");
        conferencia.setReglasGenerales("Presentar documento de identidad junto con la entrada.");

        concierto.cambiarEstado(EstadoEvento.PUBLICADO);
        teatro.cambiarEstado(EstadoEvento.PUBLICADO);
        conferencia.cambiarEstado(EstadoEvento.PUBLICADO);

        sistema.agregarEvento(concierto);
        sistema.agregarEvento(teatro);
        sistema.agregarEvento(conferencia);

        crearCompraDePrueba(sistema, concierto, zonaVIP);
    }

    private static void agregarAsientos(Zona zona, String fila, int cantidad) {
        for (int i = 1; i <= cantidad; i++) {
            Asiento asiento = new Asiento(
                    zona.getIdZona() + "-ASI-" + i,
                    fila,
                    i
            );

            zona.agregarAsiento(asiento);
        }
    }

    private static void crearCompraDePrueba(SistemaEventos sistema, Evento evento, Zona zona) {
        if (sistema.getUsuarios().isEmpty() || zona.getAsientos().isEmpty()) {
            return;
        }

        Usuario usuario = sistema.getUsuarios().get(0);
        Asiento asiento = zona.getAsientos().get(0);

        asiento.vender();

        Entrada entrada = new Entrada(
                "ENT-001",
                zona,
                asiento,
                zona.getPrecioBase()
        );

        entrada.activar();

        Compra compra = new Compra(
                "COM-001",
                usuario,
                evento
        );

        compra.agregarEntrada(entrada);
        compra.calcularTotal();

        sistema.agregarCompra(compra);
    }

    private static void cargarIncidencias(SistemaEventos sistema) {
        Incidencia incidencia = new Incidencia(
                "INC-001",
                TipoIncidencia.ERROR_PAGO,
                "Pago rechazado durante una compra de prueba."
        );

        sistema.agregarIncidencia(incidencia);
    }

    private static void cargarNotificacionesIniciales(SistemaEventos sistema) {
        if (!sistema.getUsuarios().isEmpty()) {
            Usuario usuario = sistema.getUsuarios().get(0);
            GestorNotificaciones.getInstancia().guardarNotificacion(
                    new Notificacion(
                            "NOT-001",
                            "Bienvenido",
                            "Tu cuenta fue creada correctamente.",
                            usuario.getIdUsuario(),
                            TipoNotificacion.SISTEMA
                    )
            );
        }

        if (!sistema.getAdministradores().isEmpty()) {
            GestorNotificaciones.getInstancia().guardarNotificacion(
                    new Notificacion(
                            "NOT-002",
                            "Sistema listo",
                            "La carga inicial de datos finalizo correctamente.",
                            sistema.getAdministradores().get(0).getIdAdministrador(),
                            TipoNotificacion.SISTEMA
                    )
            );
        }
    }
}
