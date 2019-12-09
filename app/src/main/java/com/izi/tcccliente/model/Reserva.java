package com.izi.tcccliente.model;

import java.io.Serializable;


public class Reserva implements Serializable {

    private String date;
    private String timeInicio;
    private String timeFinal;
    private String latitude;
    private String longitude;

    private static Reserva reserva;

    public Reserva(){


    }


    public static Reserva getReserva() {
        return reserva;
    }

    public static void setReserva(Reserva reserva) {
        Reserva.reserva = reserva;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeInicio() {
        return timeInicio;
    }

    public void setTimeInicio(String timeInicio) {
        this.timeInicio = timeInicio;
    }

    public String getTimeFinal() {
        return timeFinal;
    }

    public void setTimeFinal(String timeFinal) {
        this.timeFinal = timeFinal;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
