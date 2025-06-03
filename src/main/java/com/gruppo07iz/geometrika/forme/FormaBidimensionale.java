package com.gruppo07iz.geometrika.forme;

/**
 * Interfaccia che definisce i metodi specifici per le forme geometriche bidimensionali.
 * Estende l'interfaccia FormaPersonalizzabile aggiungendo funzionalità per gestire
 * le due dimensioni caratteristiche di queste forme.
 */
public interface FormaBidimensionale extends FormaPersonalizzabile {
    
    /**
     * Modifica le dimensioni caratteristiche della forma bidimensionale.
     * 
     * @param dim1 La nuova valore per la prima dimensione 
     * @param dim2 La nuova valore per la seconda dimensione 
     */
    void modificaDimensioni(double dim1, double dim2); 

    /**
     * Restituisce i valori correnti delle dimensioni caratteristiche della forma.
     * 
     * @return Un array di double contenente i valori delle dimensioni,
     *         dove il primo elemento corrisponde alla prima dimensione
     *         e il secondo elemento alla seconda dimensione
     */
    double[] ottieniValoriDimensioni();
}