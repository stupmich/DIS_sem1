package GUI;

import Observer.ISimDelegate;
import SimulationClasses.Sem1;
import SimulationClasses.SimulationCore;
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
    private JButton buttonPause;
    private JButton buttonEnd;
    private JButton buttonStart;
    private JPanel panelGraphB;
    private JPanel panelGraphA;
    private JPanel panelGraphC;
    private JLabel labelPaidC;
    private JLabel labelPaidB;
    private JLabel labelPaidA;
    private JPanel panelStratA;
    private JTabbedPane tabbedPane2;
    private JLabel labelRepA;
    private JLabel labelRepB;
    private JLabel labelRepC;
    private JTextField textFieldPercReplications;

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

    private Random baseGen;
    private Sem1 sem1A;
    private Sem1 sem1B;
    private Sem1 sem1C;
    private boolean isRunning = false;

    public GUI_Sem1(){
        this.setContentPane(mainPanel);
        this.setTitle("Simulácia hypoteka");
        this.setSize(1366,768);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.buttonEnd.addActionListener(this);
        //this.buttonPause.addActionListener(this);
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

        baseGen = new Random();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonStart) {
            sem1A = new Sem1(baseGen, new int[]{5, 3, 1, 1});
            sem1B = new Sem1(baseGen, new int[]{3, 3, 3, 1});
            sem1C = new Sem1(baseGen, new int[]{3, 1, 5, 1});

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

            Thread threadSem1A = new Thread(new Runnable() {
                public void run() {
                    sem1A.executeReplications(Integer.parseInt(textFieldReplications.getText()), series1, chart1);
//                    series1.add(sem1A.getExecutedReplications(), sem1A.getResult());
//                    chart1.fireChartChanged();
                }
            });
            threadSem1A.start();

            Thread threadSem1B = new Thread(new Runnable() {
                public void run() {
                    sem1B.executeReplications(Integer.parseInt(textFieldReplications.getText()), series2, chart2);
//                    series2.add(sem1B.getExecutedReplications(), sem1B.getResult());
//                    chart2.fireChartChanged();
                }
            });
            threadSem1B.start();

            Thread threadSem1C = new Thread(new Runnable() {
                public void run() {
                    sem1C.executeReplications(Integer.parseInt(textFieldReplications.getText()), series3, chart3);
//                    series3.add(sem1C.getExecutedReplications(), sem1C.getResult());
//                    chart3.fireChartChanged();
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
    public void refresh(SimulationCore simulation) {
        if (isRunning) {
            this.labelPaidA.setText(Double.toString(sem1A.getResult()));
            this.labelPaidB.setText(Double.toString(sem1B.getResult()));
            this.labelPaidC.setText(Double.toString(sem1C.getResult()));

            this.labelRepA.setText(Integer.toString(sem1A.getExecutedReplications()));
            this.labelRepB.setText(Integer.toString(sem1B.getExecutedReplications()));
            this.labelRepC.setText(Integer.toString(sem1C.getExecutedReplications()));

            XYPlot plot1 = chart1.getXYPlot();
            NumberAxis domainAxis1 = (NumberAxis) plot1.getDomainAxis();
            domainAxis1.setFixedAutoRange(sem1A.getExecutedReplications() * (Double.parseDouble(textFieldPercReplications.getText()) / 100.0));

            XYPlot plot2 = chart2.getXYPlot();
            NumberAxis domainAxis2 = (NumberAxis) plot2.getDomainAxis();
            domainAxis2.setFixedAutoRange(sem1B.getExecutedReplications() * (Double.parseDouble(textFieldPercReplications.getText()) / 100.0));

            XYPlot plot3 = chart3.getXYPlot();
            NumberAxis domainAxis3 = (NumberAxis) plot3.getDomainAxis();
            domainAxis3.setFixedAutoRange(sem1C.getExecutedReplications() * (Double.parseDouble(textFieldPercReplications.getText()) / 100.0));
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }
}
