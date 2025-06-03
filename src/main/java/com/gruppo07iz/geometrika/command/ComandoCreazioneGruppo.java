package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import com.gruppo07iz.geometrika.forme.Gruppo;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.Pane;


public class ComandoCreazioneGruppo implements CommandInterface{
    private final Model receiver;
    private final Pane padre; 
    private final List<FormaPersonalizzabile> listaForme;
    private final Map<FormaPersonalizzabile, Gruppo> mappa;
    
    // Essenziale per l'undo
    private Gruppo gruppoCreato;

    /**
     * Costruisce un comando per creare un gruppo logico di forme personalizzabili.
     *
     * @param receiver Il modello che gestisce la logica dell'applicazione.
     * @param padre Il contenitore grafico su cui operano le forme (non usato direttamente nel gruppo logico).
     * @param listaForme L'elenco delle forme da raggruppare.
     * @param mappa La mappa che associa ciascuna forma al proprio gruppo.
     */
    public ComandoCreazioneGruppo(Model receiver, Pane padre, List<FormaPersonalizzabile> listaForme, Map<FormaPersonalizzabile, Gruppo> mappa) {
        this.receiver = receiver;
        this.padre = padre;
        this.listaForme=listaForme;
        this.mappa = mappa;
    }

    /**
     * Esegue il comando creando un nuovo gruppo logico contenente le forme specificate.
     *
     * <p>Se la lista di forme è nulla o vuota, non viene effettuata alcuna operazione.</p>
     *
     * @return {@code true} se il gruppo è stato creato correttamente, {@code false} altrimenti.
     */
    @Override
    public boolean execute() {  
        if (listaForme == null || listaForme.isEmpty()) return false;

        gruppoCreato = new Gruppo();
        for (FormaPersonalizzabile forma : listaForme) {
            gruppoCreato.aggiungiForma(forma);
            mappa.put(forma, gruppoCreato); 
        }

        return true;
    }

    /**
     * Annulla la creazione del gruppo, rimuovendo tutte le associazioni dalla mappa
     * e svuotando il gruppo creato.
     */
    @Override
    public void undo() {
        for(FormaPersonalizzabile f : gruppoCreato.getVettoreForme()){
            mappa.remove(f);
        }
        mappa.remove(gruppoCreato);
        gruppoCreato = null;
    }
    public Gruppo getGruppoCreato(){
        return gruppoCreato;
    }
}
