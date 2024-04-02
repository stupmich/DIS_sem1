package Events;

import Entities.Customer;
import Entities.Worker;
import SimulationClasses.EventBasedSimulationCore;

public abstract class Event implements Comparable<Event> {
    protected Customer customer;
    protected Worker worker;
    protected double time;

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

    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }
}
