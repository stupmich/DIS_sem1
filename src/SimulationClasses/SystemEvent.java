package SimulationClasses;

public class SystemEvent extends Event {
    private int timeGap;

    public SystemEvent(double time, int timeGap) {
        super(time);
        this.timeGap = timeGap;
    }

    @Override
    public Event clone() {
        return null;
    }

    @Override
    public void execute(EventBasedSimulationCore core) {
        SystemEvent nextSystemEvent = new SystemEvent(this.time += timeGap, core.getTimeGap());
        core.addEvent(nextSystemEvent);
        try {
            Thread.sleep(1000 / timeGap );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
