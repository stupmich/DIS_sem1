package Events;

import Entities.Customer;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class StartOrderPreparationEvent extends Event {
    public StartOrderPreparationEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        double next = 0.0;

        if (this.customer.getCustomerType() == Customer.CustomerType.REGULAR || this.customer.getCustomerType() == Customer.CustomerType.CONTRACT) {
            double type = ((Sem2) core).getOrderTypeGenerator().nextDouble();

            if (type < 0.3) {
                this.customer.setOrderType(Customer.OrderType.SIMPLE);
                next = ((Sem2) core).getTimeSimpleOrderPreparationGenerator().generate();
            } else if (type < 0.7) {
                this.customer.setOrderType(Customer.OrderType.MEDIUM);
                next = ((Sem2) core).getTimeMediumOrderPreparationGenerator().nextDouble(9.0,11.0);
            } else {
                this.customer.setOrderType(Customer.OrderType.DIFFICULT);
                next = ((Sem2) core).getTimeDifficultOrderPreparationGenerator().generate();
            }
            next = next * 60.0;
        } else {
            next = ((Sem2) core).getTimeOrderHandoverGenerator().generate();
        }
//        next = 0.0;
        EndPreparationEvent endPreparationEvent = new EndPreparationEvent(time + next);
        endPreparationEvent.setCustomer(customer);
        endPreparationEvent.setWorker(worker);
        core.addEvent(endPreparationEvent);

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
