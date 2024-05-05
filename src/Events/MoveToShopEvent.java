package Events;

import Entities.Customer;
import Entities.Worker;
import SimulationClasses.Event;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class MoveToShopEvent extends EventElektro {
    public MoveToShopEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        this.customer.setStartTimeWaitingService(time);

        if (this.customer.getCustomerType() == Customer.CustomerType.REGULAR || this.customer.getCustomerType() == Customer.CustomerType.CONTRACT) {
            if (((Sem2) core).getWorkersOrderNormal().size() != 0) {
                // there are free workers for regular customers and with contract
                Worker worker = ((Sem2) core).getWorkersOrderNormal().removeFirst();
                worker.setIdCustomer(customer.getId());
                worker.setCustomer(customer);

                ((Sem2) core).getAverageUsePercentOrderNormalStat().updateStatistics(core, ((Sem2) core).getWorkersOrderWorkingNormal());
                ((Sem2) core).getWorkersOrderWorkingNormal().add(worker);

                StartServiceEvent startServiceEvent = new StartServiceEvent(time);
                startServiceEvent.setCustomer(customer);
                startServiceEvent.setWorker(worker);
                core.addEvent(startServiceEvent);
            } else {
                ((Sem2) core).getCustomersWaitingInShopBeforeOrder().add(customer);
                if (((Sem2) core).getCustomersWaitingInShopBeforeOrder().size()>9) {
                    System.out.println();
                }
            }
        } else {
            if (((Sem2) core).getWorkersOrderOnline().size() != 0) {
                // there are free workers for online customers
                Worker worker = ((Sem2) core).getWorkersOrderOnline().removeFirst();
                worker.setIdCustomer(customer.getId());
                worker.setCustomer(customer);

                ((Sem2) core).getAverageUsePercentOrderOnlineStat().updateStatistics(core, ((Sem2) core).getWorkersOrderWorkingOnline());
                ((Sem2) core).getWorkersOrderWorkingOnline().add(worker);

                StartServiceEvent startServiceEvent = new StartServiceEvent(time);
                startServiceEvent.setCustomer(customer);
                startServiceEvent.setWorker(worker);
                core.addEvent(startServiceEvent);
            } else {
                ((Sem2) core).getCustomersWaitingInShopBeforeOrder().add(customer);
                if (((Sem2) core).getCustomersWaitingInShopBeforeOrder().size()>9) {
                    System.out.println();
                }
            }
        }

        // customer stopped interacting with ticket dispenser
        ((Sem2) core).getAverageUsePercentTicketStat().updateStatistics(core, ((Sem2) core).getCustomerInteractingWithTicketDispenser());
        ((Sem2) core).getCustomerInteractingWithTicketDispenser().remove(customer);

        // if there is place in shop + customer is waiting for ticket dispenser start new interaction
        if (((Sem2) core).getQueueCustomersWaitingTicketDispenser().size() != 0
                && ((Sem2) core).getCustomersWaitingInShopBeforeOrder().size() < 9) {

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
