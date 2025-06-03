package com.gruppo07iz.geometrika;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.gruppo07iz.geometrika.forme.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Model {
    // E' solo per le forme regolari
    private FactoryFormaRegolare factory;

    /**
     * Costruttore della classe Model
     */
    public Model() {
        this.factory = new FactoryFormaRegolare();
    }

    /**
     * Metodo per la creazione di una forma regolare; per la creazione della forma
     * richiama il metodo crea froma del factory
     * 
     * @param tipo
     * @param x
     * @param y
     * @param bordo
     * @param riemp
     * @return
     */
    public FormaPersonalizzabile creaFormaRegolare(TipoFormaRegolare tipo, double x, double y, Color bordo,
            Color riemp) {
        FormaPersonalizzabile forma = factory.creaFormaRegolare(tipo, x, y, bordo, riemp);
        return forma;
    }

    /**
     * Metodo per la creazione di un poligono
     * 
     * @param punti
     * @param bordo
     * @param riemp
     * @return
     */
    public FormaPersonalizzabile creaPoligono(List<Double> punti, Color bordo, Color riemp) {
        return new Poligono(punti, bordo, riemp);
    }

    /**
     * Metodo per la creazione di un testo
     * 
     * @param testo
     * @param x
     * @param y
     * @param bordo
     * @param riemp
     * @param dimensione
     * @return
     */
    public FormaPersonalizzabile creaTesto(String testo, double x, double y, Color bordo, Color riemp,
            double dimensione) {
        return new Testo(testo, x, y, bordo, riemp, dimensione);
    }

    /**
     * Metodo per aggiungere una forma alla lavagna
     * 
     * @param lavagna
     * @param forma
     */
    public void aggiungiForma(Pane lavagna, FormaPersonalizzabile forma) {
        forma.disegna(lavagna);
    }

    /**
     * Metodo per aggiungere una forma alla lavagna in una specifica posizione
     * 
     * @param lavagna
     * @param forma
     * @param posizione
     */
    public void aggiungiForma(Pane lavagna, FormaPersonalizzabile forma, int posizione) {
        forma.disegna(lavagna, posizione);
    }

    /**
     * Metodo per l'eliminazione di una forma
     * 
     * @param padre
     * @param formaDaEliminare
     */
    public void eliminaForma(Pane padre, FormaPersonalizzabile formaDaEliminare) {
        formaDaEliminare.eliminaForma(padre);
    }

    /**
     * Metodo per la modifica delle dimensionsioni di una (@code
     * FormaBidimensionale)
     * 
     * @param forma
     * @param dimensione1
     * @param dimensione2
     */
    public void modificaDimensioni(FormaBidimensionale forma, double dimensione1, double dimensione2) {
        forma.modificaDimensioni(dimensione1, dimensione2);
    }

    /**
     * Metodo per la modifica della dimensionsione di una (@code
     * FormaMonodimensionale)
     * 
     * @param forma
     * @param dimensione
     */
    public void modificaDimensione(FormaMonodimensionale forma, double dimensione) {
        forma.modificaDimensione(dimensione);
    }

    /**
     * Metodo per la modifica del colore di bordo
     * 
     * @param forma
     * @param coloreBordo
     */
    public void modificaColoreBordo(FormaPersonalizzabile forma, Color coloreBordo) {
        forma.setColoreBordo(coloreBordo);
    }

    /**
     * Metodo per la modifica del colore di riempimento
     * 
     * @param forma
     * @param coloreRiempimento
     */
    public void modificaColoreRiempimento(FormaPersonalizzabile forma, Color coloreRiempimento) {
        forma.setColoreRiempimento(coloreRiempimento);
    }

    /**
     * Metodo per il trascinamento di una (@code FormaPersonalizzabile)
     * 
     * @param forma
     * @param dx
     * @param dy
     */
    public void trascinaForma(FormaPersonalizzabile forma, double dx, double dy) {
        forma.trascina(dx, dy);
    }

    /**
     * Sposta la forma in primo piano nel contenitore.
     *
     * @param forma la forma da spostare in primo piano
     */
    public void spostaAvanti(FormaPersonalizzabile forma) {
        forma.spostaDavanti();
    }

    /**
     * Sposta la forma in secondo piano nel contenitore.
     *
     * @param forma la forma da spostare in secondo piano
     */
    public void spostaIndietro(FormaPersonalizzabile forma) {
        forma.spostaIndietro();
    }

    /**
     * Riposiziona la forma alla posizione iniziale specificata, eliminandola e
     * ridisegnandola.
     *
     * @param padre             il contenitore padre (Pane) su cui disegnare la
     *                          forma
     * @param forma             la forma da riposizionare
     * @param posizioneIniziale la posizione z-index iniziale da assegnare alla
     *                          forma
     */
    public void spostaPosizioneIniziale(Pane padre, FormaPersonalizzabile forma, int posizioneIniziale) {
        forma.eliminaForma(padre);
        forma.disegna(padre, posizioneIniziale);
    }

    /**
     * Incolla una forma nel contenitore padre alle coordinate specificate.
     *
     * @param padre        il contenitore padre (Pane) su cui incollare la forma
     * @param formaCopiata la forma da incollare
     * @param coordinataX  la coordinata X in cui posizionare la forma
     * @param coordinataY  la coordinata Y in cui posizionare la forma
     */
    public void incollaForma(Pane padre, FormaPersonalizzabile formaCopiata, double coordinataX, double coordinataY) {
        formaCopiata.disegna(padre);
        formaCopiata.cambiaCoordinate(coordinataX, coordinataY);
    }

    public void incollaGruppo(Pane lavagna, Gruppo gruppo, double coordinataX, double coordinataY) {
    }

    /**
     * Crea un nuovo gruppo a partire da una lista di forme fornite come input.
     * 
     * Se la lista è nulla o vuota, il metodo restituisce {@code false}.
     * In caso contrario, viene istanziato un nuovo {@code Gruppo} e vi
     * vengono aggiunte tutte le forme della lista.
     *
     * Nota: il parametro {@code gruppoCreato} viene sovrascritto
     * all'interno del metodo, ma poiché Java è pass-by-value anche per i
     * riferimenti,
     * la modifica non ha effetto all'esterno. Per usare correttamente il risultato,
     * è consigliato restituire direttamente l'oggetto creato.
     *
     * @param listaForme   La lista di {@code FormaPersonalizzabile} da raggruppare.
     * @param gruppoCreato Parametro di uscita (non efficace): verrà sovrascritto
     *                     internamente.
     * @return {@code true} se il gruppo è stato creato con successo, {@code false}
     *         altrimenti.
     *
     * @see Gruppo
     */
    public boolean creaGruppo(List<FormaPersonalizzabile> listaForme, Gruppo gruppoCreato) {
        if (listaForme == null || listaForme.isEmpty())
            return false;

        gruppoCreato = new Gruppo();
        for (FormaPersonalizzabile forma : listaForme) {
            gruppoCreato.aggiungiForma(forma);
        }
        return true;
    }

    /**
     * Separa tutte le forme contenute in un {@code Gruppo}, restituendole come
     * lista
     * e svuotando il contenuto del gruppo originale.
     *
     * Questo metodo effettua la "separazione" logica delle forme da un gruppo:
     * le forme vengono estratte e restituite, mentre il gruppo viene svuotato.
     * 
     * Se il gruppo è {@code null} o vuoto, viene restituito {@code null}.
     *
     * @param listaForme Il gruppo da separare.
     * @return Una lista di {@code FormaPersonalizzabile} separate, oppure
     *         {@code null} se il gruppo era vuoto o nullo.
     *
     * @see Gruppo#getVettoreForme()
     */
    public boolean separaGruppo(List<FormaPersonalizzabile> listaForme, Gruppo gruppoSeparato) {
        if (listaForme == null || listaForme.isEmpty())
            return false;

        gruppoSeparato = new Gruppo();
        for (FormaPersonalizzabile forma : listaForme) {
            gruppoSeparato.aggiungiForma(forma);
        }

        // Smonta il gruppo rimuovendo i legami interni
        gruppoSeparato.getVettoreForme().clear();

        return true;
    }

    /**
     * Modifica l'angolo di rotazione della forma specificata.
     *
     * @param forma  la forma da ruotare
     * @param angolo l'angolo in gradi da assegnare alla forma
     */
    public void modificaAngolo(FormaPersonalizzabile forma, double angolo) {
        forma.setAngolo(angolo);
    }

    /**
     * Applica un'operazione di mirroring orizzontale alla forma specificata.
     * La trasformazione viene eseguita rispetto al centro geometrico della forma,
     * calcolato tramite il metodo {@code calcolaCentro()} di
     * {@code FormaPersonalizzabile}.
     *
     * @param forma la forma su cui applicare il mirroring orizzontale; deve essere
     *              un'istanza di {@code FormaPersonalizzabile}
     */
    public void mirroringOrizzontale(FormaPersonalizzabile forma) {
        forma.setSpecchiaturaOrizzontale();
    }

    /**
     * Applica un'operazione di mirroring verticale alla forma specificata.
     * La trasformazione viene eseguita rispetto al centro geometrico della forma,
     * calcolato tramite il metodo {@code calcolaCentro()} di
     * {@code FormaPersonalizzabile}.
     *
     * @param forma la forma su cui applicare il mirroring verticale; deve essere
     *              un'istanza di {@code FormaPersonalizzabile}
     */
    public void mirroringVerticale(FormaPersonalizzabile forma) {
        forma.setSpecchiaturaVerticale();
    }

        /**
     * Salva le forme presenti nella lavagna in un file di testo.
     * Ogni forma viene salvata su una riga con le sue proprietà, evitando
     * duplicazioni per le forme appartenenti a gruppi.
     *
     * @param file             il file in cui salvare le forme
     * @param lavagna          il pannello contenente le forme da salvare
     * @param mappaFormeGruppi mappa che associa le forme ai rispettivi gruppi
     * @throws IOException se si verifica un errore durante la scrittura del file
     */
    public void salvaFoglio(File file, Pane lavagna, Map<FormaPersonalizzabile, Gruppo> mappaFormeGruppi)
            throws IOException {
        System.out.println("Salvataggio del file: " + file.getAbsolutePath());

        Set<FormaPersonalizzabile> giaSalvate = new HashSet<>();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Node node : lavagna.getChildren()) {
                if (!(node instanceof FormaPersonalizzabile))
                    continue;

                FormaPersonalizzabile forma = (FormaPersonalizzabile) node;
                FormaPersonalizzabile radice = forma;

                // Se la forma appartiene a un gruppo, usiamo il gruppo come riferimento
                while (mappaFormeGruppi.containsKey(radice)) {
                    radice = mappaFormeGruppi.get(radice);
                }

                if (!giaSalvate.contains(radice)) {
                    writer.write(radice.toText());
                    giaSalvate.add(radice);
                }
            }
        }
    }

    public void caricaFoglio(File file, Pane lavagna, Map<FormaPersonalizzabile, Gruppo> mappaFormeGruppi) {
        System.out.println("Caricamento del file: " + file.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String tipo = parts[0];

                if (tipo.equals("GRUPPO")) {
                    System.out.println("Caricamento di un gruppo...");
                    caricaGruppoDaReader(reader, lavagna, mappaFormeGruppi, line);
                } else {
                    switch (tipo) {
                        case "POLIGONO":
                            caricaPoligono(parts, lavagna);
                            break;
                        case "TESTO":
                            caricaTesto(parts, lavagna);
                            break;
                        default:
                            try {
                                caricaFormaRegolare(parts, lavagna);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Errore nella riga: " + line);
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }


    /**
     * Carica un gruppo di forme da un file di testo.
     * @param reader
     * @param lavagna
     * @param mappaFormeGruppi
     * @param primaLinea
     * @return
     * @throws IOException
     */
    private Gruppo caricaGruppoDaReader(BufferedReader reader, Pane lavagna, Map<FormaPersonalizzabile, Gruppo> mappaFormeGruppi, String primaLinea) throws IOException {
        String[] parts = primaLinea.split(" ");
        double angoloGruppo = Double.parseDouble(parts[1]);
        double lunghezzaGruppo = Double.parseDouble(parts[2]);
        double altezzaGruppo = Double.parseDouble(parts[3]);

        List<FormaPersonalizzabile> listaForme = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null && !line.equals("FINEGRUPPO")) {
            parts = line.split(" ");
            String tipo = parts[0];

            switch (tipo) {
                case "GRUPPO":
                    // Chiamata ricorsiva per caricare i sottogruppi
                    Gruppo sottogruppo = caricaGruppoDaReader(reader, lavagna, mappaFormeGruppi, line); 
                    listaForme.add(sottogruppo);
                    break;
                case "POLIGONO":
                    listaForme.add(caricaPoligono(parts, lavagna));
                    break;
                case "TESTO":
                    listaForme.add(caricaTesto(parts, lavagna));
                    break;
                default:
                    try {
                        listaForme.add(caricaFormaRegolare(parts, lavagna));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Errore nella riga: " + line);
                        e.printStackTrace();
                    }
                    break;
            }
        }

        return aggiungiGruppo(listaForme, mappaFormeGruppi, angoloGruppo, lunghezzaGruppo, altezzaGruppo);
    }


    /**
     * Carica un gruppo di forme dalla lista e aggiorna la mappa delle forme ai
     * gruppi.
     * Ogni forma della lista viene associata al nuovo gruppo creato.
     *
     * @param listaForme       lista di forme da raggruppare
     * @param mappaFormeGruppi mappa che associa le forme ai rispettivi gruppi
     */
    private Gruppo aggiungiGruppo(List<FormaPersonalizzabile> listaForme,
            Map<FormaPersonalizzabile, Gruppo> mappaFormeGruppi, double angoloGruppo, double lunghezzaGruppo,
            double altezzaGruppo) {
        if (listaForme == null || listaForme.isEmpty()) return null;

        Gruppo gruppo = new Gruppo(angoloGruppo, lunghezzaGruppo, altezzaGruppo);
        for (FormaPersonalizzabile forma : listaForme) {
            gruppo.aggiungiForma(forma);
        }

        for (FormaPersonalizzabile f : gruppo.getVettoreForme()) {
            mappaFormeGruppi.put(f, gruppo);
        }
        return gruppo;
    }

    /**
     * Crea un oggetto Poligono a partire da una riga del file.
     *
     * @param parts   array contenente i parametri del poligono
     * @param lavagna pannello su cui disegnare
     * @return il poligono creato
     */
    private FormaPersonalizzabile caricaPoligono(String[] parts, Pane lavagna) {
        FormaPersonalizzabile poligono = ritornaPoligono(parts);
        if (poligono == null) {
            System.out.println("Errore nel caricamento del poligono.");
            return null;
        }
        lavagna.getChildren().add((Node) poligono);
        return poligono;
    }

    /**
     * Crea un oggetto Testo a partire da una riga del file.
     * La riga deve contenere il testo e i parametri necessari per creare l'oggetto
     * Testo.
     *
     * @param parts   array contenente i parametri del testo
     * @param lavagna pannello su cui disegnare
     * @return il testo creato
     */
    private FormaPersonalizzabile caricaTesto(String[] parts, Pane lavagna) {
        FormaPersonalizzabile testo = ritornaTesto(parts);
        if (testo != null) {
            lavagna.getChildren().add((Node) testo);
            return testo;
        } else {
            System.out.println("Errore nel caricamento del testo.");
            return null;
        }
    }

    /**
     * Crea una forma regolare a partire da una riga del file.
     *
     * @param parts   array contenente i parametri della forma
     * @param lavagna pannello su cui disegnare
     * @return la forma regolare creata
     */
    private FormaPersonalizzabile caricaFormaRegolare(String[] parts, Pane lavagna) {
        FormaPersonalizzabile forma = ritornaFormaRegolare(parts);
        if (forma == null) {
            System.out.println("Errore nel caricamento della forma regolare.");
            return null;
        } else {
            lavagna.getChildren().add((Node) forma);
            return forma;
        }
    }

    /**ESPORTAZIONE E IMPORTAZIONE FORME REGOLARI*/ 

    /**
     * Esporta le forme personalizzate in un file di testo.
     *
     * @param file             il file in cui esportare le forme
     * @param formePersonalizzate mappa contenente le forme da esportare
     * @throws IOException se si verifica un errore durante la scrittura del file
     */
    public void esportaFormePersonalizzate(File file, Map<String, FormaPersonalizzabile> formePersonalizzate) throws IOException {
        System.out.println("Esportazione delle forme personalizzate in: " + file.getAbsolutePath());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, FormaPersonalizzabile> entry : formePersonalizzate.entrySet()) {
                String nome = entry.getKey();
                FormaPersonalizzabile forma = entry.getValue();

                writer.write(nome);
                writer.newLine();
                writer.write(forma.toText());
            }
        }
    }

    /**
     * Importa forme personalizzate da un file di testo.
     *
     * @param file             il file da cui importare le forme
     * @param formePersonalizzate mappa in cui salvare le forme importate
     * @param mappaFormeGruppi mappa che associa le forme ai rispettivi gruppi
     * @throws IOException se si verifica un errore durante la lettura del file
     */
    public void importaFormePersonalizzate(File file, Map<String, FormaPersonalizzabile> formePersonalizzate,
            Map<FormaPersonalizzabile, Gruppo> mappaFormeGruppi) throws IOException {

        System.out.println("Importazione delle forme personalizzate da: " + file.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String nomeForma;
            while ((nomeForma = reader.readLine()) != null) {
                String lineaDescrizione = reader.readLine();
                if (lineaDescrizione == null) break;

                String[] parts = lineaDescrizione.split(" ");
                String tipo = parts[0];

                if (tipo.equals("GRUPPO")) {
                    Gruppo gruppo = importaGruppoDaReader(reader, mappaFormeGruppi, lineaDescrizione);
                    if (!formePersonalizzate.containsKey(nomeForma)) {
                        formePersonalizzate.put(nomeForma, gruppo);
                    }
                } else {
                    if (!formePersonalizzate.containsKey(nomeForma)) {
                        switch (tipo) {
                            case "POLIGONO":
                                formePersonalizzate.put(nomeForma, ritornaPoligono(parts));
                                break;
                            case "TESTO":
                                formePersonalizzate.put(nomeForma, ritornaTesto(parts));
                                break;
                            default:
                                try {
                                    formePersonalizzate.put(nomeForma, ritornaFormaRegolare(parts));
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Errore nella riga: " + lineaDescrizione);
                                    e.printStackTrace();
                                }
                                break;
                        }
                    } else {
                        System.out.println("La forma " + nomeForma + " è già presente, salto l'importazione.");
                    }
                }
            }
        }
    }


    /**
     * Ricostruisce un oggetto {@code Poligono} a partire da una riga del file.
     * La riga deve contenere i parametri necessari per creare l'oggetto Poligono.
     *
     * @param parts array contenente i parametri del poligono
     * @return il poligono creato, oppure {@code null} in caso di errore
     */
    private Gruppo importaGruppoDaReader(BufferedReader reader, Map<FormaPersonalizzabile, Gruppo> mappaFormeGruppi, String primaLinea) throws IOException {
        String[] parts = primaLinea.split(" ");
        double angoloGruppo = Double.parseDouble(parts[1]);
        double lunghezzaGruppo = Double.parseDouble(parts[2]);
        double altezzaGruppo = Double.parseDouble(parts[3]);

        List<FormaPersonalizzabile> listaForme = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null && !line.equals("FINEGRUPPO")) {
            parts = line.split(" ");
            String tipo = parts[0];

            if (tipo.equals("GRUPPO")) {

                // Chiamata ricorsiva per caricare i sottogruppi
                Gruppo sottogruppo = importaGruppoDaReader(reader, mappaFormeGruppi, line);
                listaForme.add(sottogruppo);
            } else {
                switch (tipo) {
                    case "POLIGONO":
                        listaForme.add(ritornaPoligono(parts));
                        break;
                    case "TESTO":
                        listaForme.add(ritornaTesto(parts));
                        break;
                    default:
                        try {
                            listaForme.add(ritornaFormaRegolare(parts));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Errore nella riga: " + line);
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
        return ritornaGruppo(listaForme, mappaFormeGruppi, angoloGruppo, lunghezzaGruppo, altezzaGruppo);
    }

        /**
     * Crea un nuovo gruppo a partire da una lista di forme e aggiorna la mappa delle
     * forme ai gruppi.
     *
     * @param listaForme       lista di forme da raggruppare
     * @param mappaFormeGruppi mappa che associa le forme ai rispettivi gruppi
     * @return il gruppo creato
     */
    private Gruppo ritornaGruppo(List<FormaPersonalizzabile> listaForme,
            Map<FormaPersonalizzabile, Gruppo> mappaFormeGruppi, double angoloGruppo, double lunghezzaGruppo,
            double altezzaGruppo) {
        Gruppo gruppo = new Gruppo(angoloGruppo, lunghezzaGruppo, altezzaGruppo);
        for (FormaPersonalizzabile forma : listaForme) {
            gruppo.aggiungiForma(forma);
            mappaFormeGruppi.put(forma, gruppo);
        }
        return gruppo;
    }

    /**
     * Ricostruisce un oggetto {@code Poligono} a partire da un array di stringhe
     * @param parts
     * @return
     */
    private FormaPersonalizzabile ritornaPoligono(String[] parts) {
        try {
            int numValori = Integer.parseInt(parts[1]);
            List<Double> punti = new ArrayList<>();

            // Legge le coordinate del poligono
            for (int i = 0; i < numValori; i++) {
                punti.add(Double.parseDouble(parts[2 + i]));
            }

            int idx = 2 + numValori;
            Color stroke = Color.web(parts[idx]);
            Color fill = Color.web(parts[idx + 1]);
            double angolo = Double.parseDouble(parts[idx + 2]);
            double dimensioneX = Double.parseDouble(parts[idx + 3]);
            double dimensioneY = Double.parseDouble(parts[idx + 4]);
            double specchiaturaX = Double.parseDouble(parts[idx + 5]);
            double specchiaturaY = Double.parseDouble(parts[idx + 6]);

            Poligono poligono = new Poligono(punti, fill, stroke);
            modificaAngolo(poligono, angolo);
            if (specchiaturaX == 1) {
                poligono.setSpecchiaturaOrizzontale();
            }
            if (specchiaturaY == 1) {
                poligono.setSpecchiaturaVerticale();
            }
            ((Shape) poligono).setScaleX(dimensioneX);
            ((Shape) poligono).setScaleY(dimensioneY);
            System.out.println("Poligono caricato con " + (numValori / 2) + " punti.");
            return poligono;

        } catch (Exception e) {
            System.out.println("Errore nel caricamento del poligono.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Ricostruisce un oggetto {@code Testo} a partire da un array di stringhe
     * @param parts
     * @return
     */
    private FormaPersonalizzabile ritornaTesto(String[] parts) {
        try {
            int numeroParole = Integer.parseInt(parts[1]);
            System.out.println("Numero di parole nel testo: " + numeroParole);

            // Ricostruisci il testo dal numero di parole
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < numeroParole; i++) {
                System.out.println("Parte " + i + ": " + parts[i]);
                sb.append(parts[2 + i]);
                if (i < numeroParole - 1)
                    sb.append(" ");
            }
            String stringaTesto = sb.toString();
            System.out.println("Caricamento del testo: " + stringaTesto);

            int inizioParametri = 2 + numeroParole;

            double x = Double.parseDouble(parts[inizioParametri]);
            double y = Double.parseDouble(parts[inizioParametri + 1]);
            Color bordo = Color.web(parts[inizioParametri + 2]);
            Color riempimento = Color.web(parts[inizioParametri + 3]);
            double fontSize = Double.parseDouble(parts[inizioParametri + 4]);
            double dimensioneX = Double.parseDouble(parts[inizioParametri + 5]);
            double dimensioneY = Double.parseDouble(parts[inizioParametri + 6]);
            double specchiaturaX = Double.parseDouble(parts[inizioParametri + 7]);
            double specchiaturaY = Double.parseDouble(parts[inizioParametri + 8]);
            double angolo = Double.parseDouble(parts[inizioParametri + 9]);

            Testo testo = new Testo(stringaTesto, x, y, bordo, riempimento, fontSize);
            modificaAngolo(testo, angolo);
            if (specchiaturaX == 1) {
                testo.setSpecchiaturaOrizzontale();
            }
            if (specchiaturaY == 1) {
                testo.setSpecchiaturaVerticale();
            }
            ((Shape) testo).setScaleX(dimensioneX);
            ((Shape) testo).setScaleY(dimensioneY);

            System.out.println(testo.toText());
            return testo;

        } catch (NumberFormatException e) {
            System.out.println("Errore di formato nei dati numerici del testo.");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.out.println("Errore generico nel caricamento del testo.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Ricostruisce un oggetto {@code FormaPersonalizzabile} a partire da un array di stringhe
     * @param parts
     * @return
     */
    private FormaPersonalizzabile ritornaFormaRegolare(String[] parts) {
        try {
            TipoFormaRegolare tipo = TipoFormaRegolare.valueOf(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            Color stroke = Color.web(parts[3]);
            Color fill = Color.web(parts[4]);

            FormaPersonalizzabile forma = creaFormaRegolare(tipo, x, y, stroke, fill);

            if (forma instanceof FormaBidimensionale && parts.length >= 8) {
                ((FormaBidimensionale) forma).modificaDimensioni(
                        Double.parseDouble(parts[5]),
                        Double.parseDouble(parts[6]));
                modificaAngolo(forma, Double.parseDouble(parts[7]));

            } else if (forma instanceof FormaMonodimensionale && parts.length >= 7) {
                ((FormaMonodimensionale) forma).modificaDimensione(Double.parseDouble(parts[5]));
                modificaAngolo(forma, Double.parseDouble(parts[6]));
            }

            System.out.printf("Caricata forma %s a (%.2f, %.2f) con colori: bordo=%s, riempimento=%s%n",
                    tipo, x, y, stroke, fill);
            System.out.println(forma.toText());
            return forma;

        } catch (Exception e) {
            System.out.println("Errore nel caricamento della forma regolare.");
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

}