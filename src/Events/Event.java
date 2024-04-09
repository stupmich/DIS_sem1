package Events;

import Entities.Customer;
import Entities.Worker;
import SimulationClasses.EventBasedSimulationCore;

import java.util.Comparator;

public abstract class Event implements Comparable<Event> {
    protected Customer customer;
    protected Worker worker;
    protected double time;
    protected int priority = 3;

    public Event(double time) {
        this.time = time;
    }

    public abstract void execute(EventBasedSimulationCore core);

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int compareTo(Event other) {
//        int compareTime = Double.compare(this.time, other.time);
//        if (compareTime == 0) {
//            return Integer.compare(this.priority, other.priority);
//        } else {
//            return compareTime;
//        }

        return Double.compare(this.time, other.time);
    }
}
