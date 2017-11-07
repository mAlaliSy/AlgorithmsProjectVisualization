package com.malalisy.routingalgorithmvisualizer;

import android.graphics.Point;

/**
 * Created by mohammad on 24/04/17.
 */

public final class MathUtils { // Avoid Inheritance in utils classes

    private MathUtils() { // Avoid instantiation in utils classes

    }


    public static Point midPoint(Point p1, Point p2) {
        int x = (p1.x + p2.x) / 2;
        int y = (p1.y + p2.y) / 2;

        return new Point(x, y);
    }


}
