package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import com.gruppo07iz.geometrika.forme.TipoFormaRegolare;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ComandoCreazioneFormaRegolare implements CommandInterface{
    private final Model receiver;
    private final Pane padre; 
    private final TipoFormaRegolare tipoForma;
    private final double coordinataX;
    private final double coordinataY;
    private final Color coloreBordo;
    private final Color coloreRiempimento;
    
    // Essenziale per l'undo
    private FormaPersonalizzabile formaCreata;
    
    /**
     * Costruisce un comando per creare una forma regolare con tipo, posizione e colori specificati.
     *
     * @param receiver Il modello che gestisce la logica dell'applicazione.
     * @param padre Il contenitore grafico (pane) su cui aggiungere la forma.
     * @param tipoForma Il tipo di forma regolare da creare.
     * @param coordinataX La coordinata X del centro o punto di riferimento della forma.
     * @param coordinataY La coordinata Y del centro o punto di riferimento della forma.
     * @param coloreBordo Il colore del bordo della forma.
     * @param coloreRiempimento Il colore di riempimento della forma.
     */
    public ComandoCreazioneFormaRegolare(Model receiver, Pane padre, TipoFormaRegolare tipoForma, double coordinataX, double coordinataY, Color coloreBordo, Color coloreRiempimento) {
        this.receiver = receiver;
        this.padre = padre;
        this.tipoForma = tipoForma;
        this.coordinataX = coordinataX;
        this.coordinataY = coordinataY;
        this.coloreBordo = coloreBordo;
        this.coloreRiempimento = coloreRiempimento;
    }

    /**
     * Restituisce la coordinata X specificata per la creazione della forma.
     *
     * @return Coordinata X.
     */
    public double getCoordinataX() {
        return coordinataX;
    }

    /**
     * Restituisce la coordinata Y specificata per la creazione della forma.
     *
     * @return Coordinata Y.
     */
    public double getCoordinataY() {
        return coordinataY;
    }

    /**
     * Esegue il comando creando una nuova forma regolare e aggiungendola al {@code Pane} specificato.
     *
     * @return {@code true} se la forma è stata creata e aggiunta correttamente.
     */
    @Override
    public boolean execute() {
        formaCreata = this.receiver.creaFormaRegolare(tipoForma, coordinataX, coordinataY, coloreRiempimento, coloreBordo);
        this.receiver.aggiungiForma(padre, formaCreata);
        return true;
    }

    /**
     * Annulla il comando rimuovendo la forma creata dal contenitore.
     */
    @Override
    public void undo() {
       this.receiver.eliminaForma(padre, formaCreata);
    }
    
    /**
     * Restituisce la forma regolare creata da questo comando.
     *
     * @return La forma regolare creata.
     */
    public FormaPersonalizzabile getFormaCreata(){
        return this.formaCreata;
    }
    
}
