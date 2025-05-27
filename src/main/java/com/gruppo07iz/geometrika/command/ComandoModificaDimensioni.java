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
     * Costruisce il comando di modifica dimensioni salvando le dimensioni vecchie e nuove.
     *
     * @param receiver Il modello che esegue le modifiche.
     * @param forma La forma bidimensionale da modificare.
     * @param dimensione1 Nuova dimensione 1 (ad esempio larghezza o scala X).
     * @param dimensione2 Nuova dimensione 2 (ad esempio altezza o scala Y).
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
     * Restituisce la dimensione vecchia 1.
     * @return dimensione vecchia 1
     */
    public double getDimensioneVecchia1(){
        return this.dimensioneVecchia1;
    }
    
    /**
     * Restituisce la dimensione vecchia 2.
     * @return dimensione vecchia 2
     */
    public double getDimensioneVecchia2(){
        return this.dimensioneVecchia2;
    }

    /**
     * Esegue la modifica delle dimensioni se sono diverse da quelle attuali.
     * 
     * @return true se la modifica è stata effettuata, false altrimenti.
     */
    public double getDimensione1(){
        return this.dimensione1;
    }
    
    public double getDimensione2(){
        return this.dimensione2;
    }
    
    @Override
    public boolean execute() {
        // Solo se le nuove dimensioni sono cambiate eseguo la modifica
        if(dimensione1 == dimensioneVecchia1 && dimensione2 == dimensioneVecchia2) { return false; }
        receiver.modificaDimensioni(forma, dimensione1, dimensione2);
        return true;
    }
    
    /**
     * Annulla la modifica riportando le dimensioni vecchie.
     */
    @Override
    public void undo() {
        receiver.modificaDimensioni(forma, dimensioneVecchia1, dimensioneVecchia2);
    }
    
}
