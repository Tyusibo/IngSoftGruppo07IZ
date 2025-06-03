package testClassi;

import com.gruppo07iz.geometrika.forme.Gruppo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestGruppo {
    
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
    }

    @AfterEach
    public void tearDown() {
        // Cleanup dopo ogni test (se necessario)
    }
    
    @Test
    void testCostruttore() {
        Gruppo gruppo = new Gruppo ();        
        assertNotNull(gruppo.getVettoreForme(), "La lista vettoreForme non deve essere null");
        assertTrue(gruppo.getVettoreForme().isEmpty(), "La lista vettoreForme deve essere vuota");
        assertEquals(0, gruppo.getAngolo(), "Angolo iniziale dovrebbe essere 0");
        
    }
}
