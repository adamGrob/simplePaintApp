package com.codecool.simplePaint.model;

public class Position {

    public int panelWidthAtCreation;
    public int paneHeightAtCreation;

    public int x, y;

    public Position(int x, int y, int panelWidthAtCreation, int paneHeightAtCreation) {
        this.panelWidthAtCreation = panelWidthAtCreation;
        this.paneHeightAtCreation = paneHeightAtCreation;
        this.x = x;
        this.y = y;
    }
}
