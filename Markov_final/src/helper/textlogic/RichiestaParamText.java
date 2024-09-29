package textlogic;


import text.MarkovOnText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static text.MarkovOnText.*;
import static textlogic.StampeOnText.cornice;

public class RichiestaParamText {
    private static String filePath;

    public static String askFile(String raccolta) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        cornice();
        System.out.println("Cominciamo dal leggere i file disponibili!\n");
        Thread.sleep(2000);

        File directory = new File(raccolta);
        File[] files = directory.listFiles(file -> file.getName().toLowerCase().endsWith(".txt"));

        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + getfileName(files[i].getPath()));
                Thread.sleep(1000);
            }

            cornice();
            int fileChoice = -1;
            while (fileChoice < 1 || fileChoice > files.length) {
                System.out.print("Inserisci il numero corrispondente al file desiderato: ");
                if (sc.hasNextInt()) {
                    fileChoice = sc.nextInt();
                    if (fileChoice < 1 || fileChoice > files.length) {
                        System.out.println("Scelta non valida. Inserisci un numero tra 1 e " + files.length + ".");
                    }
                } else {
                    System.out.println("Input non valido. Per favore, inserisci un numero.");
                    sc.next(); // Consuma l'input non valido
                }
            }

            filePath = files[fileChoice - 1].getPath();
            String fileName = getfileName(filePath);

            cornice();
            System.out.println("File scelto: " + fileName);
            cornice();
            return fileName;
        } else {
            throw new IllegalArgumentException("Nessun file trovato nella directory specificata.");
        }
    }

    public static int askKey() {
        Scanner sc = new Scanner(System.in);
        cornice();
        System.out.print(
                """
                        Per prima cosa, mi serve la dimensione della chiave, scrivila sul terminale per favore.
                        Più è grande, maggiore sarà il senso del testo!
                        Non accetto zero come parametro!
                        Ti consiglio di darmi un numero tra 8 e 15, non più di 20!
                        """);

        int keySize;
        do {
            System.out.print("Inserisci la dimensione della chiave: ");
            while (!sc.hasNextInt()) {
                System.out.println("Input non valido. Per favore, inserisci un numero.");
                sc.next(); // Consuma l'input non valido
            }
            keySize = sc.nextInt();

            if (keySize < 1) {
                System.out.println("La dimensione della chiave non può essere inferiore a 1.");
            } else if (keySize > 20) {
                System.out.println("La dimensione della chiave non può essere maggiore di 20.");
            }
        } while (keySize < 1 || keySize > 20);

        // Salva keySize nella classe MarkovOnText
        setParameters(keySize, MarkovOnText.getOutputSize());

        cornice();
        System.out.println("\n");
        return keySize;
    }

    public static int askOutput() throws IOException {
        Scanner sc = new Scanner(System.in);
        cornice();
        System.out.print(
                "\nOra mi serve sapere quante parole vuoi che generi.\n" +
                        "Ricorda che non posso generare più parole di quelle che mi hai dato, " +
                        "che sono esattamente: " + dimDoc() + " parole!\n");

        int outputSize;
        do {
            System.out.print("Inserisci la dimensione dell'output: ");
            while (!sc.hasNextInt()) {
                System.out.println("Input non valido. Per favore, inserisci un numero.");
                sc.next(); // Consuma l'input non valido
            }
            outputSize = sc.nextInt();

            if (outputSize < 0) {
                System.out.println("La dimensione dell'output deve essere maggiore o uguale a zero.");
            } else if (outputSize < getKeySize()) { // Usa getKeySize qui
                System.out.println("La dimensione dell'output deve essere maggiore o uguale alla dimensione della chiave.");
            } else if (outputSize > dimDoc()) {
                System.out.println("La dimensione dell'output deve essere minore o uguale al numero di parole disponibili: " + dimDoc() + " !");
            }
        } while (outputSize < 0 || outputSize < getKeySize() || outputSize > dimDoc());

        // Salva outputSize nella classe MarkovOnText
        setParameters(getKeySize(), outputSize);

        return outputSize;
    }

    private static int dimDoc() throws IOException {
        int globalCount = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split("\\s+"); // Dividi la riga in parole usando gli spazi come delimitatori
                globalCount += words.length;
            }
        }
        return globalCount;
    }
}
