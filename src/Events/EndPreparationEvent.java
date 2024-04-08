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

        if (tooBigOrder < 0.6) {
            // order is too big -> worker stays blocked till customer comes back
            customer.setBlockingWorker(this.worker);
            customer.setSizeOfOrder(Customer.SizeOfOrder.BIG);
            int numberOfFreeWorkersPayment = ((Sem2) core).getWorkersPayment().size();

            if (numberOfFreeWorkersPayment == 0) {
                // no free workers -> go to the queue according the rules
                addCustomerToQueue(this.customer, core);
            } else if (numberOfFreeWorkersPayment == 1) {
                // one free worker -> go to him and start payment
                Worker workerPayment = ((Sem2) core).getWorkersPayment().removeLast();
                workerPayment.setIdCustomer(customer.getId());
                workerPayment.setCustomer(customer);
                ((Sem2) core).getWorkersPaymentWorking().add(workerPayment);

                StartPaymentEvent startPaymentEvent = new StartPaymentEvent(time);
                startPaymentEvent.setCustomer(customer);
                startPaymentEvent.setWorker(workerPayment);
                core.addEvent(startPaymentEvent);
            } else {
                // more free workers -> pick random
                int indexOfWorkerPayment = ((Sem2) core).getIndexPaymentEmptyQueueGenerator()[numberOfFreeWorkersPayment - 2].nextInt(numberOfFreeWorkersPayment);

                Worker workerPayment = ((Sem2) core).getWorkersPayment().remove(indexOfWorkerPayment);
                workerPayment.setIdCustomer(customer.getId());
                workerPayment.setCustomer(customer);
                ((Sem2) core).getWorkersPaymentWorking().add(workerPayment);

                StartPaymentEvent startPaymentEvent = new StartPaymentEvent(time);
                startPaymentEvent.setCustomer(customer);
                startPaymentEvent.setWorker(workerPayment);
                core.addEvent(startPaymentEvent);
            }

        } else {
            // pick up order
            customer.setSizeOfOrder(Customer.SizeOfOrder.REGULAR);
            OrderPickUpEvent orderPickUpEvent = new OrderPickUpEvent(time);
            orderPickUpEvent.setCustomer(customer);
            orderPickUpEvent.setWorker(worker);
            core.addEvent(orderPickUpEvent);
        }

    }

    public void addCustomerToQueue(Customer customer, EventBasedSimulationCore core) {
        int minQueueLength = Integer.MAX_VALUE;
        LinkedList<LinkedList<Customer>> shortestQueues = new LinkedList<>();

        // get shortest queues
        for (LinkedList<Customer> queue : ((Sem2) core).getQueuesCustomersWaitingForPayment()) {
            if (queue.size() < minQueueLength) {
                shortestQueues.clear();
                shortestQueues.add(queue);
                minQueueLength = queue.size();
            } else if (queue.size() == minQueueLength) {
                shortestQueues.add(queue);
            }
        }

        // pick randomly queue where customer goes
        if (shortestQueues.size() == 1 ) {
            shortestQueues.get(0).add(customer);
        } else if (shortestQueues.size() > 1) {
            int selectedQueueIndex = ((Sem2) core).getIndexPaymentSameLengthOfQueueGenerator()[shortestQueues.size() - 2].nextInt(shortestQueues.size());
            shortestQueues.get(selectedQueueIndex).add(customer);
        }
    }
}
