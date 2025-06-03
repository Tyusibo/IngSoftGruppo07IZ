package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaBidimensionale;


public class ComandoModificaDimensioni implements CommandInterface {

    private final Model receiver;
    private final FormaBidimensionale forma;
    private final double dimensione1;
    private final double dimensione2;
    // Essenziali per l'undo
    private final double dimensioneVecchia1;
    private final double dimensioneVecchia2;
    
    /**
     * Costruisce un comando per modificare due dimensioni di una forma bidimensionale,
     * salvando le dimensioni precedenti per il ripristino tramite {@code undo()}.
     *
     * @param receiver Il modello che gestisce l'applicazione della modifica.
     * @param forma La forma da modificare.
     * @param dimensione1 La nuova prima dimensione (es. larghezza o scala X).
     * @param dimensione2 La nuova seconda dimensione (es. altezza o scala Y).
     */
    public ComandoModificaDimensioni(Model receiver, FormaBidimensionale forma, double dimensione1, double dimensione2) {
        this.receiver = receiver;
        this.forma = forma;
        this.dimensione1 = dimensione1;
        this.dimensione2 = dimensione2;
        
        double[] dimensioni = forma.ottieniValoriDimensioni();
        this.dimensioneVecchia1 = dimensioni[0];
        this.dimensioneVecchia2 = dimensioni[1];
    }
    
    /**
     * Restituisce la prima dimensione originale prima della modifica.
     *
     * @return Valore della dimensione 1 precedente.
     */
    public double getDimensioneVecchia1(){
        return this.dimensioneVecchia1;
    }
    
    /**
     * Restituisce la seconda dimensione originale prima della modifica.
     *
     * @return Valore della dimensione 2 precedente.
     */
    public double getDimensioneVecchia2(){
        return this.dimensioneVecchia2;
    }

    /**
     * Restituisce la nuova prima dimensione da applicare.
     *
     * @return Nuova dimensione 1.
     */
    public double getDimensione1(){
        return this.dimensione1;
    }
    
    /**
     * Restituisce la nuova seconda dimensione da applicare.
     *
     * @return Nuova dimensione 2.
     */
    public double getDimensione2(){
        return this.dimensione2;
    }
    
    /**
     * Esegue la modifica delle dimensioni della forma se sono diverse da quelle originali.
     *
     * @return {@code true} se la modifica è stata applicata, {@code false} se non necessaria.
     */
    @Override
    public boolean execute() {
        // Solo se le nuove dimensioni sono cambiate eseguo la modifica
        if(dimensione1 == dimensioneVecchia1 && dimensione2 == dimensioneVecchia2) { return false; }
        receiver.modificaDimensioni(forma, dimensione1, dimensione2);
        return true;
    }
    
    /**
     * Annulla la modifica ripristinando le dimensioni originali della forma.
     */
    @Override
    public void undo() {
        receiver.modificaDimensioni(forma, dimensioneVecchia1, dimensioneVecchia2);
    }
    
}
