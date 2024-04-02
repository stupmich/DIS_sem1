package SimulationClasses;

import Entities.Customer;
import Entities.Worker;
import Events.ArrivalEvent;
import Events.SystemEvent;
import Generators.ContinuousEmpiricalDistributionGenerator;
import Generators.ContinuousEmpiricalDistributionParameter;
import Generators.ExponentialDistributionGenerator;
import Generators.TriangularDistributionGenerator;
import Observer.ISimDelegate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Sem2 extends EventBasedSimulationCore {
    private int numberOfWorkersOrder;
    private int numberOfWorkersPayment;

    private int highestCustomerID;
    private int highestWorkersOrderID;
    private int highestWorkersPaymentID;
    
    private LinkedList<Worker> workersOrderNormal;
    private LinkedList<Worker> workersOrderOnline;
    private LinkedList<Worker> workersOrderWorkingNormal;
    private LinkedList<Worker> workersOrderWorkingOnline;
    private LinkedList<Worker> workersPayment;
    private LinkedList<Worker> workersPaymentWorking;

    private LinkedList<Customer> queueCustomersWaitingTicketDispenser;
    private Customer customerInteractingWithTicketDispenser;

    private LinkedList<Customer> customersWaitingInShopBeforeOrder;
    private LinkedList<Customer> customersOrdering;

    private LinkedList<LinkedList<Customer>> queuesCustomersWaitingForPayment;
    private LinkedList<Customer> customersPaying;

    private LinkedList<Customer> allCustomers;
    private int numOfPlacesInShop = 9;

    private Random seedGenerator;
    private ExponentialDistributionGenerator arrivalsGenerator;
    private Random customerTypeGenerator;
    private Random orderTypeGenerator;
    private Random timeTicketDispenserGenerator;
    private Random timeOrderGenerator;
    private TriangularDistributionGenerator timeOrderHandoverGenerator;
    private Random orderSizeGenerator;
    private Random timeBigOrderPickUpGenerator;
    private Random paymentTypeGenerator;
    private Random paymentCashGenerator;
    private Random paymentCardGenerator;
    private ContinuousEmpiricalDistributionGenerator timeSimpleOrderPreparationGenerator;
    private Random timeMediumOrderPreparationGenerator;
    private ContinuousEmpiricalDistributionGenerator timeDifficultOrderPreparationGenerator;

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

        int numberOfNormalWorkers = (int) Math.round(this.numberOfWorkersOrder * 2 / 3.0);
        int numberOfOnlineWorkers = this.numberOfWorkersOrder - numberOfNormalWorkers;

        for (int i = 0; i < numberOfNormalWorkers; i++ ) {
            Worker worker = new Worker(this.highestWorkersOrderID);
            workersOrderNormal.add(worker);
            this.highestWorkersOrderID++;
        }
        for (int i = 0; i < numberOfOnlineWorkers; i++ ) {
            Worker worker = new Worker(this.highestWorkersOrderID);
            workersOrderOnline.add(worker);
            this.highestWorkersOrderID++;
        }

        for (int i = 0; i < this.numberOfWorkersPayment; i++ ) {
            Worker worker = new Worker(this.highestWorkersPaymentID);
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

        ArrayList<ContinuousEmpiricalDistributionParameter> parameterArrayListSimpleOrder = new ArrayList<ContinuousEmpiricalDistributionParameter>();
        ContinuousEmpiricalDistributionParameter pso1 = new ContinuousEmpiricalDistributionParameter(2,5,0.6);
        ContinuousEmpiricalDistributionParameter pso2 = new ContinuousEmpiricalDistributionParameter(5,9,0.4);
        parameterArrayListSimpleOrder.add(pso1);
        parameterArrayListSimpleOrder.add(pso2);
        this.timeSimpleOrderPreparationGenerator = new ContinuousEmpiricalDistributionGenerator(this.seedGenerator, parameterArrayListSimpleOrder);

        ArrayList<ContinuousEmpiricalDistributionParameter> parameterArrayListDifficultOrder = new ArrayList<ContinuousEmpiricalDistributionParameter>();
        ContinuousEmpiricalDistributionParameter pdo1 = new ContinuousEmpiricalDistributionParameter(11,12,0.1);
        ContinuousEmpiricalDistributionParameter pdo2 = new ContinuousEmpiricalDistributionParameter(12,20,0.6);
        ContinuousEmpiricalDistributionParameter pdo3 = new ContinuousEmpiricalDistributionParameter(20,25,0.3);
        parameterArrayListDifficultOrder.add(pdo1);
        parameterArrayListDifficultOrder.add(pdo2);
        parameterArrayListDifficultOrder.add(pdo3);
        this.timeDifficultOrderPreparationGenerator = new ContinuousEmpiricalDistributionGenerator(this.seedGenerator, parameterArrayListDifficultOrder);;

        this.delegates = new ArrayList<ISimDelegate>();

        ArrivalEvent startEvent = new ArrivalEvent(0.0);
        this.addEvent(startEvent);

        if (!turboMode) {
            SystemEvent nextSystemEvent = new SystemEvent(0.0, this.timeGap);
            this.addEvent(nextSystemEvent);
        }

        this.highestCustomerID = 0;
        this.highestWorkersOrderID = 0;
        this.highestWorkersPaymentID = 0;
    }

    @Override
    public void afterOneReplication() {
        super.afterOneReplication();
        executedReplications++;
        this.currentTime = 0.0;
        highestCustomerID = 0;
        highestWorkersOrderID = 0;
        highestWorkersPaymentID = 0;

        workersOrderNormal.clear();
        workersOrderOnline.clear();
        workersOrderWorkingNormal.clear();
        workersOrderWorkingOnline.clear();
        workersPayment.clear();
        workersPaymentWorking.clear();

        int numberOfNormalWorkers = (int) Math.round(this.numberOfWorkersOrder * 2 / 3.0);
        int numberOfOnlineWorkers = this.numberOfWorkersOrder - numberOfNormalWorkers;

        for (int i = 0; i < numberOfNormalWorkers; i++ ) {
            Worker worker = new Worker(this.highestWorkersOrderID);
            workersOrderNormal.add(worker);
            this.highestWorkersOrderID++;
        }
        for (int i = 0; i < numberOfOnlineWorkers; i++ ) {
            Worker worker = new Worker(this.highestWorkersOrderID);
            workersOrderOnline.add(worker);
            this.highestWorkersOrderID++;
        }

        for (int i = 0; i < this.numberOfWorkersPayment; i++ ) {
            Worker worker = new Worker(this.highestWorkersPaymentID);
            workersPayment.add(worker);
            this.highestWorkersPaymentID++;
        }

        queueCustomersWaitingTicketDispenser.clear();
        customerInteractingWithTicketDispenser = null;
        customersWaitingInShopBeforeOrder.clear();
        customersOrdering.clear();
        for (LinkedList<Customer> queue : queuesCustomersWaitingForPayment) {
            queue.clear();
        }
        customersPaying.clear();
        allCustomers.clear();
        timeLine.clear();


        ArrivalEvent startEvent = new ArrivalEvent(0.0);
        this.addEvent(startEvent);
        if (!turboMode) {
            SystemEvent nextSystemEvent = new SystemEvent(0.0, this.timeGap);
            this.addEvent(nextSystemEvent);
        }
    }

    @Override
    public void updateStatistics() {

    }


    public int getHighestCustomerID() {
        return highestCustomerID;
    }

    public void setHighestCustomerID(int highestCustomerID) {
        this.highestCustomerID = highestCustomerID;
    }

    public void incHighestCustomerID() {
        this.highestCustomerID++;
    }

    public int getNumberOfWorkersOrder() {
        return numberOfWorkersOrder;
    }

    public int getNumberOfWorkersPayment() {
        return numberOfWorkersPayment;
    }

    public int getHighestWorkersOrderID() {
        return highestWorkersOrderID;
    }

    public int getHighestWorkersPaymentID() {
        return highestWorkersPaymentID;
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

    public Customer getCustomerInteractingWithTicketDispenser() {
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

    public int getNumOfPlacesInShop() {
        return numOfPlacesInShop;
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

    public void setNumberOfWorkersOrder(int numberOfWorkersOrder) {
        this.numberOfWorkersOrder = numberOfWorkersOrder;
    }

    public void setNumberOfWorkersPayment(int numberOfWorkersPayment) {
        this.numberOfWorkersPayment = numberOfWorkersPayment;
    }

    public void setHighestWorkersOrderID(int highestWorkersOrderID) {
        this.highestWorkersOrderID = highestWorkersOrderID;
    }

    public void setHighestWorkersPaymentID(int highestWorkersPaymentID) {
        this.highestWorkersPaymentID = highestWorkersPaymentID;
    }

    public void setWorkersPayment(LinkedList<Worker> workersPayment) {
        this.workersPayment = workersPayment;
    }

    public void setWorkersPaymentWorking(LinkedList<Worker> workersPaymentWorking) {
        this.workersPaymentWorking = workersPaymentWorking;
    }

    public void setQueueCustomersWaitingTicketDispenser(LinkedList<Customer> queueCustomersWaitingTicketDispenser) {
        this.queueCustomersWaitingTicketDispenser = queueCustomersWaitingTicketDispenser;
    }

    public void setCustomerInteractingWithTicketDispenser(Customer customerInteractingWithTicketDispenser) {
        this.customerInteractingWithTicketDispenser = customerInteractingWithTicketDispenser;
    }

    public void setCustomersWaitingInShopBeforeOrder(LinkedList<Customer> customersWaitingInShopBeforeOrder) {
        this.customersWaitingInShopBeforeOrder = customersWaitingInShopBeforeOrder;
    }

    public void setCustomersOrdering(LinkedList<Customer> customersOrdering) {
        this.customersOrdering = customersOrdering;
    }

    public void setQueuesCustomersWaitingForPayment(LinkedList<LinkedList<Customer>> queuesCustomersWaitingForPayment) {
        this.queuesCustomersWaitingForPayment = queuesCustomersWaitingForPayment;
    }

    public void setCustomersPaying(LinkedList<Customer> customersPaying) {
        this.customersPaying = customersPaying;
    }

    public void setAllCustomers(LinkedList<Customer> allCustomers) {
        this.allCustomers = allCustomers;
    }

    public void setNumOfPlacesInShop(int numOfPlacesInShop) {
        this.numOfPlacesInShop = numOfPlacesInShop;
    }

    public void setSeedGenerator(Random seedGenerator) {
        this.seedGenerator = seedGenerator;
    }

    public void setArrivalsGenerator(ExponentialDistributionGenerator arrivalsGenerator) {
        this.arrivalsGenerator = arrivalsGenerator;
    }

    public void setCustomerTypeGenerator(Random customerTypeGenerator) {
        this.customerTypeGenerator = customerTypeGenerator;
    }

    public void setOrderTypeGenerator(Random orderTypeGenerator) {
        this.orderTypeGenerator = orderTypeGenerator;
    }

    public void setTimeTicketDispenserGenerator(Random timeTicketDispenserGenerator) {
        this.timeTicketDispenserGenerator = timeTicketDispenserGenerator;
    }

    public void setTimeOrderGenerator(Random timeOrderGenerator) {
        this.timeOrderGenerator = timeOrderGenerator;
    }

    public void setTimeOrderHandoverGenerator(TriangularDistributionGenerator timeOrderHandoverGenerator) {
        this.timeOrderHandoverGenerator = timeOrderHandoverGenerator;
    }

    public void setOrderSizeGenerator(Random orderSizeGenerator) {
        this.orderSizeGenerator = orderSizeGenerator;
    }

    public void setTimeBigOrderPickUpGenerator(Random timeBigOrderPickUpGenerator) {
        this.timeBigOrderPickUpGenerator = timeBigOrderPickUpGenerator;
    }

    public void setPaymentTypeGenerator(Random paymentTypeGenerator) {
        this.paymentTypeGenerator = paymentTypeGenerator;
    }

    public void setPaymentCashGenerator(Random paymentCashGenerator) {
        this.paymentCashGenerator = paymentCashGenerator;
    }

    public void setPaymentCardGenerator(Random paymentCardGenerator) {
        this.paymentCardGenerator = paymentCardGenerator;
    }

    public void setTimeSimpleOrderPreparationGenerator(ContinuousEmpiricalDistributionGenerator timeSimpleOrderPreparationGenerator) {
        this.timeSimpleOrderPreparationGenerator = timeSimpleOrderPreparationGenerator;
    }

    public void setTimeMediumOrderPreparationGenerator(Random timeMediumOrderPreparationGenerator) {
        this.timeMediumOrderPreparationGenerator = timeMediumOrderPreparationGenerator;
    }

    public void setTimeDifficultOrderPreparationGenerator(ContinuousEmpiricalDistributionGenerator timeDifficultOrderPreparationGenerator) {
        this.timeDifficultOrderPreparationGenerator = timeDifficultOrderPreparationGenerator;
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
}
