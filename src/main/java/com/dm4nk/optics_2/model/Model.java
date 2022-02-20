package com.dm4nk.optics_2.model;

import com.dm4nk.optics_2.utility.Entity;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import lombok.Getter;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.dm4nk.optics_2.utility.FunctionsUtility.*;
import static java.lang.Math.exp;
import static java.lang.Math.pow;

public class Model {
    public static final double a1 = -5;
    public static final double a2 = 5;
    public static final int m = 2048;
    public static final int n = 200;
    public static final double b2 = pow(n, 2) / (4 * a2 * m);
    public static final double b1 = b2 * -1;
    public static double h_n = (a2-a1)/n;
    public static double l_n = (b2-b1)/n;

    private List<Double> x_k = new ArrayList<>();
    private List<Double> y_k = new ArrayList<>();
    private List<Complex> gaussBundleList = new ArrayList<>();
    @Getter
    private List<Entity<Double, Complex>> gaussBundleXKList = new ArrayList<>();

    private List<Complex> gaussBundleDFTList = new ArrayList<>();
    @Getter
    private List<Entity<Double, Complex>> gaussBundleDFTXKList = new ArrayList<>();

    private List<Complex> gaussBundleFFTList = new ArrayList<>();
    @Getter
    private List<Entity<Double, Complex>> gaussBundleFFTXKList = new ArrayList<>();


    public Model() {
        init();
    }

    private double f(double x) {
        return 2*x*exp(-pow(x, 2)/2);
    }

    private double gaussBundle(double x) {
        return exp(-pow(x, 2));
    }

    public List<Complex> DFT(List<Complex> function){
        DoubleFFT_1D discreteFourierTransform = new DoubleFFT_1D(m);

        double[] resultArray = convertComplexListToDoubleArray(
                swapList(addZerosToListToSize(function, m))
        );

        discreteFourierTransform.complexForward(resultArray);
        List<Complex> resultListAfterDFT = convertDoubleArrayToComplexList(resultArray);
        resultListAfterDFT = swapList(resultListAfterDFT).subList((m - n) / 2, (m + n) / 2);
        resultListAfterDFT.forEach(complex -> complex.multiply(h_n));

        return resultListAfterDFT;
    }

    public List<Complex> FFT(List<Complex> function){



        Complex[] res = swapList(addZerosToListToSize(function, m))
        .toArray(new Complex[0]);

        FastFourierTransformer fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
        res = fastFourierTransformer.transform(res, TransformType.FORWARD);
        List<Complex> resultListAfterFFT = Arrays.stream(res)
                .sequential()
                .map(val -> val.multiply(h_n))
                .collect(Collectors.toList());
        resultListAfterFFT = swapList(resultListAfterFFT).subList((m - n) / 2, (m + n) / 2);
        return resultListAfterFFT;
    }

    private void init() {
        for(int i = 0; i < n; ++i)
            x_k.add(a1 + i * h_n);

        for(int i = 0; i < n; ++i)
            y_k.add(b1 + i * l_n);

        gaussBundleList = x_k.stream()
                        .map(x -> new Complex(gaussBundle(x), 0))
                        .collect(Collectors.toList());

        for(int i = 0; i < x_k.size(); ++i)
            gaussBundleXKList.add(new Entity<>(x_k.get(i), gaussBundleList.get(i)));

        gaussBundleDFTList = DFT(gaussBundleList);

        for(int i = 0; i < y_k.size(); ++i)
            gaussBundleDFTXKList.add(new Entity<>(y_k.get(i), gaussBundleDFTList.get(i)));

        gaussBundleFFTList = FFT(gaussBundleList);

        for(int i = 0; i < y_k.size(); ++i)
            gaussBundleFFTXKList.add(new Entity<>(y_k.get(i), gaussBundleFFTList.get(i)));
    }
}
