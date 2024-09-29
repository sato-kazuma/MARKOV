package Matrix;

import javax.swing.*;
import java.awt.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;

public class MakeHistogram extends JFrame {

    public MakeHistogram(String title, double[] waitTimes) {
        super(title);

        // Crea il dataset per l'istogramma
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries(title, waitTimes, 20); // Il terzo parametro rappresenta il numero di intervalli

        // Crea l'istogramma
        JFreeChart chart = ChartFactory.createHistogram(
                "Histogram of Times", // Titolo del grafico
                "Wait Time (ms)", // Etichetta asse x
                "Frequency", // Etichetta asse y
                dataset);

        // Crea un pannello per mostrare il grafico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(700, 800));
        setContentPane(chartPanel);
    }

    public static void showHistogram(double[] waitTimes, String name, int x, int y) {
        EventQueue.invokeLater(() -> {
            MakeHistogram histogram = new MakeHistogram(name, waitTimes);
            histogram.pack();
            histogram.setVisible(true);
            histogram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            histogram.setName(name);
            histogram.setLocation(x,y);
        });
    }


    public static void makeHistogram(double[] times, String name, int x, int y ) {
        showHistogram(times,name,x,y);
    }
}
