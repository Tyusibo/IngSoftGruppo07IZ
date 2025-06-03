package com.gruppo07iz.geometrika.forme;

import java.util.List;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Classe PoligonoPersonalizzato, estende Polygon e implementa le funzionalità aggiuntive
 * delle interfacce FormaBidimensionale e FormaPersonalizzata.
 * La classe non può essere estesa.
 */
public final class Poligono extends Polygon implements FormaBidimensionale {
    private Specchiamento specchiamento;

    /**
     * Costruttore che crea un poligono con i punti e i colori specificati.
     * 
     * @param punti Lista di coordinate (x1, y1, x2, y2, ...) per costruire il poligono
     * @param coloreRiempimento Colore di riempimento del poligono
     * @param coloreBordo Colore del bordo del poligono
     */
    public Poligono(List<Double> punti, Color coloreRiempimento, Color coloreBordo) {
        super();
        this.getPoints().addAll(punti);
        this.setStroke(coloreBordo);
        this.setFill(coloreRiempimento);
        this.setStrokeWidth(3);
        specchiamento = new Specchiamento();
    }

    /* INIZIO OVERRIDE DI FORMA PERSONALIZZABILE */
    /**
     * Disegna il poligono sul pannello specificato, aggiungendolo in fondo alla lista dei figli.
     *
     * @param lavagna il pannello su cui disegnare il poligono
     */
    @Override
    public void disegna(Pane lavagna) {
        lavagna.getChildren().add(this);
    }
    /**
     * Disegna il poligono sul pannello specificato, inserendolo nella posizione indicata.
     *
     * @param lavagna il pannello su cui disegnare il poligono
     * @param posizione la posizione in cui inserire il poligono nel pannello
     */
    @Override
    public void disegna(Pane lavagna, int posizione) {
        lavagna.getChildren().add(posizione, this);
    }
    /**
     * Imposta il colore di riempimento del poligono.
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
     * @return il colore di riempimento del poligono
     */
    @Override
    public Color getColoreRiempimento() {
        return (Color) this.getFill();
    }
    /**
     * Imposta il colore del bordo (stroke) del poligono.
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
     * @return il colore del bordo del poligono
     */
    @Override
    public Color getColoreBordo() {
        return (Color) this.getStroke();
    }
    /**
     * Rimuove il poligono dal pannello specificato.
     *
     * @param lavagna il pannello da cui rimuovere il poligono
     */
    @Override
    public void eliminaForma(Pane lavagna) {
        lavagna.getChildren().remove(this);
    }
    /**
     * Sposta il poligono di un delta specificato in orizzontale e verticale,
     * gestendo correttamente rotazioni e specchiature.
     *
     * @param dx lo spostamento lungo l'asse X
     * @param dy lo spostamento lungo l'asse Y
     */
    @Override
    public void trascina(double dx, double dy) {
        // Salva lo stato attuale delle trasformazioni
        final double rotazioneCorrente = this.getRotate();
        final boolean ribaltatoVerticalmente = specchiamento.isSpecchiatoVerticalmente(this);
        final boolean ribaltatoOrizzontalmente = specchiamento.isSpecchiatoOrizzontalmente(this);

        // Resetta temporaneamente le trasformazioni per un movimento pulito
        this.setRotate(0);
        this.getTransforms().clear();

        // Applica lo spostamento
        for (int i = 0; i < getPoints().size(); i += 2) {
            double x = getPoints().get(i);
            double y = getPoints().get(i + 1);
            getPoints().set(i, x + dx);
            getPoints().set(i + 1, y + dy);
        }

        // Ripristina le trasformazioni originali
        if (ribaltatoOrizzontalmente) { this.setSpecchiaturaOrizzontale(); }
        if (ribaltatoVerticalmente) { this.setSpecchiaturaVerticale(); }
        
        this.setRotate(rotazioneCorrente);
    }
    /**
     * Porta il poligono davanti a tutte le altre forme nel pannello.
     */
    @Override
    public void spostaDavanti() {
        this.toFront();
    }
    /**
     * Porta il poligono dietro a tutte le altre forme nel pannello.
     */
    @Override
    public void spostaIndietro() {
        this.toBack();
    }
    /**
     * Applica una rotazione aggiuntiva al poligono.
     *
     * @param angolo angolo in gradi da aggiungere alla rotazione corrente
     */
    @Override
    public void setAngolo(double angolo) {
        this.setRotate(this.getRotate() + angolo);
    }
    /**
     * Restituisce l'angolo di rotazione corrente del poligono.
     *
     * @return angolo di rotazione in gradi
     */
    @Override
    public double getAngolo() {
        return this.getRotate();
    }
    /**
     * Applica una specchiatura orizzontale attorno al centro del poligono.
     */
    @Override
    public void setSpecchiaturaOrizzontale() {
        double[] centro = this.calcolaCentro();
        this.specchiamento.setSpecchiaturaOrizzontale(this, centro[0], centro[1]);
    }
    /**
     * Applica una specchiatura verticale attorno al centro del poligono.
     */
    @Override
    public void setSpecchiaturaVerticale() {
        double[] centro = this.calcolaCentro();
        this.specchiamento.setSpecchiaturaVerticale(this, centro[0], centro[1]);
    }
    /**
     * Cambia le coordinate del poligono spostandolo in modo che il primo punto
     * abbia le nuove coordinate specificate.
     *
     * @param coordinataX nuova coordinata X del primo punto
     * @param coordinataY nuova coordinata Y del primo punto
     */
    @Override
    public void cambiaCoordinate(double coordinataX, double coordinataY) {
        if (getPoints().size() < 2) return;
        double oldX = getPoints().get(0);
        double oldY = getPoints().get(1);
        double dx = coordinataX - oldX;
        double dy = coordinataY - oldY;
        trascina(dx, dy);
    }
    /**
     * Restituisce le coordinate del primo punto del poligono.
     *
     * @return array contenente le coordinate X e Y del primo punto,
     *         o array vuoto se non ci sono punti
     */
    @Override
    public double[] ottieniCoordinate() {
        if (getPoints().size() < 2) {
            return new double[0];
        }
        return new double[] { getPoints().get(0), getPoints().get(1) };
    }    
    /**
     * Calcola il centro geometrico del poligono come media delle coordinate dei suoi punti.
     *
     * @return array contenente le coordinate X e Y del centro
     */
    @Override
    public double[] calcolaCentro() {
        List<Double> points = getPoints();
        if (points.size() < 2) return new double[] {0, 0}; 

        double sommaX = 0;
        double sommaY = 0;
        int nPunti = points.size() / 2;

        for (int i = 0; i < points.size(); i += 2) {
            sommaX += points.get(i);
            sommaY += points.get(i + 1);
        }

        return new double[] { sommaX / nPunti, sommaY / nPunti };
    }
    /**
     * Restituisce una rappresentazione testuale del poligono, utile per il salvataggio.
     * Include numero di punti, coordinate, colori, rotazione, scale e stato di specchiatura.
     *
     * @return stringa che descrive il poligono in formato testo
     */
    @Override
    public String toText() {
        StringBuilder sb = new StringBuilder("POLIGONO");
        List<Double> points = getPoints();
        sb.append(" ").append(points.size()); 
        for (Double d : points) {
            sb.append(" ").append(d);
        }
        sb.append(" ").append(getStroke());
        sb.append(" ").append(getFill());
        
        sb.append(" ").append(getRotate());

        sb.append(" ").append(this.getScaleX());
        sb.append(" ").append(this.getScaleY());

        sb.append(" ").append(this.isSpecchiatoOrizzontalmente() ? 1 : -1);
        sb.append(" ").append(this.isSpecchiatoVerticalmente() ? 1 : -1);
        sb.append("\n");

        return sb.toString();
    }
    /**
     * Crea e restituisce una copia esatta del poligono corrente,
     * mantenendo punti, colori, rotazione, specchiature e scala.
     *
     * @return un nuovo oggetto Poligono con le stesse proprietà
     */
    @Override
    public FormaPersonalizzabile clonaForma() {  
        Poligono copia = new Poligono(this.getPoints(), 
                (Color) getFill(), (Color) getStroke());
        
        copia.setAngolo(this.getAngolo());  
        
        if (this.isSpecchiatoOrizzontalmente()) copia.setSpecchiaturaOrizzontale();
        if (this.isSpecchiatoVerticalmente()) copia.setSpecchiaturaVerticale();
        
        // Dimensioni
        copia.setScaleX(this.getScaleX());
        copia.setScaleY(this.getScaleY());
        
        return copia;
    }
    /**
     * Applica un effetto grafico DropShadow al poligono.
     *
     * @param effetto effetto DropShadow da applicare
     */
    @Override
    public void applicaEffetto(DropShadow effetto) {
        this.setEffect(effetto);
    }
    
    /* INIZIO OVERRIDE DI FORMA BIDIMENSIONALE */

    /**
     * Modifica le dimensioni del poligono cambiando le sue scale X e Y
     * in base alle dimensioni desiderate e alle dimensioni attuali.
     *
     * @param dim1 nuova larghezza desiderata
     * @param dim2 nuova altezza desiderata
     */
    @Override
    public void modificaDimensioni(double dim1, double dim2) {
       
        // Calcolo dimensioni attuali reali (dopo eventuali trasformazioni grafiche)
        double larghezzaAttuale = this.getBoundsInLocal().getWidth();
        double altezzaAttuale = this.getBoundsInLocal().getHeight();

        if (larghezzaAttuale == 0 || altezzaAttuale == 0) return; // evita divisione per zero

        // Calcola i nuovi scaleX e scaleY
        double fattoreX = dim1 / larghezzaAttuale;
        double fattoreY = dim2 / altezzaAttuale;

        this.setScaleX(fattoreX);
        this.setScaleY(fattoreY); 
    }
    /**
     * Restituisce le dimensioni correnti del poligono considerando le scale applicate.
     *
     * @return array contenente larghezza e altezza arrotondate a 2 decimali
     */
    @Override
    public double[] ottieniValoriDimensioni() {
        double dim1 = (Math.round((this.getBoundsInLocal().getWidth()* this.getScaleX() ) * 100.0)/100);
        double dim2 = (Math.round((this.getBoundsInLocal().getHeight()* this.getScaleY() ) * 100.0)/100);

        return new double[] { dim1, dim2 }; 
    }

    /* METODI NON DI OVERRIDE */
    
    /**
     * Verifica se il poligono è specchiato orizzontalmente.
     * 
     * @return true se è applicata una specchiatura orizzontale, false altrimenti
     */
    public boolean isSpecchiatoOrizzontalmente() {
        return specchiamento.isSpecchiatoOrizzontalmente(this);
    }

    /**
     * Verifica se il poligono è specchiato verticalmente.
     * 
     * @return true se è applicata una specchiatura verticale, false altrimenti
     */
    public boolean isSpecchiatoVerticalmente() {
        return specchiamento.isSpecchiatoVerticalmente(this);
    }
}