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
     * Costruisce il comando salvando la dimensione vecchia e la nuova.
     * 
     * @param receiver Il modello che esegue le modifiche.
     * @param forma La forma monodimensionale da modificare.
     * @param dimensione La nuova dimensione da applicare.
     */
    public ComandoModificaDimensione(Model receiver, FormaMonodimensionale forma, double dimensione) {
        this.receiver = receiver;
        this.forma = forma;
        this.dimensione = dimensione;
        
        this.dimensioneVecchia = forma.ottieniValoreDimensione();
    }
    
    /**
     * Restituisce la dimensione vecchia.
     * 
     * @return La dimensione precedente alla modifica.
     */
    public double getDimensioneVecchia(){
        return this.dimensioneVecchia;
    }

    /**
     * Esegue la modifica della dimensione solo se la nuova è differente dalla vecchia.
     * 
     * @return true se la modifica è stata effettuata, false altrimenti.
     */
    public double getDimensione(){
        return this.dimensione;
    }
    
    @Override
    public boolean execute() {
        // Solo se la nuova dimensione è cambiata eseguo la modifica
        if(dimensione == dimensioneVecchia) { return false; }
        receiver.modificaDimensione(forma, dimensione);
        return true;
    }

    /**
     * Annulla la modifica ripristinando la dimensione vecchia.
     */
    @Override
    public void undo() {
        receiver.modificaDimensione(forma, dimensioneVecchia);
    }
    
}
