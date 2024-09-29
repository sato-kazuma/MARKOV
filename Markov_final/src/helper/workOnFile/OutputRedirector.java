package workOnFile;


import org.apache.commons.io.output.TeeOutputStream;
import java.io.*;
import java.util.Random;

public class OutputRedirector {
    private static PrintStream originalOut = System.out;
    private static PrintStream fileStream;
    private static ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private static boolean buffering = true;
    private static boolean alreadyPrintedToFile = false;

    public static String randomId = generateRandomId();

    public static void redirectOutputToFile(String filename) {
        try {
            File file = new File(filename);

            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {

                // Scrive l'identificatore all'inizio del file
                writer.println("ID CHAIN: " + randomId);
            }

            fileStream = new PrintStream(new FileOutputStream(file, true)) {
                @Override
                public void println(String x) {
                    if (buffering) {
                        try {
                            buffer.write((x + System.lineSeparator()).getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (!alreadyPrintedToFile && fileStream != null) {
                            fileStream.println(x);
                            alreadyPrintedToFile = true;
                        }
                    }
                    originalOut.println(x); // Stampa solo se siamo in modalità buffer
                }
            };
            System.setOut(new PrintStream(new TeeOutputStream(originalOut, fileStream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void closeOutputFile() {
        if (fileStream != null) {
            fileStream.close();
        }
    }

    public static void flushBufferToFile() {
        if (buffering && fileStream != null) {
            fileStream.println(buffer.toString());
            buffer.reset();
            buffering = false;
            alreadyPrintedToFile = false; // Resetta il flag dopo il flushing
        }
    }

    // Metodo per generare un ID casuale alfanumerico
    private static String generateRandomId() {
        int length = 20; // Lunghezza dell'ID
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return builder.toString();
    }


    public static String getRandomIdChain() {
        return randomId;
    }
}
