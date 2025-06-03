package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import javafx.scene.paint.Color;


public class ComandoModificaColoreRiempimentoSingolo implements CommandInterface {

    private final Model receiver;
    private final FormaPersonalizzabile forma;
    private final Color coloreRiempimento;
    // Essenziale per l'undo
    private final Color coloreRiempimentoVecchio;
    

    /**
     * Costruisce il comando salvando il colore di riempimento vecchio e quello nuovo.
     *
     * @param receiver Il modello che esegue la modifica.
     * @param forma La forma da modificare.
     * @param coloreRiempimento Il nuovo colore di riempimento.
     */
    public ComandoModificaColoreRiempimentoSingolo(Model receiver, FormaPersonalizzabile forma, Color coloreRiempimento) {
        this.receiver = receiver;
        this.forma = forma;
        this.coloreRiempimento = coloreRiempimento;
        
        this.coloreRiempimentoVecchio = (Color) forma.getColoreRiempimento();
    }
    
    /**
     * Restituisce il colore di riempimento precedente alla modifica.
     * 
     * @return Il colore di riempimento vecchio.
     */
    public Color getColoreRiempimentoVecchio(){
        return this.coloreRiempimentoVecchio;
    }

    /**
     * Esegue la modifica solo se il nuovo colore è diverso da quello vecchio.
     *
     * @return true se la modifica è stata effettuata, false altrimenti.
     */
    public Color getColoreRiempimento(){
        return this.coloreRiempimento;
    }
    
    /**
     * Esegue il comando per modificare il colore di riempimento di una forma.
     *
     * La modifica viene eseguita solo se il nuovo colore {@code coloreRiempimento}
     * è diverso dal colore precedente {@code coloreRiempimentoVecchio}, evitando
     * operazioni ridondanti.
     *
     * @return {@code true} se il colore di riempimento è stato effettivamente modificato,
     *         {@code false} se il colore era già uguale a quello precedente.
     */
    @Override
    public boolean execute() {
        // Solo se il nuovo colore è cambiato eseguo la modifica
        if(coloreRiempimento == coloreRiempimentoVecchio){ return false; }
        receiver.modificaColoreRiempimento(forma, coloreRiempimento);
        return true;  
    }

    /**
     * Annulla la modifica ripristinando il colore di riempimento precedente.
     */
    @Override
    public void undo() {
        receiver.modificaColoreRiempimento(forma, coloreRiempimentoVecchio);
    }
    
}
