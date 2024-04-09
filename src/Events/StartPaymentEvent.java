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

        EndPaymentEvent endPaymentEvent = new EndPaymentEvent(time + next);
        endPaymentEvent.setCustomer(customer);
        endPaymentEvent.setWorker(worker);
        core.addEvent(endPaymentEvent);
    }
}
