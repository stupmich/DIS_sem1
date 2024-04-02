package Events;

import Entities.Customer;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class MoveToShopEvent extends Event {
    public MoveToShopEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        if (this.customer.getCustomerType() == Customer.CustomerType.REGULAR || this.customer.getCustomerType() == Customer.CustomerType.CONTRACT) {
            if (((Sem2) core).getWorkersOrderNormal().size() != 0) {
                StartServiceEvent startServiceEvent = new StartServiceEvent(time);
                startServiceEvent.setCustomer(customer);
                core.addEvent(startServiceEvent);
            } else {
                ((Sem2) core).getCustomersWaitingInShopBeforeOrder().add(this.customer);
            }
        } else {
            if (((Sem2) core).getWorkersOrderOnline().size() != 0) {
                StartServiceEvent startServiceEvent = new StartServiceEvent(time);
                startServiceEvent.setCustomer(customer);
                core.addEvent(startServiceEvent);
            } else {
                ((Sem2) core).getCustomersWaitingInShopBeforeOrder().add(this.customer);
            }
        }

        ((Sem2) core).setCustomerInteractingWithTicketDispenser(null);

        if (((Sem2) core).getQueueCustomersWaitingTicketDispenser().size() != 0
            && ((Sem2) core).getCustomersWaitingInShopBeforeOrder().size() < ((Sem2) core).getNumOfPlacesInShop()) {
                StartInteractionTicketDispenserEvent startInteraction = new StartInteractionTicketDispenserEvent(time);
                startInteraction.setCustomer(((Sem2) core).getQueueCustomersWaitingTicketDispenser().poll());
                core.addEvent(startInteraction);
        }
    }
}
