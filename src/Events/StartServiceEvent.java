package Events;

import Entities.Customer;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class StartServiceEvent extends Event {
    public StartServiceEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        if (this.customer.getCustomerType() == Customer.CustomerType.REGULAR || this.customer.getCustomerType() == Customer.CustomerType.CONTRACT) {
            StartOrderingEvent startOrderingEvent = new StartOrderingEvent(time);
            startOrderingEvent.setCustomer(customer);
            startOrderingEvent.setWorker(worker);
            core.addEvent(startOrderingEvent);
        } else {
            StartOrderPreparationEvent startOrderPreparationEvent = new StartOrderPreparationEvent(time);
            startOrderPreparationEvent.setCustomer(customer);
            startOrderPreparationEvent.setWorker(worker);
            core.addEvent(startOrderPreparationEvent);
        }

        if (((Sem2) core).getQueueCustomersWaitingTicketDispenser().size() != 0
                && ((Sem2) core).getCustomersWaitingInShopBeforeOrder().size() < ((Sem2) core).getNumOfPlacesInShop()) {
            StartInteractionTicketDispenserEvent startInteraction = new StartInteractionTicketDispenserEvent(time);
            startInteraction.setCustomer(((Sem2) core).getQueueCustomersWaitingTicketDispenser().poll());
            core.addEvent(startInteraction);
        }
    }
}
