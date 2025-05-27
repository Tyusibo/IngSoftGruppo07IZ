package com.gruppo07iz.geometrika.forme;

import javafx.scene.shape.Shape;

/**
 * Questa interfaccia serve per dichiarare firme di metodi comuni
 * che si vogliono far implementare a tutte le forme per renderle personalizzabili.
 */
public interface FormaPersonalizzabile{
    /**
    * Questa metodo va implementato permettendo di gestire la logica di trascinamento 
    * in accordo a come è strutturata la specifica forma personalizzata
    */
    public void trascina(double dx, double dy);
    
    /**
    * Questa metodo va implementato permettendo di gestire la logica di modifica della posizione 
    * in accordo a come è strutturata la specifica forma personalizzata
    */
    public void cambiaCoordinate(double coordinataX, double coordinataY);
    
    /**
    * Questa metodo va implementato permettendo di gestire la logica di ottenimento della posizione 
    * in accordo a come è strutturata la specifica forma personalizzata
    */
    public double[] ottieniCoordinate();


    
    /**
    * Questa metodo va implementato permettendo di gestire la logica di stampa delle informazioni 
    * in accordo a come è strutturata la specifica forma personalizzata
    */
    public String toText();
    
    /**
    * Questa metodo va implementato permettendo di gestire la logica di creazione del prototipi 
    * in accordo a come è strutturata la specifica forma personalizzata
    */
    public Shape clonaForma();

    /**
     * Questo metodo calcola il centro della forma personalizzata.
     * La logica di calcolo del centro dipende dalla forma specifica.
     * @return
     */
    public double[] calcolaCentro();

}



    