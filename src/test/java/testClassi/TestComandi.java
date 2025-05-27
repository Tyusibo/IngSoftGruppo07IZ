package testClassi;
import com.gruppo07iz.geometrika.DashboardController;
import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.command.*;
import com.gruppo07iz.geometrika.forme.FormaBidimensionale;
import com.gruppo07iz.geometrika.forme.FormaMonodimensionale;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import com.gruppo07iz.geometrika.forme.TipoFormaRegolare;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author giorg
 */
public class TestComandi {
    private static DashboardController controller;
    private static Model modello;

    @BeforeAll
    public static void setup() {
        // Necessario per inizializzare JavaFX
        Platform.startup(() -> {});
        modello = new Model();
        controller = new DashboardController();
        controller.lavagna = new Pane();
        controller.pilaComandi= new Stack<CommandInterface>();
        controller.pannelloModifica=new VBox();

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
        assertEquals(Color.RED, comando.getFormaCreata().getFill());
        assertEquals(Color.BLUE, comando.getFormaCreata().getStroke());
        assert(controller.lavagna.getChildren().contains(comando.getFormaCreata()));
        
        controller.annullaUltimoComando();
        assertEquals(figliLavagna,controller.lavagna.getChildren());  //verifica che l'annullamento del comando è andato a buon fine
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
        Shape forma = comandoCreazione.getFormaCreata();
        ComandoEliminazione comando = new ComandoEliminazione(modello, controller.lavagna, forma);
        controller.eseguiComando(comando);
        assertEquals(figliLavagnaIniziale, controller.lavagna.getChildren());   //verifica che il comando di eliminazione è andato a buon fine
        controller.annullaUltimoComando();
        assertEquals(figliLavagnaFormaCreata,controller.lavagna.getChildren());     //verifica che l'annullamento del comando è andato a buon fine   
    }

    

    /**
     * Test per verificare il comportamento del comando di eliminazione quando viene
     * tentata la rimozione di una forma non presente nella lavagna.
     * Verifica che venga lanciata un'eccezione appropriata.
     */
    @Test
    public void testComandoEliminazioneFormaNonEsistente() {
        Shape formaFittizia = new Rectangle(0, 0, 10, 10);
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

        Shape forma = ((FormaPersonalizzabile) comandoCreazione.getFormaCreata()).clonaForma();
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
        Shape originale = comandoCreazione.getFormaCreata();
        Shape clone = ((FormaPersonalizzabile) originale).clonaForma();
        
        ComandoIncolla comandoIncolla = new ComandoIncolla(modello, controller.lavagna, clone, 10, 10);
        controller.eseguiComando(comandoIncolla);
        
        // Modifica il clone e verifica che l'originale non cambia
        clone.setFill(Color.GREEN);
        assertNotEquals(originale.getFill(), clone.getFill());
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
        Shape forma = comandoCreazione.getFormaCreata();
        Color coloreBordoVecchio=(Color)forma.getStroke();
        ComandoModificaColoreBordo comando = new ComandoModificaColoreBordo(modello, forma, Color.BLUE);
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
        Shape forma = comandoCreazione.getFormaCreata();
        
        ComandoModificaColoreBordo comando = new ComandoModificaColoreBordo(modello, forma, null);
        controller.eseguiComando(comando);
        
        assertNull(forma.getStroke());
        controller.annullaUltimoComando();
        assertEquals(Color.BLUE, forma.getStroke());
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
        Shape forma = comandoCreazione.getFormaCreata();
        Color coloreRiempimentoVecchio=(Color)forma.getFill();
        ComandoModificaColoreRiempimento comando = new ComandoModificaColoreRiempimento(modello, forma, Color.BLUE);
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
        Shape formaDaSpostare = comando1.getFormaCreata();
        Shape secondaForma = comando2.getFormaCreata();
        
        Pane padre = (Pane) formaDaSpostare.getParent();
        ObservableList<Node> figli = padre.getChildren();
        int posizioneInizialeFormaDaSpostare = figli.indexOf(formaDaSpostare);
        int posizioneInizialeSecondaForma = figli.indexOf(secondaForma);
        
        ComandoSpostaAvanti comando = new ComandoSpostaAvanti(modello, formaDaSpostare);
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
        
        Shape forma2 = comando2.getFormaCreata();
        int posizioneIniziale = controller.lavagna.getChildren().indexOf(forma2);
        
        ComandoSpostaAvanti comando = new ComandoSpostaAvanti(modello, forma2);
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
        ComandoCreazioneFormaRegolare comando2 = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 10, 10, Color.GREEN, Color.YELLOW);
        controller.eseguiComando(comando1);
        controller.eseguiComando(comando2);

        Shape forma1 = comando1.getFormaCreata();
        Shape forma2 = comando2.getFormaCreata();
        Pane padre = (Pane) forma1.getParent();
        ObservableList<Node> figli = padre.getChildren();

        int posizioneIniziale = figli.indexOf(forma1);

        ComandoSpostaIndietro comando = new ComandoSpostaIndietro(modello, forma1);
        controller.eseguiComando(comando);

        // Verifica che l'esecuzione sia andata a buon fine ovvero che la forma sia stata spostata dietro (quindi ha indice inferiore)
        int nuovaPosizione = figli.indexOf(forma1);
        assertTrue(nuovaPosizione < posizioneIniziale);

        //Verifica che l'annullamento è andato a buon fiene ovvero che la posizione sia tornata quella iniziale
        controller.annullaUltimoComando();
        int posizioneRipristinata = figli.indexOf(forma1);
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
        Shape forma1 = comando.getFormaCreata();
        
        int posizioneIniziale = controller.lavagna.getChildren().indexOf(forma1);
        
        ComandoSpostaIndietro comando1 = new ComandoSpostaIndietro(modello, forma1);
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

        Shape forma = comandoCreazione.getFormaCreata();
        double[] coordinate1 = ((FormaPersonalizzabile) forma).ottieniCoordinate();
        double posizioneXIniziale = coordinate1[0];
        double posizioneYIniziale = coordinate1[1];

        FormaPersonalizzabile formaPersonalizzabile = (FormaPersonalizzabile) forma;
        ComandoTrascina comando = new ComandoTrascina(modello, formaPersonalizzabile);
        comando.aggiorna(15, 10);  // simulazione dello spostamento
        controller.eseguiComando(comando);
        controller.aggiungiComandoNellaPila(comando);
        
        double[] coordinate2 = ((FormaPersonalizzabile) forma).ottieniCoordinate();
        // Verifica che l'esecuzione sia andata a buon fine e che quindi la posizione sia cambiata
        assertEquals(posizioneXIniziale + 15, coordinate2[0], 0.01);
        assertEquals(posizioneYIniziale + 10, coordinate2[1], 0.01);

       
        // Verifica che l'annullamento del comando sia andato a buon fine ovvero che la posizione deve tornare a quella iniziale
        controller.annullaUltimoComando();
        
        double[] coordinate3 = ((FormaPersonalizzabile) forma).ottieniCoordinate();
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
