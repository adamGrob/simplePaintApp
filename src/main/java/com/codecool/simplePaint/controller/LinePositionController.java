package com.codecool.simplePaint.controller;

import com.codecool.simplePaint.model.Line;
import com.codecool.simplePaint.model.Position;

import java.util.List;

public class LinePositionController {


    public Position relativePositionConverter (Position position, Integer oldPanelWidth, Integer oldPanelHeight, Integer newPanelWidth, Integer newPanelHeight) {
            double relativeWidth = (double)position.x / (double)oldPanelWidth;
            double relativeHeight = (double) position.y / (double)oldPanelHeight;
            int updatedX =(int) Math.round(newPanelWidth * relativeWidth);
            int updatedY = (int) Math.round(newPanelHeight * relativeHeight);
        return new Position (updatedX, updatedY, newPanelWidth, newPanelHeight);
    }

    public Position PositionSnapper (Position positionToSnap, List<Line> lineList, int rangeToSnap) {
        int cordToSnapX = positionToSnap.x;
        int cordToSnapY = positionToSnap.y;
        for (Line line: lineList) {
            int startPointX = line.startPoint.x;
            int startPointY = line.startPoint.y;
            int endPointX = line.endPoint.x;
            int endPointY = line.endPoint.y;
            if ((cordToSnapX <= startPointX + rangeToSnap && cordToSnapX >= startPointX - rangeToSnap) &&
                    (cordToSnapY <= startPointY + rangeToSnap && cordToSnapY >= startPointY - rangeToSnap)) {
                positionToSnap.x = startPointX;
                positionToSnap.y = startPointY;
                return positionToSnap;

            } else if ((cordToSnapX <= endPointX + rangeToSnap && cordToSnapX >= endPointX - rangeToSnap) &&
                    (cordToSnapY <= endPointY + rangeToSnap && cordToSnapY >= endPointY - rangeToSnap)) {
                positionToSnap.x = endPointX;
                positionToSnap.y = endPointY;
                return positionToSnap;
            }
        }
        return positionToSnap;
    }
}
