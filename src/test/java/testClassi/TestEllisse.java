package testClassi;

import com.gruppo07iz.geometrika.forme.Ellisse;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per la classe {@link Ellisse}.
 * Verifica il corretto funzionamento dei metodi costruttori e delle funzionalità
 * definite dall'interfaccia {@code FormaBidimensionale}.
 */
public class TestEllisse {

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
    }

    @AfterEach
    public void tearDown() {
        // Cleanup dopo ogni test (se necessario)
    }

    /**
     * Verifica che il costruttore inizializzi correttamente l'ellisse.
     */
    @Test
    void testCostruttoreEllisse() {
        Color bordo = Color.BLACK;
        Color riemp = Color.BLUE;
        Ellisse e = new Ellisse(100, 200, bordo, riemp);

        assertEquals(100.0, e.getCenterX(), 0.0001);
        assertEquals(200.0, e.getCenterY(), 0.0001);
        assertEquals(70.0, e.getRadiusX(), 0.0001);
        assertEquals(30.0, e.getRadiusY(), 0.0001);
        assertEquals(bordo, e.getStroke());
        assertEquals(riemp, e.getFill());
        assertEquals(3.0, e.getStrokeWidth(), 0.0001);
    }

    /**
     * Verifica il corretto funzionamento del metodo modificaDimensioni.
     */
    @Test
    void testModificaDimensioni() {
        Ellisse e = new Ellisse(0, 0, Color.BLACK, Color.WHITE);
        e.modificaDimensioni(50.5, 40.25);
        assertEquals(50.5, e.getRadiusX(), 0.0001);
        assertEquals(40.25, e.getRadiusY(), 0.0001);
    }
    /**
     * Verifica il corretto funzionamento del metodo modificaDimensioni con valori negativi.
     */
    @Test
    void testModificaDimensioniNegativi() {
        Ellisse e = new Ellisse(0, 0, Color.BLACK, Color.WHITE);
        e.modificaDimensioni(-10.5, -20.5);
        assertEquals(-10.5, e.getRadiusX(), 0.0001);
        assertEquals(-20.5, e.getRadiusY(), 0.0001);
    }  

    /**
     * Verifica il corretto funzionamento del metodo modificaDimensioni con valori zero.
     */
    @Test
    void testModificaDimensioniZero() {
        Ellisse e = new Ellisse(0, 0, Color.BLACK, Color.WHITE);
        e.modificaDimensioni(0, 0);
        assertEquals(0.0, e.getRadiusX(), 0.0001);
        assertEquals(0.0, e.getRadiusY(), 0.0001);
    } 

    /**
     * Verifica che il metodo trascina sposti correttamente il centro dell’ellisse.
     */
    @Test
    void testTrascina() {
        Ellisse e = new Ellisse(10, 20, Color.BLACK, Color.WHITE);
        e.trascina(5.5, -10.5);
        assertEquals(15.5, e.getCenterX(), 0.0001);
        assertEquals(9.5, e.getCenterY(), 0.0001);
    }

    /**
     * Verifica il comportamento del metodo trascina quando dx e dy sono negativi.
     */
    @Test
    void testTrascinaNegativo() {
        Ellisse e = new Ellisse(10, 20, Color.BLACK, Color.WHITE);
        e.trascina(-5, -10);
        assertEquals(5.0, e.getCenterX(), 0.0001);
        assertEquals(10.0, e.getCenterY(), 0.0001);
    }

    /**
     * Verifica il comportamento del metodo trascina quando dx e dy sono zero.
     */
    @Test
    void testTrascinaNullo() {
        Ellisse e = new Ellisse(10, 10, Color.BLACK, Color.WHITE);
        e.trascina(0, 0);
        assertEquals(10.0, e.getCenterX(), 0.0001);
        assertEquals(10.0, e.getCenterY(), 0.0001);
    }

    /**
     * Questo test verifica che il numero di nomi delle dimensioni sia corretto.
     */
    @Test
    void testOttieniNomiDimensioniLunghezza() {
        Ellisse e = new Ellisse(0, 0, Color.BLACK, Color.WHITE);
        String[] nomi = e.ottieniNomiDimensioni();
        assertEquals(2, nomi.length, "Devono esserci esattamente 2 nomi per le dimensioni");
    }

    /**
     * Verifica i nomi delle dimensioni restituiti dal metodo ottieniNomiDimensioni.
     */
    @Test
    void testOttieniNomiDimensioni() {
        Ellisse e = new Ellisse(0, 0, Color.BLACK, Color.WHITE);
        String[] nomi = e.ottieniNomiDimensioni();
        assertArrayEquals(new String[] {"RaggioX", "RaggioY"}, nomi);
    }

    /**
     * Questo test verifica che il numero di valori delle dimensioni sia corretto.
     */
    @Test
    void testOttieniValoriDimensioniLunghezza() {
        Ellisse e = new Ellisse(0, 0, Color.BLACK, Color.WHITE);
        double[] valori = e.ottieniValoriDimensioni();
        assertEquals(2, valori.length, "Devono esserci esattamente 2 valori per le dimensioni");
    }

    /**
     * Verifica i valori delle dimensioni di creazione e restituiti dal metodo ottieniValoriDimensioni.
     */
    @Test
    void testOttieniValoriDimensioni() {
        Ellisse e = new Ellisse(0, 0, Color.BLACK, Color.WHITE);
        double[] valori = e.ottieniValoriDimensioni();
        assertArrayEquals(new double[] {70, 30}, valori, 0.0001);
    }

    /**
     * Verifica i valori delle dimensioni modificati e restituiti dal metodo ottieniValoriDimensioni.
     */
    @Test
    void testOttieniValoriDimensioni2() {
        Ellisse e = new Ellisse(0, 0, Color.BLACK, Color.WHITE);
        e.modificaDimensioni(25.75, 35.25);
        double[] valori = e.ottieniValoriDimensioni();
        assertArrayEquals(new double[] {25.75, 35.25}, valori, 0.0001);
    }


    /**
     * Testa la concatenazione di modificaDimensioni e trascina e verifica
     * che le proprietà aggiornate siano corrette.
     */
    @Test
    void testModificaETrascinaConcatenati() {
        Ellisse e = new Ellisse(10, 20, Color.RED, Color.GREEN);
        e.modificaDimensioni(50, 80);
        e.trascina(30, 40);

        // Verifica raggi modificati correttamente
        assertEquals(50, e.getRadiusX(), 0.0001, "Raggio X dopo modifica concatenata errato");
        assertEquals(80, e.getRadiusY(), 0.0001, "Raggio Y dopo modifica concatenata errato");

        // Verifica posizione aggiornata correttamente
        assertEquals(40, e.getCenterX(), 0.0001, "Coordinata X dopo trascina errata");
        assertEquals(60, e.getCenterY(), 0.0001, "Coordinata Y dopo trascina errata");

        // Verifica colori non cambiati
        assertEquals(Color.RED, e.getStroke(), "Colore bordo non deve cambiare");
        assertEquals(Color.GREEN, e.getFill(), "Colore riempimento non deve cambiare");

        // Verifica spessore bordo invariato
        assertEquals(3.0, e.getStrokeWidth(), 0.0001, "Spessore bordo non deve cambiare");
    }

    /**
     * Verifica che chiamare trascina più volte accumuli correttamente
     * lo spostamento della posizione centrale.
     */
    @Test
    void testTrascinaMultiplo() {
        Ellisse e = new Ellisse(10, 10, Color.BLUE, Color.RED);
        e.trascina(5, 5);
        e.trascina(-3, 2);
        assertEquals(12, e.getCenterX(), 0.0001, "Coordinata X dopo trascina multiplo errata");
        assertEquals(17, e.getCenterY(), 0.0001, "Coordinata Y dopo trascina multiplo errata");
    }

    /**
     * Verifica che modificare le dimensioni e poi ottenere i valori
     * restituisce i dati coerenti con le modifiche effettuate.
     */
    @Test
    void testModificaDimensioniEOttieniValoriCoerenti() {
        Ellisse e = new Ellisse(0, 0, Color.BLACK, Color.WHITE);
        e.modificaDimensioni(60, 90);
        double[] valori = e.ottieniValoriDimensioni();
        assertEquals(60, valori[0], 0.0001, "Raggio X in ottieniValoriDimensioni non coerente");
        assertEquals(90, valori[1], 0.0001, "Raggio Y in ottieniValoriDimensioni non coerente");
    }

    /**
     * Verifica che modificare dimensioni con valori negativi
     * seguiti da trascina aggiorna correttamente posizione e dimensioni.
     */
    @Test
    void testModificaNegativiETrascina() {
        Ellisse e = new Ellisse(5, 5, Color.GRAY, Color.YELLOW);
        e.modificaDimensioni(-30, -40);
        e.trascina(10, 10);

        assertEquals(-30, e.getRadiusX(), 0.0001, "Raggio X negativo non mantenuto");
        assertEquals(-40, e.getRadiusY(), 0.0001, "Raggio Y negativo non mantenuto");
        assertEquals(15, e.getCenterX(), 0.0001, "Centro X dopo trascina errato");
        assertEquals(15, e.getCenterY(), 0.0001, "Centro Y dopo trascina errato");
    }

    /**
     * Verifica che il metodo modificaDimensioni seguito da modificaDimensioni
     * con nuovi valori aggiorni sempre correttamente le dimensioni.
     */
    @Test
    void testModificaDimensioniRipetuta() {
        Ellisse e = new Ellisse(0, 0, Color.BLACK, Color.WHITE);
        e.modificaDimensioni(10, 20);
        e.modificaDimensioni(30, 40);
        assertEquals(30, e.getRadiusX(), 0.0001, "Raggio X dopo seconda modifica errato");
        assertEquals(40, e.getRadiusY(), 0.0001, "Raggio Y dopo seconda modifica errato");
    }

    /**
     * Verifica che due ellissi create con gli stessi parametri siano due oggetti distinti
     */
    @Test
    void testEllissiIndipendenti() {
        Ellisse e1 = new Ellisse(0, 0, Color.BLACK, Color.WHITE);
        Ellisse e2 = new Ellisse(0, 0, Color.BLACK, Color.WHITE);
        e1.modificaDimensioni(10, 20);
        assertNotEquals(e1.getRadiusX(), e2.getRadiusX(), 0.0001);
        assertNotEquals(e1.getRadiusY(), e2.getRadiusY(), 0.0001);
    }

    /**
     * Verifica la corretta formattazione del metodo toText.
     */
    @Test
    void testToText() {
        Ellisse e = new Ellisse(30, 40, Color.GREEN, Color.LIGHTGRAY);
        e.modificaDimensioni(10, 15);
        String testo = e.toText();

        assertTrue(testo.startsWith("ELLISSE"));
        assertTrue(testo.contains("30.0"));
        assertTrue(testo.contains("40.0"));
        assertTrue(testo.contains("10.0"));
        assertTrue(testo.contains("15.0"));
        assertTrue(testo.contains(e.getStroke().toString()));
        assertTrue(testo.contains(e.getFill().toString()));
    }
    
    /**
     * Verifica che modificando dimensioni e poi chiamando toText,
     * la stringa rispecchi correttamente i valori aggiornati.
     */
    @Test
    void testModificaDimensioniEToTextCoerenza() {
        Ellisse e = new Ellisse(0, 0, Color.BLACK, Color.WHITE);
        e.modificaDimensioni(45, 55);
        String testo = e.toText();
        assertTrue(testo.contains("45.0"), "toText deve contenere raggioX aggiornato");
        assertTrue(testo.contains("55.0"), "toText deve contenere raggioY aggiornato");
    }
    
    /**
    * Crea e restituisce una copia dell'oggetto {@code Ellisse} corrente.
    * 
    * La copia avrà:
    *  -Le stesse coordinate del centro ({@code centerX}, {@code centerY})
    *  -Lo stesso colore di bordo e di riempimento
    *  -Le stesse dimensioni (raggio X e raggio Y
    * 
    * Il metodo restituisce un nuovo oggetto {@code Ellisse} distinto dall'originale.
    *
    * @return una nuova {@code Ellisse} con le stesse proprietà dell'originale
    */
    @Test
    public void testClonaForma() {
        // Arrange
        Ellisse ellisseOriginale = new Ellisse(100, 150, Color.BLUE, Color.YELLOW);
        ellisseOriginale.modificaDimensioni(50, 80);

        // Act
        Shape formaClonata = ellisseOriginale.clonaForma();

        // Assert
        assertNotSame(ellisseOriginale, formaClonata, "La forma clonata deve essere un oggetto diverso");
        assertTrue(formaClonata instanceof Ellisse, "La forma clonata deve essere un'istanza di Ellisse");

        Ellisse ellisseClonata = (Ellisse) formaClonata;
        assertEquals(ellisseOriginale.getCenterX(), ellisseClonata.getCenterX(), "CenterX non corrisponde");
        assertEquals(ellisseOriginale.getCenterY(), ellisseClonata.getCenterY(), "CenterY non corrisponde");
        assertEquals(ellisseOriginale.getRadiusX(), ellisseClonata.getRadiusX(), "RadiusX non corrisponde");
        assertEquals(ellisseOriginale.getRadiusY(), ellisseClonata.getRadiusY(), "RadiusY non corrisponde");
        assertEquals(ellisseOriginale.getStroke(), ellisseClonata.getStroke(), "Stroke color non corrisponde");
        assertEquals(ellisseOriginale.getFill(), ellisseClonata.getFill(), "Fill color non corrisponde");
    }

}
