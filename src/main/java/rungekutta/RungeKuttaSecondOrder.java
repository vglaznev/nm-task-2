package rungekutta;

import java.util.function.BinaryOperator;

public class RungeKuttaSecondOrder implements RungeKuttaIntegrator {
    @Override
    public double integrate(double x, double y, double step, BinaryOperator<Double> function) {
        double k1 = step * function.apply(x, y);
        double k2 = step * function.apply(x + step, y + k1);
        return y + (k1 + k2) / 2;
    }
}
