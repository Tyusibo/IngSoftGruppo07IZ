package com.gruppo07iz.geometrika;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.gruppo07iz.geometrika.forme.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;


public class Model {
    // E' solo per le forme regolari
    private Factory factory;
    

    /**
     * Costruttore della classe Model
     */
    public Model() {
        this.factory = new Factory();
    }
    
    /**
     * Metodo per la creazione di una forma regolare; per la creazione della forma richiama il metodo crea froma del factory
     * @param tipo
     * @param x
     * @param y
     * @param bordo
     * @param riemp
     * @return 
     */
    public Shape creaFormaRegolare(TipoFormaRegolare tipo,  double x, double y, Color bordo, Color riemp){
        Shape forma = factory.creaForma(tipo, x, y, bordo, riemp);
        return forma;
    }
        
    /**
     * Metodo per aggiungere una forma alla lavagna
     * @param lavagna
     * @param forma 
     */
    public void aggiungiForma(Pane lavagna, Shape forma){
        lavagna.getChildren().add(forma);
    }
    
    /**
     * Metodo per l'eliminazione di una forma
     * @param padre
     * @param formaDaEliminare 
     */
    public void eliminaForma(Pane padre, Shape formaDaEliminare){
        padre.getChildren().remove(formaDaEliminare);
    }
    
    /**
     * Metodo per la modifica delle dimensionsioni di una (@code FormaBidimensionale)
     * @param forma
     * @param dimensione1
     * @param dimensione2 
     */
    public void modificaDimensioni(FormaBidimensionale forma, double dimensione1, double dimensione2){
        forma.modificaDimensioni(dimensione1, dimensione2);
    }
    
    /**
     * Metodo per la modifica della dimensionsione di una (@code FormaMonodimensionale)
     * @param forma
     * @param dimensione
     */
    public void modificaDimensione(FormaMonodimensionale forma, double dimensione){
        forma.modificaDimensione(dimensione);
    }
    
    /**
     * Metodo per la modifica del colore di bordo
     * @param forma
     * @param coloreBordo 
     */
    public void modificaColoreBordo(Shape forma, Color coloreBordo){
        forma.setStroke(coloreBordo);
    }
    
    /**
     * Metodo per la modifica del colore di riempimento
     * @param forma
     * @param coloreRiempimento 
     */
    public void modificaColoreRiempimento(Shape forma, Color coloreRiempimento){
        forma.setFill(coloreRiempimento);
    }
    
    /**
     * Metodo per il trascinamento di una (@code FormaPersonalizzabile)
     * @param forma
     * @param dx
     * @param dy 
     */
    public void trascinaForma(FormaPersonalizzabile forma, double dx, double dy){
        forma.trascina(dx, dy);
    }
    
    /**
     * Metodo per spostare avanti una (@code Shape)
     * @param forma 
     */
    public void spostaAvanti(Shape forma){
        forma.toFront();
    }
    
    /**
     * Metodo per spostare indietro una (@code Shape)
     * @param forma 
     */
    public void spostaIndietro(Shape forma){
        forma.toBack();
    }
    
    /**
     * Metodo per spostare una (@code Shape) nella posizione iniziale
     * @param padre
     * @param forma
     * @param posizioneIniziale 
     */
    public void spostaPosizioneIniziale(Pane padre, Shape forma, int posizioneIniziale){
        padre.getChildren().remove(forma);
        padre.getChildren().add(posizioneIniziale, forma);
    }
 
    /**
     * Metodo per incollare una (@code Shape)
     * @param lavagna
     * @param formaCopiata
     * @param coordinataX
     * @param coordinataY 
     */
    public void incollaForma(Pane lavagna, Shape formaCopiata, double coordinataX, double coordinataY){
        lavagna.getChildren().add(formaCopiata);
        ((FormaPersonalizzabile) formaCopiata).cambiaCoordinate(coordinataX, coordinataY);
    }
    
    /**
     * Carica un file di testo e crea le forme sulla lavagna.
     * Il file deve essere formattato in modo che ogni riga rappresenti una forma.
     *
     * @param file il file da caricare
     * @param lavagna il pannello su cui disegnare le forme
     */
    public void caricaFoglio(File file, Pane lavagna) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");

                
            try {
                caricaFormaRegolare(parts, lavagna); // si occupa di caricare una forma regolare
            } catch (IllegalArgumentException e) {
                System.out.println("Errore nella riga: " + line);
                e.printStackTrace();
        }
            
        } 
    }catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + file.getAbsolutePath());
            e.printStackTrace(); 
        }
    }


    /**
     * Metodo per caricare una (@code Shape)
     * @param parts
     * @param lavagna 
     */
    private void caricaFormaRegolare(String[] parts, Pane lavagna) {
        TipoFormaRegolare tipo = TipoFormaRegolare.valueOf(parts[0]);
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        Color stroke = Color.web(parts[3]);
        Color fill = Color.web(parts[4]);

        Shape forma = this.creaFormaRegolare(tipo, x, y, stroke, fill);

        if (forma instanceof FormaBidimensionale && parts.length >= 8) {
            ((FormaBidimensionale) forma).modificaDimensioni(Double.parseDouble(parts[6]), Double.parseDouble(parts[5]));

        } else if (forma instanceof FormaMonodimensionale && parts.length >= 7) {
            ((FormaMonodimensionale) forma).modificaDimensione(Double.parseDouble(parts[5]));
        }

        lavagna.getChildren().add(forma);

    }

    /**
     * Salva le forme presenti nella lavagna in un file di testo.
     * Ogni forma viene salvata su una riga con le sue proprietà.
     *
     * @param file il file in cui salvare le forme
     * @param lavagna il pannello contenente le forme da salvare
     * @throws IOException se si verifica un errore durante la scrittura del file
     */
    public void salvaFoglio(File file, Pane lavagna) throws IOException {

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // getChildren() fornisce già l'ordine utile per la profondità
            for (Node node : lavagna.getChildren()) {
                if (node instanceof FormaPersonalizzabile) {
                    writer.write(((FormaPersonalizzabile) node).toText());
                }
            }
        }
    }

}
