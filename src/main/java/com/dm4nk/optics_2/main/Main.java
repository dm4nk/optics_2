package com.dm4nk.optics_2.main;

import com.dm4nk.optics_2.model.Model2D;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Model2D.twoDimensionalCalculate(Model2D::gauss2, "gauss");
        Model2D.twoDimensionalCalculate(Model2D::f2, "function");
    }
}
