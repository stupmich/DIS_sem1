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

    public enum SizeOfOrder {
        REGULAR,
        BIG,
    }

    private int id;
    private CustomerType customerType;
    private OrderType orderType;
    private SizeOfOrder sizeOfOrder;
    private double timeLeaveSystem;
    private double timeArrival;
    private Worker blockingWorker;
    double startTimeWaitingService;
    double endTimeWaitingService;

    public Customer(double timeArrival, int id) {
        this.timeArrival = timeArrival;
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

    public SizeOfOrder getSizeOfOrder() {
        return sizeOfOrder;
    }

    public void setSizeOfOrder(SizeOfOrder sizeOfOrder) {
        this.sizeOfOrder = sizeOfOrder;
    }

    public double getStartTimeWaitingService() {
        return startTimeWaitingService;
    }

    public void setStartTimeWaitingService(double startTimeWaitingService) {
        this.startTimeWaitingService = startTimeWaitingService;
    }

    public double getEndTimeWaitingService() {
        return endTimeWaitingService;
    }

    public void setEndTimeWaitingService(double endTimeWaitingService) {
        this.endTimeWaitingService = endTimeWaitingService;
    }
}
