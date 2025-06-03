package com.gruppo07iz.geometrika.state;

import com.gruppo07iz.geometrika.DashboardController;
import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.command.ComandoSeparazioneGruppo;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import com.gruppo07iz.geometrika.forme.Gruppo;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class StatoSeparazioneGruppo implements StateInterface {

    private final Model modello;
    private final DashboardController context;
    private final Pane lavagna;

    // Forme selezionate da raggruppare
    private Gruppo gruppoSelezionato;



    /**
     * Costruisce lo stato per la separazione dei gruppi.
     *
     * @param modello     Il modello dati condiviso tra view e controller.
     * @param controller  Il controller della dashboard, usato per accedere a utilità comuni e mappa gruppi.
     * @param lavagna     Il pannello grafico (JavaFX Pane) su cui sono disegnate le forme.
     */
    public StatoSeparazioneGruppo(Model modello, DashboardController context, Pane lavagna) {
        this.modello = modello;
        this.context = context;
        this.lavagna = lavagna;
    }

    
    /**
     * Gestisce il clic sinistro del mouse in modalità separazione gruppo.
     * 
     * Se l'elemento cliccato è una {@code FormaPersonalizzabile} appartenente a un gruppo,
     * tutte le forme di quel gruppo vengono selezionate o deselezionate a seconda dello stato
     * attuale di selezione. L'effetto grafico di selezione è gestito tramite {@code DropShadow}.
     *
     * @param event L'evento di clic del mouse.
     */
    @Override
    public void clickSinistro(MouseEvent event) {
        Object target = event.getTarget();

        if (!(target instanceof FormaPersonalizzabile)) {
            context.mostraMessaggioInformativo("Devi cliccare su un gruppo se vuoi separarlo.");
            return;
        }

        FormaPersonalizzabile forma = (FormaPersonalizzabile) target;
        FormaPersonalizzabile gruppo = context.ottieniGruppoPiuEsterno(forma);

        if (!(gruppo instanceof Gruppo)) {
            context.mostraMessaggioInformativo("La figura selezionata non fa parte di un gruppo.");
            return;
        }

        Gruppo nuovoGruppo = (Gruppo) gruppo;

        // Se è già selezionato → deseleziona
        if (nuovoGruppo.equals(gruppoSelezionato)) {
            deselezionaGruppo();
        } else {
            selezionaGruppo(nuovoGruppo);
        }
    }

    private void selezionaGruppo(Gruppo gruppo) {
        deselezionaGruppo(); // rimuovi eventuale selezione precedente
        gruppoSelezionato = gruppo;
        DropShadow evidenzia = new DropShadow(10, Color.RED);
        gruppo.applicaEffetto(evidenzia);
    }

    // La chiamerà anche il controller/context quando farà una cambio di stato
    public void deselezionaGruppo() {
        if (gruppoSelezionato != null) {
            gruppoSelezionato.applicaEffetto(null);
            gruppoSelezionato = null;
        }
    }

    /**
     * Gestisce il clic destro del mouse per confermare la separazione del gruppo.
     *
     * Se almeno una forma è selezionata, viene eseguito il comando di separazione
     * {@code ComandoSeparazioneGruppo}, che rimuove i legami interni tra le forme 
     * selezionate e i rispettivi gruppi.
     *
     * @param event L'evento di click destro (context menu).
     */
    @Override
    public void clickDestro(ContextMenuEvent event) {
        if (gruppoSelezionato == null) {
            context.mostraMessaggioInformativo("Seleziona almeno un gruppo da rimuovere.");
            return;
        }

        
        // Crea comando
        ComandoSeparazioneGruppo comando = new ComandoSeparazioneGruppo(modello, lavagna, gruppoSelezionato, context.mappaFormeGruppi);
        context.eseguiComando(comando);

        this.deselezionaGruppo();

        context.mostraMessaggioInformativo("Gruppo separato con successo.");
    }
    /**
     * Cambia il cursore del mouse in modalità "mano" mentre si è nello stato attivo.
     *
     * @param event L'evento di movimento del mouse.
     */
    @Override
    public void movimentoMouse(MouseEvent event) {
        lavagna.setCursor(javafx.scene.Cursor.HAND);
    }
    @Override
    public void trascinamento(MouseEvent event) {
    }

    @Override
    public void mouseRilasciato(MouseEvent event) {
    }
}
