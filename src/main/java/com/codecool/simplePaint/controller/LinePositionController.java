package com.codecool.simplePaint.controller;

import com.codecool.simplePaint.model.Position;

public class LinePositionController {


    public Position relativePositionConverter (Position position, Integer oldPanelWidth, Integer oldPanelHeight, Integer newPanelWidth, Integer newPanelHeight) {
            double relativeWidth = (double)position.x / (double)oldPanelWidth;
            double relativeHeight = (double) position.y / (double)oldPanelHeight;
            int updatedX =(int) Math.round(newPanelWidth * relativeWidth);
            int updatedY = (int) Math.round(newPanelHeight * relativeHeight);

        return new Position (updatedX, updatedY, newPanelWidth, newPanelHeight);
    }
}
