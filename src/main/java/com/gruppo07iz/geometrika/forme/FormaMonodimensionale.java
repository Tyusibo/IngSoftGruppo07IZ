package com.gruppo07iz.geometrika.forme;

/**
 * Questa interfaccia serve per dichiarare firme di metodi comuni a tutte le forme a una dimensione.
 */
public interface FormaMonodimensionale extends FormaPersonalizzabile{ 
    /**
    * Questa metodo va implementato permettendo di modificare l'unica dimensione
    * della specifica forma monodimensionale
    */
    public abstract void modificaDimensione(double dim);
    /**
    * Questa metodo va implementato permettendo di restituire l'unica dimensione
    * della specifica forma monodimensionale
    */
    public String ottieniNomeDimensione();
    /**
    * Questa metodo va implementato permettendo di restituire il nominativo dell'unica dimensione
    * della specifica forma monodimensionale
    */
    public double ottieniValoreDimensione();
}

