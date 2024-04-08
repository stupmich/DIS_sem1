package Events;

import Entities.Customer;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class StartOrderingEvent extends Event{
    public StartOrderingEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        double next = ((Sem2) core).getTimeOrderGenerator().nextDouble(60,900);
//        next = 0.0;
        StartOrderPreparationEvent startOrderPreparationEvent = new StartOrderPreparationEvent(time + next);
        startOrderPreparationEvent.setCustomer(customer);
        startOrderPreparationEvent.setWorker(worker);
        core.addEvent(startOrderPreparationEvent);

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
