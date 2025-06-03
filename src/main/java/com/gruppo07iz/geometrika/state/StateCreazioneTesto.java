package com.gruppo07iz.geometrika.state;

import com.gruppo07iz.geometrika.DashboardController;
import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.command.ComandoCreazioneTesto;
import com.gruppo07iz.geometrika.command.CommandInterface;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;


public class StateCreazioneTesto implements StateInterface{
    private final Model modello;
    private final DashboardController controller;
    private final Pane lavagna;
    
    private final TextField testo;
    private final ColorPicker coloreRiempimentoCreazione;
    private final ColorPicker coloreBordoCreazione;
    private final Spinner<Double> dimensione;

    /**
     * Costruttore dello stato di creazione testo.
     *
     * @param lavagna Il pannello su cui disegnare la forma.
     * @param formaDaCreare Il tipo di forma regolare da creare.
     * @param modello Il modello contenente la logica applicativa.
     * @param coloreRiempimentoCreazione Il selettore del colore di riempimento.
     * @param coloreBordoCreazione Il selettore del colore del bordo.
     * @param controller Il controller della dashboard per la gestione dei comandi.
     */
    public StateCreazioneTesto(Model modello, DashboardController controller, Pane lavagna, TextField testo, ColorPicker coloreRiempimentoCreazione, ColorPicker coloreBordoCreazione,Spinner<Double> dimensione) {
        this.modello = modello;
        this.controller = controller;
        this.lavagna = lavagna;
        
        this.testo=testo;
        this.coloreRiempimentoCreazione = coloreRiempimentoCreazione;
        this.coloreBordoCreazione = coloreBordoCreazione; 
        this.dimensione=dimensione;
        
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
        dimensione.increment(0);
            
        String contenutoTesto = testo.getText().trim();
        if (contenutoTesto.isEmpty()) {
            controller.mostraMessaggioInformativo("Inserisci del testo prima di posizionarlo sulla lavagna.");
            return; 
        }
        
        // Delego la responsabilità di creazione al comando, fornisco coordinate e colori
        CommandInterface comando = new ComandoCreazioneTesto(modello, lavagna,
                testo.getText(), inizioX, inizioY,
                coloreRiempimentoCreazione.getValue(), coloreBordoCreazione.getValue(), dimensione.getValue());

        controller.eseguiComando(comando);
        testo.setText("");
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
        lavagna.setCursor(javafx.scene.Cursor.DEFAULT);
    }
}
