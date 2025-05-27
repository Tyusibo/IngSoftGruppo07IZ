package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;


public class ComandoSpostaIndietro implements CommandInterface{
    private final Model receiver;
    private final Shape forma;
    private final Pane padre;
    // Essenziale per l'undo
    private final int posizioneIniziale;


    /**
     * Costruttore del comando.
     * 
     * @param receiver L'oggetto modello che gestisce le operazioni sulle forme.
     * @param forma La forma da spostare indietro.
     */
    public ComandoSpostaIndietro(Model receiver, Shape forma) {
        this.receiver = receiver;
        this.forma = forma;
        this.padre = (Pane) forma.getParent(); 
        
        this.posizioneIniziale = padre.getChildren().indexOf(forma);
    }

    /**
     * Esegue lo spostamento indietro della forma nell'ordine dei nodi del Pane.
     * Se la forma è già nella prima posizione (dietro a tutti), non fa nulla.
     * 
     * @return true se lo spostamento è stato effettuato, false se la forma era già in prima posizione.
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
     * Annulla lo spostamento riportando la forma alla posizione iniziale nella lista dei nodi.
     */
    @Override
    public void undo() {
        receiver.spostaPosizioneIniziale(padre, forma, posizioneIniziale);
    }
}
