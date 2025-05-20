package com.gruppo07iz.geometrika;

import com.gruppo07iz.geometrika.classi.Factory;
import com.gruppo07iz.geometrika.classi.TipoForma;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;


public class Model {
    private Factory factory;

    public Model() {
        this.factory = new Factory();
    }
    
    /**
     * Metodo per la creazione della (@code Shape)
     * @param tipo
     * @param x
     * @param y
     * @param bordo
     * @param riemp
     * @return La forma (@code Shape) con le caratteristiche desiderate nella posizione specificata
     */
    public Shape creaForma(TipoForma tipo,  double x, double y, Color bordo, Color riemp){
        Shape forma = factory.creaForma(tipo, x, y, bordo, riemp);
        return forma;
    }
    
}
