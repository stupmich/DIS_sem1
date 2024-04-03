package Events;

import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class LeaveShopEvent extends Event {
    public LeaveShopEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        ((Sem2) core).getAllCustomers().remove(customer);
    }
}
