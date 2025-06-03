package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ComandoCreazionePoligono implements CommandInterface{
    private final Model receiver;
    private final Pane padre; 
    private final Color coloreBordo;
    private final Color coloreRiempimento;
    private final List<Double> punti;
    
    // Essenziale per l'undo
    private FormaPersonalizzabile formaCreata;
    
    /**
     * Costruisce un comando per creare un poligono con specifici punti e colori.
     *
     * @param receiver Il modello che gestisce la logica dell'applicazione.
     * @param padre Il contenitore grafico (pane) su cui aggiungere il poligono.
     * @param punti Lista di coordinate dei vertici del poligono.
     * @param coloreRiempimento Colore di riempimento del poligono.
     * @param coloreBordo Colore del bordo del poligono.
     */
    public ComandoCreazionePoligono(Model receiver, Pane padre, List<Double> punti,
            Color coloreRiempimento, Color coloreBordo) {
        this.receiver = receiver;
        this.padre = padre;
        this.coloreBordo = coloreBordo;
        this.coloreRiempimento = coloreRiempimento;
        this.punti = punti;
    }

    /**
     * Restituisce la forma creata da questo comando.
     *
     * Questo metodo è utile per accedere direttamente alla forma generata
     * durante l'esecuzione del comando, ad esempio per selezionarla o modificarla
     * dopo la creazione.
     *
     * @return L'oggetto {@code Shape} creato, oppure {@code null} se il comando non è stato ancora eseguito.
     */
    public FormaPersonalizzabile getFormaCreata(){
        return this.formaCreata;
    }
    
    /**
     * Restituisce la lista dei punti usati per creare la forma.
     *
     * I punti sono rappresentati come una lista di coordinate alternate (x1, y1, x2, y2, ...),
     * tipicamente utilizzate per costruire poligoni o forme personalizzate.
     *
     * @return Una lista di {@code Double} contenente le coordinate dei vertici della forma.
     */
    public List<Double> getPunti(){
        return this.punti;
    }
    
    /**
     * Esegue il comando creando il poligono e aggiungendolo al contenitore.
     *
     * @return true se la creazione è avvenuta con successo.
     */
    @Override
    public boolean execute() {
        formaCreata = this.receiver.creaPoligono(punti, coloreRiempimento, coloreBordo);
        this.receiver.aggiungiForma(padre, formaCreata);
        return true;
    }

    /**
     * Annulla il comando rimuovendo il poligono creato dal contenitore.
     */
    @Override
    public void undo() {
       this.receiver.eliminaForma(padre, formaCreata);
    }
    
}
