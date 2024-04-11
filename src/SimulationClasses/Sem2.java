package SimulationClasses;

import Entities.Customer;
import Entities.Worker;
import Events.ArrivalEvent;
import Events.CloseShopEvent;
import Events.LeaveShopEvent;
import Generators.ContinuousEmpiricalDistributionGenerator;
import Generators.ContinuousEmpiricalDistributionParameter;
import Generators.ExponentialDistributionGenerator;
import Generators.TriangularDistributionGenerator;
import Observer.ISimDelegate;
import Statistics.ArithmeticMeanStatistics;
import Statistics.WeightedArithmeticMean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Sem2 extends EventBasedSimulationCore {
    private int numberOfWorkersOrder;
    private int numberOfWorkersPayment;
    private int highestCustomerID;
    private int highestWorkersOrderID;
    private int highestWorkersPaymentID;
    private int numberOfNormalWorkers;
    private int numberOfOnlineWorkers;
    private LinkedList<Worker> workersOrderNormal;
    private LinkedList<Worker> workersOrderOnline;
    private LinkedList<Worker> workersOrderWorkingNormal;
    private LinkedList<Worker> workersOrderWorkingOnline;
    private LinkedList<Worker> workersPayment;
    private LinkedList<Worker> workersPaymentWorking;
    private LinkedList<Customer> queueCustomersWaitingTicketDispenser;
    private LinkedList<Customer> customerInteractingWithTicketDispenser;
    private LinkedList<Customer> customersWaitingInShopBeforeOrder;
    private LinkedList<Customer> customersOrdering;
    private LinkedList<LinkedList<Customer>> queuesCustomersWaitingForPayment;
    private LinkedList<Customer> customersPaying;
    private LinkedList<Customer> allCustomers;
    public LinkedList<Customer> allCustomersOut;
    private Random seedGenerator;
    private ExponentialDistributionGenerator arrivalsGenerator;
    private Random customerTypeGenerator;
    private Random orderTypeGenerator;
    private Random timeTicketDispenserGenerator;
    private Random timeOrderGenerator;
    private TriangularDistributionGenerator timeOrderHandoverGenerator;
    private Random orderSizeGenerator;
    private Random[] indexPaymentSameLengthOfQueueGenerator;
    private Random[] indexPaymentEmptyQueueGenerator;
    private Random timeBigOrderPickUpGenerator;
    private Random paymentTypeGenerator;
    private Random paymentCashGenerator;
    private Random paymentCardGenerator;
    private ContinuousEmpiricalDistributionGenerator timeSimpleOrderPreparationGenerator;
    private Random timeMediumOrderPreparationGenerator;
    private ContinuousEmpiricalDistributionGenerator timeDifficultOrderPreparationGenerator;
    private int customersIn;
    private int customersOut;
    private int servedCustomers;
    private ArithmeticMeanStatistics averageTimeInSystemStat;
    private ArithmeticMeanStatistics averageTimeWaitingServiceStat;
    private ArithmeticMeanStatistics averageTimeTicketStat;
    private ArithmeticMeanStatistics averageServedCustomerStat;
    private ArithmeticMeanStatistics averageTimeLeaveSystemStat;
    private WeightedArithmeticMean numberOfCustomersWaitingTicketStat;
    private WeightedArithmeticMean averageUsePercentTicketStat;
    private WeightedArithmeticMean averageUsePercentOrderNormalStat;
    private WeightedArithmeticMean averageUsePercentOrderOnlineStat;
    private WeightedArithmeticMean averageUsePercentPaymentStat;
    private double averageTimeInSystem;
    private double averageTimeTicket;
    private double averageServedCustomer;
    private double averageNumberOfCustomersWaitingTicket;
    private double averageTimeLeaveSystem;
    private double averageUsePercentTicket;
    private double averageUsePercentOrder;
    private double averageUsePercentPayment;
    private double confIntTimeInSystemLower = 0.0;
    private double confIntTimeInSystemUpper = 0.0;
    private Customer lastCustomer = null;

    public Sem2(int numberOfWorkersOrder, int numberOfWorkersPayment, double maxTime, boolean turboMode, int timeGap) {
        super(maxTime, turboMode, timeGap);
        this.numberOfWorkersOrder = numberOfWorkersOrder;
        this.numberOfWorkersPayment = numberOfWorkersPayment;

        this.workersOrderNormal = new LinkedList<Worker>();
        this.workersOrderOnline = new LinkedList<Worker>();
        this.workersOrderWorkingNormal = new LinkedList<Worker>();
        this.workersOrderWorkingOnline = new LinkedList<Worker>();
        this.workersPayment = new LinkedList<Worker>();
        this.workersPaymentWorking = new LinkedList<Worker>();

        numberOfNormalWorkers = (int) Math.round(this.numberOfWorkersOrder * 2.0 / 3.0);
        numberOfOnlineWorkers = this.numberOfWorkersOrder - numberOfNormalWorkers;

        for (int i = 0; i < numberOfNormalWorkers; i++ ) {
            Worker worker = new Worker(this.highestWorkersOrderID, Worker.WorkerType.ORDER_REGULAR_AND_CONTRACT);
            workersOrderNormal.add(worker);
            this.highestWorkersOrderID++;
        }
        for (int i = 0; i < numberOfOnlineWorkers; i++ ) {
            Worker worker = new Worker(this.highestWorkersOrderID, Worker.WorkerType.ORDER_ONLINE);
            workersOrderOnline.add(worker);
            this.highestWorkersOrderID++;
        }

        for (int i = 0; i < this.numberOfWorkersPayment; i++ ) {
            Worker worker = new Worker(this.highestWorkersPaymentID, Worker.WorkerType.PAYMENT);
            workersPayment.add(worker);
            this.highestWorkersPaymentID++;
        }

        this.queueCustomersWaitingTicketDispenser = new LinkedList<Customer>();
        this.customersWaitingInShopBeforeOrder = new LinkedList<Customer>();
        this.customersOrdering = new LinkedList<Customer>();
        this.queuesCustomersWaitingForPayment = new LinkedList<LinkedList<Customer>>();
        for (int i = 0; i < this.numberOfWorkersPayment; i++) {
            this.queuesCustomersWaitingForPayment.add(new LinkedList<Customer>());
        }
        this.customersPaying = new LinkedList<Customer>();
        this.allCustomers = new LinkedList<Customer>();
        this.allCustomersOut = new LinkedList<Customer>();
        this.customerInteractingWithTicketDispenser = new LinkedList<Customer>();

        this.seedGenerator = new Random();
        double lambda = 1.0 / (60.0/30.0);
        this.arrivalsGenerator = new ExponentialDistributionGenerator(seedGenerator, lambda);
        this.customerTypeGenerator = new Random(seedGenerator.nextInt());
        this.orderTypeGenerator = new Random(seedGenerator.nextInt());
        this.timeTicketDispenserGenerator = new Random(seedGenerator.nextInt());
        this.timeOrderGenerator = new Random(seedGenerator.nextInt());
        this.timeOrderHandoverGenerator = new TriangularDistributionGenerator(seedGenerator, 60.0, 480.0,120.0);
        this.orderSizeGenerator = new Random(seedGenerator.nextInt());
        this.timeBigOrderPickUpGenerator = new Random(seedGenerator.nextInt());
        this.paymentTypeGenerator = new Random(seedGenerator.nextInt());
        this.paymentCashGenerator = new Random(seedGenerator.nextInt());
        this.paymentCardGenerator = new Random(seedGenerator.nextInt());
        this.timeMediumOrderPreparationGenerator = new Random(seedGenerator.nextInt());

        this.indexPaymentSameLengthOfQueueGenerator = new Random[this.numberOfWorkersPayment - 1];
        for (int i = 0; i < this.numberOfWorkersPayment - 1; i++) {
            this.indexPaymentSameLengthOfQueueGenerator[i] = new Random(seedGenerator.nextInt());
        }

        this.indexPaymentEmptyQueueGenerator = new Random[this.numberOfWorkersPayment - 1];
        for (int i = 0; i < this.numberOfWorkersPayment - 1; i++) {
            this.indexPaymentEmptyQueueGenerator[i] = new Random(seedGenerator.nextInt());
        }

        ArrayList<ContinuousEmpiricalDistributionParameter> parameterArrayListSimpleOrder = new ArrayList<ContinuousEmpiricalDistributionParameter>();
        ContinuousEmpiricalDistributionParameter pso1 = new ContinuousEmpiricalDistributionParameter(2.0,5.0,0.6);
        ContinuousEmpiricalDistributionParameter pso2 = new ContinuousEmpiricalDistributionParameter(5.0,9.0,0.4);
        parameterArrayListSimpleOrder.add(pso1);
        parameterArrayListSimpleOrder.add(pso2);
        this.timeSimpleOrderPreparationGenerator = new ContinuousEmpiricalDistributionGenerator(this.seedGenerator, parameterArrayListSimpleOrder);

        ArrayList<ContinuousEmpiricalDistributionParameter> parameterArrayListDifficultOrder = new ArrayList<ContinuousEmpiricalDistributionParameter>();
        ContinuousEmpiricalDistributionParameter pdo1 = new ContinuousEmpiricalDistributionParameter(11.0,12.0,0.1);
        ContinuousEmpiricalDistributionParameter pdo2 = new ContinuousEmpiricalDistributionParameter(12.0,20.0,0.6);
        ContinuousEmpiricalDistributionParameter pdo3 = new ContinuousEmpiricalDistributionParameter(20.0,25.0,0.3);
        parameterArrayListDifficultOrder.add(pdo1);
        parameterArrayListDifficultOrder.add(pdo2);
        parameterArrayListDifficultOrder.add(pdo3);
        this.timeDifficultOrderPreparationGenerator = new ContinuousEmpiricalDistributionGenerator(this.seedGenerator, parameterArrayListDifficultOrder);;

        this.delegates = new ArrayList<ISimDelegate>();

        double next = this.arrivalsGenerator.generate() * 60.0;
        ArrivalEvent startEvent = new ArrivalEvent(next);
//        ArrivalEvent startEvent = new ArrivalEvent(0.0);
        this.addEvent(startEvent);

        CloseShopEvent closeShopEvent = new CloseShopEvent(28800.0);
        this.addEvent(closeShopEvent);

        if (!turboMode) {
            SystemEvent nextSystemEvent = new SystemEvent(0.0, this.timeGap);
            this.addEvent(nextSystemEvent);
        }

        this.highestCustomerID = 0;
        this.highestWorkersOrderID = 0;
        this.highestWorkersPaymentID = 0;

        this.averageTimeInSystemStat = new ArithmeticMeanStatistics();
        this.averageTimeWaitingServiceStat = new ArithmeticMeanStatistics();
        this.averageTimeTicketStat = new ArithmeticMeanStatistics();
        averageServedCustomerStat = new ArithmeticMeanStatistics();
        averageTimeLeaveSystemStat = new ArithmeticMeanStatistics();
        numberOfCustomersWaitingTicketStat = new WeightedArithmeticMean();
        averageUsePercentTicketStat = new WeightedArithmeticMean();
        averageUsePercentOrderOnlineStat = new WeightedArithmeticMean();
        averageUsePercentOrderNormalStat = new WeightedArithmeticMean();
        averageUsePercentPaymentStat = new WeightedArithmeticMean();
    }

    @Override
    public void afterOneReplication() {
        super.afterOneReplication();

        this.averageServedCustomer = this.averageServedCustomerStat.calculateMean(this.servedCustomers);
        double time = this.lastCustomer.getTimeLeaveSystem();
        time += 32400.0;
        this.averageTimeLeaveSystem = this.getAverageTimeLeaveSystemStat().calculateMean(time);

        this.averageTimeInSystemStat.calculateCorrectedStandardDeviation();
        this.confIntTimeInSystemLower = this.averageTimeInSystemStat.calculateConfidenceIntervalLower(1.96);
        this.confIntTimeInSystemUpper = this.averageTimeInSystemStat.calculateConfidenceIntervalUpper(1.96);

        this.getAverageUsePercentPaymentStat().updateStatistics(this, this.getWorkersPaymentWorking());
        this.getAverageUsePercentOrderNormalStat().updateStatistics(this, this.getWorkersOrderWorkingNormal());
        this.getAverageUsePercentOrderOnlineStat().updateStatistics(this, this.getWorkersOrderWorkingOnline());

       this.updateStatistics();

        this.numberOfCustomersWaitingTicketStat.setLastUpdateTime(0.0);
        this.averageUsePercentTicketStat.setLastUpdateTime(0.0);
        this.averageUsePercentOrderNormalStat.setLastUpdateTime(0.0);
        this.averageUsePercentOrderOnlineStat.setLastUpdateTime(0.0);
        this.averageUsePercentPaymentStat.setLastUpdateTime(0.0);

        executedReplications++;
        currentTime = 0.0;
        highestCustomerID = 0;
        highestWorkersOrderID = 0;
        highestWorkersPaymentID = 0;

        workersOrderNormal.clear();
        workersOrderOnline.clear();
        workersOrderWorkingNormal.clear();
        workersOrderWorkingOnline.clear();
        workersPayment.clear();
        workersPaymentWorking.clear();

        int numberOfNormalWorkers = (int) Math.round(this.numberOfWorkersOrder * 2.0 / 3.0);
        int numberOfOnlineWorkers = this.numberOfWorkersOrder - numberOfNormalWorkers;

        for (int i = 0; i < numberOfNormalWorkers; i++ ) {
            Worker worker = new Worker(this.highestWorkersOrderID, Worker.WorkerType.ORDER_REGULAR_AND_CONTRACT);
            workersOrderNormal.add(worker);
            this.highestWorkersOrderID++;
        }
        for (int i = 0; i < numberOfOnlineWorkers; i++ ) {
            Worker worker = new Worker(this.highestWorkersOrderID, Worker.WorkerType.ORDER_ONLINE);
            workersOrderOnline.add(worker);
            this.highestWorkersOrderID++;
        }

        for (int i = 0; i < this.numberOfWorkersPayment; i++ ) {
            Worker worker = new Worker(this.highestWorkersPaymentID, Worker.WorkerType.PAYMENT);
            workersPayment.add(worker);
            this.highestWorkersPaymentID++;
        }

        queueCustomersWaitingTicketDispenser.clear();
        customerInteractingWithTicketDispenser.clear();
        customersWaitingInShopBeforeOrder.clear();
        customersOrdering.clear();
        for (LinkedList<Customer> queue : queuesCustomersWaitingForPayment) {
            queue.clear();
        }
        customersPaying.clear();
        allCustomers.clear();

        timeLine.clear();

        double next = this.arrivalsGenerator.generate() * 60.0;
        ArrivalEvent startEvent = new ArrivalEvent(next);
        this.addEvent(startEvent);

        CloseShopEvent closeShopEvent = new CloseShopEvent(28800);
        this.addEvent(closeShopEvent);

        if (!turboMode) {
            SystemEvent nextSystemEvent = new SystemEvent(0.0, this.timeGap);
            this.addEvent(nextSystemEvent);
        }

        this.lastCustomer = null;
        this.customersOut = 0;
        this.customersIn = 0;
        this.servedCustomers = 0;
    }

    @Override
    public void updateStatistics() {
        this.averageNumberOfCustomersWaitingTicket = this.getNumberOfCustomersWaitingTicketStat().calculateWeightedMean();
        this.averageUsePercentTicket = this.averageUsePercentTicketStat.calculateWeightedMean() * 100.0;
        this.averageUsePercentPayment = (this.averageUsePercentPaymentStat.calculateWeightedMean() / ((double)this.numberOfWorkersPayment)) * 100.0;

        double averageUsePercentOrderNormal = this.averageUsePercentOrderNormalStat.calculateWeightedMean() / ((double)this.numberOfNormalWorkers);
        double averageUsePercentOrderOnline = this.averageUsePercentOrderOnlineStat.calculateWeightedMean() / ((double)this.numberOfOnlineWorkers);
        this.averageUsePercentOrder = (averageUsePercentOrderNormal + averageUsePercentOrderOnline) / 2.0 * 100.0;
    }

    //region getters and setters
    public int getNumberOfWorkersPayment() {
        return numberOfWorkersPayment;
    }
    public LinkedList<Worker> getWorkersPayment() {
        return workersPayment;
    }
    public LinkedList<Worker> getWorkersPaymentWorking() {
        return workersPaymentWorking;
    }
    public LinkedList<Customer> getQueueCustomersWaitingTicketDispenser() {
        return queueCustomersWaitingTicketDispenser;
    }
    public LinkedList<Customer> getCustomerInteractingWithTicketDispenser() {
        return customerInteractingWithTicketDispenser;
    }
    public LinkedList<Customer> getCustomersWaitingInShopBeforeOrder() {
        return customersWaitingInShopBeforeOrder;
    }

    public LinkedList<Customer> getCustomersOrdering() {
        return customersOrdering;
    }

    public LinkedList<LinkedList<Customer>> getQueuesCustomersWaitingForPayment() {
        return queuesCustomersWaitingForPayment;
    }

    public LinkedList<Customer> getCustomersPaying() {
        return customersPaying;
    }

    public LinkedList<Customer> getAllCustomers() {
        return allCustomers;
    }

    public Random getSeedGenerator() {
        return seedGenerator;
    }

    public ExponentialDistributionGenerator getArrivalsGenerator() {
        return arrivalsGenerator;
    }

    public Random getCustomerTypeGenerator() {
        return customerTypeGenerator;
    }

    public Random getOrderTypeGenerator() {
        return orderTypeGenerator;
    }

    public Random getTimeTicketDispenserGenerator() {
        return timeTicketDispenserGenerator;
    }

    public Random getTimeOrderGenerator() {
        return timeOrderGenerator;
    }

    public TriangularDistributionGenerator getTimeOrderHandoverGenerator() {
        return timeOrderHandoverGenerator;
    }

    public Random getOrderSizeGenerator() {
        return orderSizeGenerator;
    }

    public Random getTimeBigOrderPickUpGenerator() {
        return timeBigOrderPickUpGenerator;
    }

    public Random getPaymentTypeGenerator() {
        return paymentTypeGenerator;
    }

    public Random getPaymentCashGenerator() {
        return paymentCashGenerator;
    }

    public Random getPaymentCardGenerator() {
        return paymentCardGenerator;
    }

    public ContinuousEmpiricalDistributionGenerator getTimeSimpleOrderPreparationGenerator() {
        return timeSimpleOrderPreparationGenerator;
    }

    public Random getTimeMediumOrderPreparationGenerator() {
        return timeMediumOrderPreparationGenerator;
    }

    public ContinuousEmpiricalDistributionGenerator getTimeDifficultOrderPreparationGenerator() {
        return timeDifficultOrderPreparationGenerator;
    }

    public LinkedList<Worker> getWorkersOrderNormal() {
        return workersOrderNormal;
    }

    public void setWorkersOrderNormal(LinkedList<Worker> workersOrderNormal) {
        this.workersOrderNormal = workersOrderNormal;
    }

    public LinkedList<Worker> getWorkersOrderOnline() {
        return workersOrderOnline;
    }

    public void setWorkersOrderOnline(LinkedList<Worker> workersOrderOnline) {
        this.workersOrderOnline = workersOrderOnline;
    }

    public LinkedList<Worker> getWorkersOrderWorkingNormal() {
        return workersOrderWorkingNormal;
    }

    public void setWorkersOrderWorkingNormal(LinkedList<Worker> workersOrderWorkingNormal) {
        this.workersOrderWorkingNormal = workersOrderWorkingNormal;
    }

    public LinkedList<Worker> getWorkersOrderWorkingOnline() {
        return workersOrderWorkingOnline;
    }

    public void setWorkersOrderWorkingOnline(LinkedList<Worker> workersOrderWorkingOnline) {
        this.workersOrderWorkingOnline = workersOrderWorkingOnline;
    }

    public Random[] getIndexPaymentSameLengthOfQueueGenerator() {
        return indexPaymentSameLengthOfQueueGenerator;
    }

    public Random[] getIndexPaymentEmptyQueueGenerator() {
        return indexPaymentEmptyQueueGenerator;
    }

    public int getCustomersIn() {
        return customersIn;
    }

    public void setCustomersIn(int customersIn) {
        this.customersIn = customersIn;
    }

    public int getCustomersOut() {
        return customersOut;
    }

    public void setCustomersOut(int customersOut) {
        this.customersOut = customersOut;
    }
    public void incCustomersIn() {
        this.customersIn++;
    }
    public void incCustomersOut() {
        this.customersOut++;
    }

    public ArithmeticMeanStatistics getAverageTimeInSystemStat() {
        return averageTimeInSystemStat;
    }

    public void setAverageTimeInSystemStat(ArithmeticMeanStatistics averageTimeInSystemStat) {
        this.averageTimeInSystemStat = averageTimeInSystemStat;
    }

    public double getAverageTimeInSystem() {
        return averageTimeInSystem;
    }

    public void setAverageTimeInSystem(double averageTimeInSystem) {
        this.averageTimeInSystem = averageTimeInSystem;
    }

    public int getServedCustomers() {
        return servedCustomers;
    }

    public void setServedCustomers(int servedCustomers) {
        this.servedCustomers = servedCustomers;
    }

    public ArithmeticMeanStatistics getAverageServedCustomerStat() {
        return averageServedCustomerStat;
    }

    public void setAverageServedCustomerStat(ArithmeticMeanStatistics averageServedCustomerStat) {
        this.averageServedCustomerStat = averageServedCustomerStat;
    }

    public double getAverageServedCustomer() {
        return averageServedCustomer;
    }

    public void setAverageServedCustomer(double averageServedCustomer) {
        this.averageServedCustomer = averageServedCustomer;
    }
    public void incServedCustomers() {
        this.servedCustomers++;
    }

    public ArithmeticMeanStatistics getAverageTimeTicketStat() {
        return averageTimeTicketStat;
    }

    public void setAverageTimeTicketStat(ArithmeticMeanStatistics averageTimeTicketStat) {
        this.averageTimeTicketStat = averageTimeTicketStat;
    }

    public double getAverageTimeTicket() {
        return averageTimeTicket;
    }

    public void setAverageTimeTicket(double averageTimeTicket) {
        this.averageTimeTicket = averageTimeTicket;
    }

    public WeightedArithmeticMean getNumberOfCustomersWaitingTicketStat() {
        return numberOfCustomersWaitingTicketStat;
    }

    public void setNumberOfCustomersWaitingTicketStat(WeightedArithmeticMean numberOfCustomersWaitingTicketStat) {
        this.numberOfCustomersWaitingTicketStat = numberOfCustomersWaitingTicketStat;
    }

    public double getAverageNumberOfCustomersWaitingTicket() {
        return averageNumberOfCustomersWaitingTicket;
    }

    public void setAverageNumberOfCustomersWaitingTicket(double averageNumberOfCustomersWaitingTicket) {
        this.averageNumberOfCustomersWaitingTicket = averageNumberOfCustomersWaitingTicket;
    }

    public ArithmeticMeanStatistics getAverageTimeLeaveSystemStat() {
        return averageTimeLeaveSystemStat;
    }

    public void setAverageTimeLeaveSystemStat(ArithmeticMeanStatistics averageTimeLeaveSystemStat) {
        this.averageTimeLeaveSystemStat = averageTimeLeaveSystemStat;
    }

    public double getAverageTimeLeaveSystem() {
        return averageTimeLeaveSystem;
    }

    public void setAverageTimeLeaveSystem(double averageTimeLeaveSystem) {
        this.averageTimeLeaveSystem = averageTimeLeaveSystem;
    }

    public WeightedArithmeticMean getAverageUsePercentTicketStat() {
        return averageUsePercentTicketStat;
    }

    public void setAverageUsePercentTicketStat(WeightedArithmeticMean averageUsePercentTicketStat) {
        this.averageUsePercentTicketStat = averageUsePercentTicketStat;
    }

    public double getAverageUsePercentTicket() {
        return averageUsePercentTicket;
    }

    public void setAverageUsePercentTicket(double averageUsePercentTicket) {
        this.averageUsePercentTicket = averageUsePercentTicket;
    }

    public Customer getLastCustomer() {
        return lastCustomer;
    }

    public void setLastCustomer(Customer lastCustomer) {
        this.lastCustomer = lastCustomer;
    }

    public double getConfIntTimeInSystemLower() {
        return confIntTimeInSystemLower;
    }

    public void setConfIntTimeInSystemLower(double confIntTimeInSystemLower) {
        this.confIntTimeInSystemLower = confIntTimeInSystemLower;
    }

    public double getConfIntTimeInSystemUpper() {
        return confIntTimeInSystemUpper;
    }

    public void setConfIntTimeInSystemUpper(double confIntTimeInSystemUpper) {
        this.confIntTimeInSystemUpper = confIntTimeInSystemUpper;
    }

    public int getHighestCustomerID() {
        return highestCustomerID;
    }
    public void incHighestCustomerID() {
        this.highestCustomerID++;
    }

    public WeightedArithmeticMean getAverageUsePercentOrderNormalStat() {
        return averageUsePercentOrderNormalStat;
    }

    public WeightedArithmeticMean getAverageUsePercentOrderOnlineStat() {
        return averageUsePercentOrderOnlineStat;
    }

    public WeightedArithmeticMean getAverageUsePercentPaymentStat() {
        return averageUsePercentPaymentStat;
    }

    public double getAverageUsePercentOrder() {
        return averageUsePercentOrder;
    }

    public double getAverageUsePercentPayment() {
        return averageUsePercentPayment;
    }

    public ArithmeticMeanStatistics getAverageTimeWaitingServiceStat() {
        return averageTimeWaitingServiceStat;
    }
    //endregion
}
