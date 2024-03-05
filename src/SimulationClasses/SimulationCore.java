package SimulationClasses;

import HelperClasses.MutableDouble;
import Observer.ISimDelegate;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;

import java.util.ArrayList;

public abstract  class SimulationCore {
    protected int executedReplications = 0;
    protected boolean isRunning = false;
    protected double result = 0.0;
    protected ArrayList<ISimDelegate> delegates;

    public void executeReplications(int numberOfReplications, XYSeries series, JFreeChart chart) {
        beforeReplications(series);
        for (int i = 0; i < numberOfReplications; i++) {
            if (!isRunning) {
                result = 0.0;
                executedReplications = 0;
//                if (mutExecRepl != null) {
//                    mutExecRepl.setValue(executedReplications);
//                }

                break;
            }
            executeOneReplication();
            afterOneReplication();

//            if (mutResult != null) {
//                mutResult.setValue(result);
//            }
//
//            if (mutExecRepl != null) {
//                mutExecRepl.setValue(executedReplications);
//            }

            if (executedReplications % (numberOfReplications * 0.0001) == 0 && series != null && chart != null) {
                series.add(executedReplications, result);
                //chart.fireChartChanged();
            }
        }
        System.out.println("vysledok" + result);
    }

    public abstract void executeOneReplication();
    public void beforeReplications(XYSeries series) {
        if (series != null) {
            series.clear();
        }
    };
    public abstract void afterOneReplication();

    public void setRunning(boolean running) {
        isRunning = running;
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
            delegate.refresh(this);
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
