package com.gruppo07iz.geometrika.forme;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;


public class Gruppo implements FormaBidimensionale{
    private List <FormaPersonalizzabile> vettoreForme;
    private double lunghezza, altezza;
    private double angolo;
    
    public Gruppo(){
        this.vettoreForme=new ArrayList<>();
        this.lunghezza = this.altezza = 1;
        this.angolo = 0;
    }

    public Gruppo(double angolo, double lunghezza, double altezza) {
        this.vettoreForme=new ArrayList<>();
        this.lunghezza = lunghezza;
        this.altezza = altezza;
        this.angolo = angolo;
    }

    /**
     * Restituisce la lista delle forme attualmente contenute nel gruppo.
     *
     * @return Lista di {@code FormaPersonalizzabile}.
     */
    public List<FormaPersonalizzabile> getVettoreForme() {
        return vettoreForme;
    }
    
   /**
    * Restituisce tutte le forme contenute nel gruppo, incluse quelle nei sottogruppi.
    *
    * @return Lista piatta di tutte le {@code FormaPersonalizzabile}, anche annidate.
    */
   public List<FormaPersonalizzabile> getTutteLeForme() {
       List<FormaPersonalizzabile> tutteLeForme = new ArrayList<>();
       for (FormaPersonalizzabile f : vettoreForme) {
           if (f instanceof Gruppo) {
               // Ricorsione per esplodere il sottogruppo
               tutteLeForme.addAll(((Gruppo) f).getTutteLeForme());
           } else {
               tutteLeForme.add(f);
           }
       }
       return tutteLeForme;
   }

    /**
     * Aggiunge una forma al gruppo.
     *
     * @param forma La forma da aggiungere.
     */
    public void aggiungiForma(FormaPersonalizzabile forma){
        this.vettoreForme.add(forma);
    }
    /**
     * Rimuove una forma dal gruppo.
     *
     * @param forma La forma da rimuovere.
     */
    public void rimuoviForma(FormaPersonalizzabile forma){
        this.vettoreForme.remove(forma);
    }

    /**
     * Sposta tutte le forme contenute nel gruppo di uno spostamento dx e dy.
     * 
     * @param dx spostamento in ascissa
     * @param dy spostamento in ordinata
     */
    @Override
    public void trascina(double dx, double dy) {
        for(int i = 0; i < this.vettoreForme.size(); i++){
            this.vettoreForme.get(i).trascina(dx, dy);
        }
    }
 
    /* INIZIO METODI FORMA PERSONALIZZABILE */
    /**
     * Disegna tutte le forme del gruppo sul pane fornito.
     * 
     * @param lavagna il Pane su cui disegnare le forme
     */
    @Override
    public void disegna(Pane lavagna){
        for(int i = 0; i < this.vettoreForme.size(); i++){
            this.vettoreForme.get(i).disegna(lavagna);
        }
    }
        /**
     * Disegna tutte le forme del gruppo sul pane fornito nella posizione specificata.
     * 
     * @param lavagna il Pane su cui disegnare le forme
     * @param posizione la posizione in cui inserire le forme nel Pane
     */

    @Override
    public void disegna(Pane lavagna, int posizione){
                            System.out.println("disenga gruppo");
                        System.out.println(this.vettoreForme.size());    
        for(int i = 0; i < this.vettoreForme.size(); i++){
                        System.out.println(this.vettoreForme.get(i));

            this.vettoreForme.get(i).disegna(lavagna, posizione);
        }
    }
    /**
     * Imposta il colore di riempimento di tutte le forme del gruppo.
     * 
     * @param coloreRiempimento il colore da applicare
     */
    @Override
    public void setColoreRiempimento(Color coloreRiempimento){
        for(int i = 0; i < this.vettoreForme.size(); i++){
            this.vettoreForme.get(i).setColoreRiempimento(coloreRiempimento);
        }
    }
    /**
     * Restituisce il colore di riempimento comune a tutte le forme del gruppo,
     * oppure {@link Color#BLACK} se i colori sono diversi o il gruppo è vuoto.
     * 
     * @return il colore di riempimento comune o colore di default
     */
    @Override
    public Color getColoreRiempimento(){
        if (vettoreForme.isEmpty()) {
            return Color.BLACK; // colore di Default 
        }

        Color coloreComune = vettoreForme.get(0).getColoreRiempimento();

        for (int i = 1; i < vettoreForme.size(); i++) {
            if (!vettoreForme.get(i).getColoreRiempimento().equals(coloreComune)) {
                return Color.BLACK; // Colori diversi, ritorna default
            }
        }
        
        // Tutti i colori sono uguali
        return coloreComune; 
    }

    /**
     * Imposta il colore del bordo di tutte le forme del gruppo.
     * 
     * @param coloreBordo il colore da applicare al bordo
     */
    @Override
    public void setColoreBordo(Color coloreBordo){
        for(int i = 0; i < this.vettoreForme.size(); i++){
            this.vettoreForme.get(i).setColoreBordo(coloreBordo);
        }
    }
    
    /**
     * Restituisce il colore del bordo comune a tutte le forme del gruppo,
     * oppure {@link Color#BLACK} se i colori sono diversi o il gruppo è vuoto.
     * 
     * @return il colore del bordo comune o colore di default
     */
    @Override
    public Color getColoreBordo() {
        if (vettoreForme.isEmpty()) {
            return Color.BLACK; // colore di Default 
        }

        Color coloreComune = vettoreForme.get(0).getColoreBordo();

        for (int i = 1; i < vettoreForme.size(); i++) {
            if (!vettoreForme.get(i).getColoreBordo().equals(coloreComune)) {
                return Color.BLACK; // Colori diversi, ritorna default
            }
        }
        
        // Tutti i colori sono uguali
        return coloreComune; 
    }
    /**
     * Rimuove tutte le forme del gruppo dal Pane specificato.
     * 
     * @param lavagna il Pane da cui rimuovere le forme
     */
    @Override
    public void eliminaForma(Pane lavagna){
        for(int i = 0; i < this.vettoreForme.size(); i++){
            this.vettoreForme.get(i).eliminaForma(lavagna);
        }
        //this.vettoreForme.clear();
    }
    
    
    /**
     * Porta tutte le forme del gruppo in primo piano (davanti).
     */
    @Override
    public void spostaDavanti(){
        for(int i = 0; i < this.vettoreForme.size(); i++){
            this.vettoreForme.get(i).spostaDavanti();
        }
    }
    /**
     * Porta tutte le forme del gruppo in secondo piano (dietro).
     */
    @Override
    public void spostaIndietro(){
        for(int i = 0; i < this.vettoreForme.size(); i++){
            this.vettoreForme.get(i).spostaIndietro();
        }
    }
    /**
     * Imposta l'angolo di rotazione per tutte le forme del gruppo,
     * sommando l'angolo specificato all'angolo corrente del gruppo.
     * 
     * @param angolo l'angolo da aggiungere (in gradi)
     */
    @Override
    public void setAngolo(double angolo){
        for(int i = 0; i < this.vettoreForme.size(); i++){
            FormaPersonalizzabile forma = this.vettoreForme.get(i);
            forma.setAngolo(angolo);
        }
        this.angolo = this.angolo + angolo;
    }
    /**
     * Restituisce l'angolo di rotazione del gruppo.
     * 
     * @return l'angolo di rotazione (in gradi)
     */
    @Override
    public double getAngolo(){
        return this.angolo;
    }
    /**
     * Applica la specchiatura orizzontale a tutte le forme del gruppo.
     */
    @Override
    public void setSpecchiaturaOrizzontale(){
        for(int i = 0; i < this.vettoreForme.size(); i++){
            this.vettoreForme.get(i).setSpecchiaturaOrizzontale();
        }
    }
    /**
     * Applica la specchiatura verticale a tutte le forme del gruppo.
     */
    @Override
    public void setSpecchiaturaVerticale(){
        for(int i = 0; i < this.vettoreForme.size(); i++){
            this.vettoreForme.get(i).setSpecchiaturaVerticale();
        }
    }
           
    /**
     * Modifica le coordinate del centro dell'ellisse.
     *
     * @param coordinataX la nuova coordinata X (ascissa) del centro dell'ellisse
     * @param coordinataY la nuova coordinata Y (ordinata) del centro dell'ellisse
     */
    @Override
    public void cambiaCoordinate(double coordinataX, double coordinataY) {
        double[] centroAttuale = this.calcolaCentro();
        double dx = coordinataX - centroAttuale[0];
        double dy = coordinataY - centroAttuale[1];

        for (FormaPersonalizzabile forma : vettoreForme) {
            forma.trascina(dx, dy);
        }
    }
     /**
     * Restituisce le coordinate del centro dell'ellisse.
     *
     * @return un array double[] contenente [centerX, centerY]
     */
    @Override
    public double[] ottieniCoordinate() {
        return null;
    }

    /**
     * Restituisce la rappresentazione testuale del gruppo,
     * includendo la lista delle forme che lo compongono.
     * 
     * @return la stringa descrittiva del gruppo e delle sue forme
     */
    @Override
    public String toText() {
        StringBuilder sb = new StringBuilder();
        sb.append("GRUPPO").append(" ");
        sb.append(this.angolo).append(" ");
        sb.append(this.lunghezza).append(" ");
        sb.append(this.altezza).append("\n");

        for (FormaPersonalizzabile forma : vettoreForme) {
            sb.append(forma.toText());
        }

        sb.append("FINEGRUPPO").append("\n");
        return sb.toString();
    }

    /**
     * Crea e restituisce una copia profonda (clone) del gruppo,
     * mantenendo l'ordine grafico originale delle forme.
     * 
     * @return una nuova istanza di Gruppo clonata
     */
    @Override
    public FormaPersonalizzabile clonaForma() {
        //Preparo il clone
        Gruppo cloneGruppo = new Gruppo();

        // Tutte le forme da copiare
        List<FormaPersonalizzabile> listaForme = this.getTutteLeForme();
        
        // Devo ottenere il Pane, mi basta una forma qualsiasi
        Pane padre = null;
        if (!listaForme.isEmpty()) {
            padre = (Pane) ((Shape)listaForme.get(0)).getParent();
        }

        // Preparo la lista di forme ordinate che aggiungerò al Pane nel corretto ordine
        List<FormaPersonalizzabile> formeOrdinate = new ArrayList<>();

        if (padre != null) {
            // Scorri i figli del padre nell'ordine grafico
            for (var node : padre.getChildren()) {
                if (node instanceof FormaPersonalizzabile) {
                    FormaPersonalizzabile forma = (FormaPersonalizzabile) node;
                    // Se la forma appartiene a questo gruppo, la aggiungo in ordine
                    if (listaForme.contains(forma)) {
                        formeOrdinate.add(forma);
                    }
                }
            }
        } else {
            // Se per qualche motivo non trova il Pane le aggiunge a caso
            formeOrdinate.addAll(vettoreForme);
        }

        // Clona le forme nell'ordine corretto
        for (FormaPersonalizzabile forma : formeOrdinate) {
            cloneGruppo.aggiungiForma(forma.clonaForma());
        }

        cloneGruppo.altezza = this.altezza;
        cloneGruppo.lunghezza = this.lunghezza;
        cloneGruppo.angolo = this.angolo;

        return cloneGruppo;
    }
    
    /**
     * Applica l'effetto DropShadow a tutte le forme del gruppo.
     * 
     * @param effetto l'effetto da applicare
     */
    @Override
    public void applicaEffetto(DropShadow effetto){
        for(int i = 0; i < this.vettoreForme.size(); i++){
            this.vettoreForme.get(i).applicaEffetto(effetto);
        }
    }
    
    /**
     * Calcola e restituisce le coordinate del centro geometrico del gruppo,
     * calcolando la media delle coordinate di tutte le forme contenute.
     * 
     * @return un array double[] contenente [centerX, centerY]
     */
    @Override
    public double[] calcolaCentro() {
        if (vettoreForme.isEmpty()) { return new double[] { 0.0, 0.0 }; }

        double sommaX = 0.0;
        double sommaY = 0.0;
        int count = 0;

        for (FormaPersonalizzabile forma : vettoreForme) {
            double[] coordinate = forma.ottieniCoordinate();
            if (coordinate != null && coordinate.length == 2) {
                sommaX += coordinate[0];
                sommaY += coordinate[1];
                count++;
            }
        }

        if (count == 0) {
            return new double[] { 0.0, 0.0 };
        }

        return new double[] { sommaX / count, sommaY / count };
    
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
        double scalaX = dim1 / this.lunghezza;
        double scalaY = dim2 / this.altezza;

        for(int i = 0; i < this.vettoreForme.size(); i++){
            FormaPersonalizzabile forma = this.vettoreForme.get(i);
            if(forma instanceof FormaBidimensionale){
                FormaBidimensionale formaBidimensionale = (FormaBidimensionale) forma;
                double[] dimensioni = formaBidimensionale.ottieniValoriDimensioni();
                formaBidimensionale.modificaDimensioni(dimensioni[0] * scalaX, dimensioni[1] * scalaY);
            } else if (forma instanceof FormaMonodimensionale){
                FormaMonodimensionale formaMonodimensionale = (FormaMonodimensionale) forma;
                double dimensione = formaMonodimensionale.ottieniValoreDimensione();
                formaMonodimensionale.modificaDimensione(dimensione * scalaX);
            }
        }
        
        this.lunghezza = dim1;
        this.altezza = dim2;
    }

    /**
    * Implementazione del metodo per la restituzione dei valori delle dimensioni
    * Vengono fornite in ordine: raggio X e poi raggio Y
    * @return un array di double contenente i valori delle due dimensioni
    */
    @Override
    public double[] ottieniValoriDimensioni() {
        return new double[] { this.lunghezza, this.altezza }; 
    }  
   
}
