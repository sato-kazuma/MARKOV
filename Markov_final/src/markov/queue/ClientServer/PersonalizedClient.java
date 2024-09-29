package queue.ClientServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import static More.HelperMethod.generateSerialnumber;
import static More.HelperMethod.searchFolder;

public class PersonalizedClient {
    private boolean isRunning;
    private final String serverAddress;
    private final int serverPort;
    private final String dirListPath = searchFolder("listOfPath");
    private final Random random = new Random();
    public static String targetUrl;

    public PersonalizedClient(String serverAddress, int serverPort) {
        this.isRunning = false;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void start() {
        isRunning = true;
        System.out.println("I client sono stati avviati. Attendono la connessione al server...\n");
        // Simulazione di un breve ritardo prima di confermare la connessione
        try {
            Thread.sleep(1000); // Simula un'attesa di 1 secondo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() throws InterruptedException {
        if (isRunning) {
            System.out.println("I client stanno chiudendo la connessione al server...\n");

            // Simulazione di un breve ritardo per imitare il tempo necessario per chiudere la connessione
            try {
                Thread.sleep(1000); // Simula un'attesa di 1 secondo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            isRunning = false;
            System.out.println("I client sono stati fermati con successo.\n");
            Thread.sleep(1000);
        } else {
            System.out.println("I client sono già fermi. Nessuna connessione attiva da chiudere.\n");
        }
    }

    public void sendRequest(String targetUrl, String method, List<ClientData> clientDataList, int indexOfClient) throws InterruptedException, IOException {
        System.out.println("\n");
        System.out.println(method + " " + targetUrl + " HTTP/1.1");
        Thread.sleep(1000);
        setTargetUrl(targetUrl);

        // Configura e stampa gli header
        configureHeaders();

        // Seleziona un JSON casuale
        String jsonData = getRandomJsonData();

        // Se ci sono dati JSON (per POST o PUT), li stampiamo
        if (jsonData != null) {
            System.out.println("\n");
            System.out.println("Request Body:");
            System.out.println(jsonData);
            System.out.println();
        }
        Thread.sleep(1000);

        // Ottieni il cliente specificato dall'indice della clientDataList
        ClientData clientData = clientDataList.get(indexOfClient - 1);

        // Simulazione tempi di attesa e servizio casuali
        long actualWaitTime = simulateWaitTime();
        long actualServiceTime = simulateServiceTime();

        // Imposta i tempi nel ClientData
        clientData.setWaitTime(actualWaitTime);
        clientData.setServiceTime(actualServiceTime);

        // Aggiorna clientDataList con il nuovo ClientData modificato
        clientDataList.set(indexOfClient - 1, clientData);

        // Stampa un codice di risposta verosimile
        int responseCode = simulateResponseCode(method);
        System.out.println("Risposta da parte del server...\n");
        Thread.sleep(1000);
        System.out.println("HTTP/1.1 " + responseCode + " " + getResponseMessage(responseCode));
        Thread.sleep(1000);
        // Stampa i tempi in maniera realistica
        System.out.println("X-Wait-Time: " + actualWaitTime + " ms");
        Thread.sleep(1000);
        System.out.println("X-Service-Time: " + actualServiceTime + " ms");
        System.out.println("*---------------------------------------------------------------------------------------------------------------*");
        Thread.sleep(1000);
        System.out.println("Richiesta mandata al server...attesa elaborazione");
        Thread.sleep(2000);
        PersonalizedServer.handleClient(method, targetUrl);
    }

    // Metodo per configurare gli header, scegliendo casualmente dai file
    private void configureHeaders() throws IOException, InterruptedException {
        String contentType = getRandomLineFromFile("content-types.txt");
        String authorization = generateAuthorizationHeader();
        String acceptEncoding = getRandomLineFromFile("accept-encoding.txt");
        String userAgent = getRandomLineFromFile("user-agents.txt");
        String cacheControl = getRandomLineFromFile("cache-control.txt");
        String httpHeader = getRandomLineFromFile("http-headers.txt");

        // Stampa delle intestazioni HTTP verosimili
        System.out.println("Content-Type: " + contentType);
        System.out.println("Authorization: " + authorization);
        System.out.println("Accept-Encoding: " + acceptEncoding);
        System.out.println("User-Agent: " + userAgent);
        System.out.println("Cache-Control: " + cacheControl);
        System.out.println(httpHeader.split(":")[0] + ": " + httpHeader.split(":")[1].trim());
    }

    // Genera un header di autorizzazione in modo casuale
    private String generateAuthorizationHeader() throws InterruptedException {
        String tokenType = random.nextBoolean() ? "Bearer" : "Basic";
        return tokenType + " Token: " + generateSerialnumber();
    }

    // Metodo per simulare un codice di risposta, includendo errori casuali
    private int simulateResponseCode(String method) {
        if (random.nextInt(100) < 10) { // 10% di probabilità di errore
            return random.nextBoolean() ? 500 : 404; // Simula errori 500 o 404
        }
        switch (method) {
            case "POST":
            case "PUT":
                return 201; // Created or Updated
            case "GET":
                return 200; // OK
            case "DELETE":
                return 204; // No Content
            default:
                return 200; // Default OK
        }
    }

    // Metodo per ottenere un messaggio verosimile dal codice di risposta
    private String getResponseMessage(int responseCode) {
        switch (responseCode) {
            case 200:
                return "OK";
            case 201:
                return "Created";
            case 204:
                return "No Content";
            case 404:
                return "Not Found";
            case 500:
                return "Internal Server Error";
            default:
                return "OK";
        }
    }

    // Metodo per simulare un tempo di attesa casuale
    private long simulateWaitTime() throws InterruptedException {
        long waitTime = 300 + random.nextInt(700); // Simula attesa tra 300 e 1000 ms
        Thread.sleep(waitTime);
        return waitTime;
    }

    // Metodo per simulare un tempo di servizio casuale
    private long simulateServiceTime() throws InterruptedException {
        long serviceTime = 800 + random.nextInt(1200); // Simula servizio tra 800 e 2000 ms
        Thread.sleep(serviceTime);
        return serviceTime;
    }

    // Metodo per leggere casualmente una riga da un file
    private String getRandomLineFromFile(String fileName) throws IOException {
        Path filePath = Paths.get(dirListPath, fileName); // Unisci il percorso della directory con il nome del file
        List<String> lines = Files.readAllLines(filePath);
        return lines.get(random.nextInt(lines.size()));
    }

    // Metodo per ottenere un JSON casuale
    private String getRandomJsonData() {
        String[] jsonExamples = {
                "{\"userId\": 1, \"username\": \"johndoe\", \"email\": \"johndoe@example.com\"}",
                "{\"productId\": 101, \"productName\": \"Smartphone\", \"price\": 699.99, \"inStock\": true}",
                "{\"orderId\": 202, \"userId\": 1, \"items\": [{\"productId\": 101, \"quantity\": 2}, {\"productId\": 102, \"quantity\": 1}], \"totalPrice\": 1399.97}",
                "{\"reviewId\": 303, \"productId\": 101, \"rating\": 5, \"comment\": \"Ottimo prodotto!\"}",
                "{\"messageId\": 404, \"sender\": \"johndoe@example.com\", \"receiver\": \"janedoe@example.com\", \"content\": \"Ciao, come stai?\"}",
                "{\"eventId\": 505, \"eventName\": \"Conferenza\", \"date\": \"2024-10-01T09:00:00Z\", \"location\": \"Sala conferenze A\"}",
                "{\"profileId\": 606, \"userId\": 1, \"bio\": \"Appassionato di tecnologia e programmazione.\"}",
        };
        return jsonExamples[random.nextInt(jsonExamples.length)];
    }

    public static void setTargetUrl(String targetUrl) {
        PersonalizedClient.targetUrl = targetUrl;
    }
}
