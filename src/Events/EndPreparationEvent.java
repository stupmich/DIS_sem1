package Events;

import Entities.Customer;
import Entities.Worker;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

import java.util.LinkedList;

public class EndPreparationEvent extends Event {
    public EndPreparationEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        double tooBigOrder = ((Sem2) core).getOrderSizeGenerator().nextDouble();

        if (tooBigOrder < 60.0) {
            customer.setBlockingWorker(this.worker);
            int numberOfFreeWorkersPayment = ((Sem2) core).getWorkersPayment().size();

            if (numberOfFreeWorkersPayment == 0) {
                addCustomerToQueue(this.customer, core);
            } else if (numberOfFreeWorkersPayment == 1) {
                Worker workerPayment = ((Sem2) core).getWorkersPayment().removeLast();
                ((Sem2) core).getWorkersPaymentWorking().add(workerPayment);

                StartPaymentEvent startPaymentEvent = new StartPaymentEvent(time);
                startPaymentEvent.setCustomer(customer);
                startPaymentEvent.setWorker(workerPayment);
                core.addEvent(startPaymentEvent);
            } else {
                int indexOfWorkerPayment = ((Sem2) core).getIndexPaymentEmptyQueueGenerator().nextInt(numberOfFreeWorkersPayment);
                Worker workerPayment = ((Sem2) core).getWorkersPayment().remove(indexOfWorkerPayment);
                ((Sem2) core).getWorkersPaymentWorking().add(workerPayment);

                StartPaymentEvent startPaymentEvent = new StartPaymentEvent(time);
                startPaymentEvent.setCustomer(customer);
                startPaymentEvent.setWorker(workerPayment);
                core.addEvent(startPaymentEvent);
            }

        } else {
            OrderPickUpEvent orderPickUpEvent = new OrderPickUpEvent(time);
            orderPickUpEvent.setCustomer(customer);
            orderPickUpEvent.setWorker(worker);
            core.addEvent(orderPickUpEvent);
        }
    }

    public void addCustomerToQueue(Customer customer, EventBasedSimulationCore core) {
        int minQueueLength = Integer.MAX_VALUE;
        LinkedList<LinkedList<Customer>> shortestQueues = new LinkedList<>();

        for (LinkedList<Customer> queue : ((Sem2) core).getQueuesCustomersWaitingForPayment()) {
            if (queue.size() < minQueueLength) {
                shortestQueues.clear();
                shortestQueues.add(queue);
                minQueueLength = queue.size();
            } else if (queue.size() == minQueueLength) {
                shortestQueues.add(queue);
            }
        }

        if (shortestQueues.size() > 0) {
            int selectedQueueIndex = ((Sem2) core).getIndexPaymentSameLengthOfQueueGenerator().nextInt(shortestQueues.size());
            shortestQueues.get(selectedQueueIndex).add(customer);
        }
    }
}
