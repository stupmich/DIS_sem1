package Events;

import SimulationClasses.Event;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class CloseShopEvent extends Event {
    public CloseShopEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        ((Sem2) core).getAllCustomers().removeAll(((Sem2) core).getQueueCustomersWaitingTicketDispenser());
        for (int i = 0; i < ((Sem2) core).getQueueCustomersWaitingTicketDispenser().size(); i++) {
            ((Sem2) core).incCustomersOut();
        }
        ((Sem2) core).getQueueCustomersWaitingTicketDispenser().clear();
    }
}
