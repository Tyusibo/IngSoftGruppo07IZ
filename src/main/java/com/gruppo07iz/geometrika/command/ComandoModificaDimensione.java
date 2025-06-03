package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaMonodimensionale;


public class ComandoModificaDimensione implements CommandInterface {

    private final Model receiver;
    private final FormaMonodimensionale forma;
    private final double dimensione;
    // Essenziale per l'undo
    private final double dimensioneVecchia;
    

    /**
     * Costruisce un comando per modificare la dimensione di una forma monodimensionale.
     *
     * @param receiver Il modello responsabile dell'applicazione della modifica.
     * @param forma La forma da modificare.
     * @param dimensione La nuova dimensione da applicare.
     */
    public ComandoModificaDimensione(Model receiver, FormaMonodimensionale forma, double dimensione) {
        this.receiver = receiver;
        this.forma = forma;
        this.dimensione = dimensione;
        
        this.dimensioneVecchia = forma.ottieniValoreDimensione();
    }
    
    /**
     * Restituisce la dimensione precedente alla modifica.
     *
     * @return La dimensione originale.
     */
    public double getDimensioneVecchia(){
        return this.dimensioneVecchia;
    }

    /**
     * Restituisce la nuova dimensione da applicare alla forma.
     *
     * @return La nuova dimensione.
     */
    public double getDimensione(){
        return this.dimensione;
    }
    
    /**
     * Esegue la modifica della dimensione, solo se è diversa da quella originale.
     *
     * @return {@code true} se la modifica è stata eseguita, {@code false} se non necessaria.
     */
    @Override
    public boolean execute() {
        // Solo se la nuova dimensione è cambiata eseguo la modifica
        if(dimensione == dimensioneVecchia) { return false; }
        receiver.modificaDimensione(forma, dimensione);
        return true;
    }

    /**
     * Annulla la modifica ripristinando la dimensione originale della forma.
     */
    @Override
    public void undo() {
        receiver.modificaDimensione(forma, dimensioneVecchia);
    }
    
}
