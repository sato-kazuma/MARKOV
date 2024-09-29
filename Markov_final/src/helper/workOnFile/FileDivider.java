package workOnFile;

import java.io.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static More.HelperMethod.searchFolder;

public class FileDivider {
    private static final String outputPath = searchFolder("QrCode") + "\\FilePerCliente\\";
    private static final String path=(outputPath+"\\CLIENTE.txt");

    public static void divideFile(String inputPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line;
            boolean writeToAnalysis = false;
            boolean writeToClient = false;
            BufferedWriter writer = null;

            while ((line = reader.readLine()) != null) {
                if (line.equals("Procedo ad analizzare i dati presi dalla Lista delle persone!")) {
                    writeToAnalysis = true;
                    writeToClient = false;
                    if (writer != null) {
                        writer.close();
                    }
                    writer = new BufferedWriter(new FileWriter(outputPath + "ANALISI.txt"));
                    writer.write(line);
                    writer.newLine();
                }

                if (writeToAnalysis) {
                    if (!line.equals("Procedo ad analizzare i dati presi dalla Lista delle persone!")) {
                        if (writer != null) {
                            writer.write(line);
                            writer.newLine();
                        }
                    }
                } else if (!line.equals("Procedo ad analizzare i dati presi dalla Lista delle persone!") && !writeToClient) {
                    writeToClient = true;
                    if (writer != null) {
                        writer.close();
                    }
                    writer = new BufferedWriter(new FileWriter(outputPath + "\\CLIENTE" + ".txt"));
                    writer.write(line);
                    writer.newLine();
                } else if (writeToClient) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void divideFileUlteriore(String inputPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line;
            int count = 1;
            BufferedWriter writer = null;
            boolean isNewFile = true;
            int lastOccurrence = 0;

            while ((line = reader.readLine()) != null) {
                if (line.equals("_____________________________________________________________________________________________________")) {
                    if (writer != null) {
                        writer.close();
                    }
                    isNewFile = true;
                    lastOccurrence = count; // Memorizza l'indice dell'ultima occorrenza
                    continue;
                }

                if (isNewFile) {
                    writer = new BufferedWriter(new FileWriter(outputPath + "CLIENTE" + count + ".txt"));
                    count++;
                    isNewFile = false;
                }

                if (writer != null) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            if (writer != null) {
                writer.close();
            }

            // Ora controlliamo l'ultima occorrenza e fermiamo eventuali stampe aggiuntive
            if (lastOccurrence != 0) {
                while ((line = reader.readLine()) != null) {
                    // Ignora le stampe successive all'ultima occorrenza
                    // Se vuoi ignorare anche la riga contenente la delimitazione, puoi usare lastOccurrence - 1
                    if (count > lastOccurrence) {
                        break;
                    }
                    // Puoi aggiungere qui altre condizioni per filtrare ulteriormente le stampe
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void deleteFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteFilesInDirectory(file.getAbsolutePath());
                        file.delete();
                    } else {
                        file.delete();
                    }
                }
            }
        }
    }

    public static void deleteFileByNumber(String directoryPath, int fileNumber) {
        File directory = new File(directoryPath);

        String fileNameToDelete = "Cliente" + fileNumber + ".txt";
        File fileToDelete = new File(directory, fileNameToDelete);

        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                System.out.println("I TUOI FILE SONO PRONTI A DIvENTARE QR");
            } else {
                System.out.println("Impossibile eliminare il file: " + fileNameToDelete);
            }
        } else {
            System.out.println("Il file " + fileNameToDelete + " non esiste nella directory specificata.");
        }
    }

    public static int getHighestIndex(String directoryPath) {
        File directory = new File(directoryPath);

        File[] matchingFiles = directory.listFiles((dir, name) -> name.matches("^CLIENTE\\d+\\.txt$"));

        if (matchingFiles != null && matchingFiles.length > 0) {
            Arrays.sort(matchingFiles, (file1, file2) -> {
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher1 = pattern.matcher(file1.getName());
                Matcher matcher2 = pattern.matcher(file2.getName());

                if (matcher1.find() && matcher2.find()) {
                    int index1 = Integer.parseInt(matcher1.group());
                    int index2 = Integer.parseInt(matcher2.group());
                    return Integer.compare(index1, index2);
                }
                return 0;
            });

            String fileName = matchingFiles[matchingFiles.length - 1].getName();
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(fileName);

            if (matcher.find()) {
                return Integer.parseInt(matcher.group());
            }
        }

        return 0; // Se non trova file corrispondenti
    }

    public static void main() throws InterruptedException {
        deleteFilesInDirectory(searchFolder("FilePerCliente"));
        divideFile(searchFolder("QrCode")+"\\FinalChain.txt");
        divideFileUlteriore(path);
        int input=getHighestIndex(searchFolder("FilePerCliente"));
        deleteFileByNumber(searchFolder("FilePerCliente"),input);


    }
}
