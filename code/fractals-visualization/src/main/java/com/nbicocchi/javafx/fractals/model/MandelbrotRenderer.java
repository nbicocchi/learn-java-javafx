package com.nbicocchi.javafx.fractals.model;

import javafx.geometry.Point2D;

public class MandelbrotRenderer extends AbstractFractalRenderer {
    public MandelbrotRenderer(FractalBean viewArea) {
        super(viewArea);
    }

    public MandelbrotRenderer(FractalBean viewArea, FractalBean complexArea) {
        super(viewArea, complexArea);
    }

    public MandelbrotRenderer(FractalBean viewArea, FractalBean complexArea, int iterations) {
        super(viewArea, complexArea, iterations);
    }

    @Override
    int iterate(Point2D p) {
        double x = p.getX();
        double y = p.getY();
        int depth = 0;                              // number of iterations
        double re_z = 0, im_z = 0;                  // current value of z
        double re_z_sqr = 0, im_z_sqr = 0;          // squared values
        // iterate until |z| >= 2 or limit exceeded
        while (re_z_sqr + im_z_sqr < 4 && depth < iterations) {
            re_z_sqr = re_z * re_z;                 // calculate squares
            im_z_sqr = im_z * im_z;
            im_z = 2 * re_z * im_z + y;             // perform iteration
            re_z = re_z_sqr - im_z_sqr + x;
            depth += 1;
        }
        return depth;
    }

    @Override
    public FractalBean initialComplexArea() {
        return new FractalBean(-2.5, 1.5, -1.25, 1.25);
    }
}
