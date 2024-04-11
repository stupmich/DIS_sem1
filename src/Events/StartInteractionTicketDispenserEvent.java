package Events;

import Entities.Customer;
import SimulationClasses.Event;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class StartInteractionTicketDispenserEvent extends Event {
    public StartInteractionTicketDispenserEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        double type = ((Sem2) core).getCustomerTypeGenerator().nextDouble();

        if (type < 0.5) {
            customer.setCustomerType(Customer.CustomerType.REGULAR);
        } else if (type < 0.65) {
            customer.setCustomerType(Customer.CustomerType.CONTRACT);
        } else {
            customer.setCustomerType(Customer.CustomerType.ONLINE);
        }

        double timeTicket = time - customer.getTimeArrival();
        ((Sem2) core).setAverageTimeTicket(((Sem2) core).getAverageTimeTicketStat().calculateMean(timeTicket));

        double next = ((Sem2) core).getTimeTicketDispenserGenerator().nextDouble(30.0,120.0);
        MoveToShopEvent moveToShopEvent = new MoveToShopEvent(this.time + next);
        moveToShopEvent.setCustomer(customer);
        core.addEvent(moveToShopEvent);
    }
}
