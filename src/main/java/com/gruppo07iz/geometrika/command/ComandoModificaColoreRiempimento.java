package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import com.gruppo07iz.geometrika.forme.Gruppo;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;


public class ComandoModificaColoreRiempimento implements CommandInterface{
    private final Model receiver;
    private final Color coloreRiempimento;
    private FormaPersonalizzabile formaDaModificare;
    
    // Essenziale per l'undo
    private final List<CommandInterface> listaComandi;

    /**
     * Costruisce il comando per modificare il colore del bordo di tutte le forme nel gruppo.
     *
     * @param receiver Il modello che gestisce la modifica.
     * @param coloreBordo Il nuovo colore da applicare al bordo.
     * @param gruppo Il gruppo di forme da modificare.
     */
    public ComandoModificaColoreRiempimento(Model receiver, FormaPersonalizzabile formaDaModificare, Color coloreRiempimento ) {

        this.receiver = receiver;
        this.coloreRiempimento = coloreRiempimento;
        this.formaDaModificare=formaDaModificare;
        
         this.listaComandi = new ArrayList<>();
    }
    
 
    /**
     * Applica il nuovo colore di bordo a tutte le forme nel gruppo, salvando i colori originali.
     *
     * @return {@code true} se la modifica è stata eseguita correttamente.
     */
    @Override
    public boolean execute() {
        if(formaDaModificare instanceof Gruppo){
            Gruppo gruppo = (Gruppo) formaDaModificare;
            
            // Ottieni il vettore delle forme, ci dovrebbero essere tutte le singole le forme (senza gruppi)
            List<FormaPersonalizzabile> formeDelGruppo = ((Gruppo) gruppo).getTutteLeForme();


            for (FormaPersonalizzabile forma : formeDelGruppo) {
                CommandInterface comando = new ComandoModificaColoreRiempimentoSingolo(receiver, forma, coloreRiempimento);
                if (comando.execute()) {
                    listaComandi.add(comando);
                }
            }
        } else {
            CommandInterface comando = new ComandoModificaColoreRiempimentoSingolo(receiver, formaDaModificare, coloreRiempimento);
                if (comando.execute()) {
                    listaComandi.add(comando);
                }
        }
        
        return true;
    }

    /**
     * Annulla la modifica, ripristinando il colore originale del bordo per ciascuna forma.
     */
    @Override
    public void undo() {
        for (int i = listaComandi.size() - 1; i >= 0; i--) {
            listaComandi.get(i).undo();
        }
    }
}


