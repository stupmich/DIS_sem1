package SimulationClasses;

import Observer.ISimDelegate;
import java.util.ArrayList;

public abstract  class SimulationCore {
    protected int executedReplications = 0;
    protected boolean isRunning = false;
    protected double result = 0.0;
    protected ArrayList<ISimDelegate> delegates;

    public void executeReplications(int numberOfReplications) {
        beforeReplications();

        for (int i = 0; i < numberOfReplications; i++) {
            if (!isRunning) {
                break;
            }
            executeOneReplication();
            afterOneReplication();
        }
        this.isRunning = false;
        //System.out.println("vysledok " + result);
    }

    public abstract void executeOneReplication();

    public void beforeReplications() {
        result = 0.0;
        executedReplications = 0;
    };
    public abstract void afterOneReplication();

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public void setExecutedReplications(int executedReplications) {
        this.executedReplications = executedReplications;
    }

    public void registerDelegate(ISimDelegate delegate)
    {
        delegates.add(delegate);
    }

    protected void refreshGUI()
    {
        for (ISimDelegate delegate : delegates)
        {
            delegate.refresh();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getExecutedReplications() {
        return executedReplications;
    }

    public double getResult() {
        return result;
    }
}
