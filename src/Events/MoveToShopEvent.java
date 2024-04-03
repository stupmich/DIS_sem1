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
        if (this.customer.getCustomerType() == Customer.CustomerType.REGULAR || this.customer.getCustomerType() == Customer.CustomerType.CONTRACT) {
            if (((Sem2) core).getWorkersOrderNormal().size() != 0) {
                // there are free workers for regular customers and with contract
                Worker worker = ((Sem2) core).getWorkersOrderNormal().removeLast();
                worker.setIdCustomer(customer.getId());

                ((Sem2) core).getWorkersOrderWorkingNormal().add(worker);

                StartServiceEvent startServiceEvent = new StartServiceEvent(time);
                startServiceEvent.setCustomer(customer);
                startServiceEvent.setWorker(worker);
                core.addEvent(startServiceEvent);
            }
//            else { better to have it in start interaction
//                ((Sem2) core).getCustomersWaitingInShopBeforeOrder().add(this.customer);
//            }
        } else {
            if (((Sem2) core).getWorkersOrderOnline().size() != 0) {
                // there are free workers for online customers
                Worker worker = ((Sem2) core).getWorkersOrderOnline().removeLast();
                worker.setIdCustomer(customer.getId());

                ((Sem2) core).getWorkersOrderWorkingOnline().add(worker);

                StartServiceEvent startServiceEvent = new StartServiceEvent(time);
                startServiceEvent.setCustomer(customer);
                startServiceEvent.setWorker(worker);
                core.addEvent(startServiceEvent);
            }
//            else { better to have it in start interaction
//                ((Sem2) core).getCustomersWaitingInShopBeforeOrder().add(this.customer);
//            }
        }

        // customer stopped interacting with ticket dispenser
        ((Sem2) core).setCustomerInteractingWithTicketDispenser(null);

        // if there is place in shop + customer is waiting for ticket dispenser start new interaction
        if (((Sem2) core).getQueueCustomersWaitingTicketDispenser().size() != 0
            && ((Sem2) core).getCustomersWaitingInShopBeforeOrder().size() < ((Sem2) core).getNumOfPlacesInShop()) {
                StartInteractionTicketDispenserEvent startInteraction = new StartInteractionTicketDispenserEvent(time);
                startInteraction.setCustomer(((Sem2) core).getQueueCustomersWaitingTicketDispenser().poll());
                core.addEvent(startInteraction);
        }
    }
}
