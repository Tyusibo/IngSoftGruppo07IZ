package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

public class ComandoEliminazione implements CommandInterface{
    private final Model receiver;
    private final Pane padre;             
    private final Shape formaDaEliminare;
    
    /**
     * Costruisce un comando di eliminazione per una forma specifica.
     * 
     * @param receiver Il modello che gestisce la logica dell'applicazione.
     * @param padre Il contenitore grafico (pane) da cui eliminare la forma.
     * @param formaDaEliminare La forma da eliminare.
     */
    public ComandoEliminazione(Model receiver, Pane padre, Shape formaDaEliminare) {
        this.receiver = receiver;
        this.padre = padre;
        this.formaDaEliminare = formaDaEliminare;
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
        this.receiver.aggiungiForma(padre, formaDaEliminare);
    }
    
}
