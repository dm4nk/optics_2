package com.dm4nk.optics_2.utility;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

public class FunctionsUtility {
    public static double[] convertComplexListToDoubleArray(List<Complex> points) {
        double[] resultArray = new double[points.size() * 2];
        for (int i = 0; i < points.size(); i += 1) {
            resultArray[i * 2] = points.get(i).getReal();
            resultArray[i * 2 + 1] = points.get(i).getImaginary();
        }
        return resultArray;
    }

    public static List<Complex> convertDoubleArrayToComplexList(double[] array) {
        List<Complex> resultList = new ArrayList<>();
        for (int i = 0; i < array.length / 2; i += 1) {//array length / 2 = m
            resultList.add(new Complex(array[i * 2], array[i * 2 + 1]));
        }
        return resultList;
    }

    public static <T> List<T> swapList(List<T> list) {
        int listMiddle = list.size() / 2;
        List<T> resultList = new ArrayList<>(list.subList(listMiddle, list.size()));
        resultList.addAll(list.subList(0, listMiddle));
        return resultList;
    }

    public static List<Complex> addZerosToListToSize(List<Complex> list, int size) {
        int zeroCount = size - list.size();
        for (int i = 0; i < zeroCount; i += 2) {
            list.add(Complex.ZERO);
            list.add(0, Complex.ZERO);
        }
        return list;
    }
}
