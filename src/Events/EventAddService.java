package Events;

import Entities.Worker;
import SimulationClasses.EventBasedSimulationCore;
import SimulationClasses.Sem2;

public class EventAddService extends EventElektro{
    public EventAddService(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {

        for (int i = 0; i < 4; i++) {
            Worker newWorker = new Worker(((Sem2) core).getHighestWorkersOrderID(), Worker.WorkerType.ORDER_REGULAR_AND_CONTRACT);
            ((Sem2) core).getWorkersOrderNormal().add(newWorker);
            ((Sem2) core).incHighestWorkersOrderID();
        }
    }
}
