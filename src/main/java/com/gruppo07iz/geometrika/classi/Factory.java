package com.gruppo07iz.geometrika.classi;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Factory {
    /**
    * Crea e restituisce un'istanza di {@link Shape} in base al tipo specificato.
    * I parametri {@code x} e {@code y} rappresentano le dimensioni della forma, 
    * mentre {@code bordo} e {@code riemp} rappresentano rispettivamente il colore del bordo e del riempimento.
    * Per le forme che non prevedono riempimento (come {@code Linea}), il parametro {@code riemp} può essere ignorato.
    *
    * @param tipo il tipo di forma da creare (ELLISSE, RETTANGOLO, SEGMENTO)
    * @param x la dimensione lungo l'asse X o lunghezza della forma
    * @param y la dimensione lungo l'asse Y o altezza della forma
    * @param bordo il colore del bordo della forma
    * @param riemp il colore del riempimento della forma (se applicabile)
    * @return un oggetto {@code Shape} corrispondente al tipo specificato
    * @throws IllegalArgumentException se il tipo di forma non è supportato
    */
    public Shape creaForma(TipoForma tipo, double x, double y, Color bordo, Color riemp) {
        Shape forma;
        switch (tipo) {
            case ELLISSE:
                forma = new Ellisse(x, y, bordo, riemp);
                break;
            case RETTANGOLO:
                forma = new Rettangolo(x, y, bordo, riemp);
                break;
            case SEGMENTO:
                forma = new Linea(x, y, bordo);
                break;
                
            default:
                throw new IllegalArgumentException("Tipo di forma non supportato: " + tipo);
        }
        
        return forma;
        
    }
}