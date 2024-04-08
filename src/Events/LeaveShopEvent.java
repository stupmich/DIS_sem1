package Events;

import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class LeaveShopEvent extends Event {
    public LeaveShopEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        if (time > 28800 && ((Sem2) core).getAllCustomers().size() == 1) {
            ((Sem2) core).setAverageTimeLeaveSystem(((Sem2) core).getAverageTimeLeaveSystemStat().calculateMean(time + 32400.0));
        }

        customer.setTimeLeaveSystem(time);
        double time = customer.getTimeLeaveSystem() - customer.getTimeArrival();
        ((Sem2) core).setAverageTimeInSystem(((Sem2) core).getAverageTimeInSystemStat().calculateMean(time));

        ((Sem2) core).incCustomersOut();

        ((Sem2) core).getAllCustomers().remove(customer);
    }
}
