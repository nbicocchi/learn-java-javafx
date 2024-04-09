package com.nbicocchi.javafx.fractals.model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import net.mahdilamb.colormap.Colormap;
import net.mahdilamb.colormap.Colormaps;

public abstract class AbstractFractalRenderer implements FractalRenderer {
    public static final int DEFAULT_ITERATIONS = 128;
    FractalBean viewArea;
    FractalBean complexArea;
    Colormap colormap;
    double lastFrameTime;
    int iterations;

    public AbstractFractalRenderer(FractalBean viewArea) {
        this.viewArea = viewArea;
        this.complexArea = initialComplexArea();
        this.iterations = DEFAULT_ITERATIONS;
        this.colormap = Colormaps.get("Edge");
    }

    public AbstractFractalRenderer(FractalBean viewArea, FractalBean complexArea) {
        this.viewArea = viewArea;
        this.complexArea = complexArea;
        this.iterations = DEFAULT_ITERATIONS;
        this.colormap = Colormaps.get("Edge");
    }

    public AbstractFractalRenderer(FractalBean viewArea, FractalBean complexArea, int iterations) {
        this.viewArea = viewArea;
        this.complexArea = complexArea;
        this.iterations = iterations;
        this.colormap = Colormaps.get("Edge");
    }

    @Override
    public FractalBean getViewArea() {
        return viewArea;
    }

    @Override
    public void setViewArea(FractalBean viewArea) {
        this.viewArea = viewArea;
    }

    @Override
    public FractalBean getComplexArea() {
        return complexArea;
    }

    @Override
    public void setComplexArea(FractalBean complexArea) {
        this.complexArea = complexArea;
    }

    @Override
    public int getIterations() {
        return iterations;
    }

    @Override
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    @Override
    public double getLastFrameTime() {
        return lastFrameTime;
    }

    public Image render() {
        return render(viewArea);
    }

    public Image render(FractalBean viewArea) {
        long begin = System.nanoTime();
        WritableImage image = new WritableImage((int) viewArea.getWidth(), (int) viewArea.getHeight());
        for (int y = 0; y < viewArea.getHeight(); ++y) {
            for (int x = 0; x < viewArea.getWidth(); ++x) {
                Point2D p = viewArea.mapToBean(complexArea, x, y);
                image.getPixelWriter().setColor(x, y, colorize(iterate(p)));
            }
        }
        lastFrameTime = (System.nanoTime() - begin) / 1000000.0;
        return image;
    }

    public abstract FractalBean initialComplexArea();

    public Colormap getColormap() {
        return colormap;
    }

    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    Color colorize(int depth) {
        java.awt.Color color = colormap.get(depth / (double) iterations);
        return depth == iterations ? Color.BLACK : Color.rgb(color.getRed(), color.getGreen(), color.getBlue());
    }

    abstract int iterate(Point2D p);
}
