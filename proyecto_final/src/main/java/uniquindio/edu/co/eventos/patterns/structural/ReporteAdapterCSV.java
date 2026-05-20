package uniquindio.edu.co.eventos.patterns.structural;

import uniquindio.edu.co.eventos.model.Compra;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReporteAdapterCSV {

    private final GeneradorReporteCSV generadorReporteCSV;

    public ReporteAdapterCSV() {
        this.generadorReporteCSV = new GeneradorReporteCSV();
    }

    public String exportar(ArrayList<Compra> compras) {
        return generadorReporteCSV.generarReporte(compras);
    }

    public void exportarArchivo(ArrayList<Compra> compras, File archivo) throws IOException {
        String contenido = generadorReporteCSV.generarReporte(compras);
        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write(contenido);
        }
    }
}
