package Events;

import Entities.Customer;
import Entities.Worker;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

import java.sql.SQLOutput;
import java.util.LinkedList;

public class StartPaymentEvent extends Event{
    public StartPaymentEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        double typePayment = ((Sem2) core).getPaymentTypeGenerator().nextDouble();
        double next = 0.0;

        if (typePayment < 0.4) {
            next = ((Sem2) core).getPaymentCashGenerator().nextInt(180,481);
        } else {
            next = ((Sem2) core).getPaymentCardGenerator().nextInt(180,361);
        }
//        next = 0.0;

        EndPaymentEvent endPaymentEvent = new EndPaymentEvent(time + next);
        endPaymentEvent.setCustomer(customer);
        endPaymentEvent.setWorker(worker);
        core.addEvent(endPaymentEvent);

//        if (((Sem2) core).getQueueCustomersWaitingTicketDispenser().size() != 0
//                && ((Sem2) core).getCustomersWaitingInShopBeforeOrder().size() < 9
//                && ((Sem2) core).getCustomerInteractingWithTicketDispenser().size() == 0) {
//            // it is more safe to reserve place in shop before interaction starts
//            ((Sem2) core).getNumberOfCustomersWaitingTicketStat().updateStatistics(core, ((Sem2) core).getQueueCustomersWaitingTicketDispenser());
//
//            Customer nextCustomer = ((Sem2) core).getQueueCustomersWaitingTicketDispenser().poll();
//
////            ((Sem2) core).getCustomersWaitingInShopBeforeOrder().add(nextCustomer);
//
//            if (((Sem2) core).getCustomersWaitingInShopBeforeOrder().size() > 9) {
//                System.out.println();
//            }
//
//            ((Sem2) core).getAverageUsePercentTicketStat().updateStatistics(core, ((Sem2) core).getCustomerInteractingWithTicketDispenser());
//            ((Sem2) core).getCustomerInteractingWithTicketDispenser().add(nextCustomer);
//
//            if (((Sem2) core).getCustomerInteractingWithTicketDispenser().size() > 1) {
//                System.out.println();
//            }
//
//            StartInteractionTicketDispenserEvent startInteraction = new StartInteractionTicketDispenserEvent(time);
//            startInteraction.setCustomer(nextCustomer);
//            core.addEvent(startInteraction);
//        }
    }
}
