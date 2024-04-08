package Events;

import Entities.Customer;
import Entities.Worker;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class MoveToShopEvent extends Event {
    public MoveToShopEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        this.customer.setHasTicket(true);

        if (this.customer.getCustomerType() == Customer.CustomerType.REGULAR || this.customer.getCustomerType() == Customer.CustomerType.CONTRACT) {
            if (((Sem2) core).getWorkersOrderNormal().size() != 0) {
                // there are free workers for regular customers and with contract
                ((Sem2) core).getCustomersWaitingInShopBeforeOrder().remove(this.customer);

                Worker worker = ((Sem2) core).getWorkersOrderNormal().removeLast();
                worker.setIdCustomer(customer.getId());

                ((Sem2) core).getWorkersOrderWorkingNormal().add(worker);

                StartServiceEvent startServiceEvent = new StartServiceEvent(time);
                startServiceEvent.setCustomer(customer);
                startServiceEvent.setWorker(worker);
                core.addEvent(startServiceEvent);
            }
        } else {
            if (((Sem2) core).getWorkersOrderOnline().size() != 0) {
                // there are free workers for online customers
                ((Sem2) core).getCustomersWaitingInShopBeforeOrder().remove(this.customer);

                Worker worker = ((Sem2) core).getWorkersOrderOnline().removeLast();
                worker.setIdCustomer(customer.getId());

                ((Sem2) core).getWorkersOrderWorkingOnline().add(worker);

                StartServiceEvent startServiceEvent = new StartServiceEvent(time);
                startServiceEvent.setCustomer(customer);
                startServiceEvent.setWorker(worker);
                core.addEvent(startServiceEvent);
            }
        }

        // customer stopped interacting with ticket dispenser
        ((Sem2) core).setCustomerInteractingWithTicketDispenser(null);

        // if there is place in shop + customer is waiting for ticket dispenser start new interaction
        if (((Sem2) core).getQueueCustomersWaitingTicketDispenser().size() != 0 && ((Sem2) core).getCustomersWaitingInShopBeforeOrder().size() < ((Sem2) core).getNumOfPlacesInShop()) {
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
