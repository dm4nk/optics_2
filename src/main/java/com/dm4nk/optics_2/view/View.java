package com.dm4nk.optics_2.view;

import com.dm4nk.optics_2.model.Model;
import com.dm4nk.optics_2.utility.Entity;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.charts.Chart;
import org.apache.commons.math3.complex.Complex;

import java.util.List;

import static org.apache.commons.math3.analysis.FunctionUtils.add;

@Route("")
public class View extends VerticalLayout {

    public View() {
        Model model = new Model();

        addGraph(model.getGaussBundleXKList(), "gauss bundle: e^(-x^2)");
        addGraph(model.getGaussBundleDFTXKList(), "gauss bundle DFT: e^(-x^2)");
        addGraph(model.getGaussBundleFFTXKList(), "gauss bundle FFT: e^(-x^2)");
    }

    private void addGraph(List<Entity<Double, Complex>> list, String title) {
        Chart gaussBundle = new Chart();
        Configuration gaussBundleReal = gaussBundle.getConfiguration();
        Configuration gaussBundleImaginary = gaussBundle.getConfiguration();

        DataSeries gaussBundleRealDS = new DataSeries();
        DataSeries gaussBundleImaginaryDS = new DataSeries();
        list.forEach(complex -> {
                    gaussBundleRealDS.add(new DataSeriesItem(complex.getT(), complex.getE().getReal()));
                    gaussBundleImaginaryDS.add(new DataSeriesItem(complex.getT(), complex.getE().getImaginary()));
                });
        gaussBundleRealDS.setName("Real");
        gaussBundleImaginaryDS.setName("Imaginary");
        gaussBundleReal.addSeries(gaussBundleRealDS);
        gaussBundleImaginary.addSeries(gaussBundleImaginaryDS);
        add(new H2(title), gaussBundle);
    }
}
