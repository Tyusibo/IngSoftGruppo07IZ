package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

public class ComandoIncolla implements CommandInterface{
    private final Model receiver;
    private final Pane lavagna;
    private final Shape formaCopiata;
    private final double coordinataX;
    private final double coordinataY;

    /**
     * Costruisce un comando di incolla per una forma specifica.
     * 
     * @param receiver Il modello che gestisce la logica dell'applicazione.
     * @param lavagna Il pannello grafico su cui incollare la forma.
     * @param formaCopiata La forma da incollare (deve essere una copia distinta).
     * @param coordinataX La coordinata X in cui incollare la forma.
     * @param coordinataY La coordinata Y in cui incollare la forma.
     */
    public ComandoIncolla(Model receiver, Pane lavagna, Shape formaCopiata, double coordinataX, double coordinataY) {
        this.receiver = receiver;
        this.lavagna = lavagna;
        this.formaCopiata = formaCopiata;
        this.coordinataX = coordinataX;
        this.coordinataY = coordinataY;
    }

    /**
     * Esegue il comando incollando la forma sulla lavagna nella posizione specificata.
     * 
     * @return true se l'operazione è stata eseguita con successo.
     */
    @Override
    public boolean execute() {
        receiver.incollaForma(lavagna, formaCopiata, coordinataX, coordinataY);
        return true;
    }

    /**
     * Annulla il comando eliminando la forma incollata dalla lavagna.
     */
    @Override
    public void undo() {
        receiver.eliminaForma(lavagna, formaCopiata);
    }
    
}
