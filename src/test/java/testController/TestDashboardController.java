package testController;

import com.gruppo07iz.geometrika.DashboardController;
import com.gruppo07iz.geometrika.Model;
import com.gruppo07iz.geometrika.classi.TipoForma;
import javafx.application.Platform;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestDashboardController {

    private static DashboardController controller;

    @BeforeAll
    public static void setup() {
        // Necessario per inizializzare JavaFX
        Platform.startup(() -> {});
        controller = new DashboardController();
    }

    /**
     * Verifica che il metodo controllaDimensione effettui correttamente i controlli sulla modifica della dimensioni.
     */
    @Test
    public void testControllaDimensione() {
        assertEquals(1.0, controller.controllaDimensione(0), 0.01);
        assertEquals(500.0, controller.controllaDimensione(500.0), 0.01);
        assertEquals(1000.0, controller.controllaDimensione(2000.0), 0.01);
    }

    /**
     * Verifica che il colore di riempimento del rettangolo cambia con quello desiderato.
     */
    @Test
    public void testCambiaColoreRiempimentoRettangolo() {
        DashboardController controller = new DashboardController();
        Model modello = new Model();
        TipoForma formaDaCreare=TipoForma.RETTANGOLO;
        Shape forma=modello.creaForma(formaDaCreare, 0, 0, Color.BLUE, Color.WHITE);
        controller.formaSelezionata = forma;

        // Inizializza manualmente il ColorPicker
        controller.modificaColoreRiempimento = new ColorPicker();
        controller.modificaColoreRiempimento.setValue(Color.GREEN);

        // Imposta il listener
        controller.modificaColoreRiempimento.setOnAction(e -> {
            forma.setFill(controller.modificaColoreRiempimento.getValue());
        });

        // Simula l'azione
        controller.modificaColoreRiempimento.getOnAction().handle(null);

        assertEquals(Color.GREEN, forma.getFill());
    }
    
    /**
     * Verifica che il colore di bordo del rettangolo cambia con quello desiderato.
     */
    @Test
    public void testCambiaColoreBordoRettangolo() {
        DashboardController controller = new DashboardController();
        Model modello = new Model();
        TipoForma formaDaCreare=TipoForma.RETTANGOLO;
        Shape forma=modello.creaForma(formaDaCreare, 0, 0, Color.BLUE, Color.WHITE);
        controller.formaSelezionata = forma;

        // Inizializza il ColorPicker
        controller.modificaColoreBordo = new ColorPicker();
        controller.modificaColoreBordo.setValue(Color.RED);

        // Imposta il listener
        controller.modificaColoreBordo.setOnAction(e -> {
            forma.setStroke(controller.modificaColoreBordo.getValue());
        });

        // Simula l'azione
        controller.modificaColoreBordo.getOnAction().handle(null);

        assertEquals(Color.RED, forma.getStroke());
    }
    
    //Linea
    /**
     * Verifica che il colore di bordo della linea cambia con quello desiderato.
     */
    @Test
    public void testCambiaColoreBordoLinea() {
        DashboardController controller = new DashboardController();
        Model modello = new Model();
        TipoForma formaDaCreare=TipoForma.SEGMENTO;
        Shape forma=modello.creaForma(formaDaCreare, 0, 0, Color.BLUE, Color.WHITE);
        controller.formaSelezionata = forma;

        // Inizializza il ColorPicker
        controller.modificaColoreBordo = new ColorPicker();
        controller.modificaColoreBordo.setValue(Color.RED);

        // Imposta il listener
        controller.modificaColoreBordo.setOnAction(e -> {
            forma.setStroke(controller.modificaColoreBordo.getValue());
        });

        // Simula l'azione
        controller.modificaColoreBordo.getOnAction().handle(null);

        assertEquals(Color.RED, forma.getStroke());
    }
    
    //Ellisse
    /**
     * Verifica che il colore di riempimento dell'ellisse cambia con quello desiderato.
     */
    @Test
    public void testCambiaColoreRiempimento() {
        DashboardController controller = new DashboardController();
        Model modello = new Model();
        TipoForma formaDaCreare=TipoForma.ELLISSE;
        Shape forma=modello.creaForma(formaDaCreare, 0, 0, Color.BLUE, Color.WHITE);
        controller.formaSelezionata = forma;

        // Inizializza manualmente il ColorPicker
        controller.modificaColoreRiempimento = new ColorPicker();
        controller.modificaColoreRiempimento.setValue(Color.YELLOW);

        // Imposta il listener
        controller.modificaColoreRiempimento.setOnAction(e -> {
            forma.setFill(controller.modificaColoreRiempimento.getValue());
        });

        // Simula l'azione
        controller.modificaColoreRiempimento.getOnAction().handle(null);

        assertEquals(Color.YELLOW, forma.getFill());
    }
    
    /**
     * Verifica che il colore di bordo dell'ellisse cambia con quello desiderato.
     */
    @Test
    public void testCambiaColoreBordo() {
        DashboardController controller = new DashboardController();
        Model modello = new Model();
        TipoForma formaDaCreare=TipoForma.ELLISSE;
        Shape forma=modello.creaForma(formaDaCreare, 0, 0, Color.BLUE, Color.WHITE);
        controller.formaSelezionata = forma;

        // Inizializza il ColorPicker
        controller.modificaColoreBordo = new ColorPicker();
        controller.modificaColoreBordo.setValue(Color.RED);

        // Imposta il listener
        controller.modificaColoreBordo.setOnAction(e -> {
            forma.setStroke(controller.modificaColoreBordo.getValue());
        });

        // Simula l'azione
        controller.modificaColoreBordo.getOnAction().handle(null);

        assertEquals(Color.RED, forma.getStroke());
    }
    

}
