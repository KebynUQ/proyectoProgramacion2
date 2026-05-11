package uniquindio.edu.co.eventos.patterns.structural;

import uniquindio.edu.co.eventos.model.Asiento;
import uniquindio.edu.co.eventos.model.Compra;
import uniquindio.edu.co.eventos.model.Entrada;
import uniquindio.edu.co.eventos.model.Evento;
import uniquindio.edu.co.eventos.model.Incidencia;
import uniquindio.edu.co.eventos.model.Pago;
import uniquindio.edu.co.eventos.model.SistemaEventos;
import uniquindio.edu.co.eventos.model.Usuario;
import uniquindio.edu.co.eventos.model.Zona;
import uniquindio.edu.co.eventos.model.enums.EstadoAsiento;
import uniquindio.edu.co.eventos.model.enums.TipoIncidencia;
import uniquindio.edu.co.eventos.patterns.behavioral.EstrategiaPago;
import uniquindio.edu.co.eventos.patterns.creational.CompraBuilder;

public class CompraFacade {

    private SistemaEventos sistemaEventos;

    public CompraFacade() {
        this.sistemaEventos = SistemaEventos.getInstancia();
    }

    public Compra realizarCompra(
            Usuario usuario,
            Evento evento,
            Zona zona,
            Asiento asiento,
            EstrategiaPago estrategiaPago
    ) {
        if (usuario == null || evento == null || zona == null || asiento == null) {
            registrarIncidencia("Datos incompletos para realizar la compra");
            return null;
        }

        if (!asiento.estaDisponible()) {
            registrarIncidencia("El asiento seleccionado no está disponible");
            return null;
        }

        asiento.reservar();

        Entrada entrada = new Entrada(
                "ENT-" + System.currentTimeMillis(),
                zona,
                asiento,
                zona.getPrecioBase()
        );

        Compra compra = new CompraBuilder()
                .conId("COM-" + System.currentTimeMillis())
                .conUsuario(usuario)
                .conEvento(evento)
                .agregarEntrada(entrada)
                .build();

        Pago pago = new Pago(
                "PAG-" + System.currentTimeMillis(),
                compra.getTotal(),
                "Pago simulado",
                estrategiaPago
        );

        boolean pagoAprobado = pago.procesarPago();

        if (pagoAprobado) {
            compra.pagar(pago);
            compra.confirmarPago();
            asiento.vender();
            entrada.activar();

            sistemaEventos.agregarCompra(compra);
            sistemaEventos.agregarPago(pago);

            return compra;
        }

        asiento.liberar();
        compra.cancelar();
        registrarIncidencia("El pago fue rechazado");

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
}
