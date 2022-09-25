import rungekutta.RungeKuttaSecondOrder;
import rungekutta.RungeKuttaThirdOrder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.BinaryOperator;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class Main {

    private static double leftBorder;
    private static double rightBorder;
    private static double initialPoint;
    private static double initialValue;

    private static double minimumStep;
    private static double epsilon;

    private static FileWriter out;
    private static double k = pow(2, 3);
    private static String outputFileName = getPath("output.txt");

    private static void readConfiguration(String fileName) {
        try (var in = new BufferedReader(new FileReader(fileName))) {
            var scanner = new Scanner(in);
            leftBorder = scanner.nextDouble();
            rightBorder = scanner.nextDouble();
            initialPoint = scanner.nextDouble();
            initialValue = scanner.nextDouble();
            minimumStep = scanner.nextDouble();
            epsilon = scanner.nextDouble();
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла");
        }
    }

    private static void writeResults(String info) throws IOException {
        if (out == null) {
            out = new FileWriter(outputFileName);
        }
        out.write(info);
    }

    private static String getPath(String fileName) {
        return Objects.requireNonNull(Main.class.getClassLoader().getResource(fileName)).getPath();
    }

    public static void main(String[] args) throws IOException {
        readConfiguration(getPath("input.txt"));
        BinaryOperator<Double> function = (x, y) -> pow(x, 3) + x + 3 * y / x;

        var solutionMethod = new RungeKuttaSecondOrder();
        var methodForReferenceValue = new RungeKuttaThirdOrder();

        double x = initialPoint;
        double y = initialValue;
        double step = (rightBorder - leftBorder) / 10;

        double newX, newY;
        double error, errorAbs = epsilon / k + 1;

        while (true) {
            if (errorAbs < epsilon / k) {
                step *= 2;
            }
            while (true) {
                newX = x + step;
                newY = solutionMethod.integrate(x, y, step, function);
                error = methodForReferenceValue.integrate(x, y, step, function) - newY;
                errorAbs = abs(error);
                if (errorAbs > epsilon) {
                    step /= 2;
                    if (step < minimumStep) {
                        step = minimumStep;
                        break;
                    }
                }
            }
            if (rightBorder - (x + step) < minimumStep) {
                if (rightBorder - x >= 2 * minimumStep) {
                    double xBeforeLast = rightBorder - minimumStep;
                    double yBeforeLast = solutionMethod.integrate(x, y, xBeforeLast - x, function);
                    error = methodForReferenceValue.integrate(x, y, xBeforeLast - x, function) - yBeforeLast;
                    writeResults(xBeforeLast + " " + yBeforeLast + " " + error);

                    double xLast = rightBorder;
                    double yLast = solutionMethod.integrate(xBeforeLast, yBeforeLast, minimumStep, function);
                    error = methodForReferenceValue.integrate(xBeforeLast, yBeforeLast, minimumStep, function) - yLast;
                    writeResults(xLast + " " + yLast + " " + error);
                } else if (rightBorder - x <= 1.5 * minimumStep) {
                    double xLast = rightBorder;
                    double yLast = solutionMethod.integrate(x, y, xLast - x, function);
                    error = methodForReferenceValue.integrate(x, y, xLast - x, function) - yLast;
                    writeResults(xLast + " " + yLast + " " + error);
                } else {
                    double xBeforeLast = x + (rightBorder - x) / 2;
                    double yBeforeLast = solutionMethod.integrate(x, y, (rightBorder - x) / 2, function);
                    error = methodForReferenceValue.integrate(x, y, (rightBorder - x) / 2, function) - yBeforeLast;
                    writeResults(xBeforeLast + " " + yBeforeLast + " " + error);

                    double xLast = rightBorder;
                    double yLast = solutionMethod.integrate(xBeforeLast, yBeforeLast, xLast - xBeforeLast, function);
                    error = methodForReferenceValue.integrate(xBeforeLast, yBeforeLast, xLast - xBeforeLast, function) - yLast;
                    writeResults(xLast + " " + yLast + " " + error);
                }
                break;
            }

            x = newX;
            y = newY;
            writeResults(x + " " + y + " " + error);
        }
    }
}
