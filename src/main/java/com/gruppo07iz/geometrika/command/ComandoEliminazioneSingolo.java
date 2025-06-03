package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import javafx.scene.layout.Pane;

public class ComandoEliminazioneSingolo implements CommandInterface{
    private final Model receiver;
    private final Pane padre;             
    private final FormaPersonalizzabile formaDaEliminare;
    
    // Essenziale per l'undo
    private final int posizione;
    
    /**
     * Costruisce un comando di eliminazione per una forma specifica.
     * 
     * @param receiver Il modello che gestisce la logica dell'applicazione.
     * @param padre Il contenitore grafico (pane) da cui eliminare la forma.
     * @param formaDaEliminare La forma da eliminare.
     */
    public ComandoEliminazioneSingolo(Model receiver, Pane padre, FormaPersonalizzabile formaDaEliminare) {
        this.receiver = receiver;
        this.padre = padre;
        this.formaDaEliminare = formaDaEliminare;
        
        posizione = padre.getChildren().indexOf(formaDaEliminare);
    }

    /**
     * Esegue il comando eliminando la forma dal contenitore.
     * 
     * @return true se l'operazione è stata eseguita con successo.
     */
    @Override
    public boolean execute() {
        if (this.formaDaEliminare == null || this.padre == null) {
            throw new IllegalArgumentException("Forma da eliminare o contenitore non possono essere nulli.");
        }
        if (!padre.getChildren().contains(formaDaEliminare)) {
        throw new IllegalArgumentException("La forma da eliminare non è presente nel contenitore.");
        }
        
        this.receiver.eliminaForma(padre, formaDaEliminare);
        return true;
    }

    /**
     * Annulla il comando aggiungendo nuovamente la forma al contenitore.
     */
    @Override
    public void undo() {
            this.receiver.aggiungiForma(padre, formaDaEliminare, this.posizione);
        }  
}
