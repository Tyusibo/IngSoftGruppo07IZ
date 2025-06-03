package com.gruppo07iz.geometrika.forme;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Interfaccia che definisce i metodi comuni per la personalizzazione delle forme geometriche.
 * Fornisce le firme dei metodi per disegnare, modificare e trasformare le forme.
 */
public interface FormaPersonalizzabile {
    
    /**
     * Disegna la forma sulla lavagna specificata.
     * 
     * @param lavagna Il Pane su cui disegnare la forma
     */
    public void disegna(Pane lavagna);
    
    /**
     * Disegna la forma sulla lavagna specificata nella posizione indicata.
     * 
     * @param lavagna Il Pane su cui disegnare la forma
     * @param posizione La posizione nella lista dei figli del Pane
     */
    public void disegna(Pane lavagna, int posizione);
    
    /**
     * Imposta il colore di riempimento della forma.
     * 
     * @param coloreRiempimento Il colore da utilizzare per il riempimento
     */
    public void setColoreRiempimento(Color coloreRiempimento);
    
    /**
     * Restituisce il colore di riempimento corrente della forma.
     * 
     * @return Il colore di riempimento corrente
     */
    public Color getColoreRiempimento();

    /**
     * Imposta il colore del bordo della forma.
     * 
     * @param coloreBordo Il colore da utilizzare per il bordo
     */
    public void setColoreBordo(Color coloreBordo);
    
    /**
     * Restituisce il colore del bordo corrente della forma.
     * 
     * @return Il colore del bordo corrente
     */
    public Color getColoreBordo();
    
    /**
     * Elimina la forma dalla lavagna specificata.
     * 
     * @param lavagna Il Pane da cui rimuovere la forma
     */
    public void eliminaForma(Pane lavagna);
    
    /**
     * Trascina la forma di una distanza specificata lungo gli assi X e Y.
     * 
     * @param dx Distanza di trascinamento lungo l'asse X
     * @param dy Distanza di trascinamento lungo l'asse Y
     */
    public void trascina(double dx, double dy);
    
    /**
     * Sposta la forma in primo piano rispetto agli altri elementi.
     */
    public void spostaDavanti();
    
    /**
     * Sposta la forma in secondo piano rispetto agli altri elementi.
     */
    public void spostaIndietro();

    /**
     * Imposta l'angolo di rotazione della forma.
     * 
     * @param angolo L'angolo di rotazione in gradi
     */
    public void setAngolo(double angolo);
    
    /**
     * Restituisce l'angolo di rotazione corrente della forma.
     * 
     * @return L'angolo di rotazione corrente in gradi
     */
    public double getAngolo();
    
    /**
     * Applica una specchiatura orizzontale alla forma.
     */
    public void setSpecchiaturaOrizzontale();
    
    /**
     * Applica una specchiatura verticale alla forma.
     */
    public void setSpecchiaturaVerticale();
    
    /**
     * Cambia le coordinate della forma nella posizione specificata.
     * 
     * @param coordinataX La nuova coordinata X
     * @param coordinataY La nuova coordinata Y
     */
    public void cambiaCoordinate(double coordinataX, double coordinataY);
    
    /**
     * Restituisce le coordinate correnti della forma.
     * 
     * @return Array dove il primo elemento è la coordinata X e il secondo la coordinata Y
     */
    public double[] ottieniCoordinate();
    
    /**
     * Calcola e restituisce le coordinate del centro della forma.
     * 
     * @return Array contenente le coordinate del centro (x, y)
     */
    public double[] calcolaCentro();

    /**
     * Genera una rappresentazione testuale delle informazioni della forma.
     * 
     * @return Stringa contenente le informazioni della forma
     */
    public String toText();
    
    /**
     * Crea una copia della forma corrente.
     * 
     * @return Un nuovo oggetto FormaPersonalizzabile che è una copia dell'originale
     */
    public FormaPersonalizzabile clonaForma();

    /**
     * Applica un effetto di ombreggiatura alla forma.
     * 
     * @param effetto L'effetto DropShadow da applicare
     */
    public void applicaEffetto(DropShadow effetto);
}