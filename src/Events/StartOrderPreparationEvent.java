package Events;

import Entities.Customer;
import SimulationClasses.Event;
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

        EndPreparationEvent endPreparationEvent = new EndPreparationEvent(time + next);
        endPreparationEvent.setCustomer(customer);
        endPreparationEvent.setWorker(worker);
        core.addEvent(endPreparationEvent);
    }
}
