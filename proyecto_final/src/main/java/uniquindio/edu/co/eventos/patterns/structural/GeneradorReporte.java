package uniquindio.edu.co.eventos.patterns.structural;

import uniquindio.edu.co.eventos.model.Compra;

import java.util.ArrayList;

public interface GeneradorReporte {

    String generarReporte(ArrayList<Compra> compras);
}
