package Events;

import Entities.Customer;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

import java.awt.desktop.OpenFilesEvent;
import java.util.IllegalFormatCodePointException;
import java.util.LinkedList;

public class ArrivalEvent extends Event {
    public ArrivalEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        double type = ((Sem2) core).getCustomerTypeGenerator().nextDouble();

        if (type < 0.50) {
            customer = new Customer(time, Customer.CustomerType.REGULAR, ((Sem2) core).getHighestCustomerID());
        } else if (type < 0.65) {
            customer = new Customer(time, Customer.CustomerType.CONTRACT, ((Sem2) core).getHighestCustomerID());
        } else {
            customer = new Customer(time, Customer.CustomerType.ONLINE, ((Sem2) core).getHighestCustomerID());
        }

        ((Sem2) core).incHighestCustomerID();
        ((Sem2) core).getAllCustomers().add(customer);
        ((Sem2) core).incCustomersIn();

        ((Sem2) core).getNumberOfCustomersWaitingTicketStat().updateStatistics(core, ((Sem2) core).getQueueCustomersWaitingTicketDispenser());

        if (((Sem2) core).getCustomerInteractingWithTicketDispenser().size() == 0 && ((Sem2) core).getCustomersWaitingInShopBeforeOrder().size() < 9) {
            ((Sem2) core).getAverageUsePercentTicketStat().updateStatistics(core, ((Sem2) core).getCustomerInteractingWithTicketDispenser());
            ((Sem2) core).getCustomerInteractingWithTicketDispenser().add(customer);

            StartInteractionTicketDispenserEvent startInteraction = new StartInteractionTicketDispenserEvent(time);
            startInteraction.setCustomer(customer);
            core.addEvent(startInteraction);
        } else {
            ((Sem2) core).getQueueCustomersWaitingTicketDispenser().add(customer);
        }

        double next = (((Sem2) core).getArrivalsGenerator().generate()) * 60.0;
        if (time + next <= 28800.0) {
            ArrivalEvent arrivalEvent = new ArrivalEvent(time + next);
            core.addEvent(arrivalEvent);
        }
    }
}
