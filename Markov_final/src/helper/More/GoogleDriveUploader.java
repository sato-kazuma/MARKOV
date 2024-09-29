package More;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static More.HelperMethod.searchFolder;

public class GoogleDriveUploader {
    public static void main() {
        String filePath = searchFolder("QrCode") + "\\fileCompressed\\file_compresso.rar";
        String remoteName = "Markov";  // Nome del remoto configurato in rclone
        String destinationFolder = "Markov_Chain";  // Cartella di destinazione su Google Drive
        String overwriteFlag = "--update"; // Opzione per sovrascrivere i file esistenti
        String rcloneCommand = searchFolder("rclone") + "\\rclone-v1.65.0-windows-amd64\\rclone.exe";

        try {
            // Costruisce il comando per eseguire Rclone
            String command = rcloneCommand + " copy " + overwriteFlag + " \"" + filePath + "\" " + remoteName + ":" + destinationFolder;

            // Usa ProcessBuilder invece di exec
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true); // Redirige l'output di errore nello stesso stream dell'output standard

            // Avvia il processo
            Process process = processBuilder.start();

            // Leggi l'output del comando
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // Legge e stampa l'output standard e di errore (se necessario)
            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            while ((s = stdError.readLine()) != null) {
                System.err.println(s);
            }

            // Verifica il codice di uscita
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Upload completato con successo!");
            } else {
                System.out.println("Errore durante l'upload. Codice di uscita: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

