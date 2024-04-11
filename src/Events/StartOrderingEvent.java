package Events;

import SimulationClasses.Event;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class StartOrderingEvent extends Event {
    public StartOrderingEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        double next = ((Sem2) core).getTimeOrderGenerator().nextDouble(60.0,900.0);
        StartOrderPreparationEvent startOrderPreparationEvent = new StartOrderPreparationEvent(time + next);
        startOrderPreparationEvent.setCustomer(customer);
        startOrderPreparationEvent.setWorker(worker);
        core.addEvent(startOrderPreparationEvent);
    }
}
