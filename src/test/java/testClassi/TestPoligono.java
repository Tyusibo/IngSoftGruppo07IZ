package testClassi;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import com.gruppo07iz.geometrika.forme.Poligono;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class TestPoligono {
    
    private List<Double> puntiIniziali;
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
        puntiIniziali = Arrays.asList(0.0, 0.0, 50.0, 0.0, 25.0, 50.0);
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
        Poligono p = new Poligono(puntiIniziali, coloreRiempimento, coloreBordo);
        assertEquals(puntiIniziali, p.getPoints());
        assertEquals(coloreBordo, p.getStroke());
        assertEquals(coloreRiempimento, p.getFill());
        assertEquals(3.0, p.getStrokeWidth());
    }

    /**
     * Testa lo spostamento (trascinamento) del poligono e verifica le nuove coordinate dei punti.
     */
    @Test
    void testTrascina() {
        Poligono p = new Poligono(puntiIniziali, coloreRiempimento, coloreBordo);
        p.trascina(10, 20);
        List<Double> points = p.getPoints();

        assertEquals(10.0, points.get(0));
        assertEquals(20.0, points.get(1));
        assertEquals(60.0, points.get(2));
        assertEquals(20.0, points.get(3));
        assertEquals(35.0, points.get(4));
        assertEquals(70.0, points.get(5));
    }

    /**
     * Testa la modifica diretta delle coordinate del primo punto del poligono.
     */
    @Test
    void testCambiaCoordinate() {
        Poligono p = new Poligono(puntiIniziali, coloreRiempimento, coloreBordo);
        p.cambiaCoordinate(100, 200);
        List<Double> points = p.getPoints();

        assertEquals(100.0, points.get(0));
        assertEquals(200.0, points.get(1));
    }

    /**
     * Testa la modifica delle dimensioni del poligono tramite scala X e Y.
     */
    @Test
    void testModificaDimensioni() {
        Poligono p = new Poligono(puntiIniziali, coloreRiempimento, coloreBordo);
        p.modificaDimensioni(30.0, 30.0);
        assertEquals(30.0, p.ottieniValoriDimensioni()[0]);
        assertEquals(30.0, p.ottieniValoriDimensioni()[1]);
    }

    /**
     * Testa il recupero dei valori correnti delle dimensioni modificabili.
     */
    @Test
    void testOttieniValoriDimensioni() {
        Poligono p = new Poligono(puntiIniziali, coloreRiempimento, coloreBordo);

        double nuovaLarghezza = 15;
        double nuovaAltezza = 25;

        p.modificaDimensioni(nuovaLarghezza, nuovaAltezza);

        double[] valori = p.ottieniValoriDimensioni();

        assertEquals(nuovaLarghezza, valori[0], 0.1);
        assertEquals(nuovaAltezza, valori[1], 0.1);
    }


    /**
     * Testa la generazione del testo descrittivo della forma, assicurandosi
     * che contenga le informazioni principali come tipo, colori, rotazione e scale.
     */
    @Test
    void testToText() {
        Poligono p = new Poligono(puntiIniziali, coloreRiempimento, coloreBordo);
        p.setRotate(45);
        p.modificaDimensioni(1.2, 1.3);

        String text = p.toText();

        assertTrue(text.startsWith("POLIGONO"), "Il testo deve iniziare con 'POLIGONO'");
        assertTrue(text.contains(coloreBordo.toString()), "Il colore bordo deve essere presente");
        assertTrue(text.contains(coloreRiempimento.toString()), "Il colore riempimento deve essere presente");

        assertTrue(text.contains("45.0") || text.contains("45"), "La rotazione 45 deve essere presente");

        double[] dimensioni = p.ottieniValoriDimensioni();
        assertTrue(text.contains(String.valueOf((int) dimensioni[0])) ||
                   text.contains(String.format("%.2f", dimensioni[0])), "La larghezza deve essere presente nel testo");
        assertTrue(text.contains(String.valueOf((int) dimensioni[1])) ||
                   text.contains(String.format("%.2f", dimensioni[1])), "L'altezza deve essere presente nel testo");
    }


    /**
     * Testa la clonazione del poligono, verificando che la copia abbia le stesse proprietà
     * ma sia una istanza distinta.
     */
    @Test
    void testClonaForma() {
        Poligono originale = new Poligono(puntiIniziali, coloreRiempimento, coloreBordo);
        originale.setRotate(15);
        originale.modificaDimensioni(10.0, 15.0);
        System.out.println(originale.ottieniValoriDimensioni()[0]+" "+originale.ottieniValoriDimensioni()[1] );

        Poligono clone = (Poligono)originale.clonaForma();
        System.out.println(clone.ottieniValoriDimensioni()[0]+" "+clone.ottieniValoriDimensioni()[1] );
        
        assertEquals(originale.getPoints(), clone.getPoints());
        assertEquals(originale.getFill(), clone.getFill());
        assertEquals(originale.getStroke(), clone.getStroke());
        assertEquals(originale.getRotate(), clone.getRotate());
        assertEquals(originale.ottieniValoriDimensioni()[0], clone.ottieniValoriDimensioni()[0], 2.0);
        assertEquals(originale.ottieniValoriDimensioni()[1], clone.ottieniValoriDimensioni()[1], 2.0);
    }
}
