package SimulationClasses;

import Events.Event;

import java.util.PriorityQueue;

public abstract class EventBasedSimulationCore  extends SimulationCore{
    protected double currentTime;
    protected double maxTime;
    protected PriorityQueue<Event> timeLine;
    protected int timeGap;
    protected boolean turboMode;
    protected boolean pause = false;

    public EventBasedSimulationCore(double maxTime, boolean turboMode, int timeGap) {
        this.maxTime = maxTime;
        this.turboMode = turboMode;
        this.timeGap = timeGap;
        this.timeLine = new PriorityQueue<Event>();
    }

    @Override
    public void executeOneReplication() {
        Event lvEvent;
        while(!timeLine.isEmpty() && this.isRunning) {
            lvEvent = timeLine.poll();
            currentTime = lvEvent.getTime();

            if (currentTime > maxTime) {
                break;
            }

            lvEvent.execute(this);
            this.updateStatistics();
            if (!turboMode) {
                this.refreshGUI();
            }

            while(pause) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    @Override
    public void afterOneReplication() {
        if (!turboMode) {
            this.refreshGUI();
        }
    }

    public void addEvent(Event event) {
        if (event.getTime() < currentTime) {
            throw new IllegalArgumentException("Event time < current time");
        } else {
            timeLine.add(event);
        }
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isTurboMode() {
        return turboMode;
    }

    public void setTurboMode(boolean turboMode) {
        this.turboMode = turboMode;
    }

    public int getTimeGap() {
        return timeGap;
    }

    public void setTimeGap(int timeGap) {
        this.timeGap = timeGap;
    }

    public abstract void updateStatistics();
}
