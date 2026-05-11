package uniquindio.edu.co.eventos.model;

import java.util.ArrayList;

public class Recinto {

    private String idRecinto;
    private String nombre;
    private String direccion;
    private String ciudad;
    private ArrayList<Zona> zonas;

    public Recinto() {
        this.zonas = new ArrayList<>();
    }

    public Recinto(String idRecinto, String nombre, String direccion, String ciudad) {
        this.idRecinto = idRecinto;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.zonas = new ArrayList<>();
    }

    public void agregarZona(Zona zona) {
        if (zona != null) {
            zonas.add(zona);
        }
    }

    public void eliminarZona(Zona zona) {
        zonas.remove(zona);
    }

    public ArrayList<Zona> consultarZonas() {
        return zonas;
    }

    public String getIdRecinto() {
        return idRecinto;
    }

    public void setIdRecinto(String idRecinto) {
        this.idRecinto = idRecinto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public ArrayList<Zona> getZonas() {
        return zonas;
    }

    public void setZonas(ArrayList<Zona> zonas) {
        this.zonas = zonas;
    }

    @Override
    public String toString() {
        return nombre + " - " + ciudad;
    }
}
