package Events;

import Entities.Customer;
import Entities.Worker;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

import java.util.LinkedList;

public class OrderPickUpEvent extends Event {
    public OrderPickUpEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        if (customer.getBlockingWorker() == null) {
            // order of this customer was not too big, he picks up order and continues with payment
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
            // order of this customer was too big, he just picks up order and leaves the shop
            LeaveShopEvent leaveShopEvent = new LeaveShopEvent(time);
            leaveShopEvent.setCustomer(customer);
            core.addEvent(leaveShopEvent);
        }

        // Customer picked up order -> worker is free again and can serve another customer
        if (this.customer.getCustomerType() == Customer.CustomerType.REGULAR || this.customer.getCustomerType() == Customer.CustomerType.CONTRACT) {
            Customer nextCustomerNormal = null;

            for (Customer c : ((Sem2) core).getCustomersWaitingInShopBeforeOrder()) {
                if (c.getCustomerType() == Customer.CustomerType.CONTRACT) {
                    nextCustomerNormal = c;
                    break;
                }

                if (c.getCustomerType() == Customer.CustomerType.REGULAR && nextCustomerNormal == null) {
                    nextCustomerNormal = c;
                }
            }

            if (nextCustomerNormal != null) {
                StartServiceEvent startServiceEvent = new StartServiceEvent(time);
                startServiceEvent.setCustomer(nextCustomerNormal);
                startServiceEvent.setWorker(worker);
                core.addEvent(startServiceEvent);
            } else { // no new customer -> worker is free
                ((Sem2) core).getWorkersOrderNormal().add(worker);
                ((Sem2) core).getWorkersOrderWorkingNormal().remove(worker);
            }

        } else {
            Customer nextOnlineCustomer = null;
            for (Customer c : ((Sem2) core).getCustomersWaitingInShopBeforeOrder()) {
                if (c.getCustomerType() == Customer.CustomerType.ONLINE) {
                    nextOnlineCustomer = c;
                    break;
                }
            }

            if (nextOnlineCustomer != null) {
                StartServiceEvent startServiceEvent = new StartServiceEvent(time);
                startServiceEvent.setCustomer(nextOnlineCustomer);
                startServiceEvent.setWorker(worker);
                core.addEvent(startServiceEvent);
            } else { // no new customer -> worker is free
                ((Sem2) core).getWorkersOrderOnline().add(worker);
                ((Sem2) core).getWorkersOrderWorkingOnline().remove(worker);
            }
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
