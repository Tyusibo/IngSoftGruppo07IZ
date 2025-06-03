/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testController;

import com.gruppo07iz.geometrika.DashboardController;
import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.command.ComandoCreazioneFormaRegolare;
import com.gruppo07iz.geometrika.command.ComandoCreazionePoligono;
import com.gruppo07iz.geometrika.forme.FormaPersonalizzabile;
import com.gruppo07iz.geometrika.forme.Poligono;
import com.gruppo07iz.geometrika.forme.TipoFormaRegolare;
import javafx.application.Platform;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import static org.junit.jupiter.api.Assertions.*;

public class TestDashboardController {

    private static DashboardController controller;
    private static Model modello;

    @BeforeAll
    public static void setup() throws InterruptedException {
        if (!Platform.isFxApplicationThread()) {
            CountDownLatch latch = new CountDownLatch(1);
            new Thread(() -> {
                try {
                    Platform.startup(() -> {
                        // JavaFX toolkit inizializzato
                        latch.countDown();
                    });
                } catch (IllegalStateException e) {
                    // Toolkit già inizializzato, ignora
                    latch.countDown();
                }
            }).start();
            latch.await(); // aspetta che JavaFX sia inizializzato
        }

        modello = new Model();
        controller = new DashboardController();
        controller.lavagna = new Pane();
        controller.pilaComandi = new Stack<>();
        controller.pannelloModifica = new VBox();
        controller.rettangoloBottone = new ImageView();
        controller.ellisseBottone = new ImageView();
        controller.lineaBottone = new ImageView();
        controller.poligonoBottone = new ImageView();
        controller.mappaFormeGruppi = new HashMap<>();
    }



    /**
     * Verifica che il metodo controllaDimensione effettui correttamente i controlli sulla modifica della dimensioni.
     */
    @Test
    public void testControllaDimensione() {
        assertEquals(1.0, controller.controllaDimensione(0), 0.01);
        assertEquals(500.0, controller.controllaDimensione(500.0), 0.01);
    }

    /**
     * Verifica che il colore di riempimento del rettangolo cambia con quello desiderato.
     */
    @Test
    public void testCambiaColoreRiempimentoRettangolo() {
        TipoFormaRegolare formaDaCreare=TipoFormaRegolare.RETTANGOLO;
        FormaPersonalizzabile forma=modello.creaFormaRegolare(formaDaCreare, 0, 0, Color.BLUE, Color.WHITE);
        controller.formaSelezionata = forma;

        // Inizializza manualmente il ColorPicker
        controller.modificaColoreRiempimento = new ColorPicker();
        controller.modificaColoreRiempimento.setValue(Color.GREEN);

        // Imposta il listener
        controller.modificaColoreRiempimento.setOnAction(e -> {
            forma.setColoreRiempimento(controller.modificaColoreRiempimento.getValue());
        });

        // Simula l'azione
        controller.modificaColoreRiempimento.getOnAction().handle(null);

        assertEquals(Color.GREEN, forma.getColoreRiempimento());
    }
    
    /**
     * Verifica che il colore di bordo del rettangolo cambia con quello desiderato.
     */
    @Test
    public void testCambiaColoreBordoRettangolo() {
        DashboardController controller = new DashboardController();
        Model modello = new Model();
        TipoFormaRegolare formaDaCreare=TipoFormaRegolare.RETTANGOLO;
        FormaPersonalizzabile forma=modello.creaFormaRegolare(formaDaCreare, 0, 0, Color.BLUE, Color.WHITE);
        controller.formaSelezionata = forma;

        // Inizializza il ColorPicker
        controller.modificaColoreBordo = new ColorPicker();
        controller.modificaColoreBordo.setValue(Color.RED);

        // Imposta il listener
        controller.modificaColoreBordo.setOnAction(e -> {
            forma.setColoreBordo(controller.modificaColoreBordo.getValue());
        });

        // Simula l'azione
        controller.modificaColoreBordo.getOnAction().handle(null);

        assertEquals(Color.RED, forma.getColoreBordo());
    }
    
    //Linea
    /**
     * Verifica che il colore di bordo della linea cambia con quello desiderato.
     */
    @Test
    public void testCambiaColoreBordoLinea() {
        DashboardController controller = new DashboardController();
        Model modello = new Model();
        TipoFormaRegolare formaDaCreare=TipoFormaRegolare.LINEA;
        FormaPersonalizzabile forma=modello.creaFormaRegolare(formaDaCreare, 0, 0, Color.BLUE, Color.WHITE);
        controller.formaSelezionata = forma;

        // Inizializza il ColorPicker
        controller.modificaColoreBordo = new ColorPicker();
        controller.modificaColoreBordo.setValue(Color.RED);

        // Imposta il listener
        controller.modificaColoreBordo.setOnAction(e -> {
            forma.setColoreBordo(controller.modificaColoreBordo.getValue());
        });

        // Simula l'azione
        controller.modificaColoreBordo.getOnAction().handle(null);

        assertEquals(Color.RED, forma.getColoreBordo());
    }
    
    //Ellisse
    /**
     * Verifica che il colore di riempimento dell'ellisse cambia con quello desiderato.
     */
    @Test
    public void testCambiaColoreRiempimentoEllisse() {
        DashboardController controller = new DashboardController();
        Model modello = new Model();
        TipoFormaRegolare formaDaCreare=TipoFormaRegolare.ELLISSE;
        FormaPersonalizzabile forma=modello.creaFormaRegolare(formaDaCreare, 0, 0, Color.BLUE, Color.WHITE);
        controller.formaSelezionata = forma;

        // Inizializza manualmente il ColorPicker
        controller.modificaColoreRiempimento = new ColorPicker();
        controller.modificaColoreRiempimento.setValue(Color.YELLOW);

        // Imposta il listener
        controller.modificaColoreRiempimento.setOnAction(e -> {
            forma.setColoreRiempimento(controller.modificaColoreRiempimento.getValue());
        });

        // Simula l'azione
        controller.modificaColoreRiempimento.getOnAction().handle(null);

        assertEquals(Color.YELLOW, forma.getColoreRiempimento());
    }
    
    /**
     * Verifica che il colore di bordo dell'ellisse cambia con quello desiderato.
     */
    @Test
    public void testCambiaColoreBordoEllisse() {
        DashboardController controller = new DashboardController();
        Model modello = new Model();
        TipoFormaRegolare formaDaCreare=TipoFormaRegolare.ELLISSE;
        FormaPersonalizzabile forma=modello.creaFormaRegolare(formaDaCreare, 0, 0, Color.BLUE, Color.WHITE);
        controller.formaSelezionata = forma;

        // Inizializza il ColorPicker
        controller.modificaColoreBordo = new ColorPicker();
        controller.modificaColoreBordo.setValue(Color.RED);

        // Imposta il listener
        controller.modificaColoreBordo.setOnAction(e -> {
            forma.setColoreBordo(controller.modificaColoreBordo.getValue());
        });

        // Simula l'azione
        controller.modificaColoreBordo.getOnAction().handle(null);

        assertEquals(Color.RED, forma.getColoreBordo());
    }

    /**
    * Test del salvataggio e caricamento di un foglio contenente una forma.
    * 
    * Il test verifica che una forma creata sulla lavagna venga salvata correttamente su file
    * e poi caricata mantenendo tutte le proprietà grafiche coerenti.
    * 
    * 
    * @throws IOException se si verifica un errore durante la creazione o l'accesso al file temporaneo
    */
    @Test
    public void testSalvaECaricaFoglio() throws IOException {
        ComandoCreazioneFormaRegolare comandoCreazioneForma = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 100, 200, Color.BLACK, Color.YELLOW);
        controller.eseguiComando(comandoCreazioneForma);
        
        Shape formaOriginale = (Shape) controller.lavagna.getChildren().get(0);
        
        File fileTemp = File.createTempFile("foglio_test", ".txt");
        fileTemp.deleteOnExit();
        modello.salvaFoglio(fileTemp, controller.lavagna, controller.mappaFormeGruppi);
        controller.lavagna.getChildren().clear();
        modello.caricaFoglio(fileTemp, controller.lavagna,controller.mappaFormeGruppi);
        
        Shape formaCaricata = (Shape) controller.lavagna.getChildren().get(0);
        
        //confronto proprietà forme
        assertEquals(formaOriginale.getFill(), formaCaricata.getFill());
        assertEquals(formaOriginale.getStroke(), formaCaricata.getStroke());
        assertEquals(formaOriginale.getRotate(), formaCaricata.getRotate(), 0.01);
        assertEquals(formaOriginale.getScaleX(), formaCaricata.getScaleX(), 0.01);
        assertEquals(formaOriginale.getScaleY(), formaCaricata.getScaleY(), 0.01);
        assertEquals(formaOriginale.getLayoutX(), formaCaricata.getLayoutX(), 0.01);
        assertEquals(formaOriginale.getLayoutY(), formaCaricata.getLayoutY(), 0.01);
    }
    
    /**
    * Test del salvataggio e caricamento di un foglio contenente sia una forma regolare che un poligono personalizzato.
    *
    * Questo test verifica che entrambe le entità grafiche (una forma regolare e un {@link javafx.scene.shape.Polygon})
    * vengano salvate correttamente su file e successivamente ricaricate mantenendo le stesse proprietà grafiche,
    * come colori di riempimento e bordo, rotazione, scala, posizione e punti del poligono.
    * 
    * In particolare, vengono confrontate:
    * 
    *  Le proprietà della forma regolare: fill, stroke, rotazione, scala, layout X/Y
    *  Le proprietà del poligono: fill, stroke, rotazione, scala, e punti
    *
    *
    * @throws IOException se si verifica un errore durante la creazione o gestione del file temporaneo
    */
    @Test
    public void testSalvaECaricaFoglioConPoligono() throws IOException {
        ComandoCreazioneFormaRegolare comandoCreazioneForma = new ComandoCreazioneFormaRegolare(modello, controller.lavagna, TipoFormaRegolare.RETTANGOLO, 100, 200, Color.BLACK, Color.YELLOW);
        comandoCreazioneForma.execute();
        
        List<Double> listaPunti= new ArrayList<>();
        listaPunti.add(1.1);
        listaPunti.add(1.2);
        listaPunti.add(2.2);
        listaPunti.add(1.2);
        ComandoCreazionePoligono comandoCreazionePoligono = new ComandoCreazionePoligono(modello, controller.lavagna, listaPunti ,Color.RED, Color.BLUE);
        controller.eseguiComando(comandoCreazionePoligono);
        
        Shape formaOriginale = null;
        Poligono poligonoOriginale= null;
        
        for (Node node : controller.lavagna.getChildren()) {
            if (node instanceof Poligono) {
                poligonoOriginale = (Poligono) node;
            } else if (node instanceof Shape) {
                formaOriginale = (Shape) node;
            }
        }
        
        File fileTemp = File.createTempFile("foglio_test", ".txt");
        fileTemp.deleteOnExit();
        modello.salvaFoglio(fileTemp, controller.lavagna, controller.mappaFormeGruppi);
        controller.lavagna.getChildren().clear();
        modello.caricaFoglio(fileTemp, controller.lavagna, controller.mappaFormeGruppi);
        
        Shape formaCaricata = null;
        Poligono poligonoCaricato = null;
        
        for (Node node : controller.lavagna.getChildren()) {
            if (node instanceof Poligono) {
                poligonoCaricato = (Poligono) node;
            } else if (node instanceof Shape) {
                formaCaricata = (Shape) node;
            }
        }

        
        //confronto proprietà forme
        assertEquals(formaOriginale.getFill(), formaCaricata.getFill());
        assertEquals(formaOriginale.getStroke(), formaCaricata.getStroke());
        assertEquals(formaOriginale.getRotate(), formaCaricata.getRotate(), 0.01);
        assertEquals(formaOriginale.getScaleX(), formaCaricata.getScaleX(), 0.01);
        assertEquals(formaOriginale.getScaleY(), formaCaricata.getScaleY(), 0.01);
        assertEquals(formaOriginale.getLayoutX(), formaCaricata.getLayoutX(), 0.01);
        assertEquals(formaOriginale.getLayoutY(), formaCaricata.getLayoutY(), 0.01);

        //confronto proprietà poligono
        assertEquals(poligonoOriginale.getFill(), poligonoCaricato.getFill());
        assertEquals(poligonoOriginale.getStroke(), poligonoCaricato.getStroke());
        assertEquals(poligonoOriginale.getRotate(), poligonoCaricato.getRotate(), 0.01);
        assertEquals(poligonoOriginale.getScaleX(), poligonoCaricato.getScaleX(), 0.01);
        assertEquals(poligonoOriginale.getScaleY(), poligonoCaricato.getScaleY(), 0.01);
        assertEquals(poligonoOriginale.getPoints().size(), poligonoCaricato.getPoints().size());
        for (int i = 0; i < poligonoOriginale.getPoints().size(); i++) {
            assertEquals(poligonoOriginale.getPoints().get(i), poligonoCaricato.getPoints().get(i), 0.01);
        }
    }
    
    /**
    * Test che verifica il corretto passaggio allo stato di creazione di una forma regolare (rettangolo)
    * quando viene cliccato il bottone corrispondente.
    * Simula un evento di click sul bottone {@code rettangoloBottone} e verifica che,
    * in risposta, il controller imposti lo stato corrente su {@code statoCreazioneFormaRegolare}.
    * Questo test assicura che il collegamento tra l'interfaccia utente e la logica dello stato sia funzionante.
    */
    @Test
    public void testStateCreazioneFormaRegolareRettangolo(){
        controller.rettangoloBottone.setOnMouseClicked(e -> {
            assertEquals(controller.statoAttuale, controller.statoCreazioneFormaRegolare);
        });
        MouseEvent clickEvent = new MouseEvent(
            MouseEvent.MOUSE_CLICKED,
            0, 0, 0, 0,
            MouseButton.PRIMARY,
            1,
            false, false, false, false,
            true, false, false, true,
            false, false, null
        );

        controller.rettangoloBottone.fireEvent(clickEvent);
    }
    
    /**
    * Test che verifica il corretto passaggio allo stato di creazione di una forma regolare (ellisse)
    * quando viene cliccato il bottone corrispondente.
    * Simula un evento di click sul bottone {@code ellisseBottone} e verifica che,
    * in risposta, il controller imposti lo stato corrente su {@code statoCreazioneFormaRegolare}.
    * Questo test garantisce che l'interazione con l'interfaccia utente aggiorni correttamente lo stato del controller.
    */
    @Test
    public void testStateCreazioneFormaRegolareEllisse(){
        controller.ellisseBottone.setOnMouseClicked(e -> {
            assertEquals(controller.statoAttuale, controller.statoCreazioneFormaRegolare);
        });
        MouseEvent clickEvent = new MouseEvent(
            MouseEvent.MOUSE_CLICKED,
            0, 0, 0, 0,
            MouseButton.PRIMARY,
            1,
            false, false, false, false,
            true, false, false, true,
            false, false, null
        );

        controller.ellisseBottone.fireEvent(clickEvent);
    }
    
    /**
    * Test che verifica il corretto passaggio allo stato di creazione di una forma regolare (linea)
    * quando viene cliccato il bottone corrispondente.
    * Simula un evento di click sul bottone {@code lineaBottone} e verifica che,
    * in risposta, il controller imposti lo stato corrente su {@code statoCreazioneFormaRegolare}.
    * Questo assicura che l'interfaccia utente risponda correttamente al comando dell'utente.
    */
    @Test
    public void testStateCreazioneFormaRegolareLinea(){
        controller.lineaBottone.setOnMouseClicked(e -> {
            assertEquals(controller.statoAttuale, controller.statoCreazioneFormaRegolare);
        });
        MouseEvent clickEvent = new MouseEvent(
            MouseEvent.MOUSE_CLICKED,
            0, 0, 0, 0,
            MouseButton.PRIMARY,
            1,
            false, false, false, false,
            true, false, false, true,
            false, false, null
        );

        controller.lineaBottone.fireEvent(clickEvent);
    }
    /**
    * Test che verifica il corretto passaggio allo stato di creazione del poligono
    * quando viene cliccato il bottone corrispondente.
    * Simula un evento di click sul bottone {@code poligonoBottone} e controlla che
    * il controller imposti lo stato attuale su {@code statoCreazionePoligono}.
    * Questo test assicura la coerenza tra l'interfaccia utente e la logica del controller.
    */
    @Test
    public void testStateCreazionePoligono(){
        controller.poligonoBottone.setOnMouseClicked(e -> {
            assertEquals(controller.statoAttuale, controller.statoCreazionePoligono);
        });
        MouseEvent clickEvent = new MouseEvent(
            MouseEvent.MOUSE_CLICKED,
            0, 0, 0, 0,
            MouseButton.PRIMARY,
            1,
            false, false, false, false,
            true, false, false, true,
            false, false, null
        );

        controller.poligonoBottone.fireEvent(clickEvent);
    }


}
