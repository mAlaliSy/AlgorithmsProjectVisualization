package com.malalisy.routingalgorithmvisualizer.graph;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author mohammad
 */
public class RoutingAlgorithm {

    PriorityQueue q;
    Graph graph;

    public RoutingAlgorithm(Graph graph) {
        q = new PriorityQueue();
        this.graph = graph;
    }

    public void calculateMaxBW(int source) {

        Vertex[] vertices = graph.getVertices();

        // Initialize values to 0, for source itself INFITITY
        for (int i = 0; i < vertices.length; i++) {
            vertices[i].setValue(0);
            vertices[i].setPredecessor(-1);
        }
        vertices[graph.indexOfVertix(source)].setValue(Double.POSITIVE_INFINITY);

        // Initialize priority queue and fill it with the vertices
        for (int i = 0; i < vertices.length; i++) {
            q.enqueue(vertices[i]);
        }

        while (!q.isEmpty()) {

            Vertex max = q.extractMax();
            int maxIndex = graph.indexOfVertix(max.getName());

            double[][] adjM = graph.getAdjMatrix();

            for (int i = 0; i < adjM.length; i++) {

                Vertex v = vertices[i];

                if (adjM[maxIndex][i] != Double.POSITIVE_INFINITY ) {

                    double minBW = Math.min(adjM[maxIndex][i], max.getValue());
                    if (minBW > v.getValue()) {
                        v.setValue(minBW);
                        v.setPredecessor(max.getName());
                    }
                }
            }

        }

    }

}
