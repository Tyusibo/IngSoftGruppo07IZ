package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import com.gruppo07iz.geometrika.forme.Gruppo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.Pane;

public class ComandoSeparazioneGruppo implements CommandInterface {
    private final Model receiver;
    private final Pane padre;
    private final Gruppo gruppoDaSeparare;
    private final Map<FormaPersonalizzabile, Gruppo> mappa;
    
    //Essenziale per l'undo
    private final List<FormaPersonalizzabile> formeSeparate;
    /**
     * Costruttore del comando di separazione gruppo.
     *
     * @param receiver Il modello dell'applicazione.
     * @param padre Il contenitore grafico su cui operano le forme.
     * @param listaForme Le forme da separare dal gruppo.
     * @param mappa La mappa contenente le associazioni tra forme e gruppi.
     */
    public ComandoSeparazioneGruppo(Model receiver, Pane padre, Gruppo gruppoDaSeparare, Map<FormaPersonalizzabile, Gruppo> mappa) {
        this.receiver = receiver;
        this.padre = padre;
        this.gruppoDaSeparare = gruppoDaSeparare;
        this.mappa = mappa;
        
        // Mi serve fare una deep Copy, perchè il comando deve ricordare la lista di forme
        this.formeSeparate = new ArrayList<>(gruppoDaSeparare.getVettoreForme());
    }

    /**
     * Esegue la separazione rimuovendo le forme dalla mappa dei gruppi.
     * Rimuove anche eventuali effetti visivi associati alla selezione del gruppo.
     *
     * @return {@code true} se l'operazione è stata eseguita correttamente, {@code false} se la lista delle forme è vuota o nulla.
     */
    @Override
    public boolean execute() {
        // Rimuovi la relazione dalla mappa e svuota il gruppo
        for (FormaPersonalizzabile forma : formeSeparate) {
            mappa.remove(forma);
        }

        return true;
    }

    /**
     * Annulla la separazione ricreando un nuovo gruppo con le stesse forme e ripristinando la mappa delle associazioni.
     */
    @Override
    public void undo() {
        // Ripristina tutte le forme all'interno dello stesso oggetto gruppo
        for (FormaPersonalizzabile forma : formeSeparate) {
            mappa.put(forma, gruppoDaSeparare);
        }
    }
}
