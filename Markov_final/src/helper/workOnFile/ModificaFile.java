package workOnFile;

import java.io.*;

public class ModificaFile {
    /*Questa classe serve a modificare il file di input di Markov eseguibili.text per renderlo lavorabile*/
    public static void changeFile(String inputFile, String outputFile) {
        try {
            File input = new File(inputFile);
            File output = new File(outputFile);

            BufferedReader reader = new BufferedReader(new FileReader(input));
            BufferedWriter writer = new BufferedWriter(new FileWriter(output));

            String line;
            boolean deleteNextLine = false;
            while ((line = reader.readLine()) != null) {
                if (line.toUpperCase().contains("CAPITOLO")) {
                    // Cancella la riga con "CAPITOLO"
                    deleteNextLine = true;
                } else if (deleteNextLine) {
                    // Salta la riga successiva dopo "CAPITOLO"
                    deleteNextLine = false;
                } else if (line.toUpperCase().contains("LIBRO")) {
                    // Cancella la riga con "LIBRO"
                    deleteNextLine = true;
                } else if (deleteNextLine) {
                    // Salta la riga successiva dopo "LIBRO"
                    deleteNextLine = false;
                } else {
                    // Scrivi la riga nel file di output
                    writer.write(line);
                    writer.newLine();
                }
            }

            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
