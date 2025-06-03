package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class ComandoSpostaAvantiSingolo implements CommandInterface {
    private final Model receiver;
    private final FormaPersonalizzabile forma;
    private final Pane padre;
    // Essenziale per l'undo
    private final int posizioneIniziale;


    /**
     * Costruttore del comando di spostamento avanti.
     * 
     * @param receiver Il modello dell'applicazione.
     * @param forma La forma da spostare avanti.
     */
    public ComandoSpostaAvantiSingolo(Model receiver, Pane padre, FormaPersonalizzabile forma) {
        this.receiver = receiver;
        this.forma = forma;
        this.padre = padre;
        
        this.posizioneIniziale = padre.getChildren().indexOf(forma);
    }

    /**
     * Esegue il comando spostando la forma in avanti (incrementando il suo indice nella lista dei nodi).
     * 
     * @return {@code true} se lo spostamento è stato effettuato, {@code false} se la forma era già in primo piano.
     */
    @Override
    public boolean execute() {
        // se è nell' ultima posizione è già quello avanti
        ObservableList<Node> figli = padre.getChildren();
        if (figli.get(figli.size() - 1) == forma){
            return false;
        } else {
            receiver.spostaAvanti(forma);
            return true;
        }
    }

    /**
     * Annulla lo spostamento riportando la forma alla posizione iniziale nella lista dei figli del Pane.
     */
    @Override
    public void undo() {
        receiver.spostaPosizioneIniziale(padre, forma, posizioneIniziale);
    }
}
