package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;


public class ComandoModificaColoreBordo implements CommandInterface {

    private final Model receiver;
    private final Shape forma;
    private final Color coloreBordo;
    // Essenziale per l'undo
    private final Color coloreBordoVecchio;
    

    /**
     * Costruisce il comando salvando il colore del bordo vecchio e quello nuovo.
     *
     * @param receiver Il modello che esegue la modifica.
     * @param forma La forma da modificare.
     * @param coloreBordo Il nuovo colore del bordo.
     */
    public ComandoModificaColoreBordo(Model receiver, Shape forma, Color coloreBordo) {
        this.receiver = receiver;
        this.forma = forma;
        this.coloreBordo = coloreBordo;
        
        this.coloreBordoVecchio = (Color) forma.getStroke();
    }
    
    /**
     * Restituisce il colore del bordo precedente alla modifica.
     *
     * @return Il colore del bordo vecchio.
     */
    public Color getColoreBordoVecchio(){
        return this.coloreBordoVecchio;
    }
    
    /**
     * Esegue la modifica solo se il nuovo colore è diverso da quello vecchio.
     *
     * @return true se la modifica è stata effettuata, false altrimenti.
     */
    public Color getColoreBordo(){
        return this.coloreBordo;
    }
    
    @Override
    public boolean execute() {
        // Solo se il nuovo colore è cambiato eseguo la modifica
        if(coloreBordo == coloreBordoVecchio){ return false; }
        receiver.modificaColoreBordo(forma, coloreBordo);
        return true; 
    }

    /**
     * Annulla la modifica ripristinando il colore del bordo precedente.
     */
    @Override
    public void undo() {
        receiver.modificaColoreBordo(forma, coloreBordoVecchio);
    }
    
}
