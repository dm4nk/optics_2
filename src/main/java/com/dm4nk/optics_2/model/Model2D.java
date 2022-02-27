package com.dm4nk.optics_2.model;

import com.dm4nk.optics_2.utility.ExcelWriter;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.dm4nk.optics_2.utility.FunctionsUtility.addZerosToListToSize;
import static com.dm4nk.optics_2.utility.FunctionsUtility.swapList;
import static java.lang.Math.exp;
import static java.lang.Math.pow;

public class Model2D {
    public static final double a1 = -5;
    public static final double a2 = 5;
    public static final int m = 2048;
    public static final int n = 200;
    public static final double b2 = pow(n, 2) / (4 * a2 * m);
    public static final double b1 = b2 * -1;
    public static double l_n = (b2 - b1) / n;
    public static double h_n = (a2 - a1) / n;

    public static void twoDimensionalCalculate(BiFunction<Double, Double, Double> function, String filaHeader) throws IOException {
        List<Double> xList = IntStream.range(0, n)
                .boxed()
                .map(e -> a1 + e * h_n)
                .collect(Collectors.toList());

        List<Double> yList = IntStream.range(0, n)
                .boxed()
                .map(e -> a1 + e * h_n)
                .collect(Collectors.toList());

        List<List<Complex>> fList = calculate2DFunction(xList, yList, function);
        ExcelWriter.write(filaHeader + "_phase", xList, yList, phase2(fList));
        ExcelWriter.write(filaHeader + "_amplitude", xList, yList, amplitude2(fList));

        //FFT
        addZerosToListSize2(fList, m);
        fList = swapList2(fList);
        List<List<Complex>> FListFft = fastFourierTransform2(fList);
        FListFft = multiply2(FListFft, h_n);
        FListFft = swapList2(FListFft);
        List<List<Complex>> FListFromCenter = getElementsFromCenter2(FListFft, n);
        ExcelWriter.write(filaHeader + "_FFT phase", xList, yList, phase2(FListFromCenter));
        ExcelWriter.write(filaHeader + "_FFT amplitude", xList, yList, amplitude2(FListFromCenter));
    }

    private static List<List<Complex>> calculate2DFunction(List<Double> xList, List<Double> yList,
                                                           BiFunction<Double, Double, Double> function) {
        List<List<Complex>> result = new ArrayList<>();
        for (Double y : yList) {
            List<Complex> row = new ArrayList<>();
            for (Double x : xList) {
                row.add(Complex.valueOf(function.apply(x, y)));
            }
            result.add(row);
        }
        return result;
    }

    private static double f(double x) {
        return 2 * x * exp(-pow(x, 2) / 2);
    }

    public static double f2(double x, double y) {
        return f(x) * f(y);
    }

    public static double gauss2(double x, double y) {
        return Math.exp(-pow(x, 2) - pow(y, 2));
    }

    private static List<Double> phase(List<Complex> source) {
        return source.stream().map(Complex::getArgument).collect(Collectors.toList());
    }

    private static List<Double> amplitude(List<Complex> source) {
        return source.stream().map(Complex::abs).collect(Collectors.toList());
    }

    private static List<List<Double>> phase2(List<List<Complex>> source) {
        return source.stream().map(Model2D::phase).collect(Collectors.toList());
    }

    private static List<List<Double>> amplitude2(List<List<Complex>> source) {
        return source.stream().map(Model2D::amplitude).collect(Collectors.toList());
    }

    private static void addZerosToListSize2(List<List<Complex>> list, int needSize) {
        int zerosSize = needSize - list.size();
        for (List<Complex> row : list) {
            addZerosToListToSize(row, needSize);
        }

        List<Complex> zeros = Collections.nCopies(list.size() + zerosSize, Complex.ZERO);
        for (int i = 0; i < zerosSize; i += 2) {
            list.add(new ArrayList<>(zeros));
            list.add(0, new ArrayList<>(zeros));
        }
    }

    private static List<List<Complex>> swapList2(List<List<Complex>> list) {
        List<List<Complex>> result = new ArrayList<>();
        for (List<Complex> row : swapList(list)) {
            result.add(swapList(row));
        }
        return result;
    }

    private static List<Complex> fastFourierTransform(List<Complex> toTransform) {
        return Arrays.asList(
                new FastFourierTransformer(DftNormalization.STANDARD)
                        .transform(toTransform.toArray(new Complex[0]), TransformType.FORWARD)
        );
    }

    private static List<List<Complex>> fastFourierTransform2(List<List<Complex>> toTransform) {
        List<List<Complex>> result = new ArrayList<>();
        for (List<Complex> row : toTransform) {
            result.add(fastFourierTransform(row));
        }

        result = transpose(result);
        for (int i = 0; i < result.size(); i++) {
            result.set(i, fastFourierTransform(result.get(i)));
        }
        return transpose(result);
    }

    private static List<List<Complex>> transpose(List<List<Complex>> matrix) {
        Complex[][] array = matrix.stream().map(e -> e.toArray(new Complex[0])).toArray(Complex[][]::new);
        Complex[][] transposedArray = MatrixUtils.createFieldMatrix(array).transpose().getData();
        return Arrays.stream(transposedArray)
                .map(Arrays::asList)
                .collect(Collectors.toList());
    }

    private static List<Complex> multiply(List<Complex> source, double multiplier) {
        return source.stream().map(e -> e.multiply(multiplier)).collect(Collectors.toList());
    }

    private static List<List<Complex>> multiply2(List<List<Complex>> source, double multiplier) {
        var result = new ArrayList<List<Complex>>();
        for (List<Complex> row : source) {
            result.add(multiply(row, multiplier));
        }
        return result;
    }

    private static <T> List<T> getElementsFromCenter(List<T> list, int size) {
        int center = list.size() / 2;
        return list.subList(center - (size / 2), center + (size / 2));
    }

    private static <T> List<List<T>> getElementsFromCenter2(List<List<T>> list, int size) {
        var elementsFromCenter = getElementsFromCenter(list, size);
        var result = new ArrayList<List<T>>();
        for (List<T> row : elementsFromCenter) {
            result.add(getElementsFromCenter(row, size));
        }
        return result;
    }
}
