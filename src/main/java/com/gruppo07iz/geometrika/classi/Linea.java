package com.gruppo07iz.geometrika.classi;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import com.gruppo07iz.geometrika.interfacce.FormaMonodimensionale;
import javafx.scene.shape.Shape;

/**
 * Classe Linea Personalizzata, estende Line e implementa le funzionalità aggiuntive
 * delle interfacce FormaMonodimensionale e FormaPersonalizzata
 * La classe non può essere estesa
 */
public final class Linea extends Line implements FormaMonodimensionale{
    
    /**
    * Il costruttore vuole come parametri le coordinate del punto di inizio
    * e il colore per il bordo
    * La linea viene creata orizzontale e con una lunghezza di default
    * @param coordinataX
    * @param coordinataY
    * @param coloreBordo 
    */
    public Linea(double coordinataX, double coordinataY, Color coloreBordo){
        super(
            coordinataX,
            coordinataY,
            coordinataX + 50,
            coordinataY 
        );
        this.setStroke(coloreBordo);
        this.setFill(coloreBordo);
        this.setStrokeWidth(4); 
    }
    
    /**
    * Metodo di utilità per il calcolo della lunghezza della linea
    * @return distanza euclidea tra due punti
    */
    public double calcolaLunghezza(){
        double dx = this.getEndX() - this.getStartX();
        double dy = this.getEndY() - this.getStartY();

        return Math.sqrt(dx * dx + dy * dy);    
    }
    
    /* INIZIO METODI FORMAMONODIMENSIONALE */

    /**
    * Implementazione del metodo per la modifica delle dimensione
    * @param dim usato per la lunghezza
    */
    @Override
    public void modificaDimensione(double dim) {
        
        if (dim < 0) {
            throw new IllegalArgumentException("La dimensione negativa non è ammessa");
        }
        
        double lunghezzaAttuale = this.calcolaLunghezza();
        
        if (lunghezzaAttuale == 0) {
            // Caso linea di lunghezza zero: creo una linea orizzontale di lunghezza dim a destra del punto start
            this.setEndX(this.getStartX() + dim);
            this.setEndY(this.getStartY());
            return;
        }

        // Calcolo del rapporto di scala
        double scala = dim / lunghezzaAttuale;

        // Nuove coordinate finali mantenendo la direzione
        double nuovoEndX = this.getStartX() + (this.getEndX() - this.getStartX()) * scala;
        double nuovoEndY = this.getStartY() + (this.getEndY() - this.getStartY()) * scala;

        this.setEndX(nuovoEndX);
        this.setEndY(nuovoEndY);
    }

    /**
    * Implementazione del metodo per la restituzione del nome della dimensione, ovvero la lunghezza
    */
    @Override
    public String ottieniNomeDimensione() {
        return "Lunghezza";
    }

    /**
    * Implementazione del metodo per la restituzione del valore della dimensione, ovvero la lunghezza
    */
    @Override
    public double ottieniValoreDimensione() {
        return this.calcolaLunghezza();
    }
    
    /* FINE METODI FORMAMONODIMENSIONALE */

    /**
    * Implementazione della logica di tracinamento per la Linea
    * @param dx
    * @param dy
    */
    @Override
    public void trascina(double dx, double dy) {
        this.setStartX(this.getStartX() + dx);
        this.setStartY(this.getStartY() + dy);
        this.setEndX(this.getEndX() + dx);
        this.setEndY(this.getEndY() + dy);
    }
    
    
    
    @Override
    public String toText() {
        return "SEGMENTO" + " " + this.getStartX() + " " + this.getStartY() + " " +
                this.getStroke() + " " + this.getFill() + " " + this.calcolaLunghezza() + "\n";
    }
    
    /**
    * Il metodo restituisce un oggetto identico alla Linea su cui è stato chiamato
    */
    @Override
    public Shape clonaForma() {
    Linea copia = new Linea(this.getStartX(), this.getStartY(), (Color) this.getStroke());

    copia.setEndX(this.getEndX());
    copia.setEndY(this.getEndY());
    copia.setStrokeWidth(this.getStrokeWidth());
    
    copia.modificaDimensione(this.calcolaLunghezza());
    copia.setRotate(this.getRotate());

    return copia;
    }
}
