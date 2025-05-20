package com.gruppo07iz.geometrika.interfacce;

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
    * Questa metodo va implementato permettendo di gestire la logica di stampa delle informazioni 
    * in accordo a come è strutturata la specifica forma personalizzata
    */
    public String toText();
    
    /**
    * Questa metodo va implementato permettendo di gestire la logica di creazione del prototipi 
    * in accordo a come è strutturata la specifica forma personalizzata
    */
    public Shape clonaForma();

}



    