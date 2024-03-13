package GUI;

import Observer.ISimDelegate;
import SimulationClasses.Sem1;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GUI_Sem1 extends JFrame implements ActionListener, ISimDelegate, ChangeListener {
    private JPanel mainPanel;
    private JTextField textFieldReplications;
    private JTabbedPane tabbedPane2;
    private JPanel panelStratA;
    private JButton buttonEnd;
    private JButton buttonStart;
    private JPanel panelGraphB;
    private JPanel panelGraphA;
    private JPanel panelGraphC;
    private JLabel labelPaidC;
    private JLabel labelPaidB;
    private JLabel labelPaidA;
    private JLabel labelRepA;
    private JLabel labelRepB;
    private JLabel labelRepC;
    private JTextField textFieldPercReplications;
    private JTextField textFieldSeed;

    private XYSeries series1 = new XYSeries("Priemerna suma");
    private XYPlot plot1;
    private XYSeriesCollection dataset1 = new XYSeriesCollection(series1);
    private JFreeChart chart1;
    private ChartPanel chartPanel1;

    private XYSeries series2 = new XYSeries("Priemerna suma");
    private XYPlot plot2;
    private XYSeriesCollection dataset2 = new XYSeriesCollection(series2);
    private JFreeChart chart2;
    private ChartPanel chartPanel2;

    private XYSeries series3 = new XYSeries("Priemerna suma");
    private XYPlot plot3;
    private XYSeriesCollection dataset3 = new XYSeriesCollection(series3);
    private JFreeChart chart3;
    private ChartPanel chartPanel3;
    private Random seedGen;
    private Sem1 sem1A;
    private Sem1 sem1B;
    private Sem1 sem1C;
    private boolean isRunning = false;
    private int replicationCount;
    private int updateFrequency;
    
    public GUI_Sem1() {
        this.setContentPane(mainPanel);
        this.setTitle("Simulácia hypoteka");
        this.setSize(1920, 1080);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.buttonEnd.addActionListener(this);
        this.buttonStart.addActionListener(this);

        chart1 = ChartFactory.createXYLineChart("Zaplatená suma stratégia A",
                "Počet replikacii", "Priemerna zaplatena suma za 10 rokov", dataset1,
                PlotOrientation.VERTICAL, false, true, false);
        plot1 = chart1.getXYPlot();

        NumberAxis domainAxis = (NumberAxis) plot1.getDomainAxis();
        domainAxis.setAutoRange(true);

        NumberAxis rangeAxis = (NumberAxis) plot1.getRangeAxis();
        rangeAxis.setAutoRange(true);
        rangeAxis.setAutoRangeIncludesZero(false);

        chartPanel1 = new ChartPanel(chart1);
        this.panelGraphA.setLayout(new BorderLayout());
        this.panelGraphA.add(chartPanel1, BorderLayout.CENTER);
        this.panelGraphA.validate();

        chart2 = ChartFactory.createXYLineChart("Zaplatená suma stratégia B",
                "Počet replikacii", "Priemerna zaplatena suma za 10 rokov", dataset2,
                PlotOrientation.VERTICAL, false, true, false);
        plot2 = chart2.getXYPlot();

        NumberAxis domainAxis2 = (NumberAxis) plot2.getDomainAxis();
        domainAxis2.setAutoRange(true);

        NumberAxis rangeAxis2 = (NumberAxis) plot2.getRangeAxis();
        rangeAxis2.setAutoRange(true);
        rangeAxis2.setAutoRangeIncludesZero(false);

        chartPanel2 = new ChartPanel(chart2);
        this.panelGraphB.setLayout(new BorderLayout());
        this.panelGraphB.add(chartPanel2, BorderLayout.CENTER);
        this.panelGraphB.validate();

        chart3 = ChartFactory.createXYLineChart("Zaplatená suma stratégia C",
                "Počet replikacii", "Priemerna zaplatena suma za 10 rokov", dataset3,
                PlotOrientation.VERTICAL, false, true, false);
        plot3 = chart3.getXYPlot();

        NumberAxis domainAxis3 = (NumberAxis) plot3.getDomainAxis();
        domainAxis3.setAutoRange(true);

        NumberAxis rangeAxis3 = (NumberAxis) plot3.getRangeAxis();
        rangeAxis3.setAutoRange(true);
        rangeAxis3.setAutoRangeIncludesZero(false);

        chartPanel3 = new ChartPanel(chart3);
        this.panelGraphC.setLayout(new BorderLayout());
        this.panelGraphC.add(chartPanel3, BorderLayout.CENTER);
        this.panelGraphC.validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonStart) {
            if (this.textFieldSeed.getText().isEmpty()) {
                seedGen = new Random();
            } else {
                seedGen = new Random(Integer.parseInt(this.textFieldSeed.getText()));
            }

            sem1A = new Sem1(seedGen, new int[]{5, 3, 1, 1});
            sem1B = new Sem1(seedGen, new int[]{3, 3, 3, 1});
            sem1C = new Sem1(seedGen, new int[]{3, 1, 5, 1});

            buttonStart.setEnabled(false);
            buttonEnd.setEnabled(true);

            sem1A.setRunning(true);
            sem1A.registerDelegate(this);
            sem1B.setRunning(true);
            sem1B.registerDelegate(this);
            sem1C.setRunning(true);
            sem1C.registerDelegate(this);

            sem1A.setExecutedReplications(0);
            sem1B.setExecutedReplications(0);
            sem1C.setExecutedReplications(0);

            isRunning = true;

            this.series1.clear();
            this.series2.clear();
            this.series3.clear();

            this.replicationCount = Integer.parseInt(textFieldReplications.getText());

            if (replicationCount <= 100) {
                this.updateFrequency = 1;
            } else if (replicationCount <= 1000) {
                this.updateFrequency = 10;
            } else if (replicationCount <= 10000) {
                this.updateFrequency = 100;
            } else if (replicationCount <= 100000) {
                this.updateFrequency = 1000;
            } else if (replicationCount <= 1000000) {
                this.updateFrequency = 10000;
            } else {
                this.updateFrequency = 100000;
            }

            Thread threadSem1A = new Thread(new Runnable() {
                public void run() {
                    sem1A.executeReplications(Integer.parseInt(textFieldReplications.getText()));
                }
            });
            threadSem1A.start();

            Thread threadSem1B = new Thread(new Runnable() {
                public void run() {
                    sem1B.executeReplications(Integer.parseInt(textFieldReplications.getText()));
                }
            });
            threadSem1B.start();

            Thread threadSem1C = new Thread(new Runnable() {
                public void run() {
                    sem1C.executeReplications(Integer.parseInt(textFieldReplications.getText()));
                }
            });
            threadSem1C.start();


        } else if (e.getSource() == buttonEnd) {
            buttonStart.setEnabled(true);
            buttonEnd.setEnabled(false);

            sem1A.setRunning(false);
            sem1B.setRunning(false);
            sem1C.setRunning(false);

            isRunning = false;
        }
    }

    @Override
    public void refresh() {
        if (isRunning) {
            this.updateChartAndLabels(sem1A, series1, chart1, labelPaidA, labelRepA, updateFrequency, textFieldPercReplications);
            this.updateChartAndLabels(sem1B, series2, chart2, labelPaidB, labelRepB, updateFrequency, textFieldPercReplications);
            this.updateChartAndLabels(sem1C, series3, chart3, labelPaidC, labelRepC, updateFrequency, textFieldPercReplications);

            if (sem1A.isRunning() == false & sem1B.isRunning() == false && sem1C.isRunning() == false) {
                this.isRunning = false;
                this.buttonStart.setEnabled(true);
                this.buttonEnd.setEnabled(false);
            }
        }
    }

    private void updateChartAndLabels(Sem1 sem, XYSeries series, JFreeChart chart, JLabel labelPaid, JLabel labelRep, double updateFrequency, JTextField textFieldPercReplications) {
        if (sem.isRunning() && sem.getExecutedReplications() % updateFrequency == 0 && series != null && chart != null) {
            labelPaid.setText(Double.toString(sem.getResult()));
            labelRep.setText(Integer.toString(sem.getExecutedReplications()));

            series.add(sem.getExecutedReplications(), sem.getResult());
            chart.fireChartChanged();

            XYPlot plot = chart.getXYPlot();
            NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
            domainAxis.setFixedAutoRange(sem.getExecutedReplications() * (Double.parseDouble(textFieldPercReplications.getText()) / 100.0));
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }
}
