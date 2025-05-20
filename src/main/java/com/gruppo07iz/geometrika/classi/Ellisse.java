package com.gruppo07iz.geometrika.classi;

import com.gruppo07iz.geometrika.interfacce.FormaBidimensionale;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

/**
 * Classe Ellisse Personalizzato, estende Ellipse e implementa le funzionalità aggiuntive
 * delle interfacce FormaBidimensionale e FormaPersonalizzata
 * La classe non può essere estesa
 */
public final class Ellisse extends Ellipse implements FormaBidimensionale{
    
    /**
    * Il costruttore vuole come parametri le coordinate del punto
    * e i colori per il riempimento e per il bordo
    * L'ellisse viene creato con valori dei due raggi di default
    * @param coordinataX
    * @param coordinataY
    * @param coloreBordo
    * @param coloreRiempimento
    */
    public Ellisse(double coordinataX, double coordinataY, Color coloreBordo, Color coloreRiempiemento){
        super(coordinataX, coordinataY, 70, 30);
        this.setStroke(coloreBordo);
        this.setFill(coloreRiempiemento);
        this.setStrokeWidth(3); 
    }
    
    /* INIZIO METODI FORMABIDIMENSIONALE */

    /**
    * Implementazione del metodo per la modifica delle dimensioni
    * Il primo parametro viene usato per il raggio X e il secondo per il raggio Y
    * @param dim1
    * @param dim2
    */
    @Override
    public void modificaDimensioni(double dim1, double dim2) {
        this.setRadiusX(dim1);
        this.setRadiusY(dim2);
    }

    /**
    * Implementazione del metodo per la restituzione dei nomi delle dimensioni
    * Vengono fornite in ordine: raggio X e poi raggio Y
    * @return un array di stringhe contenente i nomi delle dimensioni: "RaggioX" e "RaggioY"
    */
    @Override
    public String[] ottieniNomiDimensioni() {
        return new String[] { "RaggioX", "RaggioY" };
    }

    /**
    * Implementazione del metodo per la restituzione dei valori delle dimensioni
    * Vengono fornite in ordine: raggio X e poi raggio Y
    * @return un array di double contenente i valori delle due dimensioni
    */
    @Override
    public double[] ottieniValoriDimensioni() {
        return new double[] { this.getRadiusX(), this.getRadiusY() };
    }
    
    /* FINE METODI FORMABIDIMENSIONALE */
    
    /**
    * Implementazione della logica di tracinamento per l'Ellisse
    * @param dx
    * @param dy
    */
    @Override
    public void trascina(double dx, double dy) {
        this.setCenterX(this.getCenterX() + dx);
        this.setCenterY(this.getCenterY() + dy);
    }
    
    @Override
    public String toText() {
        return "ELLISSE" + " " + this.getCenterX() + " " + this.getCenterY() + " " +
                this.getStroke() + " " + this.getFill() + " " + 
                this.getRadiusX() + " " + this.getRadiusY() + "\n";
    }
    
    /**
    * Il metodo restituisce un oggetto identico all'Ellisse su cui è stato chiamato
    */
    @Override
    public Shape clonaForma() {
        Ellisse copia = new Ellisse(this.getCenterX(), this.getCenterY(), 
                                (Color) this.getStroke(), (Color) this.getFill());
        copia.modificaDimensioni(this.getRadiusX(), this.getRadiusY());
        return copia;
    }

}
