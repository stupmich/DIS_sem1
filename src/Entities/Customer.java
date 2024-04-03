package Entities;

public class Customer {
    public enum CustomerType {
        REGULAR,
        CONTRACT,
        ONLINE
    }

    public enum OrderType {
        SIMPLE,
        MEDIUM,
        DIFFICULT
    }

    private int id;
    private CustomerType customerType;
    private OrderType orderType;
    private double startOfWaitingInQueue;
    private double waitingQueueTime;
    private double serviceTime;
    private double startOfService;
    private double timeLeaveSystem;
    private double timeArrival;
    private Worker blockingWorker;

    public Customer(double timeArrival, CustomerType type, int id) {
        this.timeArrival = timeArrival;
        this.customerType = type;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public double getStartOfWaitingInQueue() {
        return startOfWaitingInQueue;
    }

    public void setStartOfWaitingInQueue(double startOfWaitingInQueue) {
        this.startOfWaitingInQueue = startOfWaitingInQueue;
    }

    public double getWaitingQueueTime() {
        return waitingQueueTime;
    }

    public void setWaitingQueueTime(double waitingQueueTime) {
        this.waitingQueueTime = waitingQueueTime;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(double serviceTime) {
        this.serviceTime = serviceTime;
    }

    public double getStartOfService() {
        return startOfService;
    }

    public void setStartOfService(double startOfService) {
        this.startOfService = startOfService;
    }

    public double getTimeLeaveSystem() {
        return timeLeaveSystem;
    }

    public void setTimeLeaveSystem(double timeLeaveSystem) {
        this.timeLeaveSystem = timeLeaveSystem;
    }

    public double getTimeArrival() {
        return timeArrival;
    }

    public void setTimeArrival(double timeArrival) {
        this.timeArrival = timeArrival;
    }

    public Worker getBlockingWorker() {
        return blockingWorker;
    }

    public void setBlockingWorker(Worker blockingWorker) {
        this.blockingWorker = blockingWorker;
    }
}
