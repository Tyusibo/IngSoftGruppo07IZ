package com.gruppo07iz.geometrika.forme;

/**
 * Enum che rappresenta i tipi di forme geometriche regolari supportate dall'applicazione.
 * Definisce le forme base che possono essere create e manipolate nel sistema.
 */
public enum TipoFormaRegolare {
    /**
     * Rappresenta una linea retta tra due punti
     */
    LINEA, 
    
    /**
     * Rappresenta un rettangolo, definito da posizione, larghezza e altezza
     */
    RETTANGOLO, 
    
    /**
     * Rappresenta un'ellisse (o cerchio, nel caso di diametri uguali),
     * definita da posizione, raggioX e raggioY
     */
    ELLISSE
}