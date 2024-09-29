package queue;

import Matrix.QRCodeGenerator;
import Matrix.ServiceAndAverageTime;
import Matrix.StateofMatrix;
import Matrix.WinRARCompression;
import More.GoogleDriveUploader;
import queue.ClientServer.ClientData;
import queue.ClientServer.PersonalizedClient;
import queue.ClientServer.PersonalizedServer;
import workOnFile.FileDivider;
import workOnFile.OutputRedirector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static More.HelperMethod.attendiInput;
import static More.HelperMethod.chooseRandomPath;
import static textlogic.StampeOnText.cornice;

public class MarkovQueueSimulation {

    public static int STEPS = generateRandomNumber(); // Numero di passi della simulazione
    public static int MAX_CLIENTS = generateRandomNumber(); // Numero massimo di clienti per passo
    private static int currentState = 0; // Variabile globale per lo stato attuale
    private static double[][] transitionMatrix; // Matrice di transizione per i cambi di stato
    private static List<ClientData> clientDataList = new ArrayList<>(); // Lista per registrare i dati dei clienti
    private static StateofMatrix stateOfMatrix; // Istanza per gestire lo stato della matrice
    private static PersonalizedClient client = new PersonalizedClient("localhost", 8080); // Inizializzazione del client
    private static PersonalizedServer server = new PersonalizedServer(); // Inizializzazione del server
    private static int clientIndex = 1; // Indice globale per i clienti

    public static void main(String[] args) throws Exception {
        System.out.println("Premi invio per cominciare la simulazione.");
        attendiInput(); // Attende input dell'utente
        stampePresentazione(); // Mostra la presentazione della simulazione
        attendiInput(); // Attende input dell'utente per continuare

        // Inizio della simulazione
        System.out.println("*---------------------------------------------------------------------------------------------------------------*");
        System.out.println("INIZIAMO LA SIMULAZIONE DEL PROCESSO");
        Thread.sleep(1000);
        System.out.println("\nCome dati in ingresso, scegliamo "+STEPS+" STEPS\n");
        Thread.sleep(1000);
        System.out.println("Per la prima interazione, scegliamo: "+MAX_CLIENTS+ " clienti\n");
        setCurrentState(MAX_CLIENTS);
        Thread.sleep(1000);

        client.start(); // Avvio del client simulato
        Thread.sleep(1000);

        server.start(); // Avvio del server simulato
        Thread.sleep(1000);

        System.out.println("Connessione stabilita con il server all'indirizzo " + "localhost" + ":" + "8080" + ".\n");
        System.out.println("Client ora connessi al server.\n");
        Thread.sleep(1000);

        // Inizializzazione della matrice di transizione
        setTransitionMatrix(generateTransitionMatrix());
        stateOfMatrix = new StateofMatrix(currentState, transitionMatrix); // Passa la matrice al costruttore

        for (int step = 1; step <= STEPS; step++) {

            if(step==1){
                cornice();
                System.out.println("INIZIO RICHIESTE");
                cornice();
                System.out.println("\n");
            }

            // Simula la coda con i clienti e il server
            simulateQueueWithClientsAndServer();

            //stampa la matrice generata per questo step, tranne per l'ultimo!
            if(step != STEPS) {
                System.out.println("\n");
                System.out.println("La matrice che verrà usata per il prossimo step e' :");
                printTransitionMatrix(getTransitionMatrix());
            }

            // Aggiorna lo stato per determinare il numero di clienti nel prossimo passo
            MAX_CLIENTS = stateOfMatrix.updatingState(getCurrentState());
            setCurrentState(MAX_CLIENTS);

            if(step != STEPS) {
                System.out.println("Il server ha decretato che per la fase successiva ci saranno " + MAX_CLIENTS + " clienti!\n");
            }

            //genera la matrice generata per questo step, tranne per l'ultimo!
            if(step != STEPS) {
                generateTransitionMatrix();
            }

            System.out.println("\n");
        }

        // Stampa la lista finale dei clienti dopo la simulazione
        System.out.println("L'ultimo cliente e' stato servito.\n");
        cornice();
        System.out.println("\n");
        System.out.println("Recupero delle informazioni di tutti i clienti serviti!");
        printClientDataList();
        cornice();
        System.out.println("\n");

        OutputRedirector.closeOutputFile(); // Chiudi il file di output
        client.stop();
        server.stop();

        cornice();
        System.out.println("\n");
        // Analisi dei dati dei clienti
        ServiceAndAverageTime serviceAndAverageTime = new ServiceAndAverageTime();
        serviceAndAverageTime.analyzeData(clientDataList);

        cornice();
        System.out.println("\n");

        System.out.println("PROCEDEREMO ORA A COMPRIMERE I DATI IN UNA ZIP, E A CARICARLI SU GOOGLE DRIVE");
        // Chiamate per file QR, compressione e upload
        FileDivider.main();
        WinRARCompression.main();
        GoogleDriveUploader.main();
        QRCodeGenerator.main();

        System.out.println("Chiudi le finestre create , poi premi invio per terminare la simulazione...");
        attendiInput();
    }

    /**
     * Simula la coda tra i clienti e il server
     */
    public static void simulateQueueWithClientsAndServer() throws IOException, InterruptedException {
        String[] methods = {"GET", "POST", "PUT", "DELETE"}; // Metodi HTTP simulati
        Random random = new Random(); // Generatore di numeri casuali

        // Ciclo per simulare MAX_CLIENTS clienti
        for (int i = 0; i < MAX_CLIENTS; i++) {
            String randomMethod = methods[random.nextInt(methods.length)]; // Metodo HTTP casuale
            String targetUrl = "http://localhost:8080/" + chooseRandomPath(); // URL casuale

            // Crea un oggetto ClientData e aggiungilo alla lista
            ClientData clientData = new ClientData(clientIndex, 100, 100); // Assegna l'indice globale al cliente
            clientDataList.add(clientData);

            // Invia la richiesta al server

            cornice();
            System.out.println("Invio della richiesta al server del cliente numero: "+clientIndex+"...");
            cornice();
            Thread.sleep(2000);
            client.sendRequest(targetUrl, randomMethod, clientDataList, clientIndex);

            // Recupera e stampa i tempi di attesa e di servizio
            long waitTime = clientData.getWaitTime();
            long serviceTime = clientData.getServiceTime();

            System.out.println("Recupero delle informazioni del server del cliente: "+ clientData.getClientNumber());

            stampaInfoPerCliente(waitTime, serviceTime, clientData.getClientNumber());

            clientIndex++; // Incrementa l'indice per il prossimo cliente
        }

    }

    /**
     * Stampa la lista dei dati dei clienti
     */
    public static void printClientDataList() {
        System.out.println("\nDati dei clienti nella lista:\n");
        for (ClientData clientData : clientDataList) {
            System.out.printf("Cliente %d: Tempo di attesa: %d ms, Tempo di servizio: %d ms%n",
                    clientData.getClientNumber(), clientData.getWaitTime(), clientData.getServiceTime());
            System.out.println("\n");
        }
    }

    /**
     * Stampa le informazioni di attesa e servizio per ogni cliente
     */
    public static void stampaInfoPerCliente(long waitTime, long serviceTime, int clientNumber) {
        System.out.println("\n");
        System.out.printf("Cliente %d: Tempo di attesa: %d ms, Tempo di servizio: %d ms%n", clientNumber, waitTime, serviceTime);
        System.out.println("\n");
    }

    /**
     * Mostra le stampe di presentazione all'inizio della simulazione
     */
    public static void stampePresentazione() throws InterruptedException {
        System.out.println("In questa simulazione,simuleremo una catena di markov su una coda di clienti.\n" +
                "I clienti effettuano richieste al server e ne verrano misurati i tempi di attesa e di servizio.\n");
        Thread.sleep(1000);
        System.out.println("Definiamo una variabile STEP, che rappresenta quante volte le code di clienti verranno create.\n" +
                "Per ogni step, ci saranno Random clienti , definiti da MAX_CLIENT, con max 4 clienti per STEP.\n");
        Thread.sleep(1000);
        System.out.println("MAX_CLIENT sarà gestito con la creazione di una matrice che terrà conto delle proprietà delle catene di Markov.\n" +
                "Questa matrice verra' stampata, rappresentando per righe il numero di clienti e per colonne la probabilità di passaggio di stato ad N clienti.\n");
        Thread.sleep(1000);
        System.out.println("\nPremi invio per procedere con la simulazione.\n");
    }

    /**
     * Stampa la matrice di transizione
     */
    public static void printTransitionMatrix(double[][] matrix) throws InterruptedException {
        System.out.println("*---------------------------------------------------------------------------------------------------------------*");

        // Stampa l'intestazione delle colonne
        for (int j = 0; j < matrix[0].length; j++) {
            System.out.print("  ");
            System.out.printf("%8d", j);  // Indice delle colonne
        }
        System.out.println();

        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");

        // Stampa la matrice riga per riga
        for (int i = 0; i < matrix.length; i++) {
            System.out.printf("%3d | ", i);  // Indice della riga
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("[%7.4f] ", matrix[i][j]);  // Elementi della matrice
            }
            Thread.sleep(2000);
            System.out.println();
        }
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        System.out.println();
        System.out.println("*---------------------------------------------------------------------------------------------------------------*");
    }

    /**
     * Genera la matrice, fissa da 0 fino a 4 clienti max.
     */
    public static double[][] generateTransitionMatrix() {
        double[][] transitionMatrix = new double[5][5]; // Dimensione 5x5

        for (int i = 0; i < 5; i++) { // Cambiato a < 5
            boolean validRow = false;

            while (!validRow) {
                double[] probabilities = new double[5]; // Generiamo 5 probabilità

                // Genera probabilità casuali per ogni colonna
                for (int j = 0; j < 5; j++) { // Cambiato a < 5
                    probabilities[j] = Math.random();
                }

                // Somma le probabilità per normalizzarle
                double sum = Arrays.stream(probabilities).sum();

                // Normalizza se la somma è maggiore di 0
                if (sum > 0) {
                    for (int j = 0; j < 5; j++) { // Cambiato a < 5
                        transitionMatrix[i][j] = probabilities[j] / sum;
                    }
                    validRow = true; // Riga valida
                }
            }
        }
        return transitionMatrix;
    }

    /**
     * Genera il numero di partenza dei clienti e degli step
     */
    public static int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(3) + 2; // genera un numero tra 2 e 4
    }

    // Getters e Setters
    public static double[][] getTransitionMatrix() {
        return transitionMatrix;
    }

    public static void setTransitionMatrix(double[][] transitionMatrix) {
        MarkovQueueSimulation.transitionMatrix = transitionMatrix;
    }

    public static int getCurrentState() {
        return currentState;
    }

    public static void setCurrentState(int currentState) {
        MarkovQueueSimulation.currentState = currentState;
    }
}
