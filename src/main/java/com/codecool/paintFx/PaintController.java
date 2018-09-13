package com.codecool.paintFx;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

import javax.imageio.ImageIO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PaintController {

    private double prevX, prevY;

    private double endX, endY;

    private double startX, startY;

    private List<CustomLine> customLineList = new ArrayList<>();

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextField brushSize;

    @FXML
    private CheckBox eraser;

    @FXML
    private CheckBox straightLine;

    public void initialize() {
        double size = Double.parseDouble(brushSize.getText());
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(mouseEvent -> {
            if (straightLine.isSelected()) {
                drawStraightLines(graphicsContext, mouseEvent);
            } else {
                drawSimpleArcs(graphicsContext, mouseEvent);
            }
        });

        canvas.setOnMousePressed(e-> {
            prevX = e.getX() - size / 2;
            prevY = e.getY() - size / 2;
            startX = e.getX() - size / 2;
            startY = e.getY() - size / 2;


        });

        canvas.setOnMouseReleased(mouseReleaseEvent -> {
            if (straightLine.isSelected()) {
                endX = mouseReleaseEvent.getX()- size / 2;
                endY = mouseReleaseEvent.getY()- size / 2;
                graphicsContext.setStroke(colorPicker.getValue());
                graphicsContext.setLineWidth(size);
                graphicsContext.setLineCap(StrokeLineCap.ROUND);
                graphicsContext.strokeLine(startX, startY, endX, endY);
                customLineList.add(new CustomLine(startX, startY, endX, endY,colorPicker.getValue(), size));
            }

        });
    }

    public void onSave() {
        try {
            Image snapshot = canvas.snapshot(null, null);

            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("paint.png"));
        } catch (Exception e) {
            System.out.println("Failed to save image!");
        }
    }

    public void onExit() {
        Platform.exit();
    }

    private void drawSimpleArcs(GraphicsContext graphicsContext, MouseEvent mouseEvent) {
        double size = Double.parseDouble(brushSize.getText());
        // we offset mouseposition by half of the size
        double currX = mouseEvent.getX() - size / 2;
        double currY = mouseEvent.getY() - size / 2;
        if (eraser.isSelected()) {
            graphicsContext.clearRect(currX, currY, size, size);
        } else  {
            Paint color = colorPicker.getValue();
            graphicsContext.setStroke(color);
            graphicsContext.setLineWidth(size);
            graphicsContext.setLineCap(StrokeLineCap.ROUND);
            graphicsContext.strokeLine(prevX, prevY, currX, currY);
            customLineList.add(new CustomLine(prevX, prevY, currX, currY,color, size));
            prevX = currX;
            prevY = currY;
        }
    }

    private void drawStraightLines(GraphicsContext graphicsContext, MouseEvent mouseEvent) {

        double size = Double.parseDouble(brushSize.getText());
        double currX = mouseEvent.getX() - size / 2;
        double currY = mouseEvent.getY() - size / 2;

        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawLines(customLineList, graphicsContext);

        graphicsContext.setStroke(colorPicker.getValue());
        graphicsContext.setLineWidth(size);
        graphicsContext.setLineCap(StrokeLineCap.ROUND);
        graphicsContext.strokeLine(startX, startY, currX, currY);

    }

    private void drawLines(List<CustomLine> customLineList, GraphicsContext graphicsContext) {
        for (CustomLine customLine: customLineList) {
            graphicsContext.setStroke(customLine.getColor());
            graphicsContext.setLineWidth(customLine.getSize());
            graphicsContext.strokeLine(customLine.getStartX(), customLine.getStartY(), customLine.getEndX(), customLine.getEndY());
        }
    }
}
