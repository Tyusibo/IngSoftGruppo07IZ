package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import com.gruppo07iz.geometrika.forme.Gruppo;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;


public class ComandoSpostaAvanti implements CommandInterface{
    private final Model receiver;
    private final Pane padre;
    private final FormaPersonalizzabile formaDaSpostare;

    // Essenziale per l'undo
    private final List<CommandInterface> listaComandi;

    /**
     * Costruttore del comando.
     * 
     * @param receiver Il modello dell'applicazione.
     * @param gruppo Il gruppo di forme da spostare in avanti.
     * @param padre Il contenitore grafico (Pane) su cui si trovano le forme.
     */
    public ComandoSpostaAvanti(Model receiver, Pane padre, FormaPersonalizzabile formaDaSpostare) {
        this.receiver = receiver;
        this.formaDaSpostare = formaDaSpostare;
        this.padre = padre; 
        
        this.listaComandi = new ArrayList<>();
    }

    /**
     * Esegue lo spostamento avanti di ogni forma del gruppo, se non è già in primo piano.
     * 
     * @return true se almeno una forma è stata spostata; false se tutte erano già in primo piano.
     */
    @Override
    public boolean execute() {
        if(formaDaSpostare instanceof Gruppo){
            Gruppo gruppo = (Gruppo) formaDaSpostare;
            
            // Ottieni il vettore delle forme, ci dovrebbero essere tutte le singole le forme (senza gruppi)
            List<FormaPersonalizzabile> formeDelGruppo = ((Gruppo) gruppo).getTutteLeForme();

            // Ordina le forme in base all'indice nel parent, in ordine decrescente
            formeDelGruppo.sort((f1, f2) -> {
                int index1 = padre.getChildren().indexOf(f1);
                int index2 = padre.getChildren().indexOf(f2);
                return Integer.compare(index1, index2); // decrescente/crescente
            });


            for (FormaPersonalizzabile forma : formeDelGruppo) {
                CommandInterface comando = new ComandoSpostaAvantiSingolo(receiver, padre, forma);
                if (comando.execute()) {
                    listaComandi.add(comando);
                }
            }
        } else {
            CommandInterface comando = new ComandoSpostaAvantiSingolo(receiver, padre, formaDaSpostare);
                if (comando.execute()) {
                    listaComandi.add(comando);
                }
        }
        
        return true;
    }

    /**
     * Annulla lo spostamento riportando ogni forma del gruppo alla sua posizione originale nel Pane.
     */
    @Override
    public void undo() {
        for (int i = listaComandi.size() - 1; i >= 0; i--) {
            listaComandi.get(i).undo();
        }
    }
}
