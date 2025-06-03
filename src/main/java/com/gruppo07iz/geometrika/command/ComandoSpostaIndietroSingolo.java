package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;


public class ComandoSpostaIndietroSingolo implements CommandInterface{
    private final Model receiver;
    private final FormaPersonalizzabile forma;
    private final Pane padre;
    // Essenziale per l'undo
    private final int posizioneIniziale;


    /**
     * Costruttore del comando.
     *
     * @param receiver Il modello che gestisce le operazioni sulle forme.
     * @param forma La forma da spostare indietro nel contenitore.
     */
    public ComandoSpostaIndietroSingolo(Model receiver, Pane padre, FormaPersonalizzabile forma) {
        this.receiver = receiver;
        this.forma = forma;
        this.padre = padre; 
        
        this.posizioneIniziale = padre.getChildren().indexOf(forma);
    }

    /**
     * Esegue lo spostamento indietro della forma nell'ordine dei nodi del Pane.
     * 
     * @return true se la forma è stata spostata; false se era già in ultima posizione.
     */
    @Override
    public boolean execute() {
        // se è nella prima posizione (0) è già quello indietro
        ObservableList<Node> figli = padre.getChildren();
        if (figli.get(0) == forma){
            return false;
        } else {
            receiver.spostaIndietro(forma);
            return true;
        }
    }

    /**
     * Annulla lo spostamento riportando la forma alla posizione iniziale.
     */
    @Override
    public void undo() {
        receiver.spostaPosizioneIniziale(padre, forma, posizioneIniziale);
    }
}
