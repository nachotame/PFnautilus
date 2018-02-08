package com.example.nacho.pfnautilus;

/**
 * Created by Nacho on 08/02/2018.
 */

public class weather {
    double temp;
    double humidity;
    double tempMax;
    double tempMin;
    double nubosidad;
    long salidaSol;
    long puestaSol;

    public weather(double temp,double humidity,double tempMax,double tempMin,double nubosidad,
                   int salidaSol,int puestaSol){

        this.temp=temp;
        this.humidity=humidity;
        this.tempMax=tempMax;
        this.tempMin=tempMin;
        this.nubosidad=nubosidad;
        this.salidaSol=salidaSol;
        this.puestaSol=puestaSol;

    }

    public double getTemp() {
        return temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTempMax() {
        return tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getNubosidad() {
        return nubosidad;
    }

    public long getSalidaSol() {
        return salidaSol;
    }

    public long getPuestaSol() {
        return puestaSol;
    }

    long horasSol;

    public void tiempoSol() {
        horasSol=(puestaSol-salidaSol)/3600000;

    }
    public void  alertas(){
        String lluvia="Va a llover muy fuerte".toString();
        String sol="disfrute de la naturaleza".toString();
        String abrigase="Coja una chaqueta, o dos".toString();

    }

}
