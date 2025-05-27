package testClassi;

import com.gruppo07iz.geometrika.forme.Rettangolo;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestRettangolo {

    private Rettangolo rettangolo;

    /**
     * Setup iniziale: crea un rettangolo con posizione (10,20), bordi blu, riempimento giallo.
     */
    @BeforeEach
    void setUp() {
        rettangolo = new Rettangolo(10, 20, Color.BLUE, Color.YELLOW);
    }

    /**
     * Testa che il costruttore imposti correttamente posizione, dimensioni e colori.
     */
    @Test
    void testCostruttore() {
        assertEquals(10, rettangolo.getX(), "Coordinata X iniziale errata");
        assertEquals(20, rettangolo.getY(), "Coordinata Y iniziale errata");
        assertEquals(100, rettangolo.getWidth(), "Larghezza iniziale errata");
        assertEquals(60, rettangolo.getHeight(), "Altezza iniziale errata");
        assertEquals(Color.BLUE, rettangolo.getStroke(), "Colore bordo errato");
        assertEquals(Color.YELLOW, rettangolo.getFill(), "Colore riempimento errato");
        assertEquals(3, rettangolo.getStrokeWidth(), "Spessore bordo errato");
    }
     
    
    /**
     * Testa modificaDimensioni con valori validi e valori limite.
     */
    @Test
    void testModificaDimensioni() {
        // Modifica con valori normali
        rettangolo.modificaDimensioni(80, 120);
        assertEquals(80, rettangolo.getHeight(), "Altezza dopo modifica errata");
        assertEquals(120, rettangolo.getWidth(), "Larghezza dopo modifica errata");

        // Modifica con zero (controlla comportamento)
        rettangolo.modificaDimensioni(0, 0);
        assertEquals(0, rettangolo.getHeight(), "Altezza a zero errata");
        assertEquals(0, rettangolo.getWidth(), "Larghezza a zero errata");

        // Modifica con valori molto grandi
        rettangolo.modificaDimensioni(10000, 5000);
        assertEquals(10000, rettangolo.getHeight(), "Altezza grande errata");
        assertEquals(5000, rettangolo.getWidth(), "Larghezza grande errata");
    }

    /**
     * Testa il metodo trascina per spostare la forma in modo corretto.
     * Verifica anche spostamenti negativi e con decimali.
     */
    @Test
    void testTrascina() {
        rettangolo.trascina(15, -5);
        assertEquals(25, rettangolo.getX(), "Coordinata X dopo trascina errata");
        assertEquals(15, rettangolo.getY(), "Coordinata Y dopo trascina errata");

        rettangolo.trascina(-10.5, 7.25);
        assertEquals(14.5, rettangolo.getX(), 0.0001, "Coordinata X dopo trascina con decimali errata");
        assertEquals(22.25, rettangolo.getY(), 0.0001, "Coordinata Y dopo trascina con decimali errata");
    }

    /**
     * Testa il metodo trascina con valori pari a zero e verifica che le coordinate siano invariate.
     */
    @Test
    void testTrascinaZero() {
        double xIniziale = rettangolo.getX();
        double yIniziale = rettangolo.getY();
        rettangolo.trascina(0, 0);
        assertEquals(xIniziale, rettangolo.getX(), "X non dovrebbe cambiare");
        assertEquals(yIniziale, rettangolo.getY(), "Y non dovrebbe cambiare");
    }

    /**
     * Questo test verifica che il numero di nomi delle dimensioni sia corretto.
     */
    @Test
    void testOttieniNomiDimensioniLunghezza() {
        String[] nomi = rettangolo.ottieniNomiDimensioni();
        assertEquals(2, nomi.length, "Devono esserci esattamente 2 nomi per le dimensioni");
    }

    /**
     * Testa che i nomi delle dimensioni restituiti siano corretti.
     */
    @Test
    void testOttieniNomiDimensioni() {
        String[] nomi = rettangolo.ottieniNomiDimensioni();
        assertArrayEquals(new String[] { "Altezza", "Larghezza" }, nomi, "Nomi dimensioni errati");
    }

    /**
     * Questo test verifica che il numero di valori delle dimensioni sia corretto.
     */
    @Test
    void testOttieniValoriDimensioniLunghezza() {
        double[] valori = rettangolo.ottieniValoriDimensioni();
        assertEquals(2, valori.length, "Devono esserci esattamente 2 valori per le dimensioni");
    }

    /**
     * Testa che i valori delle dimensioni corrispondano alle proprietà altezza e larghezza.
     */
    @Test
    void testOttieniValoriDimensioni() {
        double[] valori = rettangolo.ottieniValoriDimensioni();
        assertEquals(60.0, valori[0], 0.0001, "Valore altezza errato");
        assertEquals(100.0, valori[1], 0.0001, "Valore larghezza errato");
    }


    /**
     * Testa la concatenazione di modificaDimensioni e trascina e verifica
     * che le proprietà aggiornate siano corrette.
     */
    @Test
    void testModificaETrascinaConcatenati() {
        // Modifico altezza e larghezza
        rettangolo.modificaDimensioni(200, 300);

        // Sposto il rettangolo
        rettangolo.trascina(50, 50);

        // Controllo dimensioni modificate
        assertEquals(200, rettangolo.getHeight(), "Altezza dopo modifica concatenata errata");
        assertEquals(300, rettangolo.getWidth(), "Larghezza dopo modifica concatenata errata");

        // Controllo posizione modificata
        assertEquals(60, rettangolo.getX(), "Coordinata X dopo trascina errata"); // 10 + 50
        assertEquals(70, rettangolo.getY(), "Coordinata Y dopo trascina errata"); // 20 + 50

        // Controllo colore bordo e riempimento invariati
        assertEquals(Color.BLUE, rettangolo.getStroke(), "Colore bordo non deve cambiare");
        assertEquals(Color.YELLOW, rettangolo.getFill(), "Colore riempimento non deve cambiare");

        // Controllo spessore bordo invariato
        assertEquals(3, rettangolo.getStrokeWidth(), "Spessore bordo non deve cambiare");
    }
    

    /**
     * Verifica che chiamare trascina più volte accumuli correttamente
     * lo spostamento della posizione (X, Y).
     */
    @Test
    void testTrascinaMultiplo() {
        Rettangolo r = new Rettangolo(10, 20, Color.BLUE, Color.RED);
        r.trascina(5, 5);
        r.trascina(-3, 2);
        assertEquals(12, r.getX(), 0.0001, "Coordinata X dopo trascina multiplo errata");
        assertEquals(27, r.getY(), 0.0001, "Coordinata Y dopo trascina multiplo errata");
    }

    /**
     * Verifica che modificare le dimensioni e poi ottenere i valori
     * restituisce i dati coerenti con le modifiche effettuate.
     */
    @Test
    void testModificaDimensioniEOttieniValoriCoerenti() {
        rettangolo.modificaDimensioni(120, 80);
        double[] valori = rettangolo.ottieniValoriDimensioni();
        assertEquals(120, valori[0], 0.0001, "Altezza in ottieniValoriDimensioni non coerente");
        assertEquals(80, valori[1], 0.0001, "Larghezza in ottieniValoriDimensioni non coerente");
    }

    /**
     * Verifica che modificare dimensioni con valori negativi
     * si riflette nelle proprietà.
     */
    @Test
    void testModificaDimensioniNegativi() {
        rettangolo.modificaDimensioni(-30, -40);
        double[] valori = rettangolo.ottieniValoriDimensioni();
        assertEquals(-30, valori[0], 0.0001, "Altezza negativa non mantenuta");
        assertEquals(-40, valori[1], 0.0001, "Larghezza negativa non mantenuta");
    }

    /**
     * Verifica che il metodo modificaDimensioni seguito da modificaDimensioni
     * con nuovi valori aggiorni sempre correttamente le dimensioni.
     */
    @Test
    void testModificaDimensioniRipetuta() {
        rettangolo.modificaDimensioni(10, 20);
        rettangolo.modificaDimensioni(30, 40);
        assertEquals(30, rettangolo.getHeight(), 0.0001, "Altezza dopo seconda modifica errata");
        assertEquals(40, rettangolo.getWidth(), 0.0001, "Larghezza dopo seconda modifica errata");
    }


    /**
     * Testa il metodo toText che deve contenere tutte le informazioni chiave.
     */
    @Test
    void testToText() {
        String text = rettangolo.toText();

        assertTrue(text.startsWith("RETTANGOLO"), "Testo non inizia con RETTANGOLO");
        assertTrue(text.contains("10.0"), "Testo non contiene coordinata X");
        assertTrue(text.contains("20.0"), "Testo non contiene coordinata Y");
        assertTrue(text.contains("100.0"), "Testo non contiene larghezza");
        assertTrue(text.contains("60.0"), "Testo non contiene altezza");
        assertTrue(text.contains(rettangolo.getStroke().toString()), "Testo non contiene colore bordo");
        assertTrue(text.contains(rettangolo.getFill().toString()), "Testo non contiene colore riempimento");
    }

    /**
     * Verifica che modifiche multiple alle dimensioni si riflettano correttamente
     * nel metodo toText dopo ogni aggiornamento.
     */
    @Test
    void testModificheSequenzialiDimensioniEToText() {
        Rettangolo r = new Rettangolo(0, 0, Color.BLACK, Color.WHITE);
        r.modificaDimensioni(200, 300);
        String testo1 = r.toText();
        r.modificaDimensioni(180, 250);
        String testo2 = r.toText();
        
        assertFalse(testo1.contains("180.0"), "Testo1 non deve contenere la seconda modifica");
        assertTrue(testo2.contains("180.0"), "Testo2 deve contenere la larghezza aggiornata");
        assertTrue(testo2.contains("250.0"), "Testo2 deve contenere l'altezza aggiornata");
    }


    /**
     * Verifica che modificando dimensioni e poi chiamando toText,
     * la stringa rispecchi correttamente i valori aggiornati.
     */
    @Test
    void testModificaDimensioniEToTextCoerenza() {
        Rettangolo r = new Rettangolo(0, 0, Color.BLACK, Color.WHITE);
        r.modificaDimensioni(150, 250);
        String testo = r.toText();
        assertTrue(testo.contains("150.0"), "toText deve contenere altezza aggiornata");
        assertTrue(testo.contains("250.0"), "toText deve contenere larghezza aggiornata");
    }
    
    /**
    * Test del metodo {@code clonaForma()} della classe {@link Rettangolo}.
    * Verifica che il clone:
    *   -Sia un'istanza di {@code Rettangolo}
    *   -Abbia le stesse proprietà dell'oggetto originale (posizione, colori, dimensioni, rotazione)
    *   -Sia un oggetto distinto dall'originale (cioè, non lo stesso riferimento)
    */
    @Test
    public void testClonaForma() {
        // Crea un rettangolo originale
        rettangolo.modificaDimensioni(50, 100);
        rettangolo.setRotate(45);

        // Clona il rettangolo
        Shape clone = rettangolo.clonaForma();

        // Verifica che il clone sia un Rettangolo
        assertTrue(clone instanceof Rettangolo);

        Rettangolo rettangoloClonato = (Rettangolo) clone;

        // Verifica che le proprietà siano uguali
        assertEquals(rettangolo.getX(), rettangoloClonato.getX());
        assertEquals(rettangolo.getY(), rettangoloClonato.getY());
        assertEquals(rettangolo.getStroke(), rettangoloClonato.getStroke());
        assertEquals(rettangolo.getFill(), rettangoloClonato.getFill());
        assertEquals(rettangolo.getHeight(), rettangoloClonato.getHeight());
        assertEquals(rettangolo.getWidth(), rettangoloClonato.getWidth());
        assertEquals(rettangolo.getRotate(), rettangoloClonato.getRotate());

        // Verifica che siano oggetti distinti
        assertNotSame(rettangolo, rettangoloClonato);
    }

    /**
     * Verifica il cloning di un rettangolo con valori non standard
     * e proprietà avanzate (rotazione complessa e trasformazioni).
     */
    @Test
    public void testClonaFormaConRotazioneComplessa() {
        rettangolo.setRotate(135);
        rettangolo.getTransforms().add(new Scale(2, 1.5)); // Esempio di trasformazione aggiuntiva
        
        Rettangolo clone = (Rettangolo)rettangolo.clonaForma();
        
        assertEquals(135, clone.getRotate(), 0.01, "Deve mantenere la rotazione complessa");
        assertNotSame(rettangolo.getTransforms(), clone.getTransforms(), "Deve clonare le trasformazioni come nuovo oggetto");
    }

    
}
