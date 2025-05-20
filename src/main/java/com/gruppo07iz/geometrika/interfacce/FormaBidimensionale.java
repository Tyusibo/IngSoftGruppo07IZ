package com.gruppo07iz.geometrika.interfacce;
/**
 * Questa interfaccia serve per dichiarare firme di metodi comuni a tutte le forme a 2 dimensioni.
 */
public interface FormaBidimensionale extends FormaPersonalizzabile{
    /**
    * Questa metodo va implementato permettendo di modificare le singole dimensioni
    * in accordo a come è strutturata la specifica forma bidimensionale
    */
    public void modificaDimensioni(double dim1, double dim2); 
    /**
    * Questa metodo va implementato permettendo di restituire le singole dimensioni
    * in accordo a come è strutturata la specifica forma bidimensionale
    */
    public String[] ottieniNomiDimensioni();
    /**
    * Questa metodo va implementato permettendo di restituire i nominativi delle singole dimensioni
    * in accordo a come è strutturata la specifica forma bidimensionale
    */
    public double[] ottieniValoriDimensioni();
}
