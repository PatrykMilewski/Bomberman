package com.server.fields;

import com.server.Controllers.LogicController;

public class Fire extends Field {
    private long startTime;
    private Bonus fieldUnderFireField;
    //    private Player ownerOfBomb;                   //TODO jesli bedziemy robic punktacje

    public Fire(int x, int y, boolean destroyable) {
        super(x, y, destroyable);
        this.empty = true;
        this.startTime = System.currentTimeMillis();
        this.name = "Fire";
        this.fieldUnderFireField = null;
    }

    public long getStartTime() {
        return startTime;
    }

    public Bonus getFieldUnderFireField(){
        return fieldUnderFireField;
    }

    public void setUnderField(Bonus field) {
        this.fieldUnderFireField = field;
    }


}
