package uniquindio.edu.co.eventos.patterns.structural;

import uniquindio.edu.co.eventos.model.Compra;

import java.util.ArrayList;

public class ReporteAdapterCSV {

    private GeneradorReporteCSV generadorReporteCSV;

    public ReporteAdapterCSV() {
        this.generadorReporteCSV = new GeneradorReporteCSV();
    }

    public String exportar(ArrayList<Compra> compras) {
        return generadorReporteCSV.generarReporte(compras);
    }
}
