package Events;

import Entities.Customer;
import SimulationClasses.Event;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class StartServiceEvent extends Event {
    public StartServiceEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        this.customer.setEndTimeWaitingService(time);

        double timeWaitingService = this.customer.getEndTimeWaitingService() - customer.getStartTimeWaitingService();
        double average = ((Sem2) core).getAverageTimeWaitingServiceStat().calculateMean(timeWaitingService);

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
                && ((Sem2) core).getCustomersWaitingInShopBeforeOrder().size() < 9
                && ((Sem2) core).getCustomerInteractingWithTicketDispenser().size() == 0) {

            ((Sem2) core).getNumberOfCustomersWaitingTicketStat().updateStatistics(core, ((Sem2) core).getQueueCustomersWaitingTicketDispenser());
            Customer nextCustomer = ((Sem2) core).getQueueCustomersWaitingTicketDispenser().removeFirst();

            ((Sem2) core).getAverageUsePercentTicketStat().updateStatistics(core, ((Sem2) core).getCustomerInteractingWithTicketDispenser());
            ((Sem2) core).getCustomerInteractingWithTicketDispenser().add(nextCustomer);

            StartInteractionTicketDispenserEvent startInteraction = new StartInteractionTicketDispenserEvent(time);
            startInteraction.setCustomer(nextCustomer);
            core.addEvent(startInteraction);
        }
    }
}
