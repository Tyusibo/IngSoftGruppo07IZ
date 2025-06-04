package com.gruppo07iz.geometrika;

import com.gruppo07iz.geometrika.state.*;
import com.gruppo07iz.geometrika.forme.*;
import com.gruppo07iz.geometrika.command.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.function.UnaryOperator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.util.Duration;

public class DashboardController implements Initializable {

    /* OGGETTI VISIBILI NELLA GUI */
    @FXML
    private ImageView apriFoglio, salvaFoglio;
    @FXML
    public ImageView lineaBottone, rettangoloBottone, ellisseBottone, poligonoBottone,testoBottone;
    // Riferimento al bottone attualmente selezionato per un feedback grafico
    private ImageView bottoneAttualmenteSelezionato;
    @FXML
    private ColorPicker coloreRiempimentoCreazione, coloreBordoCreazione;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane anchorPaneSP;
    @FXML
    public Pane lavagna;
    @FXML
    private ImageView bottoneUndo, bottoneSeleziona, bottoneRaggruppa, bottoneSepara,
            bottoneEsportaFormePersonalizzate, bottoneImportaFormePersonalizzate;
    @FXML
    private TextField textFieldTesto = new TextField();
    @FXML
    private Spinner<Double> spinnerDimensioneTesto = new Spinner<>();

    /* MENU CONTESTUALE */
    @FXML
    public VBox pannelloModifica;
    @FXML
    private Button spostaAvanti, spostaDietro, copiaForma, incollaForma, tagliaForma, eliminaForma,
            specchioOrizzontale, specchioVerticale, salvaFiguraPersonalizzata;
    @FXML
    public ColorPicker modificaColoreRiempimento, modificaColoreBordo;
    // Utile per la disattivazione per figure non a contorno chiuso
    @FXML
    private Label etichettaColoreRiempimento;
    
    @FXML
    private Label etichettaDimensione1, etichettaDimensione2;
    @FXML
    private TextField textFieldDimensione1 = new TextField();
    @FXML
    private TextField textFieldDimensione2 = new TextField();
    @FXML
    private TextField textFieldAngoloRotazione = new TextField();
       
    /* VARIABILI DI ISTANZA */
    private Model modello;
    
    // Pila di comandi per supportare l'undo
    public Stack<CommandInterface> pilaComandi;
    
    // formaSelezionata serve per specificare l'istanza esatta di una forma
    // su di cui si vogliono eseguire determinate operazioni
    public FormaPersonalizzabile formaSelezionata = null;
      
    /* STATI */
    // Definisco tutti gli stati perchè queste saranno create nel costruttore all'avvio dell'applicazione
    public StateInterface statoCreazioneFormaRegolare, statoSelezioneFigura, statoCreazionePoligono,
            statoCreazioneTesto, statoFormaPersonalizzata, statoCreazioneGruppo, statoSeparazioneGruppo;
    // Quando verrà selezionata uno stato piuttosto che un altro quello attuale punterà a quella selezionato
    public StateInterface statoAttuale;
    
    // Elementi per la creazione di un poligono arbitrario
    private List<Double> puntiPoligonoArbitrario = new ArrayList<>();
    private List<Circle> cerchiPoligonoArbitrario = new ArrayList<>();
    
    /**
     * Gestisce la rimozione dei punti selezionati per il disegno di un poligono
     * arbitrario. Rimuove dalla lavagna i cerchietti che rappresentano i punti
     * già scelti e svuota le liste che li memorizzano.
     */
    public void gestionePuntiPoligono() {
        lavagna.getChildren().removeAll(cerchiPoligonoArbitrario);
        
        cerchiPoligonoArbitrario.clear();
        puntiPoligonoArbitrario.clear();
    }


    /* FIGURE PERSONALIZZATE */
    //ComboBox contente i nomi delle forme selezionate
    @FXML
    private ComboBox<String> nomiFormePersonalizzate;
    // Mappa che permette il mapping tra il nome con cui è stata salvata la figura e la figura stessa
    private Map<String, FormaPersonalizzabile> formePersonalizzate;
    // Variabile che contiente la forma personalizzata salvata attualmente
    private FormaPersonalizzabile formaPersonalizzataSelezionata;
    
    /**
    * Inizializza e gestisce le forme personalizzate salvate dall'utente.
    *
    * Questo metodo crea una nuova {@link HashMap} per memorizzare le forme personalizzate,
    * imposta lo stato relativo alla creazione di forme personalizzate e configura la
    * {@link ComboBox} per la selezione delle forme salvate. Quando l'utente seleziona una
    * forma dalla lista, viene creata una copia della forma selezionata, viene aggiornata
    * la logica dello stato e lo stato attuale viene impostato su {@code statoFormaPersonalizzata}.
    */
    public void gestioneFormePersonalizzate() {
        formePersonalizzate = new HashMap<>();

        this.statoFormaPersonalizzata = new StateFormaPersonalizzata(modello, this, lavagna);
        
        //Deve essere disabilitata, essendo vuota all'avvio dell'applicazione
        nomiFormePersonalizzate.setDisable(true);
        nomiFormePersonalizzate.setPromptText("Selezionare una forma personalizzata.");

        //Listener per quando una forma viene selezionata
        nomiFormePersonalizzate.valueProperty().addListener((obs, oldVal, newVal) -> {
            // Se è stata effettivamente selezionata una forma
            if (newVal != null) {
                //Prelevo dalla hashmap la forma che corrisponde al nome scelto dall'utente
                FormaPersonalizzabile forma = formePersonalizzate.get(newVal);

                // La preparo per l'inserimento
                formaPersonalizzataSelezionata = forma.clonaForma();
                ((StateFormaPersonalizzata) statoFormaPersonalizzata).setFormaPersonalizzata(formaPersonalizzataSelezionata);
                this.statoAttuale = this.statoFormaPersonalizzata;
                
                //Levo lo stile da altri stati precedentemente selezionati
                this.aggiornaStile(null);
            } 
        });
    }

    
    /* GESTIONE GRUPPI */
    //Mapping tra una forma e il gruppo a cui essa appartiene
    public Map<FormaPersonalizzabile, Gruppo> mappaFormeGruppi;

    /* INCOLLA FIGURA */
    
    // formaCopiata contiene sempre un clone dell'ultima forma su cui è stata eseguita una copia
    // prima di una copia contiene null
    // la vera clonazione avviene nel momento dell'esecuzione di incolla
    private FormaPersonalizzabile formaCopiata;
    
    // Coordinate di dove ho fatto il click destro, serve per eseguire l'incolla nella posizione esatta
    private double coordinataIncollaX;
    private double coordinataIncollaY;
    
    
    /* GESTIONE CONTROLLI SUGLI INPUT */
    
    // Valore massimo per la dimensione di una forma
    private final double valoreDimensioneMassima = 1000.0;

     /**
     * Crea un {@code TextFormatter<Double>} per gestire l'input numerico
     * decimale nei campi di testo.Il formatter applica un filtro che permette
     * di inserire solo numeri positivi con massimo due cifre decimali.
     *
     * @param valore
     * @return un {@code TextFormatter<Double>} configurato con il filtro
     * numerico e il convertitore per Double
     */
    public TextFormatter<Double> creaTextFormatterDouble(Double valore) {
        mappaFormeGruppi = new HashMap();
        UnaryOperator<TextFormatter.Change> filtroNumerico = change -> {
            String nuovoTesto = change.getControlNewText();
            if (nuovoTesto.isEmpty()) {
                return change;
            }
            if (nuovoTesto.matches("\\d*(\\.)?\\d{0,2}")) {
                return change;
            }
            return null;
        };

        return new TextFormatter<>(
                new javafx.util.converter.DoubleStringConverter(),
                valore,
                filtroNumerico
        );
    }
    
    /**
     * Controlla e corregge il valore di una dimensione in input. Se la
     * dimensione è zero, viene impostata a 1. Se la dimensione supera il valore
     * massimo consentito (valoreDimensioneMassima), viene limitata a tale
     * massimo.
     *
     * @param dimensione il valore della dimensione da controllare
     * @return la dimensione corretta, compresa tra 1 e valoreDimensioneMassima
     */
    public double controllaDimensione(double dimensione) {
        if (dimensione == 0) {
            dimensione = 1;
        } else if (dimensione > valoreDimensioneMassima) {
            this.mostraMessaggioInformativo("Non eccedere il valore massimo per la dimensione: "
                    + this.valoreDimensioneMassima);
            dimensione = valoreDimensioneMassima;
        }
        return dimensione;
    }
    
    /**
    * Controlla e corregge il valore di un fattore di scala in input. Se il fattore
    * è zero, viene impostato a 1. Se il fattore supera il valore massimo consentito (10),
    * viene limitato a tale massimo.
    *
    * @param fattoreScala il valore del fattore di scala da controllare
    * @return il fattore di scala corretto, compreso tra 1 e 10
    */
   public double controllaFattoreScala(double fattoreScala) {
       if (fattoreScala == 0) {
           fattoreScala = 1;
       } else if (fattoreScala > 20) {
           this.mostraMessaggioInformativo("Il fattore di scala non può superare 20");
           fattoreScala = 20;
       }
       return fattoreScala;
   }

    /**
     * Controlla e corregge il valore di un angolo in input. Se l'angolo è
     * maggiore o uguale a 360 o minore di 0, viene riportato nel range [0,
     * 360).
     *
     * @param angolo il valore dell'angolo da controllare
     * @return l'angolo corretto, compreso tra 0 (incluso) e 360 (escluso)
     */
    public double controllaAngolo(double angolo) {
        angolo = angolo % 360;
        if (angolo < 0) {
            angolo += 360;
        }
        return angolo;
    }


    /* GRIGLIA E ZOOM */
    @FXML
    private Slider zoomLavagna;
    @FXML
    private RadioButton attivaGriglia;
    @FXML
    private GridPane griglia;
    @FXML
    private Slider intensitaDimensioneGriglia;
    
    private Timeline debounceTimer = new Timeline();
    int dimensione_cella = 40;
    
    /**
     * Aggiorna la griglia visibile nel componente grafico. Il metodo calcola il
     * numero di colonne e righe in base alle dimensioni della lavagna e alla
     * dimensione di ciascuna cella. Successivamente, ripulisce la griglia e
     * ricrea le colonne, le righe e le celle, impostando i vincoli di
     * dimensione e lo stile grafico per ogni cella.
     *
     * Questo metodo è tipicamente chiamato quando si effettua uno zoom sulla
     * lavagna o quando è l'utente vuole aumentare o ridurre la dimensione della
     * cella della griglia .
     */
    private void aggiornaGriglia() {
        int colonne = (int) (lavagna.getWidth() / dimensione_cella);
        int righe = (int) (lavagna.getHeight() / dimensione_cella);

        griglia.getChildren().clear();
        griglia.getColumnConstraints().clear();
        griglia.getRowConstraints().clear();

        for (int i = 0; i < colonne + 1; i++) {
            ColumnConstraints col = new ColumnConstraints(dimensione_cella);
            griglia.getColumnConstraints().add(col);
        }

        for (int j = 0; j < righe + 1; j++) {
            RowConstraints riga = new RowConstraints(dimensione_cella);
            griglia.getRowConstraints().add(riga);
        }

        for (int i = 0; i < colonne + 1; i++) {
            for (int j = 0; j < righe + 1; j++) {
                Region cella = new Region();
                cella.setStyle("-fx-border-color: #f4f4f4; -fx-background-color: transparent;");
                cella.setPrefSize(dimensione_cella, dimensione_cella);
                griglia.add(cella, i, j);
            }
        }

    }

    /**
     * Aggiorna la scala (zoom) del componente grafico modificando la dimensione
     * della griglia e adattando le dimensioni dell'anchorPane di conseguenza.
     *
     * @param scala L'oggetto Scale da applicare per ridimensionare il
     * componente.
     * @param valoreScala Il fattore di scala da applicare (ad esempio 1.0 =
     * 100%, 2.0 = 200%).
     */
    private void aggiornaZoom(Scale scala, double valoreScala) {
        Bounds bounds = anchorPaneSP.getLayoutBounds();
        scala.setX(valoreScala);
        scala.setY(valoreScala);
        //anchorPaneSP.setPrefWidth(larghezza*valoreScala);
        //anchorPaneSP.setPrefHeight(altezza*valoreScala);
        double newWidth = bounds.getWidth() * valoreScala;
        double newHeight = bounds.getHeight() * valoreScala;

        anchorPaneSP.setMinWidth(newWidth);
        anchorPaneSP.setMinHeight(newHeight);

    }
   
    /**
     * * METODO DI UTILITA' PER L'INIZIALIZZAZIONE e CARICA FILE  **
     */
    /**
     * Ripristina lo stato iniziale dell'applicazione. Svuota la pila dei
     * comandi, cancella i collegamenti tra gruppi di forme, rimuove tutti gli elementi grafici dalla lavagna, 
     * azzera le forme selezionate e copiate, e cancella i dati relativi alla costruzione
     * di un poligono arbitrario.
     */
    public void reset() {
        this.pilaComandi = new Stack();
        this.mappaFormeGruppi = new HashMap<>();
        this.lavagna.getChildren().clear();

        this.formaSelezionata = null;
        this.formaPersonalizzataSelezionata = null;
        this.formaCopiata = null;

        this.puntiPoligonoArbitrario.clear();
        this.cerchiPoligonoArbitrario.clear();
        
        this.bottoneAttualmenteSelezionato = null;
        
        coloreRiempimentoCreazione.setValue(Color.WHITE);
        spinnerDimensioneTesto.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 20));
        textFieldTesto.setPromptText("Inserisci testo...");
    }
 

    /**
     * Inizializza il controller dopo il caricamento dell'interfaccia FXML.
     *
     * @param location il percorso URL della risorsa FXML
     * @param resources il bundle di risorse per l'internazionalizzazione
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.modello = new Model();

        this.reset();

        // Creo tutte gli stati
        this.statoCreazioneFormaRegolare = new StateCreazioneFormaRegolare(modello, this, lavagna, TipoFormaRegolare.ELLISSE, coloreRiempimentoCreazione, coloreBordoCreazione);
        this.statoSelezioneFigura = new StateSelezioneFigura(modello, this, lavagna);
        this.statoCreazionePoligono = new StateCreazionePoligono(modello, this, lavagna,
                coloreRiempimentoCreazione, coloreBordoCreazione, puntiPoligonoArbitrario, cerchiPoligonoArbitrario);
        this.statoCreazioneTesto = new StateCreazioneTesto(modello, this, lavagna, textFieldTesto, coloreRiempimentoCreazione, coloreBordoCreazione, spinnerDimensioneTesto);
        this.statoCreazioneGruppo = new StatoCreazioneGruppo(modello, this, lavagna);
        this.statoSeparazioneGruppo = new StatoSeparazioneGruppo(modello, this, lavagna);

        // Inizializzo tutto ciò che riguarda le forme personalizzate, tra cui lo stato
        this.gestioneFormePersonalizzate();

        //Seleziono come quello corrente la selezione per la creazione dell'ellisse
        this.statoAttuale = this.statoCreazioneFormaRegolare;
        aggiornaStile(ellisseBottone);

        // Applicazione del TextFormattere per i textField per contenere solo valori Double, con solo 2 cifre decimali
        spinnerDimensioneTesto.getEditor().setTextFormatter(creaTextFormatterDouble(20.0));
        textFieldDimensione1.setTextFormatter(creaTextFormatterDouble(null));
        textFieldDimensione2.setTextFormatter(creaTextFormatterDouble(null));
        textFieldAngoloRotazione.setTextFormatter(creaTextFormatterDouble(null));

        

        //Risoluzione forme che escono dal pane
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(lavagna.widthProperty());
        clip.heightProperty().bind(lavagna.heightProperty());
        lavagna.setClip(clip);
        scrollPane.setPannable(false);

        scrollPane.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
            double newWidth = newVal.getWidth() * 2;
            double newHeight = newVal.getHeight() * 2;

            anchorPaneSP.setPrefWidth(newWidth);
            anchorPaneSP.setPrefHeight(newHeight);
        });

        // Mantieni lavagna delle stesse dimensioni dell'innerAnchorPane
        anchorPaneSP.prefWidthProperty().addListener((obs, oldVal, newVal) -> {
            lavagna.setPrefWidth(newVal.doubleValue());
        });

        anchorPaneSP.prefHeightProperty().addListener((obs, oldVal, newVal) -> {
            lavagna.setPrefHeight(newVal.doubleValue());
        });

        zoomLavagna.setMax(200); //valore massimo
        zoomLavagna.setMin(0); //valore minimo
        zoomLavagna.setValue(100); //valore di partenza
        zoomLavagna.setMajorTickUnit(50); //passo di 1
        zoomLavagna.setMinorTickCount(0); //solo valori interi, no tick intermedi
        zoomLavagna.setShowTickLabels(true); //mostra le labels dei tick
        zoomLavagna.setShowTickMarks(true); //mostra i tick grafici
        zoomLavagna.setSnapToTicks(true); //va al prossimo tick più vicino

        Scale scaleTransform = new Scale(1, 1, 0, 0);
        anchorPaneSP.getTransforms().add(scaleTransform);

        zoomLavagna.valueProperty().addListener((obs, oldVal, newVal) -> {
            int valore = newVal.intValue();
            double valoreScala;

            if (valore == 0) {
                valoreScala = 0.35;
            } else if (valore % 50 == 0) {
                valoreScala = valore / 100.0;
            } else {
                return; // ignora valori intermedi
            }

            debounceTimer.stop();

            debounceTimer.getKeyFrames().setAll(
                    new KeyFrame(Duration.millis(200), e -> aggiornaZoom(scaleTransform, valoreScala)
                    ));
            debounceTimer.play();
        });

        //inizializzazione della griglia alla dimensione della lavagna
        griglia.prefWidthProperty().bind(lavagna.widthProperty());
        griglia.prefHeightProperty().bind(lavagna.heightProperty());

        griglia.setVisible(false);
        griglia.setManaged(false);
        griglia.setMouseTransparent(true);

        //aggiunta vincoli riga e colonna
        ColumnConstraints colConst = new ColumnConstraints(100);
        RowConstraints rowConst = new RowConstraints(100);
        griglia.getColumnConstraints().add(colConst);
        griglia.getRowConstraints().add(rowConst);
        //aggiunta listener per aumentare dimensione griglia all'aumento della lavagna
        lavagna.widthProperty().addListener((obs, oldW, newW) -> aggiornaGriglia());
        lavagna.heightProperty().addListener((obs, oldH, newH) -> aggiornaGriglia());
        //in base a se il radio button per attivarla è selezionato o meno sarà visibile oppure no
        attivaGriglia.selectedProperty().addListener((obs, oldVal, newVal) -> {
            griglia.setVisible(newVal);
        });

        //slider dimensione cella
        intensitaDimensioneGriglia.setMax(5); //valore massimo
        intensitaDimensioneGriglia.setMin(1); //valore minimo
        intensitaDimensioneGriglia.setValue(2); //valore di partenza
        intensitaDimensioneGriglia.setMajorTickUnit(1); //passo di 1
        intensitaDimensioneGriglia.setMinorTickCount(0); //solo valori interi, no tick intermedi
        intensitaDimensioneGriglia.setShowTickLabels(true); //mostra le labels dei tick
        intensitaDimensioneGriglia.setShowTickMarks(true); //mostra i tick grafici
        intensitaDimensioneGriglia.setSnapToTicks(true); //va al prossimo tick più vicino

        intensitaDimensioneGriglia.valueProperty().addListener((obs, oldVal, newVal) -> {
            dimensione_cella = (((Number) newVal).intValue()) * 20; //cast perchè newval è un Number
            //per non fare troppi aggiornamenti con lo slider
            debounceTimer.stop();
            debounceTimer.getKeyFrames().setAll(
                    new KeyFrame(Duration.millis(200), e -> aggiornaGriglia())
            );
            debounceTimer.play();
        });
    }

    /** SEZIONE GESTIONE DELLA PILA DEI COMANDI **/
    /**
     * Esegue un comando passato come parametro e, se l'esecuzione ha successo,
     * aggiunge il comando alla pila dei comandi per un eventuale undo.
     *
     * @param command Il comando da eseguire, che deve implementare
     * {@link CommandInterface}.
     */
    public void eseguiComando(CommandInterface command) {
        if (command.execute()) {
            this.pilaComandi.push(command);
        }
    }

    /**
     * Aggiunge un comando alla pila dei comandi senza eseguirlo.
     *
     * Questo metodo inserisce il comando specificato nella pila, tipicamente
     * usato per gestire la cronologia delle operazioni.
     *
     * @param command Il comando da aggiungere alla pila, che implementa
     * {@link CommandInterface}.
     */
    public void aggiungiComandoNellaPila(CommandInterface command) {
        this.pilaComandi.push(command);
    }

    /**
     * Annulla l'ultimo comando eseguito. Se la pila dei comandi non è vuota,
     * estrae l'ultimo comando inserito, ne esegue il metodo {@code undo()} per
     * annullarne l'effetto e lo rimuove dalla pila. Infine, chiude il menu
     * toggle associato.
     */
    @FXML
    public void annullaUltimoComando() {
        if (!this.pilaComandi.isEmpty()) {
            CommandInterface comando = this.pilaComandi.pop();
            comando.undo();
            if(lineaBottone != null){ aggiornaStile(bottoneAttualmenteSelezionato); }


            Platform.runLater(() -> {
                if (comando instanceof ComandoCreazioneGruppo){
                    this.mostraMessaggioInformativo("Il gruppo creato è stato sciolto."); 
                }

                if (comando instanceof ComandoSeparazioneGruppo){
                    this.mostraMessaggioInformativo("Il gruppo sciolto è stato riunito."); 
                }
                this.chiudiToggleMenu();
            });

        } else {
            Platform.runLater(() -> {
                this.mostraMessaggioInformativo("Non ci sono comandi da annullare.");
                this.chiudiToggleMenu();
            });
        }
    }


    /*** SEZIONE GESTIONE DELLO STATO ATTIVO CON EFFETTI VISIVI NELLA TOOLBAR ***/
    /**
     * Aggiorna lo stile visivo del bottone selezionato,e prepara il contesto al cambiamento di stato.
     *
     * @param bottoneSelezionato il {@code ImageView} del bottone da aggiornare
     */
    private void aggiornaStile(ImageView bottoneSelezionato) {
        // Disabilito l'effetto per tutti
        lineaBottone.setEffect(null);
        rettangoloBottone.setEffect(null);
        ellisseBottone.setEffect(null);
        poligonoBottone.setEffect(null);
        testoBottone.setEffect(null);
        bottoneSeleziona.setEffect(null);
        bottoneRaggruppa.setEffect(null);
        bottoneSepara.setEffect(null);

        // Aggiorna l'effetto per il bottone selezionato, per figure personalizzate (null) non fare nulla
        if (bottoneSelezionato != null) {
            this.bottoneAttualmenteSelezionato = bottoneSelezionato;
            bottoneSelezionato.setEffect(new DropShadow(4, Color.BLUE));
            nomiFormePersonalizzate.getSelectionModel().clearSelection();  
            nomiFormePersonalizzate.setValue(null);   
        }

        // Disabilito il colore riempimento solo se lo stato è quello della linea
        this.coloreRiempimentoCreazione.setDisable(bottoneSelezionato == this.lineaBottone);

        // Cancello gli effetti che si ottengono in seguito a una creazione poligono incompiuta
        this.gestionePuntiPoligono();

        // Cancello gli effetti che eseguono creazione/separazione gruppo
        for (Node forma : lavagna.getChildren()) {
            forma.setEffect(null);
        }
        ((StatoCreazioneGruppo) this.statoCreazioneGruppo).svuotaListaForme();
        ((StatoSeparazioneGruppo) this.statoSeparazioneGruppo).deselezionaGruppo();

        // Chiudo il context menu se è aperto
        this.chiudiToggleMenu();
    }
    
    /**
     * Applica o rimuove un'ombra blu dal bottone a seconda dello stato attuale.
     * @param bottone il bottone su cui applicare o rimuovere l'effetto.
     */
    private void scambiaStile(ImageView bottone) {
        if (bottone == null) {
            return; 
        }
        bottone.setEffect(bottone.getEffect() == null ? new DropShadow(4, Color.BLUE) : null);
    }
    /**
     * Gestisce l'evento di clic sul bottone per la creazione di una linea.
     *
     * Imposta lo stato corrente di creazione della forma regolare alla modalità
     * LINEA. Aggiorna inoltre lo stile grafico del bottone selezionato per
     * riflettere la scelta dell'utente nell'interfaccia.
     *
     *
     * @param event L'evento del mouse associato al clic sul bottone.
     */
    @FXML
    private void gestoreLineaBottone(MouseEvent event) {
        ((StateCreazioneFormaRegolare) statoCreazioneFormaRegolare).setFormaDaCreare(TipoFormaRegolare.LINEA);
        this.statoAttuale = this.statoCreazioneFormaRegolare;
        aggiornaStile(lineaBottone);
    }

    /**
     * Gestisce l'evento di clic sul bottone per la creazione di un rettangolo.
     *
     * Imposta lo stato corrente di creazione della forma regolare alla modalità
     * RETTANGOLO. Aggiorna anche lo stile grafico del bottone selezionato per
     * indicare visivamente la scelta effettuata dall'utente.
     *
     * @param event L'evento del mouse associato al clic sul bottone.
     */
    @FXML
    private void gestoreRettangoloBottone(MouseEvent event) {
        ((StateCreazioneFormaRegolare) statoCreazioneFormaRegolare).setFormaDaCreare(TipoFormaRegolare.RETTANGOLO);
        this.statoAttuale = this.statoCreazioneFormaRegolare;
        aggiornaStile(rettangoloBottone);
    }

    /**
     * Gestisce l'evento di clic sul bottone per la creazione di un'ellisse.
     *
     * Imposta lo stato corrente di creazione della forma regolare alla modalità
     * ELLISSE. Aggiorna lo stile grafico del bottone selezionato per fornire un
     * feedback visivo all'utente sulla forma attualmente selezionata.
     *
     * @param event L'evento del mouse generato dal clic sul bottone.
     */
    @FXML
    private void gestoreEllisseBottone(MouseEvent event) {
        ((StateCreazioneFormaRegolare) statoCreazioneFormaRegolare).setFormaDaCreare(TipoFormaRegolare.ELLISSE);
        this.statoAttuale = this.statoCreazioneFormaRegolare;
        aggiornaStile(ellisseBottone);
    }

    /**
     * Gestisce il clic sul bottone per attivare la modalità di selezione delle
     * figure.
     *
     * Imposta lo stato attuale sullo stato di selezione, consentendo all'utente
     * di interagire con le figure già presenti (ad esempio selezionarle o
     * modificarle). Inoltre, aggiorna lo stile del bottone per indicare
     * visivamente che la modalità selezione è attiva.
     *
     * @param event L'evento del mouse generato dal clic sul bottone di
     * selezione.
     */
    @FXML
    private void gestorePoligonoBottone(MouseEvent event) {
        this.statoAttuale = this.statoCreazionePoligono;
        aggiornaStile(poligonoBottone);
    }

    /**
     * Gestisce il clic sul bottone per attivare la modalità di scrittura d un
     * testo.
     *
     * Imposta lo stato attuale sullo stato di creazione testo, consentendo
     * all'utente di inseire un testo nella lavagna di disegno. Inoltre,
     * aggiorna lo stile del bottone per indicare visivamente che la modalità di
     * creazione testo è attiva.
     *
     * @param event L'evento del mouse generato dal clic sul bottone di
     * selezione.
     */
    @FXML
    private void gestoreTestoBottone(MouseEvent event) {
        // Utile per aumentare la User Experience
        coloreRiempimentoCreazione.setValue(Color.BLACK);
        this.statoAttuale = this.statoCreazioneTesto;
        aggiornaStile(testoBottone);
    }

    /**
     * Gestisce il clic sul bottone di selezione.
     *
     * Imposta lo stato attuale sulla modalità di selezione delle figure,
     * permettendo all'utente di selezionare, spostare o modificare figure già
     * presenti nella scena. Aggiorna inoltre lo stile del bottone per indicare
     * che la modalità selezione è attiva.
     *
     * @param event L'evento del mouse generato dal clic sul bottone di
     * selezione.
     */
    @FXML
    private void gestoreBottoneSeleziona(MouseEvent event) {
        this.statoAttuale = this.statoSelezioneFigura;
        aggiornaStile(bottoneSeleziona);
    }

        /**
     * Gestisce l'effetto visivo del bottone "Undo" quando viene premuto.
     * Chiama {@code aggiornaStile} per applicare un effetto visivo temporaneo.
     *
     * @param event L'evento mouse associato alla pressione del bottone "Undo".
     */
    @FXML
    private void gestoreBottoneUndoPremuto(MouseEvent event) {
        this.scambiaStile(bottoneUndo);
        this.scambiaStile(bottoneAttualmenteSelezionato);
    }

    /**
     * Rimuove l'effetto visivo dal bottone "Undo" quando il tasto viene rilasciato.
     *
     * @param event L'evento mouse associato al rilascio del bottone "Undo".
     */
    @FXML
    private void gestoreBottoneUndoRilasciato(MouseEvent event) {
        this.scambiaStile(bottoneUndo);
        this.scambiaStile(bottoneAttualmenteSelezionato);
    }
    
    /**
     * Gestisce l'attivazione della modalità "Raggruppa" quando viene premuto
     * il bottone corrispondente. Imposta lo stato interno dell'applicazione
     * su {@code statoCreazioneGruppo} e aggiorna lo stile visivo del bottone.
     *
     * @param event L'evento mouse associato al clic sul bottone "Raggruppa".
     */
    @FXML
    private void gestoreBottoneRaggruppa(MouseEvent event) {
        this.statoAttuale = this.statoCreazioneGruppo;
        aggiornaStile(bottoneRaggruppa);
    }

    /**
     * Gestisce l'attivazione della modalità "Separa" quando viene premuto
     * il bottone corrispondente. Imposta lo stato interno dell'applicazione
     * su {@code statoSeparazioneGruppo} e aggiorna lo stile visivo del bottone.
     *
     * @param event L'evento mouse associato al clic sul bottone "Separa".
     */
    @FXML
    private void gestoreBottoneSepara(MouseEvent event) {
        this.statoAttuale = this.statoSeparazioneGruppo;
        aggiornaStile(bottoneSepara);
    }
    
    /**
     * Mostra una finestra di dialogo informativa con un messaggio specificato.
     * Il tipo di alert è {@code INFORMATION} e il titolo è "Attenzione".
     *
     * @param messaggio il testo da visualizzare nel contenuto della finestra di
     * dialogo
     */
    public void mostraMessaggioInformativo(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Attenzione");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    /*** SEZIONE GESTIONE DEL PANNELLO DEDICATO AL MENU CONTESTUALE ***/
    
    /**
     * Calcola la posizione corretta in cui visualizzare un menu contestuale,
     * assicurandosi che non esca dai limiti dello schermo.
     *
     * Se il menu, posizionato alle coordinate del mouse, eccede i bordi
     * visibili dello schermo, la posizione viene automaticamente regolata per
     * mantenerlo visibile.
     *
     * @param mouseX La coordinata X del mouse al momento dell'evento.
     * @param mouseY La coordinata Y del mouse al momento dell'evento.
     * @param menuWidth La larghezza del menu da visualizzare.
     * @param menuHeight L'altezza del menu da visualizzare.
     * @return Un {@link Point2D} contenente le coordinate X e Y in cui
     * posizionare il menu.
     */
    private Point2D calcolaPosizioneMenu(double mouseX, double mouseY, double menuWidth, double menuHeight) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        double posX = (mouseX + menuWidth > bounds.getMaxX()) ? mouseX - menuWidth : mouseX;
        double posY = (mouseY + menuHeight > bounds.getMaxY()) ? mouseY - menuHeight : mouseY;

        return new Point2D(posX, posY);
    }

    /**
     * Chiude il pannello di modifica (toggle menu) rendendolo invisibile e
     * spostandolo sullo sfondo nella gerarchia dei nodi.
     *
     * Questo metodo viene tipicamente utilizzato per nascondere un pannello
     * contestuale o un menu laterale dopo che un'azione è stata completata o
     * annullata.
     *
     */
    public void chiudiToggleMenu() {
        pannelloModifica.setVisible(false);
        pannelloModifica.toBack();
    }

    /**
     * Apre e visualizza il pannello di modifica (toggle menu) in una posizione
     * calcolata, in risposta a un evento di menu contestuale.
     *
     * Imposta la dimensione massima del pannello, abilita o disabilita il
     * bottone "Incolla" a seconda che una forma sia stata copiata, e calcola la
     * posizione ottimale del menu per evitare che esca fuori dallo schermo. La
     * posizione viene poi convertita in coordinate locali del
     * {@code scrollPane} per il posizionamento corretto.
     *
     *
     * @param event L'evento {@link ContextMenuEvent} che ha causato l'apertura
     * del menu.
     * @param coordinataMenuX La coordinata X in cui posizionare il menu (in
     * coordinate schermo).
     * @param coordinataMenuY La coordinata Y in cui posizionare il menu (in
     * coordinate schermo).
     */
    public void apriToggleMenu(ContextMenuEvent event, double coordinataMenuX, double coordinataMenuY) {

        pannelloModifica.setMaxWidth(Region.USE_PREF_SIZE);
        pannelloModifica.setMaxHeight(Region.USE_PREF_SIZE);

        //gestione bottone incolla
        this.incollaForma.setDisable(formaCopiata == null);

        double menuWidth = pannelloModifica.getWidth();
        double menuHeight = pannelloModifica.getHeight();

        // Calcola posizione ottimale sullo schermo
        Point2D posizioneSchermo = calcolaPosizioneMenu(coordinataMenuX, coordinataMenuY, menuWidth, menuHeight);

        // Converti la posizione da coordinate schermo a coordinate locali del pane principale
        Point2D posizioneNelPane = scrollPane.screenToLocal(posizioneSchermo);

        // Posiziona e mostra il menu
        pannelloModifica.setLayoutX(posizioneNelPane.getX());
        pannelloModifica.setLayoutY(posizioneNelPane.getY());

        pannelloModifica.setVisible(true);

        pannelloModifica.toFront();
    }

    /**
     * Abilita o disabilita i controlli presenti nel menu contestuale (pannello
     * di modifica).
     *
     * Questo metodo consente di attivare o disattivare tutti i bottoni e i
     * campi relativi alla modifica di una figura, in base al parametro fornito.
     * Viene tipicamente utilizzato quando nessuna figura è selezionata oppure
     * per evitare modifiche durante operazioni non permesse.
     *
     *
     * @param abilita {@code true} per disabilitare i controlli, {@code false}
     * per abilitarli.
     */
    public void abilitaBottoniContextMenu(boolean abilita) {
        this.modificaColoreBordo.setDisable(abilita);
        this.modificaColoreRiempimento.setDisable(abilita);
        this.textFieldDimensione1.setDisable(abilita);
        this.textFieldDimensione2.setDisable(abilita);
        this.textFieldAngoloRotazione.setDisable(abilita);
        this.spostaAvanti.setDisable(abilita);
        this.spostaDietro.setDisable(abilita);
        this.copiaForma.setDisable(abilita);
        this.tagliaForma.setDisable(abilita);
        this.eliminaForma.setDisable(abilita);
        this.specchioOrizzontale.setDisable(abilita);
        this.specchioVerticale.setDisable(abilita);
    }

    /*** SEZIONE DEDICATA ALLA GESTIONE DELLE INTERAZIONI DEL MOUSE ALL'INTERNO DELLA LAVAGNA ***/
    
    /**
     * Gestisce l'evento di click destro del mouse.
     *
     * Propaga l'evento allo stato attualmente in uso, delegando la gestione
     * specifica del click destro al comportamento definito dallo stato.
     *
     *
     * @param event L'evento di tipo {@link ContextMenuEvent} generato dal click
     * destro.
     */
    @FXML
    private void clickDestro(ContextMenuEvent event) {
        this.statoAttuale.clickDestro(event);
    }

    /**
     * Gestisce l'evento di trascinamento del mouse con il tasto sinistro
     * premuto.
     *
     * Ignora i trascinamenti effettuati con il tasto destro (secondario).
     * Delega l'elaborazione del trascinamento allo stato attuale.
     *
     *
     * @param event L'evento del mouse associato al trascinamento.
     */
    @FXML
    private void trascinamento(MouseEvent event) {
        // Qui devono avvenire solo click con il tasto sinistro del mouse
        if (event.getButton() == MouseButton.SECONDARY) { return; }

        this.statoAttuale.trascinamento(event);
    }

    /**
     * Gestisce l'evento di movimento del mouse.
     *
     * Propaga l'evento allo stato attualmente in uso per gestire comportamenti
     * specifici legati al movimento del mouse.
     *
     *
     * @param event L'evento del mouse relativo al movimento.
     */
    @FXML
    private void movimentoMouse(MouseEvent event) {
        this.statoAttuale.movimentoMouse(event);
    }

    /**
     * Gestisce l'evento di rilascio del tasto del mouse.
     *
     * Ignora gli eventi generati dal rilascio del tasto destro (secondario).
     * Delega la gestione dell'evento allo stato attuale.
     *
     *
     * @param event L'evento del mouse relativo al rilascio del tasto.
     */
    @FXML
    private void mouseRilasciato(MouseEvent event) {
        // Qui devono avvenire solo click con il tasto sinistro del mouse
        if (event.getButton() == MouseButton.SECONDARY) { return; }

        this.statoAttuale.mouseRilasciato(event);
    }

    /**
     * Gestisce l'evento di click con il tasto sinistro del mouse.
     *
     * Ignora i click effettuati con il tasto destro (secondario). Se il menu
     * contestuale è aperto, lo chiude ma continua l'elaborazione per
     * consentire, ad esempio, l'inizio di un trascinamento di una forma.
     * L'evento viene poi passato allo stato attuale per la gestione specifica.
     *
     * @param event L'evento del mouse relativo al click sinistro.
     */
    @FXML
    private void clickSinistro(MouseEvent event) {
        // Qui devono avvenire solo click con il tasto sinistro del mouse
        if (event.getButton() == MouseButton.SECONDARY) { return; }

        // Se il menu contestuale è aperto lo chiudo, non termino perchè potrebbe accadere che
        // dato il pannello aperto l'utente inizia a trascinare una forma, devo creare il comando
        this.chiudiToggleMenu();

        this.statoAttuale.clickSinistro(event);
    }

    /*** SEZIONE DEDICATO ALL'AGGIORNAMENTO DELLE INFORMAZIONI NEL MENU CONTESTUALE ***/
    
    /**
     * Aggiorna lo stato e i valori del menu contestuale di modifica.
     *
     * Questo metodo aggiorna i controlli relativi ai colori, alle dimensioni e
     * all'angolo di rotazione nel pannello di modifica, riflettendo lo stato
     * corrente della figura selezionata o dell'ambiente di lavoro.
     *
     */
    public void aggiornaContextMenu() {
        this.aggiornaColoriContextMenu();
        this.aggiornaDimensioniContextMenu();
        this.aggiornaAngoloContextMenu();
    }

    /**
     * Aggiorna i selettori di colore nel menu contestuale in base ai colori
     * correnti della forma selezionata.
     */
    public void aggiornaColoriContextMenu() {
        modificaColoreBordo.setValue((Color) this.formaSelezionata.getColoreBordo());
        modificaColoreRiempimento.setValue((Color) this.formaSelezionata.getColoreRiempimento());
    }

    /**
     * Aggiorna i campi del menu contestuale relativi alle dimensioni in base al
     * tipo e ai valori della {@code formaSelezionata}. Per forme
     * bidimensionali, mostra entrambi i campi di dimensione e li popola con i
     * nomi e i valori restituiti dai metodi dell'interfaccia
     * {@code FormaBidimensionale}. Per forme monodimensionali, mostra un solo
     * campo e nasconde il secondo. Inoltre, aggiorna sempre il campo relativo
     * all'angolo di rotazione della forma.
     */
    public void aggiornaDimensioniContextMenu() {
        // Bisogna eseguire un controllo sulla formaSelezionata
        // Per capire se è una formaBidimensionale o una formaMonodimensionale
        // La logica di gestione varia a seconda della classe che implementa l'interfaccia
        
        if (formaSelezionata instanceof FormaBidimensionale) {
            FormaBidimensionale formaBidimensionale = (FormaBidimensionale) formaSelezionata;
            double[] valori = formaBidimensionale.ottieniValoriDimensioni();
            textFieldDimensione1.setText(Double.toString(valori[0]));
            textFieldDimensione2.setText(Double.toString(valori[1]));
            textFieldDimensione2.setDisable(false);
            etichettaDimensione2.setDisable(false);
            modificaColoreRiempimento.setDisable(false);
            etichettaColoreRiempimento.setDisable(false);
            
            // Inoltre se la formaBidimensionale è un gruppo vanno riportati i suoi fattori di scala
            if (formaSelezionata instanceof Gruppo) {
                etichettaDimensione1.setText("Fattore scala X");
                etichettaDimensione2.setText("Fattore scala Y");
            } else { // Altrimenti larghezza e altezza della singola forma
                etichettaDimensione1.setText("Larghezza");
                etichettaDimensione2.setText("Altezza");
            }
               

        } else if (formaSelezionata instanceof FormaMonodimensionale) {
            FormaMonodimensionale fm = (FormaMonodimensionale) formaSelezionata;
            textFieldDimensione1.setText(Double.toString(fm.ottieniValoreDimensione()));
            etichettaDimensione1.setText("Lunghezza");
            etichettaColoreRiempimento.setDisable(true);
            modificaColoreRiempimento.setDisable(true);
            textFieldDimensione2.setDisable(true);
            etichettaDimensione2.setDisable(true);
        }
    }

    /**
     * Aggiorna il campo di testo relativo all'angolo di rotazione nel menu
     * contestuale.
     *
     * Imposta il valore del campo con l'angolo di rotazione corrente della
     * figura selezionata, convertito in stringa.
     *
     */
    public void aggiornaAngoloContextMenu() {
        this.textFieldAngoloRotazione.setText(Double.toString(this.formaSelezionata.getAngolo()));
    }

    /*** ELEMENTI CHE IL CONTROLLER NECESSITA DI OTTENERE DALLO STATO RELATIVO AL CONTEXT MENU ***/
    
    /**
    * Imposta la forma selezionata, considerando la gerarchia di gruppi.
    * Se la forma specificata appartiene a un gruppo, viene selezionato il gruppo più esterno
    * nella gerarchia. Se la forma non fa parte di alcun gruppo, viene selezionata la forma stessa.
    *
    * @param forma la forma da selezionare (può essere un singolo elemento o parte di un gruppo)
    */
    public void setFormaSelezionata(FormaPersonalizzabile forma) {
        FormaPersonalizzabile gruppoEsterno = ottieniGruppoPiuEsterno(forma);
        this.formaSelezionata = gruppoEsterno;
    }
    
    /**
     * Ricerca ricorsivamente il gruppo più esterno a cui appartiene la forma specificata.
     * Se la forma non fa parte di alcun gruppo, restituisce la forma stessa.
     *
     * @param forma la forma di cui trovare il gruppo più esterno
     * @return il gruppo più esterno a cui appartiene la forma, o la forma stessa se non è in un gruppo
     * @throws NullPointerException se il parametro forma è null
     */
    public FormaPersonalizzabile ottieniGruppoPiuEsterno(FormaPersonalizzabile forma) {
        FormaPersonalizzabile corrente = forma;
        FormaPersonalizzabile padre = mappaFormeGruppi.get(corrente);

        while (padre != null) {
            corrente = padre;
            padre = mappaFormeGruppi.get(corrente);
        }

        return corrente != forma ? corrente : forma;
    }

    /**
     * Imposta le coordinate in cui incollare una forma copiata o tagliata.
     *
     * Queste coordinate vengono utilizzate per posizionare la forma incollata
     * nel punto desiderato all'interno dell'area di lavoro.
     *
     * @param coordinataIncollaX La coordinata X dove incollare la forma.
     * @param coordinataIncollaY La coordinata Y dove incollare la forma.
     */
    public void setCoordinateIncolla(double coordinataIncollaX, double coordinataIncollaY) {
        this.coordinataIncollaX = coordinataIncollaX;
        this.coordinataIncollaY = coordinataIncollaY;
    }

    /*** SEZIONE DEDICATA ALL'AGGIORNAMENTO DELLE INFORMAZIONI DELLA FORMA DOPO UNA MODIFICA NEL MENU CONTESTUALE ***/
    
    /**
     * Aggiorna il colore di bordo della forma selezionata in base al colore
     * selezionato dal color picker
     */
    @FXML
    private void modificaColoreBordoEseguita() {
        if (formaSelezionata == null) { return; }

        Color coloreSelezionato = modificaColoreBordo.getValue();
        
        CommandInterface comando = new ComandoModificaColoreBordo(this.modello, formaSelezionata, coloreSelezionato);
        this.eseguiComando(comando);
    }

    /**
     * Aggiorna il colore di riempimento della forma selezionata in base al
     * colore selezionato dal color picker
     */
    @FXML
    private void modificaRiempimentoEseguita() {
        if (formaSelezionata == null) { return; }

        Color coloreSelezionato = modificaColoreRiempimento.getValue();
        
        CommandInterface comando = new ComandoModificaColoreRiempimento(this.modello, formaSelezionata, coloreSelezionato);
        this.eseguiComando(comando);
    }
    
    /**
    * Verifica se il tasto premuto sulla tastiera è valido per l'input.
    * 
    * Il metodo controlla se il tasto premuto è una cifra numerica (0-9), un punto (.), 
    * oppure uno dei tasti funzionali permessi (BACK_SPACE, DELETE, LEFT, RIGHT).
    * 
    * @param event L'evento della tastiera da verificare, contenente le informazioni sul tasto premuto
    * @return {@code true} se il tasto è valido (cifra, punto o tasto funzionale permesso),
    *         {@code false} se il tasto non è valido
    * @see KeyEvent
    * @see KeyCode
    */
    public boolean controllaInputTastiera(KeyEvent event){
        String carattere = event.getText();
        KeyCode codice = event.getCode();

        // Se NON è cifra/punto E NON è un tasto permesso, blocca
        boolean tastoValido = carattere.matches("[0-9\\.]") 
                              || codice == KeyCode.BACK_SPACE 
                              || codice == KeyCode.DELETE
                              || codice == KeyCode.LEFT 
                              || codice == KeyCode.RIGHT;

        return tastoValido;
    }
    /**
     * Gestisce la modifica delle dimensioni della figura selezionata tramite il menu contestuale.
     * In base al tipo della figura selezionata ({@link FormaBidimensionale} o {@link FormaMonodimensionale}),
     * e alla sua eventuale appartenenza a un {@link Gruppo}, questo metodo legge i valori delle
     * nuove dimensioni dai rispettivi campi di testo, li valida tramite {@code controllaDimensione},
     * aggiorna i text field e crea il comando corrispondente da eseguire.
     *
     * @param event l'evento che ha scatenato l'azione (ad esempio la pressione Invio).
     */
    @FXML
    private void gestoreModificheDimensioni(KeyEvent event) {
        int posizione1 = textFieldDimensione1.getCaretPosition();
        int posizione2 = textFieldDimensione2.getCaretPosition();
        double dimensione1, dimensione2;

        if (!controllaInputTastiera(event)) { return; }

        if (formaSelezionata == null) { return; }

        try {
            dimensione1 = Double.parseDouble(textFieldDimensione1.getText().trim());
            dimensione2 = Double.parseDouble(textFieldDimensione2.getText().trim());
        } catch (NumberFormatException e) {
            return; // oppure mostra un messaggio all’utente
        }

        if (formaSelezionata instanceof FormaBidimensionale) {
            
            if(formaSelezionata instanceof Gruppo){
                dimensione1 = controllaFattoreScala(dimensione1);
                dimensione2 = controllaFattoreScala(dimensione2);
            } else {
                dimensione1 = controllaDimensione(dimensione1);
                dimensione2 = controllaDimensione(dimensione2);
            }
        
            textFieldDimensione1.setText(Double.toString(dimensione1));
            textFieldDimensione2.setText(Double.toString(dimensione2));
        
            CommandInterface comando = new ComandoModificaDimensioni(this.modello, (FormaBidimensionale) formaSelezionata, dimensione1, dimensione2);
            this.eseguiComando(comando);
            
        } else if (formaSelezionata instanceof FormaMonodimensionale) {
            
                CommandInterface comando = new ComandoModificaDimensione(this.modello,
                        (FormaMonodimensionale) formaSelezionata, dimensione1);
                this.eseguiComando(comando);
                
            }
        
        textFieldDimensione1.positionCaret(posizione1);
        textFieldDimensione2.positionCaret(posizione2);
    }

    /**
    * Gestisce la modifica dell'angolo di rotazione della figura selezionata tramite il menu contestuale.
    * Il metodo legge il valore dell'angolo dal campo di testo, lo normalizza tramite {@code controllaAngolo},
    * aggiorna il campo di input con il valore corretto e crea il comando di modifica corrispondente
    * da eseguire, sia che la forma appartenga a un gruppo che singolarmente.
    *
    * @param event l'evento che ha scatenato l'azione (es. pressione Invio o perdita del focus).
    */
    @FXML
    private void gestoreModificheAngolo(KeyEvent event) {
        int posizione = textFieldAngoloRotazione.getCaretPosition();

        if (!controllaInputTastiera(event)) { return; }

        if (formaSelezionata == null) { return; }

        double angolo;
        
        try {
            angolo = Double.parseDouble(textFieldAngoloRotazione.getText());
        } catch (NumberFormatException e) {
            return; // oppure mostra un messaggio all’utente
        }
        
        
        angolo = controllaAngolo(angolo);
        textFieldAngoloRotazione.setText(String.valueOf(angolo));

        CommandInterface comando = new ComandoCambiaAngolo(modello, formaSelezionata, angolo);
        this.eseguiComando(comando);
        
        textFieldAngoloRotazione.positionCaret(posizione);
    }

    /*** SEZIONE DEDICATO ALLE INTERAZIONI CON I BOTTONI NEL MENU CONTESTUALE ***/
    
    /**
     * Gestisce l'eliminazione della figura attualmente selezionata.
     *
     * Se una figura è selezionata, crea un comando di eliminazione, lo esegue,
     * chiude il menu contestuale e resetta la selezione.
     *
     */
    @FXML
    private void gestoreEliminaForma() {
        if (formaSelezionata == null) { return; }
        
        CommandInterface comando = new ComandoEliminazione(this.modello, this.lavagna, this.formaSelezionata);
        this.eseguiComando(comando);
        
        this.formaSelezionata = null;
        this.chiudiToggleMenu();
    }

    /**
     * Gestisce la copia della figura attualmente selezionata.
     *
     * Se una figura è selezionata, la memorizza come figura copiata per poter
     * essere incollata successivamente. Non esegue alcun comando perché la
     * copia è un'operazione di controllo. Inoltre chiude il menu contestuale
     * dopo l'operazione.
     *
     */
    @FXML
    private void gestoreCopiaForma() throws CloneNotSupportedException {
        if (formaSelezionata == null) { return; }

        // Copio la forma, se copiassi solo il riferimento eventuali modifiche impatterebbero
        // La copia è un informazione di controllo quindi deve rimanere nel controller
        // Anche per disabilitare l'incolla se nulla è stato ancora copiato
        formaCopiata = formaSelezionata.clonaForma();

        this.formaSelezionata = null;
        this.chiudiToggleMenu();
    }

    /**
     * Gestisce l'incolla di una figura precedentemente copiata.
     *
     * Se esiste una figura copiata, crea una nuova copia (clonata) di tale
     * figura e la incolla alle coordinate specificate. Per evitare conflitti
     * nell'aggiunta alla lavagna, ogni incolla genera una copia distinta.
     * Esegue il comando di incolla e chiude il menu contestuale.
     *
     */
    @FXML
    private void gestoreIncollaForma() {
        
        if (formaCopiata == null) { return; }
        
        FormaPersonalizzabile formaClonata = formaCopiata.clonaForma();
        
        // Se la forma copiata è un gruppo allora devo inserire le entry nella hash map
        // Per rispettare il mapping delle relazioni tra le forme e il gruppo
        if(formaClonata instanceof Gruppo){
            Gruppo gruppo = (Gruppo) formaClonata;
            for (FormaPersonalizzabile forma : gruppo.getVettoreForme()) {
                mappaFormeGruppi.put(forma, gruppo);
            }
        }
        
        CommandInterface comando = new ComandoIncolla(this.modello, this.lavagna,
                formaClonata, this.coordinataIncollaX, this.coordinataIncollaY);
        this.eseguiComando(comando);

        this.formaSelezionata = null;
        this.chiudiToggleMenu();
    }

    /**
     * Gestisce il taglio della figura attualmente selezionata.
     *
     * Se una figura è selezionata, la memorizza come figura copiata e crea un
     * comando per eliminarla dalla lavagna. Dopo l'esecuzione del comando,
     * resetta la selezione e chiude il menu contestuale.
     *
     */
    @FXML
    private void gestoreTagliaForma() {
        if (formaSelezionata == null) { return; }
   
        formaCopiata = formaSelezionata.clonaForma();
        CommandInterface comando = new ComandoEliminazione(modello, lavagna, formaSelezionata);
        this.eseguiComando(comando);

        this.formaSelezionata = null;
        this.chiudiToggleMenu();
    }

    /**
     * Gestisce lo spostamento in avanti della figura selezionata nello stack
     * delle forme.
     *
     * Se una figura è selezionata, crea un comando per portarla in primo piano
     * rispetto alle altre forme, esegue il comando e chiude il menu
     * contestuale.
     *
     * @param event L'evento di tipo {@link MouseEvent} che attiva l'azione.
     */
    @FXML
    private void gestoreSpostaAvanti(MouseEvent event) {
        if (formaSelezionata == null) return;

        CommandInterface comando = new ComandoSpostaAvanti(this.modello, this.lavagna, formaSelezionata);
        this.eseguiComando(comando);
        
        this.formaSelezionata = null;
        this.chiudiToggleMenu();
    }
    /**
     * Gestisce lo spostamento indietro della figura selezionata nello stack
     * delle forme.
     *
     * Se una figura è selezionata, crea un comando per mandarla dietro le altre
     * forme, esegue il comando e chiude il menu contestuale.
     *
     * @param event L'evento di tipo {@link MouseEvent} che attiva l'azione.
     */
    @FXML
    private void gestoreSpostaDietro(MouseEvent event) {
        if (formaSelezionata == null) return;

        CommandInterface comando = new ComandoSpostaIndietro(this.modello, this.lavagna, formaSelezionata);
        this.eseguiComando(comando);
        
        this.formaSelezionata = null;
        this.chiudiToggleMenu();
    }

    /**
     * Gestisce l'evento di mirroring orizzontale della forma selezionata. Se
     * non è selezionata alcuna forma, il metodo termina immediatamente.
     * Altrimenti, crea e esegue un comando di mirroring orizzontale sulla forma
     * selezionata e chiude il menu toggle dell'interfaccia.
     *
     * @param event l'evento del mouse che ha attivato il mirroring orizzontale
     */
    @FXML
    private void gestoreMirroringOrizzontale(MouseEvent event) {
        if (formaSelezionata == null) { return; }
        
        CommandInterface comando = new ComandoMirroringOrizzontale(this.modello, formaSelezionata);
        this.eseguiComando(comando);

        this.formaSelezionata = null;
        this.chiudiToggleMenu();
    }

    /**
     * Gestisce l'evento di mirroring verticale della forma selezionata. Se non
     * è selezionata alcuna forma, il metodo termina immediatamente. Altrimenti,
     * crea e esegue un comando di mirroring verticale sulla forma selezionata e
     * chiude il menu toggle dell'interfaccia.
     *
     * @param event l'evento del mouse che ha attivato il mirroring verticale
     */
    @FXML
    private void gestoreMirroringVerticale(MouseEvent event) {
        if (formaSelezionata == null) { return; }
        
        CommandInterface comando = new ComandoMirroringVerticale(this.modello, formaSelezionata);
        this.eseguiComando(comando);

        this.formaSelezionata = null;
        this.chiudiToggleMenu();
    }

    /**
     * Gestisce il salvataggio di una {@code formaSelezionata} come forma personalizzata.
     * 
     * All'attivazione del metodo (tipicamente tramite clic del mouse), viene mostrato
     * un dialogo per inserire un nome identificativo per la forma selezionata.
     * Se il nome è valido e non esiste già, la forma viene salvata nella mappa 
     * {@code formePersonalizzate} e aggiunta alla lista dei nomi visualizzati nell'interfaccia
     * grafica (ComboBox o ListView {@code nomiFormePersonalizzate}).
     * 
     * Viene inoltre evitato il salvataggio di forme già presenti con lo stesso nome 
     * o della stessa istanza. Se il nome è già utilizzato o la forma è già salvata, viene 
     * mostrato un messaggio informativo all'utente.
     * 
     * @param event Il {@code MouseEvent} che ha attivato l'azione, proveniente da un controllo
     *              dell'interfaccia utente (es. pulsante).
     *
     * @see javafx.scene.control.TextInputDialog
     * @see javafx.scene.control.Alert
     */
    @FXML
    private void salvataggioFormePersonalizzate(MouseEvent event) {
        if (formaSelezionata == null) {
            return;
        }

        // Mostra un popup per chiedere il nome della forma
        TextInputDialog dialogo = new TextInputDialog("NuovaFormaPersonalizzata");
        dialogo.setTitle("Salva forma personalizzata");
        dialogo.setHeaderText("Inserisci un nome per la forma selezionata che vuoi salvare:");
        dialogo.setContentText("Nome:");

        Optional<String> risultato = dialogo.showAndWait();
        if (risultato.isPresent()) {
            String nomeCopia = risultato.get().trim();
            if (!nomeCopia.isEmpty()) {

                if (formePersonalizzate.containsKey(nomeCopia)) {
                    mostraMessaggioInformativo("Nome già esistente, esiste già una figura con questo nome. Scegli un nome diverso.");
                    return;
                }

                for (Map.Entry<String, FormaPersonalizzabile> entry : formePersonalizzate.entrySet()) {
                    if (entry.getValue().equals(formaSelezionata)) {
                        mostraMessaggioInformativo("Questa figura è già stata salvata con il nome: \"" + entry.getKey() + "\".");
                        return;
                    }
                }

                // Esegui la copia della forma
                try {
                    formePersonalizzate.put(nomeCopia, this.formaSelezionata.clonaForma());
                    nomiFormePersonalizzate.getItems().add(nomeCopia);
                    for (Map.Entry<String, FormaPersonalizzabile> elemento : formePersonalizzate.entrySet()) {
                        String nome = elemento.getKey();
                        FormaPersonalizzabile forma = elemento.getValue();

                        this.nomiFormePersonalizzate.setDisable(false);
                    }
                } catch (Exception e) {
                    mostraMessaggioInformativo("Si è verificato un errore durante la copia della figura.");
                    e.printStackTrace(); // solo in fase di sviluppo
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Campo vuoto");
                alert.setHeaderText(null);
                alert.setContentText("Il nome della forma non può essere vuoto.");
                alert.showAndWait();
            }
        }

        this.formaSelezionata = null;
        this.chiudiToggleMenu();
    }

    /*** SEZIONE DEDICATO ALLE INTERAZIONI CON I FILE ***/
    
    /**
     * *
     * Gestore per l'apertura di un nuovo foglio
     *
     * @param event
     */
    @FXML
    private void gestoreApriFoglio(MouseEvent event) {
        this.scambiaStile(apriFoglio);
        this.scambiaStile(bottoneAttualmenteSelezionato);
        
        this.chiudiToggleMenu();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Apri foglio");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Documenti di testo", "*.txt")
        );
        File file = fileChooser.showOpenDialog(lavagna.getScene().getWindow());

        if (file != null) {
            this.reset();

            // Carico il nuovo foglio
            modello.caricaFoglio(file, lavagna, mappaFormeGruppi);
        } else {
            mostraMessaggioInformativo("Operazione annullata, nessun file selezionato.");
        }
        this.scambiaStile(apriFoglio);
        this.scambiaStile(bottoneAttualmenteSelezionato);
    }

    /**
     * Gestore per il salvataggio del foglio corrente.
     *
     * @param event l'evento del mouse che ha attivato il salvataggio
     * @throws IOException se si verifica un errore durante il salvataggio
     */
    @FXML
    private void gestoreSalvaFoglio(MouseEvent event) throws IOException {
        this.scambiaStile(salvaFoglio);
        this.scambiaStile(bottoneAttualmenteSelezionato);
        
        this.chiudiToggleMenu();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salva foglio");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Documenti di testo", "*.txt")
        );
        File file = fileChooser.showSaveDialog(lavagna.getScene().getWindow());

        if (file != null) {
            modello.salvaFoglio(file, lavagna, mappaFormeGruppi);
            // Se il file è null, l'utente ha annullato l'operazione
        } else {
            mostraMessaggioInformativo("Operazione annullata, nessun file selezionato.");
        }
        this.scambiaStile(salvaFoglio);
        this.scambiaStile(bottoneAttualmenteSelezionato);
    }

    public Map<FormaPersonalizzabile, Gruppo> getMappaFormeGruppi() {
        return mappaFormeGruppi;
    }
    

   
    @FXML
    public void gestoreEsportaFormePersonalizzate(MouseEvent event) throws IOException {
        this.scambiaStile(bottoneEsportaFormePersonalizzate);
        this.scambiaStile(bottoneAttualmenteSelezionato);
        
        if(formePersonalizzate.isEmpty()){
            this.mostraMessaggioInformativo("Non sono presenti forme personalizzate da esportare.");
            this.scambiaStile(bottoneEsportaFormePersonalizzate);
            this.scambiaStile(bottoneAttualmenteSelezionato);
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Esporta figure personalizzate");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Documenti di testo", "*.txt")
        );
        File file = fileChooser.showSaveDialog(lavagna.getScene().getWindow());

        if (file != null){
            modello.esportaFormePersonalizzate(file, formePersonalizzate);
        } else {
            mostraMessaggioInformativo("Operazione annullata, nessun file selezionato.");
        }
        this.scambiaStile(bottoneEsportaFormePersonalizzate);
        this.scambiaStile(bottoneAttualmenteSelezionato);
    }

    @FXML
    public void gestoreImportaFormePersonalizzate(MouseEvent event) throws IOException {
        this.scambiaStile(bottoneImportaFormePersonalizzate);
        this.scambiaStile(bottoneAttualmenteSelezionato);
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importa forme personalizzate");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Documenti di testo", "*.txt")
        );
        File file = fileChooser.showOpenDialog(lavagna.getScene().getWindow());

        if (file != null) {

            modello.importaFormePersonalizzate(file, formePersonalizzate, mappaFormeGruppi);
            this.nomiFormePersonalizzate.getItems().clear();

            for(Map.Entry<String, FormaPersonalizzabile> entry : formePersonalizzate.entrySet()) {
                System.out.println("Controller Nome: " + entry.getKey());
                String nome = entry.getKey();
                this.nomiFormePersonalizzate.getItems().add(nome);
                System.out.println(entry.getValue().toText());
            }
        } else {
            mostraMessaggioInformativo("Operazione annullata, nessun file selezionato.");
        }

        if(!formePersonalizzate.isEmpty()) {
            nomiFormePersonalizzate.setDisable(false);
        }
        this.scambiaStile(bottoneImportaFormePersonalizzate);
        this.scambiaStile(bottoneAttualmenteSelezionato);
    }

}

