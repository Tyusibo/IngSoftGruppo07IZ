package com.gruppo07iz.geometrika.forme;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 * Classe Ellisse Personalizzato, estende Ellipse e implementa le funzionalità aggiuntive
 * delle interfacce FormaBidimensionale e FormaPersonalizzata.
 * La classe non può essere estesa.
 */
public final class Ellisse extends Ellipse implements FormaBidimensionale {
    /**
     * Costruttore che crea un'ellisse nelle coordinate specificate con colori personalizzati.
     * L'ellisse viene creato con valori di default per i raggi (70x30).
     * 
     * @param coordinataX Coordinata X del centro dell'ellisse
     * @param coordinataY Coordinata Y del centro dell'ellisse
     * @param coloreBordo Colore del bordo dell'ellisse
     * @param coloreRiempimento Colore di riempimento dell'ellisse
     */
    public Ellisse(double coordinataX, double coordinataY, Color coloreBordo, Color coloreRiempimento) {
        super(coordinataX, coordinataY, 70, 30);
        this.setStroke(coloreBordo);
        this.setFill(coloreRiempimento);
        this.setStrokeWidth(3);
    }

    /* INIZIO OVERRIDE DI FORMA PERSONALIZZABILE */
    /**
     * Disegna l'ellisse sul pannello specificato aggiungendola come figlio.
     *
     * @param lavagna il pannello su cui disegnare l'ellisse
     */
    @Override
    public void disegna(Pane lavagna) {
        lavagna.getChildren().add(this);
    }
    /**
     * Disegna l'ellisse sul pannello specificato in una posizione specifica nell'elenco dei figli.
     *
     * @param lavagna il pannello su cui disegnare l'ellisse
     * @param posizione la posizione nell'elenco dei figli in cui inserire l'ellisse
     */
    @Override
    public void disegna(Pane lavagna, int posizione) {
        lavagna.getChildren().add(posizione, this);
    }
    /**
     * Imposta il colore di riempimento dell'ellisse.
     *
     * @param coloreRiempimento il colore da utilizzare per il riempimento
     */
    @Override
    public void setColoreRiempimento(Color coloreRiempimento) {
        this.setFill(coloreRiempimento);
    }
    /**
     * Restituisce il colore di riempimento dell'ellisse.
     *
     * @return il colore di riempimento corrente
     */
    @Override
    public Color getColoreRiempimento() {
        return (Color) this.getFill();
    }
    /**
     * Imposta il colore del bordo dell'ellisse.
     *
     * @param coloreBordo il colore da utilizzare per il bordo
     */
    @Override
    public void setColoreBordo(Color coloreBordo) {
        this.setStroke(coloreBordo);
    }
    /**
     * Restituisce il colore del bordo dell'ellisse.
     *
     * @return il colore del bordo corrente
     */
    @Override
    public Color getColoreBordo() {
        return (Color) this.getStroke();
    }
    /**
     * Rimuove l'ellisse dal pannello specificato.
     *
     * @param lavagna il pannello da cui rimuovere l'ellisse
     */
    @Override
    public void eliminaForma(Pane lavagna) {
        lavagna.getChildren().remove(this);
    }
    /**
     * Sposta l'ellisse di una quantità specificata lungo gli assi X e Y.
     *
     * @param dx spostamento lungo l'asse X
     * @param dy spostamento lungo l'asse Y
     */
    @Override
    public void trascina(double dx, double dy) {
        this.setCenterX(this.getCenterX() + dx);
        this.setCenterY(this.getCenterY() + dy);
    }
    /**
     * Porta l'ellisse in primo piano nella gerarchia grafica.
     */
    @Override
    public void spostaDavanti() {
        this.toFront();
    }
    /**
     * Porta l'ellisse in secondo piano nella gerarchia grafica.
     */
    @Override
    public void spostaIndietro() {
        this.toBack();
    }
    /**
     * Ruota l'ellisse dell'angolo specificato (in gradi).
     *
     * @param angolo angolo di rotazione da aggiungere
     */
    @Override
    public void setAngolo(double angolo) {
        this.setRotate(this.getRotate() + angolo);
    }
    /**
     * Restituisce l'angolo di rotazione corrente dell'ellisse (in gradi).
     *
     * @return l'angolo di rotazione corrente
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
     * Imposta nuove coordinate per il centro dell'ellisse.
     *
     * @param coordinataX nuova coordinata X (ascissa) del centro
     * @param coordinataY nuova coordinata Y (ordinata) del centro
     */
    @Override
    public void cambiaCoordinate(double coordinataX, double coordinataY) {
        this.setCenterX(coordinataX);
        this.setCenterY(coordinataY);
    }

    /**
     * Restituisce le coordinate del centro dell'ellisse.
     *
     * @return un array con le coordinate [centerX, centerY]
     */
    @Override
    public double[] ottieniCoordinate() {
        return new double[] { this.getCenterX(), this.getCenterY() };
    }

    /**
     * Calcola e restituisce il centro geometrico dell'ellisse.
     *
     * @return un array con le coordinate del centro [centerX, centerY]
     */
    @Override
    public double[] calcolaCentro() {
        return new double[] { this.getCenterX(), this.getCenterY() };
    }
    /**
     * Restituisce una rappresentazione testuale dell'ellisse con le sue proprietà.
     *
     * @return stringa contenente il tipo di forma, posizione, colori, dimensioni e rotazione
     */
    @Override
    public String toText() {
        double[] dimensioni = this.ottieniValoriDimensioni(); 
        return "ELLISSE" + " " + this.getCenterX() + " " + this.getCenterY() + " " +
               this.getStroke() + " " + this.getFill() + " " + 
               dimensioni[0] + " " + dimensioni[1] + " " + this.getRotate() + "\n";
    }
    /**
     * Crea e restituisce una copia profonda dell'ellisse corrente.
     *
     * @return un nuovo oggetto Ellisse identico a questo
     */
    @Override
    public FormaPersonalizzabile clonaForma() {
        Ellisse copia = new Ellisse(this.getCenterX(), this.getCenterY(), 
                                 (Color) this.getStroke(), (Color) this.getFill());
        
        copia.setRotate(this.getRotate());
          
        double dimensioni[] = this.ottieniValoriDimensioni();
        copia.modificaDimensioni(dimensioni[0], dimensioni[1]);

        return copia;
    }
    /**
     * Applica un effetto grafico all'ellisse.
     *
     * @param effetto l'effetto da applicare (ad esempio DropShadow)
     */
    @Override
    public void applicaEffetto(DropShadow effetto) {
        this.setEffect(effetto);
    }
    
    /* INIZIO OVERRIDE DI FORMA BIDIMENSIONALE */
    /**
     * Modifica le dimensioni dell'ellisse impostando i raggi orizzontale e verticale.
     *
     * @param dim1 diametro orizzontale (raggio X * 2)
     * @param dim2 diametro verticale (raggio Y * 2)
     */
    @Override
    public void modificaDimensioni(double dim1, double dim2) {
        this.setRadiusX(Math.round((dim1/2) * 100.0)/100.0);
        this.setRadiusY(Math.round((dim2/2) * 100.0)/100.0);
    }
    /**
     * Restituisce i valori delle dimensioni dell'ellisse (diametro orizzontale e verticale).
     *
     * @return array contenente [diametro orizzontale, diametro verticale]
     */
    @Override
    public double[] ottieniValoriDimensioni() {
        double dim1 = (Math.round((this.getRadiusX()*2) * 100.0)/100.0);
        double dim2 = (Math.round((this.getRadiusY()*2) * 100.0)/100.0);

        return new double[] { dim1, dim2}; 
    }
}