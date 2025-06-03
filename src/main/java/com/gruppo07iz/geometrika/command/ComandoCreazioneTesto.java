package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ComandoCreazioneTesto implements CommandInterface{
    private final Model receiver;
    private final Pane padre; 
    private final double coordinataX;
    private final double coordinataY;
    private final Color coloreBordo;
    private final Color coloreRiempimento;
    private double dimensione;
    private String testo;
    // Essenziale per l'undo
    private FormaPersonalizzabile testoCreato;
    
    /**
     * Costruisce un comando per creare un testo con posizione, dimensioni e colori specificati.
     *
     * @param receiver Il modello che gestisce la logica dell'applicazione.
     * @param padre Il contenitore grafico (pane) su cui aggiungere la forma.
     * @param coordinataX La coordinata X del centro o punto di riferimento della forma.
     * @param coordinataY La coordinata Y del centro o punto di riferimento della forma.
     * @param coloreBordo Il colore del bordo della forma.
     * @param coloreRiempimento Il colore di riempimento della forma.
     * @param testo Il testo da inserire.
     */
    public ComandoCreazioneTesto(Model receiver, Pane padre, String testo, double coordinataX, double coordinataY, Color coloreBordo, Color coloreRiempimento, double dimensione) {
        this.receiver = receiver;
        this.padre = padre;
        this.coordinataX = coordinataX;
        this.coordinataY = coordinataY;
        this.coloreBordo = coloreBordo;
        this.coloreRiempimento = coloreRiempimento;
        this.dimensione=dimensione;
        this.testo=testo;
    }

    /**
     * Restituisce la coordinata X associata alla posizione della forma o dell'operazione.
     *
     * @return Il valore della coordinata X.
     */
    public double getCoordinataX() {
        return coordinataX;
    }
    /**
     * Restituisce la coordinata Y associata alla posizione della forma o dell'operazione.
     *
     * @return Il valore della coordinata Y.
     */
    public double getCoordinataY() {
        return coordinataY;
    }
    /**
     * Esegue il comando creando la forma regolare e aggiungendola al contenitore.
     *
     * @return true se la creazione è avvenuta con successo.
     */
    @Override
    public boolean execute() {
        testoCreato = this.receiver.creaTesto(testo, coordinataX, coordinataY, coloreRiempimento, coloreBordo, dimensione);
        this.receiver.aggiungiForma(padre, testoCreato);
        return true;
    }

    /**
     * Annulla il comando rimuovendo il testo creato dal contenitore.
     */
    @Override
    public void undo() {
       this.receiver.eliminaForma(padre, testoCreato);
    }
    
    /**
     * Restituisce il testo creato da questo comando.
     *
     * @return Il testo creato.
     */
    public FormaPersonalizzabile getTesto(){
        return this.testoCreato;
    }
}
