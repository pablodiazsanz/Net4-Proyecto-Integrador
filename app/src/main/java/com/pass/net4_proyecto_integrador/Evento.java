package com.pass.net4_proyecto_integrador;

public class Evento {

    private String titulo, descripcion, gradoUrgencia, userId, fecha;

    public Evento(){}

    public Evento(String userId, String titulo, String descripcion, String gradoUrgencia, String fecha) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.gradoUrgencia = gradoUrgencia;
        this.userId = userId;
        this.fecha = fecha;
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

    public String getGradoUrgencia() {
        return gradoUrgencia;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "titulo='" + titulo + '\'' +
                "userId='" + userId + '\'' +
                "fecha='" + fecha + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", gradoUrgencia='" + gradoUrgencia + '\'' +
                '}';
    }
}
