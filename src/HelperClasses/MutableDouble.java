package HelperClasses;

import java.util.function.Consumer;

public class MutableDouble {
    private double value;
    private final Consumer<Double> callback;

    public MutableDouble(double value, Consumer<Double> callback) {
        this.value = value;
        this.callback = callback;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.callback.accept(value);
        this.value = value;
    }
}
