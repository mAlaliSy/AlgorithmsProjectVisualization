package com.malalisy.routingalgorithmvisualizer.visualization;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


import com.malalisy.routingalgorithmvisualizer.AppConstants;
import com.malalisy.routingalgorithmvisualizer.MathUtils;
import com.malalisy.routingalgorithmvisualizer.graph.Graph;
import com.malalisy.routingalgorithmvisualizer.graph.Vertex;

import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;


/**
 * Created by mohammad on 23/04/17.
 */

public class VisualizationView extends View {

    private static final String TAG = "VisualizationView";


    public static final int VERTEX_TEXT_SIZE = 15;
    public static final int WEIGHT_TEXT_SIZE = 15;
    public static final int VERTEX_RADIUS = 50;
    public static final int EDGE_WIDTH = 1;
    public static final int SELECTED_EDGE_WIDTH = 3;
    public static final int PATH_VERTEX_BORDER = 7;


    public static final int VERTEX_COLOR = 0xFF3F51B5;
    public static final int EDGE_COLOR = 0xFF263238;
    public static final int PATH_COLOR = 0xFF00C853;
    public static final int VERTEX_TEXT_COLOR = 0xffffffff;
    public static final int WEIGHT_COLOR = 0xFFFF0000;
    public static final int PATH_VERTEX_BORDER_COLOR = 0xFF880E4F;


    public static final int DEFAULT_HEIGHT = 400;
    public static final int DEFAULT_WIDTH = 300;

    private int minHeight = 300;

    private int minWidth;

    private int numberOfVertices;

    private float screenDensity;
    private Point viewSize;

    Paint vertexPaint;
    Paint normalEdgePaint;
    Paint selectedEdgePaint;
    Paint vertexTextPaint;
    Paint weightPaint;
    Paint pathVertexPaint;


    private Graph graph;
    private Point[] verticesPoints;


    boolean drawPath = false;
    int destinationIndex;


    Stack<Shape> pathToDraw; //  Path Lines and Vertices to draw step by step to make animation..
    LinkedList<Shape> drawnPath; // Drawn Path Lines and Vertices

    Thread pathDrawer;
    Handler handler;

    public VisualizationView(Context context) {
        super(context);

        init();

    }

    private void init() {

        screenDensity = getResources().getDisplayMetrics().density;


        vertexPaint = new Paint();
        vertexPaint.setColor(VERTEX_COLOR);
        vertexPaint.setAntiAlias(true);


        normalEdgePaint = new Paint();
        normalEdgePaint.setColor(EDGE_COLOR);
        normalEdgePaint.setAntiAlias(true);
        normalEdgePaint.setStrokeWidth(EDGE_WIDTH * screenDensity);
        normalEdgePaint.setAntiAlias(true);


        selectedEdgePaint = new Paint();
        selectedEdgePaint.setColor(PATH_COLOR);
        selectedEdgePaint.setStrokeWidth(SELECTED_EDGE_WIDTH * screenDensity);
        selectedEdgePaint.setAntiAlias(true);

        vertexTextPaint = new Paint();
        vertexTextPaint.setStyle(Paint.Style.FILL);
        vertexTextPaint.setColor(VERTEX_TEXT_COLOR);
        vertexTextPaint.setTextSize(VERTEX_TEXT_SIZE * screenDensity);
        vertexTextPaint.setTextAlign(Paint.Align.CENTER);
        vertexTextPaint.setAntiAlias(true);

        weightPaint = new Paint();
        weightPaint.setStyle(Paint.Style.FILL);
        weightPaint.setColor(WEIGHT_COLOR);
        weightPaint.setTextSize(WEIGHT_TEXT_SIZE * screenDensity);
        weightPaint.setTextAlign(Paint.Align.CENTER);
        weightPaint.setAntiAlias(true);


        pathVertexPaint = new Paint();
        pathVertexPaint.setStyle(Paint.Style.FILL);
        pathVertexPaint.setAntiAlias(true);
        pathVertexPaint.setColor(PATH_VERTEX_BORDER_COLOR);

        viewSize = new Point();


        pathToDraw = new Stack<>();
        drawnPath = new LinkedList<>();
        handler = new Handler(getContext().getMainLooper());

    }

    public VisualizationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();


    }

    public VisualizationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VisualizationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();

    }


    public void onPause() {


    }


    public void onResume() {


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewSize.x = getMeasuredWidth();
        viewSize.y = getMeasuredHeight();


        Point temp = new Point();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getSize(temp);

        minWidth = temp.x - (int) (16 * screenDensity);

        updateSize();
        invalidate();
    }


    @Override
    public void onDraw(Canvas canvas) {


        if (verticesPoints == null)
            return;


        canvas.drawColor(Color.WHITE);


        // Drawing edges
        for (int i = 0; i < verticesPoints.length; i++) {

            for (int j = 0; j < verticesPoints.length; j++) {

                if (graph.isAdjacent(i, j)) {

                    double weight = graph.getWeight(i, j);


                    canvas.drawLine(verticesPoints[i].x, verticesPoints[i].y, verticesPoints[j].x, verticesPoints[j].y, normalEdgePaint);

                    Point midPoint = MathUtils.midPoint(verticesPoints[i], verticesPoints[j]);

                    canvas.drawText(String.valueOf((int) weight), midPoint.x, midPoint.y, weightPaint);


                }

            }

        }


        if (drawPath) {


            for (Shape shape : drawnPath) {

                if (shape instanceof Line) {

                    Line line = (Line) shape;
                    canvas.drawLine(line.getStart().x, line.getStart().y, line.getEnd().x, line.getEnd().y, selectedEdgePaint);

                    Point midPoint = MathUtils.midPoint(line.getStart(), line.getEnd());
                    canvas.drawText(String.valueOf(line.getLabel()), midPoint.x, midPoint.y, weightPaint);
                }
            }


        }


        // Drawing vertices
        for (int i = 0; i < verticesPoints.length; i++) {


            Rect vertexTextBounds = new Rect();

            vertexTextPaint.getTextBounds(String.valueOf(graph.vertixOfIndex(i)), 0, 1, vertexTextBounds);


            canvas.drawCircle(verticesPoints[i].x, verticesPoints[i].y, VERTEX_RADIUS, vertexPaint);
            canvas.drawText(String.valueOf(graph.vertixOfIndex(i)), verticesPoints[i].x, verticesPoints[i].y + vertexTextBounds.height() / 2, vertexTextPaint);


        }

        if (drawPath) {

            for (Shape shape : drawnPath) {

                if (shape instanceof Circle) {

                    Circle circle = (Circle) shape;

                    Rect vertexTextBounds = new Rect();

                    vertexTextPaint.getTextBounds(String.valueOf(circle.getLabel()), 0, 1, vertexTextBounds);


                    canvas.drawCircle(circle.getCenter().x, circle.getCenter().y, circle.getRadius(), pathVertexPaint);
                    canvas.drawCircle(circle.getCenter().x, circle.getCenter().y, VERTEX_RADIUS, vertexPaint);

                    canvas.drawText(String.valueOf(circle.getLabel()), circle.getCenter().x, circle.getCenter().y + vertexTextBounds.height() / 2, vertexTextPaint);
                }

            }

        }


    }


    public void updateGraph(Graph graph) {
        this.graph = graph;
        this.numberOfVertices = graph.getNumberOfVertices();
        drawPath = false;
        updateSize();
        generateDrawingData();
        invalidate();
    }

    private void updateSize() {

        int newWidth = (int) ((double) numberOfVertices / AppConstants.DEFAULT_NUMBER_OF_VERTICES * DEFAULT_WIDTH * screenDensity);
        int newHeight = (int) ((double) numberOfVertices / AppConstants.DEFAULT_NUMBER_OF_VERTICES * DEFAULT_HEIGHT * screenDensity);

        newWidth = Math.max(newWidth, minWidth);
        newHeight = Math.max(newHeight, minHeight);

        ViewGroup.LayoutParams params = getLayoutParams();

        params.height = newHeight;
        params.width = newWidth;

        setLayoutParams(params);


        viewSize.x = newWidth;
        viewSize.y = newHeight;


    }


    private void generateDrawingData() {
        if (graph == null)
            return;

        verticesPoints = new Point[graph.getNumberOfVertices()];

        Random random = new Random();

        int i = 0;

        int x, y;
        Point point;

        while (i < verticesPoints.length) {


            x = VERTEX_RADIUS + random.nextInt(viewSize.x - 3 * VERTEX_RADIUS);
            y = VERTEX_RADIUS + random.nextInt(viewSize.y - 2 * VERTEX_RADIUS - (int) (100 * screenDensity));
            point = new Point(x, y);

            boolean validPoint = true;
            for (int j = 0; j < i; j++) {
                if (verticesIntersect(verticesPoints[j], point)) {
                    validPoint = false;
                    break;
                }
            }

            if (validPoint) {
                verticesPoints[i] = point;
                i++;
            }

        }


    }


    public boolean verticesIntersect(Point v1, Point v2) {
        Rect r1 = new Rect();
        r1.left = v1.x - VERTEX_RADIUS - (int) (50 * screenDensity);
        r1.right = v1.x + VERTEX_RADIUS + (int) (50 * screenDensity);
        r1.top = v1.y - VERTEX_RADIUS - (int) (50 * screenDensity);
        r1.bottom = v1.y + VERTEX_RADIUS + (int) (50 * screenDensity);

        Rect r2 = new Rect();
        r2.left = v2.x - VERTEX_RADIUS;
        r2.right = v2.x + VERTEX_RADIUS;
        r2.top = v2.y - VERTEX_RADIUS;
        r2.bottom = v2.y + VERTEX_RADIUS;

        return r1.intersect(r2);
    }

    public void drawPathTo(int vertexName) {

        drawPath = true;
        pathToDraw.clear();
        drawnPath.clear();
        destinationIndex = graph.indexOfVertix(vertexName);
        fillPathStack();
        invalidate();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!pathToDraw.empty()) {
                    drawnPath.add(pathToDraw.pop());

                    invalidate();

                    if (!pathToDraw.empty())
                        handler.postDelayed(this, 500);

                }
            }
        }, 750);

    }

    public void fillPathStack() {


        Vertex[] vertices = graph.getVertices();

        Vertex temp = vertices[destinationIndex];


        while (temp.getPredecessor() != -1) {

            int index = graph.indexOfVertix(temp.getName());
            int predIndex = graph.indexOfVertix(temp.getPredecessor());

            Circle circle = new Circle(verticesPoints[index], VERTEX_RADIUS + PATH_VERTEX_BORDER, temp.getName());

            pathToDraw.push(circle);

            double weight = graph.getWeight(index, predIndex);

            Line line = new Line(verticesPoints[index], verticesPoints[predIndex], (int) weight);

            pathToDraw.push(line);

            temp = vertices[graph.indexOfVertix(temp.getPredecessor())];
        }

        Circle circle = new Circle(verticesPoints[graph.indexOfVertix(temp.getName())], VERTEX_RADIUS + PATH_VERTEX_BORDER, temp.getName());
        pathToDraw.push(circle);


    }


}
