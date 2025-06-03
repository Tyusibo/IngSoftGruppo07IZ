package com.gruppo07iz.geometrika.state;

import com.gruppo07iz.geometrika.DashboardController;
import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.command.ComandoTrascina;
import com.gruppo07iz.geometrika.command.CommandInterface;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

public class StateSelezioneFigura implements StateInterface{

    
    private final Model modello;
    private final DashboardController context;
    private final Pane lavagna;
    // Per mantenere traccia dell'inizio del trascinamento
    private double startX, startY;
    // Per eseguire il comando di trascinamento
    private CommandInterface comandoTrascinamento;
    /**
     * Costruttore dello stato di selezione figura.
     *
     * @param modello Il modello dell'applicazione contenente i dati.
     * @param context Il controller della dashboard che gestisce l'interfaccia utente.
     * @param lavagna Il pannello su cui sono disegnate le forme.
     */
    public StateSelezioneFigura(Model modello, DashboardController context, Pane lavagna) {
        this.modello = modello;
        this.context = context;
        this.lavagna = lavagna;
        
        this.comandoTrascinamento = null;
    }
    
    /**
     * Gestisce il click destro per aprire il menu contestuale.
     * Se il click avviene su una forma, seleziona la forma e mostra il menu relativo.
     * Se il click è sulla lavagna vuota, mostra il menu con opzioni generali.
     *
     * @param event Evento di click destro del mouse.
     */
    @Override
    public void clickDestro(ContextMenuEvent event) {
        // Salvo le coordinate indipendetemente da se ho cliccato con il tasto
        // destro una forma o la lavagna, perchè il menu andrà comunque aperto
        double coordinataMenuX = event.getScreenX();
        double coordinataMenuY = event.getScreenY();
        // Le coordinate per eseguire l'incolla si calcolano in modo diverso
        Point2D coordinateLocali = ((Node) event.getTarget()).screenToLocal(event.getScreenX(), event.getScreenY());
        context.setCoordinateIncolla(coordinateLocali.getX(), coordinateLocali.getY());
        
        // Se il click destro è stato effettuato su una forma
        if (event.getTarget() instanceof FormaPersonalizzabile) {
            FormaPersonalizzabile forma = (FormaPersonalizzabile) event.getTarget();
            context.setFormaSelezionata(forma);
            
            context.abilitaBottoniContextMenu(false);
            context.aggiornaContextMenu();
            context.apriToggleMenu(event, coordinataMenuX, coordinataMenuY);
            
            // Se il click destro è stato effettuato su una lavagna
        } else if (event.getTarget() == this.lavagna) {
            context.abilitaBottoniContextMenu(true);
            context.apriToggleMenu(event, coordinataMenuX, coordinataMenuY);
        }
    }
    
    /**
     * Gestisce il click sinistro.
     * Se il click è su una forma, inizia un trascinamento creando il comando relativo.
     *
     * @param event Evento di click sinistro del mouse.
     */
    @Override
    public void clickSinistro(MouseEvent event) {
        
        // Se ho cliccato una forma con il tasto sinistro si deve avviare il trascinamento
        if (event.getTarget() instanceof FormaPersonalizzabile) {
            this.startX = event.getSceneX();
            this.startY = event.getSceneY();
            FormaPersonalizzabile formaSelezionata = (FormaPersonalizzabile) event.getTarget();
            FormaPersonalizzabile gruppo = context.ottieniGruppoPiuEsterno(formaSelezionata);
            this.comandoTrascinamento = new ComandoTrascina(this.modello, (FormaPersonalizzabile) gruppo);
            }
    }

   
    /**
     * Gestisce il trascinamento del mouse.
     * Aggiorna la posizione della forma trascinata e aggiorna il comando trascinamento.
     *
     * @param event Evento di trascinamento del mouse.
     */
    @Override
    public void trascinamento(MouseEvent event) {

        // Se il target è la lavagna non devo fare niente
        if (event.getTarget() == this.lavagna) { return; }

        // Se il target è una forma allora questa va trascinata
        if (event.getTarget() instanceof FormaPersonalizzabile) {
            double dx = event.getSceneX() - startX;
            double dy = event.getSceneY() - startY;

            double[] fattoriScala = getZoomDaPane();
            dx /= fattoriScala[0];
            dy /= fattoriScala[1];

            ((ComandoTrascina) comandoTrascinamento).aggiorna(dx, dy);
            context.eseguiComando(comandoTrascinamento);
            
            startX = event.getSceneX();
            startY = event.getSceneY();

        }

    }

    /**
     * Gestisce il rilascio del mouse.
     * Completa il trascinamento aggiungendo il comando alla pila dei comandi.
     *
     * @param event Evento di rilascio del mouse.
     */
    @Override
    public void mouseRilasciato(MouseEvent event) {

        // Se ho rilasciato con il click sinistro ho terminato un trascinamento
        if (event.getTarget() instanceof FormaPersonalizzabile) {
            context.aggiungiComandoNellaPila(comandoTrascinamento);
            this.comandoTrascinamento = null;
            this.startX = this.startY = 0;
        }
    }

    /**
     * Gestisce il movimento del mouse.
     * Cambia il cursore in "mano" se sopra una forma, altrimenti usa il cursore di default.
     *
     * @param event Evento di movimento del mouse.
     */
    @Override
    public void movimentoMouse(MouseEvent event) {
        // Se il target è una forma allora metto la manina
        if (event.getTarget() instanceof FormaPersonalizzabile) {
            lavagna.setCursor(javafx.scene.Cursor.HAND);
            // Altrimenti
        } else {
            lavagna.setCursor(javafx.scene.Cursor.DEFAULT);
        }
    }
    
    /**
     * Recupera i fattori di scala (zoom) applicati al pannello {@code lavagna}
     * analizzando tutte le trasformazioni {@code Scale} presenti nella lista dei transform.
     * <p>
     * Se sono presenti più trasformazioni di scala, i fattori vengono moltiplicati per ottenere
     * il fattore di zoom complessivo lungo gli assi X e Y.
     *
     * @return un array di {@code double} contenente due valori:
     *         <ul>
     *             <li>l'indice 0 rappresenta il fattore di scala orizzontale (X)</li>
     *             <li>l'indice 1 rappresenta il fattore di scala verticale (Y)</li>
     *         </ul>
     */
    private double[] getZoomDaPane() {
        double scaleX = 1.0;
        double scaleY = 1.0;

        for (Transform t : this.lavagna.getTransforms()) {
            if (t instanceof Scale) {
                Scale s = (Scale) t;
                scaleX *= s.getX();
                scaleY *= s.getY();
            }
        }

            return new double[] { scaleX, scaleY };
    }
    
}
