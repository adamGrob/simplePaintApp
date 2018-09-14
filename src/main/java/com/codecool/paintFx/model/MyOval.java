package com.codecool.paintFx.model;

import javafx.scene.paint.Paint;

public class MyOval extends MyShape{

    private Paint color;

    private double brushSize;

    private double x, y;

    private double width, height;

    public MyOval(double x, double y, double width, double height, Paint color, double brushSize) {
        this.color = color;
        this.brushSize = brushSize;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Paint getColor() {
        return color;
    }

    public void setColor(Paint color) {
        this.color = color;
    }

    public double getBrushSize() {
        return brushSize;
    }

    public void setBrushSize(double brushSize) {
        this.brushSize = brushSize;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
