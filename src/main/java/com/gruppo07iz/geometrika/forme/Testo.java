package com.gruppo07iz.geometrika.forme;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Testo extends Text implements FormaBidimensionale{
    private Specchiamento specchiamento;

    /**
    * Il costruttore vuole come parametri le coordinate del punto per il vertice in alto a sinistra
    * e i colori per il riempimento e per il bordo
    * Il rettangolo viene creato con dimensioni di altezza e larghezza di default
    * @param testo
    * @param coordinataX
    * @param coordinataY
    * @param coloreBordo
    * @param coloreRiempiemento 
    * @param dimensione , ovvero il font
    */
    public Testo(String testo, double coordinataX, double coordinataY, Color coloreBordo, Color coloreRiempiemento, double dimensione){
        super(coordinataX, coordinataY, testo);
        this.setFont(new Font(dimensione)); 
        //this.setWrappingWidth(150);
        
        this.setStroke(coloreBordo);
        this.setFill(coloreRiempiemento);
        
        specchiamento = new Specchiamento();
    }
    
     /* INIZIO METODI FORMA PERSONALIZZABILE */

     /**
     * Disegna la forma testuale sulla lavagna aggiungendola alla fine della lista dei figli.
     *
     * @param lavagna il pannello JavaFX su cui disegnare la forma.
     */
    @Override
    public void disegna(Pane lavagna){
        lavagna.getChildren().add(this);
    }
    
    /**
     * Disegna la forma testuale sulla lavagna in una posizione specifica della lista dei figli.
     *
     * @param lavagna   il pannello JavaFX su cui disegnare la forma.
     * @param posizione la posizione in cui inserire la forma nel pannello.
     */
    @Override
    public void disegna(Pane lavagna, int posizione){
        lavagna.getChildren().add(posizione, this);
    }
    
    /**
     * Imposta il colore di riempimento del testo.
     *
     * @param coloreRiempimento il colore con cui riempire il testo.
     */
    @Override
    public void setColoreRiempimento(Color coloreRiempimento){
        this.setFill(coloreRiempimento);
    }
    /**
     * Restituisce il colore di riempimento attuale del testo.
     *
     * @return il colore di riempimento.
     */
    @Override
    public Color getColoreRiempimento(){
        return (Color) this.getFill();
    }

    /**
     * Imposta il colore del bordo (stroke) del testo.
     *
     * @param coloreBordo il colore del bordo.
     */
    @Override
    public void setColoreBordo(Color coloreBordo){
        this.setStroke(coloreBordo);
    }
    /**
     * Restituisce il colore del bordo attuale del testo.
     *
     * @return il colore del bordo.
     */
    @Override
    public Color getColoreBordo(){
        return (Color) this.getStroke();
    }
    /**
     * Rimuove il testo dalla lavagna.
     *
     * @param lavagna il pannello da cui rimuovere la forma.
     */
    @Override
    public void eliminaForma(Pane lavagna){
        lavagna.getChildren().remove(this);
    }
    /**
     * Sposta il testo di un certo delta orizzontale e verticale.
     * Eventuali trasformazioni come rotazioni o specchiature vengono gestite per evitare distorsioni.
     *
     * @param dx delta sull'asse X.
     * @param dy delta sull'asse Y.
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
        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);

        // Ripristina le trasformazioni originali
        if (ribaltatoOrizzontalmente) { this.setSpecchiaturaOrizzontale(); }
        if (ribaltatoVerticalmente) { this.setSpecchiaturaVerticale(); }
        
        this.setRotate(rotazioneCorrente);
    }
    
    /**
     * Porta la forma davanti a tutte le altre nella gerarchia dei nodi.
     */
    @Override
    public void spostaDavanti(){
        this.toFront();
    }
    /**
     * Manda la forma dietro a tutte le altre nella gerarchia dei nodi.
     */
    @Override
    public void spostaIndietro(){
        this.toBack();
    }
    /**
     * Applica una rotazione al testo.
     *
     * @param angolo l'angolo da aggiungere alla rotazione corrente (in gradi).
     */
    @Override
    public void setAngolo(double angolo){
        this.setRotate(this.getRotate() + angolo);
    }
    /**
     * Restituisce l’angolo di rotazione corrente della forma.
     *
     * @return l’angolo di rotazione in gradi.
     */
    @Override
    public double getAngolo(){
        return this.getRotate();
    }
    /**
     * Applica uno specchiamento orizzontale rispetto al centro della forma.
     */
    @Override
    public void setSpecchiaturaOrizzontale() {
        double[] centro = this.calcolaCentro();

       this.specchiamento.setSpecchiaturaOrizzontale(this, centro[0], centro[1]);
    }
    /**
     * Applica uno specchiamento verticale rispetto al centro della forma.
     */
    @Override
    public void setSpecchiaturaVerticale() {
        double[] centro = this.calcolaCentro();

        this.specchiamento.setSpecchiaturaVerticale(this, centro[0], centro[1]);
    }
    /**
     * Cambia le coordinate assolute del testo.
     *
     * @param coordinataX la nuova coordinata X.
     * @param coordinataY la nuova coordinata Y.
     */
    @Override
    public void cambiaCoordinate(double coordinataX, double coordinataY) {
        this.setX(coordinataX);
        this.setY(coordinataY);
    }
    /**
     * Restituisce le coordinate attuali del testo.
     *
     * @return un array contenente X e Y in ordine.
     */
    @Override
    public double[] ottieniCoordinate() {
        return new double[] { this.getX(), this.getY() };
    }
    /**
     * Calcola il centro geometrico del testo, utile per rotazioni e specchiamenti.
     *
     * @return un array contenente le coordinate del centro [x, y].
     */
    @Override
    public double[] calcolaCentro() {
        double centroX = this.getX() + this.getWrappingWidth() / 2;
        double centroY = this.getY() + this.getFont().getSize() / 2;
        return new double[] { centroX, centroY };
    }
    /**
     * Serializza la forma testuale in una stringa per l’esportazione.
     *
     * @return una rappresentazione testuale della forma.
     */
   @Override
    public String toText() {
        double specchiaturaX = this.isSpecchiatoOrizzontalmente() ? 1 : -1;
        double specchiaturaY = this.isSpecchiatoVerticalmente()? 1 : -1;


        // Calcolo parole
        String contenuto = this.getText().trim();
        int numeroParole = contenuto.isEmpty() ? 0 : contenuto.split(" ").length;

        return "TESTO " +
            numeroParole + " " +
            this.getText() + " " +
            this.getX() + " " +
            this.getY() + " " +
            this.getStroke() + " " +
            this.getFill() + " " +
            this.getFont().getSize() + " " + 
            this.getScaleX() + " " +
            this.getScaleY() + " " +
            specchiaturaX + " " +
            specchiaturaY + " " +
            this.getRotate() + "\n";
    }
    /**
     * Crea una copia della forma testuale, inclusi colori, trasformazioni e dimensioni.
     *
     * @return la forma clonata.
     */
    @Override
    public FormaPersonalizzabile clonaForma() {
        // Costruttore: coordinate, colori, testo e font
        Testo copia = new Testo(this.getText(), this.getX(), this.getY(), 
                                (Color) this.getStroke(), (Color) this.getFill(), this.getFont().getSize());

        // Angolo di rotazione
        copia.setAngolo(this.getAngolo());  
        
        // Specchiamento
        if (this.isSpecchiatoOrizzontalmente()) copia.setSpecchiaturaOrizzontale();
        if (this.isSpecchiatoVerticalmente()) copia.setSpecchiaturaVerticale();
        
        // Dimensioni
        copia.setScaleX(this.getScaleX());
        copia.setScaleY(this.getScaleY());
        
        return copia;
    }
    
      /**
     * Applica un effetto visivo al testo, ad esempio un'ombra.
     *
     * @param effetto l'effetto grafico da applicare.
     */
    @Override
    public void applicaEffetto(DropShadow effetto){
        this.setEffect(effetto);
    }
    
    /* INIZIO METODI FORMABIDIMENSIONALE */
    /**
     * Modifica la dimensione della forma scalando le sue dimensioni attuali.
     *
     * @param dim1 nuova larghezza desiderata.
     * @param dim2 nuova altezza desiderata.
     */
    @Override
    public void modificaDimensioni(double dim1, double dim2) {
       
        // Calcolo dimensioni attuali reali (dopo eventuali trasformazioni grafiche)
        double larghezzaAttuale = this.getBoundsInLocal().getWidth();
        double altezzaAttuale = this.getBoundsInLocal().getHeight();

        if (larghezzaAttuale == 0 || altezzaAttuale == 0) return; 

        // Calcola i nuovi scaleX e scaleY
        double fattoreX = dim1 / larghezzaAttuale;
        double fattoreY = dim2 / altezzaAttuale;

        this.setScaleX(fattoreX);
        this.setScaleY(fattoreY);
    }
    /**
     * Restituisce le dimensioni attuali (larghezza e altezza) del testo,
     * considerando eventuali trasformazioni.
     *
     * @return array contenente [larghezza, altezza].
     */
    @Override
    public double[] ottieniValoriDimensioni() {
        double dim1 = (Math.round((this.getBoundsInLocal().getWidth()* this.getScaleX() ) * 100.0)/100);
        double dim2 = (Math.round((this.getBoundsInLocal().getHeight()* this.getScaleY() ) * 100.0)/100);

        return new double[] { dim1, dim2 }; 
    }
    
    /*** METODI DI UTILITA INTERNA INDIPENDENTI DALL'INTERFACCIA IMPLEMENTATA ***/

    /**
     * Verifica se la forma è attualmente specchiata orizzontalmente.
     *
     * @return {@code true} se è specchiata orizzontalmente, altrimenti {@code false}.
     */
    public boolean isSpecchiatoOrizzontalmente() {
     return specchiamento.isSpecchiatoOrizzontalmente(this);
    }
    /**
     * Verifica se la forma è attualmente specchiata verticalmente.
     *
     * @return {@code true} se è specchiata verticalmente, altrimenti {@code false}.
     */
    public boolean isSpecchiatoVerticalmente() {
          return specchiamento.isSpecchiatoVerticalmente(this);
    }

}
