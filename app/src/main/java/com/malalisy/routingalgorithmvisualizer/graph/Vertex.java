package com.malalisy.routingalgorithmvisualizer.graph;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mohammad
 */
public class Vertex {

    private int name;
    private double value;
    private int predecessor;

    public Vertex(int name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(int predecessor) {
        this.predecessor = predecessor;
    }

    public int getName() {
        return name;
    }


   


}
