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
            // regular customer and customer with contract has to make his order first
            StartOrderingEvent startOrderingEvent = new StartOrderingEvent(time);
            startOrderingEvent.setCustomer(customer);
            startOrderingEvent.setWorker(worker);
            core.addEvent(startOrderingEvent);
        } else {
            // order of online customer can be prepared straight away
            StartOrderPreparationEvent startOrderPreparationEvent = new StartOrderPreparationEvent(time);
            startOrderPreparationEvent.setCustomer(customer);
            startOrderPreparationEvent.setWorker(worker);
            core.addEvent(startOrderPreparationEvent);
        }

        // when new service is started place for another customer is free in shop
        if (((Sem2) core).getQueueCustomersWaitingTicketDispenser().size() != 0
                && ((Sem2) core).getCustomersWaitingInShopBeforeOrder().size() < ((Sem2) core).getNumOfPlacesInShop()) {
            // it is more safe to reserve place in shop before interaction starts
            ((Sem2) core).getNumberOfCustomersWaitingTicketStat().updateStatistics(core, ((Sem2) core).getQueueCustomersWaitingTicketDispenser());

            Customer nextCustomer = ((Sem2) core).getQueueCustomersWaitingTicketDispenser().poll();

            ((Sem2) core).getCustomersWaitingInShopBeforeOrder().add(nextCustomer);

            StartInteractionTicketDispenserEvent startInteraction = new StartInteractionTicketDispenserEvent(time);
            startInteraction.setCustomer(nextCustomer);
            core.addEvent(startInteraction);
        }
    }
}
