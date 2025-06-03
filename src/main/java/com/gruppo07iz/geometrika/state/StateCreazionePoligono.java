package com.gruppo07iz.geometrika.state;

import com.gruppo07iz.geometrika.DashboardController;
import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.command.ComandoCreazionePoligono;
import com.gruppo07iz.geometrika.command.CommandInterface;
import java.util.List;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class StateCreazionePoligono implements StateInterface{
    private final Model modello;
    private final DashboardController context;
    private final Pane lavagna;

    private final ColorPicker coloreRiempimentoCreazione;
    private final ColorPicker coloreBordoCreazione;
    
    private List<Double> punti;
    private List<Circle> cerchi;

    /**
     * Costruttore dello stato di creazione poligono.
     *
     * @param lavagna Il pannello su cui disegnare il poligono e i punti.
     * @param modello Il modello contenente la logica applicativa.
     * @param context Il controller della dashboard per gestire comandi e interfaccia.
     * @param coloreRiempimentoCreazione Il selettore del colore di riempimento per il poligono.
     * @param coloreBordoCreazione Il selettore del colore del bordo per il poligono.
     * @param controller Il controller della dashboard (ripetuto: può essere rimosso o unificato a context).
     */
    public StateCreazionePoligono(Model modello, DashboardController context, Pane lavagna, 
            ColorPicker coloreRiempimentoCreazione, ColorPicker coloreBordoCreazione, List punti, List cerchi) {
        this.modello = modello;
        this.context = context;
        this.lavagna = lavagna;
        
        this.coloreRiempimentoCreazione = coloreRiempimentoCreazione;
        this.coloreBordoCreazione = coloreBordoCreazione;
        
        this.punti = punti;
        this.cerchi = cerchi;
    }
    
    /**
     * Gestisce il click sinistro per aggiungere un punto al poligono.
     * Aggiunge le coordinate del punto e disegna un cerchio rosso per indicare il punto.
     *
     * @param event Evento di click sinistro.
     */
    @Override
    public void clickSinistro(MouseEvent event){

        double x = event.getX();
        double y = event.getY();

        // Controlla se ci sono già punti e se l'ultimo punto è uguale a quello attuale
        int size = punti.size();
        if (size >= 2) {
            double lastX = punti.get(size - 2);
            double lastY = punti.get(size - 1);
            if (Double.compare(x, lastX) == 0 && Double.compare(y, lastY) == 0) {
                context.mostraMessaggioInformativo("Non possono esserci punti duplicati consecutivi.");
                return; // Punto duplicato, non fare nulla
            }
        }

        punti.add(x);
        punti.add(y);

        // Aggiunge un piccolo cerchio per indicare il punto
        Circle cerchio = new Circle(x, y, 4, Color.RED);
        lavagna.getChildren().add(cerchio);
        cerchi.add(cerchio);
    }
    
    /**
     * Gestisce il click destro per terminare la creazione del poligono.
     * Se ci sono almeno tre punti, esegue il comando per creare il poligono.
     * Rimuove i cerchi indicanti i punti e resetta la lista dei punti.
     *
     * @param event Evento di click destro.
     */
    @Override
    public void clickDestro(ContextMenuEvent event) {
        if (punti.size() >= 6) { // Almeno 3 punti (6 coordinate)
                CommandInterface comando = new ComandoCreazionePoligono(this.modello, this.lavagna, 
                        this.punti, this.coloreRiempimentoCreazione.getValue(), 
                        this.coloreBordoCreazione.getValue());

                context.eseguiComando(comando);
            } else{
                context.mostraMessaggioInformativo("Devi selezionare almeno 3 punti per creare un poligono.");
        }
        context.gestionePuntiPoligono();
    }

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
