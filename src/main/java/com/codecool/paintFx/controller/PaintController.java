package com.codecool.paintFx.controller;
import com.codecool.paintFx.model.CustomLine;
import com.codecool.paintFx.model.MyShape;
import com.codecool.paintFx.model.Position;
import com.codecool.paintFx.model.StraightLine;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javax.imageio.ImageIO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PaintController {

    private double prevX, prevY;

    private double endX, endY;

    private double startX, startY;

    private List<MyShape> drawnShapeList = new ArrayList<>();

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextField brushSize;

    @FXML
    private CheckBox straightLineChecked;

    @FXML
    private Button undo;

    @FXML
    private CheckBox lineSnapper;

    private CustomLine customLine;

    private List<StraightLine> straightLineList;

    public void initialize() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.setOnMouseDragged(mouseEvent -> {

            if (straightLineChecked.isSelected()) {
                drawStraightLines(graphicsContext, mouseEvent);
            } else {
                drawSimpleArcs(graphicsContext, mouseEvent);
            }
        });

        canvas.setOnMousePressed(e-> {
            double size = Double.parseDouble(brushSize.getText());
            straightLineList = new ArrayList<>();
            prevX = e.getX() - size / 2;
            prevY = e.getY() - size / 2;
            startX = e.getX() - size / 2;
            startY = e.getY() - size / 2;
        });

        undo.setOnAction(e->{
            if(drawnShapeList.size() != 0) {
                drawnShapeList.remove(drawnShapeList.get(drawnShapeList.size()-1));
                redraw(drawnShapeList, graphicsContext);
            }
        });

        canvas.setOnMouseReleased(mouseReleaseEvent -> {
            double size = Double.parseDouble(brushSize.getText());
            if (straightLineChecked.isSelected()) {
                endX = mouseReleaseEvent.getX()- size / 2;
                endY = mouseReleaseEvent.getY()- size / 2;
                graphicsContext.setStroke(colorPicker.getValue());
                graphicsContext.setLineWidth(size);
                graphicsContext.setLineCap(StrokeLineCap.ROUND);
                if(lineSnapper.isSelected()) {
                    LinePositionController linePositionController = new LinePositionController();
                    Position position = linePositionController.PositionSnapper(endX, endY, drawnShapeList, 30);
                    endX = position.x;
                    endY = position.y;
                }
                graphicsContext.strokeLine(startX, startY, endX, endY);
                drawnShapeList.add(new StraightLine(startX, startY, endX, endY,colorPicker.getValue(), size));
            } else {
                customLine = new CustomLine(straightLineList);
                drawnShapeList.add(customLine);
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
        Paint color = colorPicker.getValue();
        graphicsContext.setStroke(color);
        graphicsContext.setLineWidth(size);
        graphicsContext.setLineCap(StrokeLineCap.ROUND);
        graphicsContext.strokeLine(prevX, prevY, currX, currY);
        straightLineList.add(new StraightLine(prevX, prevY, currX, currY,color, size));
        prevX = currX;
        prevY = currY;
    }

    private void drawStraightLines(GraphicsContext graphicsContext, MouseEvent mouseEvent) {
        double size = Double.parseDouble(brushSize.getText());
        double currX = mouseEvent.getX() - size / 2;
        double currY = mouseEvent.getY() - size / 2;
        if(lineSnapper.isSelected()) {
            LinePositionController linePositionController = new LinePositionController();
            Position startPosition  = linePositionController.PositionSnapper(startX, startY, drawnShapeList, 30);
            Position endPosition = linePositionController.PositionSnapper(currX, currY, drawnShapeList, 30);
            startX = startPosition.x;
            startY = startPosition.y;
            currX = endPosition.x;
            currY = endPosition.y;
        }
        redraw(drawnShapeList, graphicsContext);
        graphicsContext.setStroke(colorPicker.getValue());
        graphicsContext.setLineWidth(size);
        graphicsContext.setLineCap(StrokeLineCap.ROUND);
        graphicsContext.strokeLine(startX, startY, currX, currY);

    }

    private void redraw(List<MyShape> drawnShapeList, GraphicsContext graphicsContext) {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (MyShape myShape : drawnShapeList) {
            if (myShape.getClass() == StraightLine.class) {
                StraightLine currStraightLine = (StraightLine)myShape;
                graphicsContext.setStroke(currStraightLine.getColor());
                graphicsContext.setLineWidth(currStraightLine.getSize());
                graphicsContext.strokeLine(currStraightLine.getStartX(), currStraightLine.getStartY(), currStraightLine.getEndX(), currStraightLine.getEndY());
            } else if (myShape.getClass() == CustomLine.class) {
                CustomLine customLine = (CustomLine)myShape;
                List<StraightLine> straightLineList = customLine.getStraightLineList();
                for (StraightLine currStraightLine: straightLineList) {
                    graphicsContext.setStroke(currStraightLine.getColor());
                    graphicsContext.setLineWidth(currStraightLine.getSize());
                    graphicsContext.strokeLine(currStraightLine.getStartX(), currStraightLine.getStartY(), currStraightLine.getEndX(), currStraightLine.getEndY());
                }
            }

        }
    }
}
