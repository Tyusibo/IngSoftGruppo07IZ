package testClassi;
import com.gruppo07iz.geometrika.DashboardController;
import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.command.*;
import com.gruppo07iz.geometrika.forme.FormaBidimensionale;
import com.gruppo07iz.geometrika.forme.FormaMonodimensionale;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import com.gruppo07iz.geometrika.forme.Gruppo;
import com.gruppo07iz.geometrika.forme.Poligono;
import com.gruppo07iz.geometrika.forme.TipoFormaRegolare;
import com.gruppo07iz.geometrika.forme.Rettangolo;
import com.gruppo07iz.geometrika.forme.Testo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author giorg
 */
public class TestComandi {
    private static DashboardController controller;
    private static Model modello;

    @BeforeEach
    public void setup() throws InterruptedException {
        if (!Platform.isFxApplicationThread()) {
            CountDownLatch latch = new CountDownLatch(1);
            new Thread(() -> {
                try {
                    Platform.startup(() -> {
                        latch.countDown();
                    });
                } catch (IllegalStateException e) {
                    latch.countDown();
                }
            }).start();
            latch.await();
        }
        modello = new Model();
        controller = new DashboardController();
        controller.lavagna = new Pane();
        controller.pilaComandi = new Stack<>();
        controller.pannelloModifica = new VBox();
        controller.mappaFormeGruppi = new HashMap<>();
    }


    /**
     * Test per verificare il comando di creazione di forme regolari.
     * Controlla la corretta creazione della forma, le proprietà grafiche impostate
     * e il corretto funzionamento dell'operazione di annullamento.
     */
    @Test
    public void testComandoCreazioneFormaRegolare(){
        ObservableList <Node> figliLavagna = controller.lavagna.getChildren();
        ComandoCreazioneFormaRegolare comando = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        controller.eseguiComando(comando);
        
        assertTrue(comando.getFormaCreata() instanceof Shape); //verifica che l'esecuzione del comando ha creato una Shape
        //verifica che la forma sia stata creata con le proprietà desiderate
        assertEquals(Color.RED, comando.getFormaCreata().getColoreRiempimento());
        assertEquals(Color.BLUE, comando.getFormaCreata().getColoreBordo());
        assert(controller.lavagna.getChildren().contains(comando.getFormaCreata()));
        
        controller.annullaUltimoComando();
        assertEquals(figliLavagna,controller.lavagna.getChildren());  //verifica che l'annullamento del comando è andato a buon fine
    }
    
     /**
     * Test per verificare il comando di creazione del testo.
     * Controlla la corretta creazione del testo, le proprietà grafiche impostate
     * e il corretto funzionamento dell'operazione di annullamento.
     */
    @Test
    public void testComandoCreazioneTesto(){
        ObservableList <Node> figliLavagna = controller.lavagna.getChildren();
        String testo = new String("Comando testo");
        ComandoCreazioneTesto comando = new ComandoCreazioneTesto(modello, controller.lavagna, testo, 0, 0, Color.RED, Color.BLUE, 20.0);
        controller.eseguiComando(comando);
        
        assertTrue(comando.getTesto() instanceof Shape); //verifica che l'esecuzione del comando ha creato una Shape
        assertTrue(comando.getTesto() instanceof Testo); //verifica che l'esecuzione del comando ha creato un Testo
        //verifica che il testo sia stato creato con le proprietà desiderate
        assertEquals(Color.RED, comando.getTesto().getColoreRiempimento());
        assertEquals(Color.BLUE, comando.getTesto().getColoreBordo());
        assert(controller.lavagna.getChildren().contains(comando.getTesto()));
        
        controller.annullaUltimoComando();
        assertEquals(figliLavagna,controller.lavagna.getChildren());  //verifica che l'annullamento del comando è andato a buon fine
    }
    
    /**
     * Test per verificare il comando di creazione del poligono.
     * Controlla la corretta creazione del poligono, con le proprietà grafiche impostate,
     * e il corretto funzionamento dell'operazione di annullamento.
     */
    @Test
    public void testComandoCreazionePoligono(){
        ObservableList <Node> figliLavagna = controller.lavagna.getChildren();
        List <Double> puntiPoligono = new ArrayList<>();
        puntiPoligono.add(10.0);
        puntiPoligono.add(10.0);
        puntiPoligono.add(20.0);
        puntiPoligono.add(10.0);
        puntiPoligono.add(20.0);
        puntiPoligono.add(20.0);
        puntiPoligono.add(10.0);
        puntiPoligono.add(20.0);
        ComandoCreazionePoligono comando = new ComandoCreazionePoligono(modello, controller.lavagna, puntiPoligono, Color.RED, Color.BLUE);
        controller.eseguiComando(comando);
        assertTrue(comando.getFormaCreata() instanceof Shape); //verifica che l'esecuzione del comando ha creato una Shape
        assertTrue(comando.getFormaCreata() instanceof Poligono); //verifica che l'esecuzione del comando ha creato un Poligono
        //verifica che la forma sia stata creata con le proprietà desiderate
        assertEquals(Color.RED, comando.getFormaCreata().getColoreRiempimento());
        assertEquals(Color.BLUE, comando.getFormaCreata().getColoreBordo());
        assert(controller.lavagna.getChildren().contains(comando.getFormaCreata()));
        
        controller.annullaUltimoComando();
        assertEquals(figliLavagna,controller.lavagna.getChildren());  //verifica che l'annullamento del comando è andato a buon fine
    }
    
    /**
    * Verifica il corretto comportamento del comando {@link ComandoCreazioneGruppo}.
    * Il test esegue i seguenti passaggi:
    *     Crea due forme regolari e le aggiunge alla lavagna
    *     Raggruppa le due forme in un nuovo gruppo
    *     Verifica che il gruppo contenga le forme previste
    *     Applica l'operazione di undo
    *     Verifica che il gruppo sia stato svuotato e che le forme non siano più associate a un gruppo
    */
    @Test
    public void testComandoCreazioneGruppoFormeRegolari() {
        // Step 1: Creazione delle forme e aggiunta alla lavagna
        ComandoCreazioneFormaRegolare comando1 = new ComandoCreazioneFormaRegolare(modello, controller.lavagna,
                TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        controller.eseguiComando(comando1);
        FormaPersonalizzabile forma1 = comando1.getFormaCreata();

        ComandoCreazioneFormaRegolare comando2 = new ComandoCreazioneFormaRegolare(modello, controller.lavagna,
                TipoFormaRegolare.RETTANGOLO, 10, 10, Color.BLUE, Color.RED);
        controller.eseguiComando(comando2);
        FormaPersonalizzabile forma2 = comando2.getFormaCreata();

        // Step 2: Raccolta delle forme da raggruppare
        List<FormaPersonalizzabile> formeDaRaggruppare = List.of(forma1, forma2);

        // Step 3: Esecuzione del comando di creazione gruppo
        ComandoCreazioneGruppo comandoGruppo = new ComandoCreazioneGruppo(modello, controller.lavagna,
                formeDaRaggruppare, controller.mappaFormeGruppi);
        controller.eseguiComando(comandoGruppo);
        Gruppo gruppoCreato = comandoGruppo.getGruppoCreato();

        // Step 4: Verifiche
        assertNotNull(gruppoCreato, "Il gruppo non dovrebbe essere null");
        assertEquals(2, gruppoCreato.getVettoreForme().size(), "Il gruppo deve contenere esattamente due forme");
        assertTrue(gruppoCreato.getVettoreForme().contains(forma1));
        assertTrue(gruppoCreato.getVettoreForme().contains(forma2));

        // Verifica (opzionale) che la lavagna contenga ancora le forme
        assertTrue(controller.lavagna.getChildren().contains(forma1));
        assertTrue(controller.lavagna.getChildren().contains(forma2));

        // Step 5: Undo
        controller.annullaUltimoComando();

        // Step 6: Verifica che il gruppo sia stato svuotato
        assertTrue(!controller.mappaFormeGruppi.containsValue(gruppoCreato), "Il gruppo deve essere svuotato dopo undo");

        // Verifica che le forme non siano più associate a un gruppo
        assertFalse(controller.mappaFormeGruppi.containsKey(forma1));
        assertFalse(controller.mappaFormeGruppi.containsKey(forma2));
    }

        /**
    * Verifica il corretto comportamento del comando {@link ComandoCreazioneGruppo}.
    * Il test esegue i seguenti passaggi:
    *     Crea due forme regolari e le aggiunge alla lavagna
    *     Crea un poligono e lo aggiunge alla lavagna
    *     Crea un testo e lo aggiunge alla lavagna
    *     Raggruppa le due forme, il poligono e il testo in un nuovo gruppo
    *     Verifica che il gruppo contenga le forme previste
    *     Applica l'operazione di undo
    *     Verifica che il gruppo sia stato svuotato e che le forme non siano più associate a un gruppo
    */
    @Test
    public void testComandoCreazioneGruppoFormeRegolariPoligonoTesto() {
        
        // Step 1: Creazione delle forme e aggiunta alla lavagna
        ComandoCreazioneFormaRegolare comando1 = new ComandoCreazioneFormaRegolare(modello, controller.lavagna,
                TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        controller.eseguiComando(comando1);
        FormaPersonalizzabile forma1 = comando1.getFormaCreata();

        ComandoCreazioneFormaRegolare comando2 = new ComandoCreazioneFormaRegolare(modello, controller.lavagna,
                TipoFormaRegolare.RETTANGOLO, 10, 10, Color.BLUE, Color.RED);
        controller.eseguiComando(comando2);
        FormaPersonalizzabile forma2 = comando2.getFormaCreata();
        
        // Step 2: Creazione di un poligono e aggiunta alla lavagna
        String stringaTesto = new String("Comando testo");
        ComandoCreazioneTesto comando3 = new ComandoCreazioneTesto(modello, controller.lavagna, stringaTesto, 0, 0, Color.RED, Color.BLUE, 20.0);
        controller.eseguiComando(comando3);
        FormaPersonalizzabile testo = comando3.getTesto();
        
        // Step 3: Creazione di un testo e aggiunta alla lavagna
        List <Double> puntiPoligono = new ArrayList<>();
        puntiPoligono.add(10.0);
        puntiPoligono.add(10.0);
        puntiPoligono.add(20.0);
        puntiPoligono.add(10.0);
        puntiPoligono.add(20.0);
        puntiPoligono.add(20.0);
        puntiPoligono.add(10.0);
        puntiPoligono.add(20.0);
        ComandoCreazionePoligono comando4 = new ComandoCreazionePoligono(modello, controller.lavagna, puntiPoligono, Color.RED, Color.BLUE);
        controller.eseguiComando(comando4);
        FormaPersonalizzabile poligono = comando4.getFormaCreata();
        
        // Step 4: Raccolta delle forme da raggruppare
        List<FormaPersonalizzabile> formeDaRaggruppare = List.of(forma1, forma2, testo, poligono);

        // Step 5: Esecuzione del comando di creazione gruppo
        ComandoCreazioneGruppo comandoGruppo = new ComandoCreazioneGruppo(modello, controller.lavagna,
                formeDaRaggruppare, controller.mappaFormeGruppi);
        controller.eseguiComando(comandoGruppo);
        Gruppo gruppoCreato = comandoGruppo.getGruppoCreato();

        // Step 6: Verifiche
        assertNotNull(gruppoCreato, "Il gruppo non dovrebbe essere null");
        assertEquals(4, gruppoCreato.getVettoreForme().size(), "Il gruppo deve contenere esattamente quattro forme, ovvero due forme regolari, un testo e un poligono");
        assertTrue(gruppoCreato.getVettoreForme().contains(forma1));
        assertTrue(gruppoCreato.getVettoreForme().contains(forma2));
        assertTrue(gruppoCreato.getVettoreForme().contains(testo));
        assertTrue(gruppoCreato.getVettoreForme().contains(poligono));

        // Verifica (opzionale) che la lavagna contenga ancora le forme
        assertTrue(controller.lavagna.getChildren().contains(forma1));
        assertTrue(controller.lavagna.getChildren().contains(forma2));
        assertTrue(controller.lavagna.getChildren().contains(testo));
        assertTrue(controller.lavagna.getChildren().contains(poligono));
        
        // Step 7: Undo
        controller.annullaUltimoComando();

        // Step 8: Verifica che il gruppo sia stato svuotato
        System.out.println(gruppoCreato.getVettoreForme().toString());
        assertTrue(!controller.mappaFormeGruppi.containsValue(gruppoCreato), "Il gruppo deve essere svuotato dopo undo");

        // Verifica che le forme non siano più associate a un gruppo
        assertFalse(controller.mappaFormeGruppi.containsKey(forma1));
        assertFalse(controller.mappaFormeGruppi.containsKey(forma2));
        assertFalse(controller.mappaFormeGruppi.containsKey(testo));
        assertFalse(controller.mappaFormeGruppi.containsKey(poligono));
    }
    
    /**
    * Verifica il corretto comportamento del comando {@link ComandoSeparazioneGruppo}.
    * Il test esegue i seguenti passaggi:
    *     Crea due forme e le raggruppa
    *     Associa le forme al gruppo nella mappa
    *     Esegue la separazione rimuovendo le associazioni
    *     Verifica che le forme non siano più associate a un gruppo
    *     Esegue l'operazione di undo della separazione
    *     Verifica che le forme siano nuovamente associate a un nuovo gruppo
    */
    @Test
    public void testComandoSeparazioneGruppo() {
        // Crea due forme e aggiungile alla lavagna
        ComandoCreazioneFormaRegolare comando1 = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.GREEN, Color.BLACK);
        controller.eseguiComando(comando1);
        FormaPersonalizzabile forma1 = comando1.getFormaCreata();

        ComandoCreazioneFormaRegolare comando2 = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 20, 20, Color.YELLOW, Color.BLUE);
        controller.eseguiComando(comando2);
        FormaPersonalizzabile forma2 = comando2.getFormaCreata();

        // Raggruppa le forme
        List<FormaPersonalizzabile> formeDaRaggruppare = List.of(forma1, forma2);
        ComandoCreazioneGruppo comandoGruppo = new ComandoCreazioneGruppo(modello, controller.lavagna, formeDaRaggruppare, controller.mappaFormeGruppi);
        controller.eseguiComando(comandoGruppo);

        Gruppo gruppoCreato = comandoGruppo.getGruppoCreato();
        // Associa le forme alla mappa
        for (FormaPersonalizzabile f : formeDaRaggruppare) {
            controller.mappaFormeGruppi.put(f, gruppoCreato);
        }

        // Verifica che le associazioni siano presenti
        assertEquals(gruppoCreato, controller.mappaFormeGruppi.get(forma1));
        assertEquals(gruppoCreato, controller.mappaFormeGruppi.get(forma2));
        
        // Esegui il comando di separazione
        ComandoSeparazioneGruppo comandoSeparazione = new ComandoSeparazioneGruppo(modello, controller.lavagna, gruppoCreato, controller.mappaFormeGruppi);
        controller.eseguiComando(comandoSeparazione);

        // Verifica che le forme siano state rimosse dalla mappa
        assertFalse(controller.mappaFormeGruppi.containsKey(forma1));
        assertFalse(controller.mappaFormeGruppi.containsKey(forma2));

        // Undo del comando di separazione
        controller.annullaUltimoComando();

        // Verifica che le associazioni siano ripristinate con un nuovo gruppo
        assertTrue(controller.mappaFormeGruppi.containsKey(forma1));
        assertTrue(controller.mappaFormeGruppi.containsKey(forma2));

        Gruppo gruppoRicreato = controller.mappaFormeGruppi.get(forma1);
        assertSame(gruppoRicreato, controller.mappaFormeGruppi.get(forma2), "Le due forme devono appartenere allo stesso gruppo ricreato");

        assertEquals(2, gruppoRicreato.getVettoreForme().size(), "Il gruppo ricreato deve contenere entrambe le forme");
    }

    
   
    /**
     * Test per verificare il comando di eliminazione di una forma.
     * Verifica la rimozione corretta della forma dalla lavagna e il ripristino
     * dopo l'operazione di annullamento.
     */
    @Test
    public void testComandoEliminazione(){
        ObservableList <Node> figliLavagnaIniziale = controller.lavagna.getChildren();
        ComandoCreazioneFormaRegolare comandoCreazione = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        controller.eseguiComando(comandoCreazione);
        ObservableList <Node> figliLavagnaFormaCreata = controller.lavagna.getChildren();
        FormaPersonalizzabile forma = comandoCreazione.getFormaCreata();
        ComandoEliminazione comando = new ComandoEliminazione(modello, controller.lavagna, forma);
        controller.eseguiComando(comando);
        assertEquals(figliLavagnaIniziale, controller.lavagna.getChildren());   //verifica che il comando di eliminazione è andato a buon fine
        controller.annullaUltimoComando();
        assertEquals(figliLavagnaFormaCreata,controller.lavagna.getChildren());     //verifica che l'annullamento del comando è andato a buon fine   
    }

    
    /**
     * Test per verificare il comando di modifica angolo di una forma.
     * Verifica che la rotazione venga applicata correttamente e che l'operazione di annullamento
     * ripristini l'angolo originale.
     */
    @Test
    public void testComandoCambiaAngolo(){
        ComandoCreazioneFormaRegolare comandoCreazione = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        controller.eseguiComando(comandoCreazione);
        double angoloVecchio = comandoCreazione.getFormaCreata().getAngolo();
        ComandoCambiaAngolo comando = new ComandoCambiaAngolo(modello, comandoCreazione.getFormaCreata(), 30);
        controller.eseguiComando(comando);
        assertEquals(comando.getAngolo(), 30);  //verifica che l'esecuzione del comando è andata a buon fine
        controller.annullaUltimoComando();
        assertEquals(angoloVecchio,comandoCreazione.getFormaCreata().getAngolo());   //verifica che l'annullamento del comando è andata a buon fine   
    }

    /**
     * Test per verificare il comportamento del comando di modifica angolo con valori limite.
     * Controlla il corretto handling di angoli negativi e valori superiori a 360 gradi,
     * verificando anche il ripristino dello stato dopo l'annullamento.
     */
    @Test
    public void testComandoCambiaAngoloAngoloNegativoEMaggiore360() {
        ComandoCreazioneFormaRegolare comandoCreazione = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        controller.eseguiComando(comandoCreazione);
        FormaPersonalizzabile forma = comandoCreazione.getFormaCreata();
        
        ComandoCambiaAngolo comandoNegativo = new ComandoCambiaAngolo(modello, forma, -30);
        controller.eseguiComando(comandoNegativo);
        assertEquals(-30, forma.getAngolo(), 0.01);
        controller.annullaUltimoComando();
        assertEquals(0, forma.getAngolo(), 0.01);
        
        ComandoCambiaAngolo comandoOversize = new ComandoCambiaAngolo(modello, forma, 450);
        controller.eseguiComando(comandoOversize);
        assertEquals(450, forma.getAngolo(), 0.01);
        controller.annullaUltimoComando();
        assertEquals(0, forma.getAngolo(), 0.01);
    }
    
    /**
     * Test per verificare il comportamento del comando di eliminazione quando viene
     * tentata la rimozione di una forma non presente nella lavagna.
     * Verifica che venga lanciata un'eccezione appropriata.
     */
    @Test
    public void testComandoEliminazioneFormaNonEsistente() {
        FormaPersonalizzabile formaFittizia = new Rettangolo(150,150, Color.ALICEBLUE, Color.YELLOWGREEN);

        ComandoEliminazione comando = new ComandoEliminazione(modello, controller.lavagna, formaFittizia);

        assertThrows(IllegalArgumentException.class, () -> {
            controller.eseguiComando(comando);
        }, "Dovrebbe lanciare un'eccezione per forma non presente");
    }
    
    /**
     * Test per verificare il comando di incolla forma.
     * Controlla la corretta creazione del clone, il posizionamento nella lavagna
     * e il ripristino dello stato dopo l'annullamento.
     */
    @Test
    public void testComandoIncolla(){
        ComandoCreazioneFormaRegolare comandoCreazione = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        controller.eseguiComando(comandoCreazione);
        List<Node> figliLavagnaPrima = new ArrayList<>(controller.lavagna.getChildren());

        FormaPersonalizzabile forma = (comandoCreazione.getFormaCreata()).clonaForma();
        ComandoIncolla comando = new ComandoIncolla(modello, controller.lavagna, forma, 1, 1);
        controller.eseguiComando(comando);

        ObservableList<Node> figliLavagnaDopo = controller.lavagna.getChildren();
        assertEquals(figliLavagnaPrima.size() + 1, figliLavagnaDopo.size());  //verifica che dopo l'esecuzione del comando la lavagna contiene un figlio in più
        assertTrue(figliLavagnaDopo.contains(forma)); //verifica che dopo l'esecuzione del comando la lavagna contiene la forma clonata

        controller.annullaUltimoComando();
        assertEquals(figliLavagnaPrima.size(), controller.lavagna.getChildren().size()); //verifica che l'annullamento del comando sia andato a buon fine 
        assertFalse(controller.lavagna.getChildren().contains(forma)); //verifica che dopo l'annullamento del comando la lavagna non contiene più la forma clonata   
    }

    /**
     * Test per verificare l'indipendenza tra forma originale e clone dopo l'operazione di incolla.
     * Verifica che le modifiche apportate al clone non influenzino la forma originale.
     */
    @Test
    public void testComandoIncollaModificaCloneNonInfluenzaOriginale() {
        ComandoCreazioneFormaRegolare comandoCreazione = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        controller.eseguiComando(comandoCreazione);
        FormaPersonalizzabile originale = comandoCreazione.getFormaCreata();
        FormaPersonalizzabile clone = originale.clonaForma();
        
        ComandoIncolla comandoIncolla = new ComandoIncolla(modello, controller.lavagna, clone, 10, 10);
        controller.eseguiComando(comandoIncolla);
        
        // Modifica il clone e verifica che l'originale non cambia
        clone.setColoreRiempimento(Color.GREEN);
        assertNotEquals(originale.getColoreRiempimento(), clone.getColoreRiempimento());
    }

    /**
     * Test per verificare il comando di modifica colore bordo.
     * Controlla l'aggiornamento corretto del colore e il ripristino dello stato originale
     * dopo l'operazione di annullamento.
     */
    @Test
    public void testComandoModificaColoreBordo(){
        ComandoCreazioneFormaRegolare comandoCreazione = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.WHITE);
        controller.eseguiComando(comandoCreazione);
        FormaPersonalizzabile forma = comandoCreazione.getFormaCreata();
        Color coloreBordoVecchio=forma.getColoreBordo();
        ComandoModificaColoreBordoSingolo comando = new ComandoModificaColoreBordoSingolo(modello, forma, Color.BLUE);
        controller.eseguiComando(comando);
        assertEquals(comando.getColoreBordo(), Color.BLUE); //verifica che l'esecuzione del comando sia andato a buon fine confrontando il nuovo colore di bordo
        controller.annullaUltimoComando();
        assertEquals(coloreBordoVecchio,comando.getColoreBordoVecchio());   //verifica che l'annullamento del comando è andato a buon fine confrontando il colore di bordo vecchio     
    }

    /**
     * Test per verificare il comando di modifica colore bordo con valore nullo.
     * Verifica il corretto handling della rimozione del colore del bordo e il ripristino
     * dopo l'annullamento.
     */
    @Test
    public void testComandoModificaColoreBordoANull() {
        ComandoCreazioneFormaRegolare comandoCreazione = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        controller.eseguiComando(comandoCreazione);
        FormaPersonalizzabile forma = comandoCreazione.getFormaCreata();
        
        ComandoModificaColoreBordoSingolo comando = new ComandoModificaColoreBordoSingolo(modello, forma, null);
        controller.eseguiComando(comando);
        
        assertNull(forma.getColoreBordo());
        controller.annullaUltimoComando();
        assertEquals(Color.BLUE, forma.getColoreBordo());
    }
    
    /**
     * Test per verificare il comando di modifica colore riempimento.
     * Controlla l'aggiornamento corretto del colore e il ripristino dello stato originale
     * dopo l'operazione di annullamento.
     */
    @Test
    public void testComandoModificaColoreRiempimento(){
        ComandoCreazioneFormaRegolare comandoCreazione = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.WHITE);
        controller.eseguiComando(comandoCreazione);
        FormaPersonalizzabile forma = comandoCreazione.getFormaCreata();
        Color coloreRiempimentoVecchio=(Color)forma.getColoreRiempimento();
        ComandoModificaColoreRiempimentoSingolo comando = new ComandoModificaColoreRiempimentoSingolo(modello, forma, Color.BLUE);
        controller.eseguiComando(comando);
        assertEquals(comando.getColoreRiempimento(), Color.BLUE); //verifica che l'esecuzione del comando sia andato a buon fine confrontando il nuovo colore di bordo
        controller.annullaUltimoComando();
        assertEquals(coloreRiempimentoVecchio,comando.getColoreRiempimentoVecchio());  //verifica che l'annullamento del comando è andato a buon fine confrontando il colore di riempimento vecchio      
    }
    
    /**
     * Test per verificare il comando di modifica dimensione per forme monodimensionali.
     * Verifica l'aggiornamento corretto della dimensione e il ripristino dopo annullamento.
     */
    @Test
    public void testComandoModificaDimensione(){
        ComandoCreazioneFormaRegolare comandoCreazione = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.LINEA, 0, 0, Color.RED, null);
        controller.eseguiComando(comandoCreazione);
        FormaMonodimensionale forma = (FormaMonodimensionale)comandoCreazione.getFormaCreata();
        ComandoModificaDimensione comando = new ComandoModificaDimensione(modello, forma, 5);
        double dimensioneVecchia=forma.ottieniValoreDimensione();
        controller.eseguiComando(comando);
        assertEquals(comando.getDimensione(), 5); //verifica che l'esecuzione del comando è andata a buon fine
        controller.annullaUltimoComando();
        assertEquals(dimensioneVecchia,comando.getDimensioneVecchia());    //verifica che l'annullamento del comando è andata a buon fine    
    }

    /**
     * Test per verificare il comportamento del comando di modifica dimensione con valori negativi.
     * Verifica che venga lanciata un'eccezione quando si tenta di impostare una dimensione negativa.
     */
    @Test
    public void testComandoModificaDimensioneNegativa() {
        ComandoCreazioneFormaRegolare comandoCreazione = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.LINEA, 0, 0, Color.RED, null);
        controller.eseguiComando(comandoCreazione);
        FormaMonodimensionale forma = (FormaMonodimensionale) comandoCreazione.getFormaCreata();
        
        assertThrows(IllegalArgumentException.class, () -> {
            ComandoModificaDimensione comando = new ComandoModificaDimensione(modello, forma, -5);
            controller.eseguiComando(comando);
        }, "Dimensione negativa non consentita");
    }

    
    
    /**
     * Test per verificare il comportamento del comando di modifica dimensioni con valori zero.
     * Verifica l'handling corretto di dimensioni nulle e il ripristino delle dimensioni originali.
     */
    @Test
    public void testComandoModificaDimensioniAZero() {
        ComandoCreazioneFormaRegolare comandoCreazione = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        controller.eseguiComando(comandoCreazione);
        FormaBidimensionale forma = (FormaBidimensionale) comandoCreazione.getFormaCreata();
        
        ComandoModificaDimensioni comando = new ComandoModificaDimensioni(modello, forma, 0, 0);
        controller.eseguiComando(comando);
        
        assertEquals(0, forma.ottieniValoriDimensioni()[0]);
        assertEquals(0, forma.ottieniValoriDimensioni()[1]);
        
        controller.annullaUltimoComando();
        assertNotEquals(0, forma.ottieniValoriDimensioni()[0]); // Assumendo dimensioni iniziali > 0
    }
    
    /**
     * Test per verificare il comando di modifica dimensioni per forme bidimensionali.
     * Controlla l'aggiornamento corretto di entrambe le dimensioni e il ripristino completo
     * dopo l'operazione di annullamento.
     */
    @Test
    public void testComandoModificaDimensioni(){
        ComandoCreazioneFormaRegolare comandoCreazione = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        controller.eseguiComando(comandoCreazione);
        FormaBidimensionale forma = (FormaBidimensionale)comandoCreazione.getFormaCreata();
        ComandoModificaDimensioni comando = new ComandoModificaDimensioni(modello, forma, 5, 10);
        double[] dimensioniVecchie= forma.ottieniValoriDimensioni();
        controller.eseguiComando(comando);
        assertEquals(comando.getDimensione1(), 5); //verifica che l'esecuzione del comando è andata a buon fine sulla prima dimensione
        assertEquals(comando.getDimensione2(), 10); //verifica che l'esecuzione del comando è andata a buon fine sulla seconda dimensione
        controller.annullaUltimoComando();
        assertEquals(dimensioniVecchie[0],comando.getDimensioneVecchia1());   //verifica che l'annullamento del comando è andata a buon fine sulla prima dimensione
        assertEquals(dimensioniVecchie[1],comando.getDimensioneVecchia2());   //verifica che l'annullamento del comando è andata a buon fine sulla prima dimensione
    }
    
    /**
     * Test per verificare il comando di spostamento in avanti di una forma.
     * Controlla il corretto cambiamento dell'ordine di visualizzazione e il ripristino
     * della posizione originale dopo annullamento.
     */
    @Test
    public void testComandoSpostaAvanti() {
        ComandoCreazioneFormaRegolare comando1 = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        ComandoCreazioneFormaRegolare comando2 = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 10, 10, Color.GREEN, Color.YELLOW);
        controller.eseguiComando(comando1);
        controller.eseguiComando(comando2);
        FormaPersonalizzabile formaDaSpostare = comando1.getFormaCreata();
        FormaPersonalizzabile secondaForma = comando2.getFormaCreata();
        
        Pane padre = (Pane) ((Shape)formaDaSpostare).getParent();
        ObservableList<Node> figli = padre.getChildren();
        int posizioneInizialeFormaDaSpostare = figli.indexOf(formaDaSpostare);
        int posizioneInizialeSecondaForma = figli.indexOf(secondaForma);
        
        ComandoSpostaAvanti comando = new ComandoSpostaAvanti(modello, padre, formaDaSpostare);
        controller.eseguiComando(comando);
        assertEquals(posizioneInizialeSecondaForma, figli.indexOf(formaDaSpostare)); //verifica che l'esecuzione del comando è andata a buon fine 
                                                                                    //vedendo se la nuova posizione della forma da spostare è uguale all'iniziale della seconda forma
        
        controller.annullaUltimoComando();
        int posizioneFinaleFormaSpostata = figli.indexOf(formaDaSpostare);
        assertEquals(posizioneInizialeFormaDaSpostare, posizioneFinaleFormaSpostata); //verfica che l'annullamento del comando è andato a buon fine confrontando la posizione iniziale della forma da spostare e quella raggiunta dopo l'annullamento
    }

    /**
     * Test per verificare il comportamento del comando di spostamento in avanti
     * quando la forma è già in primo piano.
     * Verifica che non avvengano cambiamenti nell'ordine di visualizzazione.
     */
    @Test
    public void testComandoSpostaAvantiFormaGiaInPrimoPiano() {
        // Crea due forme
        ComandoCreazioneFormaRegolare comando1 = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        ComandoCreazioneFormaRegolare comando2 = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 10, 10, Color.GREEN, Color.YELLOW);
        controller.eseguiComando(comando1);
        controller.eseguiComando(comando2);
        
        FormaPersonalizzabile forma2 = comando2.getFormaCreata();
        Pane padre = (Pane) ((Shape)forma2).getParent();
        int posizioneIniziale = controller.lavagna.getChildren().indexOf(forma2);
        
        ComandoSpostaAvanti comando = new ComandoSpostaAvanti(modello, padre, forma2);
        controller.eseguiComando(comando);
        
        // La forma è già in cima, non dovrebbe muoversi
        assertEquals(posizioneIniziale, controller.lavagna.getChildren().indexOf(forma2));
    }
    
    /**
     * Test per verificare il comando di spostamento all'indietro di una forma.
     * Controlla il corretto cambiamento dell'ordine di visualizzazione e il ripristino
     * della posizione originale dopo annullamento.
     */
    @Test
    public void testComandoSpostaIndietro() {
        ComandoCreazioneFormaRegolare comando1 = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        ComandoCreazioneFormaRegolare comando2 = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 1, 1, Color.GREEN, Color.YELLOW);
        controller.eseguiComando(comando1);
        controller.eseguiComando(comando2);

        FormaPersonalizzabile forma1 = comando1.getFormaCreata();
        FormaPersonalizzabile forma2 = comando2.getFormaCreata();
        Pane padre = (Pane) ((Shape)forma2).getParent();
        ObservableList<Node> figli = padre.getChildren();

        int posizioneIniziale = figli.indexOf(forma2);
        System.out.println("Prima 2:"+posizioneIniziale);

        ComandoSpostaIndietro comando3 = new ComandoSpostaIndietro(modello, padre, forma2);
        controller.eseguiComando(comando3);

        // Verifica che l'esecuzione sia andata a buon fine ovvero che la forma sia stata spostata dietro (quindi ha indice inferiore)
        int nuovaPosizione = figli.indexOf(forma2);
        
        System.out.println("Prima 2:"+posizioneIniziale);
        assertTrue(nuovaPosizione < posizioneIniziale);

        //Verifica che l'annullamento è andato a buon fiene ovvero che la posizione sia tornata quella iniziale
        controller.annullaUltimoComando();
        int posizioneRipristinata = figli.indexOf(forma2);
        assertEquals(posizioneIniziale, posizioneRipristinata);
    }

    /**
     * Test per verificare il comportamento del comando di spostamento all'indietro
     * quando la forma è già in ultima posizione.
     * Verifica che non avvengano cambiamenti nell'ordine di visualizzazione.
     */
    @Test
    public void testComandoSpostaIndietroFormaGiaInUltimaPosizione() {
        controller.lavagna.getChildren().clear(); // Assicurati che la lavagna sia vuota
        
        ComandoCreazioneFormaRegolare comando = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        controller.eseguiComando(comando);
        FormaPersonalizzabile forma1 = comando.getFormaCreata();
        Pane padre = (Pane) ((Shape)forma1).getParent();
        int posizioneIniziale = controller.lavagna.getChildren().indexOf(forma1);
        
        ComandoSpostaIndietro comando1 = new ComandoSpostaIndietro(modello, padre, forma1);
        controller.eseguiComando(comando1);

        // Non dovrebbe cambiare posizione
        assertEquals(posizioneIniziale, controller.lavagna.getChildren().indexOf(forma1));
    }

    /**
     * Test per verificare il comando di trascinamento di una forma.
     * Verifica il corretto aggiornamento delle coordinate durante lo spostamento
     * e il completo ripristino della posizione originale dopo annullamento.
     */
    @Test
    public void testComandoTrascina() {
        ComandoCreazioneFormaRegolare comandoCreazione = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        controller.eseguiComando(comandoCreazione);

        FormaPersonalizzabile forma = comandoCreazione.getFormaCreata();
        double[] coordinate1 = forma.ottieniCoordinate();
        double posizioneXIniziale = coordinate1[0];
        double posizioneYIniziale = coordinate1[1];

        ComandoTrascina comando = new ComandoTrascina(modello, forma);
        comando.aggiorna(15, 10);  // simulazione dello spostamento
        controller.eseguiComando(comando);
        controller.aggiungiComandoNellaPila(comando);
        
        double[] coordinate2 = forma.ottieniCoordinate();
        // Verifica che l'esecuzione sia andata a buon fine e che quindi la posizione sia cambiata
        assertEquals(posizioneXIniziale + 15, coordinate2[0], 0.01);
        assertEquals(posizioneYIniziale + 10, coordinate2[1], 0.01);

       
        // Verifica che l'annullamento del comando sia andato a buon fine ovvero che la posizione deve tornare a quella iniziale
        controller.annullaUltimoComando();
        
        double[] coordinate3 = forma.ottieniCoordinate();
        System.out.println(coordinate3[0]+" "+coordinate3[1]+" PIX: "+posizioneXIniziale+" PIY: "+posizioneYIniziale);
        assertEquals(coordinate1[0], coordinate3[0], 0.01);
        assertEquals(posizioneYIniziale, coordinate3[1], 0.01);
    }

    /**
     * Test per verificare il comportamento del comando di trascinamento con operazioni multiple.
     * Controlla il corretto accumulo degli spostamenti e il ripristino graduale
     * dello stato dopo annullamenti consecutivi.
     */
    @Test
    public void testComandoTrascinaMultiplo() {
        ComandoCreazioneFormaRegolare comandoCreazione = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 0, 0, Color.RED, Color.BLUE);
        controller.eseguiComando(comandoCreazione);
        FormaPersonalizzabile forma = (FormaPersonalizzabile) comandoCreazione.getFormaCreata();
        double[] coordinateIniziali = forma.ottieniCoordinate();
        System.err.println("Coordinate iniziali: " + coordinateIniziali[0] + ", " + coordinateIniziali[1]);
        
        // Primo trascinamento
        ComandoTrascina comando1 = new ComandoTrascina(modello, forma);
        comando1.aggiorna(10, 10);
        controller.eseguiComando(comando1);
        controller.aggiungiComandoNellaPila(comando1);
        
        double[] coordinateInizialiDopoPrimoTrascinamento = forma.ottieniCoordinate();
        System.err.println("Coordinate dopo primo trascinamento: " + coordinateInizialiDopoPrimoTrascinamento[0] + ", " + coordinateInizialiDopoPrimoTrascinamento[1]);

        // Secondo trascinamento
        ComandoTrascina comando2 = new ComandoTrascina(modello, forma);
        comando2.aggiorna(20, 20);
        controller.eseguiComando(comando2);
        controller.aggiungiComandoNellaPila(comando2);


        
        // Verifica posizione finale
        assertEquals(coordinateInizialiDopoPrimoTrascinamento[0] + 20, forma.ottieniCoordinate()[0], 0.01);
        assertEquals(coordinateInizialiDopoPrimoTrascinamento[1] + 20, forma.ottieniCoordinate()[1], 0.01);
        
        // Annulla due volte
        controller.annullaUltimoComando(); // Annulla secondo trascinamento
        assertEquals(coordinateIniziali[0] + 10, forma.ottieniCoordinate()[0], 0.01);
        
        controller.annullaUltimoComando(); // Annulla primo trascinamento
        assertArrayEquals(coordinateIniziali, forma.ottieniCoordinate(), 0.01);
    }


}
