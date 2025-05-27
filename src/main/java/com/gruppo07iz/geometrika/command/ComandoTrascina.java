package com.gruppo07iz.geometrika.command;

import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;

public class ComandoTrascina implements CommandInterface {
    private final Model receiver;
    private final FormaPersonalizzabile forma;
    // Per salvare l'accumulo dei vari trascinamenti
    // Da quando viene premuto con il mouse fino al rilascio
    private double deltaXTotale = 0;
    private double deltaYTotale = 0;
    // Per eseguire a ogni esecuzione l'ultimo spostamento avvenuto sulla GUI
    private double ultimoOffsetX;
    private double ultimoOffsetY;
    

    /**
     * Costruttore del comando di trascinamento.
     * 
     * @param receiver L'oggetto modello che gestisce le operazioni sulle forme.
     * @param forma La forma personalizzabile da trascinare.
     */
    public ComandoTrascina(Model receiver, FormaPersonalizzabile forma) {
        this.receiver = receiver;
        this.forma = forma;
    }

    /**
     * Aggiorna gli ultimi offset di trascinamento.
     * Questo metodo deve essere chiamato durante il trascinamento per fornire 
     * i valori correnti degli spostamenti sull'asse X e Y.
     * 
     * @param offsetX Spostamento orizzontale corrente.
     * @param offsetY Spostamento verticale corrente.
     */
    public void aggiorna(double offsetX, double offsetY) {
        this.ultimoOffsetX = offsetX;
        this.ultimoOffsetY = offsetY;
    }

    /**
     * Esegue il comando di trascinamento della forma con gli ultimi offset aggiornati.
     * Accumula gli spostamenti totali effettuati per poterli annullare.
     * 
     * @return false (indicando che questo comando non va memorizzato automaticamente nella pila).
     */
    @Override
    public boolean execute() {
        receiver.trascinaForma(forma, ultimoOffsetX, ultimoOffsetY);
        // Mantengo aggiornati gli accumuli
        deltaXTotale += ultimoOffsetX;
        deltaYTotale += ultimoOffsetY;
        return false;
    }
    
    /**
     * Annulla il comando di trascinamento riportando la forma alla posizione iniziale,
     * spostandola di quanto accumulato nei delta totali in direzione opposta.
     */
    @Override
    public void undo() {
        receiver.trascinaForma(forma, -deltaXTotale, -deltaYTotale);
    }
}