package Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static More.HelperMethod.searchFolder;

public class QRCodeGenerator {

    private static final String linkToDrive = "https://drive.google.com/drive/folders/1fTSl5K34Tggwmt_CSiznJogE7lvNpFDl?usp=drive_link";
    private static final int QR_CODE_SIZE = 200;
    private static final String qrCodePath = searchFolder("QrCode") + "QRCode.png";

    public static void main() {
        generateAndPrintQRCode(linkToDrive, qrCodePath);
    }

    public static void generateAndPrintQRCode(String link, String filePath) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(link, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, hints);

            saveImage(bitMatrix, filePath);

            printQRCode(bitMatrix);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveImage(BitMatrix bitMatrix, String filePath) throws IOException {
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", Paths.get(filePath));
        System.out.println("QR Code immagine salvato con successo!");
    }

    private static void printQRCode(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();

        int reductionFactor = Math.min(width, height) / 15; // Fattore di riduzione delle dimensioni

        for (int y = 0; y < height; y += reductionFactor * 2) {
            for (int x = 0; x < width; x += reductionFactor) {
                boolean isBlack = bitMatrix.get(x, y);

                if (isBlack) {
                    System.out.print("\u2588\u2588"); // Unicode per caratteri pieni
                } else {
                    System.out.print("  "); // Spazio vuoto
                }
            }
            System.out.println();
        }
    }
}
