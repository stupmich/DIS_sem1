package Events;

import Entities.Customer;
import Entities.Worker;
import SimulationClasses.Event;

public abstract class EventElektro extends Event {
    protected Customer customer;
    protected Worker worker;

    public EventElektro(double time) {
        super(time);
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

}
