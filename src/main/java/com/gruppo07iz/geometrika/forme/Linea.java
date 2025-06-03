package com.gruppo07iz.geometrika.forme;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Classe Linea Personalizzata, estende Line e implementa le funzionalità aggiuntive
 * delle interfacce FormaMonodimensionale e FormaPersonalizzata.
 * La classe non può essere estesa.
 */
public final class Linea extends Line implements FormaMonodimensionale {
    /**
     * Costruttore che crea una linea orizzontale con punto iniziale nelle coordinate specificate.
     * La linea viene creata con una lunghezza di default di 200 pixel.
     * 
     * @param coordinataX Coordinata X del punto iniziale
     * @param coordinataY Coordinata Y del punto iniziale
     * @param coloreBordo Colore della linea
     */
    public Linea(double coordinataX, double coordinataY, Color coloreBordo) {
        super(coordinataX, coordinataY, coordinataX + 200, coordinataY);
        this.setStroke(coloreBordo);
        this.setFill(coloreBordo);
        this.setStrokeWidth(4);
    }

    /* INIZIO OVERRIDE DI FORMA PERSONALIZZABILE */
     /**
     * Aggiunge questa linea al pannello specificato.
     * @param lavagna Il pannello (Pane) su cui disegnare la linea.
     */
    @Override
    public void disegna(Pane lavagna) {
        lavagna.getChildren().add(this);
    }
    /**
     * Aggiunge questa linea al pannello specificato in una posizione precisa.
     * @param lavagna Il pannello (Pane) su cui disegnare la linea.
     * @param posizione La posizione nell'ordine dei figli in cui inserire la linea.
     */
    @Override
    public void disegna(Pane lavagna, int posizione) {
        lavagna.getChildren().add(posizione, this);
    }
     /**
     * Imposta il colore di riempimento della linea.
     * Poiché la linea non ha riempimento, viene usato il colore di riempimento per la traccia.
     * @param coloreBordo Colore di riempimento da impostare (usato come colore traccia).
     */
    @Override
    public void setColoreRiempimento(Color coloreBordo) {
        this.setFill(coloreBordo);
    }
    /**
     * Restituisce il colore di riempimento della linea.
     * @return Il colore di riempimento (in realtà il colore di riempimento della linea).
     */
    @Override
    public Color getColoreRiempimento() {
        return (Color) this.getFill();
    }
    /**
     * Imposta il colore del bordo (traccia) della linea.
     * @param coloreRiempimento Colore da impostare come bordo/traccia.
     */
    @Override
    public void setColoreBordo(Color coloreRiempimento) {
        this.setStroke(coloreRiempimento);
    }
    /**
     * Restituisce il colore del bordo (traccia) della linea.
     * @return Il colore della traccia.
     */
    @Override
    public Color getColoreBordo() {
        return (Color) this.getStroke();
    }
    /**
     * Rimuove questa linea dal pannello specificato.
     * @param lavagna Il pannello (Pane) da cui rimuovere la linea.
     */
    @Override
    public void eliminaForma(Pane lavagna) {
        lavagna.getChildren().remove(this);
    }
    /**
     * Sposta la linea di uno scostamento dato sulle coordinate X e Y.
     * @param dx Incremento sulla coordinata X.
     * @param dy Incremento sulla coordinata Y.
     */
    @Override
    public void trascina(double dx, double dy) {        
        this.setStartX(this.getStartX() + dx);
        this.setStartY(this.getStartY() + dy);
        this.setEndX(this.getEndX() + dx);
        this.setEndY(this.getEndY() + dy);
    }
    /**
     * Porta la linea in primo piano rispetto ad altre forme nel pannello.
     */
    @Override
    public void spostaDavanti() {
        this.toFront();
    }
    /**
     * Porta la linea sullo sfondo rispetto ad altre forme nel pannello.
     */
    @Override
    public void spostaIndietro() {
        this.toBack();
    }
    /**
     * Ruota la linea aggiungendo un angolo specificato.
     * @param angolo Angolo in gradi da aggiungere alla rotazione corrente.
     */
    @Override
    public void setAngolo(double angolo) {
        this.setRotate(this.getRotate() + angolo);
    }
    /**
     * Restituisce l'angolo di rotazione attuale della linea.
     * @return Angolo di rotazione in gradi.
     */
    @Override
    public double getAngolo() {
        return this.getRotate();
    }

    @Override
    public void setSpecchiaturaOrizzontale() {}

    @Override
    public void setSpecchiaturaVerticale() {}
    /** 
     * Calcola il punto centrale della linea come media delle coordinate degli estremi.
     * @return Array di due elementi con coordinate [centroX, centroY].
     */
    @Override
    public double[] calcolaCentro() {
        double centroX = (this.getStartX() + this.getEndX()) / 2;
        double centroY = (this.getStartY() + this.getEndY()) / 2;
        return new double[] { centroX, centroY };
    }
    /**
     * Ottiene le coordinate di partenza della linea.
     * @return Array di due elementi con coordinate [startX, startY].
     */
    @Override
    public double[] ottieniCoordinate() {
        return new double[] { this.getStartX(), this.getStartY() };
    }
    /**
     * Rappresenta la linea come stringa di testo con informazioni su coordinate,
     * colori, dimensione e rotazione.
     * @return Stringa testuale che descrive la linea.
     */
    @Override
    public String toText() {
        double dimensione = ottieniValoreDimensione();
        return "LINEA" + " " + this.getStartX() + " " + this.getStartY() + " " +
               this.getStroke() + " " + this.getFill() + " " + dimensione 
               + " " + this.getRotate() + "\n";
    }
    /**
     * Crea una copia profonda di questa linea, replicandone proprietà e stato.
     * @return Nuova istanza di Linea con le stesse caratteristiche.
     */
    @Override
    public FormaPersonalizzabile clonaForma() {
        Linea copia = new Linea(this.getStartX(), this.getStartY(), (Color) this.getStroke());
        copia.setEndX(this.getEndX());
        copia.setEndY(this.getEndY());
        
        //Angolo
        copia.setRotate(this.getRotate());
        
        //Dimensione
        copia.modificaDimensione(this.ottieniValoreDimensione());
        
        return copia;
    }
    /**
     * Cambia le coordinate di partenza della linea e sposta proporzionalmente il punto finale.
     * @param coordinataX Nuova coordinata X per l'inizio della linea.
     * @param coordinataY Nuova coordinata Y per l'inizio della linea.
     */
    @Override
    public void cambiaCoordinate(double coordinataX, double coordinataY) {
        double deltaX = coordinataX - this.getStartX();
        double deltaY = coordinataY - this.getStartY();

        this.setStartX(coordinataX);
        this.setStartY(coordinataY);
        this.setEndX(this.getEndX() + deltaX);
        this.setEndY(this.getEndY() + deltaY);
    }
   /**
     * Applica un effetto grafico (ad esempio ombra) alla linea.
     * @param effetto Effetto DropShadow da applicare.
     */
    @Override
    public void applicaEffetto(DropShadow effetto) {
        this.setEffect(effetto);
    }
    
    /* INIZIO OVERRIDE DI FORMA MONODIMENSIONALE */
    /**
     * Modifica la lunghezza della linea mantenendo l'angolo e il punto iniziale fissi.
     * @param dim Nuova lunghezza desiderata della linea (non negativa).
     * @throws IllegalArgumentException se dim è negativa.
     */
    @Override
    public void modificaDimensione(double dim) {
        if (dim < 0) {
            throw new IllegalArgumentException("La dimensione negativa non è ammessa");
        }
        
        double lunghezzaAttuale = this.calcolaLunghezza();
        
        if (lunghezzaAttuale == 0) {
            this.setEndX(this.getStartX() + dim);
            this.setEndY(this.getStartY());
            return;
        }

        double scala = dim / lunghezzaAttuale;
        double nuovoEndX = this.getStartX() + (this.getEndX() - this.getStartX()) * scala;
        double nuovoEndY = this.getStartY() + (this.getEndY() - this.getStartY()) * scala;

        this.setEndX(nuovoEndX);
        this.setEndY(nuovoEndY);
    }

    @Override
    /**
     * Calcola e restituisce la lunghezza attuale della linea.
     * @return Lunghezza della linea.
     */
    public double ottieniValoreDimensione() {
        return this.calcolaLunghezza();
    }


    /* METODI NON DI OVERRIDE */
    
    /**
     * Calcola la lunghezza della linea utilizzando la distanza euclidea.
     * 
     * @return La lunghezza della linea arrotondata a due decimali
     */
    public double calcolaLunghezza() {
        double dx = this.getEndX() - this.getStartX();
        double dy = this.getEndY() - this.getStartY();
        double lunghezza = Math.sqrt(dx * dx + dy * dy);
        return Math.round(lunghezza * 100.0) / 100.0;
    }

}