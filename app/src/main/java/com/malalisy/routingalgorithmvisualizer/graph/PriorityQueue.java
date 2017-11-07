package com.malalisy.routingalgorithmvisualizer.graph;

import java.util.LinkedList;

/**
 * @author mohammad
 */
public class PriorityQueue {


    LinkedList<Vertex> vertices;

    private int size;

    public PriorityQueue() {
        vertices = new LinkedList<>();
    }

    public void enqueue(Vertex v) {

        vertices.addLast(v);
        size++;
    }

    public Vertex dequeue() {
        if (vertices.size() > 0) {
            Vertex v = vertices.getFirst();
            vertices.removeFirst();
            size--;
            return v;
        } else
            return null;
    }

    public Vertex extractMax() {
        Vertex max = vertices.getFirst();
        for (Vertex v :
                vertices) {
            if (v.getValue() > max.getValue())
                max = v;
        }

        vertices.remove(max);

        return max;
    }

    public boolean contains(Vertex v) {
        return vertices.contains(v);

    }

    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    void remove(Vertex v) {
        vertices.remove(v);
    }

    int getSize() {
        return size;
    }


}
