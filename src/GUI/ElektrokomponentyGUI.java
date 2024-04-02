package GUI;

import Entities.Customer;
import Entities.Worker;
import Observer.ISimDelegate;
import SimulationClasses.Sem2;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class ElektrokomponentyGUI extends JFrame implements ActionListener, ISimDelegate, ChangeListener {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTextField textFieldReplications;
    private JTextField textFieldCashWatchTime;
    private JTextField textFieldOrderPlacesWatchTime;
    private JSlider sliderSpeed;
    private JLabel timeUser;
    private JLabel timeProgrammer;
    private JLabel executedReplications;
    private JLabel numberWorkers1;
    private JLabel numberWorkers2;
    private JLabel numberCustomersAccept;
    private JLabel numberCarsGarage;
    private JLabel numberCustomersPayment;
    private JLabel averageTimeSystem;
    private JLabel ISAverageTimeSystem;
    private JLabel averageTimeAccept;
    private JLabel averageCountFreeWorkers1;
    private JLabel averageCountFreeWorkers2;
    private JLabel averageCountCustomersAfterEnd;
    private JButton startWatchTimeButton;
    private JButton pauseWatchTimeButton;
    private JButton endWatchTimeButton;
    private JPanel panelButtons;
    private JPanel panelTablesWorkersOrder;
    private JTable tableFreeW1;
    private JTable tableWorkingW1;
    private JPanel panelTablesWorkersPayment;
    private JTable tableFreeW2;
    private JTable tableWorkingW2;
    private JPanel panelTablesQueues;
    private JTable tableTicketQueue;
    private JTable tableOrderQueue;
    private JTable tablePaymentQueue;
    private JButton startTurboButton;
    private JButton pauseTurboButton;
    private JButton endTurboButton;
    private JTextField textFieldCashTurbo;
    private JTextField textFieldOrderPlacesTurbo;
    private JTextField textFieldReplicationsTurbo;
    private JLabel executedReplicationsTurbo;
    private Sem2 simulation;
    private boolean turboMode;
    private DefaultTableModel modelWorkersOrder;
    private DefaultTableModel modelWorkersPayment;
    private DefaultTableModel modelWorkersOrderW;
    private DefaultTableModel modelWorkersPaymentW;
    private DefaultTableModel modelCustomersWaitingTicket;
    private DefaultTableModel modelCustomersWaitingOrder;
    private DefaultTableModel modelCustomersPayment;



    public ElektrokomponentyGUI() {
        this.setContentPane(panel1);
        this.setTitle("Simulácia elektra");
        this.setSize(1920,1080);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelTablesWorkersOrder.setPreferredSize(new Dimension(500,200));
        panelTablesWorkersPayment.setPreferredSize(new Dimension(500,200));
        panelTablesQueues.setPreferredSize(new Dimension(500,200));

        modelWorkersOrder = new DefaultTableModel();
        modelWorkersOrder.addColumn("Worker ID");
        modelWorkersOrder.addColumn("Customer ID");
        tableFreeW1.setModel(modelWorkersOrder);

        modelWorkersOrderW = new DefaultTableModel();
        modelWorkersOrderW.addColumn("Worker ID");
        modelWorkersOrderW.addColumn("Customer ID");
        tableWorkingW1.setModel(modelWorkersOrderW);

        modelWorkersPayment = new DefaultTableModel();
        modelWorkersPayment.addColumn("Worker ID");
        modelWorkersPayment.addColumn("Customer ID");
        tableFreeW2.setModel(modelWorkersPayment);

        modelWorkersPaymentW = new DefaultTableModel();
        modelWorkersPaymentW.addColumn("Worker ID");
        modelWorkersPaymentW.addColumn("Customer ID");
        tableWorkingW2.setModel(modelWorkersPaymentW);

        modelCustomersWaitingTicket = new DefaultTableModel();
        modelCustomersWaitingTicket.addColumn("Customer ID");
        tableTicketQueue.setModel(modelCustomersWaitingTicket);

        modelCustomersWaitingOrder = new DefaultTableModel();
        modelCustomersWaitingOrder.addColumn("Customer ID");
        tableOrderQueue.setModel(modelCustomersWaitingOrder);

        modelCustomersPayment = new DefaultTableModel();
        modelCustomersPayment.addColumn("Customer ID");
        tablePaymentQueue.setModel(modelCustomersPayment);

        this.startTurboButton.addActionListener(this);
        this.startWatchTimeButton.addActionListener(this);
        //this.startGraphButton.addActionListener(this);
        this.pauseWatchTimeButton.addActionListener(this);
        this.pauseTurboButton.addActionListener(this);
        //this.pauseButtonGraph.addActionListener(this);
        this.endWatchTimeButton.addActionListener(this);
        this.endTurboButton.addActionListener(this);
        //this.endButtonGraph.addActionListener(this);
        this.sliderSpeed.addChangeListener(this);
    }

    @Override
    public void refresh() {
        if (turboMode) {
            this.executedReplicationsTurbo.setText(Integer.toString(simulation.getExecutedReplications()));
            //averageTimeAcceptTurbo.setText(Double.toString(simulation.getAverageWaitingTimeAccept()/60.0));
            //averageTimeSystemTurbo.setText(Double.toString(simulation.getAverageTimeSystem()/60.0));
            //averageCountAcceptTurbo.setText(Double.toString(simulation.getAverageCountCustomersWaitingAccept()));
            //this.averageCountFreeWorkers1Turbo.setText(Double.toString(simulation.getAverageCountFreeWorkersType1()));
            //this.averageCountFreeWorkers2Turbo.setText(Double.toString(simulation.getAverageCountFreeWorkersType2()));
            //this.averageNumberCustomersSystemTurbo.setText(Double.toString(simulation.getAverageCountCustomersSystem()));
//            this.ISAverageTimeSystemTurbo.setText("<" + Double.toString(simulation.getIsCustomersTimeSystemLow() / 60.0)
//                    + ";" + Double.toString(simulation.getIsCustomersTimeSystemHigh() / 60.0)
//                    + ">");
//            this.ISAverageNumberCustomersSystemTurbo.setText("<" + Double.toString(simulation.getIsCountCustomersSystemLow())
//                    + ";" + Double.toString(simulation.getIsCountCustomersSystemHigh())
//                    + ">");
//            this.averageCountCustomersAfterEndTurbo.setText(Double.toString(simulation.getAverageCarsAfterEnd()));
        } else {
            this.timeProgrammer.setText(Double.toString(simulation.getCurrentTime()));
            // Convert seconds to hours, minutes and remaining seconds
            long hours = TimeUnit.SECONDS.toHours((long) simulation.getCurrentTime());
            long minutes = TimeUnit.SECONDS.toMinutes((long) simulation.getCurrentTime()) - (hours * 60);
            long remainingSeconds = (long) (simulation.getCurrentTime() - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes));
            // Format the time as "hours:minutes:seconds"
            String time = String.format("%d:%02d:%02d", hours, minutes, remainingSeconds);
            this.timeUser.setText(time);

//            this.numberWorkers1.setText(Integer.toString(simulation.getWorkersOrderNormal().size() + simulation.getWorkersOrderOnline().size()));
//            this.numberWorkers2.setText(Integer.toString(simulation.getWorkersPayment().size()));
//            this.numberCustomersAccept.setText(Integer.toString(simulation.getQueueCustomersWaiting().size()));
//            this.numberCarsGarage.setText(Integer.toString(simulation.getPlacesInGarage().size()));
//            this.numberCustomersPayment.setText(Integer.toString(simulation.getQueueCustomersPayment().size()));

            this.executedReplications.setText(Integer.toString(simulation.getExecutedReplications()));

            try {
                SwingUtilities.invokeAndWait(new Runnable(){public void run(){
                    modelWorkersOrder.setRowCount(0);
                    for (Worker worker : simulation.getWorkersOrderNormal()) {
                        if (worker.getIdCustomer() == -1) {
                            modelWorkersOrder.addRow(new Object[]{worker.getId(),"Free"});
                        } else {
                            modelWorkersOrder.addRow(new Object[]{worker.getId(),Integer.toString(worker.getIdCustomer()) });
                        }
                    }
                    for (Worker worker : simulation.getWorkersOrderOnline()) {
                        if (worker.getIdCustomer() == -1) {
                            modelWorkersOrder.addRow(new Object[]{worker.getId(),"Free"});
                        } else {
                            modelWorkersOrder.addRow(new Object[]{worker.getId(),Integer.toString(worker.getIdCustomer()) });
                        }
                    }

                    modelWorkersOrderW.setRowCount(0);
                    for (Worker worker : simulation.getWorkersOrderWorkingNormal()) {
                        modelWorkersOrderW.addRow(new Object[]{worker.getId(),Integer.toString(worker.getIdCustomer()) });
                    }

                    for (Worker worker : simulation.getWorkersOrderWorkingOnline()) {
                        modelWorkersOrderW.addRow(new Object[]{worker.getId(),Integer.toString(worker.getIdCustomer()) });
                    }

                    modelWorkersPayment.setRowCount(0);
                    for (Worker worker : simulation.getWorkersPayment()) {
                        if (worker.getIdCustomer() == -1) {
                            modelWorkersPayment.addRow(new Object[]{worker.getId(),"Free"});

                        } else {
                            modelWorkersPayment.addRow(new Object[]{worker.getId(),Integer.toString(worker.getIdCustomer()) });
                        }
                    }

                    modelWorkersPaymentW.setRowCount(0);
                    for (Worker worker : simulation.getWorkersPaymentWorking()) {
                        modelWorkersPaymentW.addRow(new Object[]{worker.getId(),Integer.toString(worker.getIdCustomer()) });
                    }

                    modelCustomersWaitingTicket.setRowCount(0);
                    for (Customer customer : simulation.getQueueCustomersWaitingTicketDispenser()) {
                        modelCustomersWaitingTicket.addRow(new Object[]{ ((Customer) customer).getId() });
                    }

                    modelCustomersWaitingOrder.setRowCount(0);
                    for (Customer customer : simulation.getCustomersWaitingInShopBeforeOrder()) {
                        modelCustomersWaitingOrder.addRow(new Object[]{ ((Customer) customer).getId() });
                    }

                    modelCustomersPayment.setRowCount(0);
                    for (Customer customer : simulation.getCustomersPaying()) {
                        modelCustomersPayment.addRow(new Object[]{ ((Customer) customer).getId() });
                    }

                    for (LinkedList<Customer> queue : simulation.getQueuesCustomersWaitingForPayment()) {
                        for (Customer customer : queue) {
                            modelCustomersPayment.addRow(new Object[]{ ((Customer) customer).getId() });
                        }
                    }
                }});
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

//            averageTimeAccept.setText(Double.toString(simulation.getAverageWaitingTimeAccept()/60.0));
//            averageTimeSystem.setText(Double.toString(simulation.getAverageTimeSystem()/60.0));
//            averageCountAccept.setText(Double.toString(simulation.getAverageCountCustomersWaitingAccept()));
//            this.averageCountFreeWorkers1.setText(Double.toString(simulation.getAverageCountFreeWorkersType1()));
//            this.averageCountFreeWorkers2.setText(Double.toString(simulation.getAverageCountFreeWorkersType2()));
//            this.averageNumberCustomersSystem.setText(Double.toString(simulation.getAverageCountCustomersSystem()));
//            this.ISAverageNumberCustomersSystem.setText("<" + Double.toString(simulation.getIsCountCustomersSystemLow())
//                    + ";" + Double.toString(simulation.getIsCountCustomersSystemHigh())
//                    + ">");
//            this.ISAverageTimeSystem.setText("<" + Double.toString(simulation.getIsCustomersTimeSystemLow() / 60.0)
//                    + ";" + Double.toString(simulation.getIsCustomersTimeSystemHigh() / 60.0)
//                    + ">");
//            this.averageCountCustomersAfterEnd.setText(Double.toString(simulation.getAverageCarsAfterEnd()));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startTurboButton) {
            this.turboMode = true;
            startTurboButton.setEnabled(false);
            startWatchTimeButton.setEnabled(false);
            pauseTurboButton.setEnabled(true);
            pauseWatchTimeButton.setEnabled(false);
            endTurboButton.setEnabled(true);
            endWatchTimeButton.setEnabled(false);
            //this.startGraphButton.setEnabled(false);
            //this.pauseButtonGraph.setEnabled(false);
            //this.endButtonGraph.setEnabled(false);

            simulation = new Sem2(Integer.parseInt(textFieldOrderPlacesTurbo.getText()),Integer.parseInt(textFieldCashTurbo.getText()),30600.0,true,0);
            simulation.setRunning(true);
            simulation.registerDelegate(this);

            Thread threadSimulation = new Thread(new Runnable() {
                public void run() {
                    simulation.executeReplications(Integer.parseInt(textFieldReplicationsTurbo.getText()));
                }
            });
            threadSimulation.start();

        } else if (e.getSource() == startWatchTimeButton) {
            this.turboMode = false;
            startTurboButton.setEnabled(false);
            startWatchTimeButton.setEnabled(false);
            pauseWatchTimeButton.setEnabled(true);
            endWatchTimeButton.setEnabled(true);
            pauseTurboButton.setEnabled(false);
            endTurboButton.setEnabled(false);
//            this.startGraphButton.setEnabled(false);
//            this.pauseButtonGraph.setEnabled(false);
//            this.endButtonGraph.setEnabled(false);

            simulation = new Sem2(Integer.parseInt(textFieldOrderPlacesWatchTime.getText()),Integer.parseInt(textFieldCashWatchTime.getText()),30600.0,false, this.sliderSpeed.getValue());
            simulation.setRunning(true);
            simulation.registerDelegate(this);

            for (Worker worker : simulation.getWorkersOrderWorkingNormal()) {
                modelWorkersOrder.addRow(new Object[]{worker.getId(),"Free"});
            }
            for (Worker worker : simulation.getWorkersOrderWorkingOnline()) {
                modelWorkersOrder.addRow(new Object[]{worker.getId(),"Free"});
            }

            for (Worker worker : simulation.getWorkersPayment()) {
                modelWorkersPayment.addRow(new Object[]{worker.getId(),"Free"});
            }

            Thread threadSimulation = new Thread(new Runnable() {
                public void run() {
                    simulation.executeReplications(Integer.parseInt(textFieldReplications.getText()));
                }
            });
            threadSimulation.start();

        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == this.sliderSpeed) {
            if (simulation != null) {
                simulation.setTimeGap(this.sliderSpeed.getValue());
            }
        }
    }
}
