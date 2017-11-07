package com.malalisy.routingalgorithmvisualizer.visualization;

import android.graphics.Point;

/**
 * Created by mohammad on 27/04/17.
 */

public class Circle extends Shape {
    Point center;
    int radius;
    int label;

    public Circle(Point center, int radius, int label) {
        this.center = center;
        this.radius = radius;
        this.label = label;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
