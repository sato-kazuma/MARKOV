package Matrix;

import static More.HelperMethod.searchFolder;

public class WinRARCompression {
    public static void main() {
        String directoryPath = searchFolder("QrCode") + "\\FilePerCliente";
        String destinationPath = searchFolder("QrCode") + "\\fileCompressed\\file_compresso.rar";

        try {
            String command = searchFolder("cartelleWinrar") + "\\WinRAR\\WinRAR.exe"; // Percorso di installazione di WinRAR
            String[] cmdArray = { command, "a", "-ep1", "-r", destinationPath, directoryPath + "\\*" };

            ProcessBuilder processBuilder = new ProcessBuilder(cmdArray);
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            System.out.println("Compressione completata con codice di uscita: " + exitCode);
            System.out.println("Se 0, ha avuto successo!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
