package com.codecool.paintFx.model;

import java.util.List;

public class CustomLine extends MyShape {

    private List<StraightLine> straightLineList;

    public CustomLine(List<StraightLine> straightLineList) {
        this.straightLineList = straightLineList;
    }

    public List<StraightLine> getStraightLineList() {
        return straightLineList;
    }

    public void setStraightLineList(List<StraightLine> straightLineList) {
        this.straightLineList = straightLineList;
    }
}
