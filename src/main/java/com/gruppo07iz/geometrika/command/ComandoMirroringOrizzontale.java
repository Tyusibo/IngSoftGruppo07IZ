package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;


public class ComandoMirroringOrizzontale implements CommandInterface {
    private final Model receiver;
    private final FormaPersonalizzabile forma;
       
    /**
     * Costruisce un comando per applicare il mirroring orizzontale su una forma specifica.
     *
     * @param receiver Il modello che esegue l'operazione sul dato.
     * @param forma La forma grafica su cui applicare il mirroring.
     */
    public ComandoMirroringOrizzontale(Model receiver, FormaPersonalizzabile forma) {
        this.receiver = receiver;
        this.forma = forma;
    }
    
    /**
     * Esegue il mirroring orizzontale della forma.
     *
     * @return {@code true} se l'operazione è stata eseguita correttamente.
     */
    @Override
    public boolean execute() {
        this.receiver.mirroringOrizzontale(forma);
        return true;
    }

    /**
     * Annulla il mirroring orizzontale riapplicando la trasformazione, poiché è un'operazione simmetrica.
     */
    @Override
    public void undo() {
        this.receiver.mirroringOrizzontale(forma);
    }
    
}

