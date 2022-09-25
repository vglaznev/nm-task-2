package rungekutta;

import java.util.function.BinaryOperator;

public interface RungeKuttaIntegrator {
    double integrate(double x, double y, double step, BinaryOperator<Double> function);
}
