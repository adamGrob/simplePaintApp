package com.codecool.paintFx.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class MyOval extends MyShape{

    private double width, height;

    public MyOval(double x, double y, double width, double height, Paint color, double brushSize) {
        this.color = color;
        this.brushSize = brushSize;
        this.startX = x;
        this.startY = y;
        this.width = width;
        this.height = height;
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

    @Override
    public void display(GraphicsContext graphicsContext) {
        graphicsContext.strokeOval(startX, startY, width, height);
    }
}
