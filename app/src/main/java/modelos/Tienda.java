package modelos;

import java.io.Serializable;

public class Tienda implements Serializable {
    //declaracion de propiedades de la clase
    private int id;
    private String nombre;
    private String direccion;
    private double latitud;
    private double longitud;
    private String descripcion;
//cuando no se saben los dato
    public Tienda() {
    }
//para cuando se requiera hacer consultas
    public Tienda(int id, String nombre, String direccion, double latitud, double longitud, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.descripcion = descripcion;
    }
//consultar nombre y descripcion
    public Tienda(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
