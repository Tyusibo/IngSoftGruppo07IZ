package com.gruppo07iz.geometrika.forme;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;

/**
 * Classe Rettangolo Personalizzato, estende Rectangle e implementa le funzionalità aggiuntive
 * delle interfacce FormaBidimensionale e FormaPersonalizzata
 * La classe non può essere estesa
 */
public final class Rettangolo extends Rectangle implements FormaBidimensionale{
    
    /**
    * Il costruttore vuole come parametri le coordinate del punto per il vertice in alto a sinistra
    * e i colori per il riempimento e per il bordo
    * Il rettangolo viene creato con dimensioni di altezza e larghezza di default
    * @param coordinataX
    * @param coordinataY
    * @param coloreBordo
    * @param coloreRiempiemento 
    */
    public Rettangolo(double coordinataX, double coordinataY, Color coloreBordo, Color coloreRiempiemento){
        super(coordinataX, coordinataY, 100, 60);
        this.setStroke(coloreBordo);
        this.setFill(coloreRiempiemento);
        this.setStrokeWidth(3); 
    }
    
    /* INIZIO METODI FORMABIDIMENSIONALE */
    
    /**
    * Implementazione del metodo per la modifica delle dimensioni
    * Il primo parametro viene usato per l'altezza e il secondo per la larghezza
    * @param dim1
    * @param dim2
    */
    @Override
    public void modificaDimensioni(double dim1, double dim2) {
        this.setHeight(dim1);
        this.setWidth(dim2);
    }
    
    /**
    * Implementazione del metodo per la restituzione dei nomi delle dimensioni
    * Vengono fornite in ordine: l'altezza e poi la larghezza
    * @return un array di stringhe contenente i nomi delle dimensioni: "Altezza" e "Larghezza"
    */
    @Override
    public String[] ottieniNomiDimensioni() {
        return new String[] { "Altezza", "Larghezza" };
    }

    /**
    * Implementazione del metodo per la restituzione dei valori delle dimensioni
    * Vengono fornite in ordine: altezza e poi larghezza
    * @return un array di double contenente i valori delle due dimensioni
    */
    @Override
    public double[] ottieniValoriDimensioni() {
        return new double[] { this.getHeight(), this.getWidth() };
    }
    /* FINE METODI FORMABIDIMENSIONALE */
    
    /**
    * Implementazione della logica di tracinamento per il Rettangolo
    * @param dx
    * @param dy
    */
    @Override
    public void trascina(double dx, double dy) {
        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);
    }

    /**
     * Imposta le coordinate superiori sinistre del rettangolo.
     *
     * @param coordinataX la nuova coordinata X (ascissa) del rettangolo
     * @param coordinataY la nuova coordinata Y (ordinata) del rettangolo
     */
    @Override
    public void cambiaCoordinate(double coordinataX, double coordinataY) {
        this.setX(coordinataX);
        this.setY(coordinataY);
    }

    /**
     * Restituisce le coordinate dell'angolo superiore sinistro del rettangolo.
     *
     * @return un array double[] contenente [x, y]
     */
    @Override
    public double[] ottieniCoordinate() {
        return new double[] { this.getX(), this.getY() };
    }
    
    /**
     * Restituisce una rappresentazione testuale del rettangolo con le seguenti informazioni:
     * - tipo di forma ("RETTANGOLO")
     * - coordinate (X, Y)
     * - colore del bordo (stroke)
     * - colore di riempimento (fill)
     * - larghezza (width)
     * - altezza (height)
     * - angolo di rotazione (rotate)
     * La stringa termina con un carattere di nuova linea.
     *
     * @return una stringa descrittiva del rettangolo
     */
    @Override
    public String toText() {
        return "RETTANGOLO" + " " + this.getX() + " " + this.getY() + " " +
                this.getStroke() + " " + this.getFill() + " " +
                this.getWidth() + " " + this.getHeight()
                 + " " + this.getRotate() +"\n";
    }
    
    /**
    * Il metodo restituisce un oggetto identico al Rettangolo su cui è stato chiamato
    */
    @Override
    public Shape clonaForma() {
        Rettangolo copia = new Rettangolo(this.getX(), this.getY(), 
                                      (Color) this.getStroke(), (Color) this.getFill());
        copia.modificaDimensioni(this.getHeight(), this.getWidth());
        copia.setRotate(this.getRotate());
        return copia;
    }

    @Override
    public double[] calcolaCentro(){
        double centroX = this.getX() + this.getWidth() / 2;
        double centroY = this.getY() + this.getHeight() / 2;
        System.out.println(centroX);
        System.out.println(centroY);
        return new double[] { centroX, centroY };

    }
}

