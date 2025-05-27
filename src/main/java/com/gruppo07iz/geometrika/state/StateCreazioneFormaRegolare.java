package com.gruppo07iz.geometrika.state;

import com.gruppo07iz.geometrika.DashboardController;
import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.TipoFormaRegolare;
import com.gruppo07iz.geometrika.command.ComandoCreazioneFormaRegolare;
import com.gruppo07iz.geometrika.command.CommandInterface;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class StateCreazioneFormaRegolare implements StateInterface{
    private final Model modello;
    private final DashboardController controller;
    private final Pane lavagna;
    
    private TipoFormaRegolare formaDaCreare;
    private final ColorPicker coloreRiempimentoCreazione;
    private final ColorPicker coloreBordoCreazione;

    /**
     * Costruttore dello stato di creazione forma regolare.
     *
     * @param lavagna Il pannello su cui disegnare la forma.
     * @param formaDaCreare Il tipo di forma regolare da creare.
     * @param modello Il modello contenente la logica applicativa.
     * @param coloreRiempimentoCreazione Il selettore del colore di riempimento.
     * @param coloreBordoCreazione Il selettore del colore del bordo.
     * @param controller Il controller della dashboard per la gestione dei comandi.
     */
    public StateCreazioneFormaRegolare(Model modello, DashboardController controller, Pane lavagna, TipoFormaRegolare formaDaCreare, ColorPicker coloreRiempimentoCreazione, ColorPicker coloreBordoCreazione) {
        this.modello = modello;
        this.controller = controller;
        this.lavagna = lavagna;
        
        this.formaDaCreare = formaDaCreare;
        this.coloreRiempimentoCreazione = coloreRiempimentoCreazione;
        this.coloreBordoCreazione = coloreBordoCreazione; 
    }
    
    /**
     * Imposta il tipo di forma regolare da creare.
     *
     * @param formaDaCreare Il nuovo tipo di forma regolare.
     */
    public void setFormaDaCreare(TipoFormaRegolare formaDaCreare){
        this.formaDaCreare = formaDaCreare;
    }
    
    /**
     * Gestisce il click sinistro per creare una nuova forma regolare.
     * La forma viene creata alle coordinate del click con i colori selezionati.
     *
     * @param event Evento di click sinistro.
     */ 
    @Override
    public void clickSinistro(MouseEvent event){
        // Ottengo le coordinate di dove è stato effettuato il click per poter generare la forma
        double inizioX = event.getX();
        double inizioY = event.getY();

        // Delego la responsabilità di creazione al comando, fornisco coordinate e colori
        // Le dimensioni sono di default
        CommandInterface comando = new ComandoCreazioneFormaRegolare(modello, lavagna,
                formaDaCreare, inizioX, inizioY,
                coloreRiempimentoCreazione.getValue(), coloreBordoCreazione.getValue());

        controller.eseguiComando(comando);
    }
    /**
     * Gestisce il click destro. Non utilizzato in questo stato.
     *
     * @param event Evento di click destro.
     */
    @Override
    public void clickDestro(ContextMenuEvent event) {}
    /**
     * Gestisce l'evento di trascinamento. Non utilizzato in questo stato.
     *
     * @param event Evento di trascinamento del mouse.
     */
    @Override
    public void trascinamento(MouseEvent event) {}

    /**
     * Gestisce il rilascio del mouse. Non utilizzato in questo stato.
     *
     * @param event Evento di rilascio del mouse.
     */
    @Override
    public void mouseRilasciato(MouseEvent event) {}

    /**
     * Gestisce il movimento del mouse. Non utilizzato in questo stato.
     *
     * @param event Evento di movimento del mouse.
     */
    @Override
    public void movimentoMouse(MouseEvent event) {
        //this.lavagna.setCursor(javafx.scene.Cursor.CROSSHAIR);
    }
    
}
