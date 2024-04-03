package Events;

import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class StartInteractionTicketDispenserEvent extends Event {
    public StartInteractionTicketDispenserEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        ((Sem2) core).setCustomerInteractingWithTicketDispenser(customer);

        double next = ((Sem2) core).getTimeTicketDispenserGenerator().nextDouble(30.0,180.0);
        MoveToShopEvent moveToShopEvent = new MoveToShopEvent(this.time + next);
        moveToShopEvent.setCustomer(customer);
        core.addEvent(moveToShopEvent);
    }
}
