package com.example.android.voltageapp.entidades;

public class HeartRate {

    private String dateTime;
    private Integer id;
    private String hora;
    private String fecha;
    private String trama;
    private String potencia;
    private String voltaje;

    public String getVoltaje() {
        return voltaje;
    }

    public void setVoltaje(String voltaje) {
        this.voltaje = voltaje;
    }

    public HeartRate(Integer id, String fecha, String hora, String trama, String dateTime, String potencia, String voltaje) {
        this.id = id;
        this.fecha = fecha;
        this.trama = trama;
        this.hora = hora;
        this.dateTime = dateTime;
        this.potencia = potencia;
        this.voltaje = voltaje;
    }

    public HeartRate() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTrama() {
        return trama;
    }

    public void setTrama(String trama) {
        this.trama = trama;
    }

    public String getHora() {
        return this.hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPotencia() {
        return potencia;
    }

    public void setPotencia(String potencia) {
        this.potencia = potencia;
    }
}
