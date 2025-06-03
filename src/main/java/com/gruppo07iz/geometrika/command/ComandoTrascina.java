package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;

public class ComandoTrascina implements CommandInterface {
    private final Model receiver;
    private final FormaPersonalizzabile forma;
    private double deltaXTotale = 0;
    private double deltaYTotale = 0;
    private double ultimoOffsetX;
    private double ultimoOffsetY;


    /**
     * Costruttore del comando.
     * 
     * @param receiver Il modello che gestisce lo spostamento della forma.
     * @param forma La forma da trascinare.
     */
    public ComandoTrascina(Model receiver, FormaPersonalizzabile forma) {
        this.receiver = receiver;
        this.forma = forma;
    }

    /**
     * Aggiorna l'offset del trascinamento che verrà usato nel prossimo execute().
     * 
     * @param offsetX Lo spostamento lungo l'asse X.
     * @param offsetY Lo spostamento lungo l'asse Y.
     */
    public void aggiorna(double offsetX, double offsetY) {
        this.ultimoOffsetX = offsetX;
        this.ultimoOffsetY = offsetY;
    }

    /**
     * Esegue il trascinamento applicando l'offset più recente e accumulando
     * lo spostamento totale per l’operazione di undo.
     * 
     * @return false poiché questo comando può essere eseguito ripetutamente durante il drag.
     */
    @Override
    public boolean execute() {
        receiver.trascinaForma(forma, ultimoOffsetX, ultimoOffsetY);
        deltaXTotale += ultimoOffsetX;
        deltaYTotale += ultimoOffsetY;
        return false;
    }

    /**
     * Annulla tutti gli spostamenti applicati durante l’esecuzione del comando,
     * riportando la forma nella posizione iniziale.
     */
    @Override
    public void undo() {
        receiver.trascinaForma(forma, -deltaXTotale, -deltaYTotale);
    }
}
