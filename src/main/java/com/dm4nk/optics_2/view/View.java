package com.dm4nk.optics_2.view;

import com.dm4nk.optics_2.model.Model;
import com.dm4nk.optics_2.utility.Entity;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.apache.commons.math3.complex.Complex;

import java.io.IOException;
import java.util.List;

@Route("")
public class View extends VerticalLayout {

    public View() throws IOException {
        Model model = new Model();

        addGraph(model.getGaussBundleXKList(), "gauss bundle: e^(-x^2)");
        addGraph(model.getGaussBundleDFTXKList(), "gauss bundle DFT: e^(-x^2)");
        addGraph(model.getGaussBundleFFTXKList(), "gauss bundle FFT: e^(-x^2)");

        addGraph(model.getFunctionXKList(), "function: 2xe(-x^2/2)");
        //addGraph(model.getFunctionDFTXKList(), "function DFT: 2xe(-x^2/2)");
        addGraph(model.getFunctionFFTXKList(), "function FFT: 2xe(-x^2/2)");
    }

    private void addGraph(List<Entity<Double, Complex>> list, String title) {
        Chart chart = new Chart();
        Configuration real = chart.getConfiguration();
        Configuration imaginary = chart.getConfiguration();

        DataSeries phase = new DataSeries();
        DataSeries amplitude = new DataSeries();
        list.forEach(complex -> {
            phase.add(new DataSeriesItem(complex.getT(), complex.getE().getArgument()));
            amplitude.add(new DataSeriesItem(complex.getT(), complex.getE().abs()));
        });
        phase.setName("Phase");
        amplitude.setName("Amplitude");
        real.addSeries(phase);
        imaginary.addSeries(amplitude);
        add(new H2(title), chart);
    }
}