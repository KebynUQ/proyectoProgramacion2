package uniquindio.edu.co.eventos.patterns.structural;

import uniquindio.edu.co.eventos.model.Compra;

import java.util.ArrayList;

public class ReporteAdapterPDF {

    private GeneradorReportePDF generadorReportePDF;

    public ReporteAdapterPDF() {
        this.generadorReportePDF = new GeneradorReportePDF();
    }

    public String exportar(ArrayList<Compra> compras) {
        return generadorReportePDF.generarReporte(compras);
    }
}
