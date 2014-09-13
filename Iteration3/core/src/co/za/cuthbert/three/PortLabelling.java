package co.za.cuthbert.three;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public enum PortLabelling {
    TOP_LEFT (0),
    TOP_CENTRE (1),
    TOP_RIGHT (2),
    MID_LEFT (3),
    MID_RIGHT (4),
    BOTTOM_LEFT (5),
    BOTTOM_CENTRE (6),
    BOTTOM_RIGHT (7);

    public final int index;
    PortLabelling(int index){
        this.index=index;
    }
}
