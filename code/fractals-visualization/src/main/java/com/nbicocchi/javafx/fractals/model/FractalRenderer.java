package com.nbicocchi.javafx.fractals.model;

import javafx.scene.image.Image;
import net.mahdilamb.colormap.Colormap;

public interface FractalRenderer {
    FractalBean getViewArea();
    void setViewArea(FractalBean viewArea);
    FractalBean getComplexArea();
    void setComplexArea(FractalBean viewArea);
    int getIterations();
    void setIterations(int iterations);
    double getLastFrameTime();
    Image render();
    Image render(FractalBean viewArea);
    FractalBean initialComplexArea();
    Colormap getColormap();
    void setColormap(Colormap colormap);
}
