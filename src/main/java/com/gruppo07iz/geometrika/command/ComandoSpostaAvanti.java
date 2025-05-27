package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

public class ComandoSpostaAvanti implements CommandInterface {
    private final Model receiver;
    private final Shape forma;
    private final Pane padre;
    // Essenziale per l'undo
    private final int posizioneIniziale;


    /**
     * Costruttore del comando.
     * 
     * @param receiver L'oggetto modello che gestisce le operazioni sulle forme.
     * @param forma La forma da spostare avanti.
     */
    public ComandoSpostaAvanti(Model receiver, Shape forma) {
        this.receiver = receiver;
        this.forma = forma;
        this.padre = (Pane) forma.getParent(); 
        
        this.posizioneIniziale = padre.getChildren().indexOf(forma);
    }

    /**
     * Esegue lo spostamento avanti della forma nell'ordine dei nodi del Pane.
     * Se la forma è già nell'ultima posizione (in primo piano), non fa nulla.
     * 
     * @return true se lo spostamento è stato effettuato, false se la forma era già in ultima posizione.
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
     * Annulla lo spostamento riportando la forma alla posizione iniziale nella lista dei nodi.
     */
    @Override
    public void undo() {
        receiver.spostaPosizioneIniziale(padre, forma, posizioneIniziale);
    }
}
