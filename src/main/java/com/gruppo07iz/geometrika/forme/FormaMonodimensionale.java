package com.gruppo07iz.geometrika.forme;

/**
 * Interfaccia che definisce i metodi specifici per le forme geometriche monodimensionali.
 * Estende l'interfaccia FormaPersonalizzabile aggiungendo funzionalità per gestire
 * l'unica dimensione caratteristica di queste forme.
 */
public interface FormaMonodimensionale extends FormaPersonalizzabile { 
    
    /**
     * Modifica l'unica dimensione caratteristica della forma monodimensionale.
     * 
     * @param dim Il nuovo valore della dimensione 
     */
    void modificaDimensione(double dim);

    /**
     * Restituisce il valore corrente dell'unica dimensione caratteristica della forma.
     * 
     * @return Il valore corrente della dimensione
     */
    double ottieniValoreDimensione();
}
