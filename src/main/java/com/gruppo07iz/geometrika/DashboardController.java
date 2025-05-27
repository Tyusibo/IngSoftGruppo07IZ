package com.gruppo07iz.geometrika;

import com.gruppo07iz.geometrika.state.*;
import com.gruppo07iz.geometrika.forme.*;
import com.gruppo07iz.geometrika.command.*;
import com.gruppo07iz.geometrika.state.StateInterface;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.function.UnaryOperator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.util.Duration;

public class DashboardController implements Initializable {

    /*** OGGETTI GRAFICI DEFINITI NEL FILE .FXLM ***/
    /* OGGETTI VISIBILI NELLA GUI */
    @FXML
    private ImageView apriFoglio, salvaFoglio;
    @FXML
    public ImageView lineaBottone;
    @FXML
    public ImageView rettangoloBottone;
    @FXML
    public ImageView ellisseBottone;
    @FXML
    public ImageView poligonoBottone;
    @FXML
    private ImageView testoBottone;
    @FXML
    private ColorPicker coloreRiempimentoCreazione, coloreBordoCreazione;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane anchorPaneSP;
    @FXML
    public Pane lavagna;
    @FXML
    private VBox toolbar;
    @FXML
    public VBox pannelloModifica;
    @FXML
    private ImageView bottoneUndo;
    @FXML
    private ImageView bottoneSeleziona;
    
    /* PER LA MODIFICA DEI COLORI */
    @FXML
    public ColorPicker modificaColoreRiempimento;
    @FXML
    public ColorPicker modificaColoreBordo;

    /* PER LA MODIFICA DELLE DIMENSIONI */
    @FXML
    private Label etichettaDimensione1;
    @FXML
    private TextField textFieldDimensione1 = new TextField();
    @FXML
    private Label etichettaDimensione2;
    @FXML
    private TextField textFieldDimensione2 = new TextField();

    /* PER LA MODIFICA DELLA ROTAZIONE */
    @FXML
    private TextField textFieldAngoloRotazione = new TextField();
    
    /* BOTTONI NEL CONTEXT MENU */
    @FXML
    private Button spostaAvanti, spostaDietro, copiaForma, incollaForma, tagliaForma, eliminaForma;

    /* Per griglia e zoom */
    @FXML
    private Button rimpicciolisciBottone;
    @FXML
    private Button aumentaBottone;
    @FXML
    private Slider intensitaDimensioneGriglia;
    @FXML
    private Slider zoomLavagna;
    @FXML
    private RadioButton attivaGriglia;
    @FXML
    private GridPane griglia;
    @FXML
    private Pane contenitoreGrigliaLavagna;
    @FXML
    private Button specchioOrizzontale;
    @FXML
    private Button specchioVerticale;
    @FXML
    private Label etichettaColoreRiempimento;

    
    /*** VARIABILI DI ISTANZA ***/
    // formaSelezionata serve per specificare l'istanza esatta di una forma
    // su di cui si vogliono eseguire determinate operazioni
    public Shape formaSelezionata = null;
    // formaCopiata contiene sempre l'ultima forma su cui è stata eseguita una copia
    // prima di una copia contiene null
    // la vera clonazione avviene nel momento dell'esecuzione di incolla
    private FormaPersonalizzabile formaCopiata;

    private Model modello;
    // Definisco tutti gli stati perchè queste saranno create nel costruttore
    public StateInterface statoCreazioneFormaRegolare;
    public StateInterface statoSelezioneFigura;
    // Quando verrà selezionata uno stato piuttosto che un altro quello attuale punterà a quella selezionato
    public StateInterface statoAttuale;

    // Pila di comandi per supportare l'undo
    public Stack<CommandInterface> pilaComandi;
    
    // valore massimo per la dimensione di una forma
    private final double valoreDimensioneMassima = 1000.0;

    // Coordinate di dove ho fatto il click destro, serve per eseguire l'incolla nella posizione esatta
    private double coordinataIncollaX;
    private double coordinataIncollaY;
    
    private Timeline debounceTimer = new Timeline();
    int dimensione_cella = 40;
    @FXML
    private TextField textFieldTesto;
    @FXML
    private TextField textFieldDimensioneTesto;
    @FXML
    private ComboBox<String> nomiFormePersonalizzate;


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
            this.mostraMessaggioInformativo("Non eccedere il valore massimo per la dimensione: " + 
                    this.valoreDimensioneMassima);

            dimensione = valoreDimensioneMassima;
        }
        return dimensione;
    }


    
    
    /**
     * Crea un {@code TextFormatter<Double>} per gestire l'input numerico
     * decimale nei campi di testo. Il formatter applica un filtro che permette
     * di inserire solo numeri positivi con massimo due cifre decimali.
     *
     * @return un {@code TextFormatter<Double>} configurato con il filtro
     * numerico e il convertitore per Double
     */
    public TextFormatter<Double> creaTextFormatterDouble() {
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
                null,
                filtroNumerico
        );
    }
    /**
    * Aggiorna la griglia visibile nel componente grafico.
    * Il metodo calcola il numero di colonne e righe in base alle dimensioni 
    * della lavagna e alla dimensione di ciascuna cella. 
    * Successivamente, ripulisce la griglia e ricrea le colonne, le righe e le celle,
    * impostando i vincoli di dimensione e lo stile grafico per ogni cella.
    * 
    * Questo metodo è tipicamente chiamato quando si effettua uno zoom sulla lavagna 
    * o quando è l'utente vuole aumentare o ridurre la dimensione della cella della griglia .
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
                cella.setStyle("-fx-border-color: #d3d3d3; -fx-background-color: transparent;");
                cella.setPrefSize(dimensione_cella, dimensione_cella);
                griglia.add(cella, i, j);
            }
        }

    }

    /*** METODO DI UTILITA' PER L'INIZIALIZZAZIONE  ***/
    /**
    * Ripristina lo stato iniziale dell'applicazione. Svuota la pila dei comandi,
    * rimuove tutti gli elementi grafici dalla lavagna, azzera le forme selezionate
    * e copiate, e cancella i dati relativi alla costruzione di un poligono arbitrario.
    */
    public void reset(){
        this.pilaComandi = new Stack();
        this.lavagna.getChildren().clear();

        this.formaSelezionata = null;
        this.formaCopiata = null;
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
        // Colore di default per il riempimento
        coloreRiempimentoCreazione.setValue(Color.WHITE);
        
        this.reset();
        
        // Creo tutte gli stati
        this.statoCreazioneFormaRegolare = new StateCreazioneFormaRegolare(modello, this, lavagna, TipoFormaRegolare.ELLISSE, coloreRiempimentoCreazione, coloreBordoCreazione);
        this.statoSelezioneFigura = new StateSelezioneFigura(modello, this, lavagna);


        //Seleziono come quello corrente la selezione per la creazione dell'ellisse
        this.statoAttuale = this.statoCreazioneFormaRegolare;
        aggiornaStile(ellisseBottone);
        
        // Applicazione del TextFormattere per i textField per contenere solo valori Double, con solo 2 cifre decimali
        textFieldDimensione1.setTextFormatter(creaTextFormatterDouble());
        textFieldDimensione2.setTextFormatter(creaTextFormatterDouble());
        textFieldAngoloRotazione.setTextFormatter(creaTextFormatterDouble());
        
        // Definizione dei tooltip
        Tooltip tooltipSalvaFoglio = new Tooltip("Salva Foglio");
        Tooltip.install(salvaFoglio, tooltipSalvaFoglio);
        Tooltip tooltipApriFoglio = new Tooltip("Carica Foglio");
        Tooltip.install(apriFoglio, tooltipApriFoglio);

        Tooltip tooltipLineaBottone = new Tooltip("Linea");
        Tooltip.install(lineaBottone, tooltipLineaBottone);
        Tooltip tooltipRettangoloBottone = new Tooltip("Rettangolo");
        Tooltip.install(rettangoloBottone, tooltipRettangoloBottone);
        Tooltip tooltipEllisseBottone = new Tooltip("Ellisse");
        Tooltip.install(ellisseBottone, tooltipEllisseBottone);
        
        
        
        //risoluzione forme che escono dal pane
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



        Scale scaleTransform = new Scale(1, 1, 0, 0);
        anchorPaneSP.getTransforms().add(scaleTransform);


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

    
    /*** SEZIONE GESTIONE DELLA PILA DEI COMANDI ***/
    
    /**
     * Esegue un comando passato come parametro e, se l'esecuzione ha successo,
     * aggiunge il comando alla pila dei comandi per un eventuale undo o storico.
     * 
     * @param command Il comando da eseguire, che deve implementare {@link CommandInterface}.
     */
    public void eseguiComando(CommandInterface command) {
        if (command.execute()) {
            this.pilaComandi.push(command);
        }
    }

    /**
     * Aggiunge un comando alla pila dei comandi senza eseguirlo.
     * 
     * Questo metodo inserisce il comando specificato nella pila, 
     * tipicamente usato per gestire la cronologia delle operazioni.
     * 
     * @param command Il comando da aggiungere alla pila, che implementa {@link CommandInterface}.
     */
    public void aggiungiComandoNellaPila(CommandInterface command) {
        this.pilaComandi.push(command);
    }

    /**
     * Annulla l'ultimo comando eseguito.
     * Se la pila dei comandi non è vuota, estrae l'ultimo comando inserito,
     * ne esegue il metodo {@code undo()} per annullarne l'effetto e lo rimuove dalla pila.
     * Infine, chiude il menu toggle associato.
     */
    @FXML
    public void annullaUltimoComando() {
        if (!this.pilaComandi.isEmpty()) {
            CommandInterface comando = this.pilaComandi.pop();
            comando.undo();
        } else {
            this.mostraMessaggioInformativo("Non ci sono comandi da annullare.");
        }
        this.chiudiToggleMenu(); 
    }

    
    /*** SEZIONE GESTIONE DELLO STATO ATTIVO CON EFFETTI VISIVI NELLA TOOLBAR ***/
    /**
     * Aggiorna lo stile visivo del bottone selezionato.
     *
     * @param bottoneSelezionato il {@code ImageView} del bottone da aggiornare
     */
    private void aggiornaStile(ImageView bottoneSelezionato) {
        // Disabilito l'effetto per tutti
        lineaBottone.setEffect(null);
        rettangoloBottone.setEffect(null);
        ellisseBottone.setEffect(null);
        bottoneSeleziona.setEffect(null);
        
        // Aggiorna l'effetto per il bottone selezionato
        bottoneSelezionato.setEffect(new DropShadow(10, Color.BLUE));
        
        // Disabilito il colore riempimento solo se lo stato è quello della linea
        this.coloreRiempimentoCreazione.setDisable(bottoneSelezionato == this.lineaBottone);
          
        // Chiudo il context menu se è aperto
        this.chiudiToggleMenu();
    }

    /**
     * Gestisce l'evento di clic sul bottone per la creazione di una linea.
     * 
     * Imposta lo stato corrente di creazione della forma regolare alla modalità LINEA.
     * Aggiorna inoltre lo stile grafico del bottone selezionato per riflettere
     * la scelta dell'utente nell'interfaccia.
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
     * Imposta lo stato corrente di creazione della forma regolare alla modalità RETTANGOLO.
     * Aggiorna anche lo stile grafico del bottone selezionato per indicare
     * visivamente la scelta effettuata dall'utente.
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
     * Imposta lo stato corrente di creazione della forma regolare alla modalità ELLISSE.
     * Aggiorna lo stile grafico del bottone selezionato per fornire un feedback visivo
     * all'utente sulla forma attualmente selezionata.
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
     * Gestisce il clic sul bottone per attivare la modalità di selezione delle figure.
     * 
     * Imposta lo stato attuale sullo stato di selezione, consentendo 
     * all'utente di interagire con le figure già presenti (ad esempio selezionarle o modificarle).
     * Inoltre, aggiorna lo stile del bottone per indicare visivamente 
     * che la modalità selezione è attiva.
     *
     * @param event L'evento del mouse generato dal clic sul bottone di selezione.
     */
    @FXML
    private void gestorePoligonoBottone(MouseEvent event) {

    }
    
    /**
     * Gestisce il clic sul bottone di selezione.
     *
     * Imposta lo stato attuale sulla modalità di selezione delle figure,
     * permettendo all'utente di selezionare, spostare o modificare figure
     * già presenti nella scena. Aggiorna inoltre lo stile del bottone per
     * indicare che la modalità selezione è attiva.
     *
     * @param event L'evento del mouse generato dal clic sul bottone di selezione.
     */
    @FXML
    private void gestoreBottoneSeleziona(MouseEvent event){
        this.statoAttuale = this.statoSelezioneFigura;
        aggiornaStile(bottoneSeleziona);
    }

    /*** METODO DEDICATO AI MESSAGGI DA FORNIRE ALL'UTENTE ***/
    /**
    * Mostra una finestra di dialogo informativa con un messaggio specificato.
    * Il tipo di alert è {@code INFORMATION} e il titolo è "Attenzione".
    *
    * @param messaggio il testo da visualizzare nel contenuto della finestra di dialogo
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
     * Se il menu, posizionato alle coordinate del mouse, eccede i bordi visibili
     * dello schermo, la posizione viene automaticamente regolata per mantenerlo visibile.
     *
     * @param mouseX     La coordinata X del mouse al momento dell'evento.
     * @param mouseY     La coordinata Y del mouse al momento dell'evento.
     * @param menuWidth  La larghezza del menu da visualizzare.
     * @param menuHeight L'altezza del menu da visualizzare.
     * @return Un {@link Point2D} contenente le coordinate X e Y in cui posizionare il menu.
     */
    private Point2D calcolaPosizioneMenu(double mouseX, double mouseY, double menuWidth, double menuHeight) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        double posX = (mouseX + menuWidth > bounds.getMaxX()) ? mouseX - menuWidth : mouseX;
        double posY = (mouseY + menuHeight > bounds.getMaxY()) ? mouseY - menuHeight : mouseY;

        return new Point2D(posX, posY);
    }

    /**
     * Chiude il pannello di modifica (toggle menu) rendendolo invisibile
     * e spostandolo sullo sfondo nella gerarchia dei nodi.
     *
     * Questo metodo viene tipicamente utilizzato per nascondere un pannello
     * contestuale o un menu laterale dopo che un'azione è stata completata
     * o annullata.
     *
     */
    public void chiudiToggleMenu(){
        pannelloModifica.setVisible(false);
        pannelloModifica.toBack();
    }
    
    /**
     * Apre e visualizza il pannello di modifica (toggle menu) in una posizione calcolata,
     * in risposta a un evento di menu contestuale.
     * 
     * Imposta la dimensione massima del pannello, abilita o disabilita il bottone "Incolla"
     * a seconda che una forma sia stata copiata, e calcola la posizione ottimale del menu
     * per evitare che esca fuori dallo schermo. La posizione viene poi convertita in coordinate
     * locali del {@code scrollPane} per il posizionamento corretto.
     * 
     *
     * @param event            L'evento {@link ContextMenuEvent} che ha causato l'apertura del menu.
     * @param coordinataMenuX  La coordinata X in cui posizionare il menu (in coordinate schermo).
     * @param coordinataMenuY  La coordinata Y in cui posizionare il menu (in coordinate schermo).
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
     * Abilita o disabilita i controlli presenti nel menu contestuale (pannello di modifica).
     *
     * Questo metodo consente di attivare o disattivare tutti i bottoni e i campi
     * relativi alla modifica di una figura, in base al parametro fornito.
     * Viene tipicamente utilizzato quando nessuna figura è selezionata oppure
     * per evitare modifiche durante operazioni non permesse.
     *
     *
     * @param abilita {@code true} per disabilitare i controlli, {@code false} per abilitarli.
     */
    public void abilitaBottoniContextMenu(boolean abilita){
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
     * @param event L'evento di tipo {@link ContextMenuEvent} generato dal click destro.
     */
    @FXML
    private void clickDestro(ContextMenuEvent event) {
        this.statoAttuale.clickDestro(event);
    }

    /**
     * Gestisce l'evento di trascinamento del mouse con il tasto sinistro premuto.
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
     * Propaga l'evento allo stato attualmente in uso per gestire
     * comportamenti specifici legati al movimento del mouse.
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
     * Ignora i click effettuati con il tasto destro (secondario).
     * Se il menu contestuale è aperto, lo chiude ma continua l'elaborazione
     * per consentire, ad esempio, l'inizio di un trascinamento di una forma.
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
     * Questo metodo aggiorna i controlli relativi ai colori, alle dimensioni
     * e all'angolo di rotazione nel pannello di modifica, riflettendo lo stato
     * corrente della figura selezionata o dell'ambiente di lavoro.
     * 
     */
    public void aggiornaContextMenu(){
        this.aggiornaColoriContextMenu();
        this.aggiornaDimensioniContextMenu();
    }
    
    /**
     * Aggiorna i selettori di colore nel menu contestuale in base ai colori
     * correnti della forma selezionata.
     */
    public void aggiornaColoriContextMenu() {
        modificaColoreBordo.setValue((Color) this.formaSelezionata.getStroke());
        modificaColoreRiempimento.setValue((Color) this.formaSelezionata.getFill());
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
        // La logica di gestione varia a seconda dell'interfaccia che implementa
        // Vengono utilizzati i metodi dell'interfaccia

        if (formaSelezionata instanceof FormaBidimensionale) {
            FormaBidimensionale fb = (FormaBidimensionale) formaSelezionata;
            String[] nomi = fb.ottieniNomiDimensioni();
            double[] valori = fb.ottieniValoriDimensioni();

            etichettaDimensione1.setText(nomi[0]);
            textFieldDimensione1.setText(Double.toString(valori[0]));
            etichettaDimensione2.setText(nomi[1]);
            textFieldDimensione2.setText(Double.toString(valori[1]));

            textFieldDimensione2.setDisable(false);
            etichettaDimensione2.setDisable(false);
            modificaColoreRiempimento.setDisable(false);
            etichettaColoreRiempimento.setDisable(false);

        } else if (formaSelezionata instanceof FormaMonodimensionale) {
            FormaMonodimensionale fm = (FormaMonodimensionale) formaSelezionata;
            etichettaDimensione1.setText(fm.ottieniNomeDimensione());
            textFieldDimensione1.setText(Double.toString(fm.ottieniValoreDimensione()));
            
            etichettaColoreRiempimento.setDisable(true);
            modificaColoreRiempimento.setDisable(true);
            textFieldDimensione2.setText("0");
            textFieldDimensione2.setDisable(true);
            etichettaDimensione2.setText("Dimensione 2");
            etichettaDimensione2.setDisable(true);
        }
    }


    
    /* ELEMENTI CHE IL CONTROLLER NECESSITA DI OTTENERE DALLO STATO RELATIVO AL CONTEXT MENU */

    /**
     * Imposta la figura attualmente selezionata.
     * 
     * Questo metodo aggiorna il riferimento alla forma selezionata su cui
     * operare con il menu contestuale o altre funzionalità.
     *
     * @param formaSelezionata La figura {@link Shape} da impostare come selezionata.
     */
    public void setFormaSelezionata(Shape formaSelezionata) {
        this.formaSelezionata = formaSelezionata;
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
     * Gestisce le modifiche delle dimensioni della figura selezionata in risposta
     * a un evento di azione (ad esempio, l'invio di un valore da un campo di testo).
     * 
     * Se la figura selezionata è bidimensionale, recupera due dimensioni dai campi di testo,
     * crea un comando di modifica dimensioni e lo esegue.
     * Se la figura selezionata è monodimensionale, recupera una singola dimensione,
     * crea un comando di modifica dimensione e lo esegue.
     * 
     *
     * @param event L'evento di tipo {@link ActionEvent} che attiva la modifica.
    */
    @FXML
    private void gestoreModificheDimensioni(ActionEvent event) {
        if (formaSelezionata == null) { return; }

        if (formaSelezionata instanceof FormaBidimensionale) {
            double dimensione1 = Double.parseDouble(textFieldDimensione1.getText());
            double dimensione2 = Double.parseDouble(textFieldDimensione2.getText());
            dimensione1 = this.controllaDimensione(dimensione1);
            dimensione2 = this.controllaDimensione(dimensione2);
            textFieldDimensione1.setText(Double.toString(dimensione1));
            textFieldDimensione2.setText(Double.toString(dimensione2));

            CommandInterface comando = new ComandoModificaDimensioni(this.modello,
                    (FormaBidimensionale) formaSelezionata, dimensione1, dimensione2);
            this.eseguiComando(comando);
        } else if (formaSelezionata instanceof FormaMonodimensionale) {
            double dimensione = Double.parseDouble(textFieldDimensione1.getText());
            dimensione = this.controllaDimensione(dimensione);
            textFieldDimensione1.setText(Double.toString(dimensione));
            
            CommandInterface comando = new ComandoModificaDimensione(this.modello,
                    (FormaMonodimensionale) formaSelezionata, dimensione);
            this.eseguiComando(comando);
        }

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
        if (formaSelezionata == null) { return ; }
        CommandInterface comando = new ComandoEliminazione(this.modello, this.lavagna, this.formaSelezionata);
        this.eseguiComando(comando);
        this.chiudiToggleMenu(); 
        formaSelezionata = null;
        
    }
    
    /**
    * Crea una copia della forma selezionata castata a {@code FormaPersonalizzabile}.
    * <p>
    * Questo metodo effettua un cast iniziale della forma selezionata alla classe {@code FormaPersonalizzabile},
    * quindi invoca il metodo {@code clonaForma()} per ottenere una copia della forma. Il risultato è poi
    * nuovamente castato a {@code FormaPersonalizzabile} e restituito.
    * </p>
    *
    * @param formaDaCopiare la forma di tipo {@code Shape} da copiare; si presume che {@code formaSelezionata}
    *                       sia riferita allo stesso oggetto o comunque ad un oggetto compatibile.
    * @return una nuova istanza di {@code FormaPersonalizzabile} che rappresenta una copia della forma selezionata.
    * @throws ClassCastException se {@code formaSelezionata} non è un'istanza di {@code FormaPersonalizzabile}.
    */
    public FormaPersonalizzabile copiaForma(Shape formaDaCopiare){
        // Cast iniziale dell'oggetto selezionato alla classe FormaPersonalizzabile
        FormaPersonalizzabile tmp = (FormaPersonalizzabile) formaDaCopiare;

        // Clonazione della forma e nuovo cast
        return (FormaPersonalizzabile) tmp.clonaForma();
    }
    
    
    /**
     * Gestisce la copia della figura attualmente selezionata.
    * 
    * Se una figura è selezionata, la memorizza come figura copiata
    * per poter essere incollata successivamente.
    * Non esegue alcun comando perché la copia è un'operazione di controllo.
    * Inoltre chiude il menu contestuale dopo l'operazione.
    * 
    */
    @FXML
    private void gestoreCopiaForma() {
        if (formaSelezionata == null) { return ; }

        // Copio la forma, se copiassi solo il riferimento eventuali modifiche impatterebbero
        // La copia è un informazione di controllo quindi deve rimanere qui
        // Anche per disabilitare l'incolla se nulla è copiato
        formaCopiata = this.copiaForma(this.formaSelezionata);
       
        this.chiudiToggleMenu(); 
    }


    /**
     * Gestisce l'incolla di una figura precedentemente copiata.
     * 
     * Se esiste una figura copiata, crea una nuova copia (clonata) di tale figura
     * e la incolla alle coordinate specificate.
     * Per evitare conflitti nell'aggiunta alla lavagna, ogni incolla genera una copia distinta.
     * Esegue il comando di incolla e chiude il menu contestuale.
     * 
     */
    @FXML
    private void gestoreIncollaForma() {
        if (formaCopiata == null) { return ; }
        // Esegue una vera copia della forma copiata
        // Ogni incolla deve creare copie distinte per evitare errori di duplicazione del nodo

        CommandInterface comando = new ComandoIncolla(this.modello, this.lavagna,
                this.formaCopiata.clonaForma(), this.coordinataIncollaX, this.coordinataIncollaY);
        this.eseguiComando(comando);

        this.chiudiToggleMenu(); 
    }

    /**
     * Gestisce il taglio della figura attualmente selezionata.
     *
     * Se una figura è selezionata, la memorizza come figura copiata e
     * crea un comando per eliminarla dalla lavagna.
     * Dopo l'esecuzione del comando, resetta la selezione e chiude il menu contestuale.
     * 
     */
    @FXML
    private void gestoreTagliaForma() {
        if (formaSelezionata == null) { return ; }
        
        formaCopiata = this.copiaForma(this.formaSelezionata);

        CommandInterface comando = new ComandoEliminazione(modello, lavagna, formaSelezionata);
        this.eseguiComando(comando);
        formaSelezionata = null;
        this.chiudiToggleMenu(); 
    }

    /**
     * Gestisce lo spostamento in avanti della figura selezionata nello stack delle forme.
     *
     * Se una figura è selezionata, crea un comando per portarla in primo piano
     * rispetto alle altre forme, esegue il comando e chiude il menu contestuale.
     *
     * @param event L'evento di tipo {@link MouseEvent} che attiva l'azione.
     */
    @FXML
    private void gestoreSpostaAvanti(MouseEvent event) {
        if (formaSelezionata == null) { return ; }
        CommandInterface comando = new ComandoSpostaAvanti(this.modello, formaSelezionata);
        this.eseguiComando(comando);
        this.chiudiToggleMenu(); 
    }

    /**
     * Gestisce lo spostamento indietro della figura selezionata nello stack delle forme.
     *
     * Se una figura è selezionata, crea un comando per mandarla dietro le altre forme,
     * esegue il comando e chiude il menu contestuale.
     *
     * @param event L'evento di tipo {@link MouseEvent} che attiva l'azione.
     */
    @FXML
    private void gestoreSpostaDietro(MouseEvent event) {
        if (formaSelezionata == null) { return ; }
        CommandInterface comando = new ComandoSpostaIndietro(this.modello, formaSelezionata);
        this.eseguiComando(comando);
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
            modello.caricaFoglio(file, lavagna);
        }
        // Se il file è null, l'utente ha annullato l'operazione
    }

    /**
     * Gestore per il salvataggio del foglio corrente.
     *
     * @param event l'evento del mouse che ha attivato il salvataggio
     * @throws IOException se si verifica un errore durante il salvataggio
     */
    @FXML
    private void gestoreSalvaFoglio(MouseEvent event) throws IOException {
        this.chiudiToggleMenu(); 

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salva foglio");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Documenti di testo", "*.txt")
        );
        File file = fileChooser.showSaveDialog(lavagna.getScene().getWindow());

        if (file != null) {
            modello.salvaFoglio(file, lavagna);
            // Se il file è null, l'utente ha annullato l'operazione
        }
    }

    @FXML
    private void gestoreModificheAngolo(ActionEvent event) {

    }

    @FXML
    private void gestoreMirroringOrizzontale(MouseEvent event) {
    }
    

    @FXML
    private void gestoreMirroringVerticale(MouseEvent event) {
        
    }
    
    @FXML
    private void gestoreAttivaGriglia(ActionEvent event) {
    }

    @FXML
    private void gestoreRimpicciolisciBottone(ActionEvent event) {
    }

    @FXML
    private void gestoreAumentaBottone(ActionEvent event) {
    }

    @FXML
    private void gestoreCambiaBordo(ActionEvent event) {
    }

    @FXML
    private void gestoreCambiaRiempimento(ActionEvent event) {
    }

    @FXML
    private void gestoreZoomLavagna(MouseEvent event) {
    }

    @FXML
    private void gestoreIntensitaGriglia(MouseEvent event) {
    }
    
    @FXML
    private void gestoreTestoBottone(MouseEvent event) {
    }
}
