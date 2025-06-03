/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testClassi;

import com.gruppo07iz.geometrika.forme.Testo;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author giorg
 */
public class TestTesto {
    private String stringaTesto;
    private Color coloreRiempimento;
    private Color coloreBordo;
    
    @BeforeAll
    public static void setUpClass() {
        // Setup eseguito una sola volta prima di tutti i test
        
    }

    @AfterAll
    public static void tearDownClass() {
        // Cleanup eseguito una sola volta dopo tutti i test
    }

    @BeforeEach
    public void setUp() {
        // Setup prima di ogni test (se necessario)
        stringaTesto = new String("Testo");
        coloreRiempimento = Color.RED;
        coloreBordo = Color.BLACK;
    }

    @AfterEach
    public void tearDown() {
        // Cleanup dopo ogni test (se necessario)
    }

    /**
     * Testa il corretto funzionamento del costruttore di Poligono,
     * verificando che punti e colori siano corretti e che lo spessore del bordo sia 3.0.
     */
    @Test
    void testCostruttore() {
        Testo t = new Testo(stringaTesto, 0, 0, coloreBordo, coloreRiempimento, 20.0);
        assertEquals(stringaTesto, t.getText());
        assertEquals(coloreBordo, t.getStroke());
        assertEquals(coloreRiempimento, t.getFill());
        assertEquals(20, t.getFont().getSize());
    }

    /**
     * Testa lo spostamento (trascinamento) del testo e verifica le nuove coordinate.
     */
    @Test
    void testTrascina() {
        Testo t = new Testo(stringaTesto, 0, 0,coloreBordo, coloreRiempimento, 20.0);
        t.trascina(10, 20);
        assertEquals(t.getX(), 10);
        assertEquals(t.getY(), 20);
    }

    /**
     * Testa la modifica diretta delle coordinate del primo punto del poligono.
     */
    @Test
    void testCambiaCoordinate() {
        Testo t = new Testo(stringaTesto, 0, 0,coloreBordo, coloreRiempimento, 20.0);
        t.cambiaCoordinate(100, 200);
        
        assertEquals(100.0, t.ottieniCoordinate()[0]);
        assertEquals(200.0, t.ottieniCoordinate()[1]);
    }

    /**
     * Testa la modifica delle dimensioni del poligono tramite scala X e Y.
     */
    @Test
    void testModificaDimensioni() {
        Testo t = new Testo(stringaTesto, 0, 0,coloreBordo, coloreRiempimento, 20.0);
        t.modificaDimensioni(30.0, 30.0);
        assertEquals(30.0, t.ottieniValoriDimensioni()[0]);
        assertEquals(30.0, t.ottieniValoriDimensioni()[1]);
    }

    /**
     * Testa il recupero dei valori correnti delle dimensioni modificabili (scala X e Y).
     */
    @Test
    void testOttieniValoriDimensioni() {
        Testo t = new Testo(stringaTesto, 0, 0,coloreBordo, coloreRiempimento, 20.0);

        t.modificaDimensioni(15.0, 25.0);

        double[] valori = t.ottieniValoriDimensioni();

        assertEquals(15, valori[0], 0.01);
        assertEquals(25, valori[1], 0.01);
    }


    /**
     * Testa la generazione del testo descrittivo della forma Testo, assicurandosi
     * che contenga le informazioni principali come colori, dimensioni e rotazione.
     */
    @Test
    void testToText() {
        Testo t = new Testo(stringaTesto, 0, 0,coloreBordo, coloreRiempimento, 20.0);
        t.setRotate(45);
        t.modificaDimensioni(1.2, 1.3);
        Integer numeroParole = stringaTesto.split(" ").length;

        String text = t.toText();

        assertTrue(text.startsWith("TESTO"), "Il testo deve iniziare con 'TESTO'");
        assertTrue(text.contains(numeroParole.toString()), "Il testo deve contenere il numero di parole del Testo");
        assertTrue(text.contains(coloreBordo.toString()), "Il colore bordo deve essere presente");
        assertTrue(text.contains(coloreRiempimento.toString()), "Il colore riempimento deve essere presente");

        assertTrue(text.contains("45.0") || text.contains("45"), "La rotazione 45 deve essere presente");

        double[] dimensioni = t.ottieniValoriDimensioni();
        assertTrue(text.contains(String.valueOf((int) dimensioni[0])) ||
                   text.contains(String.format("%.2f", dimensioni[0])), "La larghezza deve essere presente nel testo");
        assertTrue(text.contains(String.valueOf((int) dimensioni[1])) ||
                   text.contains(String.format("%.2f", dimensioni[1])), "L'altezza deve essere presente nel testo");
    }


    /**
     * Testa la clonazione del testo, verificando che la copia abbia le stesse proprietà
     * ma sia una istanza distinta.
     */
    @Test
    void testClonaForma() {
        Testo originale = new Testo(stringaTesto, 0, 0,coloreBordo, coloreRiempimento, 20.0);
        originale.setRotate(15);
        originale.modificaDimensioni(10.0, 15.0);

        Testo clone = (Testo)originale.clonaForma();
        
        
        assertEquals(originale.getFill(), clone.getFill());
        assertEquals(originale.getStroke(), clone.getStroke());
        assertEquals(originale.getRotate(), clone.getRotate());
        assertEquals(originale.ottieniValoriDimensioni()[0], clone.ottieniValoriDimensioni()[0], 0.01);
        assertEquals(originale.ottieniValoriDimensioni()[1], clone.ottieniValoriDimensioni()[1], 0.01);
    }
}
