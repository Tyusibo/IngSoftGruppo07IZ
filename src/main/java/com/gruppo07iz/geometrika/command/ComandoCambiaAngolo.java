package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;

public class ComandoCambiaAngolo implements CommandInterface{
    private final Model receiver;
    private final FormaPersonalizzabile forma;
    private final double angolo;
    // Essenziale per l'undo
    private final double incremento;
    

    /**
     * Costruisce un comando per cambiare l'angolo di rotazione di una forma.
     *
     * @param receiver Il modello che gestisce la logica dell'applicazione.
     * @param forma La forma di cui modificare l'angolo di rotazione.
     * @param angolo Il nuovo angolo di rotazione da impostare (in gradi).
     */
    public ComandoCambiaAngolo(Model receiver, FormaPersonalizzabile forma, double angolo) {
        this.receiver = receiver;
        this.forma = forma;
        this.angolo = angolo;
        
        double angoloVecchio = forma.getAngolo();
        this.incremento = angolo - angoloVecchio;
    }
    
    /**
     * Esegue il comando modificando l'angolo di rotazione della forma.
     * L'esecuzione avviene solo se il nuovo angolo è diverso da quello corrente.
     *
     * @return true se l'angolo è stato modificato, false altrimenti.
     */
    public double getAngolo(){
        return this.angolo;
    }

    /**
     * Esegue il comando per modificare l'angolo di rotazione della forma.
     *
     * <La modifica viene applicata solo se il nuovo angolo {@code angolo}
     * è diverso da quello precedente {@code angoloVecchio}, per evitare operazioni ridondanti.
     *
     * @return {@code true} se l'angolo è stato effettivamente modificato,
     *         {@code false} se l'angolo non è cambiato e quindi nessuna azione è stata eseguita.
     *
     * @see com.gruppo07iz.geometrika.command.CommandInterface
     */
    @Override
    public boolean execute() {
        // Solo se il nuovo angolo è cambiato eseguo la modifica
        if(incremento == 0) { return false; }
        receiver.modificaAngolo(forma, incremento);
        return true;
    }

    /**
     * Annulla il comando ripristinando l'angolo di rotazione originale della forma.
     */
    @Override
    public void undo() {
        receiver.modificaAngolo(forma, -1 * incremento);
    }
    
}

