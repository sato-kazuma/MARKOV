package queue.ClientServer;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PersonalizedServer {
    private boolean isRunning;

    public PersonalizedServer() {
        this.isRunning = false; // Inizializza il server come non in esecuzione
    }

    public void start() throws InterruptedException {
        isRunning = true;
        System.out.println("Il server è stato avviato.\n");
        Thread.sleep(2000);
        System.out.println("Inizio dell'ascolto delle richieste dai client...\n");

        // Simulazione di un breve ritardo per imitare il tempo necessario per l'avvio del server
        try {
            Thread.sleep(1000); // Simula un'attesa di 1 secondo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Il server è ora in ascolto sulla porta 8080.\n");
    }


    public void stop() throws InterruptedException {
        if (isRunning) {
            System.out.println("Il server sta chiudendo tutte le connessioni attive...\n");

            // Simulazione di un breve ritardo per imitare il tempo necessario per chiudere le connessioni
            try {
                Thread.sleep(1000); // Simula un'attesa di 1 secondo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            isRunning = false;
            System.out.println("Il server è stato fermato con successo.\n");
            Thread.sleep(1000);

            // Messaggio finale
            System.out.println("Tutte le connessioni sono state chiuse. Il server non è più in ascolto.\n");
        } else {
            System.out.println("Il server è già fermo. Nessuna connessione attiva da chiudere.\n");
        }
    }

    public static void handleClient(String method, String targetUrl) throws InterruptedException {
        // Crea una richiesta dal client
        System.out.println("Sono il server..Sto ricevendo qualcosa...\n");
        Thread.sleep(1000);

        String request = method + " " + targetUrl + " HTTP/1.1";
        System.out.println("Ricevuto: " + request);
        Thread.sleep(2000);

        // Determina il tipo di richiesta e genera una risposta appropriata
        String response;
        if (method.equals("GET")) {
            response = generateHttpResponse(200, "OK", "Ecco il contenuto richiesto.");
        } else if (method.equals("POST")) {
            response = generateHttpResponse(201, "Created", "Risorsa creata con successo.");
        } else if (method.equals("DELETE")) {
            response = generateHttpResponse(204, "No Content", "Risorsa eliminata.");
        } else {
            response = generateHttpResponse(400, "Bad Request", "La richiesta non è valida.");
        }

        // Invia la risposta al client
        System.out.println("Risposta: " + response);
    }


    // Genera una risposta HTTP simulata
    private static String generateHttpResponse(int statusCode, String statusMessage, String bodyContent) throws InterruptedException {
        // Usa ZonedDateTime per includere l'offset
        ZonedDateTime zdt = ZonedDateTime.now(ZoneOffset.UTC); // Specifica l'offset desiderato
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z");

        String dateHeader = dtf.format(zdt);

        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusMessage).append("\r\n");
        response.append("Date: ").append(dateHeader).append("\r\n");
        response.append("Content-Type: eseguibili.text/html; charset=UTF-8\r\n");
        response.append("Content-Length: ").append(bodyContent.length()).append("\r\n");
        response.append("Connection: close\r\n");
        response.append("\r\n");
        response.append(bodyContent);

        return response.toString();
    }



}
