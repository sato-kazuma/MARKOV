package text;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static More.HelperMethod.attendiInput;
import static More.HelperMethod.searchFolder;
import static textlogic.RichiestaParamText.*;
import static textlogic.StampeOnText.*;


public class MarkovOnText { /*Variabile globale chiamata una sola volta, settata con il PATH che vogliamo*/
    private static String path;
    private static final String directoryBook= searchFolder("book");
    private static String fileNoExtension;
    private static int keysize;
    private static int outputsize;
    private static final Random random = new Random();
    private static int wordsGenerated = 0;
    private static int wordsCut = 0;


    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Premi invio per cominciare la simulazione!");
        attendiInput();
        simulation();
        System.out.println(markov(keysize, outputsize));
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println("Premi invio per terminare la simulazione!");
        attendiInput();
    }

    /**
     * Questo metodo gestisce la simulazione iniziale, raccoglie i parametri
     * necessari e avvia il processo di generazione del testo.
     */
    private static void simulation() throws InterruptedException, IOException, IllegalArgumentException {
        cornice();
        System.out.println("Benvenuti alla mia implementazione di una Markov Chain basata su un testo!\n");
        fileNoExtension = askFile(directoryBook);
        Thread.sleep(1000);
        setKeysize(askKey());
        Thread.sleep(1000);
        setOutputsize(askOutput());
        Thread.sleep(1000);
        System.out.println("Ora ho la chiave di grandezza: " + keysize + " e la grandezza dell'output finale di : " + outputsize + "\n");
        cornice();
        System.out.println("Inizio la nuova generazione...attendere prego...\n");
        waiting();
        Thread.sleep(1000);
        waiting();
        Thread.sleep(1000);
        waiting();
        System.out.println("ECCO A VOI IL NUOVO CAPITOLO DI " + fileNoExtension + "\n");
        cicleSimulation();
    }


    /**
     * Questo metodo genera il testo utilizzando il dizionario di parole creato
     * e le dimensioni specificate per la chiave e l'output.
     */
    public static String markov(int keySize, int outputSize) throws IOException {
        List<String> words = Arrays.asList(new String(Files.readAllBytes(Paths.get(searchFolder("book")+"\\"+fileNoExtension+".txt"))).trim().split("\\s+"));
        Map<List<String>, List<String>> dict = buildMarkovDict(keySize, words);
        List<String> output = generateInitialOutput(words, keySize);
        StringBuilder result = generateText(dict, output, outputSize, keySize);
        handleEndOfSentence(result);
        printAdditionalInfo(outputSize);
        return result.toString().trim();
    }


    /**
     * Questo metodo costruisce un dizionario per la catena di Markov
     * a partire da una lista di parole e dalla dimensione della chiave.
     */
    private static Map<List<String>, List<String>> buildMarkovDict(int keySize, List<String> words) {
        /*
        Inizializzazione del dizionario:
        Crea un LinkedHashMap chiamato dict che mantiene l'ordine d'inserimento delle chiavi.
        Utilizza List<String> come chiave e List<String> come valore nel dizionario.
       */
        Map<List<String>, List<String>> dict = new LinkedHashMap<>();
        /*
        Iterazione attraverso le parole:
        Attraverso un ciclo for, analizza la lista delle parole fornita come input, considerando ciascuna sequenza di parole di lunghezza keySize
        come chiave nel dizionario.
       */
        for (int i = 0; i < words.size() - keySize; i++) {
            /*
            Costruzione delle chiavi:
            Per ogni iterazione del ciclo, crea una chiave List<String> chiamata key utilizzando il metodo subList sulla lista delle parole words.
            Questo estrae una sottolista di parole di lunghezza keySize a partire dall'indice i.
            */
            List<String> key = words.subList(i, i + keySize);
            /*
            Aggiunta dei valori al dizionario: La variabile value viene inizializzata con la parola successiva alla sequenza corrente di parole (se disponibile).
            Quindi, utilizzando computeIfAbsent, aggiunge value alla lista dei valori associati alla chiave key nel dizionario.
            Se la chiave non è presente, crea una nuova lista di valori per quella chiave e aggiunge il valore a essa.
            */
            String value = (i + keySize < words.size()) ? words.get(i + keySize) : "";
            dict.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            /*
            Il motivo per cui la variabile
            k non viene utilizzata è che, in questo caso,
            la funzione lambda fornita a computeIfAbsent non ha bisogno di usare il valore della chiave.
            La funzione lambda viene chiamata solo se la chiave key non è presente nel dizionario, e la variabile k rappresenta
            la chiave che stiamo cercando di aggiungere.
            Quindi, il valore di k non è necessario per la creazione della lista
            (in questo caso si crea semplicemente una nuova ArrayList<>()), e può essere ignorato.
            La funzione lambda è utilizzata solo per creare e restituire una nuova lista se non esiste già una lista per quella chiave nel dizionario.
            */
        }
            /*
            Restituzione del dizionario:
            Una volta completato il ciclo attraverso tutte le parole nel corpus, restituisce il dizionario dict completo, che ora contiene tutte le sequenze
            di parole e le parole seguenti che si verificano nel testo originale.
            */
        return dict;
    }


    /**
     * Questo metodo genera una sequenza iniziale di parole da utilizzare
     * come punto di partenza per la generazione del testo.
     */
    private static List<String> generateInitialOutput(List<String> words, int keySize) {
       /*
       Selezione dell'indice iniziale:
       random.nextInt(words.size() - keySize) genera un numero casuale che rappresenta l'indice di partenza per la sequenza di parole.
       words.size() - keySize assicura che l'indice casuale generato non superi la lunghezza totale delle parole meno la dimensione della chiave,
       garantendo che ci sia spazio sufficiente per estrarre una sequenza completa di lunghezza keySize.
       */
        int startIndex = random.nextInt(words.size() - keySize);
        /*
        Estrazione della sequenza di parole: Utilizzando words.subList(startIndex, startIndex + keySize),
        ottieni una sotto-lista di parole a partire dall'indice generato casualmente startIndex fino all'indice startIndex + keySize.
        Questo rappresenta la sequenza iniziale di parole che verrà utilizzata come punto di partenza per la generazione del testo basato sulla catena di Markov.
        Creazione di una nuova lista:
        La sotto-lista di parole estratta viene poi utilizzata per creare una nuova istanza di ArrayList tramite new ArrayList<>(sottolista).
        Questo viene fatto per garantire che la sequenza iniziale restituita sia un'istanza separata e indipendente dalla lista di parole originale.
        */
        return new ArrayList<>(words.subList(startIndex, startIndex + keySize));
    }


    /**
     * Questo metodo genera il testo finale utilizzando il dizionario,
     * l'output iniziale e le dimensioni specificate.
     */
    private static StringBuilder generateText(Map<List<String>, List<String>> dict, List<String> output, int outputSize, int keySize) {
        // Usa StringBuilder per costruire il testo risultante
        StringBuilder result = new StringBuilder();

        // Variabile per determinare se la prossima parola deve iniziare con una lettera maiuscola
        boolean capitalizeNext = true;

        // Ciclo attraverso le parole già presenti in output
        for (String word : output) {
            // Capitalizza la parola se necessario
            word = capitalizeFirstLetter(word, capitalizeNext);
            // Aggiungi la parola al risultato
            result.append(word).append(" ");

            // Se la parola termina con un punto, aggiungi una nuova riga e capitalizza la prossima parola
            if (word.endsWith(".")) {
                result.append("\n");
                capitalizeNext = true;
            } else {
                capitalizeNext = false;
            }

            // Incrementa il contatore delle parole generate se la parola non è una punteggiatura
            if (!word.matches("\\p{Punct}")) {
                wordsGenerated++;
            }
        }

        // Continua a generare parole fino a raggiungere la dimensione desiderata dell'output
        while (wordsGenerated < outputSize) {
            // Ottieni la lista di parole successive basata sulle ultime 'keySize' parole in output
            List<String> suffix = dict.get(output.subList(output.size() - keySize, output.size()));

            // Se non ci sono parole successive disponibili, interrompi il ciclo
            if (suffix == null || suffix.isEmpty()) {
                break;
            }

            // Scegli la prossima parola basata su una scelta ponderata
            String nextWord = weightedRandomChoice(suffix);
            // Capitalizza la prossima parola se necessario
            nextWord = capitalizeFirstLetter(nextWord, capitalizeNext);
            // Aggiungi la parola alla lista di output
            output.add(nextWord);

            // Controlla se la parola è una punteggiatura o una rottura di linea
            boolean isPunctuation = nextWord.matches("\\p{Punct}");
            boolean isLineBreak = nextWord.equals("\n");

            // Se la parola non è punteggiatura
            if (!isPunctuation) {
                if (isLineBreak) {
                    // Se è una rottura di linea, aggiungi una nuova riga al risultato e capitalizza la prossima parola
                    result.append("\n");
                    capitalizeNext = true;
                } else {
                    // Altrimenti, aggiungi la parola e un spazio al risultato
                    result.append(nextWord).append(" ");
                    // Se la parola termina con un punto, aggiungi una nuova riga e capitalizza la prossima parola
                    if (nextWord.endsWith(".")) {
                        result.append("\n");
                        capitalizeNext = true;
                    } else {
                        capitalizeNext = false;
                    }
                    wordsGenerated++; // Incrementa il contatore delle parole generate

                    // Controlla se il numero di parole generate ha raggiunto la dimensione desiderata
                    if (wordsGenerated >= outputSize) {
                        break;
                    }
                }
            }
        }

        return result; // Ritorna il testo generato
    }


    /**
     * Questo metodo gestisce il finale di una frase, assicurandosi che
     * il testo generato abbia una corretta punteggiatura finale.
     */
    private static void handleEndOfSentence(StringBuilder result) {

        // Controllo se la stringa è vuota
        if (result == null) {
            return;
        }


        // Controllo se l'ultima frase ha un punto
        int lastDotIndex = result.lastIndexOf(".");
        if (lastDotIndex != -1 && lastDotIndex == result.length() - 1) {
            // Se l'ultima frase termina con un punto, non fare nulla
            return;
        }

        // Se l'ultima frase non ha un punto, trovare l'ultimo punto
        int lastPeriodIndex = result.lastIndexOf(".", lastDotIndex - 1);

        // Controllo se è stato trovato un punto in una posizione valida
        if (lastPeriodIndex != -1) {
            // Eliminare tutto quello che segue il punto e contare le parole eliminate
            String wordsAfterLastPeriod = result.substring(lastPeriodIndex + 1, result.length());
            String[] words = wordsAfterLastPeriod.split("[\\s\\n]+"); // Dividere le parole per spazio o nuova riga
            for (String word : words) {
                // Controllo se la parola contiene solo lettere o numeri (ignorando punteggiatura e \n)
                if (word.matches("[a-zA-Z0-9]+")) {
                    wordsCut++;
                }
            }
            result.delete(lastPeriodIndex + 1, result.length());
        }

        // Stampare il numero di parole eliminate
        if (getWordsCut() > 0)
            System.out.println("Ho eliminato alcune parole per un finale migliore!\n" +
                    "Ho tagliato: " + getWordsCut() + " parole!\n");
        cornice();

    }

    //Stampe di info
    private static void printAdditionalInfo(int outputSize) {
        /*Confrontiamo i valori*/
        System.out.println("Inizialmente hai richiesto di stampare: " + outputSize + " parole.");
        System.out.println("Io ho generato: " + (wordsGenerated - getWordsCut()) + " parole!");
        if (getWordsCut() > 0) {
            System.out.println("Ma ricorda che ho tagliato: " + (getWordsCut()) + " parole!\n" +
                    "Infatti " + wordsGenerated + " - " + wordsCut + " fa: " + (wordsGenerated - getWordsCut()) + "!");
        }
        cornice();

    }

    //Rendi la prima lettera dopo il punto maiuscola
    private static String capitalizeFirstLetter(String word, boolean capitalize) {
        /*Rende la prima lettera Maiuscola*/
        if (capitalize) {
            return word.substring(0, 1).toUpperCase() + word.substring(1);
        }
        return word;
    }


    /**
     * Questo metodo sceglie una parola da generare,
     * tenendo conto della probabilità di ciascuna.
     */
    private static String weightedRandomChoice(List<String> options) {
        // Crea una mappa per contare la frequenza di ciascuna opzione
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String option : options) {
            frequencyMap.put(option, frequencyMap.getOrDefault(option, 0) + 1);
        }

        // Calcola la frequenza totale
        int totalFrequency = frequencyMap.values().stream().mapToInt(Integer::intValue).sum();

        // Genera un valore casuale nel range delle frequenze totali
        int randomIndex = random.nextInt(totalFrequency);
        int currentIndex = 0;

        // Seleziona l'opzione in base al valore casuale
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            currentIndex += entry.getValue();
            if (randomIndex < currentIndex) {
                return entry.getKey();
            }
        }

        // In caso di errore, restituisce una scelta casuale (non dovrebbe accadere se tutto è corretto)
        return options.get(random.nextInt(options.size()));
    }

    /**
     * Questo metodo restituisce il nome del file senza l'estensione,
     * permettendo così di creare un articolo su qualsiasi testo.
     */
    public static String getfileName(String PATH) {
        File file = new File(PATH);
        String fileName = file.getName();
        int extensionIndex = fileName.lastIndexOf('.');

        /*Verifica se l'estensione è presente e ottieni solo il nome senza l'estensione*/
        return (extensionIndex == -1) ? fileName : fileName.substring(0, extensionIndex);
    }

    /**
     * Questo metodo trova il file da usare per la generazione
     */
    public static String trovaFile(String fileNoExtension) {
        // Aggiungi l'estensione .txt
        String fileName = fileNoExtension + ".txt";

        // Specifica il percorso della directory
        String directory = "src\\assets\\book";

        // Crea il percorso completo del file
        Path filePath = Paths.get(directory, fileName);

        return filePath.toString();
    }

    /**
     * Getter e setter
     */

    public static void setPath(String path) {
        MarkovOnText.path = path;
    }

    public static int getWordsCut() {
        return wordsCut;
    }

    public static String getPath() {
        return path;
    }

    public static void setKeysize(int keysize) {
        MarkovOnText.keysize = keysize;
    }

    public static int getKeySize() {
        return MarkovOnText.keysize;
    }

    public static int getOutputSize() {
        return MarkovOnText.outputsize;
    }

    public static void setParameters(int newKeySize, int newOutputSize) {
        MarkovOnText.keysize = newKeySize;
        MarkovOnText.outputsize = newOutputSize;
    }

    public static void setOutputsize(int outputsize) {
        MarkovOnText.outputsize = outputsize;
    }
}
