package com.malalisy.routingalgorithmvisualizer.graph;

/**
 * Created by mohammad on 24/04/17.
 */


import java.util.Random;

public class Graph {


    private static final double NO_CONNECTION_VALUE = Double.POSITIVE_INFINITY;

    private Vertex[] vertices;
    private double adjMatrix[][];


    public Graph(int numberOfVertices) {
        adjMatrix = new double[numberOfVertices][numberOfVertices];
        vertices = new Vertex[numberOfVertices];

        initialize();

    }

    private void initialize() {
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = 0; j < adjMatrix.length; j++) {
                adjMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }
    }

    public static Graph generateRandomly(int numberOfVertices) {

        Random random = new Random();

        Graph graph = new Graph(numberOfVertices);

        double quarter = Math.ceil(0.25 * numberOfVertices);

        for (int i = 0; i < numberOfVertices; i++) {


            graph.vertices[i] = new Vertex(graph.vertixOfIndex(i));


            int j = graph.numberOfAdjacent(i);

            while (j < quarter) {

                int randIndex = random.nextInt(numberOfVertices);

                if (randIndex != i && graph.adjMatrix[i][randIndex] == Double.POSITIVE_INFINITY) { // Ensure there is no self loop and not already connected.

                    int weight = 1 + random.nextInt(25); // from 1 - 25

                    graph.adjMatrix[i][randIndex] = weight;
                    graph.adjMatrix[randIndex][i] = weight;

                    j++;
                }

            }

        }

        return graph;

    }

    public boolean isAdjacent(int firstIndex, int secondIndex) {

        return adjMatrix[firstIndex][secondIndex] != NO_CONNECTION_VALUE;

    }

    public int numberOfAdjacent(int vertexIndex) {
        int n = 0;


        for (int i = 0; i < adjMatrix.length; i++) {
            if (adjMatrix[vertexIndex][i] != NO_CONNECTION_VALUE)
                n++;
        }

        return n;
    }

    public double getWeight(int firstIndex, int secondIndex) {
        return adjMatrix[firstIndex][secondIndex];
    }

    public int indexOfVertix(int vertixName) {
        return vertixName - 1;
    }

    public int vertixOfIndex(int index) {
        return index + 1;
    }


    public int getNumberOfVertices() {
        return adjMatrix.length;
    }


    public double[][] getAdjMatrix() {
        return adjMatrix;
    }


    public Vertex[] getVertices() {
        return vertices;
    }


}
