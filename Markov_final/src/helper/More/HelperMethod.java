package More;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class HelperMethod {

    /**
     * Questo metodo restituisce un path randomico tra quelli del file,
     */
    public static String chooseRandomPath() throws IOException, InterruptedException {
        // Legge il contenuto del file specificato
        Random random=new Random();
        String filePath = (searchFolder("listOfPath") + "\\pathname.txt");
        Path path = Paths.get(filePath);
        List<String> pathsList = Files.readAllLines(path);
        Thread.sleep(random.nextInt(1000));
        int randomNumber = random.nextInt(pathsList.size()); // Genera un numero casuale da 0 a pathsList.size() - 1
        return pathsList.get(randomNumber);
    }

    /**
     * Questo metodo restituisce un numero di 15 cifre, simulando un id.
     */
    public static String generateSerialnumber() throws InterruptedException {
        Random random = new Random();
        int SERIAL_LENGTH = 15;
        StringBuilder stringBuilder = new StringBuilder(SERIAL_LENGTH);
        Thread.sleep(random.nextInt(1000));
        for (int i = 0; i < SERIAL_LENGTH; i++) {
            int digit = random.nextInt(10); // Genera un numero casuale da 0 a 9
            stringBuilder.append(digit);
        }

        return stringBuilder.toString();
    }

    /**
     * Questo metodo attende l'input dell'utente.
     */
    public static void attendiInput(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            reader.readLine(); // Aspetta che l'utente prema Invio
        } catch (IOException e) {
            System.out.println("Errore durante la lettura dell'input.");
            e.printStackTrace();
        }
    }


    public static String searchFolder(String folderName) {
        // Ottieni la directory di partenza (qui usiamo la home directory dell'utente, ma puoi cambiarla)
        String startPath = System.getProperty("user.home");

        // Inizia la ricerca nella directory di partenza
        File rootDir = new File(startPath);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            return null; // La directory di partenza non esiste o non è una cartella
        }

        return searchForFolder(rootDir, folderName);
    }

    private static String searchForFolder(File dir, String folderName) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return null; // Il percorso non è valido
        }

        // Escludi le cartelle di sistema e quelle che iniziano con "."
        String dirPath = dir.getAbsolutePath();
        if (dirPath.contains("AppData") ||
                dirPath.contains("Program Files") ||
                dirPath.contains("Program Files (x86)") ||
                dirPath.contains("Windows") ||
                dirPath.contains("ProgramData") ||
                dirPath.contains("Recovery") ||
                dirPath.contains("$Recycle.Bin") ||
                dirPath.contains("System Volume Information") ||
                dirPath.contains("OneDrive") ||
                dir.getName().startsWith(".")) {
            return null; // Ignora la cartella di sistema o nascosta
        }

        // Cerca nella cartella corrente
        File[] filesAndDirs = dir.listFiles();
        if (filesAndDirs == null) {
            return null; // Non è stato possibile ottenere la lista di file
        }

        for (File file : filesAndDirs) {
            if (file.isDirectory()) {
                // Se trovi la cartella con il nome specificato, restituisci il percorso
                if (file.getName().equalsIgnoreCase(folderName)) {
                    return file.getAbsolutePath();
                }

                // Cerca ricorsivamente nelle sottocartelle
                String foundPath = searchForFolder(file, folderName);
                if (foundPath != null) {
                    return foundPath;
                }
            }
        }

        return null; // Cartella non trovata
    }



}
