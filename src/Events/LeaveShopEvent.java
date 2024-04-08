package Events;

import Entities.Customer;
import Entities.Worker;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class LeaveShopEvent extends Event {
    public LeaveShopEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        ((Sem2) core).setLastCustomer(customer);

        customer.setTimeLeaveSystem(time);
        double timeInSystem = customer.getTimeLeaveSystem() - customer.getTimeArrival();
        ((Sem2) core).setAverageTimeInSystem(((Sem2) core).getAverageTimeInSystemStat().calculateMean(timeInSystem));

        ((Sem2) core).incCustomersOut();

        ((Sem2) core).getAllCustomers().remove(customer);

        if (((Sem2) core).getQueueCustomersWaitingTicketDispenser().size() != 0
                && ((Sem2) core).getCustomersWaitingInShopBeforeOrder().size() < 9
                && ((Sem2) core).getCustomerInteractingWithTicketDispenser().size() == 0) {
            // it is more safe to reserve place in shop before interaction starts
            ((Sem2) core).getNumberOfCustomersWaitingTicketStat().updateStatistics(core, ((Sem2) core).getQueueCustomersWaitingTicketDispenser());

            Customer nextCustomer = ((Sem2) core).getQueueCustomersWaitingTicketDispenser().poll();

//            ((Sem2) core).getCustomersWaitingInShopBeforeOrder().add(nextCustomer);

            if (((Sem2) core).getCustomersWaitingInShopBeforeOrder().size() > 9) {
                System.out.println();
            }

            ((Sem2) core).getAverageUsePercentTicketStat().updateStatistics(core, ((Sem2) core).getCustomerInteractingWithTicketDispenser());
            ((Sem2) core).getCustomerInteractingWithTicketDispenser().add(nextCustomer);

            if (((Sem2) core).getCustomerInteractingWithTicketDispenser().size() > 1) {
                System.out.println();
            }

            StartInteractionTicketDispenserEvent startInteraction = new StartInteractionTicketDispenserEvent(time);
            startInteraction.setCustomer(nextCustomer);
            core.addEvent(startInteraction);
        }
    }
}
