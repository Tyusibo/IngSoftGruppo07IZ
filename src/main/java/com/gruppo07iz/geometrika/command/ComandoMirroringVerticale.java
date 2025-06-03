package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;


public class ComandoMirroringVerticale implements CommandInterface {
    private final Model receiver;
    private final FormaPersonalizzabile forma;
       
    /**
     * Costruisce un comando per applicare il mirroring verticale su una forma.
     *
     * @param receiver Il modello responsabile dell’esecuzione della trasformazione.
     * @param forma La forma da trasformare.
     */
    public ComandoMirroringVerticale(Model receiver, FormaPersonalizzabile forma) {
        this.receiver = receiver;
        this.forma = forma;
    }
    
    /**
     * Esegue il mirroring verticale della forma.
     *
     * @return {@code true} se l’operazione è stata eseguita correttamente.
     */
    @Override
    public boolean execute() {
        this.receiver.mirroringVerticale(forma);
        return true;
    }

    /**
     * Annulla il mirroring verticale riapplicando la stessa trasformazione.
     * Poiché il mirroring è simmetrico, il secondo applicativo annulla il primo.
     */
    @Override
    public void undo() {
        this.receiver.mirroringVerticale(forma);
    }
    
}

