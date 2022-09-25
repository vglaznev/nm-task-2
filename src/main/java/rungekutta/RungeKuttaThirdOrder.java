package rungekutta;

import java.util.function.BinaryOperator;

public class RungeKuttaThirdOrder implements RungeKuttaIntegrator {
    @Override
    public double integrate(double x, double y, double step, BinaryOperator<Double> function) {
        double k1 = step * function.apply(x, y);
        double k2 = step * function.apply(x + step / 2, y + k1 / 2);
        double k3 = step * step * function.apply(x + step, y - k1 + 2 * k2);
        return y + (k1 + 4 * k2 + k3) / 6;
    }
}
