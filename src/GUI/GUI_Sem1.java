package GUI;

import Observer.ISimDelegate;
import SimulationClasses.SimulationCore;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

public class GUI_Sem1 extends JFrame implements ActionListener, ISimDelegate, ChangeListener {
    private JPanel mainPanel;
    private JTextField textFieldReplications;
    private JButton buttonPause;
    private JButton buttonEnd;
    private JButton buttonStart;
    private JTabbedPane tabbedPane2;
    private JPanel panelStratA;
    private JPanel panelGraphC;
    private JPanel panelGraphB;
    private JPanel panelGraphA;
    private JTextField textFieldPaidA;
    private JTextField textFieldPaidB;
    private JTextField textFieldPaidC;
    private boolean turboMode;
    private DefaultTableModel modelWorkersType1;
    private DefaultTableModel modelWorkersType2;
    private DefaultTableModel modelWorkersType1W;
    private DefaultTableModel modelWorkersType2W;
    private DefaultTableModel modelCustomersWaiting;
    private DefaultTableModel modelCustomersPayment;
    private DefaultTableModel modelGarage;

    private XYSeries series1 = new XYSeries("Average Queue Length");
    private XYPlot plot1;
    private XYSeriesCollection dataset1 = new XYSeriesCollection(series1);
    private JFreeChart chart1;
    private ChartPanel chartPanel1;

    private XYSeries series2 = new XYSeries("Average Time in System");
    private XYPlot plot2;
    private XYSeriesCollection dataset2 = new XYSeriesCollection(series2);
    private JFreeChart chart2;
    private ChartPanel chartPanel2;

    private Thread threadSimulationOuter1;
    private Thread threadSimulationOuter2;
    private Thread threadSimulationInner1;
    private Thread threadSimulationInner2;

    public GUI_Sem1(){
        this.setContentPane(mainPanel);
        this.setTitle("Simulácia hypoteka");
        this.setSize(1366,768);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.buttonEnd.addActionListener(this);
        this.buttonPause.addActionListener(this);
        this.buttonStart.addActionListener(this);

        chart1 = ChartFactory.createXYLineChart("Zaplatená suma stratégia A",
                "Počet pracovníkov skupiny 1", "Priemerný počet čakajúcich", dataset1,
                PlotOrientation.VERTICAL, false, true, false);
        plot1 = chart1.getXYPlot();

        ValueAxis axis1 = plot1.getDomainAxis();
        axis1.setRange(1.0 , 15.0);

//        NumberAxis domainAxis1 = (NumberAxis) plot1.getDomainAxis();
//        domainAxis1.setAutoRange(true);

        NumberAxis rangeAxis1 = (NumberAxis) plot1.getRangeAxis();
        rangeAxis1.setAutoRange(true);
        rangeAxis1.setAutoRangeIncludesZero(false);

        chartPanel1 = new ChartPanel(chart1);
        this.panelGraphA.setLayout(new BorderLayout());
        this.panelGraphA.add(chartPanel1, BorderLayout.CENTER);
        this.panelGraphA.validate();

        chart2 = ChartFactory.createXYLineChart("Závislosť priemerný čas v prevádzke/počet pracovníkov skupiny 2", "Počet pracovníkov skupiny 2", "Priemerný čas", dataset2,
                PlotOrientation.VERTICAL, false, true, false);
        plot2 = chart2.getXYPlot();

        ValueAxis axis2 = plot2.getDomainAxis();
        axis2.setRange(10, 25);

//        NumberAxis domainAxis2 = (NumberAxis) plot1.getDomainAxis();
//        domainAxis2.setAutoRange(true);

        NumberAxis rangeAxis2 = (NumberAxis) plot1.getRangeAxis();
        rangeAxis2.setAutoRange(true);
        rangeAxis2.setAutoRangeIncludesZero(false);

        chartPanel2 = new ChartPanel(chart2);
        this.panelGraphB.setLayout(new BorderLayout());
        this.panelGraphB.add(chartPanel2, BorderLayout.CENTER);
        this.panelGraphB.validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void refresh(SimulationCore simulation) {

    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }
}
