package Events;

import Entities.Customer;
import SimulationClasses.Event;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class EndPaymentEvent extends Event {
    public EndPaymentEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        if (customer.getBlockingWorker() != null) {
            // order of this customer was too big, he needs to pick up his order
            double next = ((Sem2) core).getTimeBigOrderPickUpGenerator().nextDouble(30.0,70.0);

            OrderPickUpEvent orderPickUpEvent = new OrderPickUpEvent(time + next);
            orderPickUpEvent.setCustomer(customer);
            orderPickUpEvent.setWorker(customer.getBlockingWorker());
            core.addEvent(orderPickUpEvent);
        } else {
            // order of this customer was not too big, he just leaves the shop
            LeaveShopEvent leaveShopEvent = new LeaveShopEvent(time);
            leaveShopEvent.setCustomer(customer);
            core.addEvent(leaveShopEvent);
        }

        if (((Sem2) core).getQueuesCustomersWaitingForPayment().get(this.worker.getId()).isEmpty()) {
            // no customers in queue for this worker -> worker free
            worker.setIdCustomer(-1);
            worker.setCustomer(null);
            ((Sem2) core).getWorkersPayment().add(worker);

            ((Sem2) core).getAverageUsePercentPaymentStat().updateStatistics(core, ((Sem2) core).getWorkersPaymentWorking());
            ((Sem2) core).getWorkersPaymentWorking().remove(worker);
        } else {
            // customer is waiting in queue for this worker -> new payment
            Customer nextCustomer = ((Sem2) core).getQueuesCustomersWaitingForPayment().get(this.worker.getId()).removeFirst();
            worker.setIdCustomer(nextCustomer.getId());
            worker.setCustomer(nextCustomer);

            StartPaymentEvent startPaymentEvent = new StartPaymentEvent(time);
            startPaymentEvent.setCustomer(nextCustomer);
            startPaymentEvent.setWorker(worker);
            core.addEvent(startPaymentEvent);
        }
    }
}
