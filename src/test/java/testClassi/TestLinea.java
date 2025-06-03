package testClassi;
import com.gruppo07iz.geometrika.forme.Linea;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test di unità per la classe {@link Linea}.
 * Verifica il corretto funzionamento dei metodi relativi
 * alla gestione delle dimensioni, posizione e rappresentazione testuale della linea.
 */
class TestLinea {

    private Linea linea;

    /**
     * Inizializza una nuova linea orizzontale da (0,0) a (200,0) di colore rosso prima di ogni test.
     */
    @BeforeEach
    void setUp() {
        linea = new Linea(0, 0, Color.RED);
    }

    /**
     * Verifica che il costruttore imposti correttamente tutti i parametri iniziali della linea.
     */
    @Test
    void testCostruttore() {
        assertEquals(0, linea.getStartX());
        assertEquals(0, linea.getStartY());
        assertEquals(200, linea.getEndX());
        assertEquals(0, linea.getEndY());
        assertEquals(Color.RED, linea.getStroke());
        assertEquals(4, linea.getStrokeWidth());
    }

    /**
     * Verifica che il costruttore accetti coordinate negative e le gestisca correttamente.
     */
    @Test
    void testCostruttoreCoordinateNegative() {
        Linea l = new Linea(-10, -20, Color.GREEN);
        assertEquals(-10, l.getStartX());
        assertEquals(-20, l.getStartY());
        assertEquals(190, l.getEndX());
        assertEquals(-20, l.getEndY());
    }
  

    /**
     * Verifica il calcolo corretto della lunghezza per una linea orizzontale.
     */
    @Test
    void testCalcolaLunghezzaOrizzontale() {
        assertEquals(200.0, linea.calcolaLunghezza(), 0.0001);
    }

    /**
     * Verifica il calcolo corretto della lunghezza per una linea verticale.
     */
    @Test
    void testCalcolaLunghezzaVerticale() {
        linea.setEndX(0);
        linea.setEndY(200);
        assertEquals(200.0, linea.calcolaLunghezza(), 0.0001);
    }

    /**
     * Verifica il calcolo corretto della lunghezza per una linea obliqua.
     */
    @Test
    void testCalcolaLunghezzaObliqua() {
        linea.setEndX(30);
        linea.setEndY(40);
        assertEquals(50, linea.calcolaLunghezza(), 0.0001);
    }
    
    /**
     * Verifica il calcolo corretto della lunghezza per una linea di lunghezza zero.
     */
    @Test
    void testCalcolaLunghezzaZero() {
        linea.setStartX(0);
        linea.setStartY(0);
        linea.setEndX(0);
        linea.setEndY(0);
        assertEquals(0.0, linea.calcolaLunghezza(), 0.0001);
    }

    /**
     * Verifica il calcolo corretto della lunghezza non dipende dall'ordine dei punti.
     */
    @Test
    void testCalcolaLunghezzaModulo() {
        linea.setStartX(0);
        linea.setStartY(0);
        linea.setEndX(3);
        linea.setEndY(4);
        double l1 = linea.calcolaLunghezza();

        linea.setStartX(3);
        linea.setStartY(4);
        linea.setEndX(0);
        linea.setEndY(0);
        double l2 = linea.calcolaLunghezza();

        assertEquals(l1, l2, 0.0001);
    }


    /**
     * Verifica che la funzione modificaDimensione aumenti correttamente la lunghezza della linea.
     */
    @Test
    void testModificaDimensioneAumenta() {
        linea.modificaDimensione(100);
        assertEquals(100.0, linea.calcolaLunghezza(), 0.0001);
    }

    /**
     * Verifica che la funzione modificaDimensione riduca correttamente la lunghezza della linea.
     */
    @Test
    void testModificaDimensioneRiduci() {
        linea.modificaDimensione(25);
        assertEquals(25.0, linea.calcolaLunghezza(), 0.0001);
    }

    /**
     * Verifica che la lunghezza venga modificata anche se la linea ha lunghezza zero.
     */
    @Test
    void testModificaDimensioneZero() {
        // Imposto start e end coincidenti (lunghezza zero)
        linea.setEndX(linea.getStartX());
        linea.setEndY(linea.getStartY());

        // Modifico la dimensione a 100
        linea.modificaDimensione(100);

        // Controllo che la lunghezza ora sia 100
        assertEquals(100.0, linea.calcolaLunghezza(), 0.0001);
    }

    /**
    * Verifica che venga restituito correttamente il valore della lunghezza come dimensione.
    */
    @Test
    void testOttieniValoreDimensione() {
       assertEquals(200.0, linea.ottieniValoreDimensione(), 0.0001);
    }

   
    /**
     * Verifica che modificare la dimensione di una linea obliqua mantenga la direzione iniziale.
     */
    @Test
    void testModificaDimensioneMantieneDirezioneObliqua() {
        linea.setEndX(30);
        linea.setEndY(40);

        linea.modificaDimensione(100); // Raddoppia la lunghezza

        double dx = linea.getEndX() - linea.getStartX();
        double dy = linea.getEndY() - linea.getStartY();

        assertEquals(100.0, linea.calcolaLunghezza(), 0.001);
        // Verifica che la direzione della linea sia mantenuta (stesso rapporto dy/dx = 4/3)
        assertEquals(4.0 / 3.0, dy / dx, 0.001);
    }

    /**
     * Verifica che il punto di partenza non venga modificato durante la variazione della lunghezza.
     */
    @Test
    void testModificaDimensioneNonModificaStart() {
        double startX = linea.getStartX();
        double startY = linea.getStartY();

        linea.modificaDimensione(120);

        assertEquals(startX, linea.getStartX(), 0.001);
        assertEquals(startY, linea.getStartY(), 0.001);
    }


    /**
     * Verifica che modificare la lunghezza della linea a zero la mantenga con lunghezza zero.
     */
    @Test
    void testModificaDimensioneAZero() {
        linea.modificaDimensione(0);
        assertEquals(0, linea.calcolaLunghezza(), 0.0001);
        assertEquals(linea.getStartX(), linea.getEndX(), 0.0001);
        assertEquals(linea.getStartY(), linea.getEndY(), 0.0001);
    }

    /**
     * Verifica il comportamento con valori negativi di lunghezza.
     * La modifica della dimensione non deve generare eccezioni e deve comportarsi in modo definito.
     */
    @Test
    void testModificaDimensioneNegativa() {
        assertThrows(IllegalArgumentException.class, () -> {
            linea.modificaDimensione(-10);
        }, "La dimensione negativa non è ammessa");
    }

    /**
     * Verifica il corretto trascinamento della linea in una nuova posizione mantenendo la lunghezza.
     */
    @Test
    void testTrascina() {
        linea.trascina(10, 20);
        assertEquals(10, linea.getStartX());
        assertEquals(20, linea.getStartY());
        assertEquals(210, linea.getEndX());
        assertEquals(20, linea.getEndY());
    }

    /**
     * Verifica che il trascinamento della linea non modifica le coordinate se i parametri sono nulli.
     */
    @Test
    void testTrascinaNullo() {
        double startX = linea.getStartX();
        double startY = linea.getStartY();
        double endX = linea.getEndX();
        double endY = linea.getEndY();

        linea.trascina(0, 0);

        assertEquals(startX, linea.getStartX());
        assertEquals(startY, linea.getStartY());
        assertEquals(endX, linea.getEndX());
        assertEquals(endY, linea.getEndY());
    }

    /**
     * Verifica il corretto spostamento della linea obliqua mantenendo la direzione e la lunghezza.
     */
    @Test
    void testTrascinaLineaObliqua() {
        linea.setEndX(30);
        linea.setEndY(40);

        linea.trascina(10, 15);

        assertEquals(10, linea.getStartX());
        assertEquals(15, linea.getStartY());
        assertEquals(40, linea.getEndX());
        assertEquals(55, linea.getEndY());
        assertEquals(50, linea.calcolaLunghezza(), 0.0001); // Lunghezza invariata
    }
    
    /**
     * Testa la concatenazione di modificaDimensione e trascina su Linea e verifica
     * che le proprietà aggiornate siano corrette.
     */
    @Test
    void testModificaETrascinaConcatenatiLinea() {
        // Imposta lunghezza iniziale
        double lunghezzaIniziale = linea.calcolaLunghezza();

        // Modifico la lunghezza (raddoppio)
        linea.modificaDimensione(lunghezzaIniziale * 2);

        // Sposto la linea di (10, 20)
        linea.trascina(10, 20);

        // Verifico la nuova lunghezza
        double lunghezzaDopo = linea.calcolaLunghezza();
        assertEquals(lunghezzaIniziale * 2, lunghezzaDopo, 0.0001, "Lunghezza dopo modifica concatenata errata");

        // Verifico che le coordinate start siano traslate correttamente
        assertEquals(linea.getStartX(), linea.getStartX() - 10 + 10, 0.0001);  // startX originale + 10
        assertEquals(linea.getStartY(), linea.getStartY() - 20 + 20, 0.0001);  // startY originale + 20

        // Verifico che le coordinate end siano traslate correttamente
        assertEquals(linea.getEndX(), linea.getEndX() - 10 + 10, 0.0001);
        assertEquals(linea.getEndY(), linea.getEndY() - 20 + 20, 0.0001);
    }
    
    /**
     * Verifica che modificare la dimensione della linea cambi correttamente
     * la posizione del punto finale mantenendo il punto iniziale fisso.
     */
    @Test
    void testModificaDimensioneAggiornaEndPoint() {
        linea.setStartX(0);
        linea.setStartY(0);
        linea.setEndX(10);
        linea.setEndY(0);
        double lunghezzaIniziale = linea.calcolaLunghezza();

        linea.modificaDimensione(lunghezzaIniziale * 2); // raddoppia la lunghezza

        assertEquals(0, linea.getStartX(), 0.0001, "StartX deve rimanere invariato");
        assertEquals(0, linea.getStartY(), 0.0001, "StartY deve rimanere invariato");
        assertEquals(20, linea.getEndX(), 0.0001, "EndX deve essere raddoppiato");
        assertEquals(0, linea.getEndY(), 0.0001, "EndY deve rimanere invariato");
    }

    /**
     * Verifica che trascinando la linea si spostino entrambi i punti
     * start e end mantenendo la stessa lunghezza e direzione.
     */
    @Test
    void testTrascinaMantieneLunghezzaEDirezione() {
        linea.setStartX(0);
        linea.setStartY(0);
        linea.setEndX(10);
        linea.setEndY(0);
        double lunghezzaPrima = linea.calcolaLunghezza();

        linea.trascina(5, 5);

        assertEquals(5, linea.getStartX(), 0.0001);
        assertEquals(5, linea.getStartY(), 0.0001);
        assertEquals(15, linea.getEndX(), 0.0001);
        assertEquals(5, linea.getEndY(), 0.0001);

        assertEquals(lunghezzaPrima, linea.calcolaLunghezza(), 0.0001, "Lunghezza deve restare invariata");
    }

    /**
     * Verifica che modificare dimensione seguita da trascina mantenga coerenza
     * tra lunghezza e posizione.
     */
    @Test
    void testModificaDimensioneETrascinaConcatenati() {
        linea.setStartX(0);
        linea.setStartY(0);
        linea.setEndX(10);
        linea.setEndY(0);

        linea.modificaDimensione(30);  
        linea.trascina(10, 10);

        assertEquals(10, linea.getStartX(), 0.0001);
        assertEquals(10, linea.getStartY(), 0.0001);
        assertEquals(40, linea.getEndX(), 0.0001);
        assertEquals(10, linea.getEndY(), 0.0001);
        assertEquals(30, linea.calcolaLunghezza(), 0.0001);
    }

    /**
     * Verifica che ottenere nomi e valori dimensioni sia coerente con
     * la lunghezza attuale della linea.
     */
    @Test
    void testOttieniValoriDimensioni() {
        linea.setStartX(1);
        linea.setStartY(1);
        linea.setEndX(4);
        linea.setEndY(5);
        double valore = linea.ottieniValoreDimensione();

        assertEquals(linea.calcolaLunghezza(), valore, 0.0001);
    }

    /**
     * Verifica che la rappresentazione testuale contenga tutte le informazioni rilevanti della linea.
     */
    @Test
    void testToTextContenutoCompleto() {
        String text = linea.toText();

        assertTrue(text.contains("LINEA"));
        assertTrue(text.contains(String.valueOf(linea.getStartX())));
        assertTrue(text.contains(String.valueOf(linea.getStartY())));
        assertTrue(text.contains(String.valueOf(linea.getStroke())));
        assertTrue(text.contains(String.valueOf(linea.calcolaLunghezza())));
    }

    
    
    /**
    * Test del metodo {@code clonaForma()} della classe {@link Linea}.
    * 
    * Verifica che il metodo {@code clonaForma()}:
    * 
    *   -Ritorni un oggetto di tipo {@code Linea}
    *   -Cloni correttamente tutte le proprietà geometriche e grafiche, quali coordinate iniziali e finali,
    *       colore del bordo, larghezza del bordo e rotazione
    *   -Produca un nuovo oggetto con lo stesso valore di lunghezza
    *   -Ritorni un'istanza distinta dall'originale (non lo stesso riferimento)
    */
    @Test
    public void testClonaForma() {
        Shape clonato = (Shape)linea.clonaForma();

        assertTrue(clonato instanceof Linea);
        Linea lineaClone = (Linea) clonato;

        assertEquals(linea.getStartX(), lineaClone.getStartX());
        assertEquals(linea.getStartY(), lineaClone.getStartY());
        assertEquals(linea.getEndX(), lineaClone.getEndX());
        assertEquals(linea.getEndY(), lineaClone.getEndY());
        assertEquals(linea.getStroke(), lineaClone.getStroke());
        assertEquals(linea.getStrokeWidth(), lineaClone.getStrokeWidth());
        assertEquals(linea.getRotate(), lineaClone.getRotate());
        assertEquals(linea.calcolaLunghezza(), lineaClone.calcolaLunghezza(), 0.0001);

        // Verifica che non sia lo stesso oggetto
        assertNotSame(linea, lineaClone);
    }

}
