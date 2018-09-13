package com.codecool.paintFx.controller;

import com.codecool.paintFx.model.MyShape;
import com.codecool.paintFx.model.StraightLine;
import com.codecool.simplePaint.model.Line;
import com.codecool.paintFx.model.Position;

import java.util.List;

public class LinePositionController {

    public Position PositionSnapper (double xToSnap, double yToSnap, List<MyShape> shapeList, int rangeToSnap) {
        double cordToSnapX = xToSnap;
        double cordToSnapY = yToSnap;
        for (MyShape myShape: shapeList) {
            if(myShape.getClass() == StraightLine.class) {
                StraightLine straightLine = (StraightLine)myShape;
                double startPointX = straightLine.getStartX();
                double startPointY = straightLine.getStartY();
                double endPointX = straightLine.getEndX();
                double endPointY = straightLine.getEndY();
                if ((cordToSnapX <= startPointX + rangeToSnap && cordToSnapX >= startPointX - rangeToSnap) &&
                        (cordToSnapY <= startPointY + rangeToSnap && cordToSnapY >= startPointY - rangeToSnap)) {
                    xToSnap = startPointX;
                    yToSnap = startPointY;
                    return new Position(xToSnap, yToSnap);
                } else if ((cordToSnapX <= endPointX + rangeToSnap && cordToSnapX >= endPointX - rangeToSnap) &&
                        (cordToSnapY <= endPointY + rangeToSnap && cordToSnapY >= endPointY - rangeToSnap)) {
                    xToSnap = endPointX;
                    yToSnap = endPointY;
                    return new Position(xToSnap, yToSnap);
                }
            }
        }
        return new Position(xToSnap, yToSnap);
    }
}
