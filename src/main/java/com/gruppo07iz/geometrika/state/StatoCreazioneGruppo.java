package com.gruppo07iz.geometrika.state;

import com.gruppo07iz.geometrika.DashboardController;
import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.command.ComandoCreazioneGruppo;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class StatoCreazioneGruppo implements StateInterface {
    private final Model modello;
    private final DashboardController context;
    private final Pane lavagna;

    // Forme selezionate da raggruppare
    private final List<FormaPersonalizzabile> listaForme;

    /**
     * Costruisce un nuovo stato per la creazione di un gruppo di forme,
     * inizializzando il modello, il controller della dashboard e la lavagna.
     * Prepara inoltre una lista vuota per le forme che faranno parte del gruppo.
     *
     * @param modello  il modello dati che gestisce le forme e la logica dell'applicazione
     * @param context  il controller della dashboard che gestisce il contesto operativo
     * @param lavagna  il pannello grafico (pane) su cui vengono disegnate le forme
     */
    public StatoCreazioneGruppo(Model modello, DashboardController context, Pane lavagna) {
        this.modello = modello;
        this.context = context;
        this.lavagna = lavagna;
        this.listaForme = new ArrayList<>();
    }

    /**
     * Svuota la lista delle forme attualmente selezionate per la creazione del gruppo.
     * Questo metodo viene utilizzato per annullare o resettare la selezione corrente.
     */
    public void svuotaListaForme(){
        this.listaForme.clear();
    }
    /**
     * Click sinistro: seleziona una forma da aggiungere al gruppo
     */
    @Override
    public void clickSinistro(MouseEvent event) {
        Object target = event.getTarget();

        // Verifica che sia una forma personalizzabile
        if (!(target instanceof FormaPersonalizzabile)) {
            context.mostraMessaggioInformativo("Devi cliccare sulle forme se vuoi fare un gruppo.");
            return;
        }

        FormaPersonalizzabile forma = (FormaPersonalizzabile) target;
        // Ottiene il gruppo più esterno della forma (se presente)
        FormaPersonalizzabile gruppoSelezionato = context.ottieniGruppoPiuEsterno(forma);

        this.toggleSelezione(gruppoSelezionato);
        
    }

    /**
     * Aggiunge o rimuove la forma dalla lista e aggiorna l'effetto visivo.
     */
    private void toggleSelezione(FormaPersonalizzabile forma) {
        if (!listaForme.contains(forma)) {
            listaForme.add(forma);
            DropShadow evidenziaEffetto = new DropShadow(10, Color.RED);
            forma.applicaEffetto(evidenziaEffetto);
        } else {
            forma.applicaEffetto(null);
            listaForme.remove(forma);
        }
    }
    /**
     * Click destro: conferma e crea il gruppo
     */
    @Override
    public void clickDestro(ContextMenuEvent event) {
        if (listaForme.isEmpty() || listaForme.size() == 1) {
            context.mostraMessaggioInformativo("Seleziona almeno due forma prima di creare un gruppo.");
            return;
        }
        
        ComandoCreazioneGruppo comando = new ComandoCreazioneGruppo(modello, lavagna, listaForme, context.mappaFormeGruppi);
        context.eseguiComando(comando);


        for (FormaPersonalizzabile f : listaForme) {
            f.applicaEffetto(null);
        }

        listaForme.clear();
        context.mostraMessaggioInformativo("Gruppo creato con successo.");
    }
    /**
     * Cambia il cursore del mouse in modalità "mano" mentre si è nello stato attivo.
     *
     * @param event L'evento di movimento del mouse.
     */
    @Override public void movimentoMouse(MouseEvent event) {
        lavagna.setCursor(javafx.scene.Cursor.HAND);
    }
    @Override public void trascinamento(MouseEvent event) {}
    @Override public void mouseRilasciato(MouseEvent event) {}
}
