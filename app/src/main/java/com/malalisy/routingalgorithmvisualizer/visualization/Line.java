package com.malalisy.routingalgorithmvisualizer.visualization;

import android.graphics.Point;

/**
 * Created by mohammad on 27/04/17.
 */

public class Line extends Shape {

    Point start, end;
    int label;

    public Line(Point start, Point end, int label) {
        this.start = start;
        this.end = end;
        this.label = label;
    }


    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
