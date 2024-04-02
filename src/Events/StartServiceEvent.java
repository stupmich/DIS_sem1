package Events;

import SimulationClasses.EventBasedSimulationCore;

public class StartServiceEvent extends Event {
    public StartServiceEvent(double time) {
        super(time);
    }

    @Override
    public void execute(EventBasedSimulationCore core) {

    }
}
