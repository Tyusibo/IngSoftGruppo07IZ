package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import javafx.scene.paint.Color;


public class ComandoModificaColoreBordoSingolo implements CommandInterface {

    private final Model receiver;
    private final FormaPersonalizzabile forma;
    private final Color coloreBordo;
    // Essenziale per l'undo
    private final Color coloreBordoVecchio;
    

    /**
     * Costruisce un comando per modificare il colore del bordo di una forma specifica.
     *
     * @param receiver Il modello che esegue la modifica.
     * @param forma La forma da modificare.
     * @param coloreBordo Il nuovo colore del bordo da applicare.
     */
    public ComandoModificaColoreBordoSingolo(Model receiver, FormaPersonalizzabile forma, Color coloreBordo) {
        this.receiver = receiver;
        this.forma = forma;
        this.coloreBordo = coloreBordo;
        
        this.coloreBordoVecchio = (Color) forma.getColoreBordo();
    }
    
    /**
     * Restituisce il colore del bordo precedente alla modifica.
     *
     * @return Il colore di bordo originale prima dell’esecuzione del comando.
     */
    public Color getColoreBordoVecchio(){
        return this.coloreBordoVecchio;
    }
    
    /**
    * Restituisce il nuovo colore del bordo che verrà applicato alla forma.
    *
    * @return Il nuovo colore di bordo.
    */
    public Color getColoreBordo(){
        return this.coloreBordo;
    }
    
    /**
    * Esegue la modifica del colore del bordo se il nuovo colore è diverso da quello corrente.
    *
    * @return {@code true} se il colore è stato modificato, {@code false} se era già uguale.
    */ 
    @Override
    public boolean execute() {
        // Solo se il nuovo colore è cambiato eseguo la modifica
        if(coloreBordo == coloreBordoVecchio){ return false; }
        receiver.modificaColoreBordo(forma, coloreBordo);
        return true; 
    }

    /**
     * Annulla la modifica del colore del bordo, ripristinando il colore precedente.
     */
    @Override
    public void undo() {
        receiver.modificaColoreBordo(forma, coloreBordoVecchio);
    }
    
}
