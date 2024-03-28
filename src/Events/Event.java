package Events;

import SimulationClasses.EventBasedSimulationCore;

public abstract class Event {
    protected double time;

    public Event(double time) {
        this.time = time;
    }

    public abstract void execute(EventBasedSimulationCore core);

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }
}
