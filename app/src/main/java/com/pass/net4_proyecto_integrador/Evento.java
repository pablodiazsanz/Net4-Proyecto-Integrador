package com.pass.net4_proyecto_integrador;

public class Evento {

    private String titulo, descripcion, userId, fecha;
    private double latitud, longitud;
    private int gradoUrgencia;

    public Evento(){}

    public Evento(String userId, String titulo, String descripcion, int gradoUrgencia, String fecha, double latitud, double longitud) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.gradoUrgencia = gradoUrgencia;
        this.userId = userId;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getUserId() {
        return userId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getGradoUrgencia() {
        return gradoUrgencia;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", gradoUrgencia='" + gradoUrgencia + '\'' +
                ", userId='" + userId + '\'' +
                ", fecha='" + fecha + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}
