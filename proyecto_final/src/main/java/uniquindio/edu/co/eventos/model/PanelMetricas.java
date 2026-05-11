package uniquindio.edu.co.eventos.model;

import uniquindio.edu.co.eventos.model.enums.EstadoCompra;

import java.util.ArrayList;

public class PanelMetricas {

    private int totalCompras;
    private double totalVentas;
    private int totalEntradasVendidas;
    private int totalIncidencias;

    public PanelMetricas() {
    }

    public void calcularMetricas(ArrayList<Compra> compras, ArrayList<Incidencia> incidencias) {
        totalCompras = 0;
        totalVentas = 0;
        totalEntradasVendidas = 0;
        totalIncidencias = 0;

        if (compras != null) {
            totalCompras = compras.size();

            for (Compra compra : compras) {
                if (compra.getEstadoCompra() == EstadoCompra.CONFIRMADA ||
                    compra.getEstadoCompra() == EstadoCompra.PAGADA) {

                    totalVentas += compra.getTotal();
                    totalEntradasVendidas += compra.getEntradas().size();
                }
            }
        }

        if (incidencias != null) {
            totalIncidencias = incidencias.size();
        }
    }

    public double calcularVentas(ArrayList<Compra> compras) {
        double ventas = 0;

        if (compras != null) {
            for (Compra compra : compras) {
                ventas += compra.getTotal();
            }
        }

        this.totalVentas = ventas;
        return ventas;
    }

    public int calcularEntradasVendidas(ArrayList<Compra> compras) {
        int cantidad = 0;

        if (compras != null) {
            for (Compra compra : compras) {
                cantidad += compra.getEntradas().size();
            }
        }

        this.totalEntradasVendidas = cantidad;
        return cantidad;
    }

    public String visualizarMetricas() {
        return "Métricas del sistema\n"
                + "Total compras: " + totalCompras + "\n"
                + "Total ventas: $" + totalVentas + "\n"
                + "Entradas vendidas: " + totalEntradasVendidas + "\n"
                + "Incidencias registradas: " + totalIncidencias;
    }

    public int getTotalCompras() {
        return totalCompras;
    }

    public void setTotalCompras(int totalCompras) {
        this.totalCompras = totalCompras;
    }

    public double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(double totalVentas) {
        this.totalVentas = totalVentas;
    }

    public int getTotalEntradasVendidas() {
        return totalEntradasVendidas;
    }

    public void setTotalEntradasVendidas(int totalEntradasVendidas) {
        this.totalEntradasVendidas = totalEntradasVendidas;
    }

    public int getTotalIncidencias() {
        return totalIncidencias;
    }

    public void setTotalIncidencias(int totalIncidencias) {
        this.totalIncidencias = totalIncidencias;
    }
}
