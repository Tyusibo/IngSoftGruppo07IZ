package com.gruppo07iz.geometrika.forme;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Classe Rettangolo Personalizzato, estende Rectangle e implementa le funzionalità aggiuntive
 * delle interfacce FormaBidimensionale e FormaPersonalizzata.
 * La classe non può essere estesa.
 */
public final class Rettangolo extends Rectangle implements FormaBidimensionale {

    /**
     * Costruttore che crea un rettangolo con vertice in alto a sinistra nelle coordinate specificate.
     * Il rettangolo viene creato con dimensioni di default (100x60).
     * 
     * @param coordinataX Coordinata X del vertice in alto a sinistra
     * @param coordinataY Coordinata Y del vertice in alto a sinistra
     * @param coloreBordo Colore del bordo del rettangolo
     * @param coloreRiempimento Colore di riempimento del rettangolo
     */
    public Rettangolo(double coordinataX, double coordinataY, Color coloreBordo, Color coloreRiempimento) {
        super(coordinataX, coordinataY, 100, 60);
        this.setStroke(coloreBordo);
        this.setFill(coloreRiempimento);
        this.setStrokeWidth(3);
    }

    /* INIZIO OVERRIDE DI FORMA PERSONALIZZABILE */

    /**
     * Disegna il rettangolo sul pannello specificato, aggiungendolo in fondo alla lista dei figli.
     *
     * @param lavagna il pannello su cui disegnare il rettangolo
     */
    @Override
    public void disegna(Pane lavagna) {
        lavagna.getChildren().add(this);
    }
    /**
     * Disegna il rettangolo sul pannello specificato, inserendolo nella posizione indicata.
     *
     * @param lavagna il pannello su cui disegnare il rettangolo
     * @param posizione la posizione in cui inserire il rettangolo nel pannello
     */
    @Override
    public void disegna(Pane lavagna, int posizione) {
        lavagna.getChildren().add(posizione, this);
    }
    /**
     * Imposta il colore di riempimento del rettangolo.
     *
     * @param coloreRiempimento il colore di riempimento da impostare
     */
    @Override
    public void setColoreRiempimento(Color coloreRiempimento) {
        this.setFill(coloreRiempimento);
    }
    /**
    * Restituisce il colore di riempimento attualmente impostato.
    *
    * @return il colore di riempimento del rettangolo
    */
    @Override
    public Color getColoreRiempimento() {
        return (Color) this.getFill();
    }
    /**
     * Imposta il colore del bordo (stroke) del rettangolo.
     *
     * @param coloreBordo il colore del bordo da impostare
     */
    @Override
    public void setColoreBordo(Color coloreBordo) {
        this.setStroke(coloreBordo);
    }
    /**
     * Restituisce il colore del bordo attualmente impostato.
     *
     * @return il colore del bordo del rettangolo
     */
    @Override
    public Color getColoreBordo() {
        return (Color) this.getStroke();
    }
    /**
     * Rimuove il rettangolo dal pannello specificato.
     *
     * @param lavagna il pannello da cui rimuovere il rettangolo
     */
    @Override
    public void eliminaForma(Pane lavagna) {
        lavagna.getChildren().remove(this);
    }
    /**
     * Sposta il rettangolo di un delta specificato in orizzontale e verticale.
     *
     * @param dx lo spostamento lungo l'asse X
     * @param dy lo spostamento lungo l'asse Y
     */
    @Override
    public void trascina(double dx, double dy) {        
        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);
    }
    
    /**
     * Porta il rettangolo davanti a tutte le altre forme nel pannello.
     */
    @Override
    public void spostaDavanti() {
        this.toFront();
    }
    
    /**
     * Porta il rettangolo dietro a tutte le altre forme nel pannello.
     */
    @Override
    public void spostaIndietro() {
        this.toBack();
    }
    /**
     * Applica una rotazione aggiuntiva al rettangolo.
     *
     * @param angolo angolo in gradi da aggiungere alla rotazione corrente
     */
    @Override
    public void setAngolo(double angolo) {
        this.setRotate(this.getRotate() + angolo);
    }
    /**
     * Restituisce l'angolo di rotazione corrente del rettangolo.
     *
     * @return angolo di rotazione in gradi
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
     * Cambia le coordinate assolute del rettangolo.
     *
     * @param coordinataX nuova coordinata X
     * @param coordinataY nuova coordinata Y
     */
    @Override
    public void cambiaCoordinate(double coordinataX, double coordinataY) {
        this.setX(coordinataX);
        this.setY(coordinataY);
    }
    /**
     * Restituisce le coordinate correnti del rettangolo.
     *
     * @return array contenente le coordinate X e Y
     */
    @Override
    public double[] ottieniCoordinate() {
        return new double[] { this.getX(), this.getY() };
    }
    
    /**
     * Calcola il centro geometrico del rettangolo.
     *
     * @return array contenente le coordinate X e Y del centro
     */
    @Override
    public double[] calcolaCentro() {
        double centroX = this.getX() + this.getWidth() / 2;
        double centroY = this.getY() + this.getHeight() / 2;
        return new double[] { centroX, centroY };
    }
    /**
     * Restituisce una rappresentazione testuale del rettangolo, utile per il salvataggio.
     *
     * @return stringa con informazioni di posizione, colori, dimensioni e rotazione
     */
    @Override
    public String toText() {
        return "RETTANGOLO" + " " + this.getX() + " " + this.getY() + " " +
               this.getStroke() + " " + this.getFill() + " " +
               this.getWidth() + " " + this.getHeight() + " " + this.getRotate() + "\n";
    }
    /**
     * Crea e restituisce una copia esatta del rettangolo corrente.
     *
     * @return un nuovo oggetto Rettangolo con le stesse proprietà
     */
    @Override
    public FormaPersonalizzabile clonaForma() {
        Rettangolo copia = new Rettangolo(this.getX(), this.getY(), 
                                      (Color) this.getStroke(), (Color) this.getFill());
        
        copia.modificaDimensioni(this.getWidth(), this.getHeight());
        copia.setRotate(this.getRotate());
        
        return copia;
    }
    /**
     * Applica un effetto grafico al rettangolo.
     *
     * @param effetto effetto DropShadow da applicare
     */
    @Override
    public void applicaEffetto(DropShadow effetto) {
        this.setEffect(effetto);
    }

    /* INIZIO OVERRIDE DI FORMA BIDIMENSIONALE */

    /**
     * Modifica le dimensioni del rettangolo.
     *
     * @param dim1 nuova larghezza
     * @param dim2 nuova altezza
     */
    @Override
    public void modificaDimensioni(double dim1, double dim2) {
        this.setWidth(dim1);
        this.setHeight(dim2);
    }
    
    /**
     * Restituisce le dimensioni correnti del rettangolo.
     *
     * @return array contenente larghezza e altezza
     */
    @Override
    public double[] ottieniValoriDimensioni() {
        return new double[] { this.getWidth(), this.getHeight() };
    }
}