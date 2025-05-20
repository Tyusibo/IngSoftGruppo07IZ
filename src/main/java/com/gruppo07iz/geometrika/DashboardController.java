package com.gruppo07iz.geometrika;

import com.gruppo07iz.geometrika.classi.TipoForma;
import com.gruppo07iz.geometrika.interfacce.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Screen;


public class DashboardController implements Initializable{

    /* OGGETTI GRAFICI DEFINITI NEL FILE .FXLM */
    
    /* OGGETTI VISIBILI NELLA GUI */
    @FXML
    private ImageView apriFoglio, salvaFoglio;
    @FXML
    private ImageView lineaBottone, rettangoloBottone, ellisseBottone;
    @FXML
    private ColorPicker coloreRiempimentoCreazione, coloreBordoCreazione;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane lavagna;
    @FXML
    private VBox pannelloModifica;
    
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
    
    /*
    @FXML
    private Button copiaForma;
    @FXML
    private Button incollaForma;
    @FXML
    private Button tagliaForma;
    @FXML
    private Button eliminaForma;
    @FXML
    private Button spostaAvanti;
    @FXML
    private Button spostaDietro;
    */
    
    
    /* VARIABILI DI ISTANZA */
    // formaDaCreare serve per specificare il tipo di forma da creare 
    public TipoForma formaDaCreare = null;
    // formaSelezionata serve per specificare l'istanza esatta di una forma
    // su di cui si vogliono eseguire determinate operazioni
    public Shape formaSelezionata = null;
    // formaCopiata contiene sempre l'ultima forma su cui è stata eseguita una copia
    // prima di una copia contiene null
    // la vera clonazione avviene nel momento dell'esecuzione di incolla
    public FormaPersonalizzabile formaCopiata = null;
    
    // modello da creare in fase di instanziazione e da utilizzare per le operazioni
    // che richiedono la logica di business
    private Model modello;
    
    // valore massimo per la dimensione di una forma
    private final double valoreDimensioneMassima = 1000.0;
    
    // Utili per eseguire il trascinamento
    private double startX, startY;


    /**
    * Controlla e corregge il valore di una dimensione in input.
    * Se la dimensione è zero, viene impostata a 1.
    * Se la dimensione supera il valore massimo consentito (valoreDimensioneMassima),
    * viene limitata a tale massimo.
    * @param dimensione il valore della dimensione da controllare
    * @return la dimensione corretta, compresa tra 1 e valoreDimensioneMassima
    */
    public double controllaDimensione(double dimensione){
        if(dimensione == 0){
            dimensione = 1;
        }
        else if (dimensione > valoreDimensioneMassima){
            dimensione = valoreDimensioneMassima;
        }
        return dimensione;
    }
    
    /**
    * Crea un {@code TextFormatter<Double>} per gestire l'input numerico decimale
    * nei campi di testo.
    * Il formatter applica un filtro che permette di inserire solo numeri
    * positivi con massimo due cifre decimali.
    * @return un {@code TextFormatter<Double>} configurato con il filtro numerico e
    *         il convertitore per Double
    */
    public TextFormatter<Double> creaTextFormatterDouble() {
        UnaryOperator<TextFormatter.Change> filtroNumerico = change -> {
            String nuovoTesto = change.getControlNewText();
            if (nuovoTesto.isEmpty()) return change;
            if (nuovoTesto.matches("\\d*(\\.|,)?\\d{0,2}")) return change;
            return null;
        };

        return new TextFormatter<>(
            new javafx.util.converter.DoubleStringConverter(),
            null,
            filtroNumerico
        );
    }
    
    /**
    * Inizializza il controller dopo il caricamento dell'interfaccia FXML.
    * @param location  il percorso URL della risorsa FXML
    * @param resources il bundle di risorse per l'internazionalizzazione
    */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //risoluzione forme che escono dal pane

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(lavagna.widthProperty());
        clip.heightProperty().bind(lavagna.heightProperty());
        lavagna.setClip(clip);
        scrollPane.setPannable(false);

      
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
            
        
        // Applicazione del TextFormattere per i textField per contenere solo valori Double
        textFieldDimensione1.setTextFormatter(creaTextFormatterDouble());
        textFieldDimensione2.setTextFormatter(creaTextFormatterDouble());
        textFieldAngoloRotazione.setTextFormatter(creaTextFormatterDouble());
        
        this.modello = new Model();
        coloreRiempimentoCreazione.setValue(Color.WHITE);  
    }
    
    /**
    * Aggiorna lo stile visivo dell'icona dell'operazione selezionata.
    * @param operazioneSelezionata l'icona ImageView dell'operazione da aggiornare
    */
    private void aggiornaStileOperazioneForma(ImageView operazioneSelezionata){
        if (operazioneSelezionata.getEffect() == null) {
            // Se non ha effetto, lo aggiungo
            operazioneSelezionata.setEffect(new DropShadow(10, Color.BLUE));
        } else {
            // Se ha effetto, lo rimuovo
            operazioneSelezionata.setEffect(null);
        }

        // Rimuovo effetto da tutti gli altri
        if (operazioneSelezionata != salvaFoglio) {
            salvaFoglio.setEffect(null);
        }
        if (operazioneSelezionata != apriFoglio) {
            apriFoglio.setEffect(null);
        }
    }
    /**
    * Aggiorna lo stile visivo del bottone selezionato.
    * @param bottoneSelezionato il {@code ImageView} del bottone da aggiornare
    */
    private void aggiornaStile(ImageView bottoneSelezionato) {
        if (bottoneSelezionato.getEffect() == null) {
            // Se non ha effetto, lo aggiungo
            bottoneSelezionato.setEffect(new DropShadow(10, Color.BLUE));
        } else {
            // Se ha effetto, lo rimuovo
            bottoneSelezionato.setEffect(null);
        }

        // Rimuovo effetto da tutti gli altri
        if (bottoneSelezionato != lineaBottone) {
            lineaBottone.setEffect(null);
        }
        if (bottoneSelezionato != rettangoloBottone) {
            rettangoloBottone.setEffect(null);
        }
        if (bottoneSelezionato != ellisseBottone) {
            ellisseBottone.setEffect(null);
        }
    }

    /**
    * Gestore evento per il click sul bottone "Linea".
    * Se la forma da creare è già un segmento, la disattiva e abilita il selettore di colore riempimento.
    * Altrimenti, disabilita il selettore di colore riempimento, seleziona il bottone linea aggiornando lo stile e imposta
    * la forma da creare come segmento. 
    * @param event l'evento di tipo {@code MouseEvent} generato dal click sul bottone
    */
    @FXML
    private void gestoreLineaBottone(MouseEvent event) {
        apriToggleMenu(null, false);
        if (formaDaCreare != null && formaDaCreare == TipoForma.SEGMENTO) {
            formaDaCreare = null;
            //scrollPane.setPannable(true);
            aggiornaStile(lineaBottone);
            coloreRiempimentoCreazione.setDisable(false);
        }else{
            coloreRiempimentoCreazione.setDisable(true);
            //scrollPane.setPannable(false);
            aggiornaStile(lineaBottone);
            formaDaCreare = TipoForma.SEGMENTO;
        }
    }
    
    /**
    * Gestore evento per il click sul bottone "Rettangolo".
    * Se la forma da creare è già un rettangolo, la disattiva.
    * Altrimenti, seleziona il bottone rettangolo aggiornando lo stile e imposta
    * la forma da creare come rettangolo. 
    * @param event l'evento di tipo {@code MouseEvent} generato dal click sul bottone
    */
    @FXML
    private void gestoreRettangoloBottone(MouseEvent event) {
        apriToggleMenu(null, false);
        if (formaDaCreare != null && formaDaCreare == TipoForma.RETTANGOLO) {
            formaDaCreare = null;
            //scrollPane.setPannable(true);
            aggiornaStile(rettangoloBottone);
        }else{
            coloreRiempimentoCreazione.setDisable(false);
            //scrollPane.setPannable(false);
            aggiornaStile(rettangoloBottone);
            formaDaCreare = TipoForma.RETTANGOLO;
        }
    }
    
    /**
    * Gestore evento per il click sul bottone "Ellisse".
    * Se la forma da creare è già un ellisse, la disattiva.
    * Altrimenti, seleziona il bottone ellisse aggiornando lo stile e imposta
    * la forma da creare come ellisse. 
    * @param event l'evento di tipo {@code MouseEvent} generato dal click sul bottone
    */
    @FXML
    private void gestoreEllisseBottone(MouseEvent event) {
        apriToggleMenu(null, false);
        if (formaDaCreare != null && formaDaCreare == TipoForma.ELLISSE) {
            formaDaCreare = null;
            //scrollPane.setPannable(true);
            aggiornaStile(ellisseBottone);
        }else{
            coloreRiempimentoCreazione.setDisable(false);
            //scrollPane.setPannable(false);
            aggiornaStile(ellisseBottone);
            formaDaCreare = TipoForma.ELLISSE;
        }
    }
    
    private void menuAvanti(){
        pannelloModifica.toFront();
    }
    
    private Point2D calcolaPosizioneMenu(double mouseX, double mouseY, double menuWidth, double menuHeight) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        double posX = (mouseX + menuWidth > bounds.getMaxX()) ? mouseX - menuWidth : mouseX;
        double posY = (mouseY + menuHeight > bounds.getMaxY()) ? mouseY - menuHeight : mouseY;

        return new Point2D(posX, posY);
    }
    
    
    private void apriToggleMenu(MouseEvent event, boolean toggle) {
        if (!toggle) {
            pannelloModifica.setVisible(false);
            return;
        }
        pannelloModifica.setMaxWidth(Region.USE_PREF_SIZE);
        pannelloModifica.setMaxHeight(Region.USE_PREF_SIZE);
        

        // Calcola coordinate del mouse rispetto allo schermo
        double mouseX = event.getScreenX();
        double mouseY = event.getScreenY();

        double menuWidth = pannelloModifica.getWidth();
        double menuHeight = pannelloModifica.getHeight();

        // Calcola posizione ottimale sullo schermo
        Point2D posizioneSchermo = calcolaPosizioneMenu(mouseX, mouseY, menuWidth, menuHeight);

        // Converti la posizione da coordinate schermo a coordinate locali del pane principale
        Point2D posizioneNelPane = scrollPane.screenToLocal(posizioneSchermo);

        // Posiziona e mostra il menu
        pannelloModifica.setLayoutX(posizioneNelPane.getX());
        pannelloModifica.setLayoutY(posizioneNelPane.getY());

        pannelloModifica.setVisible(true);
        menuAvanti();
    }

    /**
    * Aggiorna i selettori di colore nel menu contestuale
    * in base ai colori correnti della forma selezionata.
    */
    private void aggiornaColoriNelContextMenu() {
        modificaColoreBordo.setValue((Color) formaSelezionata.getStroke());
        modificaColoreRiempimento.setValue((Color) formaSelezionata.getFill());
    }
    
    /**
    * Aggiorna il colore di bordo della forma selezionata
    * in base al colore selezionato dal color picker 
    */
    @FXML
    private void modificaColoreBordoEseguita() {
        Color selectedColor = modificaColoreBordo.getValue();
        this.formaSelezionata.setStroke(selectedColor); 
    }
    
    /**
    * Aggiorna il colore di riempimento della forma selezionata
    * in base al colore selezionato dal color picker 
    */
    @FXML
    private void modificaRiempimentoEseguita() {
        Color selectedColor = modificaColoreRiempimento.getValue();
        this.formaSelezionata.setFill(selectedColor); 
    }
    
    

    /**
    * Configura le interazioni di una forma grafica {@code Shape}.
    * Abilita il trascinamento della forma e imposta il menu contestuale
    * che si attiva con il click del tasto destro del mouse.
    * Quando il menu contestuale viene richiesto, 
    * aggiorna i colori dei selettori con quelli correnti della forma
    * e le dimensioni nel menu e mostra il menu nella posizione del cursore. 
    * @param forma la {@code Shape} su cui configurare le interazioni
    */
    private void configuraInterazioneForma(Shape forma) {
        // Per il trascinamento
        abilitaTrascinamento(forma);
        
        // Abilito la visualizzazione del menu contestuale
        // E imposto i parametri negessari
        // Al click sulla figura con tasto destro
        forma.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                formaSelezionata = forma;
                
                aggiornaColoriNelContextMenu();
                aggiornaDimensioniNelContextMenu();
                textFieldAngoloRotazione.setText(Double.toString(forma.getRotate()));
                
                apriToggleMenu(event, true);
            }
            event.consume();
        });

    }
    
    
    /**
    * Aggiorna i campi del menu contestuale relativi alle dimensioni
    * in base al tipo e ai valori della {@code formaSelezionata}.
    * Per forme bidimensionali, mostra entrambi i campi di dimensione e li popola
    * con i nomi e i valori restituiti dai metodi dell'interfaccia {@code FormaBidimensionale}.
    * Per forme monodimensionali, mostra un solo campo e nasconde il secondo.
    * Inoltre, aggiorna sempre il campo relativo all'angolo di rotazione della forma.
    */
    private void aggiornaDimensioniNelContextMenu() {
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

            textFieldDimensione2.setVisible(true);
            etichettaDimensione2.setVisible(true);
            //modificaColoreRiempimento.setVisible(true);

        } else if (formaSelezionata instanceof FormaMonodimensionale) {
            FormaMonodimensionale fm = (FormaMonodimensionale) formaSelezionata;
            etichettaDimensione1.setText(fm.ottieniNomeDimensione());
            textFieldDimensione1.setText(Double.toString(fm.ottieniValoreDimensione()));

            //modificaColoreRiempimento.setVisible(false);
            textFieldDimensione2.setVisible(false);
            etichettaDimensione2.setVisible(false);
        }
    }
    
    /**
    * Abilita il trascinamento della forma {@code Shape} sulla scena.
    * Imposta i gestori degli eventi per il mouse per consentire il drag & drop
    * della forma. Durante il trascinamento, viene aggiornata la posizione tramite
    * il metodo {@code trascina} definito nell'interfaccia {@code FormaPersonalizzabile}.
    * Viene inoltre modificato il cursore per indicare la possibilità di interazione.
    * @param forma la forma su cui abilitare il trascinamento
    */
    private void abilitaTrascinamento(Shape forma) {
        forma.setOnMousePressed(event -> {
            apriToggleMenu(null, false);
            this.startX = event.getSceneX();
            this.startY = event.getSceneY();
            forma.setCache(true);
            event.consume(); 
        });
        
        forma.setOnMouseDragged(event -> {
            double dx = event.getSceneX() - startX;
            double dy = event.getSceneY() - startY;

            ((FormaPersonalizzabile) forma).trascina(dx, dy);
            startX = event.getSceneX();
            startY = event.getSceneY();

            event.consume();
        });


        forma.setOnMouseReleased(event -> {
        // Azioni alla fine del trascinamento, calcolo di quanto si è spostato con startX e startY
        forma.setCache(false);
        event.consume();
        });
        
        forma.setOnMouseEntered(e -> forma.setCursor(javafx.scene.Cursor.HAND));
        forma.setOnMouseExited(e -> forma.setCursor(javafx.scene.Cursor.DEFAULT));
    }
    
    
   /**
    * Gestore dell'evento di click del mouse sulla lavagna per iniziare il disegno di una forma.
    * Quando l'utente clicca con il tasto sinistro, viene creata una nuova forma con
    * le coordinate del click, i colori selezionati e le dimensioni di default.
    * La forma viene poi configurata con le interazioni e aggiunta alla lavagna.
    * @param event l'evento {@code MouseEvent} generato dal click dell'utente
    */
    @FXML
    private void iniziaDisegno(MouseEvent event) {
        /*
        if (event.getButton() == MouseButton.SECONDARY && event.getTarget() == lavagna) {
            apriToggleMenu(event, true);
            return;
        }
        */

        // Tasto sinistro → nasconde pannello se non stai creando una forma
        if (event.getButton() == MouseButton.PRIMARY && event.getTarget() == lavagna && formaDaCreare == null && pannelloModifica.isVisible()) {
            apriToggleMenu(null, false);
            return;
        }
        if (event.getButton() == MouseButton.PRIMARY && event.getTarget() == lavagna && formaDaCreare != null && pannelloModifica.isVisible()) {
            apriToggleMenu(null, false);
            return;
        }
        

        // Se non è clic primario, pannello visibile o non si deve creare niente, esci
        if (event.getButton() != MouseButton.PRIMARY || scrollPane.isPannable() || formaDaCreare == null || pannelloModifica.isVisible()) {
            return;
        }
        

        // Ottengo le coordinate di dove è stato effettuato il click per poter generare la forma
        double inizioX = event.getX();
        double inizioY = event.getY();
        
        // Delego la responsabilità di creazione al modello, fornisco coordinate e colori
        // Le dimensioni sono di default
        Shape forma = modello.creaForma(
                formaDaCreare, 
                inizioX, 
                inizioY, 
                coloreBordoCreazione.getValue(), 
                coloreRiempimentoCreazione.getValue()
        );

        // E' necessario fornire alla forma la funzione di 
        // Far comparire il contextMenu, con le sue informazioni caricate,
        // Nel momento di un click con il tasto destro del mouse sulla specifica forma
        configuraInterazioneForma(forma);
       
        // Terminata la creazione, la forma è aggiunta nella lavagna
        lavagna.getChildren().add(forma);
    }  

    @FXML
    private void gestoreApplicaModifiche(ActionEvent event) {
        if (formaSelezionata == null) return;
        
        try {
            formaSelezionata.setRotate(Double.parseDouble(textFieldAngoloRotazione.getText()));

            if (formaSelezionata instanceof FormaBidimensionale) {
                double dim1 = Double.parseDouble(textFieldDimensione1.getText());
                double dim2 = Double.parseDouble(textFieldDimensione2.getText());
                ((FormaBidimensionale) formaSelezionata).modificaDimensioni(dim1, dim2);
            } else if (formaSelezionata instanceof FormaMonodimensionale) {
                double dim = Double.parseDouble(textFieldDimensione1.getText());
                ((FormaMonodimensionale) formaSelezionata).modificaDimensione(dim);
            }
            

        } catch (NumberFormatException e) {
            // Mostrare alert o loggare errore
        }
    }

    @FXML
    private void gestoreCopiaForma() {
        if (formaSelezionata != null) {
            formaCopiata = (FormaPersonalizzabile)formaSelezionata;
            apriToggleMenu(null, false);
        }
    }

    @FXML
    private void gestoreIncollaForma() {
        if (formaCopiata != null) {
            // eseguo una vera copia della formaCopiata
            // devo eseguire sempre copie diverse tra loro quando incollo
            // se eseguissi una singola copia nei metodi copia e taglia 
            // avrei un errore quando provo ad aggiungere alla lavagna perchè
            // sarebbe sempre lo stesso Node
            Shape copia = formaCopiata.clonaForma(); 

            // alla copia devo assegnare le coordinate di dove è avvenuto l'evento
            //copia.setX
            configuraInterazioneForma(copia);
            lavagna.getChildren().add(copia);
            //contextMenu.hide();
            apriToggleMenu(null, false);
        }
    }

    @FXML
    private void gestoreTagliaForma() {
        if (formaSelezionata != null) {
            // seleziono questa forma come formaCopiata
            formaCopiata = (FormaPersonalizzabile)formaSelezionata;
                
            lavagna.getChildren().remove(formaSelezionata);
            formaSelezionata = null;
            apriToggleMenu(null, false);
            //contextMenu.hide();
        }
    }

    @FXML
    private void gestoreEliminaForma() {
        if (formaSelezionata != null) {
            lavagna.getChildren().remove(formaSelezionata);
            apriToggleMenu(null, false);
            formaSelezionata = null;
        }
    }

    @FXML
    private void gestoreSpostaAvanti(MouseEvent event) {
        if (formaSelezionata != null) {
            formaSelezionata.toFront();
            menuAvanti();
        }
    }

    @FXML
    private void gestoreSpostaDietro(MouseEvent event) {
        if (formaSelezionata != null) {
            formaSelezionata.toBack();
            menuAvanti();
        }
    }
    
    @FXML
    private void gestoreApriFoglio(MouseEvent event) {
        aggiornaStileOperazioneForma(apriFoglio);
        
        this.lavagna.getChildren().clear();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("documento.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length < 5) continue; // Salta righe malformate

                try {
                    TipoForma tipo = TipoForma.valueOf(parts[0]);
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    Color stroke = Color.web(parts[3]);
                    // la linea non ha questo colore
                    Color fill = Color.web(parts[4]);

                    Shape forma = modello.creaForma(tipo, x, y, stroke, fill);

                    if (forma instanceof FormaBidimensionale && parts.length >= 7) {
                        ((FormaBidimensionale) forma).modificaDimensioni(
                            Double.parseDouble(parts[5]), Double.parseDouble(parts[6]));
                    } else if (forma instanceof FormaMonodimensionale && parts.length >= 6) {
                        ((FormaMonodimensionale) forma).modificaDimensione(
                            Double.parseDouble(parts[5]));
                    }
                    
                    configuraInterazioneForma(forma);
                    lavagna.getChildren().add(forma);
                    
                } catch (NumberFormatException e) {
                    System.out.println("Errore nella riga: " + line);
                    e.printStackTrace(); // o alert all'utente
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // errore nel file
        }
    }
    
    
    @FXML
    private void gestoreSalvaFoglio(MouseEvent event) throws IOException {
        aggiornaStileOperazioneForma(salvaFoglio);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("documento.txt"))) {
            // getChildren() fornisce già l'ordine utile per la profondità
            for (Node node : lavagna.getChildren()) {
                if (node instanceof FormaPersonalizzabile) {
                    writer.write(((FormaPersonalizzabile) node).toText());
                }
            }
        } 
    }
}
