package Events;

import SimulationClasses.Event;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class LeaveShopEvent extends EventElektro {
    public LeaveShopEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        customer.setTimeLeaveSystem(time);

        ((Sem2) core).setLastCustomer(customer);
        ((Sem2) core).incCustomersOut();
        ((Sem2) core).incServedCustomers();
        ((Sem2) core).getAllCustomers().remove(customer);

        double timeInSystem = customer.getTimeLeaveSystem() - customer.getTimeArrival();
        ((Sem2) core).setAverageTimeInSystem(((Sem2) core).getAverageTimeInSystemStat().calculateMean(timeInSystem));
    }
}
