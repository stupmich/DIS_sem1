package GUI;

import NewsPaperStand.Customer;
import Observer.ISimDelegate;
import STKObjects.CustomerSTK;
import STKObjects.WorkerSTK;
import SimulationClasses.SimulationCore;
import SimulationClasses.SimulationSTK;
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

    private SimulationSTK simulationSTK;
    private SimulationSTK simulationSTK2;
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
        if (e.getSource() == startTURBOButton) {
            this.turboMode = true;
            startTURBOButton.setEnabled(false);
            startWatchTimeButton.setEnabled(false);
            pauseButtonTurbo.setEnabled(true);
            pauseButton.setEnabled(false);
            endButtonTurbo.setEnabled(true);
            endButton.setEnabled(false);
            this.startGraphButton.setEnabled(false);
            this.pauseButtonGraph.setEnabled(false);
            this.buttonEnd.setEnabled(false);

            simulationSTK = new SimulationSTK(28800.0,Integer.parseInt(textFieldWorkers1Turbo.getText()),Integer.parseInt(textFieldWorkers2Turbo.getText()), true,0);
            simulationSTK.setRunning(true);
            simulationSTK.registerDelegate(this);

            Thread threadSimulation = new Thread(new Runnable() {
                public void run() {
                    simulationSTK.executeReplications(Integer.parseInt(textFieldReplicationsTurbo.getText()), null, null, null,null);
                }
            });
            threadSimulation.start();

        } else if (e.getSource() == startWatchTimeButton) {
            this.turboMode = false;
            startTURBOButton.setEnabled(false);
            startWatchTimeButton.setEnabled(false);
            pauseButton.setEnabled(true);
            endButton.setEnabled(true);
            pauseButtonTurbo.setEnabled(false);
            endButtonTurbo.setEnabled(false);
            this.startGraphButton.setEnabled(false);
            this.pauseButtonGraph.setEnabled(false);
            this.buttonEnd.setEnabled(false);

            simulationSTK = new SimulationSTK(28800.0,Integer.parseInt(textFieldWorkers1.getText()),Integer.parseInt(textFieldWorkers2.getText()), false, this.sliderSpeed.getValue());
            simulationSTK.setRunning(true);
            simulationSTK.registerDelegate(this);

            for (WorkerSTK worker : simulationSTK.getWorkersType1()) {
                modelWorkersType1.addRow(new Object[]{worker.getId(),"Free"});
            }

            for (WorkerSTK worker : simulationSTK.getWorkersType2()) {
                modelWorkersType2.addRow(new Object[]{worker.getId(),"Free"});
            }

            Thread threadSimulation = new Thread(new Runnable() {
                public void run() {
                    simulationSTK.executeReplications(Integer.parseInt(textFieldReplications.getText()), null, null, null,null);
                }
            });
            threadSimulation.start();

        } else if (e.getSource() == startGraphButton) {
            this.series1.clear();
            this.series2.clear();

            this.turboMode = true;
            startTURBOButton.setEnabled(false);
            startWatchTimeButton.setEnabled(false);
            pauseButtonTurbo.setEnabled(false);
            pauseButton.setEnabled(false);
            endButtonTurbo.setEnabled(false);
            endButton.setEnabled(false);

            this.startGraphButton.setEnabled(false);
            this.pauseButtonGraph.setEnabled(true);
            this.buttonEnd.setEnabled(true);

            threadSimulationOuter1 = new Thread(new Runnable() {
                public void run() {
                    for(int i = 1; i <= 15; i++) {
                        simulationSTK = new SimulationSTK(28800.0,i,Integer.parseInt(textFieldWorkers2Graph.getText()), true,0);
                        simulationSTK.setRunning(true);

                        threadSimulationInner1 = new Thread(new Runnable() {
                            public void run() {
                                simulationSTK.executeReplications(Integer.parseInt(textFieldReplicationsGraph.getText()), null, null, null,null);
                                series1.add(simulationSTK.getNumberOfWorkersType1(), simulationSTK.getAverageCountCustomersWaitingAccept());
                                chart1.fireChartChanged();
                            }
                        });
                        threadSimulationInner1.start();

                        while(threadSimulationInner1.isAlive()) {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException ex) {
                                return;
                            }
                        }

                    }
                }
            });
            threadSimulationOuter1.start();

            threadSimulationOuter2 = new Thread(new Runnable() {
                public void run() {
                    for(int i = 10; i <= 25; i++) {
                        simulationSTK2 = new SimulationSTK(28800.0,Integer.parseInt(textFieldWorkers1Graph.getText()),i, true,0);
                        simulationSTK2.setRunning(true);

                        threadSimulationInner2 = new Thread(new Runnable() {
                            public void run() {
                                simulationSTK2.executeReplications(Integer.parseInt(textFieldReplicationsGraph.getText()), null, null, null,null);
                                series2.add(simulationSTK2.getNumberOfWorkersType2(), simulationSTK2.getAverageTimeSystem() / 60.0);
                                chart2.fireChartChanged();
                            }
                        });
                        threadSimulationInner2.start();

                        while(threadSimulationInner2.isAlive()) {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException ex) {
                                return;
                            }
                        }

                    }
                }
            });
            threadSimulationOuter2.start();

        } else if (e.getSource() == pauseButton ) {
            if (simulationSTK.isPause()) {
                simulationSTK.setPause(false);
            } else {
                simulationSTK.setPause(true);
            }
        } else if (e.getSource() == endButton) {
            startTURBOButton.setEnabled(true);
            startWatchTimeButton.setEnabled(true);
            pauseButton.setEnabled(false);
            endButton.setEnabled(false);
            this.startGraphButton.setEnabled(true);
            this.pauseButtonGraph.setEnabled(false);
            this.buttonEnd.setEnabled(false);

            simulationSTK.setRunning(false);
            simulationSTK.setExecutedReplications(0);
        } else if (e.getSource() == pauseButtonGraph) {
            if (simulationSTK.isPause()) {
                simulationSTK.setPause(false);
            } else {
                simulationSTK.setPause(true);
            }

            if (simulationSTK2.isPause()) {
                simulationSTK2.setPause(false);
            } else {
                simulationSTK2.setPause(true);
            }
        }  else if (e.getSource() == pauseButtonTurbo) {
            if (simulationSTK.isPause()) {
                simulationSTK.setPause(false);
            } else {
                simulationSTK.setPause(true);
            }
        } else if (e.getSource() == endButtonTurbo) {
            startTURBOButton.setEnabled(true);
            startWatchTimeButton.setEnabled(true);
            pauseButtonTurbo.setEnabled(false);
            endButtonTurbo.setEnabled(false);
            this.startGraphButton.setEnabled(true);
            this.pauseButtonGraph.setEnabled(false);
            this.buttonEnd.setEnabled(false);

            simulationSTK.setRunning(false);
            simulationSTK.setExecutedReplications(0);
        } else if (e.getSource() == buttonEnd) {
            startTURBOButton.setEnabled(true);
            startWatchTimeButton.setEnabled(true);
            pauseButtonTurbo.setEnabled(false);
            endButtonTurbo.setEnabled(false);
            this.startGraphButton.setEnabled(true);
            this.pauseButtonGraph.setEnabled(false);
            this.buttonEnd.setEnabled(false);

            simulationSTK.setRunning(false);
            simulationSTK2.setRunning(false);
            simulationSTK.setExecutedReplications(0);
            simulationSTK2.setExecutedReplications(0);
            this.threadSimulationInner1.interrupt();
            this.threadSimulationInner2.interrupt();
            this.threadSimulationOuter1.interrupt();
            this.threadSimulationOuter2.interrupt();
        }
    }

    @Override
    public void refresh(SimulationCore simulation) {
        if (turboMode) {
            this.executedReplicationsTurbo.setText(Integer.toString(simulationSTK.getExecutedReplications()));
            averageTimeAcceptTurbo.setText(Double.toString(simulationSTK.getAverageWaitingTimeAccept()/60.0));
            averageTimeSystemTurbo.setText(Double.toString(simulationSTK.getAverageTimeSystem()/60.0));
            averageCountAcceptTurbo.setText(Double.toString(simulationSTK.getAverageCountCustomersWaitingAccept()));
            this.averageCountFreeWorkers1Turbo.setText(Double.toString(simulationSTK.getAverageCountFreeWorkersType1()));
            this.averageCountFreeWorkers2Turbo.setText(Double.toString(simulationSTK.getAverageCountFreeWorkersType2()));
            this.averageNumberCustomersSystemTurbo.setText(Double.toString(simulationSTK.getAverageCountCustomersSystem()));
            this.ISAverageTimeSystemTurbo.setText("<" + Double.toString(simulationSTK.getIsCustomersTimeSystemLow() / 60.0)
                    + ";" + Double.toString(simulationSTK.getIsCustomersTimeSystemHigh() / 60.0)
                    + ">");
            this.ISAverageNumberCustomersSystemTurbo.setText("<" + Double.toString(simulationSTK.getIsCountCustomersSystemLow())
                                                            + ";" + Double.toString(simulationSTK.getIsCountCustomersSystemHigh())
                                                            + ">");
            this.averageCountCustomersAfterEndTurbo.setText(Double.toString(simulationSTK.getAverageCarsAfterEnd()));
        } else {
            this.timeProgrammer.setText(Double.toString(simulationSTK.getCurrentTime()));
            // Convert seconds to hours, minutes and remaining seconds
            long hours = TimeUnit.SECONDS.toHours((long) simulationSTK.getCurrentTime());
            long minutes = TimeUnit.SECONDS.toMinutes((long) simulationSTK.getCurrentTime()) - (hours * 60);
            long remainingSeconds = (long) (simulationSTK.getCurrentTime() - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes));
            // Format the time as "hours:minutes:seconds"
            String time = String.format("%d:%02d:%02d", hours, minutes, remainingSeconds);
            this.timeUser.setText(time);

            this.numberWorkers1.setText(Integer.toString(simulationSTK.getWorkersType1().size()));
            this.numberWorkers2.setText(Integer.toString(simulationSTK.getWorkersType2().size()));
            this.numberCustomersAccept.setText(Integer.toString(simulationSTK.getQueueCustomersWaiting().size()));
            this.numberCarsGarage.setText(Integer.toString(simulationSTK.getPlacesInGarage().size()));
            this.numberCustomersPayment.setText(Integer.toString(simulationSTK.getQueueCustomersPayment().size()));

            this.executedReplications.setText(Integer.toString(simulationSTK.getExecutedReplications()));

            try {
                SwingUtilities.invokeAndWait(new Runnable(){public void run(){
                    modelWorkersType1.setRowCount(0);
                    for (WorkerSTK worker : simulationSTK.getWorkersType1()) {
                        if (worker.getIdCustomer() == -1) {
                            modelWorkersType1.addRow(new Object[]{worker.getId(),"Free"});

                        } else {
                            modelWorkersType1.addRow(new Object[]{worker.getId(),Integer.toString(worker.getIdCustomer()) });
                        }
                    }

                    modelWorkersType1W.setRowCount(0);
                    for (WorkerSTK worker : simulationSTK.getWorkersType1Working()) {
                        modelWorkersType1W.addRow(new Object[]{worker.getId(),Integer.toString(worker.getIdCustomer()) });
                    }

                    modelWorkersType2.setRowCount(0);
                    for (WorkerSTK worker : simulationSTK.getWorkersType2()) {
                        if (worker.getIdCustomer() == -1) {
                            modelWorkersType2.addRow(new Object[]{worker.getId(),"Free"});

                        } else {
                            modelWorkersType2.addRow(new Object[]{worker.getId(),Integer.toString(worker.getIdCustomer()) });
                        }
                    }

                    modelWorkersType2W.setRowCount(0);
                    for (WorkerSTK worker : simulationSTK.getWorkersType2Working()) {
                        modelWorkersType2W.addRow(new Object[]{worker.getId(),Integer.toString(worker.getIdCustomer()) });
                    }

                    modelCustomersWaiting.setRowCount(0);
                    for (Customer customer : simulationSTK.getQueueCustomersWaiting()) {
                        modelCustomersWaiting.addRow(new Object[]{ ((CustomerSTK) customer).getId() });
                    }

                    modelCustomersPayment.setRowCount(0);
                    for (Customer customer : simulationSTK.getQueueCustomersPayment()) {
                        modelCustomersPayment.addRow(new Object[]{ ((CustomerSTK) customer).getId() });
                    }

                    modelGarage.setRowCount(0);
                    for (Customer customer : simulationSTK.getPlacesInGarage()) {
                        modelGarage.addRow(new Object[]{ ((CustomerSTK) customer).getId() });
                    }
                }});
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            averageTimeAccept.setText(Double.toString(simulationSTK.getAverageWaitingTimeAccept()/60.0));
            averageTimeSystem.setText(Double.toString(simulationSTK.getAverageTimeSystem()/60.0));
            averageCountAccept.setText(Double.toString(simulationSTK.getAverageCountCustomersWaitingAccept()));
            this.averageCountFreeWorkers1.setText(Double.toString(simulationSTK.getAverageCountFreeWorkersType1()));
            this.averageCountFreeWorkers2.setText(Double.toString(simulationSTK.getAverageCountFreeWorkersType2()));
            this.averageNumberCustomersSystem.setText(Double.toString(simulationSTK.getAverageCountCustomersSystem()));
            this.ISAverageNumberCustomersSystem.setText("<" + Double.toString(simulationSTK.getIsCountCustomersSystemLow())
                    + ";" + Double.toString(simulationSTK.getIsCountCustomersSystemHigh())
                    + ">");
            this.ISAverageTimeSystem.setText("<" + Double.toString(simulationSTK.getIsCustomersTimeSystemLow() / 60.0)
                    + ";" + Double.toString(simulationSTK.getIsCustomersTimeSystemHigh() / 60.0)
                    + ">");
            this.averageCountCustomersAfterEnd.setText(Double.toString(simulationSTK.getAverageCarsAfterEnd()));
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == this.sliderSpeed) {
            if (simulationSTK != null) {
                simulationSTK.setTimeGap(this.sliderSpeed.getValue());
            }
        }
    }
}
