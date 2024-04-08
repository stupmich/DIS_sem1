package Events;

import Entities.Customer;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class CloseShopEvent extends Event{
    public CloseShopEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        for (Customer c : ((Sem2) core).getQueueCustomersWaitingTicketDispenser()) {
            // 17:00 -> customers waiting for ticket leave shop
            LeaveShopEvent leaveShopEvent = new LeaveShopEvent(time);
            leaveShopEvent.setCustomer(c);
            core.addEvent(leaveShopEvent);
        }

//        ((Sem2) core).getNumberOfCustomersWaitingTicketStat().updateStatistics(core, ((Sem2) core).getQueueCustomersWaitingTicketDispenser());
//        ((Sem2) core).getQueueCustomersWaitingTicketDispenser().clear();
    }
}
