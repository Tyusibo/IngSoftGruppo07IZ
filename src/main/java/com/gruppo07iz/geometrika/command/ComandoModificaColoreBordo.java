package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import com.gruppo07iz.geometrika.forme.Gruppo;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;


public class ComandoModificaColoreBordo implements CommandInterface{
    private final Model receiver;
    private final Color coloreBordo;
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
    public ComandoModificaColoreBordo(Model receiver, FormaPersonalizzabile formaDaModificare, Color coloreBordo ) {

        this.receiver = receiver;
        this.coloreBordo = coloreBordo;
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
                CommandInterface comando = new ComandoModificaColoreBordoSingolo(receiver, forma, coloreBordo);
                if (comando.execute()) {
                    listaComandi.add(comando);
                }
            }
        } else {
            CommandInterface comando = new ComandoModificaColoreBordoSingolo(receiver, formaDaModificare, coloreBordo);
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


