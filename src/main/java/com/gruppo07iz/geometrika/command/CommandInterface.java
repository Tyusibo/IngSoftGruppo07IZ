package com.gruppo07iz.geometrika.command;

/**
 * La CommandInterface come previsto dal pattern offre la possibilità di far implementare ai comandi
 * la loro logica di execute() e undo()
 * La natura dei comandi diversa tra loro impedisce la creazione di una classe astratta
 */
public interface CommandInterface {
    /**
    * Il metodo execute() che restituisce un true o un false
    * andando a indicare se il comando deve essere registrato nella storia
    * La natura dei comandi diversa tra loro impedisce la creazione di una classe astratta
    */
    public boolean execute();
    
    /**
    * Il metodo undo() non restituisce nulla
    */
    public void undo();
}
