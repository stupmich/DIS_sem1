package Events;

import Entities.Customer;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class ArrivalEvent extends Event {
    public ArrivalEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        double type = ((Sem2) core).getCustomerTypeGenerator().nextDouble();

        if (type < 0.50) {
            customer = new Customer(time, Customer.CustomerType.REGULAR, ((Sem2) core).getHighestCustomerID());
        } else if (type < 0.65) {
            customer = new Customer(time, Customer.CustomerType.CONTRACT, ((Sem2) core).getHighestCustomerID());
        } else {
            customer = new Customer(time, Customer.CustomerType.ONLINE, ((Sem2) core).getHighestCustomerID());
        }

        ((Sem2) core).incHighestCustomerID();
        ((Sem2) core).getAllCustomers().add(customer);

        if (((Sem2) core).getCustomerInteractingWithTicketDispenser() != null
                || ((Sem2) core).getCustomersWaitingInShopBeforeOrder().size() == ((Sem2) core).getNumOfPlacesInShop()) {
            ((Sem2) core).getQueueCustomersWaitingTicketDispenser().add(customer);
        } else {
            // it is more safe to reserve place in shop before interaction starts
            ((Sem2) core).getCustomersWaitingInShopBeforeOrder().add(this.customer);

            StartInteractionTicketDispenserEvent startInteraction = new StartInteractionTicketDispenserEvent(time);
            startInteraction.setCustomer(customer);
            core.addEvent(startInteraction);
        }

        double next = (((Sem2) core).getArrivalsGenerator().generate()) * 60.0;
        if (time + next <= 28800.0) {
            this.setTime(time + next);
            core.addEvent(this);
        }
    }
}
